package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;

public class CreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("群组");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        final EditText edit_group_name = (EditText) findViewById(R.id.edit_group_name);
        final TextView text_num = (TextView) findViewById(R.id.text_num);


        edit_group_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = edit_group_name.getText();
                int maxLength = 20;
                int length = editable.length();//原字符串长度
                if (length > maxLength) {//如果原字符串长度大于最大长度
                    int selectEndIndex = Selection.getSelectionEnd(editable);//getSelectionEnd：获取光标结束的索引值
                    String str = editable.toString();//旧字符串
                    String newStr = str.substring(0, maxLength);//截取新字符串
                    edit_group_name.setText(newStr);
                    editable = edit_group_name.getText();
                    int newLength = editable.length();//新字符串长度
                    if (selectEndIndex > newLength) {//如果光标结束的索引值超过新字符串长度
                        selectEndIndex = editable.length();
                        Toast.makeText(CreateGroupActivity.this, "最多只能输入" + maxLength + "个字哦", Toast.LENGTH_SHORT).show();
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

}
