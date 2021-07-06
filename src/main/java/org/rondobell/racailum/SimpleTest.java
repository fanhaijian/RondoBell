package org.rondobell.racailum;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class SimpleTest {
	private static final String DOMAIN="http://yuntinglive.radio.cn";
	public static void main(String[] args) {
		String url = "http://yuntinglive.radio.cn/186/radios/10370/index_10370.m3u8?type=1";
		url = url.substring(DOMAIN.length(), url.indexOf("?"));
		System.out.println(url);

		//System.out.println(Stream2Sign.addSign("http://yuntingbcast.radio.cn/80/radios/10300/index_10300.m3u8?type=1"));
		System.out.println(getPath("http://image.kaolafm.net/mz/audios/202105/6bb04bf2-ad38-428e-a63e-a6c58735e6fe.mp3"));
	}

	public static String getPath(String url) {
		String[] paths = url.split("/");
		StringBuffer path = new StringBuffer();
		if (paths.length > 3) {
			for (int i = 3; i < paths.length; i++) {
				path.append("/").append(paths[i]);
			}
		}
		return path.toString();
	}
}
