package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class AppleAudit {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();

        String version = "5.4.0";
        System.out.println(redis.hget("switch", "0appstore1"+version));

        // 审核
        //redis.hset("switch", "0appstore1"+version, "{\"id\":\"0appstore1"+version+"\",\"switchs\":{\"shareReport\":true,\"idfaAD\":true}}");
        // 过审
        redis.hset("switch", "0appstore1"+version, "{\"id\":\"0appstore1"+version+"\",\"switchs\":{\"shareReport\":true}}");

        System.out.println(redis.hget("switch", "0appstore1"+version));
    }
}
