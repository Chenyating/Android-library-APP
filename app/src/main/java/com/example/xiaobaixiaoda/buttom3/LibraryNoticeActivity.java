package com.example.xiaobaixiaoda.buttom3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.ScrollView;
import android.widget.TextView;

public class LibraryNoticeActivity extends AppCompatActivity {

    //声明控件
    private TextView library_notice_tv,tittle_tv;
    private ScrollView scrollView;

    //定义屏幕宽高
    int screenW,screenH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局
        setContentView(R.layout.activity_library_notice);
        //绑定控件
        library_notice_tv = (TextView)findViewById(R.id.library_notice_tv);
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        //设置界面标题
        tittle_tv.setText("馆内须知");
        //获取屏幕宽高
        getScreenPixels();
        //获取须知内容
        String webLinkText = "" +
                "<h4>交通职业学院移动图书馆须知</h4>"+
                "<p>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了保护电子资源的知识产权，维护瑞英中学的声誉，也为了保证广大合法用户的正当权益，图书馆要求各使用单位和个人重视并遵守电子资源知识产权的有关规定。<br>" +
                "        1、不得使用任何网络下载工具批量下载图书馆购买、试用和自建的电子资源；<br>" +
                "        2、不得连续、系统、集中、批量地进行下载、浏览、检索数据库等操作；（一般数据库商将超过正常阅读速度的下载视为不正当使用数据库行为）；<br>" +
                "        3、不得将所获得的文献提供给校外人员，更不允许利用获得的文献资料进行非法牟利；<br>" +
                "        4、不得私自设置代理服务器阅读或下载电子资源；<br>" +
                "        5、严禁泄露给校外用户个人使用各种电子资源时的账号、密码，如出现账号被盗而造成电子资源的违规使用，读者将承担相应责任。" +
                "<br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;图书馆对违规者将视其情节轻重，予以相应的处罚，情节严重者，将报请学校予以纪律处分，由此而引起的法律上的一切后果由违规者自负。<br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请广大读者协助监督，如果发现违规行为，请向图书馆举报；如您在使用电子资源时有疑问，也请联系我们。<br>" +
                "<br>" +
                "        电话：022-83955156<br>" +
                "<br>" +
                "        特此通告！<br>" +
                "<br>"+
                "</p>";
        //设置内容显示
        library_notice_tv.setText(Html.fromHtml(webLinkText));
        //设置字体大小
        library_notice_tv.setTextSize(18);
    }

    /**
     * 获取屏幕的宽和高
     */
    public void getScreenPixels() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
    }
}
