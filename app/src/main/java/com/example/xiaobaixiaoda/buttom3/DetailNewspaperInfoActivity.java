package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.HashMap;

public class DetailNewspaperInfoActivity extends AppCompatActivity {

    //声明控件
    private TextView tittle_tv;
    private TextView detail_newspaper_info_name_tv;     //报纸标题
    private TextView detail_newspaper_info_author_tv;     //报纸作者
    private TextView detail_newspaper_info_date_tv;     //报纸日期
    private TextView detail_newspaper_info_source_tv;     //报纸来源
    private TextView detail_newspaper_info_location_tv;     //报纸馆藏位置
    private TextView detail_newspaper_info_collect_tv;     //报纸加入收藏按钮
    private TextView detail_newspaper_info_reading_online_tv;     //报纸在线阅读按钮

    //设置Intent对象
    Intent intent;

    //设置消息处理对象
    private Handler handler;

    //设置期刊信息集合
    private HashMap<String,Object> detailNewspaperInfoMap = new HashMap<>();

    //定义用户序号：user_id
    int user_id = -1;
    //定义期刊序号：qikan_id
    int newspaper_id = -1;
    //取消收藏是否成功标志位
    int collect_flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_detail_newspaper_info);
        //设置标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("报纸资源详细信息");

        intent = getIntent();
        //接收传递过来的用户序号
        user_id = intent.getIntExtra("user_id",-1);
        //接收传递过来的报纸序号
        newspaper_id = intent.getIntExtra("newspaper_id",-1);
        //Log.d("user_id & newspaper_id:",user_id+" & "+newspaper_id);

        //开启子线程访问服务器，获取图书详细信息
        getNewspaperDetailInfo();

        //接收子线程的消息，更新主线程UI
        showNewspaperDetailInfo();

    }

    //开启子线程访问服务器，获取图书详细信息
    private void getNewspaperDetailInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailNewspaperInfo.php?newspaper_id="+newspaper_id+"&user_id="+user_id,"GET");
                Log.d("DETAILBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailNewspaperInfo.php?newspaper_id="+newspaper_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("jsonArray",jsonArray.length()+"");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    detailNewspaperInfoMap.put("newspaper_id",newspaper_id);
                    detailNewspaperInfoMap.put("newspaper_tittle",jsonObject.getString("newspaper_tittle"));
                    detailNewspaperInfoMap.put("newspaper_date",jsonObject.getString("newspaper_date"));
                    detailNewspaperInfoMap.put("newspaper_source",jsonObject.getString("newspaper_source"));
                    detailNewspaperInfoMap.put("newspaper_author",jsonObject.getString("newspaper_author"));
                    detailNewspaperInfoMap.put("newspaper_content",jsonObject.getString("newspaper_content"));
                    detailNewspaperInfoMap.put("newspaper_location",jsonObject.getString("newspaper_location"));
                    detailNewspaperInfoMap.put("has_collected",jsonObject.getString("has_collected"));
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 18;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //绑定控件
    private void intiWidget(){
        detail_newspaper_info_name_tv  = (TextView)findViewById(R.id.detail_newspaper_info_name_tv);     //报纸标题
        detail_newspaper_info_author_tv  = (TextView)findViewById(R.id.detail_newspaper_info_author_tv);     //报纸作者
        detail_newspaper_info_date_tv  = (TextView)findViewById(R.id.detail_newspaper_info_date_tv);     //报纸日期
        detail_newspaper_info_source_tv  = (TextView)findViewById(R.id.detail_newspaper_info_source_tv);     //报纸来源
        detail_newspaper_info_location_tv  = (TextView)findViewById(R.id.detail_newspaper_info_location_tv);     //报纸馆藏位置
        detail_newspaper_info_collect_tv  = (TextView)findViewById(R.id.detail_newspaper_info_collect_tv);     //报纸加入收藏按钮
        detail_newspaper_info_reading_online_tv  = (TextView)findViewById(R.id.detail_newspaper_info_reading_online_tv);     //报纸在线阅读按钮
    }

    //接收子线程的消息，更新主线程UI
    private void showNewspaperDetailInfo(){
        //绑定控件
        this.intiWidget();

        //接收子线程的消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 18){
                    //报纸标题
                    detail_newspaper_info_name_tv.setText(detailNewspaperInfoMap.get("newspaper_tittle").toString());
                    //报纸作者
                    detail_newspaper_info_author_tv.setText(detailNewspaperInfoMap.get("newspaper_author").toString());
                    //报纸日期
                    detail_newspaper_info_date_tv.setText(detailNewspaperInfoMap.get("newspaper_date").toString());
                    //报纸来源
                    detail_newspaper_info_source_tv.setText(detailNewspaperInfoMap.get("newspaper_source").toString());
                    //报纸馆藏位置
                    detail_newspaper_info_location_tv.setText(detailNewspaperInfoMap.get("newspaper_location").toString());

                    //期刊在线阅读按钮
                    detail_newspaper_info_reading_online_tv.setText("在线阅读");

                    //判断该期刊是否被用户收藏
                    if ("1".equals(detailNewspaperInfoMap.get("has_collected").toString())) {
                        detail_newspaper_info_collect_tv.setText("取消收藏");
                    }else if ("0".equals(detailNewspaperInfoMap.get("has_collected").toString())){
                        detail_newspaper_info_collect_tv.setText("加入收藏");
                    }else {
                        detail_newspaper_info_collect_tv.setText("唉呀，网络出现出错！");
                    }

                    //为收藏按钮设置点击监听器
                    detail_newspaper_info_collect_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(DetailNewspaperInfoActivity.this).setTitle("系统提示")//设置对话框标题
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
                                                    String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteNewspaper.php?user_id="+user_id+"&newspaper_id="+newspaper_id,"GET");
                                                    collect_flag = Integer.valueOf(str);
                                                    //创建消息对象
                                                    Message message = new Message();
                                                    //为消息对象设置标识
                                                    message.what = 27;
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

                }else if (msg.what == 27){
                    if (collect_flag > 0){
                        Toast.makeText(DetailNewspaperInfoActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                        detail_newspaper_info_collect_tv.setText("加入收藏");
                        Intent intent1 = new Intent();
                        intent1.putExtra("data_return_newspaper_id",newspaper_id);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }else {
                        Toast.makeText(DetailNewspaperInfoActivity.this, "取消收藏失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

}
