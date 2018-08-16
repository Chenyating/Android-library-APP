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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class CDSearchResultActivity extends AppCompatActivity {

    //声明控件
    private ListView cd_search_result_listview;
    private TextView tittle_tv;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> CDSearchResultInfo = new ArrayList<>();

    int user_id = -1;
    String history_content = "";
    int search_resource = 6;
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
        setContentView(R.layout.activity_cdsearch_result);

        //接收参数
        intent = getIntent();
        history_content = intent.getStringExtra("history_content");
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",6);
        search_type = intent.getIntExtra("search_type",1);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("搜索“"+history_content+"”的结果");

        //绑定ListView控件
        cd_search_result_listview = (ListView)findViewById(R.id.cd_search_result_listview);

        //获取ListView数据集数据
        getCDSearchResultInfoData();

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 53){    //显示光盘搜索结果
                    //为ListView通过匿名内部类的方法设置BaseAdapter适配器
                    baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() { //获取item总项数
                            return CDSearchResultInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {   //获取指定的item
                            return CDSearchResultInfo.get(position);
                        }

                        @Override
                        public long getItemId(int position) {   //获取指定的itemId
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) { //重写该方法，该方法返回的View将作为列表框
                            ViewHolder viewHolder;
                            View view = convertView;
                            if(view == null) {
                                view = LayoutInflater.from(CDSearchResultActivity.this).inflate(R.layout.fragment6_item, null);
                                viewHolder = new ViewHolder();
                                viewHolder.cd_id_tv = (TextView)view.findViewById(R.id.cd_number_tv);
                                viewHolder.cd_tittle_tv = (TextView)view.findViewById(R.id.cd_tittle_tv);
                                viewHolder.cd_classify_id_tv = (TextView)view.findViewById(R.id.cd_classify_number_tv);
                                viewHolder.cd_state_tv = (TextView)view.findViewById(R.id.cd_state_tv);
                                viewHolder.cd_publish_id_tv = (TextView)view.findViewById(R.id.cd_publish_identify_tv);
                                viewHolder.cd_publisher_tv = (TextView)view.findViewById(R.id.cd_publisher_tv);
                                viewHolder.cd_location_tv = (TextView)view.findViewById(R.id.cd_location_tv);
                                viewHolder.cd_num_tv = (TextView)view.findViewById(R.id.cd_available_number);
                                view.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) view.getTag();
                            }
                            viewHolder.cd_id_tv.setTextColor(Color.RED);
                            viewHolder.cd_id_tv.setText( "光盘号：" + CDSearchResultInfo.get(position).get("cd_number").toString());
                            viewHolder.cd_tittle_tv.setText("光盘标题：" + CDSearchResultInfo.get(position).get("cd_tittle").toString());
                            viewHolder.cd_classify_id_tv.setText("光盘分类号：" + CDSearchResultInfo.get(position).get("cd_classify_number").toString());
                            if (((Integer) CDSearchResultInfo.get(position).get("cd_statue")) == 1){
                                viewHolder.cd_state_tv.setText("光盘状态：" + "完好，可借");
                                viewHolder.cd_state_tv.setTextColor(Color.BLUE);
                            }else {
                                viewHolder.cd_state_tv.setText("光盘状态：" + "破损，不可借");
                                viewHolder.cd_state_tv.setTextColor(Color.RED);
                            }
                            viewHolder.cd_publish_id_tv.setText("出版标识：" + CDSearchResultInfo.get(position).get("cd_publish_identify").toString());
                            viewHolder.cd_publisher_tv.setText("出版社：" + CDSearchResultInfo.get(position).get("cd_publisher").toString());
                            viewHolder.cd_location_tv.setText("资源位置：" + CDSearchResultInfo.get(position).get("cd_location").toString());
                            viewHolder.cd_num_tv.setText("可用资源数量：" + (Integer) CDSearchResultInfo.get(position).get("cd_available_number"));
                            return view;
                        }
                        class ViewHolder {
                            protected TextView cd_id_tv,cd_tittle_tv,cd_classify_id_tv,cd_state_tv,cd_publish_id_tv,cd_publisher_tv,cd_location_tv,cd_num_tv;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    cd_search_result_listview.setAdapter(baseAdapter);
                }
            }
        };

    }
    //获取ListView数据集数据
    private void getCDSearchResultInfoData(){
        /*HashMap<String,String> map1 = new HashMap<>();
        map1.put("cd_id","S0035728");
        map1.put("cd_tittle","PHP动态网站开发案例课堂");
        map1.put("cd_classify_id","TP312/6838");
        map1.put("cd_state","好");
        map1.put("cd_publish_id","ISBN978-7-89395-662-1");
        map1.put("cd_publisher","清华大学出版社");
        map1.put("cd_location","图书馆四楼电子阅览室B435室");
        map1.put("cd_num","6");
        CDSearchResultInfo.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("cd_id","S0035728");
        map2.put("cd_tittle","PHP+MySQL网站开发入门与提高");
        map2.put("cd_classify_id","TP312/6838");
        map2.put("cd_state","好");
        map2.put("cd_publish_id","ISBN978-7-89395-662-1");
        map2.put("cd_publisher","清华大学出版社");
        map2.put("cd_location","图书馆四楼电子阅览室B435室");
        map2.put("cd_num","7");
        CDSearchResultInfo.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("cd_id","S0035728");
        map3.put("cd_tittle","PHP+MySQL+Dreamweaver网站建设全程揭秘");
        map3.put("cd_classify_id","TP312/6838");
        map3.put("cd_state","好");
        map3.put("cd_publish_id","ISBN978-7-89395-662-1");
        map3.put("cd_publisher","清华大学出版社");
        map3.put("cd_location","图书馆四楼电子阅览室B435室");
        map3.put("cd_num","8");
        CDSearchResultInfo.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("cd_id","S0035728");
        map4.put("cd_tittle","PHP从入门到精通");
        map4.put("cd_classify_id","TP312/6838");
        map4.put("cd_state","好");
        map4.put("cd_publish_id","ISBN978-7-89395-662-1");
        map4.put("cd_publisher","清华大学出版社");
        map4.put("cd_location","图书馆四楼电子阅览室B435室");
        map4.put("cd_num","9");
        CDSearchResultInfo.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("cd_id","S0035728");
        map5.put("cd_tittle","细说PHP LAMP");
        map5.put("cd_classify_id","TP312/6838");
        map5.put("cd_state","好");
        map5.put("cd_publish_id","ISBN978-7-89395-662-1");
        map5.put("cd_publisher","清华大学出版社");
        map5.put("cd_location","图书馆四楼电子阅览室B435室");
        map5.put("cd_num","10");
        CDSearchResultInfo.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("cd_id","S0035728");
        map6.put("cd_tittle","PHP编程新手自学手册 Php Bian Cheng Xin Shou Zi");
        map6.put("cd_classify_id","TP312/6838");
        map6.put("cd_state","好");
        map6.put("cd_publish_id","ISBN978-7-89395-662-1");
        map6.put("cd_publisher","清华大学出版社");
        map6.put("cd_location","图书馆四楼电子阅览室B435室");
        map6.put("cd_num","9");
        CDSearchResultInfo.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("cd_id","S0035728");
        map7.put("cd_tittle","学通PHP的24堂课 130集大型多媒体教学视频");
        map7.put("cd_classify_id","TP312/6838");
        map7.put("cd_state","好");
        map7.put("cd_publish_id","ISBN978-7-89395-662-1");
        map7.put("cd_publisher","清华大学出版社");
        map7.put("cd_location","图书馆四楼电子阅览室B435室");
        map7.put("cd_num","8");
        CDSearchResultInfo.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("cd_id","S0035728");
        map8.put("cd_tittle","Ajax+PHP 程序设计实战详解");
        map8.put("cd_classify_id","TP312/6838");
        map8.put("cd_state","好");
        map8.put("cd_publish_id","ISBN978-7-89395-662-1");
        map8.put("cd_publisher","清华大学出版社");
        map8.put("cd_location","图书馆四楼电子阅览室B435室");
        map8.put("cd_num","7");
        CDSearchResultInfo.add(map8);*/
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
                        map.put("cd_id",jsonObject.getInt("cd_id"));
                        map.put("cd_number",jsonObject.getString("cd_number"));
                        map.put("cd_tittle",jsonObject.getString("cd_tittle"));
                        map.put("cd_classify_number",jsonObject.getString("cd_classify_number"));
                        map.put("cd_publish_identify",jsonObject.getString("cd_publish_identify"));
                        map.put("cd_publisher",jsonObject.getString("cd_publisher"));
                        map.put("cd_location",jsonObject.getString("cd_location"));
                        map.put("cd_total_number",jsonObject.getInt("cd_total_number"));
                        map.put("cd_available_number",jsonObject.getInt("cd_available_number"));
                        map.put("cd_statue",jsonObject.getInt("cd_statue"));
                        map.put("cd_hot_search",jsonObject.getInt("cd_hot_search"));
                        CDSearchResultInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 53;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
