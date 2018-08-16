package com.example.xiaobaixiaoda.buttom3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchFragment3 extends Fragment {

    //声明控件
    private View rootView = null;
    private Spinner search_item_spinner;
    private String search_item = "标题";
    private SearchView sv1;
    //private ArrayList<String> data = new ArrayList<>();
    private LinearLayout hot_search_content_linearlayout,li_focus;
    private ArrayList<HashMap<String,Object>> newspaper_hot_search = new ArrayList<>();

    //接收用户序号user_id
    int user_id = -1;

    //用于记录搜索项的下拉列表中的位置
    private int search_item_position = 0;

    Handler handler;

    public SearchFragment3() {

    }

    public static SearchFragment3 newInstance(int user_id){
        SearchFragment3 fragment = new SearchFragment3();
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
            rootView = (View) inflater.inflate(R.layout.search_fragment2, container, false);

            //接收由SearchBookIndexActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("SearchFragment3 user_id=",user_id+"");

            //绑定Spinner控件
            search_item_spinner = (Spinner)rootView.findViewById(R.id.search_item_spinner);

            //设置Spinner数组
            final String[] arr = {"标题","来源","著者"};

            //创建ArrayAdapter对象
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arr);

            //为Spinner设置Adapter
            search_item_spinner.setAdapter(arrayAdapter);

            //为spinner下拉菜单绑定监听事件
            search_item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "你点击的是："+arr[position], Toast.LENGTH_SHORT).show();
                    search_item = arr[position];
                    search_item_position = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //绑定搜索框控件
            sv1 = (SearchView)rootView.findViewById(R.id.sv1);

            //设置提示文字
            sv1.setQueryHint("搜索报纸资源");

            //去掉搜索框外面的搜索图标
            int magId = getResources().getIdentifier("android:id/search_mag_icon",null, null);
            ImageView magImage = (ImageView) sv1.findViewById(magId);
            magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            //设置SearchView搜索框获取焦点后的监听事件
            sv1.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // 此处为得到焦点时的处理内容
                        Intent intent = new Intent(getActivity(),SearchNewspaperActivity.class);
                        switch (search_item_position){
                            case 0:     //标题
                                intent.putExtra("search_type",1);
                                break;
                            case 1:     //来源
                                intent.putExtra("search_type",2);
                                break;
                            case 2:     //著者
                                intent.putExtra("search_type",3);
                                break;
                            default:    //标题
                                intent.putExtra("search_type",1);
                                break;
                        }
                        intent.putExtra("search_resource",3);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        //Toast.makeText(SearchBookIndexActivity.this, "跳转到第二个搜索界面中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 此处为失去焦点时的处理内容
                    }
                }
            });

            //绑定线性布局
            hot_search_content_linearlayout = (LinearLayout)rootView.findViewById(R.id.hot_search_content_linearlayout);

            //初始化数据
            intiData();

            //处理子线程消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    //向线性布局中添加TextVIew
                    for (int i = 0; i < newspaper_hot_search.size(); i++){
                        TextView textView = new TextView(getActivity());
                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(8, 8, 8, 8);
                        textView.setLayoutParams(lp);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(Color.rgb(51,102,255));
                        textView.setTextSize(15);
                        textView.setText(newspaper_hot_search.get(i).get("newspaper_tittle").toString());
                        textView.setOnClickListener(new MyOnClickListener(i));
                        hot_search_content_linearlayout.addView(textView);
                        /*textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),DetailNewspaperInfoActivity.class);
                                startActivity(intent);
                            }
                        });*/
                    }
                }
            };

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

    //初始化数据
    private void intiData(){
        /*data.add("丛林大逃杀");
        data.add("火狐浏览器");
        data.add("择天记");
        data.add("浏览器");
        data.add("智学网");
        data.add("qq");
        data.add("微信");
        data.add("腾讯视频");
        data.add("淘宝");
        data.add("优酷");*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getHotSearchNewspaperInfo.php","GET");
                Log.d("HotSearchQikanURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getHotSearchQikanInfo.php");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    //Log.d("jsonArray",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //Log.d("test",jsonObject.getString("qikan_tittle"));
                        map.put("newspaper_id",jsonObject.getInt("newspaper_id"));
                        map.put("newspaper_tittle",jsonObject.getString("newspaper_tittle"));
                        map.put("newspaper_date",jsonObject.getString("newspaper_date"));
                        map.put("newspaper_source",jsonObject.getString("newspaper_source"));
                        map.put("newspaper_author",jsonObject.getString("newspaper_author"));
                        map.put("newspaper_content",jsonObject.getString("newspaper_content"));
                        map.put("newspaper_location",jsonObject.getString("newspaper_location"));
                        map.put("newspaper_hot_search",jsonObject.getString("newspaper_hot_search"));
                        newspaper_hot_search.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 42;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //自定义点击事件监听器
    private class MyOnClickListener implements View.OnClickListener {
        int position;
        public MyOnClickListener(int i) {
            this.position = i;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),DetailNewspaperInfoActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("newspaper_id",Integer.valueOf(newspaper_hot_search.get(position).get("newspaper_id").toString()));
            startActivity(intent);
            //Toast.makeText(getActivity(), "点击的是："+data.get(this.position), Toast.LENGTH_SHORT).show();
        }
    }
}