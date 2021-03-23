package org.rondobell.racailum.base.redis;

import com.sohu.tv.builder.ClientBuilder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisUCloud {

	private JedisPool jedisPool;

	private Map<Class<?>, Schema<?>> cachedSchema = null;
	private ConcurrentHashMap<Class<?>, SpinStatus> raceUtil = null;

	private static class SpinStatus {
		volatile boolean released;
	}

	public RedisUCloud() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(20000);
		poolConfig.setMaxIdle(600);
		poolConfig.setMaxWaitMillis(10000);
		jedisPool = ClientBuilder.redisStandalone(10030, "udredis-lt4zg0sh")
				.setTimeout(1000)
				.setPoolConfig(poolConfig).build();

		cachedSchema = new ConcurrentHashMap<>();
		raceUtil = new ConcurrentHashMap<>();
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

	public <T> void setSerializeProtoStuff(String key, T t, int expire) {
		long start = System.currentTimeMillis();
		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) getSchema(t.getClass());
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		byte[] bytes;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			bytes = ProtostuffIOUtil.toByteArray(t, schema, buffer);
			byte[] kdata = key.getBytes();
			jedis.set(kdata, bytes);
			if (expire != 0) {
				jedis.expire(kdata, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			buffer.clear();
			release(jedis);
		}
		long end = System.currentTimeMillis();
		if ((end - start) > 200) {
			System.out.println(end-start);
		}
	}

	private <T> Schema<T> getSchema(Class<T> cls) {
		Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
		if (schema == null) {

			SpinStatus spinStatus = new SpinStatus();
			SpinStatus oldSpinStatus = raceUtil.putIfAbsent(cls, spinStatus);

			if (oldSpinStatus == null) {

				schema = RuntimeSchema.getSchema(cls);
				if (schema != null) {
					cachedSchema.put(cls, schema);
				}

				spinStatus.released = true;
			} else {
				while (!oldSpinStatus.released) {
				}
				;
			}

			schema = (Schema<T>) cachedSchema.get(cls);
		}
		return schema;
	}
}
