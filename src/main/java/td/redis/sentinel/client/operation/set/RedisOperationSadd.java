package td.redis.sentinel.client.operation.set;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationSadd extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.sadd((byte[]) args[0], (byte[][]) args[1]);
	}
}
