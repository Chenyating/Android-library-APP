package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryBorrowRecordActivity extends AppCompatActivity {

    //声明TextView控件
    TextView tittle_tv,all_borrow_books_isbn_tv,all_borrow_books_name_tv,all_borrow_books_author_tv,all_borrow_books_publisher_tv,all_borrow_books_borrowdate_tv,all_borrow_books_state_tv,all_borrow_books_backdate_tv;
    //声明借阅记录列表控件
    private ListView history_borrow_record_listview;
    //定义变量
    ArrayList<HashMap<String,String>> history_borrow_record_data = new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_history_borrow_record);
        //绑定界面标题TextView控件
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置界面标题
        tittle_tv.setText("借阅记录");
        //绑定借阅记录列表ListView控件
        history_borrow_record_listview = (ListView)findViewById(R.id.history_borrow_record_listview);
        //获取借阅记录数据
        getHistoryBorrowRecordkData();
        //为ListView绑定适配器
        history_borrow_record_listview.setAdapter(createAdapter());
        //为ListView设置每一项点击监听器
        history_borrow_record_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(HistoryBorrowRecordActivity.this,DetailBookInfoActivity.class);
                startActivity(intent);
            }
        });
    }
    //获取借阅记录数据
    private void getHistoryBorrowRecordkData(){
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("ISBN","JSJ96543");
        map1.put("name","《黑珍珠号的诅咒》");
        map1.put("author","美国迪士尼公司I");
        map1.put("publisher","美国迪士尼公司");
        map1.put("borrow_date","2018.6.4");
        map1.put("back_date","2018.6.4");
        map1.put("book_state","借阅中");
        history_borrow_record_data.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("ISBN","JSJ84351");
        map2.put("name","《解忧杂货铺》");
        map2.put("author","东野圭谷");
        map2.put("publisher","角川书店");
        map2.put("borrow_date","2018.6.4");
        map2.put("back_date","2018.6.4");
        map2.put("book_state","借阅中");
        history_borrow_record_data.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("ISBN","JSJ15974");
        map3.put("name","《高级软件工程》");
        map3.put("author","罗铁坚");
        map3.put("publisher","中国科学院");
        map3.put("borrow_date","2017.10.25");
        map3.put("back_date","2017.11.25");
        map3.put("book_state","已过期");
        history_borrow_record_data.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("ISBN","JSJ85641");
        map4.put("name","《数据挖掘》");
        map4.put("author","刘莹");
        map4.put("publisher","中国科学院");
        map4.put("borrow_date","2017.10.25");
        map4.put("back_date","2017.11.25");
        map4.put("book_state","已挂失");
        history_borrow_record_data.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("ISBN","JSJ85641");
        map5.put("name","《PHP项目与典型功能模块开发全过程实录 第三版》");
        map5.put("author","明日科技");
        map5.put("publisher","清华大学出版社");
        map5.put("borrow_date","2017.10.25");
        map5.put("back_date","2017.11.25");
        map5.put("book_state","已过期");
        history_borrow_record_data.add(map5);
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
    //创建ListView适配器
    private BaseAdapter createAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return history_borrow_record_data.size();
            }

            @Override
            public Object getItem(int position) {
                return history_borrow_record_data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(HistoryBorrowRecordActivity.this).inflate(R.layout.all_borrow_books,null);
                //绑定子项控件
                initItemWidget(view);
                //设置ISBN
                all_borrow_books_isbn_tv.setText("ISBN："+history_borrow_record_data.get(position).get("ISBN"));
                //设置书名
                all_borrow_books_name_tv.setText("书名："+history_borrow_record_data.get(position).get("name"));
                //设置作者
                all_borrow_books_author_tv.setText("作者："+history_borrow_record_data.get(position).get("author"));
                //设置出版社
                all_borrow_books_publisher_tv.setText("出版社："+history_borrow_record_data.get(position).get("publisher"));
                //设置借阅日期
                all_borrow_books_borrowdate_tv.setText("借阅日期："+history_borrow_record_data.get(position).get("borrow_date"));
                //设置归还日期
                all_borrow_books_backdate_tv.setText("归还日期："+history_borrow_record_data.get(position).get("back_date"));
                if ((history_borrow_record_data.get(position).get("book_state") == "已过期") || (history_borrow_record_data.get(position).get("book_state") == "已挂失")){
                    all_borrow_books_backdate_tv.setTextColor(Color.RED);
                }
                //设置借阅图书状态
                all_borrow_books_state_tv.setText("借阅状态："+history_borrow_record_data.get(position).get("book_state"));
                switch (history_borrow_record_data.get(position).get("book_state")){
                    case "借阅中":
                        all_borrow_books_state_tv.setTextColor(Color.BLUE);
                        break;
                    case "已过期":
                        all_borrow_books_state_tv.setTextColor(Color.RED);
                        break;
                    case "已挂失":
                        all_borrow_books_state_tv.setTextColor(Color.RED);
                        break;
                    case "已归还":
                        all_borrow_books_state_tv.setTextColor(Color.GRAY);
                        break;
                }
                return view;
            }
        };
        return adapter;
    }
}
