package org.rondobell.racailum.base.redis;

import com.sohu.tv.builder.ClientBuilder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisUCloud {

	private JedisPool jedisPool;

	private Map<Class<?>, Schema<?>> cachedSchema = null;
	private ConcurrentHashMap<Class<?>, SpinStatus> raceUtil = null;

	public static void main(String[] args) {
		//String key = "key_wechat_ticket";
		String key = "ytgrv1:sl";
		RedisUCloud redis = new RedisUCloud();
		//redis.set("v1:top:album657", "1100000000078,1100002118072,1100000000012,1100002151369,1100002151292");
		System.out.println(redis.get(key));
		System.out.println(redis.set(key, args[0]));
		System.out.println(redis.get(key));

		//System.out.println(redis.get("token3163573"));
	}

	public Long hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hset(key, field, value);
		} finally {
			release(jedis);
		}
	}

	public String hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.hget(key, field);
		} finally {
			release(jedis);
		}
	}

	public String info() {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.info();
		} finally {
			release(jedis);
		}
	}


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

	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.set(key, value);
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

	public Long del(String key) {
		Jedis jedis = getJedis();
		Long value = null;
		if (jedis != null) {
			value = jedis.del(key);
			release(jedis);
		}
		return value;
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
			//System.out.println("long time query: "+(end-start));
		}
	}

	public void ScanKeys(String prefix){
		Jedis jedis = null;
		try {
			jedis = getJedis();

			ScanParams params = new ScanParams();
			params.match(prefix);
			params.count(50);
			ScanResult<String> result = jedis.scan("0", params);
			String cursor = "";
			boolean finished = false;
			int count = 1;
			while (!finished) {
				List<String> list = result.getResult();
				if (list == null || list.isEmpty()) {
					finished = true;
				}

				for (String s : list) {
					System.out.println(count + ") " + s);
					count++;
				}

				cursor = result.getCursor();
				if (cursor.equalsIgnoreCase("0")) {
					finished = true;
				}
				result = jedis.scan(cursor, params);

			}

			System.out.println(count);

		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(jedis);
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
