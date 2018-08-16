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

import static android.app.Activity.RESULT_OK;

/**
 * Created by xiaobaixiaoda on 2017/11/7.
 */

public class Fragment3 extends Fragment {
    private View rootView;
    private ListView my_favorite_newspaper_ListView;
    private ArrayList<HashMap<String,Object>> myFavoriteNewspaperList = new ArrayList<>();

    //用户登录序号
    int user_id = -1;

    int collect_flag = -1;
    int delete_newspaper_position = -1;

    private Handler handler;
    BaseAdapter baseAdapter;

    public static Fragment3 newInstance(int user_id){
        Fragment3 fragment=new Fragment3();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment3(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局
            rootView = (View) inflater.inflate(R.layout.fragment3, container, false);

            //接收由MyFavouriteActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("Fragment3：user_id=",user_id+"");
            Toast.makeText(getActivity(), "Fragment3：user_id="+user_id, Toast.LENGTH_SHORT).show();

            //绑定控件
            my_favorite_newspaper_ListView = (ListView)rootView.findViewById(R.id.my_favorite_newspaper_ListView);
            //初始化数据
            //initData();

            //开启子线程来获取用户收藏的报纸信息
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavorteNewspaper.php?user_id="+user_id,"GET");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        Log.d("jsonArray数组长度",jsonArray.length()+"");
                        for (int i = 0;i<jsonArray.length();++i){
                            HashMap<String,Object> map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            map.put("newspaper_id",jsonObject.getInt("newspaper_id"));
                            map.put("newspaper_tittle",jsonObject.getString("newspaper_tittle"));
                            map.put("newspaper_date",jsonObject.getString("newspaper_date"));
                            map.put("newspaper_source",jsonObject.getString("newspaper_source"));
                            map.put("newspaper_author",jsonObject.getString("newspaper_author"));
                            map.put("newspaper_content",jsonObject.getString("newspaper_content"));
                            map.put("newspaper_location",jsonObject.getString("newspaper_location"));
                            myFavoriteNewspaperList.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 17;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                }
            }).start();

