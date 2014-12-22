package td.redis.sentinel.client.operation.pubsub;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationPub extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.publish((byte[])args[0], (byte[])args[1]);
	}

}
