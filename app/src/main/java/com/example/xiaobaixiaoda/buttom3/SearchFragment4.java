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


public class SearchFragment4 extends Fragment {

    //声明控件
    private View rootView = null;
    private Spinner search_item_spinner;
    private String search_item = "论文";
    private SearchView sv1;
    private ArrayList<String> data = new ArrayList<>();
    private LinearLayout hot_search_content_linearlayout;
    private ArrayList<HashMap<String,Object>> paper_hot_search = new ArrayList<>();

    private int search_item_position = 0;   //下拉列表中的筛选项位置

    //声明传递过来的参数
    int user_id = -1;

    Handler handler;

    public SearchFragment4() {

    }

    public static SearchFragment4 newInstance(int user_id){
        SearchFragment4 fragment = new SearchFragment4();
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
            Log.d("SearchFragment4user_id=",user_id+"");

            //绑定Spinner控件
            search_item_spinner = (Spinner)rootView.findViewById(R.id.search_item_spinner);

            //设置Spinner数组
            final String[] arr = {"论文","作者","学位授予单位","学位类型","导师姓名"};

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
            sv1.setQueryHint("搜索学位论文资源");

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
                        // 此处为得到焦点时的处理内容
                        Intent intent = new Intent(getActivity(),SearchPaperActivity.class);
                        switch (search_item_position){
                            case 0:     //论文
                                intent.putExtra("search_type",1);
                                break;
                            case 1:     //作者
                                intent.putExtra("search_type",2);
                                break;
                            case 2:     //学位授予单位
                                intent.putExtra("search_type",3);
                                break;
                            case 3:     //学位类型
                                intent.putExtra("search_type",4);
                                break;
                            case 4:     //导师姓名
                                intent.putExtra("search_type",5);
                                break;
                            default:    //论文
                                intent.putExtra("search_type",1);
                                break;
                        }
                        intent.putExtra("search_resource",4);
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
                    for (int i = 0; i < paper_hot_search.size(); i++){
                        TextView textView = new TextView(getActivity());
                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(8, 8, 8, 8);
                        textView.setLayoutParams(lp);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(Color.rgb(51,102,255));
                        textView.setTextSize(15);
                        textView.setText(paper_hot_search.get(i).get("paper_name").toString());
                        textView.setOnClickListener(new MyOnClickListener(i));
                        hot_search_content_linearlayout.addView(textView);
                    }
                }
            };

            return rootView;
        }else {
            return rootView;
        }
    }

    /*@Override
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
    }*/

    //初始化数据
    private void intiData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getHotSearchPaperInfo.php","GET");
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
                        map.put("paper_id",jsonObject.getInt("paper_id"));
                        map.put("paper_name",jsonObject.getString("paper_name"));
                        map.put("paper_author",jsonObject.getString("paper_author"));
                        map.put("paper_school",jsonObject.getString("paper_school"));
                        map.put("paper_type",jsonObject.getString("paper_type"));
                        map.put("paper_date",jsonObject.getString("paper_date"));
                        map.put("paper_teacher",jsonObject.getString("paper_teacher"));
                        map.put("paper_content",jsonObject.getString("paper_content"));
                        map.put("paper_hot_search",jsonObject.getInt("paper_hot_search"));
                        paper_hot_search.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 45;
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
            Intent intent = new Intent(getActivity(),DetailPaperInfoActivity.class);
            intent.putExtra("user_id",user_id);
            intent.putExtra("paper_id",Integer.valueOf(paper_hot_search.get(position).get("paper_id").toString()));
            startActivity(intent);
        }
    }
}