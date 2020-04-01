package com.pingerx.rxnetgo.cookie.store

import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*
import kotlin.collections.ArrayList

/**
 * 保存在内存的Cookie，退出应用就没了
 */

class MemoryCookieStore : CookieStore {

    private val allCookies = HashMap<String, ArrayList<Cookie>>()

    override val cookies: List<Cookie>
        get() {
            val cookies = ArrayList<Cookie>()
            val httpUrls = allCookies.keys
            for (url in httpUrls) {
                val cookie = allCookies[url]
                if (cookie != null) {
                    cookies.addAll(cookie)
                }
            }
            return cookies
        }

    override fun add(uri: HttpUrl, cookie: List<Cookie>) {
        val oldCookies = allCookies[uri.host()]

        if (oldCookies != null) {
            val itNew = cookies.iterator()
            val itOld = oldCookies.iterator()
            while (itNew.hasNext()) {
                val va = itNew.next().name()
                while (itOld.hasNext()) {
                    val v = itOld.next().name()
                    if (va == v) {
                        itOld.remove()
                    }
                }
            }
            oldCookies.addAll(cookies)
        } else {
            val cookies = arrayListOf<Cookie>()
            cookie.forEach {
                cookies.add(it)
            }
            allCookies[uri.host()] = cookies
        }


    }

    override fun get(uri: HttpUrl): List<Cookie> {
        var cookies = allCookies[uri.host()]
        if (cookies == null) {
            cookies = ArrayList()
            allCookies[uri.host()] = cookies
        }
        return cookies

    }

    override fun removeAll(): Boolean {
        allCookies.clear()
        return true
    }


    override fun remove(uri: HttpUrl, cookie: Cookie): Boolean {
        val cookies = allCookies[uri.host()]
        return cookies!!.remove(cookie)
    }

}
