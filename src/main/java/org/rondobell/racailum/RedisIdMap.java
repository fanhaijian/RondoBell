package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class RedisIdMap {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();
        //String key = "kradio_group_qrcode_url";
        String key = "carOldIdMapping";

        redis.hset(key,"2117", "4396");
        redis.hset(key,"2144", "4396");
        redis.hset(key,"2139", "4396");
        //搞笑
        redis.hset(key,"2086", "3820");
        redis.hset(key,"2090", "3820");
        redis.hset(key,"2088", "3820");
        //小说
        redis.hset(key,"2109", "3822");
        redis.hset(key,"11150", "3822");
        redis.hset(key,"11151", "3822");
        redis.hset(key,"11136", "3822");
        redis.hset(key,"2134", "3822");
        redis.hset(key,"11138", "3822");
        redis.hset(key,"11139", "3822");
        redis.hset(key,"2129", "3822");
        redis.hset(key,"11135", "3822");
        redis.hset(key,"11137", "3822");
        redis.hset(key,"2126", "3822");
        redis.hset(key,"2121", "3822");
        redis.hset(key,"2114", "3822");
        //科技财经
        redis.hset(key,"2143", "3819");
        //文史
        redis.hset(key,"11153", "3821");
        redis.hset(key,"11154", "3821");
        redis.hset(key,"11155", "3821");
        //汽车
        redis.hset(key,"2084", "3825");
        //亲子
        redis.hset(key,"11158", "4397");
        redis.hset(key,"11159", "4397");
        redis.hset(key,"11160", "4397");
        //生活
        redis.hset(key,"2182", "3824");
        //教育
        redis.hset(key,"2112", "3823");
        //健康
        redis.hset(key,"2130", "3824");
    }
}
