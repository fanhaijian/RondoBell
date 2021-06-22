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
import org.rondobell.racailum.base.http.FakeSSLClient;
import org.rondobell.racailum.base.tools.AES;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class YunTingUserTest2 {
	public static void main(String[] args) throws Exception {
		autoLogin();

	}

	private static void autoLogin() throws Exception {
		AES aes = new AES();
		String phone= aes.encrypt("13503212758".getBytes(StandardCharsets.UTF_8));
		String password = "12345678";
		password = aes.encrypt(password.getBytes(StandardCharsets.UTF_8));
		CloseableHttpClient httpClient = new FakeSSLClient();
		long time = System.currentTimeMillis()/1000;
		String str = "password="+password+"&phone="+phone+"&timetemp="+time+"&key=Uhy6CImFOwftDbvpI6TwW58wfExkcIqTmD7tB4s5sRm5vszv4GYNHTzyby4YRVl1";
		String sign = DigestUtils.md5Hex(str).toUpperCase();
		//System.out.println(sign);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("timetemp", time+""));
		params.add(new BasicNameValuePair("sign", sign));
		String url = "https://ytapi.radio.cn/ytsrv/srv/twoCode/autoLogin";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		httpPost.addHeader("equipmentSource", "WEB");
		System.out.println(httpPost.toString());
		System.out.println("password="+password+"&phone="+phone+"&timetemp="+time+"&sign="+sign);

		CloseableHttpResponse response = httpClient.execute(httpPost);

		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(response.getStatusLine().toString());
		System.out.println(aes.decrypt(result));
	}
}
