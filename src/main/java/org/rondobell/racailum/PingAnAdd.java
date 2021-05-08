package org.rondobell.racailum;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.rondobell.racailum.base.http.FakeSSLClient;

import java.io.*;
import java.util.*;

public class PingAnAdd {
    public static void main(String[] args) throws IOException {

        List<String> existList = new ArrayList<>();

        String file = "D:\\pingan\\ex.html";
        try {
            if(args.length>1){
                file = args[0];
            }
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                existList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            map.put("pageSize", "500");
            String cName = "";
            String cType = "04";
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
            String cvalue = "loginFrom=portal; pssPssp-prdDmz-StickySessionRule=1327661529; WLS_HTTP_BRIDGE_PSS_PSSP=YeFF7H9NmtUGgkw0A55pPPgEqP2TR_dDjqF5MlaSXFzSfXt7yLy6!1034367857; BIGipServerPOOL_PACLOUD_PRDR2016050607913=3038027479.16415.0000; PORTALSESSION=61fe6a8aca05007b6f59bcd880cb9f82b7d321ae49d79d1b9ed8a93a7eaa1a09bf11485e1eb37dc9fa4874e6bd3322e7801dc4a91951d85b0963a18dbe834498d8f6619af2b70b9d4663e24c4a7939da7d2d21ee1551b7c619577a676d9bff8d058c65f5ae915f3e7103b2f67ee1bfd5; PASESSION=61fe6a8aca05007b6f59bcd880cb9f82b7d321ae49d79d1b9ed8a93a7eaa1a09bf11485e1eb37dc9fa4874e6bd3322e7801dc4a91951d85b0963a18dbe834498d8f6619af2b70b9d4663e24c4a7939da7d2d21ee1551b7c619577a676d9bff8d058c65f5ae915f3e7103b2f67ee1bfd5";
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
                    sb.append("<html><head><style>table { border-collapse: collapse;}\n" +
                            " tr td { border:1px solid black;}</style><style media=print>.page {\n" +
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
                        String oneCustom = PingAn.searchBill(httpClient, no, cvalue, existList);
                        i++;
                        if(oneCustom==null){
                            continue;
                        }
                        sb.append(oneCustom);
                        if(i>1) {
                            //break;
                        }
                        sb.append("<div class=\"page\"></div>");
                        Thread.sleep(777);
                    }
                    sb.append("</body></html>");
                    String outName = "D:\\pinganc"+cName+"_new0507.html";
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outName)),"gbk"));
                    out.write(sb.toString());
                    out.close();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
