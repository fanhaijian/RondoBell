package org.rondobell.racailum;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YunTingUserTest {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		String act = "cjlogin";
		HttpPost httpPost = new HttpPost("http://yuntingoltest.radio.cn/ytsrv/srv/twoCode/getUrl");

		long time = System.currentTimeMillis()/1000;
		String str = "act="+act+"&timetemp="+time+"&key=Uhy6CImFOwftDbvpI6TwW58wfExkcIqTmD7tB4s5sRm5vszv4GYNHTzyby4YRVl1";
		String sign = DigestUtils.md5Hex(str).toUpperCase();
		//System.out.println(sign);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("act", act));
		params.add(new BasicNameValuePair("timetemp", time+""));
		params.add(new BasicNameValuePair("sign", sign));
		System.out.println("act="+act+"&timetemp="+time+"&sign="+sign);

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
