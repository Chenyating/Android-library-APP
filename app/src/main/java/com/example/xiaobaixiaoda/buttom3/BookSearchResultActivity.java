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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class BookSearchResultActivity extends AppCompatActivity {

    //声明控件
    private ListView book_search_result_listview;
    private TextView tittle_tv;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> BookSearchResultInfo = new ArrayList<HashMap<String,Object>>();

    int user_id = -1;
    String history_content = "";
    int search_resource = 1;
    int search_type = 1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();

        //加载布局文件
        setContentView(R.layout.activity_book_search_result);

        //接收参数
        intent = getIntent();
        history_content = intent.getStringExtra("history_content");
        user_id = intent.getIntExtra("user_id",-1);
        search_resource = intent.getIntExtra("search_resource",1);
        search_type = intent.getIntExtra("search_type",1);

        //设置界面标题
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("搜索“"+history_content+"”的结果");

        //绑定ListView控件
        book_search_result_listview = (ListView)findViewById(R.id.book_search_result_listview);

        //获取ListView数据集数据
        getBookSearchResultInfoData();

        //接收子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 37){
                    //为ListView通过匿名内部类的方法设置BaseAdapter适配器
                    baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() { //获取item总项数
                            return BookSearchResultInfo.size();
                        }

                        @Override
                        public Object getItem(int position) {   //获取指定的item
                            return BookSearchResultInfo.get(position);
                        }

                        @Override
                        public long getItemId(int position) {   //获取指定的itemId
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) { //重写该方法，该方法返回的View将作为列表框
                            View view = LayoutInflater.from(BookSearchResultActivity.this).inflate(R.layout.detail_classify,null);
                            //获取item布局中的控件
                            ImageView detailclassify_ImageView = (ImageView)view.findViewById(R.id.detailclassify_ImageView);
                            TextView detailclassify_name = (TextView)view.findViewById(R.id.detailclassify_name);
                            TextView detailclassify_author = (TextView)view.findViewById(R.id.detailclassify_author);
                            TextView detailclassify_publisher = (TextView)view.findViewById(R.id.detailclassify_publisher);
                            TextView detailclassify_description = (TextView)view.findViewById(R.id.detailclassify_description);
                            //设置ImageView的图片资源
                            detailclassify_ImageView.setImageBitmap((Bitmap)BookSearchResultInfo.get(position).get("book_list_image"));
                            //设置书名
                            detailclassify_name.setText((String)BookSearchResultInfo.get(position).get("book_name"));
                            //设置作者名
                            detailclassify_author.setText("作者："+(String)BookSearchResultInfo.get(position).get("book_author"));
                            //设置出版社
                            detailclassify_publisher.setText("出版社："+BookSearchResultInfo.get(position).get("book_author"));
                            //设置描述
                            detailclassify_description.setText("简介："+(String)BookSearchResultInfo.get(position).get("book_introduce"));
                            return view;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    book_search_result_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    book_search_result_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent1 = new Intent(BookSearchResultActivity.this,DetailBookInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("book_id",Integer.valueOf(BookSearchResultInfo.get(position).get("book_id").toString()));
                            startActivity(intent1);
                            //Toast.makeTeext(BookSearchResultActivity.this, "点击的图书是："+BookSearchResultInfo.get(position).get("detailclassify_name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };

    }

    //获取ListView数据集数据
    private void getBookSearchResultInfoData(){
        //开启子线程，从服务器获取全部图书信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    history_content = URLEncoder.encode(history_content,"UTF-8");
                    Log.d("history_content编码：",history_content);
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/searchResult.php?user_id="+user_id+"&search_resource="+search_resource+"&search_type="+search_type+"&history_content="+history_content,"GET");
                    Log.d("SearchBookURl：","http://"+getString(R.string.sever_ip)+"/TJPUSever/searchResult.php?user_id="+user_id+"&search_resource="+search_resource+"&search_type="+search_type+"&history_content="+history_content);
                    //解析json字符串
                    JSONArray jsonArray = new JSONArray(json);
                    Log.d("jsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //图片资源
                        String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Books/"+jsonObject.getString("book_list_image");
                        //得到可用的图片
                        Bitmap bitmap = getHttpBitmap(url);
                        map.put("book_list_image",bitmap);
                        map.put("book_name",jsonObject.getString("book_name"));
                        map.put("book_author",jsonObject.getString("book_author"));
                        map.put("book_introduce",jsonObject.getString("book_introduce"));
                        map.put("book_id",jsonObject.getInt("book_id"));
                        BookSearchResultInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 37;
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
        /*HashMap<String,Object> map1 = new HashMap<String,Object>();
        map1.put("detailclassify_image",R.drawable.jlb1_48);
        map1.put("detailclassify_name","加勒比海盗Ⅰ  黑珍珠号的诅咒");
        map1.put("detailclassify_author","美国迪士尼公司");
        map1.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map1.put("detailclassify_description","传说，加勒比海域有这样一艘鬼船出没——船长和所有的船员都遭到诅咒，要永远寻找一个神....");
        BookSearchResultInfo.add(map1);
        HashMap<String,Object> map2 = new HashMap<String,Object>();
        map2.put("detailclassify_image",R.drawable.jlb2_48);
        map2.put("detailclassify_name","加勒比海盗Ⅱ  聚魂棺");
        map2.put("detailclassify_author","美国迪士尼公司");
        map2.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map2.put("detailclassify_description","传说，大海之下住着一个饱受折磨的人，名叫戴维·琼斯，他那备受摧残却依然搏动着的心脏锁...");
        BookSearchResultInfo.add(map2);
        HashMap<String,Object> map3 = new HashMap<String,Object>();
        map3.put("detailclassify_image",R.drawable.jlb3_48);
        map3.put("detailclassify_name","加勒比海Ⅲ  世界的尽头");
        map3.put("detailclassify_author","美国迪士尼公司");
        map3.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map3.put("detailclassify_description","从海盗的黄金时代起，海盗王们就一直统治着七大海域。东印度贸易公司的贝克特勋爵受命要...");
        BookSearchResultInfo.add(map3);
        HashMap<String,Object> map4 = new HashMap<String,Object>();
        map4.put("detailclassify_image",R.drawable.jlb4_48);
        map4.put("detailclassify_name","加勒比海Ⅳ  惊涛怪浪");
        map4.put("detailclassify_author","美国迪士尼公司");
        map4.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map4.put("detailclassify_description","杰克·斯派洛船长再次扬帆，航行在惊涛怪浪之上，开始了一段异常危险的旅程——寻找传说...");
        BookSearchResultInfo.add(map4);
        HashMap<String,Object> map5 = new HashMap<String,Object>();
        map5.put("detailclassify_image",R.drawable.jlb5_48);
        map5.put("detailclassify_name","加勒比Ⅴ  死无对证");
        map5.put("detailclassify_author","美国迪士尼公司");
        map5.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map5.put("detailclassify_description","《加勒比海盗》书影同步，同名电影5月26日上映。杰克·斯派洛船长发现自己的运气越来越糟...");
        BookSearchResultInfo.add(map5);
        HashMap<String,Object> map6 = new HashMap<String,Object>();
        map6.put("detailclassify_image",R.drawable.jlb1_48);
        map6.put("detailclassify_name","加勒比海盗Ⅰ  黑珍珠号的诅咒");
        map6.put("detailclassify_author","美国迪士尼公司");
        map6.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map6.put("detailclassify_description","传说，加勒比海域有这样一艘鬼船出没——船长和所有的船员都遭到诅咒，要永远寻找一个神....");
        BookSearchResultInfo.add(map6);
        HashMap<String,Object> map7 = new HashMap<String,Object>();
        map7.put("detailclassify_image",R.drawable.jlb2_48);
        map7.put("detailclassify_name","加勒比海盗Ⅱ  聚魂棺");
        map7.put("detailclassify_author","美国迪士尼公司");
        map7.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map7.put("detailclassify_description","传说，大海之下住着一个饱受折磨的人，名叫戴维·琼斯，他那备受摧残却依然搏动着的心脏锁...");
        BookSearchResultInfo.add(map7);
        HashMap<String,Object> map8 = new HashMap<String,Object>();
        map8.put("detailclassify_image",R.drawable.jlb3_48);
        map8.put("detailclassify_name","加勒比海Ⅲ  世界的尽头");
        map8.put("detailclassify_author","美国迪士尼公司");
        map8.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map8.put("detailclassify_description","从海盗的黄金时代起，海盗王们就一直统治着七大海域。东印度贸易公司的贝克特勋爵受命要...");
        BookSearchResultInfo.add(map8);
        HashMap<String,Object> map9 = new HashMap<String,Object>();
        map9.put("detailclassify_image",R.drawable.jlb4_48);
        map9.put("detailclassify_name","加勒比海Ⅳ  惊涛怪浪");
        map9.put("detailclassify_author","美国迪士尼公司");
        map9.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map9.put("detailclassify_description","杰克·斯派洛船长再次扬帆，航行在惊涛怪浪之上，开始了一段异常危险的旅程——寻找传说...");
        BookSearchResultInfo.add(map9);
        HashMap<String,Object> map10 = new HashMap<String,Object>();
        map10.put("detailclassify_image",R.drawable.jlb5_48);
        map10.put("detailclassify_name","加勒比Ⅴ  死无对证");
        map10.put("detailclassify_author","美国迪士尼公司");
        map10.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map10.put("detailclassify_description","《加勒比海盗》书影同步，同名电影5月26日上映。杰克·斯派洛船长发现自己的运气越来越糟...");
        BookSearchResultInfo.add(map10);*/
    }
}
