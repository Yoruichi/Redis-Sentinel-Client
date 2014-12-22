package td.redis.sentinel.client.operation.pipeline;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.component.Pipelined;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationPipelined extends RedisOperation{

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) new Pipelined(redis);
	}

}
