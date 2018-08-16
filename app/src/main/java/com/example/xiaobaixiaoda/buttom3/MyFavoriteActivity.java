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

public class MyFavoriteActivity extends FragmentActivity {

    //定义标题TextView控件
    private TextView tittle_tv;

    //定义其他变量
    private TabLayout tabLayout_shouye;
    private ViewPager viewPager_shouye;
    private List<String> strings = new ArrayList<String>();;
    private List<Fragment> fragments = new ArrayList<Fragment>();

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
        tittle_tv.setText("我的收藏");
        //接收用户序号：user_id
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        //构建导航条数据
        fragments = initdate();
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
    private ArrayList<Fragment> initdate(){
        ArrayList<Fragment> fragments1 = new ArrayList<Fragment>();
        //Fragment1 fragment1 = new Fragment1();
        fragments1.add(Fragment1.newInstance(user_id));
        strings.add("图书");

        //Fragment2 fragment2 = new Fragment2();
        fragments1.add(Fragment2.newInstance(user_id));
        strings.add("期刊");

        //Fragment3 fragment3 = new Fragment3();
        fragments1.add(Fragment3.newInstance(user_id));
        strings.add("报纸");

        //Fragment4 fragment4 = new Fragment4();
        fragments1.add(Fragment4.newInstance(user_id));
        strings.add("学位论文");

        //Fragment5 fragment5 = new Fragment5();
        fragments1.add(Fragment5.newInstance(user_id));
        strings.add("章节");

        //Fragment6 fragment6 = new Fragment6();
        fragments1.add(Fragment6.newInstance(user_id));
        strings.add("光盘");
        return fragments1;
    }
}
