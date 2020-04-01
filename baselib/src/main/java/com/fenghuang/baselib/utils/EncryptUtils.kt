package com.fenghuang.baselib.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Base64
import android.util.Base64.DEFAULT
import java.lang.reflect.Method
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密解密工具类
 */
object EncryptUtils {

    // 密钥
    private const val secretKey = "caipiaobao@lx100$#365#$"
    // 向量
    private const val iv = "01234567"
    // 加解密统一使用的编码方式
    private val encoding = Charsets.UTF_8

    /**
     * SHA1加密Byte数据
     */
    fun encodeSHA1(source: ByteArray): ByteArray {
        try {
            val sha1Digest = MessageDigest.getInstance("SHA-1")
            sha1Digest.update(source)
            return sha1Digest.digest()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    /**
     * SHA1加密字符串数据
     */
    fun encodeSHA1(source: String): String {
        return byte2HexStr(encodeSHA1(source.toByteArray()))
    }


    /**
     * MD5加密字符串,32位长
     */
    fun encodeMD5(source: String?): String {
        if (TextUtils.isEmpty(source)) {
            return ""
        }

        try {
            // 获得MD5摘要算法的 MessageDigest对象
            val md5Digest = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            md5Digest.update(source?.toByteArray())
            // 获得密文
            return byte2HexStr(md5Digest.digest())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }


    /**
     * MD5解密，逆向加密
     */
    fun decodeMD5(source: String?): String {
        return encodeMD5(source)
    }


    /**
     * BASE64编码
     */
    fun encodeBASE64(source: String): String? {
        var clazz: Class<*>
        var encodeMethod: Method
        try {// 优先使用第三方库
            clazz = Class.forName("org.apache.commons.codec.binary.Base64")
            encodeMethod = clazz.getMethod("encodeBase64", ByteArray::class.java)
            // 反射方法 静态方法执行无需对象
            return String(encodeMethod.invoke(null, source.toByteArray()) as ByteArray)
        } catch (e: ClassNotFoundException) {
            val vm = System.getProperty("java.vm.name")
            try {
                return if ("Dalvik" == vm) {// Android
                    clazz = Class.forName("android.util.Base64")
                    // byte[] Base64.encode(byte[] input,int flags)
                    encodeMethod = clazz.getMethod("encode", ByteArray::class.java, Int::class.javaPrimitiveType)
                    String(encodeMethod.invoke(null, source.toByteArray(), 0) as ByteArray)
                } else {// JavaSE/JavaEE
                    clazz = Class.forName("sun.misc.BASE64Encoder")
                    encodeMethod = clazz.getMethod("encode", ByteArray::class.java)
                    encodeMethod.invoke(clazz.newInstance(), source.toByteArray()) as String
                }
            } catch (e1: ClassNotFoundException) {
                return null
            } catch (e1: Exception) {
                return null
            }

        } catch (e: Exception) {
            return null
        }

    }

    /**
     * BASE64解码
     */
    fun decodeBASE64(encodeSource: String): String? {
        var clazz: Class<*>?
        var decodeMethod: Method?

        try {// 优先使用第三方库
            clazz = Class.forName("org.apache.commons.codec.binary.Base64")
            decodeMethod = clazz!!.getMethod("decodeBase64", ByteArray::class.java)
            // 反射方法 静态方法执行无需对象
            return String(decodeMethod!!.invoke(null, encodeSource.toByteArray()) as ByteArray)
        } catch (e: ClassNotFoundException) {
            val vm = System.getProperty("java.vm.name")
            try {
                return if ("Dalvik" == vm) {// Android
                    clazz = Class.forName("android.util.Base64")
                    // byte[] Base64.decode(byte[] input, int flags)
                    decodeMethod = clazz!!.getMethod("decode", ByteArray::class.java, Int::class.javaPrimitiveType)
                    String(decodeMethod!!.invoke(null, encodeSource.toByteArray(), 0) as ByteArray)
                } else { // JavaSE/JavaEE
                    clazz = Class.forName("sun.misc.BASE64Decoder")
                    decodeMethod = clazz!!.getMethod("decodeBuffer", String::class.java)
                    String(decodeMethod!!.invoke(clazz.newInstance(), encodeSource) as ByteArray)
                }
            } catch (e1: ClassNotFoundException) {
                return null
            } catch (e1: Exception) {
                return null
            }

        } catch (e: Exception) {
            return null
        }

    }

    /**
     * AES加密
     */
    @SuppressLint("GetInstance")
    fun encodeAES(content: ByteArray, password: String): ByteArray {
        try {
            val encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding")// 创建密码器
            encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(password))// 初始化
            return encryptCipher.doFinal(content) // 加密
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * AES解密
     */
    @SuppressLint("GetInstance")
    fun decodeAES(content: ByteArray, password: String): ByteArray {
        try {
            val decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding")// 创建密码器
            decryptCipher.init(Cipher.DECRYPT_MODE, getKey(password))// 初始化
            return decryptCipher.doFinal(content) // 加密结果
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        // return null;
    }

    /**
     * AES字符串加密
     */
    fun encodeAES(content: String, password: String): String {
        return byte2HexStr(encodeAES(content.toByteArray(), password))
    }

    /**
     * AES字符串解密
     */
    fun decodeAES(content: String, password: String): String {
        return String(decodeAES(hexStr2Bytes(content), password))
    }


    /**
     * 3DES加密
     */
    fun encodeDES3(plainText: String): String {
        val desKey: Key
        var encryptData: ByteArray? = null
        try {
            val spec = DESedeKeySpec(secretKey.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("desede")
            desKey = keyFactory.generateSecret(spec)

            val cipher = Cipher.getInstance("desede/CBC/PKCS5Padding")
            val ips = IvParameterSpec(iv.toByteArray())
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips)
            encryptData = cipher.doFinal(plainText.toByteArray(charset(encoding.name())))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Base64.encodeToString(encryptData, DEFAULT)
    }

    /**
     * 3DES解密
     */
    fun decodeDES3(encryptText: String?): String {
        val deskey: Key
        val decryptData: ByteArray
        var result = ""
        if (null != encryptText && "" != encryptText) {
            try {
                val spec = DESedeKeySpec(secretKey.toByteArray())
                val keyfactory = SecretKeyFactory.getInstance("desede")
                deskey = keyfactory.generateSecret(spec)
                val cipher = Cipher.getInstance("desede/CBC/PKCS5Padding")
                val ips = IvParameterSpec(iv.toByteArray())
                cipher.init(Cipher.DECRYPT_MODE, deskey, ips)

                decryptData = cipher.doFinal(Base64.decode(encryptText, DEFAULT))
                result = String(decryptData, encoding)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return result
    }


    /**
     * 从指定字符串生成密钥
     */
    @Throws(NoSuchAlgorithmException::class)
    private fun getKey(password: String): Key {
        val secureRandom = SecureRandom(password.toByteArray())
        // 生成KEY
        val kgen = KeyGenerator.getInstance("AES")
        kgen.init(128, secureRandom)
        val secretKey = kgen.generateKey()
        val enCodeFormat = secretKey.encoded
        // 转换KEY
        return SecretKeySpec(enCodeFormat, "AES")
    }

    /**
     * 将byte数组转换为表示16进制值的字符串. 如：byte[]{8,18}转换为：0812 和 byte[]
     * hexStr2Bytes(String strIn) 互为可逆的转换过程.
     */
    private fun byte2HexStr(bytes: ByteArray): String {
        val bytesLen = bytes.size
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        val hexString = StringBuilder(bytesLen * 2)
        for (aByte in bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            val hex = Integer.toHexString(aByte.toInt() and 0xFF)
            if (hex.length < 2) {
                hexString.append(0)// 如果为1位 前面补个0
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

    /**
     * 将表示16进制值的字符串转换为byte数组, 和 String byte2HexStr(byte[] bytes) 互为可逆的转换过程.
     */
    private fun hexStr2Bytes(strIn: String): ByteArray {
        val arrB = strIn.toByteArray()
        val iLen = arrB.size

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        val arrOut = ByteArray(iLen / 2)
        var i = 0
        while (i < iLen) {
            val strTmp = String(arrB, i, 2)
            arrOut[i / 2] = Integer.parseInt(strTmp, 16).toByte()
            i += 2
        }
        return arrOut
    }

}
