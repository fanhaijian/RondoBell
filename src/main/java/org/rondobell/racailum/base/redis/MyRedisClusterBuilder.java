package org.rondobell.racailum.base.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.tv.cachecloud.client.basic.heartbeat.ClientStatusEnum;
import com.sohu.tv.cachecloud.client.basic.heartbeat.HeartbeatInfo;
import com.sohu.tv.cachecloud.client.basic.util.ConstUtils;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.PipelineCluster;

public class MyRedisClusterBuilder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final long appId;
	private final String password;
	private GenericObjectPoolConfig jedisPoolConfig;
	private JedisCluster jedisCluster;
	private int connectionTimeout = 2000;
	private int soTimeout = 2000;
	private int maxRedirections = 5;
	private final Lock lock = new ReentrantLock();

	MyRedisClusterBuilder(long appId, String password) {
		this.appId = appId;
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(40);
		poolConfig.setMaxIdle(16);
		poolConfig.setMinIdle(8);
		poolConfig.setMaxWaitMillis(1000L);
		poolConfig.setJmxNamePrefix("jedis-pool");
		poolConfig.setJmxEnabled(true);
		this.jedisPoolConfig = poolConfig;
		this.password = password;
	}

	public JedisCluster build() {
		if (this.jedisCluster != null) {
			return this.jedisCluster;
		} else {
			while(true) {
				while(true) {
					try {
						this.lock.tryLock(10L, TimeUnit.SECONDS);
						if (this.jedisCluster != null) {
							JedisCluster var24 = this.jedisCluster;
							return var24;
						}

						String url = String.format(ConstUtils.REDIS_CLUSTER_URL, String.valueOf(this.appId));
						String response = HttpUtils.doGet(url);
						ObjectMapper objectMapper = new ObjectMapper();
						HeartbeatInfo heartbeatInfo = null;

						try {
							heartbeatInfo = (HeartbeatInfo)objectMapper.readValue(response, HeartbeatInfo.class);
						} catch (IOException var20) {
							this.logger.error("remote build error, appId: {}", this.appId, var20);
						}

						if (heartbeatInfo != null) {
							if (heartbeatInfo.getStatus() == ClientStatusEnum.ERROR.getStatus()) {
								throw new IllegalStateException(heartbeatInfo.getMessage());
							}

							if (heartbeatInfo.getStatus() == ClientStatusEnum.WARN.getStatus()) {
								this.logger.warn(heartbeatInfo.getMessage());
							} else {
								this.logger.info(heartbeatInfo.getMessage());
							}

							Set<HostAndPort> nodeList = new HashSet();
							String nodeInfo = heartbeatInfo.getShardInfo();
							nodeInfo = nodeInfo.replace(" ", ",");
							String[] nodeArray = nodeInfo.split(",");
							String[] var8 = nodeArray;
							int var9 = nodeArray.length;

							for(int var10 = 0; var10 < var9; ++var10) {
								String node = var8[var10];
								String[] ipAndPort = node.split(":");
								if (ipAndPort.length >= 2) {
									String ip = ipAndPort[0];
									int port = Integer.parseInt(ipAndPort[1]);
									nodeList.add(new HostAndPort(ip, port));
								}
							}

							this.jedisCluster = new PipelineCluster(this.jedisPoolConfig, nodeList, this.connectionTimeout, this.maxRedirections, this.password);
							JedisCluster var25 = this.jedisCluster;
							return var25;
						}
					} catch (Throwable var22) {
						this.logger.error(var22.getMessage(), var22);
						break;
					} finally {
						this.lock.unlock();
					}
				}

				try {
					TimeUnit.MILLISECONDS.sleep((long)(200 + (new Random()).nextInt(1000)));
				} catch (InterruptedException var21) {
					this.logger.error(var21.getMessage(), var21);
				}
			}
		}
	}

	public MyRedisClusterBuilder setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
		return this;
	}

	public MyRedisClusterBuilder setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public MyRedisClusterBuilder setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
		return this;
	}

	public MyRedisClusterBuilder setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
		return this;
	}
}
