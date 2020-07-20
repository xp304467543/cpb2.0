package com.fenghuang.caipiaobao.utils.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fenghuang.caipiaobao.R
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/15
 * @ Describe
 *
 */
class PushToast {
    private var mActivity: AppCompatActivity? = null
    private var mInstance: PushToast? = null
    private var mToast: Toast? = null
    private val SHOW = 1
    private val HIDE = 0
    private var mTN: Any? = null
    private var mShow: Method? = null
    private var mHide: Method? = null
    private var mViewFeild: Field? = null
    private val durationTime = 5 * 1000.toLong()

    fun getToastInstance(): PushToast? {
        if (mInstance == null) {
            mInstance = PushToast()
        }
        return mInstance
    }

    fun init(activity: AppCompatActivity?) {
        mActivity = activity
    }

    fun createToast(content: String?) {
        if (mActivity == null) {
            return
        }
        val inflater = mActivity?.layoutInflater //调用Activity的getLayoutInflater()
        //        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        val view: View? = inflater?.inflate(R.layout.header_toast, null) //加載layout下的布局
//        val llPushContent = view.findViewById(R.id.ll_push_content) as LinearLayout
        val tvContent = view?.findViewById(R.id.tvContent) as TextView
//        tvTitle.text = title
        tvContent.text = content
        mToast = Toast(mActivity)
        mToast?.view = view
        mToast?.duration = Toast.LENGTH_LONG
        mToast?.setGravity(Gravity.TOP, 0, 10)
        reflectEnableClick()
        reflectToast()
//        llPushContent.setOnClickListener {
//            val newsFlashId = params["InformationID"]
//            Router.newIntent(mActivity).to(NewsFlashDetailActivity::class.java).putString(IntentKey.NEWS_FLASH_ID, newsFlashId).launch()
//            handler.sendEmptyMessage(HIDE)
//        }
        if (mShow != null && mHide != null) {
            handler.sendEmptyMessage(SHOW)
        } else {
            mToast!!.show()
        }
    }

    private fun reflectEnableClick() {
        try {
            val mTN: Any?
            mTN = getField(mToast, "mTN")
            if (mTN != null) {
                val mParams = getField(mTN, "mParams")
                if (mParams != null
                        && mParams is WindowManager.LayoutParams) {
                    val params = mParams
                    //显示与隐藏动画
//                    params.windowAnimations = R.style.ClickToast;
                    //Toast可点击
                    params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                    //设置viewgroup宽高
                    params.width = WindowManager.LayoutParams.MATCH_PARENT //设置Toast宽度为屏幕宽度
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT //设置高度
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun getField(`object`: Any?, fieldName: String): Any? {
        val field: Field = `object`!!.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(`object`)
    }

    private val handler: Handler by lazy {
        return@lazy @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    SHOW -> {
                        this.sendEmptyMessageDelayed(HIDE, durationTime)
                        show()
                    }
                    HIDE -> hide()
                }
            }
        }
    }

    private fun reflectToast() {
        var field: Field? = null
        try {
            field = mToast!!.javaClass.getDeclaredField("mTN")
            field.isAccessible = true
            mTN = field.get(mToast)
            mShow = mTN!!.javaClass.getDeclaredMethod("show")
            mHide = mTN!!.javaClass.getDeclaredMethod("hide")
            mViewFeild = mTN!!.javaClass.getDeclaredField("mNextView")
            mViewFeild?.isAccessible = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e1: NoSuchMethodException) {
            e1.printStackTrace()
        }
    }

    fun show() {
        try {
            //android4.0以上就要以下处理
            val mNextViewField: Field? = mTN?.javaClass?.getDeclaredField("mNextView")
            mNextViewField?.isAccessible = true
            val inflate = mActivity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v: View? = mToast?.view
            mNextViewField?.set(mTN, v)
            val method: Method? = mTN?.javaClass?.getDeclaredMethod("show", null)
            method?.invoke(mTN, null)
            mShow?.invoke(mTN, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hide() {
        try {
            mHide?.invoke(mTN, null)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
        }
    }
}