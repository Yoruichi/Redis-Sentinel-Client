package td.redis.sentinel.client.operation.string;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationIncr extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.incr((byte[]) args[0]);
	}

}
