package com.fenghuang.caipiaobao.data.bean

import com.google.gson.JsonElement

/**
 * 最上层的Api数据结构
 * data字段有可能返回null
 */
data class BaseApiBean(val code: Int, val msg: String, val time: String, val data: JsonElement?, val total: JsonElement?, val typeList: JsonElement?)