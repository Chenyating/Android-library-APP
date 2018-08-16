package com.example.xiaobaixiaoda.buttom3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Map;

public class SubClassifyActivity extends Activity implements OnClickListener{

    public static int screenW, screenH;
    private TextView topTv,main_tv,urm_top_tv1;     //urm_top_tv1为子分类名称TextView
    private LinearLayout topll;     //为显示子分类下拉列表界面线性布局
    private TextView topLineTv,urm_top_tv;      //urm_top_tv为大分类名称TextView
    private TopMiddlePopup middlePopup;
    private ListView sub_classify_listview;     //分类图书列表ListView
    ArrayList<HashMap<String,Object>> SubClassifyInfo = new ArrayList<HashMap<String,Object>>();    //子分类图书信息
    ArrayList<HashMap<String,Object>> SubClassifyInfo1 = new ArrayList<HashMap<String,Object>>();    //子分类图书信息
    ArrayList<String> sub_classify_item = new ArrayList<>();    //子分类项集合
    ArrayList<HashMap<String,Object>> sub_classify_item1 = new ArrayList<>();

    //用户序号：user_id
    int user_id = -1;

    //分类序号：type_id
    int type_id = -1;

    //分类名：type_name
    String type_name = "";

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //加载布局
        setContentView(R.layout.activity_sub_classify);
        //获取屏幕长宽尺寸
        getScreenPixels();

        //接收分类序号
        intent = getIntent();
        type_id = intent.getIntExtra("type_id",-1);
        user_id = intent.getIntExtra("user_id",-1);
        type_name = intent.getStringExtra("type_name");
        //Log.d("SubClassifyActivity:type_id = ",type_id+"");

        //初始化各种控件
        initWidget();

        //获取该分类图书数据
        getSubClassifyInfoData();

        //获取子分类信息
        getItemsName();

