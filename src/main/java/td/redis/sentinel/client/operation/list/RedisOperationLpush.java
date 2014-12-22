package td.redis.sentinel.client.operation.list;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationLpush extends RedisOperation {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.lpush((byte[]) args[0], (byte[][]) args[1]);
	}
}
