package td.redis.sentinel.client.operation.set;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationSrem extends RedisOperation {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * td.redis.sentinel.client.operation.RedisOperation#operator(redis.clients
	 * .jedis.Jedis, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.srem((byte[]) args[0], (byte[][]) args[1]);
	}
}