package com.example.xiaobaixiaoda.buttom3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView student_number_at;
    private EditText password_edt;
    private ProgressBar login_progress;
    private View mLoginFormView;
    private LinearLayout login_ll;
    private Button login_in_button;
    private TextView login_tip;
    private Handler handler;
    //用户输入的学号
    String student_number;
    //用户输入的密码
    String password;
    //默认的用户编号，默认值为-1，代表登录失败
    int user_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_login);

        login_ll = (LinearLayout)findViewById(R.id.login_ll);
        //设置启动页图片的宽高适应屏幕
        scaleImage(this, login_ll, R.drawable.login_background2);

        //绑定控件
        student_number_at = (AutoCompleteTextView)findViewById(R.id.student_number_at);
        password_edt = (EditText)findViewById(R.id.password_edt);
        login_in_button = (Button)findViewById(R.id.login_in_button);
        login_progress = (ProgressBar)findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        login_tip = (TextView)findViewById(R.id.login_tip);

        //初始为空
        student_number_at.setText("");
        password_edt.setText("");

        //为登录按钮设置点击监听器
        login_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                //获取用户输入的学号
                student_number = student_number_at.getText().toString();
                //获取用户输入的密码
                password = password_edt.getText().toString();
                //判断学号是否为空
                if(!isEmptyStudentNumber(student_number,focusView))
                    return;
                //判断密码是否为空
                if(!isNumberStudentNumber(student_number, focusView))
                    return;;
                //判断学号是否为十位数字组成
                if(!isEmptyPassword(password,focusView))
                    return;
                //判断密码是否至少为6位
                if(!isPasswordLength(password,focusView))
                    return;
                //显示进度条
                mLoginFormView.setVisibility(View.GONE);
                login_progress.setVisibility(View.VISIBLE);
                login_tip.setVisibility(View.VISIBLE);
                //开启子线程。将学号与密码发送给服务器端，在服务器端完成验证后返回登录消息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取服务器返回的字符串
                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doLogin.php?student_number="+student_number+"&password="+password,"GET");
                        Log.d("URL","http://"+getString(R.string.sever_ip)+"/TJPUSever/doLogin.php?student_number="+student_number+"&password="+password);
                        Log.d("str.trim()的值：",str.trim());
                        Log.d("str.trim()的长度：",str.trim().length()+"");
                        Log.d("str的值：",str);
                        Log.d("str的长度：",str.length()+"");
                        //将str转化成数字
                        user_id = Integer.parseInt(str.trim());
                        //创建消息对象
                        Message message = new Message();
                        //为消息对象设置标识
                        message.what = 3;
                        //将消息对象发送给UI线程
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        //主线程接收子线程消息，处理登录结果
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 3){
                    if (user_id > 0){   //表示登录成功
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        finish();
                    }else{  //表示登录失败
                        mLoginFormView.setVisibility(View.VISIBLE);
                        login_progress.setVisibility(View.GONE);
                        login_tip.setVisibility(View.GONE);
                        student_number_at.setText("");
                        password_edt.setText("");
                        Toast.makeText(LoginActivity.this, "登录失败，请重新登录！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

    }

    //判断学号是否为空
    private boolean isEmptyStudentNumber(String student_number,View focusView){
        if (TextUtils.isEmpty(student_number)) {
            student_number_at.setError("学号不能为空！");
            focusView = student_number_at;
            return false;
        } else
            return true;
    }

    //判断密码是否为空
    private boolean isEmptyPassword(String password,View focusView){
        if (TextUtils.isEmpty(password)) {
            password_edt.setError("密码不能为空！");
            focusView = password_edt;
            return false;
        } else
            return true;
    }

    //判断学号是否为十位数字组成
    private boolean isNumberStudentNumber(String student_number,View focusView){
        String regex = "\\d{10}";
        if (Pattern.matches(regex,student_number))
            return true;
        else{
            student_number_at.setError("学号必须为十位数字！");
            focusView = student_number_at;
            return false;
        }
    }

    //判断密码是否至少为6位
    private boolean isPasswordLength(String password,View focusView){
        if (password.length() >= 6)
            return true;
        else{
            password_edt.setError("密码至少为6位！");
            focusView = password_edt;
            return false;
        }
    }

    public static void scaleImage(final Activity activity, final View view, int drawableResId) {

        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);

        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);

        if (resourceBitmap == null) {
            return;
        }

        // 开始对图片进行拉伸或者缩放

        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());

        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这里防止图像的重复创建，避免申请不必要的内存空间
                if (scaledBitmap.isRecycled())
                    //必须返回true
                    return true;


                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();


                // 计算将要裁剪的图片的顶部以及底部的偏移量
                int offset = Math.abs((scaledBitmap.getHeight() - viewHeight)) / 2;


                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, (offset+1), scaledBitmap.getWidth(),
                        scaledBitmap.getHeight() - offset * 2);


                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                    scaledBitmap.recycle();
                    System.gc();
                }


                // 设置图片显示
                view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
                return true;
            }
        });
    }
}

