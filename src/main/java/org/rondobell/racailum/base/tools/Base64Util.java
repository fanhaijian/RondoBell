package org.rondobell.racailum.base.tools;

import java.util.Base64;

public class Base64Util {

	public static String urlSafeEncodeToString(byte[] encryptData) {
		String base64String = Base64.getUrlEncoder().encodeToString(encryptData);
		return delTail(base64String);
	}

	public static byte[] urlSafeDecodeFromString(String text) {
		text = appendTail(text);
		return Base64.getUrlDecoder().decode(text);
	}

	/**
	 * 删除base64末尾的=
	 * base64的长度是4的倍数，不足的用=补
	 * @param base64String
	 * @return
	 */
	private static String delTail(String base64String) {
		for (int i = 0; i < 3; i++) {
			if(base64String.endsWith("=")) {
				base64String = base64String.substring(0,base64String.length()-1);
			}else {
				break;
			}
		}
		return base64String;
	}
	
	/**
	 * 补充base64末尾的=
	 * @param text
	 * @return
	 */
	private static String appendTail(String text) {
		int tail = 4-text.length()%4;
		if(tail == 4) {
			tail = 0;
		}
		for (int i = 0; i < tail; i++) {
			text+="=";
		}
		return text;
	}

	public static void main(String[] args) {
		String a = "12345===";
		a = delTail(a);
		System.out.println(a);
		
		String b = "123456==";
		b = delTail(b);
		System.out.println(b);
		
		String c = "1234567=";
		c = delTail(c);
		System.out.println(c);
		
		String d = "12345678";
		d = delTail(d);
		System.out.println(d);
		
		String a1 = "12345";
		a1 = appendTail(a1);
		System.out.println(a1);
		
		String b1 = "123456";
		b1 = appendTail(b1);
		System.out.println(b1);
		
		String c1 = "1234567";
		c1 = appendTail(c1);
		System.out.println(c1);
		
		String d1 = "12345678";
		d1 = appendTail(d1);
		System.out.println(d1);
		
		String e1 = "1234";
		e1 = appendTail(e1);
		System.out.println(e1);

	}
}
