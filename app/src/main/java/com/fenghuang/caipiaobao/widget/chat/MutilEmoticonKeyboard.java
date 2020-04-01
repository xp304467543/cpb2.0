package com.fenghuang.caipiaobao.widget.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.widget.chat.bean.EmoticonSet;
import java.util.ArrayList;
import java.util.List;

public class MutilEmoticonKeyboard extends LinearLayout implements EmoticonKeyboard.OnTabChangeLisenter, View.OnClickListener {

    private RadioGroup mIndexGroup;
    private EmoticonKeyboard mEmoticonKeyboard;
    private List<EmojiSetEntity> mList = new ArrayList<>();


    public MutilEmoticonKeyboard(Context context) {
        this(context, null);
    }

    public MutilEmoticonKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MutilEmoticonKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.include_mutil_emoji_panel, this);//映射
        initView();
        initEmoticonSet();

    }

    public void setupWithEditText(EditText editText) {
        mEmoticonKeyboard.setupWithEditText(editText);

    }

    public void initView() {
        mEmoticonKeyboard = this.findViewById(R.id.mpEmojiSingleKeyborad);
        mIndexGroup = this.findViewById(R.id.mpEmojiSingleKeyboradIndex);
        mEmoticonKeyboard.setOnTabChangeLisenter(this);
    }


    /**
     * 初始化索引
     *
     * @param context
     */
    private void initIndexView(Context context) {
        int[] drawable = new int[]{R.mipmap.emoji,R.mipmap.fh};
        RadioButton indexView;
        mIndexGroup.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
//            EmojiSetEntity entity = mList.get(i);
            indexView = new RadioButton(context);
            indexView.setGravity(Gravity.CENTER);
            Drawable noticeDrawable = getResources().getDrawable(drawable[i]);
            noticeDrawable.setBounds(0, 0, 80, 80);
            indexView.setCompoundDrawables(null, noticeDrawable, null, null);
            indexView.setTextSize(0);
            indexView.setButtonDrawable(null);
//            indexView.setTextColor(getResources().getColorStateList(R.color.radiobutton_textcolor));
            indexView.setBackgroundResource(R.drawable.selector_index_page);
//            indexView.setText(entity.getIndex());
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            100));

            indexView.setPadding(40, 20, 40, 20);
            indexView.setTag(i);
            mIndexGroup.addView(indexView,layoutParams);
            if (i == 0) {
                indexView.setChecked(true);
            }
            indexView.setOnClickListener(this);
        }


    }

    /**
     * 初始化表情集
     */
    private void initEmoticonSet() {
        mList.add(new EmojiSetEntity("默认", EmoticonSet.CAIPIAOBAO));
        mList.add(new EmojiSetEntity("凤凰", EmoticonSet.FENGHUANG));
        mEmoticonKeyboard.fillMutilEmoticon(mList);
        initIndexView(getContext());
    }


    @Override
    public void onTabChange(int tabPosition) {
        ((RadioButton) mIndexGroup.getChildAt(tabPosition)).setChecked(true);
    }


    @Override
    public void onClick(View v) {
        mEmoticonKeyboard.updateTabPosition((Integer) v.getTag());
    }
}
