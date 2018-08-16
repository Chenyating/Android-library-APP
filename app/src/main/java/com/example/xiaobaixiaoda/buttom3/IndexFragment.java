package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexFragment extends Fragment {
    //声明控件
    private SearchGridView gridView;
    private ArrayList<HashMap<String,Object>> data_list = new ArrayList<>();
    //获取广告轮播条图片
    private HashMap<String,String> url_maps = new HashMap<>();
    //private HashMap<String,Integer> url_maps_new = new HashMap<>();

    private SimpleAdapter simpleAdapter;
    private ScrollView index_fragment_scrollView;

    //用户序号：user_id
    private int user_id = -1;

    Handler handler;

    private View rootView = null;

    //构建数据
    private int[] icon = { R.drawable.search512, R.drawable.classify,R.drawable.borrow,
            R.drawable.yuqichuli, R.drawable.guashipeichangnew,
            R.drawable.shoucang_star512, R.drawable.fuwuzhinan,R.drawable.usually_question1,R.drawable.heirenwenhaonew};
        private  String[] iconName = { "图书查询", "图书分类","续借归还", "逾期处理", "挂失赔偿", "我的收藏", "服务指南","常见问题","馆内须知"};

    //************************新书到馆*************************************
    private TextView get_all_new_books_TextView;
    private SearchGridView new_books_GridView;
    private ArrayList<HashMap<String,Object>> new_books_list = new ArrayList<>();
    //private SimpleAdapter simpleAdapter_newbook;
    //private int[] new_boos_image = {R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan,R.drawable.javachengxuyuankaifazhinan};
    //private String[] new_boos_name = {"达芬奇密码","解忧杂货铺","为奴二十年","三体Ⅰ","冰与火之歌全集","敦刻尔克","刺客信条","美女与野兽"};
    //************************新书到馆*************************************

    public static IndexFragment newInstance(int user_id){
        IndexFragment fragment=new IndexFragment();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public IndexFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (rootView == null){
            //加载Fragment布局
            rootView = inflater.inflate(R.layout.fragment_index,container,false);

            //接收由MainActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("IndexFragment：user_id=",user_id+"");

            //绑定竖直滚动条控件
            index_fragment_scrollView = (ScrollView)rootView.findViewById(R.id.index_fragment_scrollView);
            index_fragment_scrollView.post(new Runnable() {
                //让scrollview跳转到顶部，必须放在runnable()方法中
                @Override
                public void run() {
                    index_fragment_scrollView.scrollTo(0, 0);
                }
            });

            //获取网格布局控件
            gridView = (SearchGridView) rootView.findViewById(R.id.gview);

            //获取新书到馆模块GridView控件
            new_books_GridView = (SearchGridView)rootView.findViewById(R.id.new_books_GridView);

            //获取新书到馆模块查看全部TextView控件
            get_all_new_books_TextView = (TextView)rootView.findViewById(R.id.get_all_new_books_TextView);

            //获取广告轮播条控件
            final SliderLayout mDemoSlider1 = (SliderLayout) rootView.findViewById(R.id.slider);

            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 7){
                        //新建适配器
                    /*String [] from_newbook ={"image","text"};
                    int [] to_newbook = {R.id.new_books_ImageView,R.id.new_books_name};
                    simpleAdapter_newbook = new SimpleAdapter(getActivity(), new_books_list, R.layout.new_books, from_newbook, to_newbook);
                    simpleAdapter_newbook.setViewBinder(new SimpleAdapter.ViewBinder(){
                        public boolean setViewValue(View view,Object data,String textRepresentation){
                            if(view instanceof ImageView && data instanceof Bitmap){
                                ImageView iv=(ImageView)view;
                                iv.setImageBitmap((Bitmap)data);
                                return true;
                            }
                            else return false;
                        }
                    });*/
                        //为新书到馆模块查看全部TextView绑定监听器
                        get_all_new_books_TextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), "点击查看全部，跳转到分类界面中的新书类别区", Toast.LENGTH_SHORT).show();
                            }
                        });
                        BaseAdapter baseAdapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return new_books_list.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                ViewHolder viewHolder;
                                View view = convertView;
                                view = getActivity().getLayoutInflater().inflate(R.layout.new_books, null);
                                viewHolder = new ViewHolder();
                                viewHolder.new_books_ImageView = (ImageView)view.findViewById(R.id.new_books_ImageView);
                                viewHolder.new_books_name = (TextView)view.findViewById(R.id.new_books_name);

                                viewHolder.new_books_name.setText(new_books_list.get(position).get("text").toString());
                                viewHolder.new_books_ImageView.setImageBitmap((Bitmap)new_books_list.get(position).get("image"));
                                //viewHolder.new_books_ImageView.setImageResource(R.drawable.javachengxuyuankaifazhinan);

                                return view;
                            }
                            class ViewHolder {
                                protected TextView new_books_name;
                                protected ImageView new_books_ImageView;
                            }
                        };
                        //配置适配器
                        new_books_GridView.setAdapter(baseAdapter);
                        //添加列表项点击的监听器
                        new_books_GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), "你点击的书名为："+new_books_list.get(position).get("text"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),DetailBookInfoActivity.class);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("book_id",(Integer) (new_books_list.get(position).get("book_id")));
                                startActivity(intent);
                            }
                        });
                    }else if (msg.what == 12){
                        //新建适配器
                        //String [] from ={"image","text"};
                        //int [] to = {R.id.image,R.id.text};
                        //simpleAdapter = new SimpleAdapter(getActivity(), data_list, R.layout.item, from, to);

                        BaseAdapter baseAdapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return data_list.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                ViewHolder viewHolder;
                                View view = convertView;
                                view = getActivity().getLayoutInflater().inflate(R.layout.item, null);
                                viewHolder = new ViewHolder();
                                viewHolder.item_image = (ImageView)view.findViewById(R.id.image);
                                viewHolder.item_text = (TextView)view.findViewById(R.id.text);

                                viewHolder.item_text.setText(data_list.get(position).get("text").toString());
                                viewHolder.item_image.setImageBitmap((Bitmap)data_list.get(position).get("image"));
                                //viewHolder.new_books_ImageView.setImageResource(R.drawable.javachengxuyuankaifazhinan);

                                return view;
                            }
                            class ViewHolder {
                                protected TextView item_text;
                                protected ImageView item_image;
                            }
                        };

                        //配置适配器
                        gridView.setAdapter(baseAdapter);
                        //添加列表项点击的监听器
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), "你点击了第"+position+"个图标\n功能是："+iconName[position], Toast.LENGTH_SHORT).show();
                                Intent intent;
                                switch (position){
                                    case 0:
                                        intent = new Intent(getActivity(), SearchBookIndexActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(getActivity(), ClassifyIndexActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        intent = new Intent(getActivity(), BorrowBackActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        intent = new Intent(getActivity(), DelayDealActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        startActivity(intent);
                                        break;
                                    case 4:
                                        intent = new Intent(getActivity(), LostBookActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        startActivity(intent);
                                        break;
                                    case 5:
                                        intent = new Intent(getActivity(), MyFavoriteActivity.class);
                                        intent.putExtra("user_id",user_id);
                                        startActivity(intent);
                                        break;
                                    case 6:
                                        intent = new Intent(getActivity(), ServiceGuideActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 7:
                                        intent = new Intent(getActivity(), NormalProblemActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 8:
                                        intent = new Intent(getActivity(), LibraryNoticeActivity.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        });
                    }else if (msg.what == 13){
                        //将图片加入到广告轮播条控件中
                        for(final String name : url_maps.keySet()){
                            // DefaultSliderView sliderView = new DefaultSliderView(this);
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) {
                                            Toast.makeText(getActivity(),"你点击了图片"+name,Toast.LENGTH_SHORT).show();
                                        }
                                    });//点击轮播图

                            //add your extra information 点击图片时可用到
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            mDemoSlider1.addSlider(textSliderView);
                        }
                        // 设置默认指示器位置(默认指示器白色,位置在sliderlayout底部)
                        mDemoSlider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        // 设置自定义指示器(位置自定义)
                        mDemoSlider1.setCustomIndicator((PagerIndicator) rootView.findViewById(R.id.custom_indicator));
                        // 设置TextView自定义动画
                        mDemoSlider1.setCustomAnimation(new DescriptionAnimation());
                        //mDemoSlider2.setCustomAnimation(new ChildAnimationExample()); // 多种效果，进入该类修改，参考效果github/daimajia/AndroidViewAnimations
                        // 设置持续时间
                        mDemoSlider1.setDuration(5000);
                        //mDemoSlider1.addOnPageChangeListener(this);//轮播图轮播监听
                    }
                }
            };

            //开启系线程，完成轮播图的显示
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getCarouselFigure.php","GET");
                    //Log.d("NEWBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getNewBookInfo.php");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        //Log.d("NewBookjsonArray",jsonArray.length()+"");
                        for (int i = 0;i<jsonArray.length();++i){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            url_maps.put(jsonObject.getString("carousel_figure_discription"), "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Carousel_Figure/"+jsonObject.getString("carousel_figure_name"));
                        }
                        //创建消息对象
                        Message message = new Message();
                        //为消息对象设置标识
                        message.what = 13;
                        //将消息对象发送给UI线程
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();

            //开启系线程，完成导航图标的显示
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //给data_list赋数据
                    //data_list = getData();
                /*for(int i=0;i<icon.length;i++){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("image", icon[i]);
                    map.put("text", iconName[i]);
                    data_list.add(map);
                }*/
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getIconInfo.php","GET");
                    //Log.d("NEWBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getNewBookInfo.php");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        //Log.d("IconjsonArray",jsonArray.length()+"");
                        for (int i = 0;i<jsonArray.length();++i){
                            HashMap<String,Object> map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //图片资源
                            String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Icon/"+jsonObject.getString("icon_image");
                            //Log.d("img_url",url);
                            //得到可用的图片
                            Bitmap bitmap = getHttpBitmap(url);
                            map.put("image", bitmap);
                            map.put("text", jsonObject.getString("icon_name"));
                            map.put("icon_id",jsonObject.getInt("icon_id"));
                            data_list.add(map);
                        }
                        //创建消息对象
                        Message message = new Message();
                        //为消息对象设置标识
                        message.what = 12;
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

            //*************************新书到馆************************************
            //构建新书到馆模块GridView中的item数据,给适配器集合赋数据
            //new_books_list =
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getNewBookInfo.php","GET");
                    Log.d("NEWBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getNewBookInfo.php");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        Log.d("NewBookjsonArray",jsonArray.length()+"");
                        for (int i = 0;i<jsonArray.length();++i){
                            HashMap<String,Object> map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //图片资源
                            String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Books/"+jsonObject.getString("book_image");
                            //得到可用的图片
                            Bitmap bitmap = getHttpBitmap(url);
                            map.put("image", bitmap);
                            map.put("text", jsonObject.getString("book_name"));
                            map.put("book_id",jsonObject.getInt("book_id"));
                            new_books_list.add(map);
                        }
                        //创建消息对象
                        Message message = new Message();
                        //为消息对象设置标识
                        message.what = 7;
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

            //*************************新书到馆************************************
            return rootView;
        }else {
            return rootView;
        }
    }

    /*private ArrayList<HashMap<String,Object>> getData(){
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<icon.length;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            list.add(map);
        }
        return list;
    }*/

    //给新书到馆模块的GridView的适配器赋数据
    /*private ArrayList<HashMap<String,Object>> getNewBooksData(){
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<new_boos_image.length;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", new_boos_image[i]);
            map.put("text", new_boos_name[i]);
            list.add(map);
        }
        return list;
    }*/
}