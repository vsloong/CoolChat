package com.cooloongwu.qupai;

import android.content.Intent;
import android.os.Bundle;

public class RecordResult {

    private final Bundle bundle;
    public static final String RESULT_KEY = "qupai.edit.result";
    public static final String XTRA_PATH = "path";
    public static final String XTRA_THUMBNAIL = "thumbnail";


    public RecordResult(Intent intent) {
        bundle = intent.getBundleExtra(RESULT_KEY);
    }

    public String getPath() {
        return bundle.getString(XTRA_PATH);
    }

    public String[] getThumbnail() {
        return bundle.getStringArray(XTRA_THUMBNAIL);
    }


    public static final String XTRA_DURATION = "duration";

    public long getDuration() {
        return bundle.getLong(XTRA_DURATION);
    }
}
