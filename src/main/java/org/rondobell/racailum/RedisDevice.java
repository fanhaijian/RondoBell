package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class RedisDevice {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();
        //String key = "kradio_group_qrcode_url";
        String key = "smart_device_appids";

        //redis.set(key, "http://img.kaolafm.net/lALPD4BhtR4l7HTNBQDNBQA_1280_1280.png");
        System.out.println(redis.get(key));
        if(args.length>0){
            System.out.println(redis.set(key, args[0]));
        }
        if(args.length>1){
            System.out.println(redis.del(key));
        }
    }
}
