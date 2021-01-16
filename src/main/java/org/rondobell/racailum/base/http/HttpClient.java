package org.rondobell.racailum.base.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClient {

	private CloseableHttpClient httpClient;

	public String get(String url){
		httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse res = null;
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			//System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			res = httpClient.execute(httpget);
			return EntityUtils.toString(res.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				if(res!=null){
					res.close();
				}
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
