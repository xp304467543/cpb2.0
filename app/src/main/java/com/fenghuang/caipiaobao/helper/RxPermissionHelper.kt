package com.fenghuang.caipiaobao.helper

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.fenghuang.baselib.utils.AppUtils
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * 权限申请工具类
 */
object RxPermissionHelper {

    fun request(activity: FragmentActivity, vararg permissions: String): Disposable {
        return RxPermissions(activity).request(*permissions).subscribe()
    }

    fun request(fragment: Fragment, vararg permissions: String): Disposable {
        return RxPermissions(fragment).request(*permissions).subscribe()
    }

    fun request(activity: FragmentActivity, vararg permissions: String, onNext: Consumer<Boolean>): Disposable {
        return RxPermissions(activity).request(*permissions).subscribe(onNext)
    }

    fun request(fragment: Fragment, vararg permissions: String, onNext: Consumer<Boolean>): Disposable {
        return RxPermissions(fragment).request(*permissions).subscribe(onNext)
    }

    fun requestEach(activity: FragmentActivity, vararg permissions: String): Disposable {
        return RxPermissions(activity).requestEach(*permissions).subscribe()
    }

    fun requestEach(fragment: Fragment, vararg permissions: String): Disposable {
        return RxPermissions(fragment).requestEach(*permissions).subscribe()
    }

    fun requestEach(activity: FragmentActivity, vararg permissions: String, onNext: Consumer<Permission>): Disposable {
        return RxPermissions(activity).requestEach(*permissions).subscribe(onNext)
    }

    fun requestEach(fragment: Fragment, vararg permissions: String, onNext: Consumer<Permission>): Disposable {
        return RxPermissions(fragment).requestEach(*permissions).subscribe(onNext)
    }

    fun checkPermission(permission: String): Boolean {
        val perm = AppUtils.getContext().checkCallingOrSelfPermission(permission)
        return perm == PackageManager.PERMISSION_GRANTED
    }

}