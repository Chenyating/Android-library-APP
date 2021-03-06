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

public class SearchChapterActivity extends AppCompatActivity {

    //声明控件
    private SearchView sv2;
    private ListView lv;
    private SearchBookAdapter adapter = null;    //ListView所需的适配器
    private TextView back_textview;
    List<SearchBookItem> data = null;   //ListView所需的适配器的数据

    ArrayList<HashMap<String,Object>> history = new ArrayList<>();      //搜索历史信息

    Handler handler;

    int flag = -1;

    //设置搜索的条件
    int search_type;
    int search_resource;

    //用户序号：user_id
    private int user_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局
        setContentView(R.layout.activity_search);

        //获取传递过来的数据
        Intent intent = getIntent();
        search_type = intent.getIntExtra("search_type",0);
        search_resource = intent.getIntExtra("search_resource",5);
        user_id = intent.getIntExtra("user_id",-1);
        Log.d("SearchFragment5的search_type：",search_type+"");
        Log.d("SearchFragment5的search_resource：",search_resource+"");
        Log.d("SearchFragment5的用户序号：",user_id+"");

        //绑定搜索框控件
        sv2 = (SearchView)findViewById(R.id.sv2);

        //设置搜索框搜索提示
        sv2.setQueryHint("请输入章节搜索内容");

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

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 49){    //显示章节搜索资源前三条历史搜索记录
                    //创建适配器
                    adapter = new SearchBookAdapter(SearchChapterActivity.this,data);
                    //设置下拉列表项目点击监听事件
                    if (!("暂无任何历史记录".equals(adapter.data.get(1).getSearch_book_name()))){
                        Log.d("章节第1次有历史记录测试！","章节第1次有历史记录测试！");
                        //绑定适配器
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0){
                                    //应该跳转到我的全部章节搜索历史记录界面
                                    Intent intent1 = new Intent(SearchChapterActivity.this,UserAllSearchHistoryActivity.class);
                                    intent1.putExtra("user_id",user_id);
                                    intent1.putExtra("search_resource",search_resource);
                                    startActivity(intent1);
                                }else {
                                    //应该跳转到该条历史记录搜索结果显示界面
                                    Intent intent1 = new Intent(SearchChapterActivity.this,ChapterSearchResultActivity.class);
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
                        Log.d("章节第1次无历史记录测试！","章节第1次无历史记录测试！");
                    }

                    //为搜索框设置监听事件
                    sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                            Intent intent1 = new Intent(SearchChapterActivity.this,ChapterSearchResultActivity.class);
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
                                    Log.d("章节第2次有历史记录测试！","章节第2次有历史记录测试！");
                                    //绑定适配器
                                    lv.setAdapter(adapter);
                                    //设置下拉列表项目点击监听事件
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (position == 0){
                                                //应该跳转到我的全部章节搜索历史记录界面
                                                Intent intent1 = new Intent(SearchChapterActivity.this,UserAllSearchHistoryActivity.class);
                                                intent1.putExtra("user_id",user_id);
                                                intent1.putExtra("search_resource",search_resource);
                                                startActivity(intent1);
                                            }else {
                                                //应该跳转到该条历史记录搜索结果显示界面
                                                Intent intent1 = new Intent(SearchChapterActivity.this,ChapterSearchResultActivity.class);
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
                                    Log.d("章节第2次无历史记录测试！","章节第2次无历史记录测试！");
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
                    if (msg.what == 49){    //显示章节搜索资源前三条历史搜索记录
                        //创建适配器
                        adapter = new SearchBookAdapter(SearchChapterActivity.this,data);
                        //设置下拉列表项目点击监听事件
                        if (!("暂无任何历史记录".equals(adapter.data.get(1).getSearch_book_name()))){
                            Log.d("章节第3次有历史记录测试！","章节第3次有历史记录测试！");
                            //绑定适配器
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    if (position == 0){
                                        //应该跳转到我的全部章节搜索历史记录界面
                                        Intent intent1 = new Intent(SearchChapterActivity.this,UserAllSearchHistoryActivity.class);
                                        intent1.putExtra("user_id",user_id);
                                        intent1.putExtra("search_resource",search_resource);
                                        startActivity(intent1);
                                    }else {
                                        //应该跳转到该条历史记录搜索结果显示界面
                                        Intent intent1 = new Intent(SearchChapterActivity.this,ChapterSearchResultActivity.class);
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
                            Log.d("章节第3次无历史记录测试！","章节第3次无历史记录测试！");
                            lv.setAdapter(null);
                        }

                        //为搜索框设置监听事件
                        sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                                Intent intent1 = new Intent(SearchChapterActivity.this,ChapterSearchResultActivity.class);
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
                                        Log.d("章节第4次有历史记录测试！","章节第4次有历史记录测试！");
                                        //绑定适配器
                                        lv.setAdapter(adapter);
                                        //设置下拉列表项目点击监听事件
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if (position == 0){
                                                    //应该跳转到我的全部章节搜索历史记录界面
                                                    Intent intent1 = new Intent(SearchChapterActivity.this,UserAllSearchHistoryActivity.class);
                                                    intent1.putExtra("user_id",user_id);
                                                    intent1.putExtra("search_resource",search_resource);
                                                    startActivity(intent1);
                                                }else {
                                                    //应该跳转到该条历史记录搜索结果显示界面
                                                    Intent intent1 = new Intent(SearchChapterActivity.this,ChapterSearchResultActivity.class);
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
                                        Log.d("章节第4次无历史记录测试！","章节第4次无历史记录测试！");
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
                    message.what = 49;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
