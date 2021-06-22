
package org.rondobell.racailum.base.tools;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class CryptUtil {
    static AES gAes = new AES();

    private CryptUtil() {
    }

    public static byte[] rsaEncrypt(byte[] data, Key key) {
        byte[] ret = null;
        if (data != null && data.length > 0 && key != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(1, key);
                ret = cipher.doFinal(data);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return ret;
    }

    public static byte[] rsaDecrypt(byte[] data, Key key) {
        byte[] ret = null;
        if (data != null && data.length > 0 && key != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(2, key);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException var4) {
                var4.printStackTrace();
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return ret;
    }

    public static KeyPair generateRSAKey(int keySize) {
        KeyPair ret = null;

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keySize);
            ret = kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        }

        return ret;
    }

    public static byte[] aesEncrypt(byte[] data, byte[] password) {
        byte[] ret = null;
        if (data != null && password != null && data.length > 0 && password.length == 16) {
            try {
                Cipher cipher = Cipher.getInstance("AES");
                SecretKeySpec key = new SecretKeySpec(password, "AES");
                cipher.init(1, key);
                ret = cipher.doFinal(data);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return ret;
    }

    public static byte[] aesDecrypt(byte[] data, byte[] password) {
        byte[] ret = null;
        if (data != null && password != null && data.length > 0 && password.length == 16) {
            try {
                Cipher cipher = Cipher.getInstance("AES");
                SecretKeySpec key = new SecretKeySpec(password, "AES");
                cipher.init(2, key);
                ret = cipher.doFinal(data);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return ret;
    }

    public static byte[] desedeEncrypt(byte[] data, byte[] password) {
        byte[] ret = null;
        if (data != null && password != null && data.length > 0 && password.length == 24) {
            try {
                Cipher cipher = Cipher.getInstance("DESede");
                DESedeKeySpec keySpec = new DESedeKeySpec(password);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                cipher.init(1, secretKey);
                ret = cipher.doFinal(data);
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        return ret;
    }

    public static byte[] desedeDecrypt(byte[] data, byte[] password) {
        byte[] ret = null;
        if (data != null && password != null && data.length > 0 && password.length == 24) {
            try {
                Cipher cipher = Cipher.getInstance("DESede");
                DESedeKeySpec keySpec = new DESedeKeySpec(password);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                cipher.init(2, secretKey);
                ret = cipher.doFinal(data);
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        return ret;
    }

    public static byte[] desEncrypt(byte[] data, byte[] password) {
        byte[] ret = null;
        if (data != null && password != null && data.length > 0 && password.length == 8) {
            try {
                Cipher cipher = Cipher.getInstance("DES");
                DESKeySpec keySpec = new DESKeySpec(password);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                cipher.init(1, secretKey);
                ret = cipher.doFinal(data);
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        return ret;
    }

    public static byte[] desDecrypt(byte[] data, byte[] password) {
        byte[] ret = null;
        if (data != null && password != null && data.length > 0 && password.length == 8) {
            try {
                Cipher cipher = Cipher.getInstance("DES");
                DESKeySpec keySpec = new DESKeySpec(password);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                cipher.init(2, secretKey);
                ret = cipher.doFinal(data);
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        return ret;
    }

    public static String hexEncode(byte[] data) {
        String ret = null;
        if (data != null) {
            StringBuilder sb = new StringBuilder();
            byte[] var3 = data;
            int var4 = data.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                byte b = var3[var5];
                int i = b & 255;
                String s = Integer.toHexString(i);
                if (i < 16) {
                    sb.append('0');
                }

                sb.append(s);
            }

            ret = sb.toString();
        }

        return ret;
    }

    public static byte[] hexDecode(String str) {
        byte[] ret = null;
        if (str != null) {
            int len = str.length();
            if (len > 0 && len % 2 == 0) {
                ret = new byte[len >> 1];
                int rLen = ret.length;

                for(int i = 0; i < rLen; ++i) {
                    int start = i * 2;
                    String subStr = str.substring(start, start + 2);
                    int i1 = Integer.parseInt(subStr, 16);
                    ret[i] = (byte)i1;
                }
            }
        }

        return ret;
    }

//    public static void aesTest() {
//        String dateStr = DateUtil.getNowDateStr("ddMMyyyy");
//        String md5 = MD5(dateStr);
//        String content = "联汇科技2016";
//        String password = md5.substring(8, 24);
//        byte[] contentBytes = content.getBytes();
//        byte[] passwordBytes = password.getBytes();
//        byte[] bytes = aesEncrypt(contentBytes, passwordBytes);
//        String encodeString = Base64.encodeToString(bytes, 2);
//        YLog.i("加密后BASE64：" + encodeString);
//        byte[] base64Decodedbytes = Base64.decode(encodeString.getBytes(), 2);
//        byte[] byteDecode = aesDecrypt(base64Decodedbytes, passwordBytes);
//        String decodeString = new String(byteDecode);
//        YLog.i("解密后：" + decodeString);
//    }

    public static String aesEncryptParameter(String parameter) {
        if (StringUtils.isEmpty(parameter)) {
            return null;
        } else {
            byte[] sendBytes = null;

            try {
                sendBytes = parameter.getBytes("UTF-8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }

            return gAes.encrypt(sendBytes);
        }
    }

    public static String aesDecodeParameter(String parameter) {
        try {
            String result = gAes.decrypt(parameter);
            return result;
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < md.length; ++i) {
                int val = md[i] & 255;
                if (val < 16) {
                    sb.append("0");
                }

                sb.append(Integer.toHexString(val));
            }

            return sb.toString();
        } catch (Exception var7) {
            return null;
        }
    }
}
