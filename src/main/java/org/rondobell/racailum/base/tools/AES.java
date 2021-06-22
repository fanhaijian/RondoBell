//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.rondobell.racailum.base.tools;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
    private final int HASH_ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;
    private char[] humanPassphrase = new char[]{'P', 'n', 'e', 'r', 's', ' ', 'v', 's', 'a', 'l', 'l', 'u', 'm', ' ', 'd', 'u', 'b', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a', 'n', 't', ' '};
    private byte[] salt = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private PBEKeySpec myKeyspec;
    private final String CIPHERMODEPADDING;
    private SecretKeyFactory keyfactory;
    private SecretKey sk;
    private SecretKeySpec skforAES;
    private byte[] iv;
    private IvParameterSpec IV;

    public AES() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        this.myKeyspec = new PBEKeySpec(this.humanPassphrase, this.salt, 10000, 256);
        this.CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
        this.keyfactory = null;
        this.sk = null;
        this.skforAES = null;
        this.iv = new byte[]{10, 1, 11, 5, 4, 15, 7, 9, 23, 3, 1, 6, 8, 12, 13, 91};

        try {
            this.keyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            this.sk = this.keyfactory.generateSecret(this.myKeyspec);
        } catch (NoSuchAlgorithmException var2) {
            //YLog.e("AESdemo", "no key factory support for PBEWITHSHAANDTWOFISH-CBC");
        } catch (InvalidKeySpecException var3) {
            //YLog.e("AESdemo", "invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
        }

        byte[] skAsByteArray = this.sk.getEncoded();
        this.skforAES = new SecretKeySpec(skAsByteArray, "AES");
        this.IV = new IvParameterSpec(this.iv);
    }

    public String encrypt(byte[] plaintext) {
        byte[] ciphertext = this.encrypt(this.CIPHERMODEPADDING, this.skforAES, this.IV, plaintext);
        String base64_ciphertext = Base64Encoder.encode(ciphertext);
        return base64_ciphertext;
    }

    public String decrypt(String ciphertext_base64) {
        byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
        String decrypted = new String(this.decrypt(this.CIPHERMODEPADDING, this.skforAES, this.IV, s));
        return decrypted;
    }

    private byte[] addPadding(byte[] plain) {
        int shortage = 16 - plain.length % 16;
        if (shortage == 0) {
            shortage = 16;
        }

        byte[] plainpad = new byte[plain.length + shortage];

        int i;
        for (i = 0; i < plain.length; ++i) {
            plainpad[i] = plain[i];
        }

        for (i = plain.length; i < plain.length + shortage; ++i) {
            plainpad[i] = (byte) shortage;
        }

        return plainpad;
    }

    private byte[] dropPadding(byte[] plainpad) {
        int drop = plainpad[plainpad.length - 1];
        byte[] plain = new byte[plainpad.length - drop];

        for (int i = 0; i < plain.length; ++i) {
            plain[i] = plainpad[i];
            plainpad[i] = 0;
        }

        return plain;
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(1, sk, IV);
            return c.doFinal(msg);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(2, sk, IV);
            return c.doFinal(ciphertext);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        AES gAes = new AES();
        String sendString = "CCYT_0aeffd82eb02b6d4725b1643e3a6aba2_1561018615425";
        byte[] sendBytes = null;

        try {
            sendBytes = sendString.getBytes("UTF8");
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        String str = gAes.encrypt(sendBytes);
        System.out.println(str);
        String result = gAes.decrypt(str);
        System.out.println(result);
    }
}
