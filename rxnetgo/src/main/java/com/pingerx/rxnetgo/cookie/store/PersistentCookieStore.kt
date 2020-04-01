package com.pingerx.rxnetgo.cookie.store

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.pingerx.rxnetgo.cookie.SerializableCookie
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 持久存储的Cookie，使用Sp保存
 */

open class PersistentCookieStore(context: Context) : CookieStore {


    private val cookieMap: HashMap<String, ConcurrentHashMap<String, Cookie>> = HashMap()
    private val cookiePrefs: SharedPreferences = context.getSharedPreferences(COOKIE_PREFS, 0)
    override val cookies: List<Cookie>
        get() {
            val ret = ArrayList<Cookie>()
            for (key in cookieMap.keys)
                ret.addAll(cookieMap[key]!!.values)

            return ret
        }

    init {
        // Load any previously stored cookies into the store
        val prefsMap = cookiePrefs.all
        for ((key, value) in prefsMap) {
            if (value != null && !(value as String).startsWith(COOKIE_NAME_PREFIX)) {
                val cookieNames = TextUtils.split(value, ",")
                for (name in cookieNames) {
                    val encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null)
                    if (encodedCookie != null) {
                        val decodedCookie = decodeCookie(encodedCookie)
                        if (decodedCookie != null) {
                            if (!cookieMap.containsKey(key))
                                cookieMap[key] = ConcurrentHashMap()
                            cookieMap[key]!![name] = decodedCookie
                        }
                    }
                }

            }
        }
    }

    protected fun add(uri: HttpUrl, cookie: Cookie) {
        val name = getCookieToken(cookie)

        if (cookie.persistent()) {
            if (!cookieMap.containsKey(uri.host())) {
                cookieMap[uri.host()] = ConcurrentHashMap()
            }
            cookieMap[uri.host()]!![name] = cookie
        } else {
            if (cookieMap.containsKey(uri.host())) {
                cookieMap[uri.host()]!!.remove(name)
            } else {
                return
            }
        }

        // Save cookie into persistent store
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.putString(uri.host(), TextUtils.join(",", cookieMap[uri.host()]!!.keys))
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(SerializableCookie(uri.host(), cookie)))
        prefsWriter.apply()
    }

    private fun getCookieToken(cookie: Cookie): String {
        return cookie.name() + cookie.domain()
    }

    override fun add(uri: HttpUrl, cookie: List<Cookie>) {
        for (i in cookie) {
            add(uri, i)
        }
    }

    override fun get(uri: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        if (cookieMap.containsKey(uri.host())) {
            val cookies = this.cookieMap[uri.host()]!!.values
            for (cookie in cookies) {
                if (isCookieExpired(cookie)) {
                    remove(uri, cookie)
                } else {
                    ret.add(cookie)
                }
            }
        }

        return ret
    }

    override fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookieMap.clear()
        return true
    }

    override fun remove(uri: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)

        return if (cookieMap.containsKey(uri.host()) && cookieMap[uri.host()]!!.containsKey(name)) {
            cookieMap[uri.host()]!!.remove(name)

            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name)
            }
            prefsWriter.putString(uri.host(), TextUtils.join(",", cookieMap[uri.host()]!!.keys))
            prefsWriter.apply()

            true
        } else {
            false
        }
    }

    private fun encodeCookie(cookie: SerializableCookie?): String? {
        if (cookie == null)
            return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException in encodeCookie", e)
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as SerializableCookie).getCookie()
        } catch (e: IOException) {
            Log.d(LOG_TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e)
        }

        return cookie
    }

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element.toInt() and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    companion object {

        private const val LOG_TAG = "PersistentCookieStore"
        private const val COOKIE_PREFS = "CookiePrefsFile"
        private const val COOKIE_NAME_PREFIX = "cookie_"

        private fun isCookieExpired(cookie: Cookie): Boolean {
            return cookie.expiresAt() < System.currentTimeMillis()
        }
    }
}
