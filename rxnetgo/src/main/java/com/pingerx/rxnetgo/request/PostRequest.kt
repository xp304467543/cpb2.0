package com.pingerx.rxnetgo.request

import com.pingerx.rxnetgo.request.base.ApiService
import com.pingerx.rxnetgo.request.base.BodyRequest

import io.reactivex.Flowable

/**
 * @author Pinger
 * @since 18-10-23 下午3:16
 *
 * 构造Post请求，请求体封装请查看父类[BodyRequest]
 */
class PostRequest<T>(url: String, service: ApiService?, flowable: Flowable<T>?) : BodyRequest<T>(url, service, flowable) {

    override fun getMethod(): RequestType {
        return RequestType.POST
    }
}
