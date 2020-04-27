package com.fenghuang.caipiaobao.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ Author  QinTian
 * @ Date  2020/4/27
 * @ Describe
 */
public class AESUtilsNew {

//    private static final String SKEY = "uHNTivV09dQxLor9";

    /**
     * 加密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String Encrypt(String key,String sSrc) {
        try {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return BinaryToHex(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String Decrypt(String key,String sSrc) {
        try {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(HexToBinary(sSrc));
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 二进制转16进制
     *
     * @param str
     * @return
     */
    private static String BinaryToHex(byte str[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            String hex = Integer.toHexString(str[i] & 0xFF);
            if (hex.length() == 1) {
                hex = 0 + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制转二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] HexToBinary(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
