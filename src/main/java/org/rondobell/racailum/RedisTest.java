package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class RedisTest {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();
        System.out.println(redis.info());
    }
}
