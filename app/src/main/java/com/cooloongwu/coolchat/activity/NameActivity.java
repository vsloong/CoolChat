package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.utils.ToastUtils;

/**
 * 用来起名字的，给群组起名或者修改好友的备注名
 */
public class NameActivity extends BaseActivity {

    public static int REQUEST_REMARK_NAME = 0x01;
    public static int REQUEST_CREATEGROUP = 0x02;
    public static int REQUEST_CHANGE_GROUP_NAME = 0x03;
    private EditText edit_name;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        initViews();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        switch (type) {
            case "remarkName":
                initToolbar("设置备注名");
                break;
            case "createGroup":
                initToolbar("创建群组");
                break;
            case "changeGroupName":
                initToolbar("修改群组名称");
                edit_name.setText(intent.getStringExtra("name"));
                break;
            default:
                break;
        }
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        edit_name = (EditText) findViewById(R.id.edit_name);
        final TextView text_num = (TextView) findViewById(R.id.text_num);

        edit_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = edit_name.getText();
                int maxLength = 20;
                int length = editable.length();//原字符串长度
                if (length > maxLength) {//如果原字符串长度大于最大长度
                    int selectEndIndex = Selection.getSelectionEnd(editable);//getSelectionEnd：获取光标结束的索引值
                    String str = editable.toString();//旧字符串
                    String newStr = str.substring(0, maxLength);//截取新字符串
                    edit_name.setText(newStr);
                    editable = edit_name.getText();
                    int newLength = editable.length();//新字符串长度
                    if (selectEndIndex > newLength) {//如果光标结束的索引值超过新字符串长度
                        selectEndIndex = editable.length();

                        ToastUtils.showShort(NameActivity.this, "最多只能输入" + maxLength + "个字哦");
                    }
                    Selection.setSelection(editable, selectEndIndex);//设置新光标所在的位置
                } else {
                    text_num.setText(s.length() + "/20");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                String name = edit_name.getText().toString().trim();
                if (name.isEmpty()) {
                    ToastUtils.showShort(NameActivity.this, "名字不可为空");
                } else {
                    returnName(name);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnName(String name) {
        switch (type) {
            case "remarkName":
                Intent intent = new Intent();
                intent.putExtra("name", name);
                setResult(REQUEST_REMARK_NAME, intent);
                finish();
                break;
            case "createGroup":
                ToastUtils.showShort(NameActivity.this, "名字为：" + name);
                break;
            case "changeGroupName":
                Intent changeNameIntent = new Intent();
                changeNameIntent.putExtra("name", name);
                setResult(REQUEST_CHANGE_GROUP_NAME, changeNameIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
