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

public class ConvertAlbum implements Runnable{

	static Long startId = 1000026296476L;

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

		HttpClient httpClient = new HttpClient();


		List<String> serverList = new ArrayList<>();
		//serverList.add("http://10.51.151.117:8084,4");
		serverList.add("http://10.51.151.119:8080,4");
		//serverList.add("http://10.51.151.126:8081,4");
		serverList.add("http://10.51.151.120:8081,4");
		//serverList.add("http://10.51.151.135:8080,1");
		//serverList.add("http://10.51.151.136:8080,1");

		List<ConvertInfo> convertInfoList = new ArrayList<>();

		SqlSession session = SqlSessionFactoryHolder.getSession();
			MzMapper mapper = session.getMapper(MzMapper.class);
			convertInfoList = mapper.queryAudioByAlbumId(1100000000924L);
			session.close();
			int i=1;
			for(ConvertInfo info:convertInfoList){
				System.out.println(info.getAudioId()+" "+info.getFilePath());
				String server = serverList.get(i%2);
				String result = httpClient.get(server+"/kaola-new-audioConvert/old/convert?" +
						"audioId="+info.getAudioId()+"&catalogId="+info.getCatalogId()+"&filePath="+info.getFilePath());

				i++;
				try {
					Thread.sleep(1000*30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		/*for (String serverInfo:serverList){
			String server = serverInfo.split(",")[0];
			int size = Integer.parseInt(serverInfo.split(",")[1]);

			for (int i=0;i<size;i++) {
				ConvertAlbum task = new ConvertAlbum();
				task.setServer(server);
				task.setId(i);
				new Thread(task).start();
				//break;
			}
			//break;
		}*/

	}

	@Override
	public void run() {

		HttpClient httpClient = new HttpClient();
		List<ConvertInfo> convertInfoList = new ArrayList<>();

		SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while (true) {
			synchronized (ConvertAlbum.class){
				SqlSession session = SqlSessionFactoryHolder.getSession();
				try {
					MzMapper mapper = session.getMapper(MzMapper.class);
					convertInfoList = mapper.queryAudioConvertInfo(startId, 1);
					if (convertInfoList.size()>0) {
						startId = convertInfoList.get(0).getAudioId();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					session.close();
				}
			}
			if(convertInfoList.size()==0){
				System.out.println(server+" "+id+"   finish");
				break;
			}else{
				try {
					ConvertInfo info = convertInfoList.get(0);
					System.out.println(formator.format(new Date())+" "+server+" "+id+" "+info.getAudioId()+" "+info.getFilePath());
					long start = System.currentTimeMillis();
					String result = httpClient.get(server+"/kaola-new-audioConvert/old/sync_convert?" +
							"audioId="+info.getAudioId()+"&catalogId="+info.getCatalogId()+"&filePath="+info.getFilePath());

					long end = System.currentTimeMillis();
					System.out.println(formator.format(new Date())+" "+server+" "+id+" "+info.getAudioId()+" "+(end- start)+" "+result);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}


	}
}
