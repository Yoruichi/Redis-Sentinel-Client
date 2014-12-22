package td.redis.sentinel.client;

import java.util.UUID;

import td.redis.sentinel.client.component.Sentinel;

public class TestCommandSet {

	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("appcpa", "127.0.0.1:26380",
			"127.0.0.1:26381", "127.0.0.1:26382");
		RedisClient r = new RedisClient(sentinel);
		
		final String key = "test_set";
		final String member = "66d8cff8-6c5d-4001-9651-a2129d06dee6";
		
		final String memberAnother = UUID.randomUUID().toString();
		
		System.out.println(r.sadd(key, member, memberAnother));
		System.out.println(r.sismember(key, member));
		System.out.println(r.scard(key));
		System.out.println(r.spop(key));
		System.out.println(r.srem(key,member));
		System.out.println(r.smembers(key));
		
		sentinel.shutdown();
	}
	
}
