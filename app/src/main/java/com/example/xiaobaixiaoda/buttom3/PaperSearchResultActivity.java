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

public class PaperSearchResultActivity extends AppCompatActivity {

    //声明控件
    private ListView paper_search_result_listview;
    private TextView tittle_tv;

    int user_id = -1;
    String history_content = "";
    int search_resource = 4;
    int search_type = 1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> PaperSearchResultInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_paper_search_result);

        //接收参数
        intent = getIntent();
        history_content = intent.getStringExtra("history_content");
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",4);
        search_type = intent.getIntExtra("search_type",1);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("搜索“"+history_content+"”的结果");

        //绑定ListView控件
        paper_search_result_listview = (ListView)findViewById(R.id.paper_search_result_listview);

        //获取ListView数据集数据
        getPaperSearchResultInfoData();

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 47){
                    //构建适配器
                    baseAdapter = new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return PaperSearchResultInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return PaperSearchResultInfo.get(position);
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
                                view = LayoutInflater.from(PaperSearchResultActivity.this).inflate(R.layout.fragment4_item, null);
                                viewHolder = new ViewHolder();
                                viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                viewHolder.date_tv = (TextView)view.findViewById(R.id.date_tv);
                                viewHolder.teacher_tv = (TextView)view.findViewById(R.id.teacher_tv);
                                viewHolder.school_tv = (TextView)view.findViewById(R.id.school_tv);
                                viewHolder.type_tv = (TextView)view.findViewById(R.id.type_tv);
                                view.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) view.getTag();
                            }
                            viewHolder.name_tv.setTextColor(Color.BLUE);
                            viewHolder.name_tv.setText((position+1) + ". " + PaperSearchResultInfo.get(position).get("paper_name"));
                            viewHolder.author_tv.setText("作 者：" + PaperSearchResultInfo.get(position).get("paper_author"));
                            viewHolder.date_tv.setText("学位年度：" + PaperSearchResultInfo.get(position).get("paper_date"));
                            viewHolder.teacher_tv.setText("导师姓名：" + PaperSearchResultInfo.get(position).get("paper_teacher"));
                            viewHolder.school_tv.setText("学位授予单位：" + PaperSearchResultInfo.get(position).get("paper_school"));
                            viewHolder.type_tv.setText("学位名称：" + PaperSearchResultInfo.get(position).get("paper_type"));
                            return view;
                        }
                        class ViewHolder {
                            protected TextView name_tv,author_tv,school_tv,type_tv,date_tv,teacher_tv;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    paper_search_result_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    paper_search_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent1 = new Intent(PaperSearchResultActivity.this,DetailPaperInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("paper_id",Integer.valueOf(PaperSearchResultInfo.get(position).get("paper_id").toString()));
                            startActivity(intent1);
                        }
                    });
                }
            }
        };

    }
    //获取ListView数据集数据
    private void getPaperSearchResultInfoData(){
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
                        map.put("paper_name",jsonObject.getString("paper_name"));
                        map.put("paper_author",jsonObject.getString("paper_author"));
                        map.put("paper_school",jsonObject.getString("paper_school"));
                        map.put("paper_type",jsonObject.getString("paper_type"));
                        map.put("paper_date",jsonObject.getString("paper_date"));
                        map.put("paper_teacher",jsonObject.getString("paper_teacher"));
                        map.put("paper_content",jsonObject.getString("paper_content"));
                        map.put("paper_id",jsonObject.getInt("paper_id"));
                        PaperSearchResultInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 47;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /*HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","五子棋计算机博弈系统的研究与设计");
        map1.put("author","张效见");
        map1.put("school","安徽大学");
        map1.put("type","硕士");
        map1.put("date","2017");
        map1.put("teacher","李龙澍");
        PaperSearchResultInfo.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","基于能力本位的中职计算机专业课程体系的研究");
        map2.put("author","卢新贞");
        map2.put("school","河北师范大学");
        map2.put("type","硕士");
        map2.put("date","2017");
        map2.put("teacher","武金玲");
        PaperSearchResultInfo.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","计算机图形图像发展史研究");
        map3.put("author","吴毅儒");
        map3.put("school","天津工业大学");
        map3.put("type","硕士");
        map3.put("date","2017");
        map3.put("teacher","李铁");
        PaperSearchResultInfo.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name","计算机类论文HCI and Software Engineering for User Interface Plasticity英汉翻译实践报告");
        map4.put("author","张爽");
        map4.put("school","黑龙江大学");
        map4.put("type","硕士");
        map4.put("date","2017");
        map4.put("teacher","高战荣");
        PaperSearchResultInfo.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","应变场下钨抗辐照性能的计算机模拟");
        map5.put("author","王栋");
        map5.put("school","中国科学院大学(中国科学院近代物理研究所)");
        map5.put("type","博士");
        map5.put("date","2017");
        map5.put("teacher","王志光");
        PaperSearchResultInfo.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","基于网络攻击的计算机病毒传播模型的优化算法研究");
        map6.put("author","裴宏悦");
        map6.put("school","天津工业大学");
        map6.put("type","硕士");
        map6.put("date","2017");
        map6.put("teacher","裴永珍，张建勇");
        PaperSearchResultInfo.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","基于VARK模型的中职计算机辅助教学系统设计与实现");
        map7.put("author","高翼飞");
        map7.put("school","中国科学院大学工程科学学院");
        map7.put("type","硕士");
        map7.put("date","2017");
        map7.put("teacher","吴广洲，林意");
        PaperSearchResultInfo.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","Investigation on Exponential Synchronization via Various Control Techniques for Complex Networks of Networks");
        map8.put("author","Mohmmed Alsiddig Alamin Ahmed");
        map8.put("school","扬州大学");
        map8.put("type","博士");
        map8.put("date","2017");
        map8.put("teacher","刘玉荣，张文兵");
        PaperSearchResultInfo.add(map8);*/
    }
}
