package td.redis.sentinel.client;

import java.util.UUID;

import td.redis.sentinel.client.component.Sentinel;

public class TestCommandList {
	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("appcpa", "10.10.67.21:26379", "10.10.67.21:26380","10.10.67.21:26381");
		RedisClient r = new RedisClient(sentinel);

		final String key = "test_list";
		final String member = "66d8cff8-6c5d-4001-9651-a2129d06dee6";
		final String memberAnother = UUID.randomUUID().toString();

		System.out.println(r.lpush(key, member, memberAnother));
		System.out.println(r.lpush(key, member));
		System.out.println(r.lrange(key, 0l, -1l));
		System.out.println(r.llen(key));
		System.out.println(r.lrem(key, 2l, member));
		System.out.println(r.lpop(key));
		System.out.println(r.rpop(key));
		System.out.println(r.llen(key));
		System.out.println(r.lrange(key, 0l, -1l));

		sentinel.shutdown();
	}
}
