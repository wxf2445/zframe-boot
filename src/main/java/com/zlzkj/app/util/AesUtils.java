package com.zlzkj.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * 加密请求参数
 * @author: Alan Fu
 */
public class AesUtils {
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * 加密方式
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 字符编码
     */
    private static final String ENCODING = "utf-8";
    /**
     * 密钥
     */
    private static final String KEY = "$A$KKJ0a/At$BJy.";

    private static Logger LOGGER = LoggerFactory.getLogger(AesUtils.class);


    /**
     * 加密
     *
     * @param value
     * @return
     */
    public static String valueEncrypt(String value) {
        try {
            return URLEncoder.encode(AesUtils.encrypt(value), ENCODING);
        } catch (Exception e) {
            return value;
        }
    }


    /**
     * 解密
     *
     * @param content
     * @return
     */
    public static String valueDecrypt(String content) {
        try {
            String text = decrypt(content);
            return StringUtils.isEmpty(text) ? content : text;
        } catch (Exception e) {
            return content;
        }
    }

    public static void main(String[] args) {
        String pwd = valueEncrypt("1");
        System.out.println("加密 ====>>>> " + pwd);
        System.out.println("解密 ====>>>> " + valueDecrypt(pwd));
    }

    /**
     * 加密方式--这种加密方式有两种限制
     * 1、密钥必须是16位的
     * 2、待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出现异常
     */
    public static String encrypt(String content) {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), KEY_ALGORITHM);
        Cipher cipher;
        try {
            //Cipher对象实际完成解密操作
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            //用密钥加密明文(plainText),生成密文(cipherText)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化
            //得到加密后的字节数组
            byte[] result = cipher.doFinal(content.getBytes(ENCODING));
            return encodeHexString(result);//转16进制
        } catch (Exception e) {
            LOGGER.error("加密 " + content + " 失败!!!", e);
            throw new RuntimeException("加密失败" + content, e);
        }
    }

    /**
     * 解密参数
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), KEY_ALGORITHM);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);// 初始化
            byte[] result = cipher.doFinal(decodeHex(content.toCharArray()));
            return new String(result);
        } catch (Exception e) {
            LOGGER.error("解密 " + content + " 失败!!!", e);
            throw new RuntimeException("参数解密失败" + content, e);
        }
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String encodeHexString(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
//			sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();

    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param data src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;

    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch    十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at impl " + index);
        }
        return digit;
    }
}