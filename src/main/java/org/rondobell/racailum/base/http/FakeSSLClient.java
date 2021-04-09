package org.rondobell.racailum.base.http;

import com.alibaba.fastjson.JSONObject;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class FakeSSLClient extends DefaultHttpClient {
    public FakeSSLClient() throws Exception {
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
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }

    public static void main(String[] args) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/clientCenter.searchClient.do";
        try {
            httpClient = new FakeSSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            map.put("pageNo", "1");
            map.put("pageSize", "100");
            map.put("clientType", "03");//在职单客户 04纯孤，05非纯孤
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(entity);
            }


            //String cvalue = "";
            String cvalue = "loginFrom=portal; pssPssp-prdDmz-StickySessionRule=1596491363; BIGipServerPOOL_PACLOUD_PRDR2016050607913=2736037591.16415.0000; WLS_HTTP_BRIDGE_PSS_PSSP=XF21tBAwi1UZLXGT-4cm95SeGws7bUsHOSNI22uE8kJ3vV7TAGBD!-1970981686; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f8215e4d3834ba684195fba1f40db652da8129f45898c151f19b4b2553bdc583ddee1ee56ca878973d09ba2ca066d9a0264a47484127f7c42f7f4fa9ac0fc24f5ee72bede961b3f020053a850f809db9d1a7b047124152cf82167eb11c7cb68e783; PASESSION=61fe6a8aca05007b6f59bcd880cb9f8215e4d3834ba684195fba1f40db652da8129f45898c151f19b4b2553bdc583ddee1ee56ca878973d09ba2ca066d9a0264a47484127f7c42f7f4fa9ac0fc24f5ee72bede961b3f020053a850f809db9d1a7b047124152cf82167eb11c7cb68e783";
            httpPost.setHeader("cookie", cvalue);
            httpPost.setHeader("origin", "https://pssp.pa18.com");
            httpPost.setHeader("referer", "https://pssp.pa18.com/clientCenter.loadSearchClientView.do");
            httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            //httpPost.setHeader("origin","https://pssp.pa18.com");


            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    //System.out.println(result);
                    Document doc = Jsoup.parse(result);
                    Element table = doc.getElementById("clientListTable");
                    List<Element> elements = table.child(0).children();
                    elements.remove(0);

                    for (Element tr : elements) {
                        int index = tr.html().indexOf("showDetailByClientNo('");
                        //System.out.println(tr.html());
                        String tail = tr.html().substring(index + 22);
                        int index2 = tail.indexOf("')");
                        String no = tail.substring(0, index2);
                        System.out.println(no);
                        searchBill(httpClient, no, cvalue);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void searchBill(CloseableHttpClient httpClient, String no, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/clientCenter.fillInClientInfoFormByClientNo.do?clientNo=" + no;
        try {
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            map.put("pageNo", "1");
            map.put("pageSize", "100");
            map.put("clientType", "02");//在职单客户，纯孤，非纯孤都是02
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                //list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(entity);
            }


            //String cvalue = "";
            //String cvalue = "loginFrom=portal; pssPssp-prdDmz-StickySessionRule=2040417074; WLS_HTTP_BRIDGE_PSS_PSSP=pbiyNBiS7UG7aRMv00j8u_RsNbK8Gmc5rlnzxOXTdVbysKWFVnpI!530312128; BIGipServerPOOL_PACLOUD_PRDR2016050607913=286564055.16415.0000; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea; PASESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea";
            httpPost.setHeader("cookie", cvalue);
            httpPost.setHeader("origin", "https://pssp.pa18.com");
            //httpPost.setHeader("referer","https://pssp.pa18.com/clientCenter.loadSearchClientView.do");
            httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            //httpPost.setHeader("origin","https://pssp.pa18.com");


            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    //System.out.println(result);
                    int index = result.indexOf("clientMainId");
                    String tail = result.substring(index + 13);
                    int index2 = tail.indexOf("&amp;");
                    String cmId = tail.substring(0, index2);
                    System.out.println(cmId);

                    //base info
                    //https://pssp.pa18.com/clientCenter.fillInClientInfoForm.do
                    handleBaseInfo(httpClient, cmId, cvalue);
                    //cha bao dan
                    //https://pssp.pa18.com/queryClientPurchasedProductsNew.do
                    handleOrderList(httpClient, cmId, cvalue);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void handleBaseInfo(CloseableHttpClient httpClient, String cmId, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/clientCenter.fillInClientInfoForm.do";
        try {
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            map.put("clientMainId", cmId);
            map.put("clientType", "02");//在职单客户，纯孤，非纯孤都是02
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(entity);
            }


            //String cvalue = "";
            //String cvalue = "loginFrom=portal; pssPssp-prdDmz-StickySessionRule=2040417074; WLS_HTTP_BRIDGE_PSS_PSSP=pbiyNBiS7UG7aRMv00j8u_RsNbK8Gmc5rlnzxOXTdVbysKWFVnpI!530312128; BIGipServerPOOL_PACLOUD_PRDR2016050607913=286564055.16415.0000; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea; PASESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea";
            httpPost.setHeader("cookie", cvalue);
            httpPost.setHeader("origin", "https://pssp.pa18.com");
            //httpPost.setHeader("referer","https://pssp.pa18.com/clientCenter.loadSearchClientView.do");
            httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            //httpPost.setHeader("origin","https://pssp.pa18.com");


            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    //System.out.println(result);
                    int index = result.indexOf("var clientDetailInfo = eval(");
                    String tail = result.substring(index+28);
                    index = tail.indexOf(");");
                    String json = tail.substring(0, index);
                    System.out.println(json);
                    JSONObject jsonObject1 = JSONObject.parseObject(json);



                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void handleOrderList(CloseableHttpClient httpClient, String cmId, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/clientCenter.fillInClientInfoFormByClientNo.do?";
        try {
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            map.put("clientMainId", cmId);

            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(entity);
            }


            //String cvalue = "";
            //String cvalue = "loginFrom=portal; pssPssp-prdDmz-StickySessionRule=2040417074; WLS_HTTP_BRIDGE_PSS_PSSP=pbiyNBiS7UG7aRMv00j8u_RsNbK8Gmc5rlnzxOXTdVbysKWFVnpI!530312128; BIGipServerPOOL_PACLOUD_PRDR2016050607913=286564055.16415.0000; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea; PASESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea";
            httpPost.setHeader("cookie", cvalue);
            httpPost.setHeader("origin", "https://pssp.pa18.com");
            //httpPost.setHeader("referer","https://pssp.pa18.com/clientCenter.loadSearchClientView.do");
            httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            //httpPost.setHeader("origin","https://pssp.pa18.com");


            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    //System.out.println(result);

                    //for
                    String pno = "P010000016894315";
                    handleOrderDetail(httpClient, pno, cvalue);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void handleOrderDetail(CloseableHttpClient httpClient, String pno, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://sales.pa18.com/life/agent.getpolicyinformation.do?polno="+pno;
        try {
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            //map.put("clientMainId", cmId);

            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(entity);
            }


            //String cvalue = "";
            //String cvalue = "loginFrom=portal; pssPssp-prdDmz-StickySessionRule=2040417074; WLS_HTTP_BRIDGE_PSS_PSSP=pbiyNBiS7UG7aRMv00j8u_RsNbK8Gmc5rlnzxOXTdVbysKWFVnpI!530312128; BIGipServerPOOL_PACLOUD_PRDR2016050607913=286564055.16415.0000; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea; PASESSION=61fe6a8aca05007b6f59bcd880cb9f82aa0dfd1c67b844af935499c883ffd44da873595b0ec4532a1babcf9c7374ce5d2ca310ee01a90b6d250b36733c88665d1aa880a88407ca9f0a7481c72f7462d582fcba2d21ee4f2ae4df60afd27d3bf63c6661b292ee814418c8204e72c9aeea";
            httpPost.setHeader("cookie", cvalue);
            httpPost.setHeader("origin", "https://pssp.pa18.com");
            //httpPost.setHeader("referer","https://pssp.pa18.com/clientCenter.loadSearchClientView.do");
            httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            //httpPost.setHeader("origin","https://pssp.pa18.com");


            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    System.out.println(result);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}