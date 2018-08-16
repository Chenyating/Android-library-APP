package com.example.xiaobaixiaoda.buttom3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ReadBookOnlineActivity extends AppCompatActivity {

    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_read_book_online);
        //绑定WebView控件
        web_view = (WebView)findViewById(R.id.web_view);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.setWebViewClient(new WebViewClient());
        //http://lilancunwangzhefeng.duapp.com/H5ReadOnlineApp/index.php?isbn=123
        web_view.loadUrl("http://"+getString(R.string.sever_ip)+"/TJPUSever/H5ReadOnlineApp/index.php?isbn=123");
    }
}
