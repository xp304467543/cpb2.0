package com.fenghuang.caipiaobao.helper

import com.fenghuang.baselib.utils.SoftInputUtils
import com.pingerx.rxnetgo.RxNetGo

/**
 * MainActivity退出时的数据销毁工具助手
 * 销毁内存中的静态数据
 */
object DestroyHelper {

    /**
     * App退出，销毁数据
     */
    fun onDestroy() {
        // 取消所有网络请求的订阅
        RxNetGo.getInstance().dispose()

        // 清除软键盘中过滤的View
        SoftInputUtils.clearFilterView()
    }

}