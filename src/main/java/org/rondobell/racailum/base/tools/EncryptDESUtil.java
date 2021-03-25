package org.rondobell.racailum.base.tools;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * DES 对称加密
 * 
 * @author fanhj
 *
 */
public class EncryptDESUtil {
	
	//url 安全的base64编码
	final static Base64.Decoder Base64Decoder = Base64.getDecoder();
	final static Base64.Encoder Base64Encoder = Base64.getEncoder();

	public static byte[] encrypt(byte[] content, String key) {
		try {
			/*SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key.getBytes("utf-8"));
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);*/
			
			SecretKey securekey = generateKey(key);
			
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象,ENCRYPT_MODE用于将 Cipher 初始化为加密模式的常量
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			// 现在，获取数据并加密
			// 正式执行加密操作
			byte[] data = cipher.doFinal(content); // 按单部分操作加密或解密数据，或者结束一个多部分操作
			// 加密完成
			return data;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SecretKey generateKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(key.getBytes("utf-8"));
		// 为我们选择的DES算法生成一个KeyGenerator对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance("DES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		kg.init(random);
		SecretKey securekey = kg.generateKey();
		return securekey;
	}

	public static byte[] decrypt(byte[] secret, String key) throws Exception {
		/*
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(key.getBytes("utf-8"));
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 返回实现指定转换的 Cipher 对象
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		*/
		
		SecretKey securekey = generateKey(key);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		// 真正开始解密操作
		byte[] data = cipher.doFinal(secret);
		// 返回原文字符串
		return data;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// 待加密内容
		String str = "加密测试";
		// 密码，长度要是8的倍数
		String key = "12345678901234567890123456789012345678901234567890123456789012";
		
		System.out.println("加密前：" + str + "      "+key.getBytes("utf-8").length);

		byte[] result = EncryptDESUtil.encrypt(str.getBytes("utf-8"), key);
		System.out.println("加密字节长度：" + result.length);

		String resultStr = Base64Encoder.encodeToString(result);
		System.out.println("加密后：" + resultStr);
		// 直接将如上内容解密
		byte[] resultByte = Base64Decoder.decode(resultStr);
		System.out.println("加密字节长度：" + resultByte.length);

		try {
			byte[] decryResult = EncryptDESUtil.decrypt(resultByte, key);
			System.out.println("解密后：" + new String(decryResult,"utf-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
