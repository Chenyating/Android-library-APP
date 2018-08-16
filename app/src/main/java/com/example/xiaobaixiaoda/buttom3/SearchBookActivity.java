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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchBookActivity extends AppCompatActivity {

    //声明控件
    private SearchView sv2;     //真正搜索界面的搜索框
    private ListView lv;        //真正搜索界面的搜索历史和热门搜索下拉列表ListView控件
    private SearchBookAdapter adapter = null,adapterAll;    //真正搜索界面的下拉列表ListView的适配器，是对BaseAdapter的重写
    private TextView back_textview;     //真正搜索界面取消搜索文本按钮

    List<SearchBookItem> data = null;       //真正搜索界面中下拉列表项的数据集合，SearchBookItem是搜索项类，对搜索项进行了简单的封装

    List<SearchBookItem> dataAll = null;    //真正搜索界面中将所有图书信息作为下拉列表项的数据集合，SearchBookItem是搜索项类，对搜索项进行了简单的封装

    ArrayList<HashMap<String,Object>> history = new ArrayList<>();      //真正搜索界面的搜索历史集合
    ArrayList<HashMap<String,Object>> hot_book = new ArrayList<>();     //真正搜索界面的热门图书集合
    ArrayList<HashMap<String,Object>> all_book = new ArrayList<>();     //真正搜索界面的全部图书集合

    Handler handler;

    //设置搜索的条件
    int search_type;
    int search_resource;

    //用户序号：user_id
    private int user_id = -1;

    int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局
        setContentView(R.layout.activity_search);

        //获取传递过来的数据
        Intent intent = getIntent();
        search_type = intent.getIntExtra("search_type",1);
        search_resource = intent.getIntExtra("search_resource",1);
        user_id = intent.getIntExtra("user_id",-1);
        //Toast.makeText(this, "搜索项是："+search_item, Toast.LENGTH_SHORT).show();
        Log.d("第二个界面的search_type：",search_type+"");
        Log.d("第二个界面的search_resource：",search_resource+"");
        Log.d("第二个界面的用户序号：",user_id+"");

        //绑定搜索框控件
        sv2 = (SearchView)findViewById(R.id.sv2);

        //设置搜索框搜索提示
        sv2.setQueryHint("请输入图书搜索内容");

        //绑定下拉列表控件
        lv = (ListView)findViewById(R.id.lv);

        //开启ListView的过滤器功能
        lv.setTextFilterEnabled(true);

        //绑定取消文本控件
        back_textview = (TextView)findViewById(R.id.back_textview);

        //设置搜索框显示搜索按钮
        sv2.setSubmitButtonEnabled(true);

        //设置搜索框失去焦点
        sv2.clearFocus();

        //去掉搜索框外面的搜索图标
        int magId = getResources().getIdentifier("android:id/search_mag_icon",null, null);
        ImageView magImage = (ImageView) sv2.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        //获取下拉列表中的历史搜索记录数据
        initHistoryRecord();

        //获取所有图书数据，提供给下拉列表用作用户输入过滤
        initAllBookInfo();

        //为取消文本控件设置点击事件处理
        back_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 33){    //子线程获取到用户的搜索历史和热门搜索图书后，主线程需要在下拉列表中显示历史记录和热门搜索项
                    //创建适配器
                    adapter = new SearchBookAdapter(SearchBookActivity.this,data);
                    //绑定适配器
                    lv.setAdapter(adapter);
                    //设置下拉列表项目点击监听事件
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0){     //点击查看更多搜索历史按钮后的触发事件
                                //Toast.makeText(SearchBookActivity.this, "应该跳转到我的查询页面", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(SearchBookActivity.this,UserAllSearchHistoryActivity.class);
                                intent1.putExtra("user_id",user_id);
                                intent1.putExtra("search_resource",search_resource);
                                startActivity(intent1);
                            }else {     //点击某一项搜索历史或热门搜索的触发事件
                                Toast.makeText(SearchBookActivity.this, "你选择的历史记录或者热门搜索是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                if (position > 0 && position < 4 && history.size() > 0){  //用户点击的是搜索历史记录
                                    Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                                    intent1.putExtra("history_content",history.get(position-1).get("history_content").toString());
                                    intent1.putExtra("user_id",user_id);
                                    intent1.putExtra("search_resource",search_resource);
                                    intent1.putExtra("search_type",Integer.valueOf(history.get(position-1).get("search_type").toString()));
                                    startActivity(intent1);
                                }else {     //用户点击的是热门搜索
                                    Intent intent1 = new Intent(SearchBookActivity.this,DetailBookInfoActivity.class);
                                    intent1.putExtra("user_id",user_id);
                                    if (history.size() > 0){
                                        intent1.putExtra("book_id",Integer.valueOf(hot_book.get(position-5).get("book_id").toString()));
                                    }else{
                                        intent1.putExtra("book_id",Integer.valueOf(hot_book.get(position-2).get("book_id").toString()));
                                    }
                                    startActivity(intent1);
                                }
                            }
                        }
                    });
                }else if (msg.what == 34){      //当子线程获取到所有图书信息后，主线程需要为下拉列表提供过滤功能
                    //创建适配器
                    adapterAll = new SearchBookAdapter(SearchBookActivity.this,dataAll);
                    //为搜索框设置监听事件
                    sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                            /*
                             * 实际应用中应该在方法内执行实际查询
                             * 此处仅使用Toast心啊是用户需要查询的内容
                             */
                            Toast.makeText(SearchBookActivity.this, "您输入的查询内容是："+query, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                            intent1.putExtra("history_content",query);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("search_resource",search_resource);
                            intent1.putExtra("search_type",search_type);
                            //判断用户输入的ISBN号格式是否正确
                            if (search_type == 2 && search_resource == 1){
                                Pattern pattern = Pattern.compile("(-{0,1}[0-9]{1,5}-{0,1}){0,3}");
                                Matcher matcher = pattern.matcher(query);
                                if (matcher.matches()){
                                    startActivity(intent1);
                                }else{
                                    Toast.makeText(SearchBookActivity.this, "ISBN号输入格式不正确！", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                startActivity(intent1);
                            }

                            /*Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                            intent1.putExtra("query_content",query);
                            startActivity(intent1);*/
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {  //用户输入字符时触发该方法
                            if (TextUtils.isEmpty(newText.trim())){     //当用户没有输入任何字符时，应该显示用户的搜索历史记录和热门搜索图书
                                //绑定适配器
                                lv.setAdapter(adapter);
                                //开启ListView的过滤器功能
                                lv.setTextFilterEnabled(true);
                                //设置下拉列表项目点击监听事件
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (position == 0){
                                            Toast.makeText(SearchBookActivity.this, "应该跳转到我的查询页面", Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(SearchBookActivity.this,UserAllSearchHistoryActivity.class);
                                            intent1.putExtra("user_id",user_id);
                                            intent1.putExtra("search_resource",search_resource);
                                            startActivity(intent1);
                                        }else {
                                            Toast.makeText(SearchBookActivity.this, "你选择的查询内容是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                            if (position > 0 && position < 4){  //用户点击的是搜索历史记录
                                                Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                                                intent1.putExtra("history_content",history.get(position-1).get("history_content").toString());
                                                intent1.putExtra("user_id",user_id);
                                                intent1.putExtra("search_resource",search_resource);
                                                intent1.putExtra("search_type",Integer.valueOf(history.get(position-1).get("search_type").toString()));
                                                startActivity(intent1);
                                            }else {     //用户点击的是热门搜索
                                                Intent intent1 = new Intent(SearchBookActivity.this,DetailBookInfoActivity.class);
                                                intent1.putExtra("user_id",user_id);
                                                intent1.putExtra("book_id",Integer.valueOf(hot_book.get(position-5).get("book_id").toString()));
                                                startActivity(intent1);
                                            }
                                        }
                                    }
                                });
                                //设置过滤器
                                adapter.getFilter().filter(newText);
                                return true;
                            }else {     //根据用户输入的字符进行搜索匹配提示
                                //绑定适配器
                                lv.setAdapter(adapterAll);
                                //开启ListView的过滤器功能
                                lv.setTextFilterEnabled(true);
                                //设置下拉列表项目点击监听事件
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(SearchBookActivity.this, "你选择的搜索推荐是"+adapterAll.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                        String book_name = adapterAll.data.get(position).getSearch_book_name();
                                        for (int i = 0;i < all_book.size();i++){
                                            if (book_name.equals(all_book.get(i).get("book_name").toString())){
                                                position = i;
                                                break;
                                            }
                                        }
                                        Intent intent1 = new Intent(SearchBookActivity.this,DetailBookInfoActivity.class);
                                        intent1.putExtra("user_id",user_id);
                                        intent1.putExtra("book_id",Integer.valueOf(all_book.get(position).get("book_id").toString()));
                                        startActivity(intent1);
                                    }
                                });
                                //设置过滤器
                                adapterAll.getFilter().filter(newText);
                                return true;
                            }
                        }
                    });
                }
            }
        };

    }

    //获取下拉列表中的历史搜索记录数据
    private void initHistoryRecord(){

        //开启子线程，从服务其获取三条历史记录和五条热门搜索
        new Thread(new Runnable() {
            @Override
            public void run() {
                history.clear();
                hot_book.clear();
                data = new ArrayList<SearchBookItem>();
                data.add(new SearchBookItem("",0));
                //获取json字符串
                String json_history = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getUserSearchHistory.php?user_id="+user_id+"&search_resource="+search_resource,"GET");
                String json_hot_search = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getHotBookSearch.php?","GET");
                //解析json字符串
                JSONArray jsonArrayHistory = null;
                JSONArray jsonArrayHotSearch = null;
                try {
                    jsonArrayHistory = new JSONArray(json_history);
                    jsonArrayHotSearch = new JSONArray(json_hot_search);
                    //Log.d("NewBookjsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArrayHistory.length();++i){    //将获取到的三个搜索历史记录放到history集合中
                        JSONObject jsonObjectHistory = jsonArrayHistory.getJSONObject(i);
                        data.add(new SearchBookItem(jsonObjectHistory.getString("history_content"),2));
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        map.put("history_id",jsonObjectHistory.getInt("history_id"));
                        map.put("history_content",jsonObjectHistory.getString("history_content"));
                        map.put("search_type",jsonObjectHistory.getString("search_type"));
                        history.add(map);
                    }
                    data.add(new SearchBookItem("",3));
                    for (int i = 0;i<jsonArrayHotSearch.length();++i){      //将获取到的热门搜索图书放到hot_book集合中
                        JSONObject jsonObjectHotSearch = jsonArrayHotSearch.getJSONObject(i);
                        data.add(new SearchBookItem(jsonObjectHotSearch.getString("book_name"),2));
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        map.put("book_id",jsonObjectHotSearch.getInt("book_id"));
                        map.put("book_name",jsonObjectHotSearch.getString("book_name"));
                        hot_book.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 33;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //获取所有图书数据，提供给下拉列表用作用户输入过滤
    private void initAllBookInfo(){

        //开启子线程，从服务器获取全部图书信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                all_book.clear();
                dataAll = new ArrayList<SearchBookItem>();
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getAllBookInfo.php","GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    //Log.d("NewBookjsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){    //获取用户的搜索历史
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataAll.add(new SearchBookItem(jsonObject.getString("book_name"),1));
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        map.put("book_id",jsonObject.getInt("book_id"));
                        map.put("book_name",jsonObject.getString("book_name"));
                        all_book.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 34;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag < 0){
            flag = 1;
        }else {
            //获取历史记录
            initHistoryRecord();
            //获取图书热门搜索记录
            initAllBookInfo();
            //处理子线程消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 33){
                        //创建适配器
                        adapter = new SearchBookAdapter(SearchBookActivity.this,data);
                        //绑定适配器
                        lv.setAdapter(adapter);
                        //设置下拉列表项目点击监听事件
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0){     //点击查看更多搜索历史按钮后的触发事件
                                    //Toast.makeText(SearchBookActivity.this, "应该跳转到我的查询页面", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(SearchBookActivity.this,UserAllSearchHistoryActivity.class);
                                    intent1.putExtra("user_id",user_id);
                                    intent1.putExtra("search_resource",search_resource);
                                    startActivity(intent1);
                                }else {     //点击某一项搜索历史或热门搜索的触发事件
                                    Toast.makeText(SearchBookActivity.this, "你选择的历史记录或者热门搜索是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                    if (position > 0 && position < 4 && history.size() > 0){  //用户点击的是搜索历史记录
                                        Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                                        intent1.putExtra("history_content",history.get(position-1).get("history_content").toString());
                                        intent1.putExtra("user_id",user_id);
                                        intent1.putExtra("search_resource",search_resource);
                                        intent1.putExtra("search_type",Integer.valueOf(history.get(position-1).get("search_type").toString()));
                                        startActivity(intent1);
                                    }else {     //用户点击的是热门搜索
                                        Intent intent1 = new Intent(SearchBookActivity.this,DetailBookInfoActivity.class);
                                        intent1.putExtra("user_id",user_id);
                                        if (history.size() > 0){
                                            intent1.putExtra("book_id",Integer.valueOf(hot_book.get(position-5).get("book_id").toString()));
                                        }else{
                                            intent1.putExtra("book_id",Integer.valueOf(hot_book.get(position-2).get("book_id").toString()));
                                        }
                                        startActivity(intent1);
                                    }
                                }
                            }
                        });
                    }else if (msg.what == 34){
                        //创建适配器
                        adapterAll = new SearchBookAdapter(SearchBookActivity.this,dataAll);
                        //为搜索框设置监听事件
                        sv2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {    //点击搜索按钮时触发该方法
                            /*
                             * 实际应用中应该在方法内执行实际查询
                             * 此处仅使用Toast心啊是用户需要查询的内容
                             */
                                Toast.makeText(SearchBookActivity.this, "您输入的查询内容是："+query, Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                                intent1.putExtra("history_content",query);
                                intent1.putExtra("user_id",user_id);
                                intent1.putExtra("search_resource",search_resource);
                                intent1.putExtra("search_type",search_type);
                                //判断用户输入的ISBN号格式是否正确
                                if (search_type == 2 && search_resource == 1){
                                    Pattern pattern = Pattern.compile("(-{0,1}[0-9]{1,5}-{0,1}){0,3}");
                                    Matcher matcher = pattern.matcher(query);
                                    if (matcher.matches()){
                                        startActivity(intent1);
                                    }else{
                                        Toast.makeText(SearchBookActivity.this, "ISBN号输入格式不正确！", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    startActivity(intent1);
                                }

                            /*Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                            intent1.putExtra("query_content",query);
                            startActivity(intent1);*/
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
                                                Toast.makeText(SearchBookActivity.this, "应该跳转到我的查询页面", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(SearchBookActivity.this,UserAllSearchHistoryActivity.class);
                                                intent1.putExtra("user_id",user_id);
                                                intent1.putExtra("search_resource",search_resource);
                                                startActivity(intent1);
                                            }else {
                                                Toast.makeText(SearchBookActivity.this, "你选择的查询内容是"+adapter.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                                if (position > 0 && position < 4){  //用户点击的是搜索历史记录
                                                    Intent intent1 = new Intent(SearchBookActivity.this,BookSearchResultActivity.class);
                                                    intent1.putExtra("history_content",history.get(position-1).get("history_content").toString());
                                                    intent1.putExtra("user_id",user_id);
                                                    intent1.putExtra("search_resource",search_resource);
                                                    intent1.putExtra("search_type",Integer.valueOf(history.get(position-1).get("search_type").toString()));
                                                    startActivity(intent1);
                                                }else {     //用户点击的是热门搜索
                                                    Intent intent1 = new Intent(SearchBookActivity.this,DetailBookInfoActivity.class);
                                                    intent1.putExtra("user_id",user_id);
                                                    intent1.putExtra("book_id",Integer.valueOf(hot_book.get(position-5).get("book_id").toString()));
                                                    startActivity(intent1);
                                                }
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
                                            Toast.makeText(SearchBookActivity.this, "你选择的搜索推荐是"+adapterAll.data.get(position).getSearch_book_name(), Toast.LENGTH_SHORT).show();
                                            String book_name = adapterAll.data.get(position).getSearch_book_name();
                                            for (int i = 0;i < all_book.size();i++){
                                                if (book_name.equals(all_book.get(i).get("book_name").toString())){
                                                    position = i;
                                                    break;
                                                }
                                            }
                                            Intent intent1 = new Intent(SearchBookActivity.this,DetailBookInfoActivity.class);
                                            intent1.putExtra("user_id",user_id);
                                            intent1.putExtra("book_id",Integer.valueOf(all_book.get(position).get("book_id").toString()));
                                            startActivity(intent1);
                                        }
                                    });
                                    //设置过滤器
                                    adapterAll.getFilter().filter(newText);
                                    return true;
                                }
                            }
                        });
                    }
                }
            };
        }
    }
}
