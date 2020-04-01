package com.fenghuang.baselib.base.recycler

import com.fenghuang.baselib.base.basic.IMvpContract

/**
 * 列表Fragment MVP协议
 */
class BaseRecyclerContract {

    interface View : IMvpContract.View {

        /**
         * 清空数据
         */
        fun clear()

        /**
         * 往集合底端添加一条数据
         */
        fun <T> addItem(data: T?)

        /**
         * 移除一条数据
         */
        fun removeItem(position: Int)

        /**
         * 添加一个集合数据
         */
        fun <T> addAll(datas: List<T>?)

        /**
         * 指定某一个位置插入一条数据
         */
        fun <T> insertItem(position: Int, data: T?)

        /**
         * 展示第一屏内容
         * @param datas 加载第一屏的数据
         */
        fun <T> showContent(datas: List<T>?)

        /**
         * 展示一个数据
         */
        fun <T> showContent(data: T?)

        /**
         * 更新某一条数据
         */
        fun <T> updateItem(data: T?, position: Int)
    }


    interface Presenter<V : View> : IMvpContract.Presenter<V> {

        /**
         * 加载数据
         */
        fun loadData(page: Int)
    }

}