package org.rondobell.racailum.base.redis;

import com.sohu.tv.builder.ClientBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUCloud {

	private JedisPool jedisPool;

	public RedisUCloud() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(20000);
		poolConfig.setMaxIdle(600);
		poolConfig.setMaxWaitMillis(10000);
		jedisPool = ClientBuilder.redisStandalone(10030, "udredis-lt4zg0sh")
				.setTimeout(1000)
				.setPoolConfig(poolConfig).build();
	}

	private void release(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	private Jedis getJedis() {
		return jedisPool.getResource();
	}

	public void set(String key, String value, int expire) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
			jedis.expire(key, expire);
		} finally {
			release(jedis);
		}
	}

	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} finally {
			release(jedis);
		}
	}

	public String get(String key) {
		Jedis jedis = getJedis();
		String value = null;
		if (jedis != null) {
			value = jedis.get(key);
			release(jedis);
		}
		return value;
	}

	public static void main(String[] args) {
		RedisUCloud redis = new RedisUCloud();
		//redis.set("kradio_group_qrcode_url", "http://img.kaolafm.net/kradio/kradio-wx-group.jpg");
		System.out.println(redis.get("kradio_group_qrcode_url"));
	}
}
