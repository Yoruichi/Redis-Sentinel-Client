package td.redis.sentinel.client;

import td.redis.sentinel.client.component.Sentinel;

public class TestCommandString {

	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("appcpa", "10.10.67.21:26379", "10.10.67.21:26380","10.10.67.21:26381");
		RedisClient r = new RedisClient(sentinel);
		
		final String key = "test1";
		final String value = "hello sentinel";
		
		System.out.println(r.set(key, value));
		System.out.println(r.exists(key+"1"));
		System.out.println(r.get(key+"1"));
		System.out.println(r.expire(key, 100));
		System.out.println(r.ttl(key));
		String[] testkeys = new String[285];
		for(int i=0; i<testkeys.length; i++) {
			testkeys[i] = "test"+i;
		}
		System.out.println(r.mget(testkeys));
		
		System.out.println(r.mget(new String[]{"test0","test2"}));
		System.out.println(r.exists(key));
		r.set(key+"1", value);
		System.out.println(r.del(key,key+"1"));
		System.out.println(r.exists(key));
		
		sentinel.shutdown();
	}
	
}
