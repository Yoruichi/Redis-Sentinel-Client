package td.redis.sentinel.client.operation.bitmap;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

/**
 * Created by apple on 14/12/3.
 */
public class RedisOperationBitcount extends RedisOperation {
    @Override
    public <T> T operator(Jedis redis, Object... args) {
        return (T) redis.bitcount((byte[])args[0]);
    }
}
