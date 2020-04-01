package com.fenghuang.baselib.web.utils;

/**
 * @ Author  QinTian
 * @ Date  2019/12/13- 13:40
 * @ Describe
 */

import android.annotation.SuppressLint;

import androidx.core.content.FileProvider;

/**
 * Android 7.0 禁止在应用外部公开 file:// URI，所以我们必须使用 content:// 替代
 */
@SuppressLint("Registered")
public class UploadFileProvider extends FileProvider {

}
