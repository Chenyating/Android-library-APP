package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MyOrderActivity extends AppCompatActivity {

    //声明控件
    ListView my_order_listview;
    TextView tittle_tv,borrow_back_isbn_tv,borrow_back_name_tv,borrow_back_author_tv,borrow_back_publisher_tv,order_start_date_tv,order_end_date_tv,order_hold_start_date_tv,order_hold_end_date_tv,order_wait_hole_tv,order_canncle_tv;

    //定义变量
    ArrayList<HashMap<String,Object>> my_order_data = new ArrayList<>();

    //定义屏幕宽高
    int screenW,screenH;

    //用户序号
    int user_id = -1;

    //取消预约标志位
    int order_canncle_flag = -1;
    int order_canncle_positon = -1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_my_order);

        //接收用户参数
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);

        //绑定控件
        initWidget();

        //获取用户的预约信息
        getOrderBookData();

        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 63){
                    baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            if (my_order_data.size() > 0)
                                return my_order_data.size();
                            else
                                return 1;
                        }

                        @Override
                        public Object getItem(int position) {
                            return my_order_data.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            View view;
                            if (my_order_data.size() <= 0){
                                view = LayoutInflater.from(MyOrderActivity.this).inflate(android.R.layout.simple_list_item_1,null);
                                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                                textView.setText("暂无任何已预约的图书");
                                textView.setTextSize(15);
                                textView.setTextColor(Color.BLUE);
                                textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                            }else {
                                view = LayoutInflater.from(MyOrderActivity.this).inflate(R.layout.activity_my_order_item,null);
                                //绑定子项控件
                                initItemWidget(view);
                                //获取屏幕宽高
                                getScreenPixels();
                                //设置ISBN
                                borrow_back_isbn_tv.setWidth(screenW*2/3);
                                borrow_back_isbn_tv.setText("ISBN："+my_order_data.get(position).get("book_isbn").toString());
                                //设置书名
                                borrow_back_name_tv.setWidth(screenW*2/3);
                                borrow_back_name_tv.setText("书名："+my_order_data.get(position).get("book_name").toString());
                                //设置作者
                                borrow_back_author_tv.setWidth(screenW*2/3);
                                borrow_back_author_tv.setText("作者："+my_order_data.get(position).get("book_author").toString());
                                //设置出版社
                                borrow_back_publisher_tv.setWidth(screenW*2/3);
                                borrow_back_publisher_tv.setText("出版社："+my_order_data.get(position).get("book_publisher").toString());
                                //设置预约开始日期
                                order_start_date_tv.setWidth(screenW*2/3);
                                order_start_date_tv.setText("预约开始日期："+my_order_data.get(position).get("order_start_date").toString());
                                //设置预约结束日期
                                order_end_date_tv.setWidth(screenW*2/3);
                                order_end_date_tv.setText("预约结束日期："+my_order_data.get(position).get("order_end_date").toString());
                                //设置预约保留开始日期
                                order_hold_start_date_tv.setWidth(screenW*2/3);
                                order_hold_start_date_tv.setText("预约保留开始日期："+my_order_data.get(position).get("order_hold_start_date").toString());
                                //设置预约保留结束日期
                                order_hold_end_date_tv.setWidth(screenW*2/3);
                                order_hold_end_date_tv.setText("预约保留结束日期："+my_order_data.get(position).get("order_hold_end_date").toString());
                                //显示预约等待期或预约保留期
                                order_wait_hole_tv.setWidth(screenW/2);
                                if (Integer.valueOf(my_order_data.get(position).get("order_statue").toString()) == 1){
                                    order_wait_hole_tv.setText("预约等待期");
                                }else if (Integer.valueOf(my_order_data.get(position).get("order_statue").toString()) == 2){
                                    order_wait_hole_tv.setText("预约保留，点击取书");
                                    order_wait_hole_tv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MyOrderActivity.this,DetailBookInfoActivity.class);
                                            intent.putExtra("user_id",user_id);
                                            intent.putExtra("book_id",Integer.valueOf(my_order_data.get(position).get("book_id").toString()));
                                            startActivity(intent);
                                        }
                                    });
                                }
                                //显示图书预约取消
                                order_canncle_tv.setWidth(screenW/2);
                                order_canncle_tv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AlertDialog.Builder(MyOrderActivity.this).setTitle("系统提示")//设置对话框标题
                                                .setMessage("您确定要取消预约"+my_order_data.get(position).get("book_name").toString()+"吗？")//设置显示的内容
                                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                        // TODO Auto-generated method stub
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/canncleOrderBook.php?order_id="+Integer.valueOf(my_order_data.get(position).get("order_id").toString()),"GET");
                                                                Log.d("canncleOrderBook.php",str);
                                                                order_canncle_flag = Integer.valueOf(str);
                                                                if (order_canncle_flag > 0){
                                                                    order_canncle_positon = position;
                                                                }
                                                                //创建消息对象
                                                                Message message = new Message();
                                                                //为消息对象设置标识
                                                                message.what = 65;
                                                                //将消息对象发送给UI线程
                                                                handler.sendMessage(message);
                                                            }
                                                        }).start();
                                                    }
                                                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                                // TODO Auto-generated method stub
                                                Log.i("alertdialog"," 请保存数据！");
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                                    }
                                });

                            }
                            return view;
                        }
                    };

                    //为ListView设置适配器
                    my_order_listview.setAdapter(baseAdapter);
                }else if (msg.what == 65){
                    if (order_canncle_flag > 0){
                        Toast.makeText(MyOrderActivity.this, "已经成功取消预约图书"+my_order_data.get(order_canncle_positon).get("book_name").toString(), Toast.LENGTH_SHORT).show();
                        my_order_data.remove(order_canncle_positon);
                        baseAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
    }

    //绑定控件
    private void initWidget(){
        //标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        //设置标题
        tittle_tv.setText("图书预约");
        //图书借阅信息列表
        my_order_listview = (ListView)findViewById(R.id.my_order_listview);
    }

    //获取用户的预约信息
    private void  getOrderBookData(){
        //开启子线程，获取该用户的图书借阅信息
        new Thread(new Runnable(){
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getOrderBookInfo.php?user_id="+user_id,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("book_id",jsonObject.getInt("book_id"));
                        map.put("order_id",jsonObject.getInt("order_id"));
                        map.put("book_isbn",jsonObject.getString("book_isbn"));
                        map.put("book_name",jsonObject.getString("book_name"));
                        map.put("book_author",jsonObject.getString("book_author"));
                        map.put("book_publisher",jsonObject.getString("book_publisher"));
                        map.put("order_start_date",jsonObject.getString("order_start_date"));
                        map.put("order_end_date",jsonObject.getString("order_end_date"));
                        map.put("order_hold_start_date",jsonObject.getString("order_hold_start_date"));
                        map.put("order_hold_end_date",jsonObject.getString("order_hold_end_date"));
                        map.put("order_statue",jsonObject.getInt("order_statue"));
                        my_order_data.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 63;
                //将消息对象发送给UI线程
                handler.sendMessage(message);
            }
        }).start();
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
        //预约开始日期
        order_start_date_tv = (TextView) convertView.findViewById(R.id.order_start_date_tv);
        //预约结束日期
        order_end_date_tv = (TextView) convertView.findViewById(R.id.order_end_date_tv);
        //预约保留开始日期
        order_hold_start_date_tv = (TextView) convertView.findViewById(R.id.order_hold_start_date_tv);
        //预约保留结束日期
        order_hold_end_date_tv = (TextView) convertView.findViewById(R.id.order_hold_end_date_tv);
        //预约等待期
        order_wait_hole_tv = (TextView) convertView.findViewById(R.id.order_wait_hole_tv);
        //预约取消
        order_canncle_tv = (TextView) convertView.findViewById(R.id.order_canncle_tv);
    }

    //获取屏幕的宽和高
    public void getScreenPixels() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
    }
}
