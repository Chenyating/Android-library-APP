package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailBookInfoActivity extends AppCompatActivity {

    private TextView tittle_tv,detail_book_info_name_tv,detail_book_info_author_tv,detail_book_info_publisher_tv,detail_book_info_date_tv,detail_book_info_isbn_tv,detail_book_info_page_tv,detail_book_info_price_tv,detail_book_info_location_tv,detail_book_info_key_tv,detail_book_info_introduce_tv,detail_book_info_reading_online_tv,detail_book_info_order_tv,detail_book_info_back_online_tv,detail_book_info_collect_tv,detail_book_info_total_num_tv,detail_book_info_remain_num_tv;
    private ImageView detail_book_info_image_tv;

    int user_id = -1;
    int book_id = -1;
    //取消收藏是否成功标志位
    int collect_flag = -1;

    //是否成功收藏标志位
    int do_collect_flag = -1;

    //是否借阅成功标志位
    int borrow_flag = -1;

    //是否预约标志位
    int order_flag = -1;

    //是否加入书架成功标志位
    int do_add_shelf_flag = -1;

    Handler handler;
    Intent intent;

    private ArrayList<HashMap<String,Object>> detailBookInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载布局文件
        setContentView(R.layout.activity_detail_book_info);
        //设置标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("图书详情");

        //接收user_id
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        //接受book_id
        book_id = intent.getIntExtra("book_id",-1);

        //绑定控件
        initWidget();
        //开启子线程访问服务器，获取图书详细信息
        getBookDetailInfo();
        //主线程接收子线程消息，更新UI，显示信息
        showBookDetailInfo();
    }
    //绑定手环
    private void initWidget(){
        //图书图片
        detail_book_info_image_tv = (ImageView)findViewById(R.id.detail_book_info_image_tv);

        //图书名称
        detail_book_info_name_tv = (TextView)findViewById(R.id.detail_book_info_name_tv);
        //图书作者
        detail_book_info_author_tv = (TextView)findViewById(R.id.detail_book_info_author_tv);
        //图书出版社
        detail_book_info_publisher_tv = (TextView)findViewById(R.id.detail_book_info_publisher_tv);
        //图书出版日期
        detail_book_info_date_tv = (TextView)findViewById(R.id.detail_book_info_date_tv);
        //图书ISBN号
        detail_book_info_isbn_tv = (TextView)findViewById(R.id.detail_book_info_isbn_tv);
        //图书页数
        detail_book_info_page_tv = (TextView)findViewById(R.id.detail_book_info_page_tv);
        //原书定价
        detail_book_info_price_tv = (TextView)findViewById(R.id.detail_book_info_price_tv);
        //图书馆藏位置
        detail_book_info_location_tv = (TextView)findViewById(R.id.detail_book_info_location_tv);
        //图书主题词
        detail_book_info_key_tv = (TextView)findViewById(R.id.detail_book_info_key_tv);
        //图书总数量
        detail_book_info_total_num_tv = (TextView)findViewById(R.id.detail_book_info_total_num_tv);
        //图书剩余数量
        detail_book_info_remain_num_tv = (TextView)findViewById(R.id.detail_book_info_remain_num_tv);
        //图书内容提要
        detail_book_info_introduce_tv = (TextView)findViewById(R.id.detail_book_info_introduce_tv);

        //图书在线借阅按钮
        detail_book_info_reading_online_tv = (TextView)findViewById(R.id.detail_book_info_reading_online_tv);
        //图书在线阅读按钮
        detail_book_info_order_tv = (TextView)findViewById(R.id.detail_book_info_order_tv);
        //图书在线还书按钮
        detail_book_info_back_online_tv = (TextView)findViewById(R.id.detail_book_info_back_online_tv);
        //图书加入收藏按钮
        detail_book_info_collect_tv = (TextView)findViewById(R.id.detail_book_info_collect_tv);
    }

    //开启子线程访问服务器，获取图书详细信息
    private void getBookDetailInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailBookInfo.php?book_id="+book_id+"&user_id="+user_id,"GET");
                Log.d("DETAILBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailBookInfo.php?book_id="+book_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("jsonArray",jsonArray.length()+"");
                    //for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        //图片资源
                        String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Books/"+jsonObject.getString("book_image");
                        //得到可用的图片
                        Bitmap bitmap = getHttpBitmap(url);
                        map.put("book_image",bitmap);
                        map.put("book_name",jsonObject.getString("book_name"));
                        map.put("book_author",jsonObject.getString("book_author"));
                        map.put("book_id",jsonObject.getInt("book_id"));
                        map.put("book_isbn",jsonObject.getString("book_isbn"));
                        map.put("book_publisher",jsonObject.getString("book_publisher"));
                        map.put("book_publisher_date",jsonObject.getString("book_publisher_date"));
                        map.put("book_page",jsonObject.getInt("book_page"));
                        map.put("book_price",jsonObject.getDouble("book_price"));
                        map.put("book_location",jsonObject.getString("book_location"));
                        map.put("book_number",jsonObject.getInt("book_number"));
                        map.put("book_remain_number",jsonObject.getInt("book_remain_number"));
                        map.put("book_borrow_number",jsonObject.getInt("book_borrow_number"));
                        map.put("book_has_ordered_user_id",jsonObject.getInt("book_has_ordered_user_id"));
                        map.put("book_introduce",jsonObject.getString("book_introduce"));
                        map.put("book_key_words",jsonObject.getString("book_key_words"));
                        map.put("book_ebook_resource",jsonObject.getInt("book_ebook_resource"));
                        map.put("has_collected",jsonObject.getString("has_collected"));
                        map.put("has_borrowed",jsonObject.getString("has_borrowed"));
                        map.put("has_in_shelf",jsonObject.getString("has_in_shelf"));
                        detailBookInfo.add(map);
                    //}
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 9;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public Bitmap getHttpBitmap(String url){
                URL myFileURL;
                Bitmap bitmap=null;
                try{
                    myFileURL = new URL(url);
                    //获得连接
                    HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
                    //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
                    conn.setConnectTimeout(6000);
                    //连接设置获得数据流
                    conn.setDoInput(true);
                    //不使用缓存
                    conn.setUseCaches(false);
                    //这句可有可无，没有影响
                    //conn.connect();
                    //得到数据流
                    InputStream is = conn.getInputStream();
                    //解析得到图片
                    bitmap = BitmapFactory.decodeStream(is);
                    //关闭数据流
                    is.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return bitmap;
            }
        }).start();
    }

    //主线程接收子线程消息，更新UI，显示信息
    private void showBookDetailInfo(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 9){
                    //图书图片
                    detail_book_info_image_tv.setImageBitmap((Bitmap)(detailBookInfo.get(0).get("book_image")));

                    //图书名称
                    detail_book_info_name_tv.setText(detailBookInfo.get(0).get("book_name").toString());
                    //图书作者
                    detail_book_info_author_tv.setText(detailBookInfo.get(0).get("book_author").toString());
                    //图书出版社
                    detail_book_info_publisher_tv.setText(detailBookInfo.get(0).get("book_publisher").toString());
                    //图书出版日期
                    detail_book_info_date_tv.setText(detailBookInfo.get(0).get("book_publisher_date").toString());
                    //图书ISBN号
                    detail_book_info_isbn_tv.setText(detailBookInfo.get(0).get("book_isbn").toString());
                    //图书页数
                    detail_book_info_page_tv.setText(((Integer) detailBookInfo.get(0).get("book_page"))+"");
                    //原书定价
                    detail_book_info_price_tv.setText(((Double)(detailBookInfo.get(0).get("book_price")))+"");
                    //图书馆藏位置
                    detail_book_info_location_tv.setText(detailBookInfo.get(0).get("book_location").toString());
                    //图书主题词
                    detail_book_info_key_tv.setText(detailBookInfo.get(0).get("book_key_words").toString());
                    //图书总数量
                    detail_book_info_total_num_tv.setText(((Integer) detailBookInfo.get(0).get("book_number"))+"");
                    //图书剩余数量
                    detail_book_info_remain_num_tv.setText(((Integer) detailBookInfo.get(0).get("book_remain_number"))+"");
                    //图书内容提要book_has_ordered_user_id
                    detail_book_info_introduce_tv.setText(detailBookInfo.get(0).get("book_introduce").toString());

                    //判断是显示图书借阅还是显示图书预约，并为图书借阅（预约）按钮设置点击监听器
                    if (Integer.valueOf(detailBookInfo.get(0).get("book_remain_number").toString()) > 0){   //该图书有存量
                        if (Integer.valueOf(detailBookInfo.get(0).get("has_borrowed").toString()) > 0){     //该用户已经借阅过该图书
                            detail_book_info_reading_online_tv.setText("已经借阅");
                        }else{      //该用户没有借阅过该图书，且图书馆有存量，可以进行借阅
                            if (user_id == Integer.valueOf(detailBookInfo.get(0).get("book_has_ordered_user_id").toString())){  //说明是预约取书
                                detail_book_info_reading_online_tv.setText("预约取书");
                            }else {     //说明是正常借阅
                                detail_book_info_reading_online_tv.setText("图书借阅");
                            }
                            detail_book_info_reading_online_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(DetailBookInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                            .setMessage("您确定要借阅"+detailBookInfo.get(0).get("book_name").toString()+"吗？")//设置显示的内容
                                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doBorrowBook.php?user_id="+user_id+"&book_id="+book_id+"&book_remain_number="+detailBookInfo.get(0).get("book_remain_number").toString()+"&book_borrow_number="+detailBookInfo.get(0).get("book_borrow_number").toString()+"&book_has_ordered_user_id="+detailBookInfo.get(0).get("book_has_ordered_user_id").toString(),"GET");
                                                            Log.d("doBorrowBook.php",str);
                                                            borrow_flag = Integer.valueOf(str);
                                                            //创建消息对象
                                                            Message message = new Message();
                                                            //为消息对象设置标识
                                                            message.what = 61;
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
                        if ((user_id == Integer.valueOf(detailBookInfo.get(0).get("book_has_ordered_user_id").toString())) && (Integer.valueOf(detailBookInfo.get(0).get("book_has_ordered_user_id").toString()) > 0)){  //说明是预约取书
                            detail_book_info_reading_online_tv.setText("预约取书");
                            detail_book_info_reading_online_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(DetailBookInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                            .setMessage("您确定要借阅"+detailBookInfo.get(0).get("book_name").toString()+"吗？")//设置显示的内容
                                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doBorrowBook.php?user_id="+user_id+"&book_id="+book_id+"&book_remain_number="+detailBookInfo.get(0).get("book_remain_number").toString()+"&book_borrow_number="+detailBookInfo.get(0).get("book_borrow_number").toString()+"&book_has_ordered_user_id="+detailBookInfo.get(0).get("book_has_ordered_user_id").toString(),"GET");
                                                            Log.d("doBorrowBook.php",str);
                                                            borrow_flag = Integer.valueOf(str);
                                                            //创建消息对象
                                                            Message message = new Message();
                                                            //为消息对象设置标识
                                                            message.what = 61;
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
                        }else {     //说明是正常预约
                            detail_book_info_reading_online_tv.setText("图书预约");
                            detail_book_info_reading_online_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //如果该用户已经借阅了该图书，则不能再预约该图书
                                    if (Integer.valueOf(detailBookInfo.get(0).get("has_borrowed").toString()) > 0){
                                        Toast.makeText(DetailBookInfoActivity.this, "您已经借阅了该图书，无法再次预约！", Toast.LENGTH_SHORT).show();
                                    }else if((user_id != Integer.valueOf(detailBookInfo.get(0).get("book_has_ordered_user_id").toString())) && (Integer.valueOf(detailBookInfo.get(0).get("book_has_ordered_user_id").toString()) > 0)){
                                        Log.d("到底有没有人真正预约图书？",Integer.valueOf(detailBookInfo.get(0).get("book_has_ordered_user_id").toString())+"");
                                        Toast.makeText(DetailBookInfoActivity.this, "该图书已经被其他人预约，无法再次预约！", Toast.LENGTH_SHORT).show();
                                    }else {     //访问服务器，完成预约操作
                                        new AlertDialog.Builder(DetailBookInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                                .setMessage("您确定要预约"+detailBookInfo.get(0).get("book_name").toString()+"吗？")//设置显示的内容
                                                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                        // TODO Auto-generated method stub
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doOrderBook.php?user_id="+user_id+"&book_id="+book_id,"GET");
                                                                Log.d("doOrderBook.php",str);
                                                                order_flag = Integer.valueOf(str);
                                                                //创建消息对象
                                                                Message message = new Message();
                                                                //为消息对象设置标识
                                                                message.what = 62;
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
                                }
                            });
                        }
                    }

                    //图书收藏
                    if ("1".equals(detailBookInfo.get(0).get("has_collected").toString())){
                        detail_book_info_collect_tv.setText("取消收藏");
                        //为图书收藏设置点击监听器
                        detail_book_info_collect_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(DetailBookInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要取消收藏吗？")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteBook.php?user_id="+user_id+"&book_id="+book_id,"GET");
                                                        collect_flag = Integer.valueOf(str);
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 25;
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
                    }else if ("0".equals(detailBookInfo.get(0).get("has_collected").toString())) {
                        detail_book_info_collect_tv.setText("加入收藏");
                        detail_book_info_collect_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(DetailBookInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要收藏吗？")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/doFavorteBook.php?user_id="+user_id+"&book_id="+book_id,"GET");
                                                        do_collect_flag = Integer.valueOf(str);
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 60;
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

                    //在线阅读
                    detail_book_info_order_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("1".equals(detailBookInfo.get(0).get("has_in_shelf").toString())){  //用户已经将该本图书加入到书架中，点击在线阅读按钮后，可以直接跳转到内容界面中
                                Log.d("用户已经将该图书加入到书架中","用户已经将该图书加入到书架中");
                                Intent intent = new Intent(DetailBookInfoActivity.this,ReadBookOnlineActivity.class);
                                startActivity(intent);
                            }else if ("0".equals(detailBookInfo.get(0).get("has_in_shelf").toString())){    //用户还没有将该本图书加入到书架中，因此需要首先将图书加入到书架后才能跳转到内容界面
                                Log.d("用户没有将该图书加入到书架中","用户没有将该图书加入到书架中");
                                //需要开启子线程，将该图书加入到用户的书架中
                                new AlertDialog.Builder(DetailBookInfoActivity.this).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要将该图书加入书架并开始阅读吗？")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/addBookToMyShelf.php?user_id="+user_id+"&book_id="+book_id,"GET");
                                                        do_add_shelf_flag = Integer.valueOf(str);
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 68;
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
                        }
                    });

                }else if (msg.what == 25){
                    if (collect_flag > 0){
                        Toast.makeText(DetailBookInfoActivity.this, "图书取消收藏成功！", Toast.LENGTH_SHORT).show();
                        detail_book_info_collect_tv.setText("加入收藏");
                        Intent intent1 = new Intent();
                        intent1.putExtra("data_return_book_id",book_id);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }else {
                        Log.d("BOOK_JSON_DATA","图书取消收藏失败！"+collect_flag);
                        Toast.makeText(DetailBookInfoActivity.this, "图书取消收藏失败！", Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == 60){
                    if (do_collect_flag > 0){
                        Toast.makeText(DetailBookInfoActivity.this, "图书收藏成功！", Toast.LENGTH_SHORT).show();
                        detail_book_info_collect_tv.setText("取消收藏");
                        Intent intent1 = new Intent();
                        intent1.putExtra("data_return_book_id",book_id);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }else {
                        Log.d("BOOK_JSON_DATA","图书收藏失败！"+collect_flag);
                        Toast.makeText(DetailBookInfoActivity.this, "图书收藏失败！", Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == 61){
                    if (borrow_flag > 0){
                        Toast.makeText(DetailBookInfoActivity.this, "图书借阅成功！", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        intent1.putExtra("data_return_book_id",book_id);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }else if(borrow_flag == -2){    //该用户已经借阅了5本图书，达到借阅上限
                        Log.d("BOOK_JSON_DATA",borrow_flag+"图书借阅数量达到上限！"+collect_flag);
                        Toast.makeText(DetailBookInfoActivity.this, "图书借阅数量已达到上限！", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d("BOOK_JSON_DATA","图书借阅失败！"+collect_flag);
                        Toast.makeText(DetailBookInfoActivity.this, "图书借阅失败！", Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == 62){
                    if (order_flag > 0){
                        Toast.makeText(DetailBookInfoActivity.this, "图书预约成功！请关注预约信息，以免错过预约图书！", Toast.LENGTH_SHORT).show();
                    }else if (order_flag == -2){
                        Toast.makeText(DetailBookInfoActivity.this, "该图书已经被其他人预约，无法再次预约！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DetailBookInfoActivity.this, "图书预约失败！", Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == 68){  //处理用户将指定图书加入到书架的消息
                    if (do_add_shelf_flag == -2){   //说明用户的书架已满，不能再添加图书
                        Toast.makeText(DetailBookInfoActivity.this, "书架图书已满，不能添加图书！", Toast.LENGTH_SHORT).show();
                    }else if (do_add_shelf_flag >= 0){  //说明将该图书添加到书架成功，可以跳转到阅读界面
                        Toast.makeText(DetailBookInfoActivity.this, "图书已经添加到书架中！", Toast.LENGTH_SHORT).show();
                        detailBookInfo.get(0).put("has_in_shelf","1");
                        Intent intent = new Intent(DetailBookInfoActivity.this,ReadBookOnlineActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
    }
}
