package com.pingerx.rxnetgo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import com.pingerx.rxnetgo.RxNetGo
import com.pingerx.rxnetgo.model.HttpHeaders
import com.pingerx.rxnetgo.model.HttpParams
import okhttp3.*
import java.io.*
import java.net.URLConnection
import java.net.URLDecoder

/**
 * @author Pinger
 * @since 18-10-17 上午11:11
 */
object HttpUtils {


    private const val FILE_SUFFIX = ".tmpl"
    private const val DEFAULT_FILENAME = "rxnetgo_downfile$FILE_SUFFIX"
    private const val DEFAULT_FILEDIR = "Downloads"


    /**
     * 通用的拼接请求头
     */
    fun appendHeaders(builder: Request.Builder, headers: HttpHeaders): Request.Builder {
        if (headers.getHeaderParams().isEmpty()) return builder
        val headerBuilder = Headers.Builder()
        try {
            for ((key) in headers.getHeaderParams()) {
                //对头信息进行 utf-8 编码,防止头信息传中文,这里暂时不编码,可能出现未知问题,如有需要自行编码
                headerBuilder.add(key, key)
            }
        } catch (e: Exception) {
            NetLogger.printStackTrace(e)
        }

        builder.headers(headerBuilder.build())
        return builder
    }


    fun createDownloadFile(path: String?, name: String?, isDelete: Boolean = true): File {
        val fileName = if (TextUtils.isEmpty(name)) {
            DEFAULT_FILENAME
        } else name

        val fileDir = if (TextUtils.isEmpty(path)) {
            getDownloadPath()
        } else path

        val file = File(fileDir, fileName)
        if (file.exists() && isDelete) {
            file.delete()
        }

        return file
    }

