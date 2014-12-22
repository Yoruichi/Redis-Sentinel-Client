package td.redis.sentinel.client.operation.key;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationTTL extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.ttl((byte[]) args[0]);
	}
}
