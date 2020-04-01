package com.fenghuang.caipiaobao.widget.spanlite;

import android.text.SpannableStringBuilder;
import android.widget.TextView;

/**
 * @ Author  QinTian
 * @ Date  2020-02-20
 * @ Describe 建造者模式,用于组装完整spanBuild 为textView使用
 */
public class SpanLite {

    private TextView spanTv;
    private SpannableStringBuilder spanBuilder;

    private SpanLite() {
    }

    private SpanLite(TextView textView) {
        this.spanTv = textView;
        spanBuilder = new SpannableStringBuilder();
    }

    //加入环境
    public static SpanLite with(TextView spanTv) {
        return new SpanLite( spanTv );
    }

    //拼接
    public SpanLite append(SpannableStringBuilder spanBuilder) {
        this.spanBuilder.append( spanBuilder );
        return this;
    }

    //设置超级span，用来特殊处理
    public SpanLite setSpan(Object what, int start, int end, int flags) {

        int length = spanBuilder.length();

        if (end > length){
            end = length;
        }

        if (end >= start){
            Util.setSpan( spanBuilder, what, start, end, flags );
        }

        return this;
    }

    //生效
    public void active() {
        Util.setMovementMethod( spanTv );
        spanTv.setText(spanBuilder);
        Util.setHighlightColor( spanTv );
    }


}