        //处理子线程消息，更新主线程UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 29){
                    //为ListView设置初始适配器
                    baseAdapter = new BaseAdapter() {   //图书信息列表适配器
                        @Override
                        public int getCount() { //获取item总项数
                            return SubClassifyInfo.size();
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
                            View view = LayoutInflater.from(SubClassifyActivity.this).inflate(R.layout.detail_classify,null);
                            //获取item布局中的控件
                            ImageView detailclassify_ImageView = (ImageView)view.findViewById(R.id.detailclassify_ImageView);
                            TextView detailclassify_name = (TextView)view.findViewById(R.id.detailclassify_name);
                            TextView detailclassify_author = (TextView)view.findViewById(R.id.detailclassify_author);
                            TextView detailclassify_publisher = (TextView)view.findViewById(R.id.detailclassify_publisher);
                            TextView detailclassify_description = (TextView)view.findViewById(R.id.detailclassify_description);
                            //设置ImageView的图片资源
                            detailclassify_ImageView.setImageBitmap((Bitmap)SubClassifyInfo.get(position).get("image"));
                            //设置书名
                            detailclassify_name.setText((String)SubClassifyInfo.get(position).get("book_name"));
                            //设置作者名
                            detailclassify_author.setText("作者："+(String)SubClassifyInfo.get(position).get("book_author"));
                            //设置出版社
                            detailclassify_publisher.setText("出版社："+SubClassifyInfo.get(position).get("book_publisher"));
                            //设置描述
                            detailclassify_description.setText("简介："+(String)SubClassifyInfo.get(position).get("book_introduce"));
                            return view;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    sub_classify_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    sub_classify_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(SubClassifyActivity.this, "点击的图书是："+SubClassifyInfo.get(position).get("book_name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(SubClassifyActivity.this,DetailBookInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("book_id",(Integer) SubClassifyInfo.get(position).get("book_id"));
                            startActivity(intent1);
                        }
                    });
                }else if (msg.what == 30){
                    urm_top_tv1.setText(sub_classify_item.get(0).toString());
                }else if (msg.what == 31){
                    SubClassifyInfo.clear();
                    SubClassifyInfo.addAll(SubClassifyInfo1);
                    baseAdapter.notifyDataSetChanged();
                    //为ListView设置初始适配器
                    /*baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() { //获取item总项数
                            return SubClassifyInfo.size();
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
                            View view = LayoutInflater.from(SubClassifyActivity.this).inflate(R.layout.detail_classify,null);
                            //获取item布局中的控件
                            ImageView detailclassify_ImageView = (ImageView)view.findViewById(R.id.detailclassify_ImageView);
                            TextView detailclassify_name = (TextView)view.findViewById(R.id.detailclassify_name);
                            TextView detailclassify_author = (TextView)view.findViewById(R.id.detailclassify_author);
                            TextView detailclassify_publisher = (TextView)view.findViewById(R.id.detailclassify_publisher);
                            TextView detailclassify_description = (TextView)view.findViewById(R.id.detailclassify_description);
                            //设置ImageView的图片资源
                            detailclassify_ImageView.setImageBitmap((Bitmap)SubClassifyInfo.get(position).get("image"));
                            //设置书名
                            detailclassify_name.setText((String)SubClassifyInfo.get(position).get("book_name"));
                            //设置作者名
                            detailclassify_author.setText("作者："+(String)SubClassifyInfo.get(position).get("book_author"));
                            //设置出版社
                            detailclassify_publisher.setText("出版社："+SubClassifyInfo.get(position).get("book_publisher"));
                            //设置描述
                            detailclassify_description.setText("简介："+(String)SubClassifyInfo.get(position).get("book_introduce"));
                            return view;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    sub_classify_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    sub_classify_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(SubClassifyActivity.this, "点击的图书是："+SubClassifyInfo.get(position).get("book_name")+"\nitem位置是："+position, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(SubClassifyActivity.this,DetailBookInfoActivity.class);
                            intent1.putExtra("user_id",user_id);
                            intent1.putExtra("book_id",(Integer) SubClassifyInfo.get(position).get("book_id"));
                            startActivity(intent1);
                        }
                    });*/
                }
            }
        };

    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        topll = (LinearLayout) findViewById(R.id.urm_top_ll);
        topLineTv = (TextView) findViewById(R.id.rule_line_tv);
        topTv = (TextView) findViewById(R.id.urm_top_tv);
        urm_top_tv = (TextView)findViewById(R.id.urm_top_tv);
        //main_tv = (TextView)findViewById(R.id.main_tv);
        urm_top_tv1 = (TextView)findViewById(R.id.urm_top_tv1);

        urm_top_tv.setText(type_name);

        //为子分类下拉别表弹窗设置点击监听器
        topll.setOnClickListener(this);

        sub_classify_listview = (ListView)findViewById(R.id.sub_classify_listview);

    }

    //为子分类下拉别表弹窗的点击监听器实现点击方法，显示子分类弹窗
    @Override
    public void onClick(View v) {
        setPopup(0);
        middlePopup.show(topLineTv);
    }

    /**
     * 设置弹窗
     *
     * @param type
     */
    private void setPopup(int type) {
        middlePopup = new TopMiddlePopup(SubClassifyActivity.this, screenW, screenH,
                onItemClickListener, sub_classify_item, type);
    }

    /*
     * 设置弹窗内容
     */
    private void getItemsName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getSubClassifyItems.php?type_id="+type_id,"GET");
                //Log.d("ClassifyAllBookURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getClassifyAllBook.php?type_id="+type_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("NewBookjsonArray",jsonArray.length()+"");
                    sub_classify_item.add("全部");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<String, Object>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sub_classify_item.add(jsonObject.getString("subtype_name")+",共"+jsonObject.getInt("subtype_count")+"本书");
                        map.put("subtype_id",jsonObject.getInt("subtype_id"));
                        map.put("subtype_name",jsonObject.getString("subtype_name"));
                        map.put("subtype_count",jsonObject.getInt("subtype_count"));
                        sub_classify_item1.add(map);
                    }
                    if (jsonArray.length() >= 20){
                        for (int j = 1;j< (jsonArray.length() - 20);j++){
                            sub_classify_item.add("");
                        }
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 30;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /*sub_classify_item.add("全部 情感、民族等25类");
        sub_classify_item.add("情感 共1828本书");
        sub_classify_item.add("民族 共5本书");
        sub_classify_item.add("青春 共24本书");
        sub_classify_item.add("世界名著 共627本书");
        sub_classify_item.add("作品集 共1097本书");
        sub_classify_item.add("影视 共303本书");
        sub_classify_item.add("历史 共627本书");
        sub_classify_item.add("官场 共223本书");
        sub_classify_item.add("财经 共88本书");
        sub_classify_item.add("职场 共258本书");
        sub_classify_item.add("乡土 共201本书");
        sub_classify_item.add("都市 共1237本书");
        sub_classify_item.add("社会 共1636本书");
        sub_classify_item.add("当代 共580本书");
        sub_classify_item.add("军事 共264本书");
        sub_classify_item.add("武侠 共321本书");
        sub_classify_item.add("科幻 共397本书");
        sub_classify_item.add("魔幻 共215本书");
        sub_classify_item.add("惊悚 共354本书");
        sub_classify_item.add("悬疑 共1480本书");
        sub_classify_item.add("外国 共297本书");
        sub_classify_item.add("港澳台 共3本书");
        sub_classify_item.add("四大名著 共47本书");
        sub_classify_item.add("古典 共308本书");
        sub_classify_item.add("近代史 共84本书");
        sub_classify_item.add("");
        sub_classify_item.add("");*/
    }

    /*
    * 获取ListView数据集初始数据
    * */
    private void getSubClassifyInfoData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getClassifyAllBook.php?type_id="+type_id,"GET");
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
                        SubClassifyInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 29;
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
        SubClassifyInfo.add(map1);
        HashMap<String,Object> map2 = new HashMap<String,Object>();
        map2.put("detailclassify_image",R.drawable.jlb2_48);
        map2.put("detailclassify_name","加勒比海盗Ⅱ  聚魂棺");
        map2.put("detailclassify_author","美国迪士尼公司");
        map2.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map2.put("detailclassify_description","传说，大海之下住着一个饱受折磨的人，名叫戴维·琼斯，他那备受摧残却依然搏动着的心脏锁...");
        SubClassifyInfo.add(map2);
        HashMap<String,Object> map3 = new HashMap<String,Object>();
        map3.put("detailclassify_image",R.drawable.jlb3_48);
        map3.put("detailclassify_name","加勒比海Ⅲ  世界的尽头");
        map3.put("detailclassify_author","美国迪士尼公司");
        map3.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map3.put("detailclassify_description","从海盗的黄金时代起，海盗王们就一直统治着七大海域。东印度贸易公司的贝克特勋爵受命要...");
        SubClassifyInfo.add(map3);
        HashMap<String,Object> map4 = new HashMap<String,Object>();
        map4.put("detailclassify_image",R.drawable.jlb4_48);
        map4.put("detailclassify_name","加勒比海Ⅳ  惊涛怪浪");
        map4.put("detailclassify_author","美国迪士尼公司");
        map4.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map4.put("detailclassify_description","杰克·斯派洛船长再次扬帆，航行在惊涛怪浪之上，开始了一段异常危险的旅程——寻找传说...");
        SubClassifyInfo.add(map4);
        HashMap<String,Object> map5 = new HashMap<String,Object>();
        map5.put("detailclassify_image",R.drawable.jlb5_48);
        map5.put("detailclassify_name","加勒比Ⅴ  死无对证");
        map5.put("detailclassify_author","美国迪士尼公司");
        map5.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map5.put("detailclassify_description","《加勒比海盗》书影同步，同名电影5月26日上映。杰克·斯派洛船长发现自己的运气越来越糟...");
        SubClassifyInfo.add(map5);
        HashMap<String,Object> map6 = new HashMap<String,Object>();
        map6.put("detailclassify_image",R.drawable.jlb1_48);
        map6.put("detailclassify_name","加勒比海盗Ⅰ  黑珍珠号的诅咒");
        map6.put("detailclassify_author","美国迪士尼公司");
        map6.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map6.put("detailclassify_description","传说，加勒比海域有这样一艘鬼船出没——船长和所有的船员都遭到诅咒，要永远寻找一个神....");
        SubClassifyInfo.add(map6);
        HashMap<String,Object> map7 = new HashMap<String,Object>();
        map7.put("detailclassify_image",R.drawable.jlb2_48);
        map7.put("detailclassify_name","加勒比海盗Ⅱ  聚魂棺");
        map7.put("detailclassify_author","美国迪士尼公司");
        map7.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map7.put("detailclassify_description","传说，大海之下住着一个饱受折磨的人，名叫戴维·琼斯，他那备受摧残却依然搏动着的心脏锁...");
        SubClassifyInfo.add(map7);
        HashMap<String,Object> map8 = new HashMap<String,Object>();
        map8.put("detailclassify_image",R.drawable.jlb3_48);
        map8.put("detailclassify_name","加勒比海Ⅲ  世界的尽头");
        map8.put("detailclassify_author","美国迪士尼公司");
        map8.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map8.put("detailclassify_description","从海盗的黄金时代起，海盗王们就一直统治着七大海域。东印度贸易公司的贝克特勋爵受命要...");
        SubClassifyInfo.add(map8);
        HashMap<String,Object> map9 = new HashMap<String,Object>();
        map9.put("detailclassify_image",R.drawable.jlb4_48);
        map9.put("detailclassify_name","加勒比海Ⅳ  惊涛怪浪");
        map9.put("detailclassify_author","美国迪士尼公司");
        map9.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map9.put("detailclassify_description","杰克·斯派洛船长再次扬帆，航行在惊涛怪浪之上，开始了一段异常危险的旅程——寻找传说...");
        SubClassifyInfo.add(map9);
        HashMap<String,Object> map10 = new HashMap<String,Object>();
        map10.put("detailclassify_image",R.drawable.jlb5_48);
        map10.put("detailclassify_name","加勒比Ⅴ  死无对证");
        map10.put("detailclassify_author","美国迪士尼公司");
        map10.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map10.put("detailclassify_description","《加勒比海盗》书影同步，同名电影5月26日上映。杰克·斯派洛船长发现自己的运气越来越糟...");
        SubClassifyInfo.add(map10);*/
    }

    private void getSubClassifyDetailInfoData(int position){
        int subtype_id = 0;
        if ("全部".equals(sub_classify_item.get(position))){
            subtype_id = 0;
        }else {
            String str = sub_classify_item.get(position);
            String type_name = str.split(",")[0];
            for (int i = 0; i< sub_classify_item1.size();i++){
                if (type_name.equals(sub_classify_item1.get(i).get("subtype_name").toString())){
                    subtype_id = Integer.valueOf(sub_classify_item1.get(i).get("subtype_id").toString());
                    break;
                }
            }
        }
        Log.d("TYPE_NAME",type_name);
        final int finalSubtype_id = subtype_id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getSubClassifyBookInfo.php?subtype_id="+ finalSubtype_id +"&type_id="+type_id,"GET");
                Log.d("SubClassifyBookInfoURL1","http://"+getString(R.string.sever_ip)+"/TJPUSever/getSubClassifyBookInfo.php?subtype_id="+ finalSubtype_id +"&type_id="+type_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("SubClassifyBookInfojsonArray1",jsonArray.length()+"");
                    SubClassifyInfo1.clear();
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
                        SubClassifyInfo1.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 31;
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
        SubClassifyInfo.add(map1);
        HashMap<String,Object> map2 = new HashMap<String,Object>();
        map2.put("detailclassify_image",R.drawable.jlb2_48);
        map2.put("detailclassify_name","加勒比海盗Ⅱ  聚魂棺");
        map2.put("detailclassify_author","美国迪士尼公司");
        map2.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map2.put("detailclassify_description","传说，大海之下住着一个饱受折磨的人，名叫戴维·琼斯，他那备受摧残却依然搏动着的心脏锁...");
        SubClassifyInfo.add(map2);
        HashMap<String,Object> map3 = new HashMap<String,Object>();
        map3.put("detailclassify_image",R.drawable.jlb3_48);
        map3.put("detailclassify_name","加勒比海Ⅲ  世界的尽头");
        map3.put("detailclassify_author","美国迪士尼公司");
        map3.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map3.put("detailclassify_description","从海盗的黄金时代起，海盗王们就一直统治着七大海域。东印度贸易公司的贝克特勋爵受命要...");
        SubClassifyInfo.add(map3);
        HashMap<String,Object> map4 = new HashMap<String,Object>();
        map4.put("detailclassify_image",R.drawable.jlb4_48);
        map4.put("detailclassify_name","加勒比海Ⅳ  惊涛怪浪");
        map4.put("detailclassify_author","美国迪士尼公司");
        map4.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map4.put("detailclassify_description","杰克·斯派洛船长再次扬帆，航行在惊涛怪浪之上，开始了一段异常危险的旅程——寻找传说...");
        SubClassifyInfo.add(map4);
        HashMap<String,Object> map5 = new HashMap<String,Object>();
        map5.put("detailclassify_image",R.drawable.jlb5_48);
        map5.put("detailclassify_name","加勒比Ⅴ  死无对证");
        map5.put("detailclassify_author","美国迪士尼公司");
        map5.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map5.put("detailclassify_description","《加勒比海盗》书影同步，同名电影5月26日上映。杰克·斯派洛船长发现自己的运气越来越糟...");
        SubClassifyInfo.add(map5);
        HashMap<String,Object> map6 = new HashMap<String,Object>();
        map6.put("detailclassify_image",R.drawable.jlb1_48);
        map6.put("detailclassify_name","加勒比海盗Ⅰ  黑珍珠号的诅咒");
        map6.put("detailclassify_author","美国迪士尼公司");
        map6.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map6.put("detailclassify_description","传说，加勒比海域有这样一艘鬼船出没——船长和所有的船员都遭到诅咒，要永远寻找一个神....");
        SubClassifyInfo.add(map6);
        HashMap<String,Object> map7 = new HashMap<String,Object>();
        map7.put("detailclassify_image",R.drawable.jlb2_48);
        map7.put("detailclassify_name","加勒比海盗Ⅱ  聚魂棺");
        map7.put("detailclassify_author","美国迪士尼公司");
        map7.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map7.put("detailclassify_description","传说，大海之下住着一个饱受折磨的人，名叫戴维·琼斯，他那备受摧残却依然搏动着的心脏锁...");
        SubClassifyInfo.add(map7);
        HashMap<String,Object> map8 = new HashMap<String,Object>();
        map8.put("detailclassify_image",R.drawable.jlb3_48);
        map8.put("detailclassify_name","加勒比海Ⅲ  世界的尽头");
        map8.put("detailclassify_author","美国迪士尼公司");
        map8.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map8.put("detailclassify_description","从海盗的黄金时代起，海盗王们就一直统治着七大海域。东印度贸易公司的贝克特勋爵受命要...");
        SubClassifyInfo.add(map8);
        HashMap<String,Object> map9 = new HashMap<String,Object>();
        map9.put("detailclassify_image",R.drawable.jlb4_48);
        map9.put("detailclassify_name","加勒比海Ⅳ  惊涛怪浪");
        map9.put("detailclassify_author","美国迪士尼公司");
        map9.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map9.put("detailclassify_description","杰克·斯派洛船长再次扬帆，航行在惊涛怪浪之上，开始了一段异常危险的旅程——寻找传说...");
        SubClassifyInfo.add(map9);
        HashMap<String,Object> map10 = new HashMap<String,Object>();
        map10.put("detailclassify_image",R.drawable.jlb5_48);
        map10.put("detailclassify_name","加勒比Ⅴ  死无对证");
        map10.put("detailclassify_author","美国迪士尼公司");
        map10.put("detailclassify_publisher","美国迪士尼公司中国出版社");
        map10.put("detailclassify_description","《加勒比海盗》书影同步，同名电影5月26日上映。杰克·斯派洛船长发现自己的运气越来越糟...");
        SubClassifyInfo.add(map10);*/
    }

    /**
     * 弹窗点击事件
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            if (sub_classify_item.get(position) != ""){
                System.out.println("--onItemClickListener--:");
                Toast.makeText(SubClassifyActivity.this, "您点击的是："+sub_classify_item.get(position), Toast.LENGTH_SHORT).show();
                //main_tv.setText(getItemsName().get(position));
                if (position >= 0){
                    SubClassifyInfo.clear();
                    urm_top_tv1.setText(sub_classify_item.get(position));
                    //获取ListView数据集初始数据
                    getSubClassifyDetailInfoData(position);
                }
                middlePopup.dismiss();
            }
        }
    };

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
