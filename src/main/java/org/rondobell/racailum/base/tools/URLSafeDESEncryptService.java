package org.rondobell.racailum.base.tools;

import java.io.UnsupportedEncodingException;

public class URLSafeDESEncryptService {
	
	
	String key = "9Q26kj97jYtls1D6gkF95hq52dyAie1c";

	public String encrypt(String text) throws UnsupportedEncodingException {
		byte[] encryptData = EncryptDESUtil.encrypt(text.getBytes("utf-8"), key);
		String encryptString = Base64Util.urlSafeEncodeToString(encryptData);
		return encryptString;
	}

	public String encrypt(String prefix, String text) throws Exception {
		byte[] encryptData = EncryptDESUtil.encrypt(text.getBytes("utf-8"), prefix+key);
		String encryptString = Base64Util.urlSafeEncodeToString(encryptData);
		return encryptString;
	}

	public String decrypt(String prefix, String text) throws Exception {
		byte[] encryptData = Base64Util.urlSafeDecodeFromString(text);
		byte[] decryptData = EncryptDESUtil.decrypt(encryptData, prefix + key);
		return new String(decryptData,"utf-8");
	}

	public static void main(String[] args) throws Exception {
		byte[] encryptData = Base64Util.urlSafeDecodeFromString("i5NEoiWFPzw");
		byte[] decryptData = EncryptDESUtil.decrypt(encryptData, "nz3554" + "9Q26kj97jYtls1D6gkF95hq52dyAie1c");
		System.out.println(new String(decryptData, "utf-8"));
	}

}
