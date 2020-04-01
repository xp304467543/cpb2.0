package com.pingerx.rxnetgo.request.base

import com.pingerx.rxnetgo.model.HttpParams
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * @author Pinger
 * @since 18-10-23 下午6:37
 *
 * 请求体的相关行为定义，给实现类实现
 */
interface IBody<R> {

    fun isMultipart(isMultipart: Boolean): R

    fun upRequestBody(requestBody: RequestBody): R

    fun params(key: String, file: File): R

    fun addFileParams(key: String, files: List<File>): R

    fun addFileWrapperParams(key: String, fileWrappers: List<HttpParams.FileWrapper>): R

    fun params(key: String, file: File, fileName: String): R

    fun params(key: String, file: File, fileName: String, contentType: MediaType): R

    fun upString(string: String): R

    fun upString(string: String, mediaType: MediaType): R

    fun upJson(json: String): R

    fun upJson(jsonObject: JSONObject): R

    fun upJson(jsonArray: JSONArray): R

    fun upJson(any: Any): R

    fun upBytes(bs: ByteArray): R

    fun upBytes(bs: ByteArray, mediaType: MediaType): R

    fun upFile(file: File): R

    fun upFile(file: File, mediaType: MediaType): R
}
