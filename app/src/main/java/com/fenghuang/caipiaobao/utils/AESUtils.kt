package com.fenghuang.caipiaobao.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/26
 * @ Describe
 *
 */
object AESUtils {

    //AES/ECB/PKCS5Padding默认对应PHP则为：AES-128-ECB
    private const val CBC_PKCS5_PADDING = "AES/ECB/PKCS5Padding"

    private const val AES = "AES" //AES 加密


    //加密
    fun encrypt(key: String, cleartext: String): String? {
        return try {
            val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
            val keyspace = SecretKeySpec(key.toByteArray(), AES)
            cipher.init(Cipher.ENCRYPT_MODE, keyspace)
            val encrypted = cipher.doFinal(cleartext.toByteArray())
            Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //解密
    fun decrypt(key: String, encrypted: String?): String? {
        return try {
            val encrypted1 = Base64.decode(encrypted?.toByteArray(), Base64.DEFAULT)
            val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
            val keyspace = SecretKeySpec(key.toByteArray(), AES)
            cipher.init(Cipher.DECRYPT_MODE, keyspace)
            val original = cipher.doFinal(encrypted1)
            //转换为字符串
            String(original)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}