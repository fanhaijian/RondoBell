package org.rondobell.racailum.convert;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;
import org.rondobell.racailum.base.dto.ConvertInfo;
import org.rondobell.racailum.base.http.HttpClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConvertTask implements Runnable{

	static Long startId = 1000025832654L;

	String server;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public static void main(String[] args) throws ParseException {


		List<String> serverList = new ArrayList<>();
		serverList.add("http://10.8.1.6:8080,12");
		serverList.add("http://10.8.1.8:8080,12");
		serverList.add("http://10.8.1.9:8080,6");
		serverList.add("http://10.8.1.10:8081,10");
		serverList.add("http://10.8.1.58:8081,10");
		serverList.add("http://10.8.1.3:8080,10");

		for (String serverInfo:serverList){
			String server = serverInfo.split(",")[0];
			int size = Integer.parseInt(serverInfo.split(",")[1]);

			for (int i=0;i<size;i++) {
				ConvertTask task = new ConvertTask();
				task.setServer(server);
				task.setId(i);
				new Thread(task).start();
				//break;
			}
			//break;
		}

	}

	@Override
	public void run() {

		HttpClient httpClient = new HttpClient();
		List<ConvertInfo> convertInfoList = new ArrayList<>();

		while (true) {
			synchronized (ConvertTask.class){
				SqlSession session = SqlSessionFactoryHolder.getSession();
				MzMapper mapper = session.getMapper(MzMapper.class);
				convertInfoList = mapper.queryAudioConvertInfo(startId, 1);
				if (convertInfoList.size()>0) {
					startId = convertInfoList.get(0).getAudioId();
				}
				session.close();
			}
			if(convertInfoList.size()==0){
				System.out.println(server+" "+id+"   finish");
				break;
			}else{
				ConvertInfo info = convertInfoList.get(0);
				System.out.println(server+" "+id+" "+info.getAudioId()+" "+info.getFilePath());
				String result = httpClient.get(server+"/kaola-new-audioConvert/old/sync_convert?" +
						"audioId="+info.getAudioId()+"&catalogId="+info.getCatalogId()+"&filePath="+info.getFilePath());

				System.out.println(server+" "+id+" "+info.getAudioId()+" "+result);

			}
		}


	}
}
