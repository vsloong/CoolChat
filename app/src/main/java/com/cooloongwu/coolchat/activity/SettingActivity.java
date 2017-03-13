package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.Api;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.AppManager;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.utils.ToastUtils;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initToolbar();
        initView();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        TextView text_logout = (TextView) findViewById(R.id.text_logout);
        TextView text_check = (TextView) findViewById(R.id.text_check);
        text_logout.setOnClickListener(this);
        text_check.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_logout:
                logout();
                break;
            case R.id.text_check:
                checkUpdate();
                break;
            default:
                break;
        }
    }

    private void checkUpdate() {
        Api.getUpdateInfo(SettingActivity.this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e("检查更新", "成功：" + response.toString());
                try {
                    String versionName = response.getString("version");
                    String description = response.getString("description");
                    String apkUrl = response.getString("apkUrl");

                    if (!versionName.equals(AppConfig.getVersionName(SettingActivity.this))) {
                        showUpdateInfo("有更新啦", description, apkUrl);
                    } else {
                        new MaterialDialog.Builder(SettingActivity.this)
                                .title("温馨提示")
                                .content("当前已是最新版本")
                                .positiveText("确定")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShort(SettingActivity.this, "检查更新失败，稍后再试吧");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                LogUtils.e("检查更新", "取消");
            }

        });
    }

    private void showUpdateInfo(String title, String content, final String url) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .positiveText("立即更新")
                .negativeText("稍后更新")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        downloadApk(url);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void downloadApk(String url) {
        final String fileName = url.substring(url.lastIndexOf("/") + 1) + ".apk";//加.apk确保
        File fileDir = new File(AppConfig.FILE_SAVE_PATH);
        File filePath = new File(AppConfig.FILE_SAVE_PATH, fileName);
        if (filePath.exists()) {
            Api.installApk(SettingActivity.this, fileName);
            return;
        }
        showProgressDialog();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        Api.download(SettingActivity.this, url, new FileAsyncHttpResponseHandler(filePath) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                //showToast("下载失败，请稍后再试~");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progressDialog.dismiss();
                Api.installApk(SettingActivity.this, fileName);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int progress = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                LogUtils.e("下载进度>>>>>", progress + "%");
                progressDialog.setProgress(progress);
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new MaterialDialog.Builder(SettingActivity.this)
                .title("下载中...")
                .content("正在下载")
                .progress(false, 100, true)
                .progressNumberFormat("%1d/%2d")
                .canceledOnTouchOutside(false)
                .show();
    }

    private void logout() {
        //清除SharedPreferences的数据
        AppConfig.clearAllInfo(SettingActivity.this);
        //清除数据库的数据
        GreenDAOUtils.getInstance(SettingActivity.this).clearAllData();
        //打开登录页面
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        //删除其他的Activity
        AppManager.getInstance().finishAllActivityExcept(LoginActivity.class);
    }
}
