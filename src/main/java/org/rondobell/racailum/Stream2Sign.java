package org.rondobell.racailum;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Stream2Sign {
    public static void main(String[] args) throws Exception {

        //湖南交通
        //1600000000079
        //http://yuntingbcast.radio.cn/80/radios/10300/index_10300.m3u8?type=1
        System.out.println(addSign("http://yuntingbcast.radio.cn/80/radios/10300/index_10300.m3u8?type=1"));

    }

    public static String addSign(String url){
        String domain = url.substring(url.indexOf("//")+2);
        domain = domain.substring(domain.indexOf("/"),domain.indexOf("?"));
        //System.out.println(url);
        String time =  Long.toHexString(System.currentTimeMillis()/1000 );
        String key = DigestUtils.md5Hex(domain + "nwaQ2in1eqZzfgKJs2jz"  + time);
        String result = (url+ "&key=" + key + "&time=" + time);
        return result;
    }
}
