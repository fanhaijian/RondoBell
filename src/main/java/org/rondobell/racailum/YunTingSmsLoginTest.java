package org.rondobell.racailum;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.rondobell.racailum.base.http.FakeSSLClient;

import java.util.ArrayList;
import java.util.List;

public class YunTingSmsLoginTest {
	public static void main(String[] args) throws Exception {
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpClient httpClient = new FakeSSLClient();


		String act = "13488863669";
		String type = "1014";
		//HttpPost httpPost = new HttpPost("http://yuntingoltest.radio.cn/ytsrv/srv/twoCode/getUrl");
		HttpPost httpPost = new HttpPost("https://ytapi.radio.cn/ytsrv/srv/phoneUser/login");


		long time = System.currentTimeMillis()/1000;
		String str = "mobilNumber="+act+"&timetemp="+time+"&key=Uhy6CImFOwftDbvpI6TwW58wfExkcIqTmD7tB4s5sRm5vszv4GYNHTzyby4YRVl1";
		String sign = DigestUtils.md5Hex(str).toUpperCase();
		//System.out.println(sign);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mobilNumber", act));
		params.add(new BasicNameValuePair("businessType", type));
		params.add(new BasicNameValuePair("params", ""));
		params.add(new BasicNameValuePair("plantName", "CCYT"));

		//params.add(new BasicNameValuePair("timetemp", time+""));
		//params.add(new BasicNameValuePair("sign", sign));
		//System.out.println("act="+act+"&timetemp="+time+"&sign="+sign);

		httpPost.setEntity(new UrlEncodedFormEntity(params));
		httpPost.addHeader("equipmentSource", "WEB");

		CloseableHttpResponse response = httpClient.execute(httpPost);

		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(result);
		/*JSONObject json = JSON.parseObject(result);
		JSONArray conArray = json.getJSONArray("con");
		String img = conArray.getJSONObject(0).getString("img");
		System.out.println(img);*/

	}
}
