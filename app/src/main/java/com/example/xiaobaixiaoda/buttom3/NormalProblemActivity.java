package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NormalProblemActivity extends AppCompatActivity {

    //声明服务指南项目列表控件
    private ListView normal_problem_listview;
    //声明标题
    private TextView tittle_tv;

    //定义数据变量
    ArrayList<String> normal_problem_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_normal_problem);
        //绑定标题TextView
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置标题
        tittle_tv.setText("常见问题");
        //绑定ListView控件
        normal_problem_listview = (ListView)findViewById(R.id.normal_problem_listview);
        //获取服务指南信息数据
        getNormalProblemData();
        //为ListView绑定适配器
        normal_problem_listview.setAdapter(createAdapter());
        //为ListView设置点击监听事件
        normal_problem_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NormalProblemActivity.this, "点击的是："+normal_problem_data.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NormalProblemActivity.this,ShowServiceContentActivity.class);
                switch (position){
                    case 0:     //本馆概况
                        intent.putExtra("html_name","benguangaikuang.html");
                        break;
                    case 1:     //开馆时间
                        intent.putExtra("html_name","kaiguanshijian.html");
                        break;
                    case 2:     //馆藏布局
                        intent.putExtra("html_name","guancangbuju.html");
                        break;
                    case 3:     //借阅规则
                        intent.putExtra("html_name","jieyueguize.html");
                        break;
                    case 4:     //规章制度
                        intent.putExtra("html_name","guizhangzhidu.html");
                        break;
                    case 5:     //Q & A
                        intent.putExtra("html_name","Q_A.html");
                        break;
                    case 6:     //联系我们
                        intent.putExtra("html_name","connect_us.html");
                        break;
                    default:
                        intent.putExtra("html_name","xuekeguanyuan.html");
                        break;
                }
                startActivity(intent);
            }
        });
    }
    //获取服务指南信息数据
    private void getNormalProblemData(){
        normal_problem_data.add("本馆概况");
        normal_problem_data.add("开馆时间");
        normal_problem_data.add("馆藏布局");
        normal_problem_data.add("借阅规则");
        normal_problem_data.add("规章制度");
        normal_problem_data.add("Q & A");
        normal_problem_data.add("联系我们");
    }
    //创建ListView适配器
    private BaseAdapter createAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return normal_problem_data.size();
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
                View view = LayoutInflater.from(NormalProblemActivity.this).inflate(R.layout.service_guide_item,null);
                ImageView service_guide_icon = (ImageView)view.findViewById(R.id.service_guide_icon);
                service_guide_icon.setImageResource(R.drawable.usually_question2_32);
                TextView service_guide_tv = (TextView) view.findViewById(R.id.service_guide_tv);
                service_guide_tv.setText(normal_problem_data.get(position));
                return view;
            }
        };
        return adapter;
    }
}
