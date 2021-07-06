package org.rondobell.racailum;

import org.rondobell.racailum.base.redis.RedisUCloud;

public class RedisDelPlayList {
    public static void main(String[] args) {
        RedisUCloud redis = new RedisUCloud();

        redis.ScanKeys("AI_RADIO_USER_PLAYLIST_120*");
    }
}
