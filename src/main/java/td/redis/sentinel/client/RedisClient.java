package td.redis.sentinel.client;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.SafeEncoder;
import td.redis.sentinel.client.component.Command;
import td.redis.sentinel.client.component.JedisFactory;
import td.redis.sentinel.client.component.Pipelined;
import td.redis.sentinel.client.component.Sentinel;

import java.util.*;
import java.util.logging.Logger;

public class RedisClient {
    /**
     * @author yoruichi
     */

    private JedisFactory factory;
    private Sentinel sentinel;

    private static final Object lock = new Object();

    private int retryTimes;
    private int maxActive;
    private int maxIdle;
    private long maxWait;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private int timeout;

    private Logger log = Logger.getLogger(getClass().getName());

    public RedisClient(Sentinel sentinel, int retryTimeout) {
        this(sentinel, retryTimeout, 30, 3, 1000, true, true, 1000);
    }

    public RedisClient(Sentinel sentinel) {
        this(sentinel, 0, 30, 3, 1000, true, true, 1000);
    }

    public RedisClient(Sentinel sentinel, int retryTimeout, int maxActive, int maxIdle, long maxWait, boolean testOnBorrow, boolean testOnReturn, int timeout) {
        this.retryTimes = retryTimeout;
        this.maxActive = maxActive;
        this.maxIdle = maxIdle;
        this.maxWait = maxWait;
        this.testOnBorrow = testOnBorrow;
        this.testOnReturn = testOnReturn;
        this.sentinel = sentinel;
        refresh();
    }

