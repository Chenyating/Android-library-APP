package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by xiaobaixiaoda on 2017/11/7.
 */

public class Fragment6 extends Fragment {
    private View rootView = null;
    private ListView my_favorite_CD_ListView;
    private ArrayList<HashMap<String,Object>> myFavoriteCDList = new ArrayList<>();
    private Handler handler;
    String json = "";
    int collect_flag = 1;
    int delete_cd_position = -1;
    //用户序号：user_id
    private int user_id = -1;
    BaseAdapter baseAdapter;

    public static Fragment6 newInstance(int user_id){
        Fragment6 fragment=new Fragment6();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment6(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局文件
            rootView = (View) inflater.inflate(R.layout.fragment6, container, false);

            //接收由MyFavouriteActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("Fragment6：user_id=",user_id+"");
            Toast.makeText(getActivity(), "Fragment6：user_id="+user_id, Toast.LENGTH_SHORT).show();

            //绑定ListView控件
            my_favorite_CD_ListView = (ListView)rootView.findViewById(R.id.my_favorite_CD_ListView);
            //初始化数据
            //initData();
            //开始接收子线程消息，根据消息内容，更改UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            Log.d("CD_JSON_DATA",json);
                            //构建适配器
                            baseAdapter = new BaseAdapter() {

                                @Override
                                public int getCount() {
                                    return myFavoriteCDList.size();
                                }

                                @Override
                                public Object getItem(int position) {
                                    return myFavoriteCDList.get(position);
                                }

                                @Override
                                public long getItemId(int position) {
                                    return position;
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    ViewHolder viewHolder;
                                    View view = convertView;
                                    if(view == null) {
                                        view = getActivity().getLayoutInflater().inflate(R.layout.fragment6_item, null);
                                        viewHolder = new ViewHolder();
                                        //光盘编号
                                        viewHolder.cd_number_tv = (TextView)view.findViewById(R.id.cd_number_tv);
                                        //光盘标题
                                        viewHolder.cd_tittle_tv = (TextView)view.findViewById(R.id.cd_tittle_tv);
                                        //光盘分类号
                                        viewHolder.cd_classify_number_tv = (TextView)view.findViewById(R.id.cd_classify_number_tv);
                                        viewHolder.cd_state_tv = (TextView)view.findViewById(R.id.cd_state_tv);
                                        viewHolder.cd_publish_identify_tv = (TextView)view.findViewById(R.id.cd_publish_identify_tv);
                                        viewHolder.cd_publisher_tv = (TextView)view.findViewById(R.id.cd_publisher_tv);
                                        viewHolder.cd_location_tv = (TextView)view.findViewById(R.id.cd_location_tv);
                                        viewHolder.cd_available_number = (TextView)view.findViewById(R.id.cd_available_number);
                                        view.setTag(viewHolder);
                                    } else {
                                        viewHolder = (ViewHolder) view.getTag();
                                    }
                                    viewHolder.cd_number_tv.setTextColor(Color.RED);
                                    viewHolder.cd_number_tv.setText( "光盘号：" + myFavoriteCDList.get(position).get("cd_number"));
                                    viewHolder.cd_tittle_tv.setText("光盘标题：" + myFavoriteCDList.get(position).get("cd_tittle"));
                                    viewHolder.cd_classify_number_tv.setText("光盘分类号：" + myFavoriteCDList.get(position).get("cd_classify_number"));
                                    viewHolder.cd_state_tv.setText("光盘状态：" + myFavoriteCDList.get(position).get("cd_statue"));
                                    viewHolder.cd_publish_identify_tv.setText("出版标识：" + myFavoriteCDList.get(position).get("cd_publish_identify"));
                                    viewHolder.cd_publisher_tv.setText("出版社：" + myFavoriteCDList.get(position).get("cd_publisher"));
                                    viewHolder.cd_location_tv.setText("资源位置：" + myFavoriteCDList.get(position).get("cd_location"));
                                    viewHolder.cd_available_number.setText("可用资源数量：" + myFavoriteCDList.get(position).get("cd_available_number"));
                                    return view;
                                }
                                class ViewHolder {
                                    protected TextView cd_number_tv,cd_tittle_tv,cd_classify_number_tv,cd_state_tv,cd_publish_identify_tv,cd_publisher_tv,cd_location_tv,cd_available_number;
                                }
                            };
                            //为ListView绑定适配器
                            my_favorite_CD_ListView.setAdapter(baseAdapter);
                            break;
                        case 2:
                            if (collect_flag > 0){
                                Log.d("CD_JSON_DATA","取消收藏成功！"+collect_flag+"\n"+delete_cd_position);
                                myFavoriteCDList.remove(delete_cd_position);
                                baseAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "取消收藏成功！", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.d("CD_JSON_DATA","取消收藏失败！"+collect_flag);
                                Toast.makeText(getActivity(), "取消收藏失败！", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

            //开启子线程进行网络访问，与服务器进行通信，获取用户收藏的光盘信息
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //获取json字符串
                    json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavorteCD.php?user_id="+user_id,"GET");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        for (int i = 0;i<jsonArray.length();++i){
                            HashMap<String,Object> map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            map.put("cd_id",jsonObject.getInt("cd_id"));
                            map.put("cd_number",jsonObject.getString("cd_number"));
                            map.put("cd_tittle",jsonObject.getString("cd_tittle"));
                            map.put("cd_classify_number",jsonObject.getString("cd_classify_number"));
                            if (jsonObject.getInt("cd_statue") == 1){
                                map.put("cd_statue","好");
                            }
                            if (jsonObject.getInt("cd_statue") == 0){
                                map.put("cd_statue","破损");
                            }
                            map.put("cd_publish_identify",jsonObject.getString("cd_publish_identify"));
                            map.put("cd_publisher",jsonObject.getString("cd_publisher"));
                            map.put("cd_location",jsonObject.getString("cd_location"));
                            map.put("cd_total_number",jsonObject.getInt("cd_total_number"));
                            map.put("cd_available_number",jsonObject.getInt("cd_available_number"));
                            myFavoriteCDList.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 1;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                }
            }).start();

            //为ListView的每个item绑定长按取消收藏监听器
            my_favorite_CD_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                    Toast.makeText(getActivity(), "取消收藏"+myFavoriteCDList.get(position).get("cd_tittle"), Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                            .setMessage("您确定要取消收藏"+myFavoriteCDList.get(position).get("cd_tittle")+"吗？")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            delete_cd_position = position;
                                            //获取json字符串
                                            int cd_id = (Integer) myFavoriteCDList.get(position).get("cd_id");
                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteCD.php?user_id="+user_id+"&cd_id="+cd_id,"GET");
                                            collect_flag = Integer.valueOf(str);
                                            //getActivity().finish();
                                            //创建消息对象
                                            Message message = new Message();
                                            //为消息对象设置标识
                                            message.what = 2;
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
                    return true;
                }
            });
        }else{
            Toast.makeText(getActivity(), "Fragment6的rootView不为空", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }
    //构建数据
    /*private void initData(){
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("cd_id","S0035728");
        map1.put("cd_tittle","PHP动态网站开发案例课堂");
        map1.put("cd_classify_id","TP312/6838");
        map1.put("cd_state","好");
        map1.put("cd_publish_id","ISBN978-7-89395-662-1");
        map1.put("cd_publisher","清华大学出版社");
        map1.put("cd_location","图书馆四楼电子阅览室B435室");
        map1.put("cd_num","6");
        myFavoriteCDList.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("cd_id","S0035728");
        map2.put("cd_tittle","PHP+MySQL网站开发入门与提高");
        map2.put("cd_classify_id","TP312/6838");
        map2.put("cd_state","好");
        map2.put("cd_publish_id","ISBN978-7-89395-662-1");
        map2.put("cd_publisher","清华大学出版社");
        map2.put("cd_location","图书馆四楼电子阅览室B435室");
        map2.put("cd_num","7");
        myFavoriteCDList.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("cd_id","S0035728");
        map3.put("cd_tittle","PHP+MySQL+Dreamweaver网站建设全程揭秘");
        map3.put("cd_classify_id","TP312/6838");
        map3.put("cd_state","好");
        map3.put("cd_publish_id","ISBN978-7-89395-662-1");
        map3.put("cd_publisher","清华大学出版社");
        map3.put("cd_location","图书馆四楼电子阅览室B435室");
        map3.put("cd_num","8");
        myFavoriteCDList.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("cd_id","S0035728");
        map4.put("cd_tittle","PHP从入门到精通");
        map4.put("cd_classify_id","TP312/6838");
        map4.put("cd_state","好");
        map4.put("cd_publish_id","ISBN978-7-89395-662-1");
        map4.put("cd_publisher","清华大学出版社");
        map4.put("cd_location","图书馆四楼电子阅览室B435室");
        map4.put("cd_num","9");
        myFavoriteCDList.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("cd_id","S0035728");
        map5.put("cd_tittle","细说PHP LAMP");
        map5.put("cd_classify_id","TP312/6838");
        map5.put("cd_state","好");
        map5.put("cd_publish_id","ISBN978-7-89395-662-1");
        map5.put("cd_publisher","清华大学出版社");
        map5.put("cd_location","图书馆四楼电子阅览室B435室");
        map5.put("cd_num","10");
        myFavoriteCDList.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("cd_id","S0035728");
        map6.put("cd_tittle","PHP编程新手自学手册 Php Bian Cheng Xin Shou Zi");
        map6.put("cd_classify_id","TP312/6838");
        map6.put("cd_state","好");
        map6.put("cd_publish_id","ISBN978-7-89395-662-1");
        map6.put("cd_publisher","清华大学出版社");
        map6.put("cd_location","图书馆四楼电子阅览室B435室");
        map6.put("cd_num","9");
        myFavoriteCDList.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("cd_id","S0035728");
        map7.put("cd_tittle","学通PHP的24堂课 130集大型多媒体教学视频");
        map7.put("cd_classify_id","TP312/6838");
        map7.put("cd_state","好");
        map7.put("cd_publish_id","ISBN978-7-89395-662-1");
        map7.put("cd_publisher","清华大学出版社");
        map7.put("cd_location","图书馆四楼电子阅览室B435室");
        map7.put("cd_num","8");
        myFavoriteCDList.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("cd_id","S0035728");
        map8.put("cd_tittle","Ajax+PHP 程序设计实战详解");
        map8.put("cd_classify_id","TP312/6838");
        map8.put("cd_state","好");
        map8.put("cd_publish_id","ISBN978-7-89395-662-1");
        map8.put("cd_publisher","清华大学出版社");
        map8.put("cd_location","图书馆四楼电子阅览室B435室");
        map8.put("cd_num","7");
        myFavoriteCDList.add(map8);
    }*/
}
