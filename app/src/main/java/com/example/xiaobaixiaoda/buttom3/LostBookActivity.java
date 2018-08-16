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

public class LostBookActivity extends AppCompatActivity {

    //声明控件
    ListView lost_book_listview;
    TextView tittle_tv,all_borrow_books_isbn_tv,all_borrow_books_name_tv,all_borrow_books_author_tv,all_borrow_books_publisher_tv,all_borrow_books_borrowdate_tv,all_borrow_books_lost_tv,all_borrow_books_price_tv;

    //定义变量
    ArrayList<HashMap<String,Object>> borrow_back_data = new ArrayList<>();

    //定义屏幕的宽高
    int screenW,screenH;

    //用户序号
    int user_id = -1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    //挂失图书集合下标
    int lost_book_position;

    //挂失标志位
    int do_lost_book_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_lost_book);

        //接收用户序号参数
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);

        //设置界面的标题文字
        setTitle("图书挂失赔偿");

        //绑定控件
        initWidget();

        //获取图书借阅信息数据
        getBorrowBackData();

        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 57){
                    //为ListView绑定适配器
                    lost_book_listview.setAdapter(createAdapter());
                    //为查看挂失未处理图书设置监听器
                    lost_book_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 && borrow_back_data.size() > 0){
                                Intent intent = new Intent(LostBookActivity.this,AllLostBooksActivity.class);
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                            }
                        }
                    });
                }else if (msg.what == 59){
                    if (do_lost_book_flag >= 0){
                        Toast.makeText(LostBookActivity.this, "挂失成功，请联系图书管理员缴纳赔偿金"+borrow_back_data.get(lost_book_position).get("book_price").toString()+"元", Toast.LENGTH_SHORT).show();
                        borrow_back_data.remove(lost_book_position);
                        baseAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(LostBookActivity.this, "挂失图书失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

    }

    //绑定控件
    private void initWidget(){
        //图书借阅信息列表
        lost_book_listview = (ListView)findViewById(R.id.lost_book_listview);
        //标题
        tittle_tv = (TextView) findViewById(R.id.tittle_tv);
        //设置标题
        tittle_tv.setText("挂失赔偿");
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
        //图书单价
        all_borrow_books_price_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_price_tv);
        //一键挂失提示
        all_borrow_books_lost_tv = (TextView) convertView.findViewById(R.id.all_borrow_books_lost_tv);
    }

    //创建ListView适配器
    private BaseAdapter createAdapter(){
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (borrow_back_data.size() > 0){
                    return borrow_back_data.size();
                }else {
                    return 1;
                }
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view;
                Log.d("borrow_back_data.size()的大小为：",borrow_back_data.size()+"");
                if (borrow_back_data.size() > 0){
                    Log.d("borrow_back_data.size() > 0时：",borrow_back_data.size()+"");
                    if (position == 0){
                        view = LayoutInflater.from(LostBookActivity.this).inflate(android.R.layout.simple_list_item_1,null);
                        TextView textView = (TextView)view.findViewById(android.R.id.text1);
                        textView.setText("查看已挂失未处理的图书");
                        textView.setTextSize(15);
                        textView.setTextColor(Color.BLUE);
                        textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                    }else {
                        view = LayoutInflater.from(LostBookActivity.this).inflate(R.layout.set_lost_book_item,null);
                        //绑定子项控件
                        initItemWidget(view);
                        //获取屏幕宽高
                        getScreenPixels();
                        //设置ISBN
                        all_borrow_books_isbn_tv.setWidth(screenW*2/3);
                        all_borrow_books_isbn_tv.setText("ISBN："+borrow_back_data.get(position).get("book_isbn").toString());
                        //设置书名
                        all_borrow_books_name_tv.setWidth(screenW*2/3);
                        all_borrow_books_name_tv.setText("书名："+borrow_back_data.get(position).get("book_name").toString());
                        //设置作者
                        all_borrow_books_author_tv.setWidth(screenW*2/3);
                        all_borrow_books_author_tv.setText("作者："+borrow_back_data.get(position).get("book_author").toString());
                        //设置出版社
                        all_borrow_books_publisher_tv.setWidth(screenW*2/3);
                        all_borrow_books_publisher_tv.setText("出版社："+borrow_back_data.get(position).get("book_publisher").toString());
                        //设置借阅日期
                        all_borrow_books_borrowdate_tv.setWidth(screenW*2/3);
                        all_borrow_books_borrowdate_tv.setText("借阅日期："+borrow_back_data.get(position).get("borrow_date").toString());
                        //设置图书单价
                        all_borrow_books_price_tv.setWidth(screenW*2/3);
                        all_borrow_books_price_tv.setText("图书单价："+borrow_back_data.get(position).get("book_price").toString()+"元");
                        //为一键挂失按钮设置监听器
                        all_borrow_books_lost_tv.setWidth(screenW/3);
                        all_borrow_books_lost_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(LostBookActivity.this, "一键挂失", Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(LostBookActivity.this).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要挂失"+borrow_back_data.get(position).get("book_name").toString()+"吗？\n挂失成功后将原价赔偿"+borrow_back_data.get(position).get("book_price").toString()+"元")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        lost_book_position = position;
                                                        //获取json字符串
                                                        int borrow_id = Integer.valueOf(borrow_back_data.get(position).get("borrow_id").toString());
                                                        int book_id = Integer.valueOf(borrow_back_data.get(position).get("book_id").toString());
                                                        int book_number = Integer.valueOf(borrow_back_data.get(position).get("book_number").toString());
                                                        int book_borrow_number = Integer.valueOf(borrow_back_data.get(position).get("book_borrow_number").toString());
                                                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doLostBook.php?borrow_id="+borrow_id+"&book_id="+book_id+"&book_number="+book_number+"&book_borrow_number="+book_borrow_number,"GET");
                                                        do_lost_book_flag = Integer.valueOf(str);
                                                        //getActivity().finish();
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 59;
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
                }else {
                    Log.d("borrow_back_data.size() <= 0时：",borrow_back_data.size()+"");
                    view = LayoutInflater.from(LostBookActivity.this).inflate(android.R.layout.simple_list_item_1,null);
                    TextView textView = (TextView)view.findViewById(android.R.id.text1);
                    textView.setText("暂无任何可挂失的图书");
                    textView.setTextSize(15);
                    textView.setTextColor(Color.BLUE);
                    textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                }
                return view;
            }
        };
        return baseAdapter;
    }

    //获取图书借阅信息数据
    private void getBorrowBackData(){
        //开启子线程，获取该用户的图书借阅信息
        new Thread(new Runnable(){
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getCanLostBookInfo.php?user_id="+user_id,"GET");
                Log.d("获取可以进行挂失操作的图书信息URL：","http://"+getString(R.string.sever_ip)+"/TJPUSever/getCanLostBookInfo.php?user_id="+user_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
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
                        map.put("book_price",jsonObject.getDouble("book_price"));
                        map.put("book_number",jsonObject.getInt("book_number"));
                        map.put("book_borrow_number",jsonObject.getInt("book_borrow_number"));
                        borrow_back_data.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 57;
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
