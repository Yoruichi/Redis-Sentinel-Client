package td.redis.sentinel.client.operation;

import redis.clients.jedis.Jedis;

public abstract class RedisOperation {
	/**
	 * Do something with the Jedis instance given by a parameter
	 * 
	 * @param redis
	 * @param args
	 * @return
	 */
	public abstract <T> T operator(Jedis redis, Object... args);

}
