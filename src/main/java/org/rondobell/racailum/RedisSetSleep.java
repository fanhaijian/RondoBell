package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class RedisSetSleep {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();

        if (args.length>0) {
            redis.set("ytgrv1:sl",args[0]);
            System.out.println("set "+args[0]);
        }else{
            redis.set("ytgrv1:sl","120000");
            System.out.println("set 120000");

        }
    }
}
