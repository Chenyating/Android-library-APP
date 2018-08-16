package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchNewspaperActivity extends AppCompatActivity {

    //声明控件
    private SearchView sv2;
    private ListView lv;
    private SearchBookAdapter adapter = null,adapterAll;       //ListView所需的适配器
    private TextView back_textview;
    List<SearchBookItem> data = null;   //ListView所需的适配器的数据
    List<SearchBookItem> dataAll = null;
    ArrayList<HashMap<String,Object>> history = new ArrayList<>();      //搜索历史信息

    //用户序号
    int user_id = -1;
    //搜索资源
    int search_resource = 3;
    //搜索类型
    int search_type = 1;

    Intent intent;

    Handler handler;

    int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局
        setContentView(R.layout.activity_search);

        //获取传递过来的数据
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",3);
        search_type = intent.getIntExtra("search_type",1);
        Log.d("SearchNewspaperActivity的search_type：",search_type+"");
        Log.d("SearchNewspaperActivity的search_resource：",search_resource+"");
        Log.d("SearchNewspaperActivity的用户序号：",user_id+"");

        //绑定搜索框控件
        sv2 = (SearchView)findViewById(R.id.sv2);

        //设置搜索框搜索提示
        sv2.setQueryHint("请输入报纸搜索内容");

        //设置搜索框显示搜索按钮
        sv2.setSubmitButtonEnabled(true);

        //设置搜索框失去焦点
        sv2.clearFocus();

        //去掉搜索框外面的搜索图标
        int magId = getResources().getIdentifier("android:id/search_mag_icon",null, null);
        ImageView magImage = (ImageView) sv2.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        //绑定下拉列表控件
        lv = (ListView)findViewById(R.id.lv);

        //绑定取消文本控件
        back_textview = (TextView)findViewById(R.id.back_textview);

        //为取消文本控件设置点击事件处理
        back_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化下拉列表历史记录数据
        initHistoryRecord();

        //初始化下拉列表所有图书数据
        //initAllBookInfo();
        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 43){    //显示前三条历史搜索记录
                    //创建适配器
                    adapter = new SearchBookAdapter(SearchNewspaperActivity.this,data);
                    //设置下拉列表项目点击监听事件
                    if (!("暂无任何历史记录".equals(adapter.data.get(1).getSearch_book_name()))){
                        Log.d("第1次有历史记录测试！","第1次有历史记录测试！");
                        //绑定适配器
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0){
                                    Toast.makeText(SearchNewspaperActivity.this, "应该跳转到我的全部报纸搜索历史记录界面", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(SearchNewspaperActivity.this,UserAllSearchHistoryActivity.class);
                                    intent1.putExtra("user_id",user_id);
                                    intent1.putExtra("search_resource",search_resource);
                                    startActivity(intent1);
                                }else {
                                    Toast.makeText(SearchNewspaperActivity.this, "你选择的报纸搜索记录是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                                    intent1.putExtra("user_id",user_id);
                                    intent1.putExtra("history_content",adapter.data.get(position).getSearch_book_name());
                                    intent1.putExtra("search_resource",search_resource);
                                    for (int i=0;i<history.size();i++){
                                        if (history.get(i).get("history_content").toString().equals(adapter.data.get(position).getSearch_book_name())){
                                            intent1.putExtra("book_id",Integer.valueOf(history.get(i).get("history_id").toString()));
                                            intent1.putExtra("search_type",Integer.valueOf(history.get(i).get("search_type").toString()));
                                            break;
                                        }
                                    }
                                    startActivity(intent1);
                                }
                            }
                        });
                    }else{
                        Log.d("第1次无历史记录测试！","第1次无历史记录测试！");
                    }

                    //为搜索框设置监听事件
                    sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                            Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                            intent1.putExtra("history_content",query);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("search_resource",search_resource);
                            intent1.putExtra("search_type",search_type);
                            startActivity(intent1);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {  //用户输入字符时触发该方法
                            if (TextUtils.isEmpty(newText.trim())){
                                if (!("暂无任何历史记录".equals(adapter.data.get(1).getSearch_book_name()))){
                                    Log.d("第2次有历史记录测试！","第2次有历史记录测试！");
                                    //绑定适配器
                                    lv.setAdapter(adapter);
                                    //设置下拉列表项目点击监听事件
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (position == 0){
                                                Toast.makeText(SearchNewspaperActivity.this, "应该跳转到我的全部期刊搜索历史记录界面", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(SearchNewspaperActivity.this,UserAllSearchHistoryActivity.class);
                                                intent1.putExtra("user_id",user_id);
                                                intent1.putExtra("search_resource",search_resource);
                                                startActivity(intent1);
                                            }else {
                                                Toast.makeText(SearchNewspaperActivity.this, "你选择的期刊搜索记录是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                                                intent1.putExtra("user_id",user_id);
                                                intent1.putExtra("history_content",adapter.data.get(position).getSearch_book_name());
                                                intent1.putExtra("search_resource",search_resource);
                                                for (int i=0;i<history.size();i++){
                                                    if (history.get(i).get("history_content").toString().equals(adapter.data.get(position).getSearch_book_name())){
                                                        intent1.putExtra("book_id",Integer.valueOf(history.get(i).get("history_id").toString()));
                                                        intent1.putExtra("search_type",Integer.valueOf(history.get(i).get("search_type").toString()));
                                                        break;
                                                    }
                                                }
                                                startActivity(intent1);
                                            }
                                        }
                                    });
                                }else{
                                    Log.d("第2次无历史记录测试！","第2次无历史记录测试！");
                                }
                                return true;
                            }
                            else {
                                //绑定适配器
                                lv.setAdapter(null);
                                return true;
                            }
                        }
                    });

                }
            }
        };

        /*//创建适配器
        adapter = new SearchBookAdapter(SearchNewspaperActivity.this,data);
        adapterAll = new SearchBookAdapter(SearchNewspaperActivity.this,dataAll);

        //绑定适配器
        lv.setAdapter(adapter);
        //开启ListView的过滤器功能
        lv.setTextFilterEnabled(true);
        //设置下拉列表项目点击监听事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Toast.makeText(SearchNewspaperActivity.this, "应该跳转到我的查询页面", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SearchNewspaperActivity.this, "你选择的搜索推荐是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                    intent1.putExtra("query_content",adapter.data.get(position).getSearch_book_name());
                    startActivity(intent1);
                }
            }
        });

        //为搜索框设置监听事件
        sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                *//*
                 * 实际应用中应该在方法内执行实际查询
                 * 此处仅使用Toast心啊是用户需要查询的内容
                 *//*
                Toast.makeText(SearchNewspaperActivity.this, "您想要查询的内容是："+query, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                intent1.putExtra("query_content",query);
                startActivity(intent1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {  //用户输入字符时触发该方法
                if (TextUtils.isEmpty(newText.trim())){
                    //绑定适配器
                    lv.setAdapter(adapter);
                    //开启ListView的过滤器功能
                    lv.setTextFilterEnabled(true);
                    //设置下拉列表项目点击监听事件
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0){
                                Toast.makeText(SearchNewspaperActivity.this, "应该跳转到我的查询页面", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SearchNewspaperActivity.this, "你选择的搜索推荐是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                                intent1.putExtra("query_content",adapter.data.get(position).getSearch_book_name());
                                startActivity(intent1);
                            }
                        }
                    });
                    //设置过滤器
                    adapter.getFilter().filter(newText);
                    return true;
                }
                else {
                    //绑定适配器
                    lv.setAdapter(adapterAll);
                    //开启ListView的过滤器功能
                    lv.setTextFilterEnabled(true);
                    //设置下拉列表项目点击监听事件
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(SearchNewspaperActivity.this, "你选择的搜索推荐是"+adapterAll.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                            intent1.putExtra("query_content",adapterAll.data.get(position).getSearch_book_name());
                            startActivity(intent1);
                        }
                    });
                    //设置过滤器
                    adapterAll.getFilter().filter(newText);
                    return true;
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag < 0){
            flag = 1;
        }else {
            //初始化下拉列表历史记录数据
            initHistoryRecord();
            //处理子线程消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 43){    //显示前三条历史搜索记录
                        //创建适配器
                        adapter = new SearchBookAdapter(SearchNewspaperActivity.this,data);
                        //设置下拉列表项目点击监听事件
                        if (!("暂无任何历史记录".equals(adapter.data.get(1).getSearch_book_name()))){
                            Log.d("第3次有历史记录测试！","第3次有历史记录测试！");
                            //绑定适配器
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == 0){
                                        Toast.makeText(SearchNewspaperActivity.this, "应该跳转到我的全部报纸搜索历史记录界面", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(SearchNewspaperActivity.this,UserAllSearchHistoryActivity.class);
                                        intent1.putExtra("user_id",user_id);
                                        intent1.putExtra("search_resource",search_resource);
                                        startActivity(intent1);
                                    }else {
                                        Toast.makeText(SearchNewspaperActivity.this, "你选择的报纸搜索记录是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                                        intent1.putExtra("user_id",user_id);
                                        intent1.putExtra("history_content",adapter.data.get(position).getSearch_book_name());
                                        intent1.putExtra("search_resource",search_resource);
                                        for (int i=0;i<history.size();i++){
                                            if (history.get(i).get("history_content").toString().equals(adapter.data.get(position).getSearch_book_name())){
                                                intent1.putExtra("book_id",Integer.valueOf(history.get(i).get("history_id").toString()));
                                                intent1.putExtra("search_type",Integer.valueOf(history.get(i).get("search_type").toString()));
                                                break;
                                            }
                                        }
                                        startActivity(intent1);
                                    }
                                }
                            });
                        }else{
                            Log.d("第3次无历史记录测试！","第3次无历史记录测试！");
                            lv.setAdapter(null);
                        }

                        //为搜索框设置监听事件
                        sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                                Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                                intent1.putExtra("history_content",query);
                                intent1.putExtra("user_id",user_id);
                                intent1.putExtra("search_resource",search_resource);
                                intent1.putExtra("search_type",search_type);
                                startActivity(intent1);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {  //用户输入字符时触发该方法
                                if (TextUtils.isEmpty(newText.trim())){
                                    if (!("暂无任何历史记录".equals(adapter.data.get(1).getSearch_book_name()))){
                                        Log.d("第4次有历史记录测试！","第4次有历史记录测试！");
                                        //绑定适配器
                                        lv.setAdapter(adapter);
                                        //设置下拉列表项目点击监听事件
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if (position == 0){
                                                    Toast.makeText(SearchNewspaperActivity.this, "应该跳转到我的全部期刊搜索历史记录界面", Toast.LENGTH_SHORT).show();
                                                    Intent intent1 = new Intent(SearchNewspaperActivity.this,UserAllSearchHistoryActivity.class);
                                                    intent1.putExtra("user_id",user_id);
                                                    intent1.putExtra("search_resource",search_resource);
                                                    startActivity(intent1);
                                                }else {
                                                    Toast.makeText(SearchNewspaperActivity.this, "你选择的期刊搜索记录是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                                    Intent intent1 = new Intent(SearchNewspaperActivity.this,NewspaperSearchResultAvtivity.class);
                                                    intent1.putExtra("user_id",user_id);
                                                    intent1.putExtra("history_content",adapter.data.get(position).getSearch_book_name());
                                                    intent1.putExtra("search_resource",search_resource);
                                                    for (int i=0;i<history.size();i++){
                                                        if (history.get(i).get("history_content").toString().equals(adapter.data.get(position).getSearch_book_name())){
                                                            intent1.putExtra("book_id",Integer.valueOf(history.get(i).get("history_id").toString()));
                                                            intent1.putExtra("search_type",Integer.valueOf(history.get(i).get("search_type").toString()));
                                                            break;
                                                        }
                                                    }
                                                    startActivity(intent1);
                                                }
                                            }
                                        });
                                    }else{
                                        Log.d("第4次无历史记录测试！","第4次无历史记录测试！");
                                        lv.setAdapter(null);
                                    }
                                    return true;
                                }
                                else {
                                    //绑定适配器
                                    lv.setAdapter(null);
                                    return true;
                                }
                            }
                        });

                    }
                }
            };
        }
    }

    private void initHistoryRecord(){
        /*data = new ArrayList<SearchBookItem>();
        data.add(new SearchBookItem("",0));
        data.add(new SearchBookItem("aaaaaaaaaa",2));
        data.add(new SearchBookItem("bbbbbbbbbbbb",2));
        data.add(new SearchBookItem("cccccccccccc",2));
        data.add(new SearchBookItem("",3));
        data.add(new SearchBookItem("eeeeeeeaaabbb",2));
        data.add(new SearchBookItem("dddeeaaaccccc",2));
        data.add(new SearchBookItem("sdfasfdsv2dfgqwe",2));
        data.add(new SearchBookItem("zcvsdfbaerqwer2",2));
        data.add(new SearchBookItem("lghkjighpopofgo",2));*/
        //开启子线程，从服务其获取三条历史记录和五条热门搜索
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = new ArrayList<SearchBookItem>();
                data.add(new SearchBookItem("",0));
                //获取json字符串
                String json_history = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getUserSearchHistory.php?user_id="+user_id+"&search_resource="+search_resource,"GET");
                try {
                    //解析json字符串
                    JSONArray jsonArrayHistory = new JSONArray(json_history);
                    Log.d("HistortjsonArray",jsonArrayHistory.length()+"");
                    if (jsonArrayHistory.length() <= 0){
                        data.add(new SearchBookItem("暂无任何历史记录",2));
                    }else {
                        for (int i = 0;i<jsonArrayHistory.length();++i){    //获取用户的搜索历史
                            JSONObject jsonObjectHistory = jsonArrayHistory.getJSONObject(i);
                            data.add(new SearchBookItem(jsonObjectHistory.getString("history_content"),2));
                            HashMap<String,Object> map = new HashMap<String, Object>();
                            map.put("history_id",jsonObjectHistory.getInt("history_id"));
                            map.put("history_content",jsonObjectHistory.getString("history_content"));
                            map.put("search_type",jsonObjectHistory.getString("search_type"));
                            history.add(map);
                        }
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 43;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*private void initAllBookInfo(){
        dataAll = new ArrayList<SearchBookItem>();
        dataAll.add(new SearchBookItem("《计算机体系结构》",1));
        dataAll.add(new SearchBookItem("《高级软件工程》",1));
        dataAll.add(new SearchBookItem("《机器学习》",1));
        dataAll.add(new SearchBookItem("《算法分析与设计》",1));
        dataAll.add(new SearchBookItem("《人工智能》",1));
        dataAll.add(new SearchBookItem("《自然语言分析》",1));
        dataAll.add(new SearchBookItem("《Linux内核源码解析》",1));
        dataAll.add(new SearchBookItem("《计算机网络》",1));
        dataAll.add(new SearchBookItem("《数据结构》",1));
        dataAll.add(new SearchBookItem("《计算机组成原理》",1));
        dataAll.add(new SearchBookItem("《操作系统》",1));
        dataAll.add(new SearchBookItem("《社交媒体数据挖掘》",1));
        dataAll.add(new SearchBookItem("《Java从入门到放弃》",1));
        dataAll.add(new SearchBookItem("《PHP从入门到放弃》",1));
        dataAll.add(new SearchBookItem("《Android从入门到放弃》",1));
        dataAll.add(new SearchBookItem("《C++从入门到放弃》",1));
        dataAll.add(new SearchBookItem("《JavaScript从入门到放弃》",1));
    }*/

}
