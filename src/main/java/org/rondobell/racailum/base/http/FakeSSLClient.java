package org.rondobell.racailum.base.http;

import com.alibaba.fastjson.JSONArray;
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
import java.io.*;
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
            String cName = "";
            String cType = "03";
            if(cType.equals("03")){
                cName = "在职单客户";
            }
            if(cType.equals("04")){
                cName = "纯孤儿单";
            }
            if(cType.equals("05")){
                cName = "非纯孤儿单";
            }
            map.put("clientType", cType);//03在职单客户 04纯孤，05非纯孤
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
            String cvalue = "pssPssp-prdDmz-StickySessionRule=375763794; BIGipServerPOOL_PACLOUD_PRDR2016050607913=2853478103.16415.0000; loginFrom=portal; WLS_HTTP_BRIDGE_PSS_PSSP=iSnBSP2BUlTCFbDNeNQTN3tUZUXy4Xj_UK-_P20Q9o7NGj9bxZcn!-1321431421; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f826efdde52e8aca0bb0e7fed3856279743c82f96b3afba9c7f359ac3b8ac3da0f5cbe31cae8d8ac306ccf4a2a8356aa9cf0fa8018147546a2b6f003c3e7476dfdc76aa2e6e71738da167bbceed37c77cf867434b4e102b625b292b14eebf0ded05; PASESSION=61fe6a8aca05007b6f59bcd880cb9f826efdde52e8aca0bb0e7fed3856279743c82f96b3afba9c7f359ac3b8ac3da0f5cbe31cae8d8ac306ccf4a2a8356aa9cf0fa8018147546a2b6f003c3e7476dfdc76aa2e6e71738da167bbceed37c77cf867434b4e102b625b292b14eebf0ded05";
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
                    StringBuffer sb = new StringBuffer();
                    sb.append("<html><head><style media=print>.page {\n" +
                            " page-break-after: always;}</style></head><body>");
                    //sb.append("<span>"+cName+"</span>");
                    int i = 0;
                    for (Element tr : elements) {
                        int index = tr.html().indexOf("showDetailByClientNo('");
                        //System.out.println(tr.html());
                        String tail = tr.html().substring(index + 22);
                        int index2 = tail.indexOf("')");
                        String no = tail.substring(0, index2);
                        System.out.println(no);
                        String oneCustom = searchBill(httpClient, no, cvalue);
                        sb.append(oneCustom);
                        i++;
                        if(i>1) {
                            break;
                        }
                        sb.append("<div class=\"page\"></div>");
                    }
                    sb.append("</body></html>");
                    String outName = "D:\\pinganc"+cName+".html";
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outName)),"gbk"));
                    out.write(sb.toString());
                    out.close();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String searchBill(CloseableHttpClient httpClient, String no, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/clientCenter.fillInClientInfoFormByClientNo.do?clientNo=" + no;
        try {
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Map<String, String> map = new HashMap<>();
            //map.put("pageNo", "1");
            //map.put("pageSize", "100");
            //map.put("clientType", "02");//在职单客户，纯孤，非纯孤都是02
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
                    String baseHtml = handleBaseInfo(httpClient, cmId, cvalue);
                    //cha bao dan
                    //https://pssp.pa18.com/queryClientPurchasedProductsNew.do
                    String baoHtml = handleOrderList(httpClient, cmId, cvalue);
                    return baseHtml+baoHtml;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String handleBaseInfo(CloseableHttpClient httpClient, String cmId, String cvalue) {
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
                    StringBuffer bf = new StringBuffer();
                    bf.append("<table border=\"1\" style=\"font-size:14px\"><tr>");
                    bf.append("<td>姓名</td><td>").append(jsonObject1.getString("clientName")).append("</td>");
                    bf.append("<td>性别</td><td>").append("m".equalsIgnoreCase(jsonObject1.getString("clientSex"))?"男":"女").append("</td>");
                    bf.append("<td>身份证号</td><td>").append(jsonObject1.getString("idNo")).append("</td>");
                    bf.append("<td>生日</td><td>").append(jsonObject1.getString("clientBirthdayStr")).append("</td>");
                    bf.append("<td>文化</td><td>").append(jsonObject1.getString("eduDescription")).append("</td>");
                    bf.append("<td>身高</td><td>").append(jsonObject1.getString("height")).append("</td>");
                    bf.append("<td>体重</td><td>").append(jsonObject1.getString("weight")).append("</td>");
                    bf.append("<td>车牌</td><td>").append(jsonObject1.getString("carNumber")).append("</td></tr><tr>");
                    bf.append("<td>婚姻</td><td>").append(jsonObject1.getString("mariDescription")).append("</td>");
                    bf.append("<td>邮编</td><td>").append(jsonObject1.getString("homePostcode")).append("</td>");
                    bf.append("<td>电话</td><td>").append(jsonObject1.getString("clientMobile")).append("</td>");
                    bf.append("<td>宅电</td><td>").append(jsonObject1.getString("homeTel")).append("</td>");
                    bf.append("<td>职业</td><td>").append(jsonObject1.getString("profName")).append("</td>");
                    bf.append("<td>职位</td><td>").append(jsonObject1.getString("position")).append("</td>");
                    bf.append("<td>收入</td><td>").append(jsonObject1.getString("anIncomeDescription")).append("</td><td></td><td></td></tr><tr>");
                    bf.append("<td>家庭住址</td><td colspan=\"15\">").append(jsonObject1.getString("homeAddress")).append("</td></tr><tr>");
                    bf.append("<td>工作单位</td><td colspan=\"15\">").append(jsonObject1.getString("companyName")).append("</td></tr></table>");

                    String space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    bf.append("<br><table border=\"1\" style=\"font-size:14px\"><tr><td>信用卡</td><td>财险</td><td>养老</td><td>寿险</td><td>健康险</td><td>证券</td><td>银行</td><td>基金</td><td>信托</td></tr><tr>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyCard"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyProperty"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyPension"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyLife"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyHealth"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuySecurities"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyBank"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyFund"))?"有"+space:"无"+space).append("</td>");
                    bf.append("<td>").append("1".equalsIgnoreCase(jsonObject1.getString("isBuyTrust"))?"有"+space:"无"+space).append("</td></tr></table>");

                    return bf.toString();


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String handleOrderList(CloseableHttpClient httpClient, String cmId, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://pssp.pa18.com/queryClientPurchasedProductsNew.do";
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
                    JSONObject jsonObject1 = JSONObject.parseObject(result);
                    JSONArray array = jsonObject1.getJSONObject("data").getJSONArray("allPolInfo");
                    StringBuffer sb = new StringBuffer();
                    sb.append("<br><table border=\"1\" style=\"font-size:12px\"><tr>");
                    sb.append("<td>单号</td>");
                    sb.append("<td>投保人</td>");
                    sb.append("<td>投保日期</td>");
                    sb.append("<td>保费</td>");
                    sb.append("<td>方式</td>");
                    sb.append("<td>已交期数</td>");
                    sb.append("<td>已交保费</td>");
                    sb.append("<td>开户行</td>");
                    sb.append("<td>卡号</td>");
                    sb.append("<td>被保人</td>");
                    sb.append("<td>受益人</td>");
                    sb.append("<td>主险</td>");
                    sb.append("<td>附加</td>");
                    sb.append("<td>保额</td>");
                    sb.append("<td>交费年限</td>");
                    sb.append("<td>保险期限</td></tr>");

                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        String pno = jo.getString("polNo");
                        String oneBaoHtml = handleOrderDetail(httpClient, pno, cvalue);
                        List<String> oneBaoDetailHtml = handleOrderDetail2(httpClient, pno, cvalue);
                        String oneBaoRenHtml = handleOrderHost1Detail(httpClient, pno, cvalue);
                        String oneBaoShouHtml = handleOrderShouDetail(httpClient, pno, cvalue);
                        for (int j=0;j<oneBaoDetailHtml.size();j++) {
                            if (j==0) {
                                sb.append("<tr>").append(oneBaoHtml).append(oneBaoRenHtml).append(oneBaoShouHtml).append(oneBaoDetailHtml.get(j)).append("</tr>");
                            }else{
                                sb.append("<tr><td colspan=\"11\">-</td>")
                                        .append(oneBaoDetailHtml.get(j)).append("</tr>");
                            }
                        }
                    }
                    sb.append("</table>");
                    return sb.toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String handleOrderDetail(CloseableHttpClient httpClient, String pno, String cvalue) {
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
                    Document doc = Jsoup.parse(result);
                    StringBuffer sb = new StringBuffer();
                    sb.append("<td>").append(pno).append("</td>");
                    //户主，投保人
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield6").get(1).attr("value")).append("</td>");
                    // 投保日期
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield3").get(0).attr("value")).append("</td>");
                    // 期交保费
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield5").get(1).attr("value")).append("</td>");
                    // 方式
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield7").get(0).attr("value")).append("</td>");
                    //
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield5").get(2).attr("value")).append("</td>");
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield6").get(0).attr("value")).append("</td>");
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield9").get(0).attr("value")).append("</td>");
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield10").get(0).attr("value")).append("</td>");

                    return sb.toString();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static List<String> handleOrderDetail2(CloseableHttpClient httpClient, String pno, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://sales.pa18.com/life/agent.getplaninformation.do?polno="+pno;
        List<String> returnList = new ArrayList<>();
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
                    Document doc = Jsoup.parse(result);
                    StringBuffer sb = null;
                    int size = doc.getElementsByAttributeValue("name", "textfield4").size();

                            for (int i=0; i<size;i++) {
                                sb = new StringBuffer();
                                if (i==0) {
                                    // 主
                                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield4").get(i).attr("value")).append("</td>");
                                    sb.append("<td>-</td>");
                                }else{
                                    sb.append("<td>-</td>");
                                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield4").get(i).attr("value")).append("</td>");

                                }

                                // 保额
                                sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield5").get(i).attr("value")).append("</td>");
                                // 年限
                                sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield8").get(i).attr("value")).append("</td>");
                                // 期限
                                sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield9").get(i).attr("value")).append("</td>");
                                returnList.add(sb.toString());
                            }



                    return returnList;

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String handleOrderHost1Detail(CloseableHttpClient httpClient, String pno, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://sales.pa18.com/life/agent.getinsuredinformation.do?polno="+pno;
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
                    Document doc = Jsoup.parse(result);
                    StringBuffer sb = new StringBuffer();
                    sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield").get(0).attr("value")).append("</td>");

                    return sb.toString();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String handleOrderShouDetail(CloseableHttpClient httpClient, String pno, String cvalue) {
        HttpPost httpPost = null;
        String result = null;
        String url = "https://sales.pa18.com/life/agent.getbeneficiaryinformation.do?polno="+pno;
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
                    Document doc = Jsoup.parse(result);
                    StringBuffer sb = new StringBuffer();
                    if (doc.getElementsByAttributeValue("name", "textfield2").size()>0) {
                        sb.append("<td>").append(doc.getElementsByAttributeValue("name", "textfield2").get(0).attr("value")).append("</td>");
                    }else{
                        sb.append("<td>空</td>");
                    }

                    return sb.toString();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}