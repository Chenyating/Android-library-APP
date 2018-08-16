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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BorrowBackActivity extends AppCompatActivity {

    //声明控件
    ListView borrow_back_listview;
    TextView tittle_tv,borrow_back_isbn_tv,borrow_back_name_tv,borrow_back_author_tv,borrow_back_publisher_tv,borrow_back_borrowdate_tv,borrow_back_backdate_tv,borrow_back_continueborrow_tv,borrow_back_backbook_tv,borrow_back_delay_days_tv,borrow_back_delay_money_tv;

    //定义变量
    ArrayList<HashMap<String,Object>> borrow_back_data = new ArrayList<>();

    //定义屏幕宽高
    int screenW,screenH;

    //用户序号
    int user_id = -1;

    //是否完成续借标志位
    int do_continue_borrow_flag = -1;

    //续借图书位置
    int continue_book_position = -1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_borrow_back);

        //接收传递过来的用户序号参数
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);

        //设置界面的标题文字
        setTitle("图书续借与归还");

        //绑定控件
        initWidget();

        //获取图书借阅信息数据
        getBorrowBackData();

        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 54){
                    //为ListView绑定适配器
                    borrow_back_listview.setAdapter(createAdapter());

                    //为查看全部借阅信息设置监听器
                    borrow_back_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0 && borrow_back_data.size() > 1){
                                Intent intent = new Intent(BorrowBackActivity.this,AllBorrowBooksActivity.class);
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                                Toast.makeText(BorrowBackActivity.this, "查看全部借阅信息", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else if (msg.what == 58){
                    if (do_continue_borrow_flag > 0){
                        borrow_back_data.remove(continue_book_position);
                        baseAdapter.notifyDataSetChanged();
                        Toast.makeText(BorrowBackActivity.this, "续借图书成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BorrowBackActivity.this, "续借图书失败！请重试！", Toast.LENGTH_SHORT).show();
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
        tittle_tv.setText("图书续借与归还");
        //图书借阅信息列表
        borrow_back_listview = (ListView)findViewById(R.id.borrow_back_listview);
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
        //每一项的图书归还提示
        borrow_back_backbook_tv = (TextView) convertView.findViewById(R.id.borrow_back_backbook_tv);
        //超期天数
        borrow_back_delay_days_tv = (TextView) convertView.findViewById(R.id.borrow_back_delay_days_tv);
        //罚款金额
        borrow_back_delay_money_tv = (TextView) convertView.findViewById(R.id.borrow_back_delay_money_tv);
    }

    //创建ListView适配器
    private BaseAdapter createAdapter(){
        baseAdapter = new BaseAdapter() {
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view;
                if (position == 0){
                    Log.d("position==0时，borrow_back_data.size()的大小为：",borrow_back_data.size()+"");
                    view = LayoutInflater.from(BorrowBackActivity.this).inflate(android.R.layout.simple_list_item_1,null);
                    TextView textView = (TextView)view.findViewById(android.R.id.text1);
                    if (borrow_back_data.size() > 1){
                        textView.setText("查看全部借阅图书信息");
                    }else{
                        textView.setText("暂无任何可续借的图书");
                    }
                    textView.setTextSize(15);
                    textView.setTextColor(Color.BLUE);
                    textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                }else {
                    Log.d("position!=0时，borrow_back_data.size()的大小为：",borrow_back_data.size()+"");
                    view = LayoutInflater.from(BorrowBackActivity.this).inflate(R.layout.borrow_back,null);
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
                    //隐藏逾期天数
                    borrow_back_delay_days_tv.setVisibility(View.GONE);
                    //隐藏罚款金额
                    borrow_back_delay_money_tv.setVisibility(View.GONE);
                    //设置续借按钮宽高
                    borrow_back_continueborrow_tv.setWidth(screenW/3);
                    //设置还书按钮宽高
                    borrow_back_backbook_tv.setWidth(screenW/3);
                    //为续借设置监听器
                    borrow_back_continueborrow_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(BorrowBackActivity.this, "续借图书", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(BorrowBackActivity.this).setTitle("系统提示")//设置对话框标题
                                    .setMessage("您确定要续借"+borrow_back_data.get(position).get("book_name").toString()+"吗？")//设置显示的内容
                                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    continue_book_position = position;
                                                    //获取json字符串
                                                    int borrow_id = Integer.valueOf(borrow_back_data.get(position).get("borrow_id").toString());
                                                    String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doContinueBorrowBook.php?borrow_id="+borrow_id,"GET");
                                                    do_continue_borrow_flag = Integer.valueOf(str);
                                                    //getActivity().finish();
                                                    //创建消息对象
                                                    Message message = new Message();
                                                    //为消息对象设置标识
                                                    message.what = 58;
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
                    //为还书设置监听器
                    borrow_back_backbook_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Integer.valueOf(borrow_back_data.get(position).get("borrow_model").toString()) == 1){   //实体书籍
                                Toast.makeText(BorrowBackActivity.this, "该功能只对电子书籍的归还开放", Toast.LENGTH_SHORT).show();
                            }else if (Integer.valueOf(borrow_back_data.get(position).get("borrow_model").toString()) == 2){     //电子书籍
                                Toast.makeText(BorrowBackActivity.this, "电子书籍归还功能", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/CopyOfgetBorrowBackInfo.php?user_id="+user_id,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                Log.d("续借图书数量：",json);
                Log.d("续借图书URL：","http://"+getString(R.string.sever_ip)+"/TJPUSever/CopyOfgetBorrowBackInfo.php?user_id="+user_id);
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
                        map.put("borrow_model",jsonObject.getInt("borrow_model"));
                        borrow_back_data.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 54;
                //将消息对象发送给UI线程
                handler.sendMessage(message);
            }
        }).start();
        /*HashMap<String,String> map0 = new HashMap<>();
        map0.put("ISBN","");
        map0.put("name","");
        map0.put("author","");
        map0.put("publisher","");
        map0.put("borrow_date","");
        map0.put("back_date","");
        borrow_back_data.add(map0);
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("ISBN","JSJ96543");
        map1.put("name","《计算机体系结构》");
        map1.put("author","胡伟武");
        map1.put("publisher","中国科学院");
        map1.put("borrow_date","2017.10.25");
        map1.put("back_date","2017.11.25");
        borrow_back_data.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("ISBN","JSJ84351");
        map2.put("name","《算法分析大冒险》");
        map2.put("author","陈玉福");
        map2.put("publisher","中国科学院");
        map2.put("borrow_date","2017.10.25");
        map2.put("back_date","2017.11.25");
        borrow_back_data.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("ISBN","JSJ15974");
        map3.put("name","《高级软件工程》");
        map3.put("author","罗铁坚");
        map3.put("publisher","中国科学院");
        map3.put("borrow_date","2017.10.25");
        map3.put("back_date","2017.11.25");
        borrow_back_data.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("ISBN","JSJ85641");
        map4.put("name","《数据挖掘》");
        map4.put("author","刘莹");
        map4.put("publisher","中国科学院");
        map4.put("borrow_date","2017.10.25");
        map4.put("back_date","2017.11.25");
        borrow_back_data.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("ISBN","JSJ85641");
        map5.put("name","《PHP项目开发全过程实录 第三版》");
        map5.put("author","明日科技");
        map5.put("publisher","清华大学出版社");
        map5.put("borrow_date","2017.10.25");
        map5.put("back_date","2017.11.25");
        borrow_back_data.add(map5);*/
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
