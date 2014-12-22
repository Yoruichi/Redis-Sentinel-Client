package td.redis.sentinel.client.operation.string;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationSet extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.set((byte[]) args[0], (byte[]) args[1]);
	}

}
