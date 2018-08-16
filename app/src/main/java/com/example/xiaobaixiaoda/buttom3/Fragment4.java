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

public class Fragment4 extends Fragment {
    private View rootView;
    private ListView my_favorite_lunwen_ListView;
    private ArrayList<HashMap<String,Object>> myFavoritePaperList = new ArrayList<>();

    //用户登录序号
    int user_id = -1;

    int collect_flag = -1;
    int delete_paper_position = -1;

    private Handler handler;
    BaseAdapter baseAdapter;

    public static Fragment4 newInstance(int user_id){
        Fragment4 fragment=new Fragment4();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment4(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局
            rootView = (View) inflater.inflate(R.layout.fragment4, container, false);

            //接收由MyFavouriteActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("Fragment4：user_id=",user_id+"");
            Toast.makeText(getActivity(), "Fragment4：user_id="+user_id, Toast.LENGTH_SHORT).show();

            //绑定控件
            my_favorite_lunwen_ListView = (ListView)rootView.findViewById(R.id.my_favorite_lunwen_ListView);
            //开启子线程，初始化数据
            initData();

            //接收子线程消息，更新主线程UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 19:
                            //构建适配器
                            baseAdapter = new BaseAdapter() {

                                @Override
                                public int getCount() {
                                    return myFavoritePaperList.size();
                                }

                                @Override
                                public Object getItem(int position) {
                                    return myFavoritePaperList.get(position);
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
                                        view = getActivity().getLayoutInflater().inflate(R.layout.fragment4_item, null);
                                        viewHolder = new ViewHolder();
                                        viewHolder.name_tv = (TextView)view.findViewById(R.id.name_tv);
                                        viewHolder.author_tv = (TextView)view.findViewById(R.id.author_tv);
                                        viewHolder.date_tv = (TextView)view.findViewById(R.id.date_tv);
                                        viewHolder.teacher_tv = (TextView)view.findViewById(R.id.teacher_tv);
                                        viewHolder.school_tv = (TextView)view.findViewById(R.id.school_tv);
                                        viewHolder.type_tv = (TextView)view.findViewById(R.id.type_tv);
                                        view.setTag(viewHolder);
                                    } else {
                                        viewHolder = (ViewHolder) view.getTag();
                                    }
                                    viewHolder.name_tv.setTextColor(Color.BLUE);
                                    viewHolder.name_tv.setText((position+1) + ". " + myFavoritePaperList.get(position).get("paper_name"));
                                    viewHolder.author_tv.setText("作 者：" + myFavoritePaperList.get(position).get("paper_author"));
                                    viewHolder.date_tv.setText("学位年度：" + myFavoritePaperList.get(position).get("paper_date"));
                                    viewHolder.teacher_tv.setText("导师姓名：" + myFavoritePaperList.get(position).get("paper_teacher"));
                                    viewHolder.school_tv.setText("学位授予单位：" + myFavoritePaperList.get(position).get("paper_school"));
                                    viewHolder.type_tv.setText("学位名称：" + myFavoritePaperList.get(position).get("paper_type"));
                                    return view;
                                }
                                class ViewHolder {
                                    protected TextView name_tv,author_tv,school_tv,type_tv,date_tv,teacher_tv;
                                }
                            };
                            //为ListView绑定适配器
                            my_favorite_lunwen_ListView.setAdapter(baseAdapter);
                            //为ListView的每个item绑定点击监听器
                            my_favorite_lunwen_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getActivity(), "点击的是："+myFavoritePaperList.get(position).get("paper_name"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(),DetailPaperInfoActivity.class);
                                    intent.putExtra("user_id",user_id);
                                    intent.putExtra("paper_id",Integer.valueOf(myFavoritePaperList.get(position).get("paper_id").toString()));
                                    //getActivity().startActivity(intent);
                                    startActivityForResult(intent,4);
                                }
                            });
                            //为ListView的每个item绑定长按取消收藏监听器
                            my_favorite_lunwen_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    Toast.makeText(getActivity(), "取消收藏"+myFavoritePaperList.get(position).get("paper_name"), Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                                            .setMessage("您确定要取消收藏"+myFavoritePaperList.get(position).get("paper_name")+"吗？")//设置显示的内容
                                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    // TODO Auto-generated method stub
                                                    //getActivity().finish();

                                                    //开启子线程，完成期刊取消收藏
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            delete_paper_position = position;
                                                            //获取json字符串
                                                            int paper_id = (Integer) myFavoritePaperList.get(position).get("paper_id");
                                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavortePaper.php?user_id="+user_id+"&paper_id="+paper_id,"GET");
                                                            collect_flag = Integer.valueOf(str);
                                                            //getActivity().finish();
                                                            //创建消息对象
                                                            Message message = new Message();
                                                            //为消息对象设置标识
                                                            message.what = 20;
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
                        case 20:
                            if (collect_flag > 0){
                                Log.d("Paper_JSON_DATA","取消收藏成功！"+collect_flag+"\n"+delete_paper_position);
                                myFavoritePaperList.remove(delete_paper_position);
                                baseAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "取消收藏成功！", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.d("Paper_JSON_DATA","取消收藏失败！"+collect_flag);
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
            case 4:
                if (resultCode == RESULT_OK){
                    //Log.d("爷爷","我又回来了！");
                    int delete_paper_id = data.getIntExtra("data_return_paper_id",-1);
                    int delete_paper_position1 = -1;
                    for (int i = 0;i<myFavoritePaperList.size();i++){
                        if ((Integer)myFavoritePaperList.get(i).get("paper_id") == delete_paper_id){
                            delete_paper_position1 = i;
                            break;
                        }
                    }
                    //Log.d("delete_paper_position1:",delete_paper_position1+"");
                    myFavoritePaperList.remove(delete_paper_position1);
                    baseAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //开启子线程来获取用户收藏的学位论文信息
    private void initData(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavortePaper.php?user_id="+user_id,"GET");
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("jsonArray数组长度",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("paper_id",jsonObject.getInt("paper_id"));
                        map.put("paper_name",jsonObject.getString("paper_name"));
                        map.put("paper_author",jsonObject.getString("paper_author"));
                        map.put("paper_school",jsonObject.getString("paper_school"));
                        map.put("paper_type",jsonObject.getString("paper_type"));
                        map.put("paper_date",jsonObject.getString("paper_date"));
                        map.put("paper_teacher",jsonObject.getString("paper_teacher"));
                        map.put("paper_content",jsonObject.getString("paper_content"));
                        myFavoritePaperList.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //创建消息对象
                Message message = new Message();
                //为消息对象设置标识
                message.what = 19;
                //将消息对象发送给UI线程
                handler.sendMessage(message);
            }
        }).start();
        /*HashMap<String,String> map1 = new HashMap<>();
        map1.put("name","五子棋计算机博弈系统的研究与设计");
        map1.put("author","张效见");
        map1.put("school","安徽大学");
        map1.put("type","硕士");
        map1.put("date","2017");
        map1.put("teacher","李龙澍");
        myFavoriteLunWenList.add(map1);
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("name","基于能力本位的中职计算机专业课程体系的研究");
        map2.put("author","卢新贞");
        map2.put("school","河北师范大学");
        map2.put("type","硕士");
        map2.put("date","2017");
        map2.put("teacher","武金玲");
        myFavoriteLunWenList.add(map2);
        HashMap<String,String> map3 = new HashMap<>();
        map3.put("name","计算机图形图像发展史研究");
        map3.put("author","吴毅儒");
        map3.put("school","天津工业大学");
        map3.put("type","硕士");
        map3.put("date","2017");
        map3.put("teacher","李铁");
        myFavoriteLunWenList.add(map3);
        HashMap<String,String> map4 = new HashMap<>();
        map4.put("name","计算机类论文HCI and Software Engineering for User Interface Plasticity英汉翻译实践报告");
        map4.put("author","张爽");
        map4.put("school","黑龙江大学");
        map4.put("type","硕士");
        map4.put("date","2017");
        map4.put("teacher","高战荣");
        myFavoriteLunWenList.add(map4);
        HashMap<String,String> map5 = new HashMap<>();
        map5.put("name","应变场下钨抗辐照性能的计算机模拟");
            map5.put("author","王栋");
        map5.put("school","中国科学院大学(中国科学院近代物理研究所)");
        map5.put("type","博士");
        map5.put("date","2017");
        map5.put("teacher","王志光");
        myFavoriteLunWenList.add(map5);
        HashMap<String,String> map6 = new HashMap<>();
        map6.put("name","基于网络攻击的计算机病毒传播模型的优化算法研究");
        map6.put("author","裴宏悦");
        map6.put("school","天津工业大学");
        map6.put("type","硕士");
        map6.put("date","2017");
        map6.put("teacher","裴永珍，张建勇");
        myFavoriteLunWenList.add(map6);
        HashMap<String,String> map7 = new HashMap<>();
        map7.put("name","基于VARK模型的中职计算机辅助教学系统设计与实现");
        map7.put("author","高翼飞");
        map7.put("school","中国科学院大学工程科学学院");
        map7.put("type","硕士");
        map7.put("date","2017");
        map7.put("teacher","吴广洲，林意");
        myFavoriteLunWenList.add(map7);
        HashMap<String,String> map8 = new HashMap<>();
        map8.put("name","Investigation on Exponential Synchronization via Various Control Techniques for Complex Networks of Networks");
        map8.put("author","Mohmmed Alsiddig Alamin Ahmed");
        map8.put("school","扬州大学");
        map8.put("type","博士");
        map8.put("date","2017");
        map8.put("teacher","刘玉荣，张文兵");
        myFavoriteLunWenList.add(map8);*/

    }
}
