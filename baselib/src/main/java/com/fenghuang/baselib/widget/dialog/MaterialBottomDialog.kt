package com.fenghuang.baselib.widget.dialog

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 底部弹出的对话框基类，用于上层包裹，如果要使用，请设置[setContentView]方法
 */
class MaterialBottomDialog(context: Context) : BottomSheetDialog(context)