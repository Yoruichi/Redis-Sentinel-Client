package td.redis.sentinel.client.component;

import java.util.HashMap;
import java.util.Map;

import td.redis.sentinel.client.operation.RedisOperation;
import td.redis.sentinel.client.operation.bitmap.RedisOperationBitcount;
import td.redis.sentinel.client.operation.bitmap.RedisOperationBitop;
import td.redis.sentinel.client.operation.bitmap.RedisOperationSetbit;
import td.redis.sentinel.client.operation.hash.RedisOperationHdel;
import td.redis.sentinel.client.operation.hash.RedisOperationHget;
import td.redis.sentinel.client.operation.hash.RedisOperationHgetall;
import td.redis.sentinel.client.operation.hash.RedisOperationHincrBy;
import td.redis.sentinel.client.operation.hash.RedisOperationHkeys;
import td.redis.sentinel.client.operation.hash.RedisOperationHlen;
import td.redis.sentinel.client.operation.hash.RedisOperationHset;
import td.redis.sentinel.client.operation.hash.RedisOperationHsetnx;
import td.redis.sentinel.client.operation.key.RedisOperationExists;
import td.redis.sentinel.client.operation.key.RedisOperationExpire;
import td.redis.sentinel.client.operation.key.RedisOperationExpireAt;
import td.redis.sentinel.client.operation.key.RedisOperationTTL;
import td.redis.sentinel.client.operation.list.RedisOperationLlen;
import td.redis.sentinel.client.operation.list.RedisOperationLpop;
import td.redis.sentinel.client.operation.list.RedisOperationLpush;
import td.redis.sentinel.client.operation.list.RedisOperationLrange;
import td.redis.sentinel.client.operation.list.RedisOperationLrem;
import td.redis.sentinel.client.operation.list.RedisOperationRpop;
import td.redis.sentinel.client.operation.pipeline.RedisOperationPipelined;
import td.redis.sentinel.client.operation.pubsub.RedisOperationPsub;
import td.redis.sentinel.client.operation.pubsub.RedisOperationPub;
import td.redis.sentinel.client.operation.pubsub.RedisOperationSub;
import td.redis.sentinel.client.operation.script.RedisOperationEval;
import td.redis.sentinel.client.operation.set.RedisOperationSadd;
import td.redis.sentinel.client.operation.set.RedisOperationScard;
import td.redis.sentinel.client.operation.set.RedisOperationSismember;
import td.redis.sentinel.client.operation.set.RedisOperationSmembers;
import td.redis.sentinel.client.operation.set.RedisOperationSpop;
import td.redis.sentinel.client.operation.set.RedisOperationSrem;
import td.redis.sentinel.client.operation.string.RedisOperationDel;
import td.redis.sentinel.client.operation.string.RedisOperationGet;
import td.redis.sentinel.client.operation.string.RedisOperationIncr;
import td.redis.sentinel.client.operation.string.RedisOperationMget;
import td.redis.sentinel.client.operation.string.RedisOperationSet;

