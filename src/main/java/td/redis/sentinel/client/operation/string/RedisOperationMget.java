package td.redis.sentinel.client.operation.string;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationMget extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.mget((byte[][]) args[0]);
	}

}