package com.cooloongwu.coolchat.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * 一个简单的对话框的类
 * Created by CooLoongWu on 2016-12-1 18:54.
 */

public class DialogUtils {

    private static AlertDialog alertDialog;

    public static AlertDialog showMdAlert(Activity activity, String title, String msg,
                                          String firstTxt, String secondTxt, String thirdTxt,
                                          boolean outsideCancleable, boolean cancleable,
                                          final IMyDialogListener listener) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(cancleable)
                .setPositiveButton(firstTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onFirst(dialog);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(secondTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSecond(dialog);
                        dialog.dismiss();
                    }
                }).setNeutralButton(thirdTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onThird(dialog);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(outsideCancleable);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.onCancle();
            }
        });
        dialog.show();
        return dialog;
    }

}
