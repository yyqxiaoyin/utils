package com.smallcake.utils

import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Md5Utils {
    //公盐(前缀+后缀)
    private val SALT_START = "a0bjd35ff4kk9t6"
    private val SALT_END = "h3m8sh3l3s5lls"

    //十六进制下数字到字符的映射数组
    private val hexDigits = arrayOf(
        "0", "1", "2", "3", "4",
        "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
    )

    /**
     *
     * @param source 需要加密的字符串
     * @return MD5加密字符串
     */
    fun encryptInfo(source: String): String? {
        // 需要加密字符串+公盐
        return md5(SALT_START + source + SALT_END)
    }

    /**
     * md5加密算法
     *
     * @param encryptStr
     * @return 32位大写
     */
    fun md5(encryptStr: String): String {
        try {
            // 创建具有指定算法名称的信息摘要
            val md = MessageDigest.getInstance("MD5")
            // 获取二进制
            val bytes = encryptStr.toByteArray()
            // 执行加密并获得加密的结果,结果为byte字节数组
            val results = md.digest(bytes)
            // 将得到的字节数组变成字符串返回
            val resultString = byteArrayToHexString(results)
            return resultString.toUpperCase(Locale.ROOT)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }
    /**
     * md5加密算法
     * @param encryptStr
     * @return 16位大写
     */
    fun encrypt16(encryptStr: String): String {
        return md5(encryptStr).substring(8, 24)
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @param字节数组
     * @return 十六进制字符串
     */
    private fun byteArrayToHexString(b: ByteArray): String {
        val resultSb = StringBuffer()
        for (i in b.indices) {
            resultSb.append(byteToHexString(b[i]))
        }
        return resultSb.toString()
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private fun byteToHexString(b: Byte): String? {
        var n = b.toInt()
        if (n < 0) n = 256 + n
        val d1 = n / 16
        val d2 = n % 16
        return hexDigits[d1] + hexDigits[d2]
    }

}

/**
 * Base64加密解密
 */
object Base64Utils {

    /**
     * BASE64加密
     * 你好啊！
     * 5L2g5aW95ZWK77yB
     */
    fun encryptBase64(key: String): String {
        return  String(Base64.encode(key.toByteArray(), Base64.DEFAULT))
    }
    private fun encryptBase64(key: ByteArray?): String {
        return  String(Base64.encode(key, Base64.DEFAULT))
    }

    /**
     * BASE64解密
     * 5L2g5aW95ZWK77yB
     * 你好啊！
     */
    fun decryptBase64(key: String): String {
        return String(Base64.decode(key, Base64.DEFAULT))
    }

    fun decryptBase64ByteArray(key: String):ByteArray {
        return Base64.decode(key, Base64.DEFAULT)
    }


    /**
     * 采用AES加密算法
     */
    private const val KEY_ALGORITHM = "AES"
    private const val CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"
    /**
     * AES 加密
     *
     * @param secretKey 加密密码，长度：16 或 32 个字符
     * @param data      待加密内容
     * @return 返回Base64转码后的加密数据
     */
    fun encryptAES(secretKey: String, data: String): String {
        try {
            // 创建AES秘钥
            val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), KEY_ALGORITHM)
            // 创建密码器
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val encryptByte = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            // 将加密以后的数据进行 Base64 编码
            return encryptBase64(encryptByte)
        } catch (e: Exception) {
            e.message?.let { Log.e("encrypt", it) }
        }
        return ""
    }

    /**
     * AES 解密
     *
     * @param secretKey  解密的密钥，长度：16 或 32 个字符
     * @param base64Data 加密的密文 Base64 字符串
     */
    fun decryptAES(secretKey: String, base64Data: String): String {
        try {
            val data: ByteArray = decryptBase64ByteArray(base64Data)
            // 创建AES秘钥
            val secretKeySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), KEY_ALGORITHM)
            // 创建密码器
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            // 执行解密操作
            val result = cipher.doFinal(data)
            return String(result, Charsets.UTF_8)
        } catch (e: java.lang.Exception) {
            e.message?.let { Log.e("decrypt", it) }
        }
        return ""
    }




}