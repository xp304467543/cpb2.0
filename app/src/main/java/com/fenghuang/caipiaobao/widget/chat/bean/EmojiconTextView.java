/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fenghuang.caipiaobao.widget.chat.bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.fenghuang.caipiaobao.widget.chat.EmojiconHandler;


/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
@SuppressLint("AppCompatCustomView")
public class EmojiconTextView extends AppCompatTextView  {
    private int mEmojiconSize;
    private SpannableStringBuilder builder;

    public EmojiconTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mEmojiconSize = (int) getTextSize();

        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            builder = new SpannableStringBuilder(text);
            EmojiconHandler.handleCaiPiaoBaoEmoticon(getContext(), builder, 50);
            EmojiconHandler.handleFengHuangEmoticon(getContext(), builder, 50);
            text = builder;
        }
        super.setText(text, type);
    }


    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
        super.setText(getText());
    }

}
