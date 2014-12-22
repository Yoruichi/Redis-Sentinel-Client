package td.redis.sentinel.client;

import java.util.Arrays;
import java.util.HashSet;

import td.redis.sentinel.client.component.Sentinel;
import td.redis.sentinel.client.component.SentinelPool;

public class TestSentinelPool {

	public static void main(String[] args) {
		SentinelPool p = new SentinelPool("mymaster", new HashSet<String>(
				Arrays.asList(new String[] { "10.10.3.200:26380",
						"10.10.3.200:26381", "10.10.3.201:26380",
						"10.10.3.201:26381", "10.10.3.201:26382" })));
		Sentinel sentinel = new Sentinel("mymaster", "10.10.3.200:26380",
				"10.10.3.200:26381", "10.10.3.201:26380", "10.10.3.201:26381",
				"10.10.3.201:26382");
		RedisClient client = new RedisClient(sentinel);
		while (true) {
//			System.out.println(p.getCurrentHostMaster());
			System.out.println(sentinel.getMasterInfo());
			System.out.println(client.get("test"));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
