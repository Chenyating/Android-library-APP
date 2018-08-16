package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFragment extends Fragment {

    private ListView news_listview;
    private ArrayList<HashMap<String,String>> news_data = new ArrayList<>();

    public NewsFragment(){  //构造方法

    }

    public static NewsFragment newInstance(){  //获取Fragment实例并传递数据
        NewsFragment fragment=new NewsFragment();
        //Bundle args=new Bundle();
        //args.putString("args1",param1);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_news,container,false);
        //Bundle bundle=getArguments();
        //String args1=bundle.getString("args1");
        //绑定ListView控件
        news_listview = (ListView)view.findViewById(R.id.news_listview);
        //初始化新闻数据
        initNewsData();
        //为新闻列表ListView设置适配器
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return news_data.size();
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
                view = getActivity().getLayoutInflater().inflate(R.layout.fragment_news_item, null);
                viewHolder = new ViewHolder();
                viewHolder.news_date_tv = (TextView)view.findViewById(R.id.news_date_tv);
                viewHolder.news_tittle_tv = (TextView)view.findViewById(R.id.news_tittle_tv);
                view.setTag(viewHolder);

                viewHolder.news_date_tv.setText("[" + news_data.get(position).get("news_date") + "]");
                viewHolder.news_tittle_tv.setText(news_data.get(position).get("news_tittle"));
                return view;
            }
            class ViewHolder {
                protected TextView news_date_tv,news_tittle_tv;
            }
        };
        //为新闻列表ListView绑定适配器
        news_listview.setAdapter(baseAdapter);
        //为新闻列表ListView设置点击监听器
        news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击的是："+news_data.get(position).get("news_tittle"), Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getActivity(),ShowServiceContentActivity.class);
                intent.putExtra("html_name","xuekeguanyuan.html");
                startActivity(intent);
            }
        });
        return view;
    }

    //初始化新闻数据
    private void initNewsData(){
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("news_date","2017-11-09");
        map1.put("news_tittle","悦读悦学资源平台(试用）通知");
        news_data.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("news_date","2017-11-02");
        map2.put("news_tittle","InCites - Benchmarking数据库正式开通通知");
        news_data.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("news_date","2017-10-25");
        map3.put("news_tittle","Emerald电子系列丛书正式开通通知");
        news_data.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("news_date","2017-10-25");
        map4.put("news_tittle","图书馆关于新生入馆教育等活动颁奖通知");
        news_data.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("news_date","2017-10-19");
        map5.put("news_tittle","2017级新生入馆教育全校排名前20名名单通知");
        news_data.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("news_date","2017-10-16");
        map6.put("news_tittle","橙艺艺术在线(试用)通知");
        news_data.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("news_date","2017-10-10");
        map7.put("news_tittle","运动会期间图书馆开放时间调整通知");
        news_data.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("news_date","2017-10-09");
        map8.put("news_tittle","“书香中国”中文在线图书阅读平台正式开通通知");
        news_data.add(map8);
        HashMap<String,String> map9 = new HashMap<>();
        map9.put("news_date","2017-09-28");
        map9.put("news_tittle","检ProQuest数据库，享Starbucks咖啡！");
        news_data.add(map9);
        HashMap<String,String> map10 = new HashMap<>();
        map10.put("news_date","2017-09-28");
        map10.put("news_tittle","奇才艺术长廊（试用）通知");
        news_data.add(map10);
    }
}