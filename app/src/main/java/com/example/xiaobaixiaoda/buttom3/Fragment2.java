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

public class Fragment2 extends Fragment {
    private View rootView = null;
    private ListView my_favorite_qikan_ListView;
    private ArrayList<HashMap<String,Object>> myFavoriteQikanList = new ArrayList<>();

    //用户登录序号
    int user_id = -1;

    int collect_flag = -1;
    int delete_qikan_position = -1;

    private Handler handler;
    BaseAdapter baseAdapter;

    public static Fragment2 newInstance(int user_id){
        Fragment2 fragment=new Fragment2();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment2(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局
            rootView = (View) inflater.inflate(R.layout.fragment2, container, false);

            //接收由MyFavouriteActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("Fragment2：user_id=",user_id+"");
            Toast.makeText(getActivity(), "Fragment2：user_id="+user_id, Toast.LENGTH_SHORT).show();

            //绑定控件
            my_favorite_qikan_ListView = (ListView)rootView.findViewById(R.id.my_favorite_qikan_ListView);
            //初始化数据
            //initData();

            //开始接收子线程的消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 14:
                            //Log.d("CD_JSON_DATA",json);
                            //构建适配器
                            baseAdapter = new BaseAdapter() {

                                @Override
                                public int getCount() {
                                    return myFavoriteQikanList.size();
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
                                    if(view == null) {
                                        view = getActivity().getLayoutInflater().inflate(R.layout.fragment2_item, null);
                                        viewHolder = new ViewHolder();
                                        viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                        viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                        viewHolder.kanming_tv = (TextView)view.findViewById(R.id.kanming_tv);
                                        viewHolder.publish_date_tv = (TextView)view.findViewById(R.id.publish_date_tv);
                                        viewHolder.qihao_tv = (TextView)view.findViewById(R.id.qihao_tv);
                                        view.setTag(viewHolder);
                                    } else {
                                        viewHolder = (ViewHolder) view.getTag();
                                    }
                                    viewHolder.name_tv.setTextColor(Color.BLUE);
                                    viewHolder.name_tv.setText((position+1) + "." + myFavoriteQikanList.get(position).get("name"));
                                    viewHolder.author_tv.setText("作者：" + myFavoriteQikanList.get(position).get("author"));
                                    viewHolder.kanming_tv.setText("刊名：" + myFavoriteQikanList.get(position).get("kanming"));
                                    viewHolder.publish_date_tv.setText("出版日期：" + myFavoriteQikanList.get(position).get("publish_date"));
                                    viewHolder.qihao_tv.setText("期号：" + myFavoriteQikanList.get(position).get("qikan_number"));
                                    return view;
                                }
                                class ViewHolder {
                                    protected TextView name_tv,author_tv,kanming_tv,publish_date_tv,qihao_tv;
                                }
                            };
                            //为ListView绑定适配器
                            my_favorite_qikan_ListView.setAdapter(baseAdapter);
                            //为ListView的每个item绑定点击监听器
                            my_favorite_qikan_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getActivity(), "点击的是："+myFavoriteQikanList.get(position).get("name"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(),DetailQiKanInfoActivity.class);
                                    intent.putExtra("user_id",user_id);
                                    intent.putExtra("qikan_id",Integer.valueOf(myFavoriteQikanList.get(position).get("qikan_id").toString()));
                                    //getActivity().startActivity(intent);
                                    startActivityForResult(intent,2);
                                }
                            });
                            //为ListView的每个item绑定长按取消收藏监听器
                            my_favorite_qikan_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    Toast.makeText(getActivity(), "取消收藏"+myFavoriteQikanList.get(position).get("name"), Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                                            .setMessage("您确定要取消收藏"+myFavoriteQikanList.get(position).get("name")+"吗？")//设置显示的内容
                                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub
                                                    //getActivity().finish();

                                                    //开启子线程，完成期刊取消收藏
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            delete_qikan_position = position;
                                                            //获取json字符串
                                                            int qikan_id = (Integer) myFavoriteQikanList.get(position).get("qikan_id");
                                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteQikan.php?user_id="+user_id+"&qikan_id="+qikan_id,"GET");
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
                                Log.d("Qikan_JSON_DATA","取消收藏成功！"+collect_flag+"\n"+delete_qikan_position);
                                myFavoriteQikanList.remove(delete_qikan_position);
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

            //开启子线程进行网络访问，与服务器进行通信，获取用户收藏的光盘信息
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavorteQikan.php?user_id="+user_id,"GET");
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        for (int i = 0;i<jsonArray.length();++i){
                            HashMap<String,Object> map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            map.put("qikan_id",jsonObject.getInt("qikan_id"));
                            map.put("name",jsonObject.getString("qikan_name"));
                            map.put("author",jsonObject.getString("qikan_author"));
                            map.put("kanming",jsonObject.getString("qikan_tittle"));
                            map.put("publish_date",jsonObject.getString("qikan_date"));
                            map.put("qikan_number",jsonObject.getString("qikan_number"));
                            myFavoriteQikanList.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 14;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                }
            }).start();

            return rootView;
        }else{
            return rootView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if (resultCode == RESULT_OK){
                    //Log.d("爷爷","我又回来了！");
                    int delete_qikan_id = data.getIntExtra("data_return_qikan_id",-1);
                    int delete_qikan_position1 = -1;
                    for (int i = 0;i<myFavoriteQikanList.size();i++){
                        if ((Integer)myFavoriteQikanList.get(i).get("qikan_id") == delete_qikan_id){
                            delete_qikan_position1 = i;
                            break;
                        }
                    }
                    //Log.d("delete_paper_position1:",delete_paper_position1+"");
                    myFavoriteQikanList.remove(delete_qikan_position1);
                    baseAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //构建猜你喜欢数据
    /*private void initData(){
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","浅谈计算机与计算机技术");
        map1.put("author","石俊杰");
        map1.put("kanming","中外交流");
        map1.put("publish_date","2017");
        map1.put("qihao","第31期");
        myFavoriteQikanList.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","计算机网络维护");
        map2.put("author","玉明文");
        map2.put("kanming","大科技");
        map2.put("publish_date","2017");
        map2.put("qihao","第11期");
        myFavoriteQikanList.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","量子计算机");
        map3.put("author","刘小桐");
        map3.put("kanming","科技创新与应用");
        map3.put("publish_date","2017");
        map3.put("qihao","第19期");
        myFavoriteQikanList.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name","兔子计算机");
        map4.put("author","Kydll Nikitine，王师");
        map4.put("kanming","新发现");
        map4.put("publish_date","2017");
        map4.put("qihao","第4期");
        myFavoriteQikanList.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","计算机应用浅析");
        map5.put("author","刘彩梅");
        map5.put("kanming","速读(上旬)");
        map5.put("publish_date","2017");
        map5.put("qihao","第1期");
        myFavoriteQikanList.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","作为计算机的大脑");
        map6.put("author","Karlheinz Meier");
        map6.put("kanming","科技纵览");
        map6.put("publish_date","2017");
        map6.put("qihao","第6期");
        myFavoriteQikanList.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","浅谈计算机与计算机技术");
        map7.put("author","石俊杰");
        map7.put("kanming","中外交流");
        map7.put("publish_date","2017");
        map7.put("qihao","第31期");
        myFavoriteQikanList.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","基于计算机视觉技术的番茄花青素含量检测");
        map8.put("author","雷静");
        map8.put("kanming","农机化研究");
        map8.put("publish_date","2017");
        map8.put("qihao","第3期");
        myFavoriteQikanList.add(map8);
    }*/
}