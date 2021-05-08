package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class RedisTest {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();
        String key = "kradio_group_qrcode_url";
        redis.set(key, "http://img.kaolafm.net/lALPD4BhtR4l7HTNBQDNBQA_1280_1280.png");
        System.out.println(redis.get(key));
    }
}
