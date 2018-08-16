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
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

public class GiveYourAdviceActivity extends AppCompatActivity {

    //声明控件
    private TextView tittle_cancel_tv,tittle_submit_tv;
    private MultiAutoCompleteTextView advice_multiAutoCompleteTextView;

    int user_id = -1;
    int flag = -1;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_give_your_advice);

        //获取user_id
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);

        //绑定意见反馈输入框
        advice_multiAutoCompleteTextView = (MultiAutoCompleteTextView)findViewById(R.id.advice_multiAutoCompleteTextView);

        //绑定取消按钮TextVIew控件
        tittle_cancel_tv = (TextView)findViewById(R.id.tittle_cancel_tv);

        //绑定提交按钮TextVIew控件
        tittle_submit_tv = (TextView)findViewById(R.id.tittle_submit_tv);

        //为取消按钮设置点击监听器
        tittle_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置提交按钮字体颜色为灰色
        tittle_submit_tv.setTextColor(Color.GRAY);

        //为意见反馈输入框设置内容变化监听器
        advice_multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    //设置保存按钮字体颜色为绿色
                    tittle_submit_tv.setTextColor(Color.rgb(51,153,0));
                    //为保存按钮设置点击监听器
                    tittle_submit_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(GiveYourAdviceActivity.this, ""+advice_multiAutoCompleteTextView.getText(), Toast.LENGTH_SHORT).show();
                            //开启子线程，提交意见反馈内容
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //获取消息字符串
                                    String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getUserAdvice.php?user_id="+user_id+"&advice_content="+advice_multiAutoCompleteTextView.getText().toString(),"GET");
                                    Log.d("ADVICE_URL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getUserAdvice.php?user_id="+user_id+"&advice_content="+advice_multiAutoCompleteTextView.getText().toString());
                                    flag = Integer.valueOf(str);
                                    //创建消息对象
                                    Message message = new Message();
                                    //为消息对象设置标识
                                    message.what = 6;
                                    //将消息对象发送给UI线程
                                    handler.sendMessage(message);
                                }
                            }).start();
                        }
                    });
                } else{
                    //设置保存按钮字体颜色为灰色
                    tittle_submit_tv.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //主线程接收子线程的消息，更新UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 6){
                    if (flag > 0){  //表示用户提交意见反馈成功
                        new AlertDialog.Builder(GiveYourAdviceActivity.this).setTitle("系统提示")//设置对话框标题
                                .setMessage("我们已经收到您的意见，真诚地感谢您的反馈！")//设置显示的内容
                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        // TODO Auto-generated method stub
                                        finish();
                                    }
                                }).show();//在按键响应事件中显示此对话框
                    }else{  //表示用户提交意见没有成功
                        Toast.makeText(GiveYourAdviceActivity.this, "提交失败，请重新提交", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
}
