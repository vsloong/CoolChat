package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.Api;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.MyService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_phone;
    private EditText edit_password;

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                checkInput();
                break;
            default:
                break;
        }
    }

    private void checkInput() {
        String phone = edit_phone.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "手机号或者密码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Api.login(LoginActivity.this, phone, password, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                btn_login.setText("登录中...");
                btn_login.setClickable(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int status = response.getInt("status");
                    String msg = response.getString("msg");
                    switch (status) {
                        case -1:
                            showToast(msg);
                            btn_login.setText("登录");
                            btn_login.setClickable(true);
                            break;
                        case 0:
                            showToast(msg);
                            btn_login.setText("登录");
                            btn_login.setClickable(true);
                            break;
                        case 1:
                            AppConfig.setUserId(LoginActivity.this, response.getInt("userId"));
                            AppConfig.setUserName(LoginActivity.this, response.getString("name"));
                            AppConfig.setUserAvatar(LoginActivity.this, response.getString("avatar"));
                            AppConfig.setUserSex(LoginActivity.this, response.getString("sex"));
                            AppConfig.setQiniuToken(LoginActivity.this, response.getString("token"));
                            AppConfig.setUserLoginTime(LoginActivity.this, System.currentTimeMillis());

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                            Intent intent = new Intent(LoginActivity.this, MyService.class);
                            startService(intent);
                            break;
                        default:
                            showToast("未知错误");
                            btn_login.setText("登录");
                            btn_login.setClickable(true);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                btn_login.setText("登录");
                btn_login.setClickable(true);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
