package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailQiKanInfoActivity extends AppCompatActivity {

    //声明控件
    private TextView tittle_tv;     //标题
    private TextView detail_qikan_info_name_tv;     //期刊名
    private TextView detail_oher_resource_info_author_tv;     //期刊作者
    private TextView detail_qikan_info_kanming_tv;     //期刊标题
    private TextView detail_qikan_info_date_tv;     //期刊出版日期
    private TextView detail_qikan_info_qihao_tv;     //期刊期号
    private TextView detail_qikan_info_location_tv;     //期刊馆藏位置
    private TextView detail_qikan_info_author_introduce_tv;     //期刊作者简介
    private TextView detail_qikan_info_source_tv;     //期刊来源
    private TextView detail_qikan_info_reading_online_tv;     //期刊在线阅读按钮
    private TextView detail_qikan_info_collect_tv;     //期刊加入收藏按钮

    //设置Intent对象
    Intent intent;

    //设置消息处理对象
    private Handler handler;

    //设置期刊信息集合
    private HashMap<String,Object> detailQikanInfoMap = new HashMap<>();

    //定义用户序号：user_id
    int user_id = -1;
    //定义期刊序号：qikan_id
    int qikan_id = -1;
    //取消收藏是否成功标志位
    int collect_flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_detail_qikan_info);
        //设置标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("期刊资源详细信息");

        intent = getIntent();
        //接收传递过来的用户序号
        user_id = intent.getIntExtra("user_id",-1);
        //接收传递过来的期刊序号
        qikan_id = intent.getIntExtra("qikan_id",-1);

        //开启子线程访问服务器，获取图书详细信息
        getQikanDetailInfo();

        //接收子线程的消息，更新主线程UI
        showBookDetailInfo();

    }

    //开启子线程访问服务器，获取图书详细信息
    private void getQikanDetailInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailQikanInfo.php?qikan_id="+qikan_id+"&user_id="+user_id,"GET");
                Log.d("DETAILBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailQikanInfo.php?qikan_id="+qikan_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("jsonArray",jsonArray.length()+"");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    detailQikanInfoMap.put("qikan_id",qikan_id);
                    detailQikanInfoMap.put("qikan_author",jsonObject.getString("qikan_author"));
                    detailQikanInfoMap.put("qikan_tittle",jsonObject.getString("qikan_tittle"));
                    detailQikanInfoMap.put("qikan_name",jsonObject.getString("qikan_name"));
                    detailQikanInfoMap.put("qikan_number",jsonObject.getString("qikan_number"));
                    detailQikanInfoMap.put("qikan_date",jsonObject.getString("qikan_date"));
                    detailQikanInfoMap.put("qikan_content",jsonObject.getString("qikan_content"));
                    detailQikanInfoMap.put("qikan_location",jsonObject.getString("qikan_location"));
                    detailQikanInfoMap.put("qikan_author_introduction",jsonObject.getString("qikan_author_introduction"));
                    detailQikanInfoMap.put("qikan_resource",jsonObject.getString("qikan_resource"));
                    detailQikanInfoMap.put("has_collected",jsonObject.getString("has_collected"));
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 15;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //绑定控件
    private void initWidget(){
        detail_qikan_info_name_tv = (TextView)findViewById(R.id.detail_qikan_info_name_tv);     //期刊名
        detail_oher_resource_info_author_tv = (TextView)findViewById(R.id.detail_oher_resource_info_author_tv);     //期刊作者
        detail_qikan_info_kanming_tv = (TextView)findViewById(R.id.detail_qikan_info_kanming_tv);     //期刊标题
        detail_qikan_info_date_tv = (TextView)findViewById(R.id.detail_qikan_info_date_tv);     //期刊出版日期
        detail_qikan_info_qihao_tv = (TextView)findViewById(R.id.detail_qikan_info_qihao_tv);     //期刊期号
        detail_qikan_info_location_tv = (TextView)findViewById(R.id.detail_qikan_info_location_tv);     //期刊馆藏位置
        detail_qikan_info_author_introduce_tv = (TextView)findViewById(R.id.detail_qikan_info_author_introduce_tv);     //期刊作者简介
        detail_qikan_info_source_tv = (TextView)findViewById(R.id.detail_qikan_info_source_tv);     //期刊来源
        detail_qikan_info_reading_online_tv = (TextView)findViewById(R.id.detail_qikan_info_reading_online_tv);     //期刊在线阅读按钮
        detail_qikan_info_collect_tv = (TextView)findViewById(R.id.detail_qikan_info_collect_tv);     //期刊加入收藏按钮
    }

    //主线程接收子线程消息，更新UI，显示信息
    private void showBookDetailInfo(){

        //绑定控件
        this.initWidget();

        //处理子线程消息
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 15){
                    //期刊名
                    detail_qikan_info_name_tv.setText(detailQikanInfoMap.get("qikan_name").toString());
                    //期刊作者
                    detail_oher_resource_info_author_tv.setText(detailQikanInfoMap.get("qikan_author").toString());
                    //期刊标题
                    detail_qikan_info_kanming_tv.setText(detailQikanInfoMap.get("qikan_tittle").toString());
                    //期刊出版日期
                    detail_qikan_info_date_tv.setText(detailQikanInfoMap.get("qikan_date").toString());
                    //期刊期号
                    detail_qikan_info_qihao_tv.setText("第"+detailQikanInfoMap.get("qikan_number").toString()+"期");
                    //期刊馆藏位置
                    detail_qikan_info_location_tv.setText(detailQikanInfoMap.get("qikan_location").toString());
                    //期刊作者简介
                    detail_qikan_info_author_introduce_tv.setText(detailQikanInfoMap.get("qikan_author_introduction").toString());
                    //期刊来源
                    detail_qikan_info_source_tv.setText(detailQikanInfoMap.get("qikan_resource").toString());

                    //期刊在线阅读按钮
                    detail_qikan_info_reading_online_tv.setText("在线阅读");

                    //判断该期刊是否被用户收藏
                    if ("1".equals(detailQikanInfoMap.get("has_collected").toString())) {
                        detail_qikan_info_collect_tv.setText("取消收藏");
                    }else if ("0".equals(detailQikanInfoMap.get("has_collected").toString())){
                        detail_qikan_info_collect_tv.setText("加入收藏");
                    }else {
                        detail_qikan_info_collect_tv.setText("唉呀，网络出现出错！");
                    }

                    //为收藏按钮设置点击监听器
                    detail_qikan_info_collect_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(DetailQiKanInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                    .setMessage("您确定要取消收藏吗？")//设置显示的内容
                                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            //getActivity().finish();

                                            //开启子线程，完成期刊取消收藏
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteQikan.php?user_id="+user_id+"&qikan_id="+qikan_id,"GET");
                                                    collect_flag = Integer.valueOf(str);
                                                    //创建消息对象
                                                    Message message = new Message();
                                                    //为消息对象设置标识
                                                    message.what = 26;
                                                    //将消息对象发送给UI线程
                                                    handler.sendMessage(message);
                                                }
                                            }).start();

                                        }
                                    }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                    // TODO Auto-generated method stub
                                    Log.i("alertdialog"," 请保存数据！");
                                }
                            }).show();//在按键响应事件中显示此对话框
                        }
                    });

                }else if (msg.what == 26){
                    if (collect_flag > 0){
                        Toast.makeText(DetailQiKanInfoActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                        detail_qikan_info_collect_tv.setText("加入收藏");
                        Intent intent1 = new Intent();
                        intent1.putExtra("data_return_qikan_id",qikan_id);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }else {
                        Log.d("Qikan_JSON_DATA","取消收藏失败！"+collect_flag);
                        Toast.makeText(DetailQiKanInfoActivity.this, "取消收藏失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

}
