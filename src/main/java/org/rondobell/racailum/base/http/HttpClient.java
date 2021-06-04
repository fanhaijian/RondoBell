package org.rondobell.racailum.base.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

	public String post(String url){
		httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse res = null;
		try {
			// 创建httpget.
			HttpPost httpget = new HttpPost(url);
			//System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			res = httpClient.execute(httpget);
			System.out.println(res);
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

	public static void main(String[] args) {
		HttpClient http = new HttpClient();
		System.out.println("post https://ytapi.radio.cn/ytsrv/srv/twoCode/getUrl?act=cjlogin");
		//System.out.println("response: "+http.post("https://ytapi.radio.cn/ytsrv/srv/twoCode/getUrl?act=cjlogin"));
		System.out.println("");
		System.out.println("");
		String url2 = "http://yuntingoltest.radio.cn/ytsrv/srv/twoCode/getUrl?act=cjlogin";
		System.out.println("post "+url2);
		//System.out.println(""+http.post(url2));


		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		//String url = "https://pssp.pa18.com/clientCenter.searchClient.do";
		try {
			httpClient = new FakeSSLClient();
			httpPost = new HttpPost(url2);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				System.out.println(response);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
