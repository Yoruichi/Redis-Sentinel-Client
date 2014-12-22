package td.redis.sentinel.client;

import redis.clients.jedis.Jedis;

public class TestTwemProxy {
	public static void main(String[] args) {
		Jedis redis = new Jedis("127.0.0.1", 22121);
//		redis.set("test_proxy", "hello proxy");
		String message = redis.get("test_proxy");
//		System.out.println("done");
		System.out.println(message);
	}
}
