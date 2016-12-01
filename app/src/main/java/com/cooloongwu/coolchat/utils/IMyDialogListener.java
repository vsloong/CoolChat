package com.cooloongwu.coolchat.utils;

import android.content.DialogInterface;

/**
 * Created by CooLoongWu on 2016-12-1 19:06.
 */

public interface IMyDialogListener {
    public abstract void onFirst(DialogInterface dialog);

    public abstract void onSecond(DialogInterface dialog);

    public void onThird(DialogInterface dialog);


    public void onCancle();


}
