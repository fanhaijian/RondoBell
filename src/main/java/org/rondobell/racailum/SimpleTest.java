package org.rondobell.racailum;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class SimpleTest {
	public static void main(String[] args) {
//		String url = "https://m.kaolafm.com:87/location/serv";
//		if(url.startsWith("https")){
//			url = url.substring("https://".length());
//			url = "http://"+ url.replaceFirst(":[1-9]+[0-9]*/|/",":443/");
//		}
//		System.out.println(url);
		Calendar cal = Calendar.getInstance();
		cal.set(2021,3,23,14,10,0);
		System.out.println(cal.getTime());

		String uid = "1234567890";
		System.out.println(uid.substring(uid.length()-4));
	}
}
