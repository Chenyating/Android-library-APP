package com.example.xiaobaixiaoda.buttom3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LockSocialMediaActivity extends AppCompatActivity {

    //声明标题控件
    private TextView tittle_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_lock_social_media);
        //绑定界面标题TextView控件
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置界面标题
        tittle_tv.setText("账号绑定设置");
    }
}
