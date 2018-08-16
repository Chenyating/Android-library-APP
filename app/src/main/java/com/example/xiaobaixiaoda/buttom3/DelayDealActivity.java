package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;
import java.util.HashMap;

public class DelayDealActivity extends AppCompatActivity {

    //声明控件
    ListView delay_deal_listview;
    TextView tittle_tv,borrow_back_isbn_tv,borrow_back_name_tv,borrow_back_author_tv,borrow_back_publisher_tv,borrow_back_borrowdate_tv,borrow_back_backdate_tv,borrow_back_continueborrow_tv,borrow_back_delay_deal_tv,borrow_back_backbook_tv,borrow_back_delay_days_tv,borrow_back_delay_money_tv;

    //定义变量
    ArrayList<HashMap<String,Object>> borrow_back_data = new ArrayList<>();

    //定义屏幕宽高
    int screenW,screenH;

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
        setContentView(R.layout.activity_delay_deal);

        //接收用户参数
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);

        //设置界面的标题文字
        setTitle("图书逾期处理");

        //绑定控件
        initWidget();

        //获取图书借阅信息数据
        getBorrowBackData();

        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 56){
                    //为ListView绑定适配器
                    delay_deal_listview.setAdapter(createAdapter());

                    //为查看全部借阅信息设置监听器
                    delay_deal_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 && borrow_back_data.size() > 1){
                                Intent intent = new Intent(DelayDealActivity.this,AllBorrowBooksActivity.class);
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                                Toast.makeText(DelayDealActivity.this, "查看全部借阅信息", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        };

    }
    //绑定控件
    private void initWidget(){
        //标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置标题
        tittle_tv.setText("逾期处理");
        //图书借阅信息列表
        delay_deal_listview = (ListView)findViewById(R.id.delay_deal_listview);
    }
    //绑定子项目控件
    private void initItemWidget(View convertView){
        //每一项的图书ISBN号
        borrow_back_isbn_tv = (TextView) convertView.findViewById(R.id.borrow_back_isbn_tv);
        //每一项的图书名称
        borrow_back_name_tv = (TextView) convertView.findViewById(R.id.borrow_back_name_tv);
        //每一项的图书作者
        borrow_back_author_tv = (TextView) convertView.findViewById(R.id.borrow_back_author_tv);
        //每一项的图书出版社
        borrow_back_publisher_tv = (TextView) convertView.findViewById(R.id.borrow_back_publisher_tv);
        //每一项的图书借阅日期
        borrow_back_borrowdate_tv = (TextView) convertView.findViewById(R.id.borrow_back_borrowdate_tv);
        //每一项的图书归还日期
        borrow_back_backdate_tv = (TextView) convertView.findViewById(R.id.borrow_back_backdate_tv);
        //每一项的图书续借提示
        borrow_back_continueborrow_tv = (TextView) convertView.findViewById(R.id.borrow_back_continueborrow_tv);
        //每一项的图书逾期处理提示
        borrow_back_delay_deal_tv = (TextView) convertView.findViewById(R.id.borrow_back_delay_deal_tv);
        //每一项的图书归还提示
        borrow_back_backbook_tv = (TextView) convertView.findViewById(R.id.borrow_back_backbook_tv);
        //超期天数
        borrow_back_delay_days_tv = (TextView) convertView.findViewById(R.id.borrow_back_delay_days_tv);
        //应缴纳罚金数
        borrow_back_delay_money_tv = (TextView) convertView.findViewById(R.id.borrow_back_delay_money_tv);
    }

    //创建ListView适配器
    private BaseAdapter createAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return borrow_back_data.size();
            }

            @Override
            public Object getItem(int position) {
                return borrow_back_data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (position == 0){
                    view = LayoutInflater.from(DelayDealActivity.this).inflate(android.R.layout.simple_list_item_1,null);
                    TextView textView = (TextView)view.findViewById(android.R.id.text1);
                    if (borrow_back_data.size() > 1){
                        textView.setText("查看全部借阅图书信息");
                    }else{
                        textView.setText("暂无任何借阅逾期图书");
                    }
                    textView.setTextSize(15);
                    textView.setTextColor(Color.BLUE);
                    textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                }else {
                    view = LayoutInflater.from(DelayDealActivity.this).inflate(R.layout.borrow_back,null);
                    //绑定子项控件
                    initItemWidget(view);
                    //获取屏幕宽高
                    getScreenPixels();
                    //设置ISBN
                    borrow_back_isbn_tv.setWidth(screenW*2/3);
                    borrow_back_isbn_tv.setText("ISBN："+borrow_back_data.get(position).get("book_isbn").toString());
                    //设置书名
                    borrow_back_name_tv.setWidth(screenW*2/3);
                    borrow_back_name_tv.setText("书名："+borrow_back_data.get(position).get("book_name").toString());
                    //设置作者
                    borrow_back_author_tv.setWidth(screenW*2/3);
                    borrow_back_author_tv.setText("作者："+borrow_back_data.get(position).get("book_author").toString());
                    //设置出版社
                    borrow_back_publisher_tv.setWidth(screenW*2/3);
                    borrow_back_publisher_tv.setText("出版社："+borrow_back_data.get(position).get("book_publisher").toString());
                    //设置借阅日期
                    borrow_back_borrowdate_tv.setWidth(screenW*2/3);
                    borrow_back_borrowdate_tv.setText("借阅日期："+borrow_back_data.get(position).get("borrow_date").toString());
                    //设置归还日期
                    borrow_back_backdate_tv.setWidth(screenW*2/3);
                    borrow_back_backdate_tv.setText("归还日期："+borrow_back_data.get(position).get("borrow_return_date").toString());
                    borrow_back_backdate_tv.setTextColor(Color.RED);
                    //设置超期天数
                    borrow_back_delay_days_tv.setWidth(screenW*2/3);
                    borrow_back_delay_days_tv.setText("超期天数："+borrow_back_data.get(position).get("delay_days").toString()+"天");
                    borrow_back_delay_days_tv.setTextColor(Color.RED);
                    //设置罚金数
                    borrow_back_delay_money_tv.setWidth(screenW*2/3);
                    borrow_back_delay_money_tv.setText("缴纳罚金："+borrow_back_data.get(position).get("delay_money").toString()+"元");
                    borrow_back_delay_money_tv.setTextColor(Color.RED);
                    //显示逾期处理
                    borrow_back_delay_deal_tv.setWidth(screenW/3);
                    borrow_back_delay_deal_tv.setVisibility(View.VISIBLE);
                    //隐藏还书和续借提示
                    borrow_back_continueborrow_tv.setVisibility(View.GONE);
                    borrow_back_backbook_tv.setVisibility(View.GONE);
                    //为逾期处理设置监听器
                    borrow_back_delay_deal_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(DelayDealActivity.this, "该功能暂未开通，请到图书馆联系图书管理员进行罚金缴费", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return view;
            }
        };
        return adapter;
    }

    //获取图书借阅信息数据
    private void getBorrowBackData(){
        //开启子线程，获取该用户的图书借阅信息
        new Thread(new Runnable(){
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getDelayBookInfo.php?user_id="+user_id,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    HashMap<String,Object> map0 = new HashMap<>();
                    map0.put("book_id","");
                    map0.put("borrow_id","");
                    map0.put("book_isbn","");
                    map0.put("book_name","");
                    map0.put("book_author","");
                    map0.put("book_publisher","");
                    map0.put("borrow_date","");
                    map0.put("borrow_return_date","");
                    borrow_back_data.add(map0);
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
                        map.put("delay_days",jsonObject.getInt("delay_days"));
                        map.put("delay_money",jsonObject.getDouble("delay_money"));
                        borrow_back_data.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 56;
                //将消息对象发送给UI线程
                handler.sendMessage(message);
            }
        }).start();
    }
    /**
     * 获取屏幕的宽和高
     */
    public void getScreenPixels() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
    }
}