    /**
     * Refresh the master information.If the sentinel(cluster) server has
     * problem, throw a RunntimeException<br>
     * When it be called under multi-thread mode, it allows only one user to
     * process the code in synchronized field.<br>
     * What it does in the synchronized field is necessary.it can create the
     * factory which provide Jedis(connection of redis server) or reboot the
     * factory<br>
     */
    public void refresh() {
        if (!sentinel.isHealth())
            throw new JedisConnectionException("Sentinel servers has been down.");
        String _info = sentinel.getMasterInfo();
        if (_info == null) {
            log.warning("Sentinel get master failed.");
            throw new JedisConnectionException("Sentinel servers has been down.");
        }
        String[] info = _info.split(":");
        String name = sentinel.masterName;
        String masterHost = info[0];
        String sMasterPort = info[1];
        int masterPort = sMasterPort != null ? Integer.valueOf(sMasterPort) : 6379;
        synchronized (lock) {
            if (factory == null) {
                factory = new JedisFactory(masterHost, masterPort, maxActive, maxIdle, maxWait, testOnBorrow, testOnReturn, timeout);
                factory.setName(name);
            }
            if (!factory.isHealth()) {
                factory.destory();
                factory.reboot(masterHost, masterPort, maxActive, maxIdle, maxWait, testOnBorrow, testOnReturn, timeout);
                if (factory.isHealth()) {
                    log.info(Thread.currentThread().getName() + " " + name + " now @ " + masterHost + ":" + masterPort);
                } else {
                    log.warning(Thread.currentThread().getName() + " Could not get a resource from the pool at [" + masterHost + ":" + masterPort + "] named [" + name
                            + "].Try to get master info after 5s later");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * Do command with operator which implements RedisOperation.<br>
     * Note that,the Jedis instance which is got from the factory is only used
     * in current thread and current method called it.<br>
     * The operator which implements RedisOperation has initialized in Command
     * at system initialization<br>
     * And,if an exception has been thrown when operator processes the command,
     * it will call refresh method every second until command do completed or
     * retry time out.
     *
     * @param command
     * @param args
     * @return T
     */
    public <T> T doAction(Command command, Object... args) {
        int times = -1;
        T res = null;
        Jedis redis = null;
        while (times < retryTimes) {
            try {
                if (redis == null)
                    redis = factory.getJedis();
                res = Command.operations.get(command).operator(redis, args);
                break;
            } catch (JedisConnectionException e) {
                try {
                    if (redis != null)
                        factory.returnBrokenJedis(redis);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                redis = null;
                log.warning("There is something wrong when " + command.name());
                if (retryTimes > 0)
                    times++;
                if (times >= retryTimes) {
                    throw new JedisConnectionException(e);
                } else {
                    refresh();
                }
            }
        }
        if (redis != null) {
            if (!command.equals(Command.PIPELINE))
                factory.returnJedis(redis);
        }
        redis = null;
        return res;
    }

    // pipeline operation

    /**
     * Get pipelined
     *
     * @return a pipelined instance
     */
    public Pipelined pipelined() {
        return doAction(Command.PIPELINE);
    }

    /**
     * Process the commands in the parameter pipelined synchronized,and return
     * the connection of this pipelined to factory
     *
     * @param pipelined
     */
    public void sync(Pipelined pipelined) {
        pipelined.superSync();
        factory.returnJedis(pipelined.getConnection());
    }

    /**
     * Process the commands in the parameter pipelined synchronized,and return
     * the connection of this pipelined to factory.Beside,this method could
     * return all the responses in the order as a list.
     *
     * @param pipelined
     * @return A list of all the responses in the order you executed them.
     */
    public List<Object> syncAndReturnAll(Pipelined pipelined) {
        List<Object> list = pipelined.superSyncAndReturnAll();
        factory.returnJedis(pipelined.getConnection());
        return list;
    }

    // string operation
    public Long incr(final String key) {
        return incr(SafeEncoder.encode(key));
    }

    public Long incr(final byte[] key) {
        return doAction(Command.INCR, key);
    }

    public Long del(final String... keys) {
        return del(SafeEncoder.encodeMany(keys));
    }

    public Long del(final byte[][] keys) {
        return doAction(Command.DEL, new Object[]{keys});
    }

    public List<String> mget(final String[] keys) {
        List<byte[]> l = mget(SafeEncoder.encodeMany(keys));
        if (l == null)
            return null;
        final ArrayList<String> result = new ArrayList<String>(l.size());
        for (final byte[] barray : l) {
            if (barray == null) {
                result.add(null);
            } else {
                result.add(SafeEncoder.encode(barray));
            }
        }
        return result;
    }

    public List<byte[]> mget(final byte[][] keys) {
        return doAction(Command.MGET, new Object[]{keys});
    }

    public String set(final byte[] key, byte[] value) {
        return doAction(Command.SET, key, value);
    }

    public String set(final String key, String value) {
        return doAction(Command.SET, SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    public String get(final String key) {
        byte[] b = get(SafeEncoder.encode(key));
        if (b == null)
            return null;
        return SafeEncoder.encode(b);
    }

    public byte[] get(final byte[] key) {
        return doAction(Command.GET, key);
    }

    // key operation
    public Boolean exists(final byte[] key) {
        return doAction(Command.EXISTS, key);
    }

    public Boolean exists(final String key) {
        return doAction(Command.EXISTS, SafeEncoder.encode(key));
    }

    public Long expire(final String key, Integer seconds) {
        return doAction(Command.EXPIRE, SafeEncoder.encode(key), seconds);
    }

    public Long expire(final byte[] key, Integer seconds) {
        return doAction(Command.EXPIRE, key, seconds);
    }

    public Long expireAt(final byte[] key, Long times) {
        return doAction(Command.EXPIRE, key, times);
    }

    public Long expireAt(final String key, Long times) {
        return doAction(Command.EXPIRE, SafeEncoder.encode(key), times);
    }

    public Long ttl(final String key) {
        return doAction(Command.TTL, SafeEncoder.encode(key));
    }

    public Long ttl(final byte[] key) {
        return doAction(Command.TTL, key);
    }

    // set operation
    public Long srem(final byte[] key, byte[]... members) {
        return doAction(Command.SREM, key, members);
    }

    public Long srem(final String key, String... members) {
        return srem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    public Set<byte[]> smembers(final byte[] key) {
        return doAction(Command.SMEMBERS, key);
    }

    public Set<String> smembers(final String key) {
        Set<byte[]> s = smembers(SafeEncoder.encode(key));
        Set<String> result = new HashSet<String>();
        if (s == null)
            return null;
        for (byte[] bs : s) {
            result.add(SafeEncoder.encode(bs));
        }
        return result;
    }

    public Long sadd(String key, String... member) {
        return doAction(Command.SADD, SafeEncoder.encode(key), SafeEncoder.encodeMany(member));
    }

    public Long sadd(byte[] key, byte[]... member) {
        return doAction(Command.SADD, key, member);
    }

    public Boolean sismember(final byte[] key, byte[] member) {
        return doAction(Command.SISMEMBER, key, member);
    }

    public Boolean sismember(final String key, String member) {
        return doAction(Command.SISMEMBER, SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    public Long scard(final byte[] key) {
        return doAction(Command.SCARD, key);
    }

    public Long scard(final String key) {
        return doAction(Command.SCARD, SafeEncoder.encode(key));
    }

    public byte[] spop(final byte[] key) {
        return doAction(Command.SPOP, key);
    }

    public String spop(final String key) {
        byte[] b = spop(SafeEncoder.encode(key));
        if (b == null)
            return null;
        return SafeEncoder.encode(b);
    }

    // list operation
    public Long lrem(final byte[] key, Long count, byte[] value) {
        return doAction(Command.LREM, key, count, value);
    }

    public Long lrem(final String key, Long count, String value) {
        return doAction(Command.LREM, SafeEncoder.encode(key), count, SafeEncoder.encode(value));
    }

    public Long lpush(final String key, String... member) {
        return doAction(Command.LPUSH, SafeEncoder.encode(key), SafeEncoder.encodeMany(member));
    }

    public Long lpush(final byte[] key, byte[]... member) {
        return doAction(Command.LPUSH, key, member);
    }

    public String lpop(final String key) {
        byte[] b = lpop(SafeEncoder.encode(key));
        if (b == null)
            return null;
        return SafeEncoder.encode(b);
    }

    public byte[] lpop(final byte[] key) {
        return doAction(Command.LPOP, key);
    }

    public Long llen(final String key) {
        return doAction(Command.LLEN, SafeEncoder.encode(key));
    }

    public Long llen(final byte[] key) {
        return doAction(Command.LLEN, key);
    }

    public List<String> lrange(final String key, Long start, Long end) {
        List<byte[]> list = lrange(SafeEncoder.encode(key), start, end);
        if (list == null)
            return null;
        List<String> resp = new ArrayList<String>();
        for (byte[] b : list) {
            resp.add(SafeEncoder.encode(b));
        }
        return resp;
    }

    public List<byte[]> lrange(final byte[] key, Long start, Long end) {
        return doAction(Command.LRANGE, key, start, end);
    }

    public String rpop(final String key) {
        byte[] b = rpop(SafeEncoder.encode(key));
        if (b == null)
            return null;
        return SafeEncoder.encode(b);
    }

    public byte[] rpop(final byte[] key) {
        return doAction(Command.RPOP, key);
    }

    // hash operation
    public Long hdel(final String key, String... fields) {
        return doAction(Command.HDEL, SafeEncoder.encode(key), SafeEncoder.encodeMany(fields));
    }

    public Long hdel(final byte[] key, byte[]... fields) {
        return doAction(Command.HDEL, key, fields);
    }

    public Map<byte[], byte[]> hgetAll(final byte[] key) {
        return doAction(Command.HGETALL, key);
    }

    public Map<String, String> hgetAll(final String key) {
        Map<byte[], byte[]> m = hgetAll(SafeEncoder.encode(key));
        if (m == null)
            return null;
        Map<String, String> result = new HashMap<String, String>();
        for (byte[] bm : m.keySet()) {
            result.put(SafeEncoder.encode(bm), SafeEncoder.encode(m.get(bm)));
        }
        return result;
    }

    public Long hset(final String key, String field, String value) {
        return doAction(Command.HSET, SafeEncoder.encode(key), SafeEncoder.encode(field), SafeEncoder.encode(value));
    }

    public Long hset(final byte[] key, byte[] field, byte[] value) {
        return doAction(Command.HSET, key, field, value);
    }

    public String hget(final String key, String field) {
        byte[] b = hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
        if (b == null)
            return null;
        return SafeEncoder.encode(b);
    }

    public byte[] hget(final byte[] key, byte[] field) {
        return doAction(Command.HGET, key, field);
    }

    public Long hlen(final String key) {
        return doAction(Command.HLEN, SafeEncoder.encode(key));
    }

    public Long hlen(final byte[] key) {
        return doAction(Command.HLEN, key);
    }

    public Long hincrby(final String key, String field, Long incr) {
        return doAction(Command.HINCRBY, SafeEncoder.encode(key), SafeEncoder.encode(field), incr);
    }

    public Long hincrby(final byte[] key, byte[] field, Long incr) {
        return doAction(Command.HINCRBY, key, field, incr);
    }

    public Set<String> hkeys(final String key) {
        Set<byte[]> set = hkeys(SafeEncoder.encode(key));
        if (set == null)
            return null;
        Set<String> resp = new HashSet<String>();
        for (byte[] b : set) {
            resp.add(SafeEncoder.encode(b));
        }
        return resp;
    }

    public Set<byte[]> hkeys(final byte[] key) {
        return doAction(Command.HKEYS, key);
    }

    public Long hsetnx(String key, String field, String value) {
        return hsetnx(SafeEncoder.encode(key), SafeEncoder.encode(field), SafeEncoder.encode(value));
    }

    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return doAction(Command.HSETNX, key, field, value);
    }

    // pub/sub operation
    public Long publish(String channel, String message) {
        return publish(channel.getBytes(), message.getBytes());
    }

    public Long publish(byte[] channel, byte[] message) {
        return doAction(Command.PUB, channel, message);
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        doAction(Command.SUB, jedisPubSub, channels);
    }

    public void psubscribe(JedisPubSub jedisPubSub, String... pattens) {
        doAction(Command.PSUB, jedisPubSub, pattens);
    }

    // lua script
    public Object eval(final String script, final List<String> keys, final List<String> args) {
        byte[] _script = SafeEncoder.encode(script);
        List<byte[]> _keys = new ArrayList<byte[]>();
        List<byte[]> _args = new ArrayList<byte[]>();
        for (String string : keys) {
            _keys.add(SafeEncoder.encode(string));
        }
        for (String string : args) {
            _args.add(SafeEncoder.encode(string));
        }
        return eval(_script, _keys, _args);
    }

    private Object eval(byte[] _script, List<byte[]> _keys, List<byte[]> _args) {
        return doAction(Command.EVAL, _script, _keys, _args);
    }

    // bitmap operation
    public Boolean setbit(final String key, final Long offset, Boolean value) {
        return setbit(SafeEncoder.encode(key), offset, value);
    }

    private Boolean setbit(final byte[] key, final Long offset, Boolean value) {
        return doAction(Command.SETBIT, new Object[]{key, offset, value});
    }

    public Long bitCount(final String key) {
        return bitCount(SafeEncoder.encode(key));
    }

    private Long bitCount(final byte[] key) {
        return doAction(Command.BITCOUNT, key);
    }

    public Long bitAnd(final String targetKey, String... srcKeys) {
        final byte[][] srcKeysByteArray = SafeEncoder.encodeMany(srcKeys);
        final byte[] targetKeyByteArray = SafeEncoder.encode(targetKey);
        return bitAnd(targetKeyByteArray, srcKeysByteArray);
    }

    private Long bitAnd(final byte[] targetKey, byte[][] srcKeys) {
        return doAction(Command.BITOP, BitOP.AND, targetKey, srcKeys);
    }

    public Long bitOr(final String targetKey, String... srcKeys) {
        final byte[][] srcKeysByteArray = SafeEncoder.encodeMany(srcKeys);
        final byte[] targetKeyByteArray = SafeEncoder.encode(targetKey);
        return bitOr(targetKeyByteArray, srcKeysByteArray);
    }

    private Long bitOr(final byte[] targetKey, byte[][] srcKeys) {
        return doAction(Command.BITOP, BitOP.OR, targetKey, srcKeys);
    }

    public Long bitNot(final String targetKey, String srcKey) {
        return bitNot(SafeEncoder.encode(targetKey), SafeEncoder.encodeMany(srcKey));
    }

    private Long bitNot(final byte[] targetKey, byte[][] srcKey) {
        return doAction(Command.BITOP, BitOP.NOT, targetKey, srcKey);
    }
}
