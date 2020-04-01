package com.pingerx.rxnetgo.request.base

import io.reactivex.Flowable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @author Pinger
 * @since 18-10-18 下午3:20
 *
 * 默认的Retrofit的请求Service，可以直接调用GET和POST方法，节省代码成本
 * Retrofit的Service的方法上不能带有泛型，就是不能有未知类型，这里将原来的泛型改成了ResponseBody,自己主动去处理数据
 * 如果想使用Retrofit结合Gson自动解析数据，可以自定义Flowable,然后使用NetGo请求。
 *
 * JvmSuppressWildcards注解是为了解决Retrofit加载Kotlin中Any编译成了?识别不了的问题。
 *
 */
interface ApiService {

    /**
     * post异步请求
     *
     * @param url     服务器接口
     * @param headers 请求头
     * @param params  请求参数
     * @param body    请求体，为RequestBody对象
     */
    @POST
    @JvmSuppressWildcards
    fun postAsync(
            @Url url: String,
            @HeaderMap headers: Map<String, Any>,
            @QueryMap params: Map<String, Any>,
            @Body body: RequestBody?): Flowable<ResponseBody>


    /**
     * post同步请求
     *
     * @param url     服务器接口
     * @param headers 请求头
     * @param params  请求参数
     * @param body    请求体，为RequestBody对象
     */
    @POST
    @JvmSuppressWildcards
    fun postSync(
            @Url url: String,
            @HeaderMap headers: Map<String, Any>,
            @QueryMap params: Map<String, Any>,
            @Body body: RequestBody?): Call<ResponseBody>


    /**
     * get异步请求
     *
     * @param url     服务器接口
     * @param headers 请求头
     * @param params  参数
     */
    @GET
    @JvmSuppressWildcards
    fun getAsync(
            @Url url: String,
            @HeaderMap headers: Map<String, Any>,
            @QueryMap params: Map<String, Any>): Flowable<ResponseBody>


    /**
     * get同步请求
     *
     * @param url    服务器接口
     * @param params 参数
     */
    @GET
    @JvmSuppressWildcards
    fun getSync(
            @Url url: String,
            @HeaderMap headers: Map<String, Any>,
            @QueryMap params: Map<String, Any>): Call<ResponseBody>
}
