package td.redis.sentinel.client.component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {

	private JedisPool pool;
	private String host;
	private int port;
	private String name;

	/*
	 * After Jedis-2.3.0,the parameter named maxActive & maxWait is unuseful.
	 */
	public JedisFactory(String host, int port, int maxActive, int maxIdle, long maxWait, boolean testOnBorrow, boolean testOnReturn, int timeout) {
		this.setHost(host);
		this.setPort(port);
		this.setName("welcome");
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);
		pool = new JedisPool(config, host, port, timeout);
	}

	public JedisFactory(String host, int port) {
		reboot(host, port);
	}

	public void reboot(String host, int port) {
		this.setHost(host);
		this.setPort(port);
		destory();
		pool = new JedisPool(host, port);
	}
	/*
	 * After Jedis-2.3.0,the parameter named maxActive & maxWait is unuseful.
	 */
	public void reboot(String host, int port, int maxActive, int maxIdle, long maxWait, boolean testOnBorrow, boolean testOnReturn, int timeout) {
		this.setHost(host);
		this.setPort(port);
		this.setName("welcome");
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);
		destory();
		pool = new JedisPool(config, host, port, timeout);
	}

	public boolean isHealth() {
		boolean health = false;
		Jedis redis = null;
		try {
			redis = getJedis();
			health = true;
		} catch (Exception e) {
			if (redis != null)
				try {
					returnBrokenJedis(redis);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			redis = null;
		} finally {
			if (redis != null)
				returnJedis(redis);
			redis = null;
		}
		return health;
	}

	public void destory() {
		if (pool == null)
			return;
		pool.destroy();
		pool = null;
	}

	public Jedis getJedis() {
		return pool.getResource();
	}

	public void returnJedis(Jedis j) {
		pool.returnResource(j);
	}

	public void returnBrokenJedis(Jedis j) {
		pool.returnBrokenResource(j);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