    fun getDownloadPath(): String {
        val context = RxNetGo.getInstance().getContext()
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val file = File(Environment.getExternalStorageDirectory().absolutePath
                    + File.separator + context.packageName)
            if (!file.exists()) {
                file.mkdir()
            }
            file.absolutePath
        } else {
            val file = File(context.getExternalFilesDir(null)?.absolutePath
                    ?: context.cacheDir.absolutePath
                    + File.separator + DEFAULT_FILEDIR)
            if (!file.exists()) {
                file.mkdirs()
            }
            file.absolutePath
        }
    }

    /**
     * 根据响应头或者url获取文件名
     */
    fun getNetFileName(response: Response, url: String): String {
        var fileName = getHeaderFileName(response)
        if (TextUtils.isEmpty(fileName)) fileName = getUrlFileName(url)
        if (TextUtils.isEmpty(fileName)) fileName = "unknownfile_" + System.currentTimeMillis()
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            NetLogger.printStackTrace(e)
        }

        return fileName!!
    }

    /**
     * 解析文件头
     * Content-Disposition:attachment;filename=FileName.txt
     * Content-Disposition: attachment; filename*="UTF-8''%E6%9B%BF%E6%8D%A2%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A.pdf"
     */
    private fun getHeaderFileName(response: Response): String? {
        var dispositionHeader = response.header(HttpHeaders.HEAD_KEY_CONTENT_DISPOSITION)
        if (dispositionHeader != null) {
            //文件名可能包含双引号，需要去除
            dispositionHeader = dispositionHeader.replace("\"".toRegex(), "")
            var split = "filename="
            var indexOf = dispositionHeader.indexOf(split)
            if (indexOf != -1) {
                return dispositionHeader.substring(indexOf + split.length, dispositionHeader.length)
            }
            split = "filename*="
            indexOf = dispositionHeader.indexOf(split)
            if (indexOf != -1) {
                var fileName = dispositionHeader.substring(indexOf + split.length, dispositionHeader.length)
                val encode = "UTF-8''"
                if (fileName.startsWith(encode)) {
                    fileName = fileName.substring(encode.length, fileName.length)
                }
                return fileName
            }
        }
        return null
    }

    /**
     * 通过 ‘？’ 和 ‘/’ 判断文件名
     * http://mavin-manzhan.oss-cn-hangzhou.aliyuncs.com/1486631099150286149.jpg?x-oss-process=image/watermark,image_d2F0ZXJtYXJrXzIwMF81MC5wbmc
     */
    private fun getUrlFileName(url: String): String? {
        var filename: String? = null
        val strings = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (string in strings) {
            if (string.contains("?")) {
                val endIndex = string.indexOf("?")
                if (endIndex != -1) {
                    filename = string.substring(0, endIndex)
                    return filename
                }
            }
        }
        if (strings.isNotEmpty()) {
            filename = strings[strings.size - 1]
        }
        return filename
    }

    /**
     * 根据路径删除文件
     */
    fun deleteFile(path: String): Boolean {
        if (TextUtils.isEmpty(path)) return true
        val file = File(path)
        if (!file.exists()) return true
        if (file.isFile) {
            val delete = file.delete()
            NetLogger.e("deleteFile:$delete path:$path")
            return delete
        }
        return false
    }

    /**
     * 根据文件名获取MIME类型
     */
    fun guessMimeType(fileName: String): MediaType? {
        var file = fileName
        val fileNameMap = URLConnection.getFileNameMap()
        file = file.replace("#", "")   //解决文件名中含有#号异常的问题
        val contentType = fileNameMap.getContentTypeFor(file)
                ?: return HttpParams.MEDIA_TYPE_STREAM
        return MediaType.parse(contentType)
    }

    fun <T> checkNotNull(any: T?, message: String) {
        if (any == null) {
            throw NullPointerException(message)
        }
    }


    fun toByteArray(input: Any): ByteArray? {
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(input)
            oos.flush()
            return baos.toByteArray()
        } catch (e: IOException) {
            NetLogger.printStackTrace(e)
        } finally {
            closeQuietly(oos)
            closeQuietly(baos)
        }
        return null
    }


    private fun closeQuietly(closeable: Closeable?) {
        if (closeable == null) return
        try {
            closeable.close()
        } catch (e: Exception) {
            NetLogger.printStackTrace(e)
        }

    }

    /**
     * 网络是否连接
     */
    fun isNetConnected(context: Context): Boolean {
        val manager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo.isConnected
    }


    /**
     * 拼接请求参数
     */
    fun appendUrlParams(url: String, params: Map<String, Any>?): String {
        var path = url
        if (params != null && params.isNotEmpty()) {
            val builder = Uri.parse(path).buildUpon()
            for ((key, value) in params) {
                builder.appendQueryParameter(key, value.toString())
            }
            path = builder.build().toString()
        }
        return path
    }


    /**
     * 生成类似表单的请求体
     */
    fun generateMultipartRequestBody(params: HttpParams, isMultipart: Boolean): RequestBody {
        if (params.getFileParams().isEmpty() && !isMultipart) {
            //表单提交，没有文件
            val bodyBuilder = FormBody.Builder()
            for (key in params.getUrlParams().keys) {
                bodyBuilder.addEncoded(key, params.getUrlParams()[key].toString())
            }
            return bodyBuilder.build()
        } else {
            //表单提交，有文件
            val multipartBodybuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
            //拼接键值对
            if (!params.getUrlParams().isEmpty()) {
                for ((key, value) in params.getUrlParams()) {
                    multipartBodybuilder.addFormDataPart(key, value.toString())
                }
            }
            //拼接文件
            for ((key, fileValues) in params.getFileParams()) {
                for (fileWrapper in fileValues) {
                    val fileBody = RequestBody.create(fileWrapper.contentType, fileWrapper.file)
                    multipartBodybuilder.addFormDataPart(key, fileWrapper.fileName, fileBody)
                }
            }
            return multipartBodybuilder.build()
        }
    }


    /**
     * 网络缓存的路径
     */
    fun getCacheFile(context: Context): File {
        return File(context.cacheDir.path + File.separator + "data-cache")
    }

    fun close(close: Closeable?) {
        if (close != null) {
            try {
                closeThrowException(close)
            } catch (ignored: IOException) {
                NetLogger.e(ignored)
            }
        }
    }

    @Throws(IOException::class)
    fun closeThrowException(close: Closeable?) {
        close?.close()
    }

}