            //接收子线程消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 17:
                            //Log.d("CD_JSON_DATA",json);
                            //构建适配器
                            baseAdapter = new BaseAdapter() {

                                @Override
                                public int getCount() {
                                    return myFavoriteNewspaperList.size();
                                }

                                @Override
                                public Object getItem(int position) {
                                    return myFavoriteNewspaperList.get(position);
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
                                        view = getActivity().getLayoutInflater().inflate(R.layout.fragment3_item, null);
                                        viewHolder = new ViewHolder();
                                        viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                        viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                        viewHolder.date_tv = (TextView)view.findViewById(R.id.date_tv);
                                        viewHolder.source_tv = (TextView)view.findViewById(R.id.source_tv);
                                        view.setTag(viewHolder);
                                    } else {
                                        viewHolder = (ViewHolder) view.getTag();
                                    }
                                    viewHolder.name_tv.setTextColor(Color.BLUE);
                                    viewHolder.name_tv.setText((position+1) + ". " + myFavoriteNewspaperList.get(position).get("newspaper_tittle"));
                                    viewHolder.author_tv.setText("作 者：" + myFavoriteNewspaperList.get(position).get("newspaper_author"));
                                    viewHolder.date_tv.setText("日 期：" + myFavoriteNewspaperList.get(position).get("newspaper_date"));
                                    viewHolder.source_tv.setText("来 源：" + myFavoriteNewspaperList.get(position).get("newspaper_source"));
                                    return view;
                                }
                                class ViewHolder {
                                    protected TextView name_tv,author_tv,date_tv,source_tv;
                                }
                            };
                            //为ListView绑定适配器
                            my_favorite_newspaper_ListView.setAdapter(baseAdapter);
                            //为ListView的每个item绑定点击监听器
                            my_favorite_newspaper_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getActivity(), "点击的是："+myFavoriteNewspaperList.get(position).get("name"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(),DetailNewspaperInfoActivity.class);
                                    intent.putExtra("user_id",user_id);
                                    intent.putExtra("newspaper_id",Integer.valueOf(myFavoriteNewspaperList.get(position).get("newspaper_id").toString()));
                                    //getActivity().startActivity(intent);
                                    startActivityForResult(intent,3);
                                }
                            });
                            //为ListView的每个item绑定长按取消收藏监听器
                            my_favorite_newspaper_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    Toast.makeText(getActivity(), "取消收藏"+myFavoriteNewspaperList.get(position).get("newspaper_tittle"), Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                                            .setMessage("您确定要取消收藏"+myFavoriteNewspaperList.get(position).get("newspaper_tittle")+"吗？")//设置显示的内容
                                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub
                                                    //getActivity().finish();

                                                    //开启子线程，完成期刊取消收藏
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            delete_newspaper_position = position;
                                                            //获取json字符串
                                                            int newspaper_id = (Integer) myFavoriteNewspaperList.get(position).get("newspaper_id");
                                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteNewspaper.php?user_id="+user_id+"&newspaper_id="+newspaper_id,"GET");
                                                            collect_flag = Integer.valueOf(str);
                                                            //getActivity().finish();
                                                            //创建消息对象
                                                            Message message = new Message();
                                                            //为消息对象设置标识
                                                            message.what = 16;
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
                        case 16:
                            if (collect_flag > 0){
                                Log.d("Newspaper_JSON_DATA","取消收藏成功！"+collect_flag+"\n"+delete_newspaper_position);
                                myFavoriteNewspaperList.remove(delete_newspaper_position);
                                baseAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "取消收藏成功！", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.d("Qikan_JSON_DATA","取消收藏失败！"+collect_flag);
                                Toast.makeText(getActivity(), "取消收藏失败！", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                }
            };

            return rootView;
        }else{
            return rootView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 3:
                if (resultCode == RESULT_OK){
                    int delete_newspaper_id = data.getIntExtra("data_return_newspaper_id",-1);
                    int delete_newspaper_position1 = -1;
                    for (int i = 0;i<myFavoriteNewspaperList.size();i++){
                        if ((Integer)myFavoriteNewspaperList.get(i).get("newspaper_id") == delete_newspaper_id){
                            delete_newspaper_position1 = i;
                            break;
                        }
                    }
                    myFavoriteNewspaperList.remove(delete_newspaper_position1);
                    baseAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //构建猜你喜欢数据
    /*private void initData(){
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","超级计算机全球最快");
        map1.put("date","2017.09.13");
        map1.put("source","南通日报");
        map1.put("author","");
        myFavoriteNewspaperList.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","参观超级计算机");
        map2.put("date","2017.06.14");
        map2.put("source","无锡商报");
        map2.put("author","");
        myFavoriteNewspaperList.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","MEN嵌入式计算机");
        map3.put("date","2017.09.05");
        map3.put("source","人民铁道报");
        map3.put("author","梁宇");
        myFavoriteNewspaperList.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
            map4.put("name","量子计算机中国造 超越早期经典计算机");
        map4.put("date","2017.05.04");
        map4.put("source","山西日报");
        map4.put("author","");
        myFavoriteNewspaperList.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","预防计算机病毒");
        map5.put("date","2017.07.10");
        map5.put("source","齐鲁晚报");
        map5.put("author","");
        myFavoriteNewspaperList.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","我国光量子计算机超越早期经典计算机");
        map6.put("date","2017.05.04");
        map6.put("source","信息时报");
        map6.put("author","");
        myFavoriteNewspaperList.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","计算机科学与技术学院");
        map7.put("date","2017.06.29");
        map7.put("source","工学周报");
        map7.put("author","");
        myFavoriteNewspaperList.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","世界第一台计算机问世");
        map8.put("date","2017.02.14");
        map8.put("source","巴中晚报");
        map8.put("author","");
        myFavoriteNewspaperList.add(map8);
    }*/
}
