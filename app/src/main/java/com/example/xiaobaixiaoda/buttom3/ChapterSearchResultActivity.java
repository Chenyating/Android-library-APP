package com.example.xiaobaixiaoda.buttom3;

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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ChapterSearchResultActivity extends AppCompatActivity {

    //声明控件
    private ListView chapter_search_result_listview;
    private TextView tittle_tv;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> ChapterSearchResultInfo = new ArrayList<>();

    int user_id = -1;
    String history_content = "";
    int search_resource = 2;
    int search_type = 1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_chapter_search_result);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);

        //接收参数
        intent = getIntent();
        history_content = intent.getStringExtra("history_content");
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",5);
        search_type = intent.getIntExtra("search_type",1);

        tittle_tv.setText("搜索“"+history_content+"”的结果");

        //绑定ListView控件
        chapter_search_result_listview = (ListView)findViewById(R.id.chapter_search_result_listview);

        //获取ListView数据集数据
        getChapterSearchResultInfoData();

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 50){    //显示报纸资源的搜索结果
                    //构建适配器
                    baseAdapter = new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return ChapterSearchResultInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return ChapterSearchResultInfo.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            ViewHolder viewHolder;
                            View view = convertView;
                            if(view == null) {
                                view = LayoutInflater.from(ChapterSearchResultActivity.this).inflate(R.layout.fragment5_item, null);
                                viewHolder = new ViewHolder();
                                viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                viewHolder.source_tv = (TextView)view.findViewById(R.id.source_tv);
                                viewHolder.page_tv = (TextView)view.findViewById(R.id.page_tv);
                                viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                viewHolder.date_tv = (TextView)view.findViewById(R.id.date_tv);
                                view.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) view.getTag();
                            }
                            viewHolder.name_tv.setTextColor(Color.BLUE);
                            viewHolder.name_tv.setText((position+1) + ". " + ChapterSearchResultInfo.get(position).get("chapter_name"));
                            viewHolder.source_tv.setText("来 自：" + ChapterSearchResultInfo.get(position).get("chapter_source"));
                            viewHolder.page_tv.setText("页码：第" + ChapterSearchResultInfo.get(position).get("chapter_page") + "页");
                            viewHolder.author_tv.setText("作 者：" + ChapterSearchResultInfo.get(position).get("chapter_author"));
                            viewHolder.date_tv.setText("出版时间：" + ChapterSearchResultInfo.get(position).get("chapter_publish_date") + "年");
                            return view;
                        }
                        class ViewHolder {
                            protected TextView name_tv,source_tv,page_tv,date_tv,author_tv;
                        }
                    };

                    //为ListView绑定BaseAdapter适配器
                    chapter_search_result_listview.setAdapter(baseAdapter);

                    //为ListView设置点击监听器
                    chapter_search_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(ChapterSearchResultActivity.this, "点击的期刊是："+ChapterSearchResultInfo.get(position).get("name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

    }
    //获取ListView数据集数据
    private void getChapterSearchResultInfoData(){

        //开启子线程，从服务器获取全部图书信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    history_content = URLEncoder.encode(history_content,"UTF-8");
                    Log.d("history_content编码：",history_content);
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/searchResult.php?user_id="+user_id+"&search_resource="+search_resource+"&search_type="+search_type+"&history_content="+history_content,"GET");
                    Log.d("SearchBookURl：","http://"+getString(R.string.sever_ip)+"/TJPUSever/searchResult.php?user_id="+user_id+"&search_resource="+search_resource+"&search_type="+search_type+"&history_content="+history_content);
                    //解析json字符串
                    JSONArray jsonArray = new JSONArray(json);
                    Log.d("jsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("chapter_name",jsonObject.getString("chapter_name"));
                        map.put("chapter_source",jsonObject.getString("chapter_source"));
                        map.put("chapter_page",jsonObject.getString("chapter_page"));
                        map.put("chapter_author",jsonObject.getString("chapter_author"));
                        map.put("chapter_publish_date",jsonObject.getString("chapter_publish_date"));
                        map.put("chapter_content",jsonObject.getString("chapter_content"));
                        map.put("chapter_hot_search",jsonObject.getInt("chapter_hot_search"));
                        map.put("chapter_id",jsonObject.getInt("chapter_id"));
                        ChapterSearchResultInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 50;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