public enum Command {
	SYNCANDRETURNALL, PSUB, PUB, SUB, PING, SET, GET, QUIT, EXISTS, DEL, TYPE, FLUSHDB, KEYS, RANDOMKEY, RENAME, RENAMENX, RENAMEX, DBSIZE, EXPIRE, EXPIREAT, TTL, SELECT, MOVE, FLUSHALL, GETSET, MGET, SETNX, SETEX, MSET, MSETNX, DECRBY, DECR, INCRBY, INCR, APPEND, SUBSTR, HSET, HGET, HSETNX, HMSET, HMGET, HINCRBY, HEXISTS, HDEL, HLEN, HKEYS, HVALS, HGETALL, RPUSH, LPUSH, LLEN, LRANGE, LTRIM, LINDEX, LSET, LREM, LPOP, RPOP, RPOPLPUSH, SADD, SMEMBERS, SREM, SPOP, SMOVE, SCARD, SISMEMBER, SINTER, SINTERSTORE, SUNION, SUNIONSTORE, SDIFF, SDIFFSTORE, SRANDMEMBER, ZADD, ZRANGE, ZREM, ZINCRBY, ZRANK, ZREVRANK, ZREVRANGE, ZCARD, ZSCORE, MULTI, DISCARD, EXEC, WATCH, UNWATCH, SORT, BLPOP, BRPOP, AUTH, SUBSCRIBE, PUBLISH, UNSUBSCRIBE, PSUBSCRIBE, PUNSUBSCRIBE, ZCOUNT, ZRANGEBYSCORE, ZREVRANGEBYSCORE, ZREMRANGEBYRANK, ZREMRANGEBYSCORE, ZUNIONSTORE, ZINTERSTORE, SAVE, BGSAVE, BGREWRITEAOF, LASTSAVE, SHUTDOWN, INFO, MONITOR, SLAVEOF, CONFIG, STRLEN, SYNC, LPUSHX, PERSIST, RPUSHX, ECHO, LINSERT, DEBUG, BRPOPLPUSH, SETBIT, GETBIT, SETRANGE, GETRANGE, EVAL, EVALSHA, SCRIPT, SLOWLOG, OBJECT, BITCOUNT, BITOP, SENTINEL, DUMP, RESTORE, PEXPIRE, PEXPIREAT, PTTL, INCRBYFLOAT, PSETEX, CLIENT, TIME, MIGRATE, HINCRBYFLOAT, PIPELINE;

	public static Map<Command, RedisOperation> operations;
	static {
		operations = new HashMap<Command, RedisOperation>();
		operations.put(Command.BITCOUNT, new RedisOperationBitcount());
		operations.put(Command.BITOP, new RedisOperationBitop());
		operations.put(Command.SETBIT, new RedisOperationSetbit());
		operations.put(Command.EVAL, new RedisOperationEval());
		operations.put(Command.INCR, new RedisOperationIncr());
		operations.put(Command.HDEL, new RedisOperationHdel());
		operations.put(Command.DEL, new RedisOperationDel());
		operations.put(Command.SREM, new RedisOperationSrem());
		operations.put(Command.SMEMBERS, new RedisOperationSmembers());
		operations.put(Command.HGETALL, new RedisOperationHgetall());
		operations.put(Command.MGET, new RedisOperationMget());
		operations.put(Command.SUB, new RedisOperationSub());
		operations.put(Command.PUB, new RedisOperationPub());
		operations.put(Command.PSUB, new RedisOperationPsub());
		operations.put(Command.SET, new RedisOperationSet());
		operations.put(Command.GET, new RedisOperationGet());
		operations.put(Command.EXISTS, new RedisOperationExists());
		operations.put(Command.EXPIRE, new RedisOperationExpire());
		operations.put(Command.EXPIREAT, new RedisOperationExpireAt());
		operations.put(Command.TTL, new RedisOperationTTL());
		operations.put(Command.SADD, new RedisOperationSadd());
		operations.put(Command.SCARD, new RedisOperationScard());
		operations.put(Command.SISMEMBER, new RedisOperationSismember());
		operations.put(Command.SPOP, new RedisOperationSpop());
		operations.put(Command.LPUSH, new RedisOperationLpush());
		operations.put(Command.LREM, new RedisOperationLrem());
		operations.put(Command.LPOP, new RedisOperationLpop());
		operations.put(Command.RPOP, new RedisOperationRpop());
		operations.put(Command.LLEN, new RedisOperationLlen());
		operations.put(Command.LRANGE, new RedisOperationLrange());
		operations.put(Command.HSET, new RedisOperationHset());
		operations.put(Command.HGET, new RedisOperationHget());
		operations.put(Command.HINCRBY, new RedisOperationHincrBy());
		operations.put(Command.HLEN, new RedisOperationHlen());
		operations.put(Command.HKEYS, new RedisOperationHkeys());
		operations.put(Command.HSETNX, new RedisOperationHsetnx());
		operations.put(Command.PIPELINE, new RedisOperationPipelined());
	}
}
