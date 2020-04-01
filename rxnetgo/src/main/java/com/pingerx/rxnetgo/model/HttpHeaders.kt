package com.pingerx.rxnetgo.model

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import com.pingerx.rxnetgo.RxNetGo
import com.pingerx.rxnetgo.utils.NetLogger
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Pinger
 * @since 18-10-17 上午10:25
 *
 * 请求头对象，配置请求的请求头
 */
class HttpHeaders : Serializable {

    private var headersMap: LinkedHashMap<String, Any> = LinkedHashMap()

    /**
     * 获取请求头参数集合
     */
    fun getHeaderParams(): Map<String, Any> = headersMap

    constructor() {
        clear()
    }

    constructor(key: String, value: String) {
        clear()
        put(key, value)
    }

    fun put(key: String?, value: String?) {
        if (key != null && value != null) {
            headersMap[key] = value
        }
    }

    fun put(headers: HttpHeaders?) {
        if (headers != null) {
            if (headers.headersMap.isNotEmpty())
                headersMap.putAll(headers.headersMap)
        }
    }

    fun get(key: String): Any? {
        return headersMap[key]
    }

    fun remove(key: String): Any? {
        return headersMap.remove(key)
    }

    fun clear() {
        headersMap.clear()
    }

    fun toJSONString(): String {
        val jsonObject = JSONObject()
        try {
            for ((key, value) in headersMap) {
                jsonObject.put(key, value)
            }
        } catch (e: JSONException) {
            NetLogger.printStackTrace(e)
        }

        return jsonObject.toString()
    }

    override fun toString(): String {
        return "HttpHeaders{" + "headersMap=" + headersMap + '}'.toString()
    }

