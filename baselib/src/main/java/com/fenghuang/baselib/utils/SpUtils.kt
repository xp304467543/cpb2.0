package com.fenghuang.baselib.utils

import android.content.Context
import android.content.SharedPreferences

object SpUtils {

    private var sharedPreferences: SharedPreferences? = null
    private val sp: SharedPreferences
        @Synchronized get() {
            if (sharedPreferences == null) {
                synchronized(SpUtils::class.java) {
                    if (sharedPreferences == null) {
                        sharedPreferences = AppUtils.getContext().getSharedPreferences(AppUtils.getAppInfo()?.packageName, Context.MODE_PRIVATE)
                    }
                }
            }
            return sharedPreferences!!
        }

    // =====================Boolean=====================
    fun putBoolean(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    @JvmOverloads
    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return sp.getBoolean(key, defValue)
    }

    // =====================String=====================
    fun putString(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String = ""): String? {
        return sp.getString(key, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>): Set<String>? {
        return sp.getStringSet(key, defValue)
    }

    fun setStringSet(key: String, values: Set<String>) {
        sp.edit().putStringSet(key, values).apply()
    }

    // =====================Int=====================
    fun putInt(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int = 0): Int {
        return sp.getInt(key, defValue)
    }

    // =====================Long=====================
    fun putLong(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long = 0): Long {
        return sp.getLong(key, defValue)
    }


    // =====================Float=====================
    fun putFloat(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, defValue: Float = 0f): Float {
        return sp.getFloat(key, defValue)
    }


    // =====================contains=====================
    operator fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    // =====================clear=====================
    fun clear(key: String): Boolean {
        return sp.edit().remove(key).commit()
    }

    // =====================clearAll=====================
    fun clearAll() {
        sp.edit().clear().apply()
    }

}