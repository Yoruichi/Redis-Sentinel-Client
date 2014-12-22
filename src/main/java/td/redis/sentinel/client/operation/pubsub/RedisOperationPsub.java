package td.redis.sentinel.client.operation.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationPsub extends RedisOperation {

	@Override
	public <T> T operator(Jedis redis, Object... args) {
		redis.psubscribe((JedisPubSub) args[0], (String[])args[1]);
		return null;
	}

}
