package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowServiceContentActivity extends AppCompatActivity {

    private WebView web_view;

    String html_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_show_service_content);

        //获取参数
        Intent intent = getIntent();
        html_name = intent.getStringExtra("html_name");

        //绑定WebView控件
        web_view = (WebView)findViewById(R.id.web_view);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.setWebViewClient(new WebViewClient());
        web_view.loadUrl("file:///android_asset/" + html_name);

    }
}
