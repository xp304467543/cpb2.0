package com.pingerx.rxnetgo.cookie.store

import okhttp3.Cookie
import okhttp3.HttpUrl


interface CookieStore {

    val cookies: List<Cookie>

    fun add(uri: HttpUrl, cookie: List<Cookie>)

    operator fun get(uri: HttpUrl): List<Cookie>

    fun remove(uri: HttpUrl, cookie: Cookie): Boolean

    fun removeAll(): Boolean
}
