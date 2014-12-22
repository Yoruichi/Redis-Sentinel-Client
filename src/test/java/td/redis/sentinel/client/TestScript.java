package td.redis.sentinel.client;

import java.util.ArrayList;
import java.util.List;

import td.redis.sentinel.client.component.Sentinel;

public class TestScript {
	public static void main(String[] args) {
		Sentinel sentinel = new Sentinel("appcpa", "localhost:26379", "localhost:26380", "localhost:26381");
		RedisClient r = new RedisClient(sentinel);
		String script = "if redis.call('ttl', KEYS[1])<0 then redis.call('expire', KEYS[1], ARGV[1]) end return redis.call('ttl', KEYS[1])";
		String key = "test_incr";
		String expireTime = "100";
		List<String> _keys = new ArrayList<String>();
		List<String> _args = new ArrayList<String>();
		_keys.add(key);
		_args.add(expireTime);
		
		System.out.println(r.incr(key));
		System.out.println(r.eval(script, _keys, _args));
		System.out.println(r.get(key));
		System.out.println(r.ttl(key));
		sentinel.shutdown();
	}
}
