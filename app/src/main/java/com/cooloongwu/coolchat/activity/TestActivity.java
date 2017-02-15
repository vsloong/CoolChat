package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.view.MyRadioButton;

public class TestActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private LinearLayout layout_linear;
    private int checkedId = -1;

    CheckBox checkbox111;
    CheckBox checkbox112;
    CheckBox checkbox113;
    CheckBox checkbox114;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        final MyRadioButton radio_btn1 = (MyRadioButton) findViewById(R.id.radio_btn1);
        layout_linear = (LinearLayout) findViewById(R.id.layout_linear);
        checkbox111 = (CheckBox) findViewById(R.id.checkbox111);
        checkbox112 = (CheckBox) findViewById(R.id.checkbox112);
        checkbox113 = (CheckBox) findViewById(R.id.checkbox113);
        checkbox114 = (CheckBox) findViewById(R.id.checkbox114);
        checkbox111.setOnCheckedChangeListener(this);
        checkbox112.setOnCheckedChangeListener(this);
        checkbox113.setOnCheckedChangeListener(this);
        checkbox114.setOnCheckedChangeListener(this);

        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox2);

        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radio_group1);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radio_group2);
        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.radio_group3);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.e("RadioGroup1中选择了" + checkedId);
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.e("RadioGroup2中选择了" + checkedId);
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.e("RadioGroup3中选择了" + checkedId);
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LogUtils.e("选择了" + checkBox1.getId());
                    radio_btn1.setChecked(true);
                } else {
                    LogUtils.e("未选择1");
//                    radio_btn1.toggle();
                    radio_btn1.setChecked(false);
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LogUtils.e("选择了" + checkBox2.getId());
                } else {
                    LogUtils.e("未选择2");
                }
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        LogUtils.e("子元素数量" + layout_linear.getChildCount());
        if (isChecked) {
            checkedId = buttonView.getId();
        } else {
            if (checkedId == buttonView.getId()) {
                checkedId = -1;
            }
        }
        //把其他checkbox的选中状态取消
        checkbox111.setChecked(checkbox111.getId() == checkedId);
        checkbox112.setChecked(checkbox112.getId() == checkedId);
        checkbox113.setChecked(checkbox113.getId() == checkedId);
        checkbox114.setChecked(checkbox114.getId() == checkedId);

        switch (buttonView.getId()) {
            case R.id.checkbox111:
                break;

            case R.id.checkbox112:
                break;

            case R.id.checkbox113:
                break;

            case R.id.checkbox114:
                break;

        }
    }
}
