package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xiaobaixiaoda on 2017/11/7.
 */

public class Fragment5 extends Fragment {
    private View rootView;
    private ListView my_favorite_chapter_ListView;
    private ArrayList<HashMap<String,Object>> myFavoriteChapterList = new ArrayList<>();

    //用户登录序号
    int user_id = -1;

    int collect_flag = -1;
    int delete_chapter_position = -1;

    private Handler handler;
    BaseAdapter baseAdapter;

    public static Fragment5 newInstance(int user_id){
        Fragment5 fragment=new Fragment5();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment5(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局文件
            rootView = (View) inflater.inflate(R.layout.fragment5, container, false);

            //接收由MyFavouriteActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("Fragment5：user_id=",user_id+"");
            Toast.makeText(getActivity(), "Fragment5：user_id="+user_id, Toast.LENGTH_SHORT).show();

            //绑定ListView
            my_favorite_chapter_ListView = (ListView)rootView.findViewById(R.id.my_favorite_chapter_ListView);

            //开启子线程，初始化数据
            initData();

            //接收子线程消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 22:
                            //构建适配器
                            baseAdapter = new BaseAdapter() {

                                @Override
                                public int getCount() {
                                    return myFavoriteChapterList.size();
                                }

                                @Override
                                public Object getItem(int position) {
                                    return myFavoriteChapterList.get(position);
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
                                        view = getActivity().getLayoutInflater().inflate(R.layout.fragment5_item, null);
                                        viewHolder = new ViewHolder();
                                        viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                        viewHolder.source_tv = (TextView)view.findViewById(R.id.source_tv);
                                        viewHolder.page_tv = (TextView)view.findViewById(R.id.page_tv);
                                        viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                        viewHolder.date_tv = (TextView)view.findViewById(R.id.date_tv);
                                        view.setTag(viewHolder);
                                    } else {
                                        viewHolder = (ViewHolder) view.getTag();
                                    }
                                    viewHolder.name_tv.setTextColor(Color.BLUE);
                                    viewHolder.name_tv.setText((position+1) + ". " + myFavoriteChapterList.get(position).get("chapter_name"));
                                    viewHolder.source_tv.setText("来 自：" + myFavoriteChapterList.get(position).get("chapter_source"));
                                    viewHolder.page_tv.setText("页码：第" + myFavoriteChapterList.get(position).get("chapter_page") + "页");
                                    viewHolder.author_tv.setText("作 者：" + myFavoriteChapterList.get(position).get("chapter_author"));
                                    viewHolder.date_tv.setText("出版时间：" + myFavoriteChapterList.get(position).get("chapter_publish_date") + "年");
                                    return view;
                                }
                                class ViewHolder {
                                    protected TextView name_tv,source_tv,page_tv,date_tv,author_tv;
                                }
                            };
                            //为ListView绑定适配器
                            my_favorite_chapter_ListView.setAdapter(baseAdapter);

                            //为ListView的每个item绑定长按取消收藏监听器
                            my_favorite_chapter_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    Toast.makeText(getActivity(), "取消收藏"+myFavoriteChapterList.get(position).get("chapter_name"), Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                                            .setMessage("您确定要取消收藏"+myFavoriteChapterList.get(position).get("chapter_name")+"吗？")//设置显示的内容
                                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub

                                                    //开启子线程，完成期刊取消收藏
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            delete_chapter_position = position;
                                                            //获取json字符串
                                                            int chapter_id = (Integer) myFavoriteChapterList.get(position).get("chapter_id");
                                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteChapter.php?user_id="+user_id+"&chapter_id="+chapter_id,"GET");
                                                            collect_flag = Integer.valueOf(str);
                                                            //getActivity().finish();
                                                            //创建消息对象
                                                            Message message = new Message();
                                                            //为消息对象设置标识
                                                            message.what = 23;
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
                            break;
                        case 23:
                            if (collect_flag > 0){
                                Log.d("Chapter_JSON_DATA","取消收藏成功！"+collect_flag+"\n"+delete_chapter_position);
                                myFavoriteChapterList.remove(delete_chapter_position);
                                baseAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "取消收藏成功！", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.d("Chapter_JSON_DATA","取消收藏失败！"+collect_flag);
                                Toast.makeText(getActivity(), "取消收藏失败！", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

            return rootView;
        }else {
            return rootView;
        }
    }
    //构建数据
    private void initData(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavorteChapter.php?user_id="+user_id,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("jsonArray数组长度",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("chapter_id",jsonObject.getInt("chapter_id"));
                        map.put("chapter_name",jsonObject.getString("chapter_name"));
                        map.put("chapter_source",jsonObject.getString("chapter_source"));
                        map.put("chapter_page",jsonObject.getString("chapter_page"));
                        map.put("chapter_author",jsonObject.getString("chapter_author"));
                        map.put("chapter_publish_date",jsonObject.getString("chapter_publish_date"));
                        map.put("chapter_content",jsonObject.getString("chapter_content"));
                        myFavoriteChapterList.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 22;
                //将消息对象发送给UI线程
                handler.sendMessage(message);
            }
        }).start();
        /*HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","计算机系统");
        map1.put("source","《计算机应用基础》");
        map1.put("page","20");
        map1.put("author","马志强，李玉丽，崔田明");
        map1.put("date","2014");
        myFavoriteChapterList.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","多媒体计算机");
        map2.put("source","《计算机应用基础》");
        map2.put("page","220 221");
        map2.put("author","夏宝睿");
        map2.put("date","2014");
        myFavoriteChapterList.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","计算机的分类");
        map3.put("source","《计算机应用基础》");
        map3.put("page","9 10");
        map3.put("author","马志强，李玉丽，崔田明");
        map3.put("date","2014");
        myFavoriteChapterList.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name","超导计算机");
        map4.put("source","《电子信息材料》");
        map4.put("page","102 103");
        map4.put("author","常永勤");
        map4.put("date","2014");
        myFavoriteChapterList.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","计算机安全");
        map5.put("source","《计算机应用基础》");
        map5.put("page","203 204");
        map5.put("author","夏宝睿");
        map5.put("date","2014");
        myFavoriteChapterList.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","计算机的特点");
        map6.put("source","《计算机应用基础》");
        map6.put("page","8 9");
        map6.put("author","马志强，李玉丽，崔田明");
        map6.put("date","2014");
        myFavoriteChapterList.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","计算机系统");
        map7.put("source","《计算机操作系统原理》");
        map7.put("page","中国科学院大学工程科学学院");
        map7.put("author","271 272");
        map7.put("date","2014");
        myFavoriteChapterList.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","计算机的产生");
        map8.put("source","《计算机应用基础》");
        map8.put("page","2 3");
        map8.put("author","马志强，李玉丽，崔田明");
        map8.put("date","2014");
        myFavoriteChapterList.add(map8);*/
    }
}
