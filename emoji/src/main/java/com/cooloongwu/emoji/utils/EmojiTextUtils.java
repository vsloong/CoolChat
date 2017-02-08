package com.cooloongwu.emoji.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.cooloongwu.emoji.entity.Emoticons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditText显示表情
 * Created by CooLoongWu on 2017-2-8 16:17.
 */

public class EmojiTextUtils {

    private static final String EMOJI = "\\[(\\S+?)\\]";//emoji 表情

    public static SpannableStringBuilder getEditTextContent(String source, Context context, TextView textView) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);

        Pattern pattern = Pattern.compile(EMOJI);
        Matcher matcher = pattern.matcher(spannableStringBuilder);

        if (matcher.find()) {
            if (!(textView instanceof EditText)) {
                textView.setFocusable(false);
                textView.setClickable(false);
                textView.setLongClickable(false);
            }
            matcher.reset();
        }

        while (matcher.find()) {
            final String emoji = matcher.group();

            //emoji
            if (emoji != null) {
                int start = matcher.start();
                int end = start + emoji.length();
                String imgName = Emoticons.getEmojiName(emoji);
                if (!TextUtils.isEmpty(imgName)) {
                    int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
                    Drawable emojiDrawable = ContextCompat.getDrawable(context, resId);
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, DensityUtils.sp2px(context, 36), DensityUtils.sp2px(context, 36));
                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
                            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                                Drawable b = getDrawable();
                                canvas.save();
                                int transY = bottom - b.getBounds().bottom;
                                transY -= paint.getFontMetricsInt().descent / 2;
                                canvas.translate(x, transY);
                                b.draw(canvas);
                                canvas.restore();
                            }
                        };
                        spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

        }
        return spannableStringBuilder;
    }
}
