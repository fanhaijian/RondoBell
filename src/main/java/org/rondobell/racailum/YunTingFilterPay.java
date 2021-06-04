package org.rondobell.racailum;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YunTingFilterPay {
	public static void main(String[] args) throws IOException, InterruptedException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		Map<String, String> map = new HashMap<>();
		FileReader fileReader = new FileReader("D:\\project\\kaola-server\\kaola-job\\src\\main\\resources\\yunting_album_finish.txt");
		BufferedReader in = new BufferedReader(fileReader);
		String line;
		while ((line = in.readLine()) != null) {
			String[] infos = line.split("\t");
			//list.add(infos[0]+"\t"+infos[1]);
			map.put(infos[0], infos[1]);

		}
		fileReader.close();
		in.close();

		SqlSession session = SqlSessionFactoryHolder.getSession();
		MzMapper mapper = session.getMapper(MzMapper.class);
		List<Long> myIds = mapper.queryYunIds();

		for (Long id : myIds) {

			String albumId = map.get(String.valueOf(id));
			if(StringUtils.isEmpty(albumId)){
				//System.out.println(albumId+"\tnot exist");
				continue;
			}
			HttpPost httpPost = new HttpPost("http://esopendyn.radio.cn/appstk8k/dyn5105f815be0318f9/5f5c518bf9eaa940e2535a27671eaf2f");

			long time = System.currentTimeMillis() / 1000;
			String str = "albumId=" + albumId + "&timetemp=" + time + "&key=4tDyYxcBYKrKbu1y6Xt3HcKc2EUGEhbWU2BCbSUWyavFjqu2lsS5pUUbs8aJdzIi";
			String sign = DigestUtils.md5Hex(str).toUpperCase();
			//System.out.println(sign);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("albumId", albumId));
			params.add(new BasicNameValuePair("timetemp", time + ""));
			params.add(new BasicNameValuePair("sign", sign));
			//System.out.println("albumId=" + albumId + "&timetemp=" + time + "&sign=" + sign);

			httpPost.setEntity(new UrlEncodedFormEntity(params));
			httpPost.addHeader("equipmentSource", "WEB");

			response = httpClient.execute(httpPost);

			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			response.close();
			//System.out.println(result);

			JSONObject json = JSON.parseObject(result);
			JSONArray conArray = json.getJSONArray("con");
			if(conArray==null){
				continue;
			}
			JSONObject al = conArray.getJSONObject(0);
			int songPay = al.getIntValue("songNeedPay");
			int pay = al.getIntValue("needPay");
			int vip = al.getIntValue("isVip");
			if(songPay!=0||pay!=0||vip!=0){
				System.out.println(id+"\t"+songPay+"-"+pay+"-"+vip);
			}
			Thread.sleep(111);
		}
	}
}
