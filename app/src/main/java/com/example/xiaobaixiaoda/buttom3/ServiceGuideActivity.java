package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ServiceGuideActivity extends AppCompatActivity {

    //声明服务指南项目列表控件
    private ListView service_guide_listview;
    //声明标题
    private TextView tittle_tv;

    //定义数据变量
    ArrayList<String> service_guide_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_service_guide);
        //绑定标题TextView
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置标题
        tittle_tv.setText("服务指南");
        //绑定ListView控件
        service_guide_listview = (ListView)findViewById(R.id.service_guide_listview);
        //获取服务指南信息数据
        getBorrowBackData();
        //为ListView绑定适配器
        service_guide_listview.setAdapter(createAdapter());
        //为ListView设置点击监听事件
        service_guide_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ServiceGuideActivity.this, "点击的是："+service_guide_data.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ServiceGuideActivity.this,ShowServiceContentActivity.class);
                switch (position){
                    case 0:
                        intent.putExtra("html_name","xuekeguanyuan.html");
                        break;
                    case 1:
                        intent.putExtra("html_name","guanjihujie.html");
                        break;
                    case 2:
                        intent.putExtra("html_name","dianhuazixun.html");
                        break;
                    case 3:
                        intent.putExtra("html_name","quanwenchuandi.html");
                        break;
                    case 4:
                        intent.putExtra("html_name","kejichaxin.html");
                        break;
                    case 5:
                        intent.putExtra("html_name","chengguorenzheng.html");
                        break;
                }
                startActivity(intent);
            }
        });
    }
    //获取服务指南数据
    private void getBorrowBackData(){
        service_guide_data.add("学科馆员");
        service_guide_data.add("馆际互借");
        //service_guide_data.add("代理检索");
        //service_guide_data.add("博硕士论文提交");
        service_guide_data.add("电话咨询");
        //service_guide_data.add("上网服务");
        service_guide_data.add("全文传递");
        service_guide_data.add("科技查新");
        //service_guide_data.add("远程资源访问");
        service_guide_data.add("成果认证");
        //service_guide_data.add("网络咨询");
        //service_guide_data.add("文印服务");
        //service_guide_data.add("借还服务");
    }
    //创建ListView适配器
    private BaseAdapter createAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return service_guide_data.size();
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
                View view = LayoutInflater.from(ServiceGuideActivity.this).inflate(R.layout.service_guide_item,null);
                TextView service_guide_tv = (TextView) view.findViewById(R.id.service_guide_tv);
                service_guide_tv.setText(service_guide_data.get(position));
                return view;
            }
        };
        return adapter;
    }
}
