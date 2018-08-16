package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAllSearchHistoryActivity extends AppCompatActivity {

    //声明控件
    private ListView all_search_history_listview;
    //声明标题
    private TextView tittle_name_tv,tittle_clean_tv;

    Intent intent;

    Handler handler;

    ArrayAdapter adapter;

    int delete_history_position = -1;   //单个删除用户搜索历史记录在ListView中的位置

    int flag = -1;

    int clean_history_flag = -1;

    int if_has_history = -1;        //是否存在该用户某一资源的搜索历史记录

    int user_id,search_resource;

    //定义数据变量
    ArrayList<HashMap<String,Object>> all_search_history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_user_all_search_history);

        //获取传递过来的参数
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",1);

        //绑定标题TextView
        tittle_name_tv = (TextView)findViewById(R.id.tittle_name_tv);
        tittle_clean_tv = (TextView)findViewById(R.id.tittle_clean_tv);

        //设置标题
        switch (search_resource){
            case 1:
                tittle_name_tv.setText("图书资源搜索历史");
                break;
            case 2:
                tittle_name_tv.setText("期刊资源搜索历史");
                break;
            case 3:
                tittle_name_tv.setText("报纸资源搜索历史");
                break;
            case 4:
                tittle_name_tv.setText("学位论文资源搜索历史");
                break;
            case 5:
                tittle_name_tv.setText("章节资源搜索历史");
                break;
            case 6:
                tittle_name_tv.setText("光盘资源搜索历史");
                break;
        }

        //绑定ListView控件
        all_search_history_listview = (ListView)findViewById(R.id.all_search_history_listview);

        //开启子线程，从服务器获取用户的资源搜索历史
        getHistory();

        //接收子线程的消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 35){
                    String[] data;
                    if (if_has_history > 0){    //如果有历史记录
                        data = new String[all_search_history.size()];
                        for (int i=0;i<all_search_history.size();i++){
                            data[i] = all_search_history.get(i).get("history_content").toString();
                        }
                    }else {     //如果没有历史记录
                        data = new String[1];
                        data[0] = "暂无任何历史记录";
                    }
                    adapter = new ArrayAdapter<String>(UserAllSearchHistoryActivity.this,android.R.layout.simple_list_item_1,data);
                    //将适配器分配给ListView
                    all_search_history_listview.setAdapter(adapter);
                    if (if_has_history > 0){
                        //为ListView设置点击监听事件
                        all_search_history_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //跳转到相应资源的搜索结果界面
                                switch (search_resource){
                                    case 1:     //图书资源搜索
                                        intent = new Intent(UserAllSearchHistoryActivity.this,BookSearchResultActivity.class);
                                        break;
                                    case 2:     //期刊资源搜索
                                        intent = new Intent(UserAllSearchHistoryActivity.this,QiKanSearchResultActivity.class);
                                        break;
                                    case 3:     //期刊资源搜索
                                        intent = new Intent(UserAllSearchHistoryActivity.this,NewspaperSearchResultAvtivity.class);
                                        break;
                                    case 4:     //学位论文资源搜索
                                        intent = new Intent(UserAllSearchHistoryActivity.this,PaperSearchResultActivity.class);
                                        break;
                                    case 5:     //章节资源搜索
                                        intent = new Intent(UserAllSearchHistoryActivity.this,ChapterSearchResultActivity.class);
                                        break;
                                    case 6:     //光盘资源搜索
                                        intent = new Intent(UserAllSearchHistoryActivity.this,ChapterSearchResultActivity.class);
                                        break;
                                }
                                intent.putExtra("history_content",all_search_history.get(position).get("history_content").toString());
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("search_resource",search_resource);
                                intent.putExtra("search_type",Integer.valueOf(all_search_history.get(position).get("search_type").toString()));
                                startActivity(intent);
                            }
                        });
                        //为ListView设置长按监听事件
                        all_search_history_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                new AlertDialog.Builder(UserAllSearchHistoryActivity.this).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要删除"+all_search_history.get(position).get("history_content")+"吗？")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub

                                                //开启子线程，完成删除历史记录
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        delete_history_position = position;
                                                        //获取json字符串
                                                        int history_id = (Integer) all_search_history.get(position).get("history_id");
                                                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/deleteUserSearchHistory.php?user_id="+user_id+"&history_id="+history_id+"&search_resource="+search_resource,"GET");
                                                        flag = Integer.valueOf(str);
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 36;
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
                                return true;
                            }
                        });

                        tittle_clean_tv.setText("清空");
                        tittle_clean_tv.setTextColor(Color.parseColor("#FF0000"));
                        tittle_clean_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(UserAllSearchHistoryActivity.this).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要清空历史记录吗？")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                // 开启子线程，清空历史记录
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //获取json字符串
                                                        String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cleanSearchHistory.php?user_id="+user_id+"&search_resource="+search_resource,"GET");
                                                        clean_history_flag = Integer.valueOf(json);
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 41;
                                                        //将消息对象发送给UI线程
                                                        handler.sendMessage(message);
                                                    }
                                                }).start();
                                            }
                                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                                // TODO Auto-generated method stub
                                                // Log.i("alertdialog"," 请保存数据！");
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                                }
                            });
                    }
                }else if (msg.what == 36){
                    if (flag > 0){
                        all_search_history.remove(delete_history_position);
                        String[] data = new String[all_search_history.size()];
                        for (int i=0;i<all_search_history.size();i++){
                            data[i] = all_search_history.get(i).get("history_content").toString();
                        }
                        adapter = new ArrayAdapter<String>(UserAllSearchHistoryActivity.this,android.R.layout.simple_list_item_1,data);
                        //将适配器分配给ListView
                        all_search_history_listview.setAdapter(adapter);
                        Toast.makeText(UserAllSearchHistoryActivity.this, "删除历史记录成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UserAllSearchHistoryActivity.this, "删除历史记录失败！", Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == 41){
                    if (clean_history_flag > 0){
                        all_search_history.clear();
                        String[] data = {"暂无任何历史记录"};
                        adapter = new ArrayAdapter<String>(UserAllSearchHistoryActivity.this,android.R.layout.simple_list_item_1,data);
                        //将适配器分配给ListView
                        all_search_history_listview.setAdapter(adapter);
                        tittle_clean_tv.setText("");
                        Toast.makeText(UserAllSearchHistoryActivity.this, "清空历史记录成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UserAllSearchHistoryActivity.this, "清空历史记录失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

    }

    //开启子线程，从服务器获取用户的资源搜索历史
    private void getHistory(){
        //开启子线程，从服务器获取全部图书信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getAllHistoryInfo.php?user_id="+user_id+"&search_resource="+search_resource,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    if (jsonArray.length() > 0){    //如果有历史记录
                        if_has_history = 1;
                        for (int i = 0;i<jsonArray.length();++i){    //获取用户的搜索历史
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String,Object> map = new HashMap<String, Object>();
                            map.put("history_id",jsonObject.getInt("history_id"));
                            map.put("history_content",jsonObject.getString("history_content"));
                            map.put("search_type",jsonObject.getInt("search_type"));
                            all_search_history.add(map);
                        }
                    }else {     //如果没有历史记录
                        if_has_history = -1;
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 35;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
