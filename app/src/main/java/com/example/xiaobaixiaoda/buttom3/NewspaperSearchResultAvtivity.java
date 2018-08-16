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

public class NewspaperSearchResultAvtivity extends AppCompatActivity {

    //声明控件
    private ListView newspaper_search_result_listview;
    private TextView tittle_tv;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> NewspaperSearchResultInfo = new ArrayList<>();

    int user_id = -1;
    String history_content = "";
    int search_resource = 1;
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
        setContentView(R.layout.activity_newspaper_search_result_avtivity);

        //接收参数
        intent = getIntent();
        history_content = intent.getStringExtra("history_content");
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",3);
        search_type = intent.getIntExtra("search_type",1);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("搜索“"+history_content+"”的结果");

        //绑定ListView控件
        newspaper_search_result_listview = (ListView)findViewById(R.id.newspaper_search_result_listview);

        //获取ListView数据集数据
        getNewspaperSearchResultInfoData();

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 44){        //显示报纸资源搜索结果信息
                    //构建适配器
                    baseAdapter = new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return NewspaperSearchResultInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return NewspaperSearchResultInfo.get(position);
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
                                view = LayoutInflater.from(NewspaperSearchResultAvtivity.this).inflate(R.layout.fragment3_item, null);
                                viewHolder = new ViewHolder();
                                viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                viewHolder.date_tv = (TextView)view.findViewById(R.id.date_tv);
                                viewHolder.source_tv = (TextView)view.findViewById(R.id.source_tv);
                                view.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) view.getTag();
                            }
                            viewHolder.name_tv.setText(NewspaperSearchResultInfo.get(position).get("newspaper_tittle").toString());
                            if (NewspaperSearchResultInfo.get(position).get("author") != "")
                                viewHolder.author_tv.setText("作 者：" + NewspaperSearchResultInfo.get(position).get("newspaper_author").toString());
                            viewHolder.date_tv.setText("日 期：" + NewspaperSearchResultInfo.get(position).get("newspaper_date").toString());
                            viewHolder.source_tv.setText("来 源：" + NewspaperSearchResultInfo.get(position).get("newspaper_source").toString());
                            return view;
                        }
                        class ViewHolder {
                            protected TextView name_tv,author_tv,date_tv,source_tv;
                        }
                    };

                    //为ListView绑定BaseAdapter适配器
                    newspaper_search_result_listview.setAdapter(baseAdapter);

                    //为ListView设置点击监听器
                    newspaper_search_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(NewspaperSearchResultAvtivity.this, "点击的期刊是："+NewspaperSearchResultInfo.get(position).get("name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(NewspaperSearchResultAvtivity.this,DetailNewspaperInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("newspaper_id",Integer.valueOf(NewspaperSearchResultInfo.get(position).get("newspaper_id").toString()));
                            startActivity(intent1);
                        }
                    });
                }
            }
        };

    }
    //获取ListView数据集数据
    private void getNewspaperSearchResultInfoData(){
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
                        map.put("newspaper_tittle",jsonObject.getString("newspaper_tittle"));
                        map.put("newspaper_date",jsonObject.getString("newspaper_date"));
                        map.put("newspaper_source",jsonObject.getString("newspaper_source"));
                        map.put("newspaper_author",jsonObject.getString("newspaper_author"));
                        map.put("newspaper_content",jsonObject.getString("newspaper_content"));
                        map.put("newspaper_location",jsonObject.getString("newspaper_location"));
                        map.put("newspaper_hot_search",jsonObject.getString("newspaper_hot_search"));
                        map.put("newspaper_id",jsonObject.getInt("newspaper_id"));
                        NewspaperSearchResultInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 44;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /*HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","超级计算机全球最快");
        map1.put("date","2017.09.13");
        map1.put("source","南通日报");
        map1.put("author","");
        NewspaperSearchResultInfo.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","参观超级计算机");
        map2.put("date","2017.06.14");
        map2.put("source","无锡商报");
        map2.put("author","");
        NewspaperSearchResultInfo.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","MEN嵌入式计算机");
        map3.put("date","2017.09.05");
        map3.put("source","人民铁道报");
        map3.put("author","梁宇");
        NewspaperSearchResultInfo.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name","量子计算机中国造 超越早期经典计算机");
        map4.put("date","2017.05.04");
        map4.put("source","山西日报");
        map4.put("author","");
        NewspaperSearchResultInfo.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","预防计算机病毒");
        map5.put("date","2017.07.10");
        map5.put("source","齐鲁晚报");
        map5.put("author","");
        NewspaperSearchResultInfo.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","我国光量子计算机超越早期经典计算机");
        map6.put("date","2017.05.04");
        map6.put("source","信息时报");
        map6.put("author","");
        NewspaperSearchResultInfo.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","计算机科学与技术学院");
        map7.put("date","2017.06.29");
        map7.put("source","工学周报");
        map7.put("author","");
        NewspaperSearchResultInfo.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","世界第一台计算机问世");
        map8.put("date","2017.02.14");
        map8.put("source","巴中晚报");
        map8.put("author","");
        NewspaperSearchResultInfo.add(map8);*/
    }
}
