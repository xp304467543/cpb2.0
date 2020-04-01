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
package com.fenghuang.caipiaobao.widget.chat;

import android.content.Context;
import android.text.Spannable;
import android.text.style.DynamicDrawableSpan;
import android.util.SparseIntArray;

import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.widget.chat.bean.EmojiconSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public final class EmojiconHandler {
    /**
     * 解析其他表情
     *
     * @param context 上下文
     * @param spannable Spannable
     * @param fontSize 字体大小
     * @return
     */
    public static final Pattern CAIPIAOBAO = Pattern.compile("\\:(.*?)\\:");//" 自定义表情"
    public static final Pattern OTHER_RANGE = Pattern.compile("\\[e\\](.*?)\\[/e\\]");//"[e]xxx[/e] 自定义表情"
    public static final Pattern EMOJI_RANGE = Pattern.compile("[\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee]");
    public static final SparseIntArray sEmojisMap = new SparseIntArray(1029); //codepoint
    public static final SparseIntArray sSoftbanksMap = new SparseIntArray(471);//char
    public static Map<String, Integer> sEmojisModifiedMap = new HashMap<>();
    public static Map<String, Integer> caiPiaoBaoEmojisModifiedMap = new HashMap<>();
    public static Map<String, Integer> fengHuangjisModifiedMap = new HashMap<>();
    static {

        //凤凰表情
        fengHuangjisModifiedMap.put(":G1:",R.drawable.g1);
        fengHuangjisModifiedMap.put(":G2:",R.drawable.g2);
        fengHuangjisModifiedMap.put(":G3:",R.drawable.g3);
        fengHuangjisModifiedMap.put(":G4:",R.drawable.g4);
        fengHuangjisModifiedMap.put(":G5:",R.drawable.g5);
        fengHuangjisModifiedMap.put(":G6:",R.drawable.g6);
        fengHuangjisModifiedMap.put(":G7:",R.drawable.g7);
        fengHuangjisModifiedMap.put(":G8:",R.drawable.g8);
        fengHuangjisModifiedMap.put(":G9:",R.drawable.g9);
        fengHuangjisModifiedMap.put(":G10:",R.drawable.g10);
        fengHuangjisModifiedMap.put(":G11:",R.drawable.g11);
        fengHuangjisModifiedMap.put(":G12:",R.drawable.g12);
        fengHuangjisModifiedMap.put(":G13:",R.drawable.g13);
        fengHuangjisModifiedMap.put(":G14:",R.drawable.g14);
        fengHuangjisModifiedMap.put(":G15:",R.drawable.g15);
        fengHuangjisModifiedMap.put(":G16:",R.drawable.g16);
        fengHuangjisModifiedMap.put(":G17:",R.drawable.g17);
        fengHuangjisModifiedMap.put(":G18:",R.drawable.g18);
        fengHuangjisModifiedMap.put(":G19:",R.drawable.g19);
        fengHuangjisModifiedMap.put(":G20:",R.drawable.g20);
        fengHuangjisModifiedMap.put(":G21:",R.drawable.g21);
        fengHuangjisModifiedMap.put(":G22:",R.drawable.g22);
        fengHuangjisModifiedMap.put(":G23:",R.drawable.g23);
        fengHuangjisModifiedMap.put(":G24:",R.drawable.g24);
        fengHuangjisModifiedMap.put(":G25:",R.drawable.g25);
        fengHuangjisModifiedMap.put(":G26:",R.drawable.g26);
        fengHuangjisModifiedMap.put(":G27:",R.drawable.g27);
        fengHuangjisModifiedMap.put(":G28:",R.drawable.g28);
        fengHuangjisModifiedMap.put(":G29:",R.drawable.g29);
        fengHuangjisModifiedMap.put(":G30:",R.drawable.g30);
        fengHuangjisModifiedMap.put(":G31:",R.drawable.g31);
        fengHuangjisModifiedMap.put(":G32:",R.drawable.g32);
        fengHuangjisModifiedMap.put(":G33:",R.drawable.g33);
        fengHuangjisModifiedMap.put(":G34:",R.drawable.g34);
        fengHuangjisModifiedMap.put(":G35:",R.drawable.g35);
        fengHuangjisModifiedMap.put(":G36:",R.drawable.g36);
        fengHuangjisModifiedMap.put(":G37:",R.drawable.g37);
        fengHuangjisModifiedMap.put(":G38:",R.drawable.g38);


        //彩票宝表情
        caiPiaoBaoEmojisModifiedMap.put(":bowtie:", R.drawable.bowtie);
        caiPiaoBaoEmojisModifiedMap.put(":smiley:", R.drawable.smiley);
        caiPiaoBaoEmojisModifiedMap.put(":kissing_heart:", R.drawable.kissing_heart);
        caiPiaoBaoEmojisModifiedMap.put(":satisfied:", R.drawable.satisfied);
        caiPiaoBaoEmojisModifiedMap.put(":stuck_out_tongue_closed_eyes:", R.drawable.stuck_out_tongue_closed_eyes);
        caiPiaoBaoEmojisModifiedMap.put(":stuck_out_tongue:", R.drawable.stuck_out_tongue);

        caiPiaoBaoEmojisModifiedMap.put(":smile:", R.drawable.smile);
        caiPiaoBaoEmojisModifiedMap.put(":relaxed:", R.drawable.relaxed);
        caiPiaoBaoEmojisModifiedMap.put(":kissing_closed_eyes:", R.drawable.kissing_closed_eyes);
        caiPiaoBaoEmojisModifiedMap.put(":grin:", R.drawable.grin);
        caiPiaoBaoEmojisModifiedMap.put(":grinning:", R.drawable.grinning);
        caiPiaoBaoEmojisModifiedMap.put(":sleeping:", R.drawable.sleeping);

        caiPiaoBaoEmojisModifiedMap.put(":laughing:", R.drawable.laughing);
        caiPiaoBaoEmojisModifiedMap.put(":smirk:", R.drawable.smirk);
        caiPiaoBaoEmojisModifiedMap.put(":flushed:", R.drawable.flushed);
        caiPiaoBaoEmojisModifiedMap.put(":wink:", R.drawable.wink);
        caiPiaoBaoEmojisModifiedMap.put(":kissing:", R.drawable.kissing);
        caiPiaoBaoEmojisModifiedMap.put(":worried:", R.drawable.worried);

        caiPiaoBaoEmojisModifiedMap.put(":blush:", R.drawable.blush);
        caiPiaoBaoEmojisModifiedMap.put(":heart_eyes:", R.drawable.heart_eyes);
        caiPiaoBaoEmojisModifiedMap.put(":relieved:", R.drawable.relieved);
        caiPiaoBaoEmojisModifiedMap.put(":stuck_out_tongue_winking_eye:", R.drawable.stuck_out_tongue_winking_eye);
        caiPiaoBaoEmojisModifiedMap.put(":kissing_smiling_eyes:", R.drawable.kissing_smiling_eyes);
        caiPiaoBaoEmojisModifiedMap.put(":frowning:", R.drawable.frowning);
    }

    private EmojiconHandler() {
    }

    //判断是否是软银的emoji，软银的是char类型
    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }

    //根据iunicode
    private static int getEmojiByCodePoint(int codePoint) {
        return sEmojisMap.get(codePoint);
    }

    //根据char取软银的id
    private static int getEmojiByChar(char c) {
        return sSoftbanksMap.get(c);
    }

    /**
     * 处理所有表情
     *
     * @param context
     * @param spannable
     * @param size
     */

    public static Spannable handleAllEmoticon(Context context, Spannable spannable, int size) {
//        handleEmojiEmoticon(context, spannable, size);
        handleFengHuangEmoticon(context, spannable, size);
        handleCaiPiaoBaoEmoticon(context, spannable, size);
        return spannable;
    }

    public static void handleOtherEmoticon(Context context, Spannable spannable, int fontSize) {
        Matcher matcher = OTHER_RANGE.matcher(spannable.toString());
        while (matcher.find()) {
            String key = matcher.group();
            if (sEmojisModifiedMap.containsKey(key)) {
                int resId = sEmojisModifiedMap.get(key);
                spannable.setSpan(new EmojiconSpan(context, resId, fontSize, DynamicDrawableSpan.ALIGN_BASELINE, fontSize), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static void handleCaiPiaoBaoEmoticon(Context context, Spannable spannable, int fontSize) {

        Matcher matcher = CAIPIAOBAO.matcher(spannable.toString());
        while (matcher.find()) {
            String key = matcher.group();
            if (caiPiaoBaoEmojisModifiedMap.containsKey(key)) {
                int resId = caiPiaoBaoEmojisModifiedMap.get(key);
                EmojiconSpan emojiconSpan = new EmojiconSpan(context, resId, 50, DynamicDrawableSpan.ALIGN_BASELINE, fontSize);
                spannable.setSpan(emojiconSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static void handleFengHuangEmoticon(Context context, Spannable spannable, int fontSize) {
        Matcher matcher = CAIPIAOBAO.matcher(spannable.toString());
        while (matcher.find()) {
            String key = matcher.group();
            if (fengHuangjisModifiedMap.containsKey(key)) {
                int resId = fengHuangjisModifiedMap.get(key);
                spannable.setSpan(new EmojiconSpan(context, resId, 50, DynamicDrawableSpan.ALIGN_BASELINE, fontSize), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 获取图片的emoji
     *
     * @param unicode
     * @return
     */
    private static int getKeyCapEmoji(int unicode) {
        int icon = 0;
//        switch (unicode) {
//            case 0x0023:
//                icon = R.drawable.emoji_0023;
//                break;
//            case 0x002a:
//                icon = R.drawable.emoji_002a_20e3;
//                break;
//            case 0x0030:
//                icon = R.drawable.emoji_0030;
//                break;
//            case 0x0031:
//                icon = R.drawable.emoji_0031;
//                break;
//            case 0x0032:
//                icon = R.drawable.emoji_0032;
//                break;
//            case 0x0033:
//                icon = R.drawable.emoji_0033;
//                break;
//            case 0x0034:
//                icon = R.drawable.emoji_0034;
//                break;
//            case 0x0035:
//                icon = R.drawable.emoji_0035;
//                break;
//            case 0x0036:
//                icon = R.drawable.emoji_0036;
//                break;
//            case 0x0037:
//                icon = R.drawable.emoji_0037;
//                break;
//            case 0x0038:
//                icon = R.drawable.emoji_0038;
//                break;
//            case 0x0039:
//                icon = R.drawable.emoji_0039;
//                break;
//            default:
//                break;
//        }
        return icon;
    }


    public static void handleEmojiEmoticon(Context context, Spannable spannable, int fontSize) {
        Matcher matcher = EMOJI_RANGE.matcher(spannable.toString());
        while (matcher.find()) {
            int unicode = Character.codePointAt(matcher.group(), 0);
            int resId = sEmojisMap.get(unicode);
            if (resId <= 0) {
                String emojiHex = Integer.toHexString(unicode);//将unicode转化为string
                String resName = "emoji_" + emojiHex;
                resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
            }
            if (resId > 0) {
                spannable.setSpan(new EmojiconSpan(context, resId, fontSize, DynamicDrawableSpan.ALIGN_BASELINE, fontSize), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }


    //=============================================以下为emojiicon的处理方式，效率不高=====================================================================


    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, 0, -1, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param index
     * @param length
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, index, length, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, boolean useSystemDefault) {
        addEmojis(context, text, emojiSize, emojiAlignment, textSize, 0, -1, useSystemDefault);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param emojiSize
     * @param emojiAlignment
     * @param textSize
     * @param index
     * @param length
     * @param useSystemDefault
     */
    public static void addEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment, int textSize, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault) {
            return;
        }
        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length + index);//处理的长度

        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, textLength, EmojiconSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }

        int skip;
        for (int i = index; i < textLengthToProcess; i += skip) {
            skip = 0;
            int icon = 0;
            char c = text.charAt(i);

            //取出第一个字符判读是否是软银的
            if (isSoftBankEmoji(c)) {
                icon = getEmojiByChar(c);//取软银的
                skip = icon == 0 ? 0 : 1;
            }
            if (icon == 0) {
                int unicode = Character.codePointAt(text, i);
                skip = Character.charCount(unicode);
                if (unicode > 0xff) {
                    icon = getEmojiByCodePoint(unicode);//取默认的
                }

                if (i + skip < textLengthToProcess) {
                    int followUnicode = Character.codePointAt(text, i + skip);
                    //Non-spacing mark (Combining mark)
                    if (followUnicode == 0xfe0f) {
                        int followSkip = Character.charCount(followUnicode);
                        if (i + skip + followSkip < textLengthToProcess) {

                            int nextFollowUnicode = Character.codePointAt(text, i + skip + followSkip);
                            if (nextFollowUnicode == 0x20e3) {
                                int nextFollowSkip = Character.charCount(nextFollowUnicode);
                                int tempIcon = getKeyCapEmoji(unicode);

                                if (tempIcon == 0) {
                                    followSkip = 0;
                                    nextFollowSkip = 0;
                                } else {
                                    icon = tempIcon;
                                }
                                skip += (followSkip + nextFollowSkip);
                            }
                        }
                    } else if (followUnicode == 0x20e3) {
                        //some older versions of iOS don't use a combining character, instead it just goes straight to the second part
                        int followSkip = Character.charCount(followUnicode);
                        int tempIcon = getKeyCapEmoji(unicode);
                        if (tempIcon == 0) {
                            followSkip = 0;
                        } else {
                            icon = tempIcon;
                        }
                        skip += followSkip;

                    } else {

                        //handle other emoji modifiers
                        int followSkip = Character.charCount(followUnicode);

                        //TODO seems like we could do this for every emoji type rather than having that giant static map, maybe this is too slow?
                        String hexUnicode = Integer.toHexString(unicode);
                        String hexFollowUnicode = Integer.toHexString(followUnicode);

                        String resourceName = "emoji_" + hexUnicode + "_" + hexFollowUnicode;
                        int resourceId = 0;
                        if (sEmojisModifiedMap.containsKey(resourceName)) {
                            resourceId = sEmojisModifiedMap.get(resourceName);
                        } else {
                            resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getApplicationContext().getPackageName());
                            if (resourceId != 0) {
                                sEmojisModifiedMap.put(resourceName, resourceId);
                            }
                        }

                        if (resourceId == 0) {
                            followSkip = 0;
                        } else {
                            icon = resourceId;
                        }
                        skip += followSkip;
                    }
                }
            }

            if (icon > 0) {//设置span
                text.setSpan(new EmojiconSpan(context, icon, emojiSize, emojiAlignment, textSize), i, i + skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    public String getInsideString(String str, String strStart, String strEnd) {
        if (str.indexOf(strStart) < 0) {
            return "";
        }
        if (str.indexOf(strEnd) < 0) {
            return "";
        }
        return str.substring(str.indexOf(strStart) + strStart.length(), str.indexOf(strEnd));
    }

}
