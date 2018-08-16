package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

public class ModifyPasswordActivity extends AppCompatActivity {

    //声明控件
    private TextView tittle_cancel_tv,tittle_save_tv;
    private EditText old_password_edt,new_password_edt;

    //用户序号
    int user_id = -1;

    //用户旧密码
    String user_password = "";

    //密码是否修改的标识，默认为-1 <0 修改不成功
    int flag = -1;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_modify_password);

        //接收用户序号user_id,user_password
        final Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        user_password = intent.getStringExtra("user_password");

        //绑定取消按钮控件
        tittle_cancel_tv = (TextView)findViewById(R.id.tittle_cancel_tv);

        //为取消按钮设置点击监听器
        tittle_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //绑定保存按钮控件
        tittle_save_tv = (TextView)findViewById(R.id.tittle_save_tv);

        //设置保存按钮字体颜色为灰色
        tittle_save_tv.setTextColor(Color.GRAY);

        //绑定旧密码输入框控件
        old_password_edt = (EditText)findViewById(R.id.old_password_edt);

        //绑定新密码输入框控件
        new_password_edt = (EditText)findViewById(R.id.new_password_edt);

        //为新密码输入框设置内容变化监听器
        new_password_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && !old_password_edt.getText().toString().equals("")){
                    //设置保存按钮字体颜色为绿色
                    tittle_save_tv.setTextColor(Color.rgb(51,153,0));
                    //为保存按钮设置点击监听器
                    tittle_save_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ModifyPasswordActivity.this, ""+new_password_edt.getText(), Toast.LENGTH_SHORT).show();
                            //获取旧密码
                            if (!old_password_edt.getText().toString().equals(user_password)){   //说明旧密码输入错误
                                Toast.makeText(ModifyPasswordActivity.this, "用户旧密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                                old_password_edt.setText("");
                                new_password_edt.setText("");
                                return;
                            }
                            //判断密码至少为6位
                            if (new_password_edt.getText().toString().length() < 6){
                                Toast.makeText(ModifyPasswordActivity.this, "用户密码至少为6位，请重新输入", Toast.LENGTH_SHORT).show();
                                old_password_edt.setText("");
                                new_password_edt.setText("");
                                return;
                            }
                            //开启子线程更新用户密码
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //获取消息字符串
                                    String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/changeUserPassword.php?user_id="+user_id+"&new_password="+new_password_edt.getText().toString(),"GET");
                                    flag = Integer.valueOf(str);
                                    //创建消息对象
                                    Message message = new Message();
                                    //为消息对象设置标识
                                    message.what = 5;
                                    //将消息对象发送给UI线程
                                    handler.sendMessage(message);
                                }
                            }).start();
                        }
                    });
                } else{
                    //设置保存按钮字体颜色为灰色
                    tittle_save_tv.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //主线程接收消息，更新UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 5){
                    if (flag > 0){  //表示用户更新密码成功
                        new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("系统提示")//设置对话框标题
                                .setMessage("密码更新成功，请重新登录！")//设置显示的内容
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        // TODO Auto-generated method stub
                                        startActivity(new Intent(ModifyPasswordActivity.this,LoginActivity.class));
                                    }
                                }).show();//在按键响应事件中显示此对话框
                    }else{  //表示更新密码没有成功
                        old_password_edt.setText("");
                        new_password_edt.setText("");
                    }
                }
            }
        };
    }
}
