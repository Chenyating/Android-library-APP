package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailPaperInfoActivity extends AppCompatActivity {

    //声明控件
    private TextView tittle_tv;
    private TextView detail_paper_info_name_tv;     //学位论文名称
    private TextView detail_paper_info_author_tv;     //学位论文作者
    private TextView detail_paper_info_school_tv;     //学位论文授予单位
    private TextView detail_paper_info_type_tv;     //学位论文类型
    private TextView detail_paper_info_date_tv;     //学位论文年度
    private TextView detail_paper_info_teacher_tv;     //学位论文导师姓名
    private TextView detail_paper_info_reading_online_tv;     //学位论文在线阅读按钮
    private TextView detail_paper_info_collect_tv;     //学位论文加入收藏按钮

    //设置Intent对象
    Intent intent;

    //设置消息处理对象
    private Handler handler;

    //设置学位论文信息集合
    private HashMap<String,Object> detailPaperInfoMap = new HashMap<>();

    //定义用户序号：user_id
    int user_id = -1;
    //定义期刊序号：qikan_id
    int paper_id = -1;
    //取消收藏是否成功标志位
    int collect_flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_detail_paper_info);
        //设置标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("学位论文资源详细信息");

        intent = getIntent();
        //接收传递过来的用户序号
        user_id = intent.getIntExtra("user_id",-1);
        //接收传递过来的报纸序号
        paper_id = intent.getIntExtra("paper_id",-1);
        //Log.d("user_id & paper_id:",user_id+" & "+paper_id);

        //开启子线程访问服务器，获取图书详细信息
        getPaperDetailInfo();

        //接收子线程的消息，更新主线程UI
        showPaperDetailInfo();

    }

    //开启子线程访问服务器，获取图书详细信息
    private void getPaperDetailInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailPaperInfo.php?paper_id="+paper_id+"&user_id="+user_id,"GET");
                Log.d("DETAILBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailPaperInfo.php?paper_id="+paper_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("jsonArray",jsonArray.length()+"");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    detailPaperInfoMap.put("paper_id",paper_id);
                    detailPaperInfoMap.put("paper_name",jsonObject.getString("paper_name"));
                    detailPaperInfoMap.put("paper_author",jsonObject.getString("paper_author"));
                    detailPaperInfoMap.put("paper_school",jsonObject.getString("paper_school"));
                    detailPaperInfoMap.put("paper_type",jsonObject.getString("paper_type"));
                    detailPaperInfoMap.put("paper_date",jsonObject.getString("paper_date"));
                    detailPaperInfoMap.put("paper_teacher",jsonObject.getString("paper_teacher"));
                    detailPaperInfoMap.put("paper_content",jsonObject.getString("paper_content"));
                    detailPaperInfoMap.put("has_collected",jsonObject.getString("has_collected"));
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 21;
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
        detail_paper_info_name_tv = (TextView)findViewById(R.id.detail_paper_info_name_tv);     //学位论文名称
        detail_paper_info_author_tv = (TextView)findViewById(R.id.detail_paper_info_author_tv);     //学位论文作者
        detail_paper_info_school_tv = (TextView)findViewById(R.id.detail_paper_info_school_tv);     //学位论文授予单位
        detail_paper_info_type_tv = (TextView)findViewById(R.id.detail_paper_info_type_tv);     //学位论文类型
        detail_paper_info_date_tv = (TextView)findViewById(R.id.detail_paper_info_date_tv);     //学位论文年度
        detail_paper_info_teacher_tv = (TextView)findViewById(R.id.detail_paper_info_teacher_tv);     //学位论文导师姓名
        detail_paper_info_reading_online_tv = (TextView)findViewById(R.id.detail_paper_info_reading_online_tv);     //学位论文在线阅读按钮
        detail_paper_info_collect_tv = (TextView)findViewById(R.id.detail_paper_info_collect_tv);     //学位论文加入收藏按钮
    }

    //接收子线程的消息，更新主线程UI
    private void showPaperDetailInfo(){
        //绑定控件
        initWidget();

        //接收子线程的消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 21){
                    detail_paper_info_name_tv.setText(detailPaperInfoMap.get("paper_name").toString());     //学位论文名称
                    detail_paper_info_author_tv.setText(detailPaperInfoMap.get("paper_author").toString());     //学位论文作者
                    detail_paper_info_school_tv.setText(detailPaperInfoMap.get("paper_school").toString());     //学位论文授予单位
                    detail_paper_info_type_tv.setText(detailPaperInfoMap.get("paper_type").toString());     //学位论文类型
                    detail_paper_info_date_tv.setText(detailPaperInfoMap.get("paper_date").toString());     //学位论文年度
                    detail_paper_info_teacher_tv.setText(detailPaperInfoMap.get("paper_teacher").toString());     //学位论文导师姓名

                    //期刊在线阅读按钮
                    detail_paper_info_reading_online_tv.setText("在线阅读");

                    //判断该期刊是否被用户收藏
                    if ("1".equals(detailPaperInfoMap.get("has_collected").toString())) {
                        detail_paper_info_collect_tv.setText("取消收藏");
                    }else if ("0".equals(detailPaperInfoMap.get("has_collected").toString())){
                        detail_paper_info_collect_tv.setText("加入收藏");
                    }else {
                        detail_paper_info_collect_tv.setText("唉呀，网络出现出错！");
                    }

                    //为加入收藏按钮添加点击事件
                    detail_paper_info_collect_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(DetailPaperInfoActivity.this).setTitle("系统提示")//设置对话框标题
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
                                                    //获取json字符串
                                                    String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavortePaper.php?user_id="+user_id+"&paper_id="+paper_id,"GET");
                                                    collect_flag = Integer.valueOf(str);
                                                    //getActivity().finish();
                                                    //创建消息对象
                                                    Message message = new Message();
                                                    //为消息对象设置标识
                                                    message.what = 24;
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

                }else if (msg.what == 24){
                    Toast.makeText(DetailPaperInfoActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                    detail_paper_info_collect_tv.setText("加入收藏");
                    Intent intent1 = new Intent();
                    intent1.putExtra("data_return_paper_id",paper_id);
                    setResult(RESULT_OK,intent1);
                    finish();
                }
            }
        };
    }

}
