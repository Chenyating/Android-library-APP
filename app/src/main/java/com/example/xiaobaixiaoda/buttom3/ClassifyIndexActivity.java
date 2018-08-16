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
import android.widget.ArrayAdapter;
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

public class ClassifyIndexActivity extends AppCompatActivity {

    //声明控件
    private ListView classify_listview;
    private TextView tittle_tv;

    //接收用户序号：user_id
    int user_id = -1;

    Intent intent;

    Handler handler;

    BaseAdapter baseAdapter;

    //设置ListView数据集合
    ArrayList<HashMap<String,Object>> classifyInfo = new ArrayList<HashMap<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        getSupportActionBar().hide();
        //加载界面布局文件
        setContentView(R.layout.activity_classify_index);
        //设置界面标题
        setTitle("全部分类");

        //接收user_id
        intent = getIntent();
        user_id = intent.getIntExtra("user_id",-1);
        Log.d("ClassifyIndexActivity:user_id = ",user_id+"");

        //绑定控件
        classify_listview = (ListView)findViewById(R.id.classify_listview);
        tittle_tv = (TextView)findViewById(R.id.tittle_tv);
        tittle_tv.setText("全部分类");

        //获取ListView数据集数据
        getClassifyInfoData();

        //接收子线程消息，更新主线程
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 28){
                    //为ListView通过匿名内部类的方法设置BaseAdapter适配器
                    BaseAdapter baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() { //获取item总项数
                            return classifyInfo.size();
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
                            View view = LayoutInflater.from(ClassifyIndexActivity.this).inflate(R.layout.classify,null);
                            //获取item布局中的控件
                            ImageView classify_ImageView = (ImageView)view.findViewById(R.id.classify_ImageView);
                            TextView classify_name = (TextView)view.findViewById(R.id.classify_name);
                            TextView classify_description = (TextView)view.findViewById(R.id.classify_description);
                            //设置ImageView的图片资源
                            classify_ImageView.setImageBitmap((Bitmap)classifyInfo.get(position).get("image"));
                            //设置书名
                            classify_name.setText((String)classifyInfo.get(position).get("type_name"));
                            //设置作者名
                            classify_description.setText((String)classifyInfo.get(position).get("type_description"));
                            return view;
                        }
                    };
                    //为ListView绑定BaseAdapter适配器
                    classify_listview.setAdapter(baseAdapter);
                    //为ListView设置点击监听器
                    classify_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(ClassifyIndexActivity.this, "您点击的位置是：" + position + "\n分类名为：" + (String) classifyInfo.get(position).get("classify_name"), Toast.LENGTH_SHORT).show();
                            /*
                             * 设置点击跳转。如果没有子分类，则跳转到DetailClassifyActivity。
                             * 如果有子分类，则跳转到SubClassifyActivity
                             */
                            Intent intent = new Intent();
                            if (!"无子分类".equals(classifyInfo.get(position).get("type_description").toString())){ //有子分类，则跳转到SubClassifyActivity
                                intent.setClass(ClassifyIndexActivity.this,SubClassifyActivity.class);
                                intent.putExtra("type_id",(Integer) classifyInfo.get(position).get("type_id"));
                                intent.putExtra("type_name",classifyInfo.get(position).get("type_name").toString());
                                intent.putExtra("user_id",user_id);
                            }else { //没有子分类，则跳转到DetailClassifyActivity
                                intent.putExtra("type_name",(String) classifyInfo.get(position).get("type_name"));
                                intent.putExtra("type_id",(Integer) classifyInfo.get(position).get("type_id"));
                                intent.putExtra("user_id",user_id);
                                intent.setClass(ClassifyIndexActivity.this,DetailClassifyActivity.class);
                            }
                            startActivity(intent);
                        }
                    });
                }
            }
        };

    }

    //获取ListView数据集合数据
    private void getClassifyInfoData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getBookClassifyInfo.php","GET");
                Log.d("CLASSIFYBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getBookClassifyInfo.php");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("ClassifyjsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //图片资源
                        String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Book_Classify/"+jsonObject.getString("type_image_name");
                        //得到可用的图片
                        Bitmap bitmap = getHttpBitmap(url);
                        map.put("image", bitmap);
                        map.put("type_name", jsonObject.getString("type_name"));
                        map.put("type_id",jsonObject.getInt("type_id"));
                        map.put("type_description",jsonObject.getString("type_description"));
                        classifyInfo.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 28;
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
        map1.put("classify_image",R.drawable.ic_launcher);
        map1.put("classify_name","小说");
        map1.put("classify_description","情感、民族等25类");
        classifyInfo.add(map1);
        HashMap<String,Object> map2 = new HashMap<String,Object>();
        map2.put("classify_image",R.drawable.ic_launcher);
        map2.put("classify_name","成功励志");
        map2.put("classify_description","礼仪、经典等20类");
        classifyInfo.add(map2);
        HashMap<String,Object> map3 = new HashMap<String,Object>();
        map3.put("classify_image",R.drawable.ic_launcher);
        map3.put("classify_name","经济管理");
        map3.put("classify_description","信息系统、一般管理等25类");
        classifyInfo.add(map3);
        HashMap<String,Object> map4 = new HashMap<String,Object>();
        map4.put("classify_image",R.drawable.ic_launcher);
        map4.put("classify_name","互联网");
        map4.put("classify_description","");
        classifyInfo.add(map4);
        HashMap<String,Object> map5 = new HashMap<String,Object>();
        map5.put("classify_image",R.drawable.ic_launcher);
        map5.put("classify_name","社科");
        map5.put("classify_description","非遗、各国文化等48类");
        classifyInfo.add(map5);
        HashMap<String,Object> map6 = new HashMap<String,Object>();
        map6.put("classify_image",R.drawable.ic_launcher);
        map6.put("classify_name","投资理财");
        map6.put("classify_description","证券股票、基金等12类");
        classifyInfo.add(map6);
        HashMap<String,Object> map7 = new HashMap<String,Object>();
        map7.put("classify_image",R.drawable.ic_launcher);
        map7.put("classify_name","青春");
        map7.put("classify_description","校园、情感等13类");
        classifyInfo.add(map7);
        HashMap<String,Object> map8 = new HashMap<String,Object>();
        map8.put("classify_image",R.drawable.ic_launcher);
        map8.put("classify_name","生活");
        map8.put("classify_description","茶酒饮料、宠物等60类");
        classifyInfo.add(map8);
        HashMap<String,Object> map9 = new HashMap<String,Object>();
        map9.put("classify_image",R.drawable.ic_launcher);
        map9.put("classify_name","历史");
        map9.put("classify_description","史家名著、逸文野史等14类");
        classifyInfo.add(map9);
        HashMap<String,Object> map10 = new HashMap<String,Object>();
        map10.put("classify_image",R.drawable.ic_launcher);
        map10.put("classify_name","计算机");
        map10.put("classify_description","期刊、数码杂志等26类");
        classifyInfo.add(map10);
        return classifyInfo;*/
    }
}
