package com.pingerx.rxnetgo.model

import android.text.TextUtils
import com.pingerx.rxnetgo.utils.HttpUtils
import okhttp3.MediaType
import java.io.*
import java.util.*

/**
 * @author Pinger
 * @since 18-10-17 上午10:24
 *
 * 请求参数，用于包装发起请求携带的参数
 */
class HttpParams {

    companion object {
        val MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8")
        val MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8")
        val MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream")
    }


    /**
     * 普通的键值对参数
     */
    private var urlParamsMap: LinkedHashMap<String, Any> = LinkedHashMap()

    /**
     * 文件的键值对参数
     */
    private var fileParamsMap: LinkedHashMap<String, ArrayList<FileWrapper>> = LinkedHashMap()

    /**
     * 获取参数集合
     */
    fun getUrlParams(): Map<String, Any> = urlParamsMap

    /**
     * 获取文件集合
     */
    fun getFileParams(): Map<String, ArrayList<FileWrapper>> = fileParamsMap


    constructor() {
        clear()
    }

    constructor(key: String, value: String) {
        clear()
        put(key, value)
    }

    constructor(key: String, file: File) {
        clear()
        put(key, file)
    }


    fun put(params: HttpParams?) {
        if (params != null) {
            if (params.urlParamsMap.isNotEmpty())
                urlParamsMap.putAll(params.urlParamsMap)
            if (params.fileParamsMap.isNotEmpty())
                fileParamsMap.putAll(params.fileParamsMap)
        }
    }

    fun put(params: Map<String, String>?) {
        if (params == null || params.isEmpty()) return
        for ((key, value) in params) {
            put(key, value)
        }
    }


    fun put(key: String, any: Any?) {
        if (!TextUtils.isEmpty(key) && any != null) {
            urlParamsMap[key] = any
        }
    }

    fun put(key: String, fileWrapper: FileWrapper?) {
        if (!TextUtils.isEmpty(key) && fileWrapper != null) {
            put(key, fileWrapper.file, fileWrapper.fileName, fileWrapper.contentType)
        }
    }

    fun put(key: String, file: File, fileName: String) {
        put(key, file, fileName, HttpUtils.guessMimeType(fileName))
    }

    fun put(key: String, file: File, fileName: String, contentType: MediaType?) {
        var fileWrappers: ArrayList<FileWrapper>? = fileParamsMap[key]
        if (fileWrappers == null) {
            fileWrappers = arrayListOf()
            fileParamsMap[key] = fileWrappers
        }
        fileWrappers.add(FileWrapper(file, fileName, contentType))
    }

    fun putFileParams(key: String?, files: List<File>?) {
        if (key != null && files != null && files.isNotEmpty()) {
            for (file in files) {
                put(key, file)
            }
        }
    }

    fun putFileWrapperParams(key: String?, fileWrappers: List<FileWrapper>?) {
        if (key != null && fileWrappers != null && !fileWrappers.isEmpty()) {
            for (fileWrapper in fileWrappers) {
                put(key, fileWrapper)
            }
        }
    }

    fun removeUrl(key: String) {
        urlParamsMap.remove(key)
    }

    fun removeFile(key: String) {
        fileParamsMap.remove(key)
    }

    fun remove(key: String) {
        removeUrl(key)
        removeFile(key)
    }

    fun clear() {
        urlParamsMap.clear()
        fileParamsMap.clear()
    }

    /**
     * 文件类型的包装类
     */
    class FileWrapper(var file: File, var fileName: String, var contentType: MediaType?) : Serializable {

        var fileSize: Long = file.length()


        @Throws(IOException::class)
        private fun writeObject(out: ObjectOutputStream) {
            out.defaultWriteObject()
            out.writeObject(contentType.toString())
        }

        @Throws(IOException::class, ClassNotFoundException::class)
        private fun readObject(ois: ObjectInputStream) {
            ois.defaultReadObject()
            contentType = MediaType.parse(ois.readObject() as String)
        }

        override fun toString(): String {
            return "FileWrapper{" + //

                    "file=" + file + //

                    ", fileName=" + fileName + //

                    ", contentType=" + contentType + //

                    ", fileSize=" + fileSize +//

                    "}"
        }

        companion object {
            private const val serialVersionUID = -2356139899636767776L
        }
    }

    override fun toString(): String {
        val result = StringBuilder()
        for ((key, value) in urlParamsMap) {
            if (result.isNotEmpty()) result.append("&")
            result.append(key).append("=").append(value)
        }
        for ((key, value) in fileParamsMap) {
            if (result.isNotEmpty()) result.append("&")
            result.append(key).append("=").append(value)
        }
        return result.toString()
    }

}
