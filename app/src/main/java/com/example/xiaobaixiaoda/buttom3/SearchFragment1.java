package com.example.xiaobaixiaoda.buttom3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;


public class SearchFragment1 extends Fragment {

    //声明控件
    private View rootView = null;
    private Spinner search_item_spinner;

    //设置图书搜索的搜索条件，默认为书名搜索
    private String search_item = "书名";
    private int search_item_position = 0;

    private SearchView sv1;
    private ArrayList<String> data = new ArrayList<>();
    private SearchListView guess_you_like_content_ListView;
    private TextView tittle_tv,change_guess_you_like_TextView;
    private ArrayList<HashMap<String,Object>> guessYouLikeBookList = new ArrayList<>();
    private ScrollView scrollView;
    private LinearLayout li_focus;
    //用户序号
    int user_id = -1;

    Handler handler;

    public SearchFragment1() {

    }

    public static SearchFragment1 newInstance(int user_id){
        SearchFragment1 fragment=new SearchFragment1();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局
            rootView = (View) inflater.inflate(R.layout.search_fragment1, container, false);

            //接收由SearchBookIndexActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("SearchFragment1user_id=",user_id+"");

            //绑定Spinner控件
            search_item_spinner = (Spinner)rootView.findViewById(R.id.search_item_spinner);

            //设置Spinner数组
            final String[] arr = {"书名","ISBN","著者","出版社"};

            //创建ArrayAdapter对象
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arr);

            //为Spinner设置Adapter
            search_item_spinner.setAdapter(arrayAdapter);

