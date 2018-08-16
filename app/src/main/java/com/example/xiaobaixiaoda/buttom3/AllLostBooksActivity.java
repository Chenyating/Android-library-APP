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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllLostBooksActivity extends AppCompatActivity {

    //声明控件
    ListView all__books_listview;
    TextView tittle_tv,all_borrow_books_isbn_tv,all_borrow_books_name_tv,all_borrow_books_author_tv,all_borrow_books_publisher_tv,all_borrow_books_borrowdate_tv,all_borrow_books_state_tv,all_borrow_books_backdate_tv;

    //定义变量
    ArrayList<HashMap<String,Object>> lost_book_data = new ArrayList<>();

    //用户序号
    int user_id = -1;

    Intent intent;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_all_lost_books);
        //设置界面的标题文字
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("挂失未处理图书信息");
        //接收参数
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        //绑定控件
        all__books_listview = (ListView)findViewById(R.id.all__books_listview);
        //获取用户挂失未处理图书信息
        getLostBookInfo();
        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 64){
                    //为ListView绑定适配器
                    all__books_listview.setAdapter(createAdapter());
                }
            }
        };
    }

    //获取用户挂失未处理图书信息
    private void getLostBookInfo(){
        //开启子线程，获取该用户的图书借阅信息
        new Thread(new Runnable(){
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getAllLostBookInfo.php?user_id="+user_id,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("全部借阅图书json：",json);
                    Log.d("全部借阅图书数量：",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("book_id",jsonObject.getInt("book_id"));
                        map.put("borrow_id",jsonObject.getInt("borrow_id"));
                        map.put("book_isbn",jsonObject.getString("book_isbn"));
                        map.put("book_name",jsonObject.getString("book_name"));
                        map.put("book_author",jsonObject.getString("book_author"));
                        map.put("book_publisher",jsonObject.getString("book_publisher"));
                        map.put("borrow_date",jsonObject.getString("borrow_date"));
                        map.put("borrow_return_date",jsonObject.getString("borrow_return_date"));
                        map.put("borrow_statue",jsonObject.getInt("borrow_statue"));
                        map.put("book_price",jsonObject.getDouble("book_price"));
                        lost_book_data.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 64;
                //将消息对象发送给UI线程
                handler.sendMessage(message);
            }
        }).start();
    }

    //创建ListView适配器
    private BaseAdapter createAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return lost_book_data.size();
            }

            @Override
            public Object getItem(int position) {
                return lost_book_data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(AllLostBooksActivity.this).inflate(R.layout.all_borrow_books,null);
                //绑定子项控件
                initItemWidget(view);
                //设置ISBN
                all_borrow_books_isbn_tv.setText("ISBN："+lost_book_data.get(position).get("book_isbn").toString());
                //设置书名
                all_borrow_books_name_tv.setText("书名："+lost_book_data.get(position).get("book_name").toString());
                //设置作者
                all_borrow_books_author_tv.setText("作者："+lost_book_data.get(position).get("book_author").toString());
                //设置出版社
                all_borrow_books_publisher_tv.setText("出版社："+lost_book_data.get(position).get("book_publisher").toString());
                //设置借阅日期
                all_borrow_books_borrowdate_tv.setText("借阅日期："+lost_book_data.get(position).get("borrow_date").toString());
                //设置归还日期
                all_borrow_books_backdate_tv.setText("归还日期："+lost_book_data.get(position).get("borrow_return_date").toString());
                if (Integer.valueOf(lost_book_data.get(position).get("borrow_statue").toString()) == 2){
                    all_borrow_books_backdate_tv.setTextColor(Color.RED);
                }
                all_borrow_books_state_tv.setText("赔偿金额："+Double.valueOf(lost_book_data.get(position).get("book_price").toString())+"元");
                return view;
            }
        };
        return adapter;
    }

    //绑定子项目控件
    private void initItemWidget(View convertView){
        //每一项的图书ISBN号
        all_borrow_books_isbn_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_isbn_tv);
        //每一项的图书名称
        all_borrow_books_name_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_name_tv);
        //每一项的图书作者
        all_borrow_books_author_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_author_tv);
        //每一项的图书出版社
        all_borrow_books_publisher_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_publisher_tv);
        //每一项的图书借阅日期
        all_borrow_books_borrowdate_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_borrowdate_tv);
        //每一项的图书归还日期
        all_borrow_books_backdate_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_backdate_tv);
        //借阅图书状态
        all_borrow_books_state_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_state_tv);
    }
}
