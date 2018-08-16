package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchBookIndexActivity extends FragmentActivity {

    //定义标题TextView控件
    private TextView tittle_tv;

    //定义其他变量
    private TabLayout tabLayout_shouye;
    private ViewPager viewPager_shouye;
    private List<String> strings = new ArrayList<String>();;
    private List<Fragment> fragments = new ArrayList<Fragment>();;

    //用户编号
    int user_id = -1;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局文件
        setContentView(R.layout.activity_my_favorite);
        //绑定标题TextView控件
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置标题
        tittle_tv.setText("馆藏资源搜索");
        //接收用户序号：user_id
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        //构建导航条数据
        initdate();
        //初始化控件
        initView();
    }
    //初始化控件
    private void initView(){
        tabLayout_shouye = (TabLayout)findViewById(R.id.tablayout_shouye);
        viewPager_shouye = (ViewPager)findViewById(R.id.viewpager_ShouYe);
        viewPager_shouye.setAdapter(new TabFragmentShouYeAdapter(fragments,strings,
                getSupportFragmentManager(),this));
        tabLayout_shouye.setupWithViewPager(viewPager_shouye);
        tabLayout_shouye.setTabTextColors(Color.GRAY,Color.RED);
    }
    //构建导航条数据
    private void initdate(){
        SearchFragment1 searchFragment1 = new SearchFragment1();
        fragments.add(searchFragment1.newInstance(user_id));
        strings.add("图书");
        SearchFragment2 searchFragment2 = new SearchFragment2();
        fragments.add(searchFragment2.newInstance(user_id));
        strings.add("期刊");
        SearchFragment3 fragment3 = new SearchFragment3();
        fragments.add(fragment3.newInstance(user_id));
        strings.add("报纸");
        SearchFragment4 fragment4 = new SearchFragment4();
        fragments.add(fragment4.newInstance(user_id));
        strings.add("学位论文");
        SearchFragment5 fragment5 = new SearchFragment5();
        fragments.add(fragment5.newInstance(user_id));
        strings.add("章节");
        SearchFragment6 fragment6 = new SearchFragment6();
        fragments.add(fragment6.newInstance(user_id));
        strings.add("光盘");
    }
}
