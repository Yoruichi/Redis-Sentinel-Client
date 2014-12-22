package td.redis.sentinel.client;

import java.util.UUID;

import redis.clients.jedis.JedisPubSub;
import td.redis.sentinel.client.component.Sentinel;

public class TestPubSub {

	public static void main(String[] args) {
		final Sentinel sentinel = new Sentinel("mymaster01", "localhost:26380",
				"localhost:26390");
		RedisClient client = new RedisClient(sentinel);
		for (int i = 0; i < 3; i++) {
			new P(client, "channel_" + i).start();
		}
//		for (int i = 0; i < 3; i++) {
//			new S(client, "channel_" + i).start();
//		}
		new PS(client, "channel_*").start();
	}

}

class P extends Thread {
	private RedisClient client;
	private String channel;

	public P(RedisClient client, String channel) {
		this.client = client;
		this.channel = channel;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String message = UUID.randomUUID().toString()
						+ " from sender : " + getName();
				client.publish(channel, message);
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class PS extends Thread {
	private RedisClient client;
	private String channel;

	public PS(RedisClient client, String channel) {
		this.client = client;
		this.channel = channel;
	}

	@Override
	public void run() {
		JedisPubSub jedisPubSub = new JedisPubSub() {

			@Override
			public void onMessage(String channel, String message) {
			}

			@Override
			public void onPMessage(String pattern, String channel,
					String message) {
				System.out.println(getName() + " receive message from "
						+ channel + " and message \n[" + message + "].");
			}

			@Override
			public void onSubscribe(String channel, int subscribedChannels) {
			}

			@Override
			public void onUnsubscribe(String channel, int subscribedChannels) {
			}

			@Override
			public void onPUnsubscribe(String pattern, int subscribedChannels) {
			}

			@Override
			public void onPSubscribe(String pattern, int subscribedChannels) {
				System.out.println("start psubscribe channel pattern " + pattern);
			}
		};
		client.psubscribe(jedisPubSub, channel);
	}
}

class S extends Thread {
	private RedisClient client;
	private String channel;

	public S(RedisClient client, String channel) {
		this.client = client;
		this.channel = channel;
	}

	@Override
	public void run() {
		JedisPubSub jedisPubSub = new JedisPubSub() {

			@Override
			public void onMessage(String channel, String message) {
				System.out.println(getName() + " receive message from "
						+ channel + " and message \n[" + message + "].");
			}

			@Override
			public void onPMessage(String pattern, String channel,
					String message) {
			}

			@Override
			public void onSubscribe(String channel, int subscribedChannels) {
				System.out.println("start subscribe channel " + channel);
			}

			@Override
			public void onUnsubscribe(String channel, int subscribedChannels) {
			}

			@Override
			public void onPUnsubscribe(String pattern, int subscribedChannels) {
			}

			@Override
			public void onPSubscribe(String pattern, int subscribedChannels) {
			}
		};
		client.subscribe(jedisPubSub, channel);
	}
}