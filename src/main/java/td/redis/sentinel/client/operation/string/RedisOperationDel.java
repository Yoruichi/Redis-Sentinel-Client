package td.redis.sentinel.client.operation.string;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationDel extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.del((byte[][]) args[0]);
	}

}
