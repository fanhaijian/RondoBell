package org.rondobell.racailum;

public class SimpleTest {
	public static void main(String[] args) {
		String url = "https://m.kaolafm.com:87/location/serv";
		if(url.startsWith("https")){
			url = url.substring("https://".length());
			url = "http://"+ url.replaceFirst(":[1-9]+[0-9]*/|/",":443/");
		}
		System.out.println(url);
	}
}
