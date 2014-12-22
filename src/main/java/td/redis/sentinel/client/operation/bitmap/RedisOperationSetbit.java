package td.redis.sentinel.client.operation.bitmap;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

/**
 * Created by apple on 14/12/3.
 */
public class RedisOperationSetbit extends RedisOperation {
    @Override
    public <T> T operator(Jedis redis, Object... args) {
        return (T) redis.setbit((byte[])args[0],(Long)args[1],(Boolean)args[2]);
    }
}
