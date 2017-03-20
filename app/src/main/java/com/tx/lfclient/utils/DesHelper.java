package com.tx.lfclient.utils;

import android.annotation.SuppressLint;

import com.sun.crypto.provider.SunJCE;

import org.xutils.common.util.LogUtil;

import javax.crypto.Cipher;

import java.security.Key;
import java.security.Security;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/7
 */

public class DesHelper {

    private static final String KEY = "IMP_LF_YDD";
    private static final String DES = "DES";

    public static String getEncode(String content) {
        String res = "ydd";
        try {
            res = byteArr2HexStr(encrypt(content.getBytes()));
        } catch (Exception e) {
            LogUtil.w(e);
        }
        return res;
    }

    public static String getDecode(String content) {
        String res = "ydd";
        try {
            res = new String(decrypt(hexStr2ByteArr(content)));
        } catch (Exception e) {
            LogUtil.w(e);
        }
        return res;
    }


    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    private static byte[] encrypt(byte[] arrB) throws Exception {
        return getEncodeCipher().doFinal(arrB);
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    private static byte[] decrypt(byte[] arrB) throws Exception {
        return getDecodeCipher().doFinal(arrB);
    }

    private static Cipher getEncodeCipher() throws Exception {
        Key key = getKey(KEY.getBytes());
        @SuppressLint("GetInstance") Cipher encryptCipher = Cipher.getInstance(DES);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher;
    }

    private static Cipher getDecodeCipher() throws Exception {
        Key key = getKey(KEY.getBytes());
        @SuppressLint("GetInstance") Cipher encryptCipher = Cipher.getInstance(DES);
        encryptCipher.init(Cipher.DECRYPT_MODE, key);
        return encryptCipher;
    }

    private static Key getKey(byte[] arrBTmp) {
        Security.addProvider(new SunJCE());
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        return new javax.crypto.spec.SecretKeySpec(arrB, "DES");
    }


    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (byte anArrB : arrB) {
            int intTmp = anArrB;
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */
    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

}