    @SuppressLint("PrivateApi")
    companion object {

        private const val serialVersionUID = 8458647755751403873L

        private const val FORMAT_HTTP_DATA = "EEE, dd MMM y HH:mm:ss 'GMT'"
        private val GMT_TIME_ZONE = TimeZone.getTimeZone("GMT")

        const val HEAD_KEY_RESPONSE_CODE = "ResponseCode"
        const val HEAD_KEY_RESPONSE_MESSAGE = "ResponseMessage"
        const val HEAD_KEY_ACCEPT = "Accept"
        const val HEAD_KEY_ACCEPT_ENCODING = "Accept-Encoding"
        const val HEAD_VALUE_ACCEPT_ENCODING = "gzip, deflate"
        const val HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language"
        const val HEAD_KEY_CONTENT_TYPE = "Content-Type"
        const val HEAD_KEY_CONTENT_LENGTH = "Content-Length"
        const val HEAD_KEY_CONTENT_ENCODING = "Content-Encoding"
        const val HEAD_KEY_CONTENT_DISPOSITION = "Content-Disposition"
        const val HEAD_KEY_CONTENT_RANGE = "Content-Range"
        const val HEAD_KEY_RANGE = "Range"
        const val HEAD_KEY_CACHE_CONTROL = "Cache-Control"
        const val HEAD_KEY_CONNECTION = "Connection"
        const val HEAD_VALUE_CONNECTION_KEEP_ALIVE = "keep-alive"
        const val HEAD_VALUE_CONNECTION_CLOSE = "close"
        const val HEAD_KEY_DATE = "Date"
        const val HEAD_KEY_EXPIRES = "Expires"
        const val HEAD_KEY_E_TAG = "ETag"
        const val HEAD_KEY_PRAGMA = "Pragma"
        const val HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since"
        const val HEAD_KEY_IF_NONE_MATCH = "If-None-Match"
        const val HEAD_KEY_LAST_MODIFIED = "Last-Modified"
        const val HEAD_KEY_LOCATION = "Location"
        const val HEAD_KEY_USER_AGENT = "User-Agent"
        const val HEAD_KEY_COOKIE = "Cookie"
        const val HEAD_KEY_COOKIE2 = "Cookie2"
        const val HEAD_KEY_SET_COOKIE = "Set-Cookie"
        const val HEAD_KEY_SET_COOKIE2 = "Set-Cookie2"

        /**
         * Accept-Language: zh-CN,zh;q=0.8
         */
        var acceptLanguage: String? = null
            get() {
                if (TextUtils.isEmpty(field)) {
                    val locale = Locale.getDefault()
                    val language = locale.language
                    val country = locale.country
                    val acceptLanguageBuilder = StringBuilder(language)
                    if (!TextUtils.isEmpty(country))
                        acceptLanguageBuilder.append('-').append(country).append(',').append(language).append(";q=0.8")
                    acceptLanguage = acceptLanguageBuilder.toString()
                    return field
                }
                return field
            }


        /**
         * User-Agent: Mozilla/5.0 (Linux; U; Android 5.0.2; zh-cn; Redmi Note 3 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36
         */
        // We have nothing to do
        // Add version
        // default to "1.0"
        // default to "en"
        // add the model for the release build
        var userAgent: String? = null
            get() {
                if (TextUtils.isEmpty(field)) {
                    var webUserAgent: String? = null
                    try {
                        val sysResCls = Class.forName("com.android.internal.R\$string")
                        val webUserAgentField = sysResCls.getDeclaredField("web_user_agent")
                        val resId = webUserAgentField.get(null) as Int
                        webUserAgent = RxNetGo.getInstance().getContext().getString(resId)
                    } catch (e: Exception) {
                    }

                    if (TextUtils.isEmpty(webUserAgent)) {
                        webUserAgent = "okhttp-netgo/pinger"
                    }

                    val locale = Locale.getDefault()
                    val buffer = StringBuffer()
                    val version = Build.VERSION.RELEASE
                    if (version.isNotEmpty()) {
                        buffer.append(version)
                    } else {
                        buffer.append("1.0")
                    }
                    buffer.append("; ")
                    val language = locale.language
                    if (language != null) {
                        buffer.append(language.toLowerCase(locale))
                        val country = locale.country
                        if (!TextUtils.isEmpty(country)) {
                            buffer.append("-")
                            buffer.append(country.toLowerCase(locale))
                        }
                    } else {
                        buffer.append("en")
                    }
                    if ("REL" == Build.VERSION.CODENAME) {
                        val model = Build.MODEL
                        if (model.isNotEmpty()) {
                            buffer.append("; ")
                            buffer.append(model)
                        }
                    }
                    val id = Build.ID
                    if (id.isNotEmpty()) {
                        buffer.append(" Build/")
                        buffer.append(id)
                    }
                    userAgent = String.format(webUserAgent!!, buffer, "Mobile ")
                    return field
                }
                return field
            }

        fun getDate(gmtTime: String): Long {
            return try {
                parseGMTToMillis(gmtTime)
            } catch (e: ParseException) {
                0
            }

        }

        fun getDate(milliseconds: Long): String {
            return formatMillisToGMT(milliseconds)
        }

        fun getExpiration(expiresTime: String): Long {
            return try {
                parseGMTToMillis(expiresTime)
            } catch (e: ParseException) {
                -1
            }

        }

        fun getLastModified(lastModified: String): Long {
            return try {
                parseGMTToMillis(lastModified)
            } catch (e: ParseException) {
                0
            }

        }

        fun getCacheControl(cacheControl: String?, pragma: String?): String? {
            // first http1.1, second http1.0
            return cacheControl ?: pragma
        }

        @Throws(ParseException::class)
        fun parseGMTToMillis(gmtTime: String): Long {
            if (TextUtils.isEmpty(gmtTime)) return 0
            val formatter = SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US)
            formatter.timeZone = GMT_TIME_ZONE
            val date = formatter.parse(gmtTime)
            return date.time
        }

        fun formatMillisToGMT(milliseconds: Long): String {
            val date = Date(milliseconds)
            val simpleDateFormat = SimpleDateFormat(FORMAT_HTTP_DATA, Locale.US)
            simpleDateFormat.timeZone = GMT_TIME_ZONE
            return simpleDateFormat.format(date)
        }
    }
}
