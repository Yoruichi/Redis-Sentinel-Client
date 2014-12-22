package td.redis.sentinel.client.component;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import td.redis.sentinel.client.exception.PipelinedException;

public class Pipelined extends Pipeline {

	private Jedis connection;

	public Pipelined(Jedis connection) {
		setConnection(connection);
		setClient(connection.getClient());
	}

	public Jedis getConnection() {
		return connection;
	}

	public void setConnection(Jedis connection) {
		this.connection = connection;
	}

	@Override
	public void sync() {
		throw new PipelinedException("It's not aollowed to execute pipeline by this method.Please run method sync() with RedisClient");
	}

	@Override
	public List<Object> syncAndReturnAll() {
		throw new PipelinedException("It's not aollowed to execute pipeline by this method.Please run method syncAndReturnAll() with RedisClient");
	}

	public void superSync() {
		super.sync();
	}

	public List<Object> superSyncAndReturnAll() {
		return super.syncAndReturnAll();
	}
}
