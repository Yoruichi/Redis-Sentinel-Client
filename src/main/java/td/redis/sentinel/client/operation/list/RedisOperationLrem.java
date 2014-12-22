package td.redis.sentinel.client.operation.list;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationLrem extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.lrem((byte[])args[0], (Long)args[1], (byte[])args[2]);
	}

}
