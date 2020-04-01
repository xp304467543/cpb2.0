package com.pingerx.rxnetgo.cookie

import com.pingerx.rxnetgo.cookie.store.CookieStore

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * 给OkHttp设置Cookie，这里提供了两种实现方法
 *
 *
 * 使用方法：
 *
 *
 * 方法一：使用sp保持cookie，如果cookie不过期，则一直有效
 * builder.cookieJar(new CookieJarImpl(new PersistentCookieStore(this)));
 *
 *
 * 方法二：使用内存保持cookie，app退出后，cookie消失
 * builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
 */

class CookieJarImpl(private val cookieStore: CookieStore) : CookieJar {


    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.add(url, cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url]
    }
}
