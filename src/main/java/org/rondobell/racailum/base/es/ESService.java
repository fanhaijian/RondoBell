package org.rondobell.racailum.base.es;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.rondobell.racailum.base.io.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ESService {

	static TransportClient client;
	static String esHost = "n1.es.kaolat.cn";

	/*static {
		try {
			Settings settings = Settings.builder()
					.put("cluster.name", "api-es-cluster-6")
					.put("client.transport.sniff", true)
					.build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(esHost), 9331));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}*/

	public void createIndices () throws IOException, ParseException {
		String apiProjectPath = "D:\\project";
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");


		File schemePath = new File(apiProjectPath+"\\kaola-server\\kaola-api\\src\\main\\es-scheme");
		File[] schemeTypes = schemePath.listFiles();
		for(int i=0;i<schemeTypes.length;i++) {
			File type = schemeTypes[i];
			System.out.println(type.getName());
			File[] schemes = type.listFiles();
			Date compare = null;
			File latestFile = null;
			for (int j = 0; j < schemes.length; j++) {
				File scheme = schemes[j];
				String filename = scheme.getName();
				System.out.println("==========="+filename);

				if(filename.endsWith("json")){
					String dateStr = filename.substring(filename.length()-15,filename.length()-5);
					Date date = format.parse(dateStr);
					if(compare==null||date.after(compare)){
						compare = date;
						latestFile = scheme;
					}
				}
			}
			System.out.println("latest=========="+latestFile.getName());
			//System.out.println(esHost+":9220/"+latestFile.getName().substring(0,latestFile.getName().length()-5)+"?pretty");

			createIndex(latestFile);

		}
	}

	private void createIndex(File file) throws IOException {
		HttpPut put = new HttpPut("http://"+esHost+":9220/"+file.getName().substring(0,file.getName().length()-5)+"?pretty");
		String esBody = FileUtil.fileToString(file);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		put.setEntity(new StringEntity(esBody, ContentType.APPLICATION_JSON));
		HttpResponse httpResponse = httpClient.execute(put);

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		System.out.println("response code " + statusCode);
		String result = EntityUtils.toString(httpResponse.getEntity());

		System.out.println(result);
	}

	public static void main(String[] args) throws IOException, ParseException {
		ESService service = new ESService();
		service.createIndices();
	}

}
