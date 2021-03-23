package org.rondobell.racailum;

import org.rondobell.racailum.base.dto.OAuth2Authorization;
import org.rondobell.racailum.base.dto.OAuth2Token;
import org.rondobell.racailum.base.redis.RedisUCloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AddAccessToken {
    static String appId = "hk8185";
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("test.log"));
        String str;
        List<String> list = new ArrayList<>();
        while ((str = in.readLine()) != null) {
            list.add(str);
        }

        RedisUCloud redis = new RedisUCloud();

        for(String id:list){
            OAuth2Token token = new OAuth2Token();
            String value = UUID.randomUUID().toString();
            token.setValue(value);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.HOUR, 2);
            token.setExpiredTime(now.getTime());

            OAuth2Token refresh = new OAuth2Token();
            String value2 = UUID.randomUUID().toString();
            refresh.setValue(value2);
            Calendar now2 = Calendar.getInstance();
            now2.add(Calendar.DATE, 30);
            refresh.setExpiredTime(now2.getTime());

            OAuth2Authorization oauth2 = new OAuth2Authorization();
            oauth2.setAppId(appId);
            oauth2.setRedirectUri("");
            oauth2.setScope("basic");
            oauth2.setOpenUid(id);
            oauth2.setRefreshToken(refresh);
            oauth2.setAccessToken(token);

            redis.setSerializeProtoStuff("o2:t:"+token, oauth2, 60*60*2);
            redis.setSerializeProtoStuff("o2:ft:"+ refresh, oauth2, 60*60*24*30);

        }
    }
}
