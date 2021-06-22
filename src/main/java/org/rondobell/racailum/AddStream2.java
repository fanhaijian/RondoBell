package org.rondobell.racailum;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.dto.BroadcastStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStream2 {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        SqlSession session = null;
        //FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\stream.csv");
        FileReader fileReader = new FileReader("C:\\Users\\fanhj\\Desktop\\广播\\broadcast.txt");

        BufferedReader in = new BufferedReader(fileReader);
        String line;
        int em = 0;
        int fi = 0;
        Map<String,Integer> cMap = new HashMap<>();
        while ((line = in.readLine()) != null) {
            if(line.startsWith("160000")){
                int mod = (int)((Long.valueOf(line))%4);
                String url = "http://10.51.151.13"+mod+":8080/broadcast/";
                response = httpClient.execute(new HttpGet(url+"stopById?id="+line.trim()));
                response.close();
                Thread.sleep(888);
                response = httpClient.execute(new HttpGet(url+"startById?id="+line.trim()));
                response.close();
                Thread.sleep(888);
                response = httpClient.execute(new HttpGet(url+"infoById?id="+line.trim()));
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                response.close();
                //System.out.println(result);
                JSONObject json = JSON.parseObject(result);
                JSONArray conArray = json.getJSONArray("streamInfos");
                System.out.println(conArray);

                int mod2 = (int)((Long.valueOf(line))%2);
                String url2 = "http://10.51.151.13"+(mod2==0?"4":"7")+":8090/broadcast/";
                response = httpClient.execute(new HttpGet(url2+"stopById?id="+line.trim()));
                response.close();
                Thread.sleep(888);
                response = httpClient.execute(new HttpGet(url2+"startById?id="+line.trim()));
                response.close();
                Thread.sleep(888);
                response = httpClient.execute(new HttpGet(url2+"infoById?id="+line.trim()));
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
                response.close();
                //System.out.println(result);
                json = JSON.parseObject(result);
                conArray = json.getJSONArray("streamInfos");
                System.out.println(conArray);

            }
        }
        fileReader.close();
        in.close();


    }
}
