package org.rondobell.racailum.base.redis;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.tv.cachecloud.client.basic.util.ConstUtils;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;
import com.sohu.tv.cachecloud.client.basic.util.StringUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

public class MyRedisStandaloneBuilder {
	private Logger logger = LoggerFactory.getLogger(com.sohu.tv.builder.RedisStandaloneBuilder.class);
	private static final Lock LOCK = new ReentrantLock();
	private volatile JedisPool jedisPool;
	private GenericObjectPoolConfig poolConfig;
	private final long appId;
	private final String password;
	private int timeout = 2000;

	MyRedisStandaloneBuilder(long appId) {
		this.appId = appId;
		this.password = null;
		this.poolConfig = new GenericObjectPoolConfig();
		this.poolConfig.setMaxTotal(24);
		this.poolConfig.setMaxIdle(16);
		this.poolConfig.setMinIdle(0);
		this.poolConfig.setJmxEnabled(true);
		this.poolConfig.setJmxNamePrefix("jedis-pool");
	}

	MyRedisStandaloneBuilder(long appId, String password) {
		this.appId = appId;
		this.password = password;
		this.poolConfig = new GenericObjectPoolConfig();
		this.poolConfig.setMaxTotal(24);
		this.poolConfig.setMaxIdle(16);
		this.poolConfig.setMinIdle(0);
		this.poolConfig.setJmxEnabled(true);
		this.poolConfig.setJmxNamePrefix("jedis-pool");
	}

	public JedisPool build() {
		if (this.jedisPool != null) {
			return this.jedisPool;
		} else {
			while(true) {
				while(true) {
					while(true) {
						while(true) {
							try {
								LOCK.tryLock(100L, TimeUnit.MILLISECONDS);
								if (this.jedisPool == null) {
									String response = HttpUtils.doGet(String.format(ConstUtils.REDIS_STANDALONE_URL, this.appId));
									if (response == null || response.isEmpty()) {
										this.logger.warn("cannot get response from server, appId={}. continue...", this.appId);
									} else {
										ObjectMapper mapper = new ObjectMapper();
										JsonNode responseJson = null;

										try {
											responseJson = mapper.readTree(response);
										} catch (Exception var6) {
											this.logger.error("read json from response error, appId: {}.", this.appId, var6);
										}

										if (responseJson != null) {
											String instance = responseJson.get("standalone").asText();
											String[] instanceArr = instance.split(":");
											if (instanceArr.length == 2) {
												if (StringUtil.isBlank(this.password)) {
													this.jedisPool = new JedisPool(this.poolConfig, instanceArr[0], Integer.valueOf(instanceArr[1]), this.timeout);
												} else {
													this.jedisPool = new JedisPool(this.poolConfig, this.password, instanceArr[0], Integer.valueOf(instanceArr[1]), this.timeout);
												}

												return this.jedisPool;
											}

											this.logger.warn("instance info is invalid, instance: {}, appId: {}, continue...", instance, this.appId);
										} else {
											this.logger.warn("invalid response, appId: {}. continue...", this.appId);
										}
									}
								}
							} catch (InterruptedException var7) {
								this.logger.error("error in build().", var7);
							}
						}
					}
				}
			}
		}
	}

	public MyRedisStandaloneBuilder setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
		return this;
	}

	public MyRedisStandaloneBuilder setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
}
