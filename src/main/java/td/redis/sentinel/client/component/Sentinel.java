package td.redis.sentinel.client.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Sentinel {
	/**
	 * @author yoruichi
	 */
	private SentinelPool pool;
	public String masterName;
	private Set<String> addresses;

	/**
	 * A constructor of sentinel.When the address are all not available,this
	 * method will be blocking.
	 * 
	 * @param masterName
	 * @param address
	 */
	public Sentinel(String masterName, String... address) {
		this.masterName = masterName;
		addresses = new HashSet<String>(Arrays.asList(address));
		pool = new SentinelPool(masterName, addresses);
	}

	/**
	 * If the unavailable sentinel server in the address given has been more
	 * than a half of the cluster,this method would return false.
	 * 
	 * @return
	 */
	public boolean isHealth() {
		boolean health = false;
		int brokenNum = 0;
		int limitNum = addresses.size() / 2 + addresses.size() % 2;
		for (String address : addresses) {
			try {
				String[] _address = address.split(":");
				String host = _address[0];
				String sport = _address[1];
				int port = Integer.parseInt(sport);
				Jedis j = new Jedis(host, port);
				j.ping();
			} catch (Exception e) {
				brokenNum++;
			}
		}
		if (brokenNum < limitNum)
			health = true;
		return health;
	}

	/**
	 * Get current master information.if something goes wrong,this would return
	 * null.
	 * 
	 * @return
	 */
	public String getMasterInfo() {
		Object o = null;
		try {
			o = pool.getCurrentHostMaster();
			if (o != null)
				return o.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void shutdown() {
		pool.destroy();
	}
}
