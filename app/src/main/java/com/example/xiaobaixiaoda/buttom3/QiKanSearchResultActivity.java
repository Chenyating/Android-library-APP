package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

public class QiKanSearchResultActivity extends AppCompatActivity {

    //声明控件
    private ListView qikan_search_result_listview;
    private TextView tittle_tv;

    int user_id = -1;
    String history_content = "";
    int search_resource = 2;
    int search_type = 1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> QiKanSearchResultInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_qi_kan_search_result);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);

        //接收参数
        Intent intent = getIntent();
        history_content = intent.getStringExtra("history_content");
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",2);
        search_type = intent.getIntExtra("search_type",1);

        //设置标题
        tittle_tv.setText("搜索“"+history_content+"”的结果");

        //绑定ListView控件
        qikan_search_result_listview = (ListView)findViewById(R.id.qikan_search_result_listview);

        //获取ListView数据集数据
        getQiKanSearchResultInfoData();

        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 40){
                    //构建适配器
                    baseAdapter = new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return QiKanSearchResultInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            ViewHolder viewHolder;
                            View view = convertView;
                            if(view == null) {
                                view = LayoutInflater.from(QiKanSearchResultActivity.this).inflate(R.layout.fragment2_item, null);
                                viewHolder = new ViewHolder();
                                viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                viewHolder.kanming_tv = (TextView)view.findViewById(R.id.kanming_tv);
                                viewHolder.publish_date_tv = (TextView)view.findViewById(R.id.publish_date_tv);
                                viewHolder.qihao_tv = (TextView)view.findViewById(R.id.qihao_tv);
                                view.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) view.getTag();
                            }
                            viewHolder.name_tv.setText(QiKanSearchResultInfo.get(position).get("qikan_name").toString());
                            viewHolder.author_tv.setText("作者：" + QiKanSearchResultInfo.get(position).get("qikan_author").toString());
                            viewHolder.kanming_tv.setText("刊名：" + QiKanSearchResultInfo.get(position).get("qikan_tittle").toString());
                            viewHolder.publish_date_tv.setText("出版日期：" + QiKanSearchResultInfo.get(position).get("qikan_date").toString());
                            viewHolder.qihao_tv.setText("期号：" + QiKanSearchResultInfo.get(position).get("qikan_number").toString());
                            return view;
                        }
                        class ViewHolder {
                            protected TextView name_tv,author_tv,kanming_tv,publish_date_tv,qihao_tv;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    qikan_search_result_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    qikan_search_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent1 = new Intent(QiKanSearchResultActivity.this,DetailQiKanInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("qikan_id",Integer.valueOf(QiKanSearchResultInfo.get(position).get("qikan_id").toString()));
                            startActivity(intent1);
                            Toast.makeText(QiKanSearchResultActivity.this, "点击的期刊是："+QiKanSearchResultInfo.get(position).get("name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

    }
    //获取ListView数据集数据
    private void getQiKanSearchResultInfoData(){
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
                        map.put("qikan_author",jsonObject.getString("qikan_author"));
                        map.put("qikan_tittle",jsonObject.getString("qikan_tittle"));
                        map.put("qikan_name",jsonObject.getString("qikan_name"));
                        map.put("qikan_number",jsonObject.getString("qikan_number"));
                        map.put("qikan_date",jsonObject.getString("qikan_date"));
                        map.put("qikan_id",jsonObject.getInt("qikan_id"));
                        QiKanSearchResultInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 40;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /*HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","浅谈计算机与计算机技术");
        map1.put("author","石俊杰");
        map1.put("kanming","中外交流");
        map1.put("publish_date","2017");
        map1.put("qihao","第31期");
        QiKanSearchResultInfo.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","计算机网络维护");
        map2.put("author","玉明文");
        map2.put("kanming","大科技");
        map2.put("publish_date","2017");
        map2.put("qihao","第11期");
        QiKanSearchResultInfo.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","量子计算机");
        map3.put("author","刘小桐");
        map3.put("kanming","科技创新与应用");
        map3.put("publish_date","2017");
        map3.put("qihao","第19期");
        QiKanSearchResultInfo.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name","兔子计算机");
        map4.put("author","Kydll Nikitine，王师");
        map4.put("kanming","新发现");
        map4.put("publish_date","2017");
        map4.put("qihao","第4期");
        QiKanSearchResultInfo.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","计算机应用浅析");
        map5.put("author","刘彩梅");
        map5.put("kanming","速读(上旬)");
        map5.put("publish_date","2017");
        map5.put("qihao","第1期");
        QiKanSearchResultInfo.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","作为计算机的大脑");
        map6.put("author","Karlheinz Meier");
        map6.put("kanming","科技纵览");
        map6.put("publish_date","2017");
        map6.put("qihao","第6期");
        QiKanSearchResultInfo.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","浅谈计算机与计算机技术");
        map7.put("author","石俊杰");
        map7.put("kanming","中外交流");
        map7.put("publish_date","2017");
        map7.put("qihao","第31期");
        QiKanSearchResultInfo.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","基于计算机视觉技术的番茄花青素含量检测");
        map8.put("author","雷静");
        map8.put("kanming","农机化研究");
        map8.put("publish_date","2017");
        map8.put("qihao","第3期");
        QiKanSearchResultInfo.add(map8);*/
    }
}
