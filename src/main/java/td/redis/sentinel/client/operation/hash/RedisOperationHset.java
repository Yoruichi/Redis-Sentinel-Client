package td.redis.sentinel.client.operation.hash;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationHset extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.hset((byte[]) args[0], (byte[]) args[1],
				(byte[]) args[2]);
	}
}
