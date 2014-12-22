package td.redis.sentinel.client;

import redis.clients.jedis.Jedis;

public class TestJedis {
	public static void main(String[] args) {
		Jedis j = new Jedis("127.0.0.1", 6380);
	}
}
