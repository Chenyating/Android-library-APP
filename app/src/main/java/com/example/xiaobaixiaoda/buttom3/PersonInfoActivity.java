package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class PersonInfoActivity extends AppCompatActivity {

    //声明标题TextView控件
    private TextView tittle_tv;

    //声明修改用户密码和绑定社交账号线性布局控件
    private LinearLayout person_info_modify_password_linearlayout,person_info_lock_social_media_linearlayout;

    private TextView user_name_tv,user_student_no_tv,user_college_tv,user_class_tv,user_credit_level_tv;

    //声明Intent对象
    Intent intent;

    //用户信息集合
    HashMap<String,Object> user_info = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_person_info);
        //绑定界面标题TextView控件
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置界面标题
        tittle_tv.setText("个人信息");

        //接收用户信息集合
        intent = getIntent();
        user_info = (HashMap<String,Object>) intent.getSerializableExtra("user_info");
        Log.d("user_info",user_info.toString());
        //绑定TextView控件
        user_name_tv = (TextView)findViewById(R.id.user_name_tv);
        user_student_no_tv = (TextView)findViewById(R.id.user_student_no_tv);
        user_college_tv = (TextView)findViewById(R.id.user_college_tv);
        user_class_tv = (TextView)findViewById(R.id.user_class_tv);
        user_credit_level_tv = (TextView)findViewById(R.id.user_credit_level_tv);

        //设置TextView的值
        user_name_tv.setText(user_info.get("user_name").toString());
        user_student_no_tv.setText(user_info.get("user_student_no").toString());
        user_college_tv.setText(user_info.get("user_college").toString());
        user_class_tv.setText(user_info.get("user_class").toString());
        switch ((Integer) user_info.get("user_credit_level")){
            case 0:
                user_credit_level_tv.setText("极低");
                break;
            case 1:
                user_credit_level_tv.setText("低");
                break;
            case 2:
                user_credit_level_tv.setText("中等");
                break;
            case 3:
                user_credit_level_tv.setText("高");
                break;
            case 4:
                user_credit_level_tv.setText("极高");
                break;
            default:
                break;
        }

        //绑定修改用户密码线性布局控件
        person_info_modify_password_linearlayout = (LinearLayout)findViewById(R.id.person_info_modify_password_linearlayout);
        //为修改用户密码线性布局控件设置点击监听器
        person_info_modify_password_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonInfoActivity.this, "点击修改用户密码", Toast.LENGTH_SHORT).show();
                intent = new Intent(PersonInfoActivity.this,ModifyPasswordActivity.class);
                intent.putExtra("user_id",(Integer) user_info.get("user_id"));
                intent.putExtra("user_password",user_info.get("user_password").toString());
                startActivity(intent);
            }
        });

        //绑定 绑定社交账号线性布局控件
        person_info_lock_social_media_linearlayout = (LinearLayout)findViewById(R.id.person_info_lock_social_media_linearlayout);
        //为绑定社交账号线性布局控件设置点击监听器
        person_info_lock_social_media_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonInfoActivity.this, "点击绑定社交账号", Toast.LENGTH_SHORT).show();
                intent = new Intent(PersonInfoActivity.this,LockSocialMediaActivity.class);
                startActivity(intent);
            }
        });
    }
}
