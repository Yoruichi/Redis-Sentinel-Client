package td.redis.sentinel.client.operation.list;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationLrange extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.lrange((byte[]) args[0], (Long) args[1],
				(Long) args[2]);
	}
}
