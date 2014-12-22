package td.redis.sentinel.client.operation.script;

import java.util.List;

import redis.clients.jedis.Jedis;
import td.redis.sentinel.client.operation.RedisOperation;

public class RedisOperationEval extends RedisOperation {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T operator(Jedis redis, Object... args) {
		return (T) redis.eval((byte[]) args[0], (List<byte[]>)args[1], (List<byte[]>)args[2]);
	}

}
