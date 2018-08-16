package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationBar mBottomNavigationBar;
    private ArrayList<Fragment>fragments;
    private TextView tittle_tv;
    Intent intent;
    int user_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        Log.d("MainAvtivity：user_id=",user_id+"");
        if (user_id < 0)
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        setContentView(R.layout.activity_main);
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("交通职业学院移动图书馆");
        assignViews();
    }

    //添加页面
    private void assignViews(){
        //添加标签的消息数量
        /*BadgeItem numberBadgeItem=new BadgeItem()
                .setBorderWidth(4)
                .setText("3")
                .setHideOnSelect(true);*/
        mBottomNavigationBar=(BottomNavigationBar)findViewById(R.id.bottom_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(android.R.color.black);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_launcher,"首页").setInActiveColorResource(android.R.color.white).setActiveColorResource(android.R.color.holo_red_light))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher,"书架").setInActiveColorResource(android.R.color.white).setActiveColorResource(android.R.color.holo_red_light))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher,"公告").setInActiveColorResource(android.R.color.white).setActiveColorResource(android.R.color.holo_red_light))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher,"我的").setInActiveColorResource(android.R.color.white).setActiveColorResource(android.R.color.holo_red_light))
                .setFirstSelectedPosition(0)
                .initialise();
        fragments=getFragments();   //获取Fragment集合
        setDefaultFragment();//设置默认Fragment
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {   //未选中 -> 选中
                if(fragments!=null){
                    if(position<fragments.size()){
                        FragmentManager fm=getSupportFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        Fragment fragment=fragments.get(position);
                        Log.d("位置",position+"");
                            ft.replace(R.id.root,fragment);
                        ft.commitAllowingStateLoss();
                    }
                }
            }

            @Override
            public void onTabUnselected(int position) {     //选中 -> 未选中
                if(fragments!=null){
                    FragmentManager fm=getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Fragment fragment=fragments.get(position);
                    ft.remove(fragment);
                    ft.commitAllowingStateLoss();
                }
            }

            @Override
            public void onTabReselected(int position) {     //选中 -> 选中

            }
        }); //设置监听

    }
    private void setDefaultFragment(){  //设置默认的Fragment
        FragmentManager fragmentManager=getSupportFragmentManager();  //获取FragmentManager
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();//开启一个事务
        fragmentTransaction.add(R.id.root, IndexFragment.newInstance(user_id));
        fragmentTransaction.commit();}

    private ArrayList<Fragment> getFragments(){     //创建Fragment集合
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(IndexFragment.newInstance(user_id));
        fragments.add(ShelfFragment.newInstance(user_id));
        fragments.add(NewsFragment.newInstance());
        fragments.add(MineFragment.newInstance(user_id));
        return fragments;}

}
