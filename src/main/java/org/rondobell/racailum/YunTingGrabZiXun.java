package org.rondobell.racailum;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YunTingGrabZiXun {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		String key = "4tDyYxcBYKrKbu1y6Xt3HcKc2EUGEhbWU2BCbSUWyavFjqu2lsS5pUUbs8aJdzIi";
		String radioId = "10025";
		long time = System.currentTimeMillis()/1000;
		String str = "radioId="+radioId+"&timetemp="+time+"&key="+key;
		String sign = DigestUtils.md5Hex(str).toUpperCase();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("radioId", radioId+""));
		params.add(new BasicNameValuePair("timetemp", time+""));
		params.add(new BasicNameValuePair("sign", sign));
		System.out.println("radioId="+radioId+"&timetemp="+time+"&sign="+sign);
		HttpGet httpPost = new HttpGet("http://esopendyn.radio.cn/appstk8k/dyn80360dade381ab97/5f5c518bf9eaa940e2535a27671eaf2f?"+"radioId="+radioId+"&timetemp="+time+"&sign="+sign);

		//httpPost.setEntity(new UrlEncodedFormEntity(params));
		httpPost.addHeader("equipmentSource", "WEB");
		CloseableHttpResponse response = httpClient.execute(httpPost);

		//String str = "timetemp="+time+"&radioId="+radioId+"&key="+key;

		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		response.close();
		System.out.println(result);
		/*System.out.println(result);
		JSONObject json = JSON.parseObject(result);
		JSONArray conArray = json.getJSONArray("con");
		String img = conArray.getJSONObject(0).getString("img");
		System.out.println(img);*/

	}
}
