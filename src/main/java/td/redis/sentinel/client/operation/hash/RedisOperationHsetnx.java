package td.redis.sentinel.client.operation.hash;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationHsetnx extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.hsetnx((byte[]) args[0], (byte[]) args[1],
				(byte[]) args[2]);
	}

}