            //为spinner下拉菜单绑定监听事件
            search_item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity(), "你点击的是："+arr[position], Toast.LENGTH_SHORT).show();
                    Log.d("BookSearchSpinnerItem",arr[position]);
                    search_item = arr[position];
                    search_item_position = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //绑定搜索框控件
            sv1 = (SearchView)rootView.findViewById(R.id.sv1);

            //去掉搜索框外面的搜索图标
            int magId = getResources().getIdentifier("android:id/search_mag_icon",null, null);
            ImageView magImage = (ImageView) sv1.findViewById(magId);
            magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            //设置SearchView搜索框获取焦点后的监听事件，弹出搜索历史和热门搜索界面
            sv1.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // 此处为得到焦点时的处理内容
                        Log.d("第一个界面搜索框获取了焦点","从第一个界面弹出搜索历史和热门搜索界面");
                        Intent intent = new Intent(getActivity(),SearchBookActivity.class);
                        switch (search_item_position){
                            case 0:
                                intent.putExtra("search_type",1);
                                break;
                            case 1:
                                intent.putExtra("search_type",2);
                                break;
                            case 2:
                                intent.putExtra("search_type",3);
                                break;
                            case 3:
                                intent.putExtra("search_type",4);
                                break;
                            case 4:
                                intent.putExtra("search_type",5);
                                break;
                        }
                        intent.putExtra("search_resource",1);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        //Toast.makeText(getActivity(), "跳转到第二个搜索界面中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 此处为失去焦点时的处理内容
                    }
                }
            });

            //获取猜你喜欢模块换一批TextView控件
            change_guess_you_like_TextView = (TextView)rootView.findViewById(R.id.change_guess_you_like_TextView);

            //为猜你喜欢模块的换一批绑定点击事件
            change_guess_you_like_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "点击换一批，更换一批猜你喜欢推荐给用户的图书信息", Toast.LENGTH_SHORT).show();
                }
            });

            //初始化猜你喜欢数据
            //initGuessYouLikeBookData();
            //开启子线程，从服务器中获取猜你喜欢图书数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getGuessYouLikeBookInfo.php","GET");
                    Log.d("GUESSYOULIKEBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getGuessYouLikeBookInfo.php");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
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
                            map.put("book_id",jsonObject.getInt("book_id"));
                            guessYouLikeBookList.add(map);
                        }
                        //创建消息对象
                        Message message = new Message();
                        //为消息对象设置标识
                        message.what = 8;
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

            //获取猜你喜欢ListView控件
            guess_you_like_content_ListView = (SearchListView) rootView.findViewById(R.id.guess_you_like_content_ListView);

            //构建猜你喜欢适配器
            //GuessYouLikeAdapter guessYouLikeAdapter = new GuessYouLikeAdapter(getContext(),R.layout.guess_you_like,guessYouLikeBookList);
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 8){
                        //构建适配器
                        BaseAdapter baseAdapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return guessYouLikeBookList.size();
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
                                view = getActivity().getLayoutInflater().inflate(R.layout.guess_you_like, null);
                                viewHolder = new ViewHolder();
                                viewHolder.guess_you_like_ImageView = (ImageView)view.findViewById(R.id.guess_you_like_ImageView);
                                viewHolder.guess_you_like_book_name = (TextView)view.findViewById(R.id.guess_you_like_book_name);
                                viewHolder.guess_you_like_author_name = (TextView)view.findViewById(R.id.guess_you_like_author_name);

                                viewHolder.guess_you_like_book_name.setText(guessYouLikeBookList.get(position).get("book_name").toString());
                                viewHolder.guess_you_like_author_name.setText(guessYouLikeBookList.get(position).get("book_author").toString());
                                viewHolder.guess_you_like_ImageView.setImageBitmap((Bitmap)guessYouLikeBookList.get(position).get("book_list_image"));

                                return view;
                            }
                            class ViewHolder {
                                protected TextView guess_you_like_book_name,guess_you_like_author_name;
                                protected ImageView guess_you_like_ImageView;
                            }
                        };
                        //为猜你喜欢ListView绑定适配器
                        guess_you_like_content_ListView.setAdapter(baseAdapter);

                        //为猜你喜欢ListView的每个item绑定监听器
                        guess_you_like_content_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), "书名："+guessYouLikeBookList.get(position).get("book_name").toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),DetailBookInfoActivity.class);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("book_id",(Integer) (guessYouLikeBookList.get(position).get("book_id")));
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                }
            };

            scrollView = (ScrollView)rootView.findViewById(R.id.test_scrollView);
            scrollView.post(new Runnable() {
                //让scrollview跳转到顶部，必须放在runnable()方法中
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });

            return rootView;
        }else {
            return rootView;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        li_focus = (LinearLayout)rootView.findViewById(R.id.li_focus);
        li_focus.setFocusable(true);
        li_focus.setFocusableInTouchMode(true);
        li_focus.requestFocus();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        li_focus = (LinearLayout)rootView.findViewById(R.id.li_focus);
        li_focus.setFocusable(true);
        li_focus.setFocusableInTouchMode(true);
        li_focus.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        li_focus = (LinearLayout)rootView.findViewById(R.id.li_focus);
        li_focus.setFocusable(true);
        li_focus.setFocusableInTouchMode(true);
        li_focus.requestFocus();
    }

   @Override
    public void onStart() {
        super.onStart();
        li_focus = (LinearLayout)rootView.findViewById(R.id.li_focus);
        li_focus.setFocusable(true);
        li_focus.setFocusableInTouchMode(true);
        li_focus.requestFocus();
    }

    //构建猜你喜欢数据
    /*private void initGuessYouLikeBookData(){
        guessYouLikeBookList.add(new GuessYouLikeBook("加勒比海盗1   黑珍珠号的诅咒","迪士尼出品",R.drawable.jlb1_48));
        guessYouLikeBookList.add(new GuessYouLikeBook("加勒比海盗2   聚魂棺","迪士尼出品",R.drawable.jlb2_48));
        guessYouLikeBookList.add(new GuessYouLikeBook("加勒比海盗3   世界的尽头","迪士尼出品",R.drawable.jlb3_48));
        guessYouLikeBookList.add(new GuessYouLikeBook("加勒比海盗4   惊涛怪浪","迪士尼出品",R.drawable.jlb4_48));
        guessYouLikeBookList.add(new GuessYouLikeBook("加勒比海盗5   死无对证","迪士尼出品",R.drawable.jlb5_48));
    }*/

}



/* //绑定线性布局
        hot_search_content_linearlayout = (LinearLayout)findViewById(R.id.hot_search_content_linearlayout);
        //初始化数据
        intiData();
        //向线性布局中添加TextVIew
        for (int i = 0; i < 10; i++){
            TextView textView = new TextView(this);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(8, 8, 8, 8);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.rgb(51,102,255));
            textView.setTextSize(15);
            textView.setText(data.get(i));
            textView.setOnClickListener(new MyOnClickListener(i));
            hot_search_content_linearlayout.addView(textView);
        }

    }*/


/*//自定义点击事件监听器
    private class MyOnClickListener implements View.OnClickListener {
        int position;
        public MyOnClickListener(int i) {
            this.position = i;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(SearchFragment1.this, "点击的是："+data.get(this.position), Toast.LENGTH_SHORT).show();
        }
    }*/


/* //初始化数据
    private void intiData(){
        data.add("丛林大逃杀");
        data.add("火狐浏览器");
        data.add("择天记");
        data.add("浏览器");
        data.add("智学网");
        data.add("qq");
        data.add("微信");
        data.add("腾讯视频");
        data.add("淘宝");
        data.add("优酷");
    }*/