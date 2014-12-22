package td.redis.sentinel.client.operation.key;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationExpireAt extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.expireAt((byte[]) args[0], (Long) args[1]);
	}
}
