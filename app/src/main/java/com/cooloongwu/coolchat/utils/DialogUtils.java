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
                                          boolean outsideCancelable, boolean cancelable,
                                          final IMyDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(cancelable)
                .setPositiveButton(firstTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositive(dialog);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(secondTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNegative(dialog);
                        dialog.dismiss();
                    }
                }).setNeutralButton(thirdTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNeutral(dialog);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(outsideCancelable);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listener.onCancel();
            }
        });
        dialog.show();
        return dialog;
    }

}
