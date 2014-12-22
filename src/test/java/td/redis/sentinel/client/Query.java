package td.redis.sentinel.client;

import redis.clients.jedis.Jedis;

public class Query {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Jedis j = new Jedis("localhost", 6379);
		String key = "hll";
		String key1 = "hll1";
		String key2 = "hll2";
		for (int i = 0; i < 100000; i++) {
			j.pfadd(key, i + "");
		}
		for (int i = 0; i < 50000; i++) {
			j.pfadd(key1, i + "");
		}
		for (int i = 100000; i < 200000; i++) {
			j.pfadd(key2, i + "");
		}
		System.out.println(j.pfcount(key));
		System.out.println(j.pfcount(key1));
		System.out.println(j.pfcount(key2));
		System.out.println(j.pfcount(key,key1,key2));
	}
}
