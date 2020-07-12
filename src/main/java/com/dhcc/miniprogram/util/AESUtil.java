package com.dhcc.miniprogram.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;

/**
 * @author cb
 * @date 2020/6/29
 * description：
 */
public class AESUtil {
    /** 字符集名称 */
    private static final String CHARSET_NAME = "UTF-8";
    /** 算法 */
    private static final String ALGORITHM = "AES";
    /** 算法/模式/补码方式 */
    private static final String TRANSFORMATION_PKCS5 = "AES/CBC/PKCS5Padding";
    private static final String TRANSFORMATION_PKCS7 = "AES/CBC/PKCS7Padding";

    static{
        // 添加PKCS7Padding支持
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     * @param secretKey 消息秘钥
     * @param secretIv 秘钥初始化向量
     * @param content 要加密内容
     * @return 返回Base64编码的String
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String pkcs5PaddingEncrypt(String secretKey, String secretIv, String content) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        // 获取消息秘钥字节数组
        byte[] raw = secretKey.getBytes(CHARSET_NAME);
        // 秘钥规范
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
        // 创建密码
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_PKCS5);
        // 向量参数规范
        IvParameterSpec ips = new IvParameterSpec(secretIv.getBytes());
        // 初始化密码
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ips);
        // 加密内容
        byte[] encrypted = cipher.doFinal(content.getBytes());
        // 返回Base64加密字符串
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * 解密
     * @param secretKey 消息秘钥
     * @param secretIv 消息秘钥向量
     * @param content 要解密的内容
     * @return 返回原文
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String pkcs5PaddingDecrypt(String secretKey, String secretIv, String content) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 消息秘钥
        byte[] raw = secretKey.getBytes(CHARSET_NAME);
        // 创建密码
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_PKCS5);
        // 秘钥规范
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
        // 向量参数规范
        IvParameterSpec ips = new IvParameterSpec(secretIv.getBytes());
        // 密码初始化
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ips);
        // Base64解码
        byte[] decrypted = Base64.decodeBase64(content);
        // 解除密码
        byte[] original = cipher.doFinal(decrypted);
        // 返回原文
        return new String(original);
    }

    /**
     *
     * 解密
     * @param secretKey 消息秘钥
     * @param secretIv 消息秘钥向量
     * @param content 要加密的内容
     * @return 返回Base64编码的String
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     */
    public static String pkcs7PaddingEncrypt(String secretKey, String secretIv, String content) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchProviderException {
        // 创建密码
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_PKCS7,"BC");
        // 秘钥规范
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(secretKey), ALGORITHM);
        // 向量参数规范
        IvParameterSpec ips = new IvParameterSpec(Base64.decodeBase64(secretIv));
        // 初始化密码
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ips);
        // 加密内容
        byte[] encrypted = cipher.doFinal(content.getBytes());
        // 返回Base64加密字符串
        return Base64.encodeBase64String(encrypted);
    }

    /**
     *
     * 解密
     * @param secretKey 消息秘钥
     * @param secretIv 消息秘钥向量
     * @param content 要解密的内容
     * @return 返回原文
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws NoSuchProviderException
     */
    public static String pkcs7PaddingDecrypt(String secretKey, String secretIv, String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchProviderException {
        // 创建Cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_PKCS7,"BC");
        // 获取秘钥字节数组
        byte[] keyBytes = Base64.decodeBase64(secretKey);
        // 如果密钥不足16位，那么就补足.
        padding(keyBytes,128);
        // 创建秘钥规范
        Key skey = new SecretKeySpec(keyBytes, ALGORITHM);
        // 创建向量参数规范
        IvParameterSpec ips = new IvParameterSpec(Base64.decodeBase64(secretIv));
        // 初始化Cipher
        cipher.init(Cipher.DECRYPT_MODE,skey,ips);
        // 解密并返回String
        return new String(cipher.doFinal(Base64.decodeBase64(content)),CHARSET_NAME);
    }

    public static String pkcs7PaddingDecrypt2(String secretKey, String secretIv, String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchProviderException {
        // 创建Cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_PKCS7,"BC");
        // 获取秘钥字节数组
        byte[] keyBytes = Base64.decodeBase64(secretKey);
        // 如果密钥不足16位，那么就补足.
        padding(keyBytes,256);
        // 创建秘钥规范
        Key skey = new SecretKeySpec(keyBytes, ALGORITHM);
        // 创建向量参数规范
        IvParameterSpec ips = new IvParameterSpec(Base64.decodeBase64(secretIv));
        // 初始化Cipher
        cipher.init(Cipher.DECRYPT_MODE,skey,ips);
        // 解密并返回String
        return new String(cipher.doFinal(Base64.decodeBase64(content)),CHARSET_NAME);
    }


    public static void padding(byte[] keyBytes,int bitLength){
        int base = bitLength >> 3;
        int keyLength = keyBytes.length;
        if (keyLength % base != 0) {
            int groups = keyLength / base + (keyLength % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyLength);
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
    }

}
