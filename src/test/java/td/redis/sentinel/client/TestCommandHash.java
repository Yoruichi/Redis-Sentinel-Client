package td.redis.sentinel.client;

import td.redis.sentinel.client.component.Sentinel;


public class TestCommandHash {
	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("appcpa", "10.10.67.21:26379", "10.10.67.21:26380","10.10.67.21:26381");
		RedisClient r = new RedisClient(sentinel);

		final String key = "test_hash";
		final String field = "007";
		final String fieldAnother = "008";
		final String member = "66d8cff8-6c5d-4001-9651-a2129d06dee6";
		final String memberAnother = "1";

		System.out.println(r.hsetnx(key, field, member));
		System.out.println(r.hget(key, field));
		System.out.println(r.hlen(key));
		System.out.println(r.hset(key, fieldAnother, memberAnother));
		System.out.println(r.hincrby(key, fieldAnother, 1l));
		System.out.println(r.hget(key, fieldAnother));
		System.out.println(r.hkeys(key));
		System.out.println(r.hgetAll(key));
		System.out.println(r.hdel(key,field,fieldAnother));
		System.out.println(r.hgetAll(key));

		sentinel.shutdown();
	}
}
