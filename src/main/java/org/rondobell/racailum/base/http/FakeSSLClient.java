package org.rondobell.racailum.base.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class FakeSSLClient extends DefaultHttpClient {
    public FakeSSLClient() throws Exception{
        super();
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }

    public static void main(String[] args) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/clientCenter.searchClient.do";
        try{
            httpClient = new FakeSSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,"utf-8");
                httpPost.setEntity(entity);
            }


            httpPost.setHeader("cookie","pssPssp-prdDmz-StickySessionRule=-369188778; BIGipServerPOOL_PACLOUD_PRDR2016050607913=3105136343.16415.0000; WLS_HTTP_BRIDGE_PSS_PSSP=MPOs22h13YTYvashsROtv3FyGQd3YC030g2mmjm7jtkVUz2Vm9wB!1977446527; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f827d77b8dd8930f8e3b5225efe38eabda5709d8f36ebf70b54e44e856bdf94ffb90119fb69087ddfff27721d820283eea6297c7be77f5d7d22a0f7fe4712356c75979f5043261b7473b7aa7b4b697e1d2fece043f967df7e101c77f719efa382dd; PASESSION=61fe6a8aca05007b6f59bcd880cb9f827d77b8dd8930f8e3b5225efe38eabda5709d8f36ebf70b54e44e856bdf94ffb90119fb69087ddfff27721d820283eea6297c7be77f5d7d22a0f7fe4712356c75979f5043261b7473b7aa7b4b697e1d2fece043f967df7e101c77f719efa382dd");
            httpPost.setHeader("origin","https://pssp.pa18.com");
            httpPost.setHeader("referer","https://pssp.pa18.com/clientCenter.loadSearchClientView.do");
            httpPost.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            //httpPost.setHeader("origin","https://pssp.pa18.com");


            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                    System.out.println(result);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}