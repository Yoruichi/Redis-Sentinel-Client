package td.redis.sentinel.client;

import td.redis.sentinel.client.component.Sentinel;

public class TestClientLog {
	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("mymaster", "10.10.3.200:26380",
				"10.10.3.200:26381", "10.10.3.201:26380", "10.10.3.201:26381",
				"10.10.3.201:26382");
		RedisClient client = new RedisClient(sentinel, 0);
		while (true) {
//			client.refresh();
			System.out.println(sentinel.getMasterInfo());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
