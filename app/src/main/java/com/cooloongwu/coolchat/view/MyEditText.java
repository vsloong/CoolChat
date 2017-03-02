package com.cooloongwu.coolchat.view;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;

/**
 * 自定义的EditText，主要为了解决当键盘弹出时无法检测到onKeyDown事件
 * Created by CooLoongWu on 2017-3-2 18:06.
 */

public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_SOFT_LEFT) {
            LogUtils.e("dispatchKeyEventPreIme", "按下了返回键");
            if (mOnCancelInputLayout != null) {
                mOnCancelInputLayout.onCancelInputLayout();
            }
            return false;
        }
        return super.dispatchKeyEventPreIme(event);
    }

    private OnCancelInputLayout mOnCancelInputLayout;

    public void setOnCancelInputLayout(OnCancelInputLayout onCancelInputLayout) {
        this.mOnCancelInputLayout = onCancelInputLayout;
    }

    /**
     * 在这里定义一个接口 用于在输入框弹出(评论)的时候  点击back按键不响应onKeyDown  和 onKeyPressed 方法
     * 但是查询api  是可以在自定义的view里面走dispatchKeyEventPreIme这个方法的(这个方法的响应在软键盘的响应之前)
     * 所以当按下back的时候,肯定会优先走这个方法  所以在这里写一个回调接口,那么就可以在调用这个回调的时候触发我们需要
     * 响应的逻辑
     */
    public interface OnCancelInputLayout {
        void onCancelInputLayout();
    }
}
