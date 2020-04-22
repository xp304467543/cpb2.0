package com.fenghuang.baselib.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * 与时间相关的工具类
 */
@SuppressLint("ConstantLocale")
object TimeUtils {

    private val formatYearMonthDay = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val formatMonthDay = SimpleDateFormat("M月d日", Locale.getDefault())
    private val formatHourMinute = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val formatHourAndYear = SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault())

    private const val minute = (60 * 1000).toLong()// 1分钟
    private const val hour = 60 * minute// 1小时
    private const val day = 24 * hour// 1天
    private const val month = 31 * day// 月
    private val year = 12 * month// 年


    private val formatYearMonthDayHourMinuteSecond = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())


    private val formatYearMonthDayHourMinute1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val formatYearMonthDayHourMinute = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    /**
     * 字符串格式化
     */
    fun initData(data: String): String {
        val date = formatYearMonthDayHourMinute1.parse(data)
        return formatYearMonthDayHourMinute1.format(date)
    }


    /**
     * 获取今天的日期，格式为 2019-01-10
     */
    fun getToday(): String {
        return getYearMonthDay(System.currentTimeMillis())
    }

    /**
     * 获取昨天的日期，格式为 2019-01-10
     */
    fun getYesterday(): String {
        var timeTemp = System.currentTimeMillis()
        //将当前判断时间加上一天，如果和今天处于同一天，则表示为昨天的时间
        timeTemp -= TimeUnit.DAYS.toMillis(1)
        return getYearMonthDay(timeTemp)
    }

    /**
     * 获取前天的日期，格式为 2019-01-10
     */
    fun getBeforeYesterday(): String {
        var timeTemp = System.currentTimeMillis()
        timeTemp -= TimeUnit.DAYS.toMillis(2)
        return getYearMonthDay(timeTemp)
    }

    /**
     * 获取小时和分钟，格式为23:02
     */
    fun getHourMinute(time: Long): String {
        return formatHourMinute.format(getDate(time))
    }

    /**
     * 获取月日的字符串，格式为 10月10日
     */
    fun getMonthDay(time: Long): String {
        return formatMonthDay.format(getDate(time))
    }

    /**
     * 获取年月日的字符串，格式为 2019-01-10
     */
    fun getYearMonthDay(time: Long): String {
        return formatYearMonthDay.format(getDate(time))
    }

    /**
     * 生成正式中文星期格式，格式为：周一
     */
    fun getDayOfWeek2(time: Long): String {
        return when (getDayOfWeek1(time)) {
            1 -> "周一"
            2 -> "周二"
            3 -> "周三"
            4 -> "周四"
            5 -> "周五"
            6 -> "周六"
            else -> "周日"
        }
    }

    /**
     * 获取星期几，格式为：1 - 7
     */
    fun getDayOfWeek1(time: Long): Int {
        val calendar = getCalendar(getDate(time))
        var result = calendar.get(Calendar.DAY_OF_WEEK)
        // 返回的星期数是按照周日为第一天的，所以需要转换成周一为第一天
        if (result == 1) {
            result = 7
        } else {
            result--
        }
        return result
    }


    /**
     * 判断时间是否为今天
     */
    fun isToday(time: Long): Boolean {
        return isSameDay(time, System.currentTimeMillis())
    }

    /**
     * 判断时间是否为昨天
     */
    fun isYesterday(time: Long): Boolean {
        var timeTemp = time
        //将当前判断时间加上一天，如果和今天处于同一天，则表示为昨天的时间
        timeTemp += TimeUnit.DAYS.toMillis(1)
        return isToday(timeTemp)
    }

    /**
     * 是否是明天
     */
    fun isTomorrow(time: Long): Boolean {
        var timeTemp = time
        timeTemp -= TimeUnit.DAYS.toMillis(1)
        return isToday(timeTemp)
    }


    /**
     * 判断时间是否为前天
     */
    fun isTheDayBeforeYesterday(time: Long): Boolean {
        var timeTemp = time
        timeTemp += TimeUnit.DAYS.toMillis(2)
        return isToday(timeTemp)
    }

    /**
     * 判断时间是否在本月
     */
    fun isCurrentMonth(time: Long): Boolean {
        return isSameYearAndMonth(time, System.currentTimeMillis())
    }

    /**
     * 判断时间是否在本年
     */
    fun isCurrentYear(time: Long): Boolean {
        return isSameYear(time, System.currentTimeMillis())
    }


    fun isSameDay(time1: Long, time2: Long): Boolean {
        return isSameDay(Date(time1), Date(time2))
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        return (getYear(date1) == getYear(date2)
                && getMonth(date1) == getMonth(date2)
                && getDay(date1) == getDay(date2))
    }

    fun isSameWeek(time1: Long, time2: Long): Boolean {
        return getWeekOfYear(Date(time1)) == getWeekOfYear(Date(time2))
    }

    fun isSameYearAndMonth(time1: Long, time2: Long): Boolean {
        val sameYear = isSameYear(time1, time2)
        val sameMonth = isSameMonth(time1, time2)
        return sameYear && sameMonth
    }

    fun isSameMonth(time1: Long, time2: Long): Boolean {
        return getMonth(Date(time1)) == getMonth(Date(time2))
    }

    fun isSameYear(time1: Long, time2: Long): Boolean {
        return getYear(Date(time1)) == getYear(Date(time2))
    }


    /**
     * 获取第一次安装app的时间
     */
    fun getFirstInstallTime(): Long {
        return AppUtils.getPackageInfo().firstInstallTime
    }


    /**
     * 获取安装app的天数
     */
    fun getInstalledDays(): Int {
        val installTime = getFirstInstallTime()
        val subTime = System.currentTimeMillis() - installTime
        val dayTime = 24 * 60 * 60 * 1000
        var dayNum = (subTime / dayTime).toInt()
        dayNum = if (subTime % dayTime == 0L) dayNum else dayNum + 1
        return dayNum
    }


    /**
     * 获取安装app的分钟数
     */
    fun getInstalledMinutes(): Int {
        val installTime = getFirstInstallTime()
        val subTime = System.currentTimeMillis() - installTime
        val dayTime = 60 * 1000
        var dayNum = (subTime / dayTime).toInt()
        dayNum = if (subTime % dayTime == 0L) dayNum else dayNum + 1
        return dayNum
    }


    /**
     * 获取安装app的小时数
     */
    fun getInstalledHours(): Int {
        val installTime = getFirstInstallTime()
        val subTime = System.currentTimeMillis() - installTime
        val dayTime = 60 * 60 * 1000
        var dayNum = (subTime / dayTime).toInt()
        dayNum = if (subTime % dayTime == 0L) dayNum else dayNum + 1
        return dayNum
    }

    /**
     * 获取毫秒级单位
     * @param time 秒级单位或者毫秒单位
     */
    fun getTimeByMillisecond(time: Long): Long {
        val millis = Calendar.getInstance().timeInMillis
        return if (!TextUtils.isEmpty(millis.toString())) {
            if (millis.toString().length == time.toString().length) {  // 毫秒级单位
                time
            } else {
                time * 1000
            }
        } else {
            time
        }
    }

    /**
     * 获取秒级单位
     * @param time 秒级单位或者毫秒单位
     */
    fun getTimeBySecond(time: Long): Long {
        val millis = Calendar.getInstance().timeInMillis
        return if (!TextUtils.isEmpty(millis.toString())) {
            if (millis.toString().length == time.toString().length) {  // 毫秒级单位
                time / 1000
            } else {
                time
            }
        } else {
            time
        }
    }


    private fun getCalendar(date: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    private fun getDate(time: Long): Date {
        return Date(time)
    }

    /**
     * 获取年份
     */
    private fun getYear(date: Date): Int {
        val calendar = getCalendar(date)
        return calendar.get(Calendar.YEAR)
    }

    /**
     * 获取月份
     */
    private fun getMonth(date: Date): Int {
        val calendar = getCalendar(date)
        // Calendar的月份是从0开始的
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 获取日
     */
    private fun getDay(date: Date): Int {
        val calendar = getCalendar(date)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }


    /**
     * 获取小时
     */
    private fun getHour(date: Date): Int {
        return getCalendar(date).get(Calendar.HOUR) + 1
    }

    /**
     * 获取分钟
     */
    private fun getMinute(date: Date): Int {
        return getCalendar(date).get(Calendar.MINUTE)
    }

    /**
     * 获取周数，既当前是一年中的第几周
     */
    private fun getWeekOfYear(date: Date): Int {
        val calendar = getCalendar(date)
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }

    /**
     * 根据两个相对的时间戳返回时间格式 78’88”
     * @param time 毫秒值
     */
    fun getMinuteSecond(time: Long): String {
        val seconds = time / 1000
        val minutesStr: String = if ((seconds / 60).toString().length == 1) "0${seconds / 60}" else "${seconds / 60}"
        val secondsStr = if ((seconds % 60).toString().length == 1) "0${seconds % 60}" else "${seconds % 60}"
        return "${minutesStr}’${secondsStr}”"
    }

    /**
     * 根据两个相对的时间戳返回时间格式 78’88”
     * @param time 秒值
     */
    fun getMinuteSecond2(time: Long): String {
        val seconds = getTimeBySecond(time)
        val minutesStr: String = if ((seconds / 60).toString().length == 1) "0${seconds / 60}" else "${seconds / 60}"
        val secondsStr = if ((seconds % 60).toString().length == 1) "0${seconds % 60}" else "${seconds % 60}"
        return "${minutesStr}’${secondsStr}”"
    }

    /**
     * 获取年月日的下一天
     * @param day 1，2，3往后的天数，-1-2-3往前的天数
     */
    fun getYearMonthNextDay(time: Long, day: Int): String {
        val calendar = getCalendar(Date(time))
        calendar.add(Calendar.DAY_OF_MONTH, day)
        return formatYearMonthDay.format(calendar.time)
    }

    /**
     * 年月日格式的时间转换成毫秒值，格式：2018-12-12
     */
    fun getMillisecondByDate(date: String): Long {
        return formatYearMonthDay.parse(date).time
    }

    /**
     * 返回小时和年份时间，格式：12:10  2019-03-01
     */
    fun getHourAndYear(time: Long): String {
        return formatHourAndYear.format(getDate(time))
    }

    /**
     * 返回根据自定义simpleDateFormat 转换的日历
     */
    fun getDateFormat(time: Long, dateFormat: SimpleDateFormat): String {
        return dateFormat.format(getDate(time))
    }

    /**
     * long 类型转换成日期
     */
    fun longToDateString(long: Long): String? {
        val date = Date(long * 1000)
        return formatYearMonthDayHourMinute1.format(date)
    }

    /**
     * long 类型转换成日期  只有月日 时分
     */
    private val formatMDSF = SimpleDateFormat("mm-dd HH:mm", Locale.getDefault())

    fun longToDateStringMDTime(long: Long): String? {
        val date = Date(long * 1000)
        return formatMDSF.format(date)
    }

    /**
     * long 类型转换成日期  只有时分
     */
    private val format = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun longToDateStringTime(long: Long): String? {
        val date = Date(long * 1000)
        return format.format(date)
    }

    /**
     * long 类型转换成日期  只有时分
     */
    private val formatHMS = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun longToDateStringTimeHMS(long: Long): String? {
        val date = Date(long * 1000)
        return formatHMS.format(date)
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    fun formatFriendly(time: Long): String {
        val date = getDate(time)
        val diff = Date().time - date.time
        var r: Long = 0
        when {
            diff > year -> {
                r = diff / year
                return longToDateString(time)!!
            }
            diff > month -> {
                r = diff / month
                return longToDateString(time)!!
            }
            diff > day -> {
                r = diff / day
                return r.toString() + "天前"
            }
            diff > hour -> {
                r = diff / hour
                return r.toString() + "个小时前"
            }
            diff > minute -> {
                r = diff / minute
                return r.toString() + "分钟前"
            }
            else -> return "刚刚"
        }

    }

    /*时间戳转换成月日时分秒*/
    fun getDateToHMSString(time: Long): String {
        val d = Date(time)
        val sf = SimpleDateFormat("HH:mm:ss")
        return sf.format(d)
    }
}