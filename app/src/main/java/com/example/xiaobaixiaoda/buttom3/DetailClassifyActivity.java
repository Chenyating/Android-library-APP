package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailClassifyActivity extends AppCompatActivity {

    //声明控件
    private ListView detailclassify_listview;
    private TextView tittle_tv;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> DetailClassifyInfo = new ArrayList<HashMap<String,Object>>();

    //用户序号：uesr_id
    int user_id = -1;

    //大分类序号：type_id
    int type_id = -1;

    //大分类名称：type_name
    String type_name = "";

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_detail_classify);

        //获取参数
        intent = getIntent();
        type_name = intent.getStringExtra("type_name");
        user_id = intent.getIntExtra("user_id",-1);
        type_id = intent.getIntExtra("type_id",-1);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText(type_name);

        //绑定控件
        detailclassify_listview = (ListView)findViewById(R.id.detailclassify_listview);

        //获取ListView数据集数据
        getClassifyInfoData();

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 32){
                    //为ListView通过匿名内部类的方法设置BaseAdapter适配器
                    baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() { //获取item总项数
                            return DetailClassifyInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {   //获取指定的item
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {   //获取指定的itemId
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) { //重写该方法，该方法返回的View将作为列表框
                            View view = LayoutInflater.from(DetailClassifyActivity.this).inflate(R.layout.detail_classify,null);
                            //获取item布局中的控件
                            ImageView detailclassify_ImageView = (ImageView)view.findViewById(R.id.detailclassify_ImageView);
                            TextView detailclassify_name = (TextView)view.findViewById(R.id.detailclassify_name);
                            TextView detailclassify_author = (TextView)view.findViewById(R.id.detailclassify_author);
                            TextView detailclassify_publisher = (TextView)view.findViewById(R.id.detailclassify_publisher);
                            TextView detailclassify_description = (TextView)view.findViewById(R.id.detailclassify_description);
                            //设置ImageView的图片资源
                            detailclassify_ImageView.setImageBitmap((Bitmap)DetailClassifyInfo.get(position).get("image"));
                            //设置书名
                            detailclassify_name.setText((String)DetailClassifyInfo.get(position).get("book_name"));
                            //设置作者名
                            detailclassify_author.setText("作者："+(String)DetailClassifyInfo.get(position).get("book_author"));
                            //设置出版社
                            detailclassify_publisher.setText("出版社："+DetailClassifyInfo.get(position).get("book_publisher"));
                            //设置描述
                            detailclassify_description.setText("简介："+(String)DetailClassifyInfo.get(position).get("book_introduce"));
                            return view;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    detailclassify_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    detailclassify_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(DetailClassifyActivity.this, "点击的图书是："+DetailClassifyInfo.get(position).get("book_name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(DetailClassifyActivity.this,DetailBookInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("book_id",(Integer) DetailClassifyInfo.get(position).get("book_id"));
                            startActivity(intent1);
                        }
                    });
                }
            }
        };

    }
    //获取ListView数据集合数据
    private void getClassifyInfoData(){

        //开启子线程，从服务器获取图书信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getDetailClassifyAllBook.php?type_id="+type_id,"GET");
                Log.d("ClassifyAllBookURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getClassifyAllBook.php?type_id="+type_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("NewBookjsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //图片资源
                        String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Books/"+jsonObject.getString("book_list_image");
                        //得到可用的图片
                        Bitmap bitmap = getHttpBitmap(url);
                        map.put("image", bitmap);
                        map.put("book_name", jsonObject.getString("book_name"));
                        map.put("book_id",jsonObject.getInt("book_id"));
                        map.put("book_author", jsonObject.getString("book_author"));
                        map.put("book_publisher", jsonObject.getString("book_publisher"));
                        map.put("book_introduce", jsonObject.getString("book_introduce"));
                        DetailClassifyInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 32;
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
}
