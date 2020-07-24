package com.fenghuang.caipiaobao.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_act_success.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/24
 * @ Describe
 *
 */

class ActivityDialogSuccess : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_act_success)
        if (intent.getBooleanExtra("is_success",false)){
            tvMsgSu.text = "申请成功"
        }else tvMsgSu.text = "申请失败"
        tvMsgContent.text =  intent.getStringExtra("msgSuccess")?:"您的推广员申请已成功，马上开启\n赚钱之旅吧"
        btSuccessSure.setOnClickListener {
            finish()
        }
    }

}