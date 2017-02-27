package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.github.florent37.expectanim.ExpectAnim;

import static com.github.florent37.expectanim.core.Expectations.bottomOfParent;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.width;

public class TestActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        ImageView img = (ImageView) findViewById(R.id.img_avatar);
        ExpectAnim anim = new ExpectAnim()
                .expect(img)
                .toBe(
                        bottomOfParent().withMarginDp(16),
                        leftOfParent().withMarginDp(16),
                        width(40).toDp().keepRatio()
                )
                .toAnimation()
                .setDuration(2000)
                .start();
        anim.setPercent(20);
    }

}
