package com.pingerx.rxnetgo.request.base

import com.pingerx.rxnetgo.model.HttpParams
import com.pingerx.rxnetgo.utils.HttpUtils
import com.pingerx.rxnetgo.utils.JsonUtils
import io.reactivex.Flowable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * @author Pinger
 * @since 18-10-23 下午3:16
 *
 * 封装有请求体的请求，一般为post
 */
abstract class BodyRequest<T>(url: String, service: ApiService?, flowable: Flowable<T>?) : Request<T>(url, service, flowable), IBody<Request<T>> {

    private var mediaType: MediaType? = null        //上传的MIME类型
    private var content: String? = null             //上传的文本内容
    private var bs: ByteArray? = null                  //上传的字节数据
    private var file: File? = null                  //单纯的上传一个文件

    private var isMultipart = false  //是否强制使用 multipart/form-data 表单上传
    private var requestBody: RequestBody? = null


    override fun generateRequestBody(): RequestBody? {

        return when {
            // 自定义的请求体
            requestBody != null -> requestBody
            // 上传字符串数据
            content != null && mediaType != null -> RequestBody.create(mediaType, content!!)
            // 上传字节数组
            bs != null && mediaType != null -> RequestBody.create(mediaType, bs!!)
            // 上传一个文件
            file != null && mediaType != null -> RequestBody.create(mediaType, file!!)
            else -> HttpUtils.generateMultipartRequestBody(getParams(), isMultipart)
        }
    }


    override fun isMultipart(isMultipart: Boolean): Request<T> {
        this.isMultipart = isMultipart
        return this
    }

    override fun params(key: String, file: File): Request<T> {
        getParams().put(key, file)
        return this
    }

    override fun addFileParams(key: String, files: List<File>): Request<T> {
        getParams().putFileParams(key, files)
        return this
    }

    override fun addFileWrapperParams(key: String, fileWrappers: List<HttpParams.FileWrapper>): Request<T> {
        getParams().putFileWrapperParams(key, fileWrappers)
        return this
    }

    override fun params(key: String, file: File, fileName: String): Request<T> {
        getParams().put(key, file, fileName)
        return this
    }

    override fun params(key: String, file: File, fileName: String, contentType: MediaType): Request<T> {
        getParams().put(key, file, fileName, contentType)
        return this
    }

    override fun upRequestBody(requestBody: RequestBody): Request<T> {
        this.requestBody = requestBody
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upString(string: String): Request<T> {
        this.content = string
        this.mediaType = HttpParams.MEDIA_TYPE_PLAIN
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     * 该方法用于定制请求content-type
     */
    override fun upString(string: String, mediaType: MediaType): Request<T> {
        this.content = string
        this.mediaType = mediaType
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upJson(json: String): Request<T> {
        this.content = json
        this.mediaType = HttpParams.MEDIA_TYPE_JSON
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upJson(jsonObject: JSONObject): Request<T> {
        this.content = jsonObject.toString()
        this.mediaType = HttpParams.MEDIA_TYPE_JSON
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upJson(jsonArray: JSONArray): Request<T> {
        this.content = jsonArray.toString()
        this.mediaType = HttpParams.MEDIA_TYPE_JSON
        return this
    }

    override fun upJson(any: Any): Request<T> {
        this.content = JsonUtils.toJson(any)
        this.mediaType = HttpParams.MEDIA_TYPE_JSON
        return this
    }


    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upBytes(bs: ByteArray): Request<T> {
        this.bs = bs
        this.mediaType = HttpParams.MEDIA_TYPE_STREAM
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upBytes(bs: ByteArray, mediaType: MediaType): Request<T> {
        this.bs = bs
        this.mediaType = mediaType
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upFile(file: File): Request<T> {
        this.file = file
        this.mediaType = HttpUtils.guessMimeType(file.name)
        return this
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    override fun upFile(file: File, mediaType: MediaType): Request<T> {
        this.file = file
        this.mediaType = mediaType
        return this
    }

}
