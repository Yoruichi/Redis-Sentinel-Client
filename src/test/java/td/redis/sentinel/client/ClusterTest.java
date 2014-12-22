package td.redis.sentinel.client;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class ClusterTest {
	public static void main(String[] args) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		// Jedis Cluster will attempt to discover cluster nodes automatically
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7000));
		JedisCluster jc = new JedisCluster(jedisClusterNodes,1,1000);

		int maxThread = 1;
		for (int threadIndex = 0; threadIndex < maxThread; threadIndex++) {
			new Thread(new T(jc)).start();
		}

		// int all = 1000000;
		// int failed = 0;
		// long start = System.currentTimeMillis();
		// for (int i = 0; i < all; i++) {
		// try {
		// jc.set("key_" + i, "value_" + i);
		// } catch (Exception e) {
		// System.out.println(String.format("set %s:%s failed.", "key_" + i,
		// "value_" + i));
		// failed++;
		// // e.printStackTrace();
		// }
		// }
		// System.out.println(String.format("write %s message success.%s message failed.usetime %s ms.",
		// (all - failed), failed, (System.currentTimeMillis() - start)));
	}
}

class T implements Runnable {
	private JedisCluster jc;
	public static int CURRENT_NUMBER = 0;
	public static String CONSISTENCY_KEY = "consistency_key";
	public static int READ_TIMES = 0;
	public static int WRITE_TIMES = 0;
	public static int READ_ERROR = 0;
	public static int WRITE_ERROR = 0;
	public int currentNumberFromRedis = 0;

	public T(JedisCluster jc) {
		this.jc = jc;
	}

	@Override
	public void run() {
		while (true) {
			try {
				currentNumberFromRedis = Integer.valueOf(jc.get(CONSISTENCY_KEY));
				READ_TIMES++;
			} catch (Exception e) {
				READ_ERROR++;
			}
			try {
				jc.incr(CONSISTENCY_KEY);
				WRITE_TIMES++;
			} catch (Exception e) {
				WRITE_ERROR++;
			}
			System.out.println(String.format("%s [R %d (%d err)| W %d (%d err) | Number_In_Redis--%d | Number_In_Memery--%d]", Thread.currentThread().getName(), READ_TIMES, READ_ERROR, WRITE_TIMES,
				WRITE_ERROR, currentNumberFromRedis, CURRENT_NUMBER));
			CURRENT_NUMBER++;
		}
	}
}