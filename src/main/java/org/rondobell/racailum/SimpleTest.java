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
import org.rondobell.racailum.base.http.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SimpleTest {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		String albumId = "15998281188230";
		HttpPost httpPost = new HttpPost("http://esopendyn.radio.cn/appstk8k/dyn5105f815be0318f9/5f5c518bf9eaa940e2535a27671eaf2f");

		long time = System.currentTimeMillis()/1000;
		String str = "albumId="+albumId+"&timetemp="+time+"&key=4tDyYxcBYKrKbu1y6Xt3HcKc2EUGEhbWU2BCbSUWyavFjqu2lsS5pUUbs8aJdzIi";
		String sign = DigestUtils.md5Hex(str).toUpperCase();
		System.out.println(sign);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("albumId", albumId));
		params.add(new BasicNameValuePair("timetemp", time+""));
		params.add(new BasicNameValuePair("sign", sign));

		httpPost.setEntity(new UrlEncodedFormEntity(params));
		httpPost.addHeader("equipmentSource", "WEB");

		CloseableHttpResponse response = httpClient.execute(httpPost);

		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(result);

	}
}
