package com.cooloongwu.coolchat.utils;

import android.content.DialogInterface;

/**
 * Created by CooLoongWu on 2016-12-1 19:06.
 */

public interface IMyDialogListener {
    //最右边的按钮，确定
    void onPositive(DialogInterface dialog);

    //右边第二个按钮，取消
    void onNegative(DialogInterface dialog);

    //最左边的按钮
    void onNeutral(DialogInterface dialog);

    void onCancel();


}
