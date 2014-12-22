package td.redis.sentinel.client;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanResult;

public class ClearRedisKey {
	public static void main(String[] args) {
		Jedis j = new Jedis("10.10.0.53", 6380, 50000);
		// Jedis j = new Jedis("127.0.0.1", 6380);
		String index = "0";
		ScanResult<String> rs = j.scan(index);
		// System.out.println(rs.getStringCursor());
		String next = null;
		int seconds = 12;
		int num = 0;
		List<String> keys = null;
		while (next == null | !(next = rs.getStringCursor()).equals(index)) {
			keys = rs.getResult();
			System.out.println("get " + keys.size() + " keys from " + next);
			Pipeline p = j.pipelined();
			for (String string : keys) {
				p.expire(string, seconds);
				// System.out.println(string);
				num++;
			}
			p.sync();
			if (next != null)
				rs = j.scan(next);
		}
		System.out.println("clear keys number : " + num);
	}
}
