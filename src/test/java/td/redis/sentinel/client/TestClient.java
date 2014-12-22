package td.redis.sentinel.client;

import java.util.UUID;

import td.redis.sentinel.client.component.Sentinel;

public class TestClient {

	public static void main(String[] args) {
		final Sentinel sentinel = new Sentinel("appcpa", "10.10.0.50:26379");
		RedisClient redisOperation = new RedisClient(sentinel);
		String key = "1456106.37.188.194“wgw0132”的 iPhone02:00:00:00:00:00 8b17fbc611feb1a17339977f024480e9181e54b0inst";
		System.out.println(redisOperation.exists(key));
		sentinel.shutdown();
	}

}

class Foo extends Thread {
	public RedisClient redisOperation;

	public Foo(RedisClient redisOperation) {
		this.redisOperation = redisOperation;
	}

	@Override
	public void run() {
		String key = "test_set" + this.getName();

		long start = System.currentTimeMillis();

		int succ = 0;
		int fail = 0;
		int missing = 0;

		int max = 1000 * 1000;
		for (int i = 0; i < max; i++) {
			Long res = redisOperation.sadd(key, UUID.randomUUID().toString());
			if (res == null)
				missing++;
			else if (res.longValue() > 0) {
				succ++;
			} else {
				fail++;
			}
		}

		System.out.println(this.getName() + " sadd success [" + succ + "] and failed [" + fail
			+ "] and missing [" + missing + "] use time : " + (System.currentTimeMillis() - start) + " ms.");

	}

}
