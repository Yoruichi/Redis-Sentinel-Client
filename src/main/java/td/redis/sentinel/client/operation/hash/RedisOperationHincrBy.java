package td.redis.sentinel.client.operation.hash;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationHincrBy extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.hincrBy((byte[]) args[0], (byte[]) args[1],
				(Long) args[2]);
	}
}
