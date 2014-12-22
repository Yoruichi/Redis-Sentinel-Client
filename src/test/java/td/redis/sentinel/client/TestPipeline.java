package td.redis.sentinel.client;

import java.util.List;
import java.util.UUID;

import td.redis.sentinel.client.component.Pipelined;
import td.redis.sentinel.client.component.Sentinel;

public class TestPipeline {

	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("mymaster", "10.10.3.200:26380",
				"10.10.3.200:26381", "10.10.3.201:26380", "10.10.3.201:26381",
				"10.10.3.201:26382");
		RedisClient redisOperation = new RedisClient(sentinel);
		for (int i = 0; i < 5; i++) {
			new Too(redisOperation).start();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		sentinel.shutdown();
	}

}

class Too extends Thread {
	public RedisClient redisOperation;

	public Too(RedisClient redisOperation) {
		this.redisOperation = redisOperation;
	}

	@Override
	public void run() {
		String key = "test_set" + this.getName();

		long start = System.currentTimeMillis();

		int succ = 0;
		int fail = 0;
		int missing = 0;

		int max = 5 * 1000;
		Pipelined p = redisOperation.pipelined();
		for (int i = 0; i < max; i++) {
			p.sadd(key, UUID.randomUUID().toString());
		}
		List<Object> list = redisOperation.syncAndReturnAll(p);
		for (Object res : list)
			if (res == null)
				missing++;
			else if (((Long) res).longValue() > 0) {
				succ++;
			} else {
				fail++;
			}

		System.out.println(this.getName() + " sadd success [" + succ
				+ "] and failed [" + fail + "] and missing [" + missing
				+ "] use time : " + (System.currentTimeMillis() - start)
				+ " ms.");

	}

}