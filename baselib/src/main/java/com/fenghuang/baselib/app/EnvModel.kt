package com.fenghuang.baselib.app


/**
 * app的开发环境模式
 */
enum class EnvModel(internal var value: Int) {
    RELEASE(1),    // 线上模式
    BETA(2),       // 灰度模式
    TEST(3),       // 测试模式
    DEV(4)         // 开发模式
}