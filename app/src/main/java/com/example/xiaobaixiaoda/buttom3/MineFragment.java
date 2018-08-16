package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class MineFragment extends Fragment {

    //声明线性布局控件
    private LinearLayout person_info_linearlayout,borrow_record_linearlayout,have_borrowed_linearlayout,my_order_linearlayout,resource_collect_linearlayout,advice_linearlayout,logout_linearlayout;

    private TextView user_name_tv,user_student_no_tv;

    private View rootView;

    public Intent intent;

    //用户编号
    private int user_id;

    //用户信息JSON字符串
    String json;

    //用户信息集合
    HashMap<String,Object> user_info = new HashMap<>();

    Handler handler;

    public MineFragment(){  //构造方法

    }

    public static MineFragment newInstance(int user_id){  //获取Fragment实例并传递数据
        MineFragment fragment=new MineFragment();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_mine,container,false);
        Bundle bundle=getArguments();
        user_id = bundle.getInt("user_id");
        Log.d("MineFragment：user_id=",user_id+"");
        //绑定线性布局控件
        person_info_linearlayout = (LinearLayout)rootView.findViewById(R.id.person_info_linearlayout);
        borrow_record_linearlayout = (LinearLayout)rootView.findViewById(R.id.borrow_record_linearlayout);
        have_borrowed_linearlayout = (LinearLayout)rootView.findViewById(R.id.have_borrowed_linearlayout);
        my_order_linearlayout = (LinearLayout)rootView.findViewById(R.id.my_order_linearlayout);
        resource_collect_linearlayout = (LinearLayout)rootView.findViewById(R.id.resource_collect_linearlayout);
        advice_linearlayout = (LinearLayout)rootView.findViewById(R.id.advice_linearlayout);
        logout_linearlayout = (LinearLayout)rootView.findViewById(R.id.logout_linearlayout);
        user_name_tv = (TextView) rootView.findViewById(R.id.user_name_tv);
        user_student_no_tv = (TextView)rootView.findViewById(R.id.user_student_no_tv);

        //开启子线程获取对应user_id的所有信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getUserInfo.php?user_id="+user_id,"GET");
                Log.d("我的界面json",json);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    user_info.put("user_id",user_id);
                    user_info.put("user_name",jsonObject.getString("user_name"));
                    user_info.put("user_student_no",jsonObject.getString("user_student_no"));
                    user_info.put("user_password",jsonObject.getString("user_password"));
                    user_info.put("user_image",jsonObject.getString("user_image"));
                    user_info.put("user_college",jsonObject.getString("user_college"));
                    user_info.put("user_class",jsonObject.getString("user_class"));
                    user_info.put("user_credit_level",jsonObject.getInt("user_credit_level"));
                    user_info.put("user_phone_flag",jsonObject.getInt("user_phone_flag"));
                    user_info.put("user_wechat_flag",jsonObject.getInt("user_wechat_flag"));
                    user_info.put("user_qq_flag",jsonObject.getInt("user_qq_flag"));
                    user_info.put("user_weibo_flag",jsonObject.getInt("user_weibo_flag"));
                    user_info.put("user_first_login",jsonObject.getInt("user_first_login"));
                    user_info.put("user_identify_flag",jsonObject.getInt("user_identify_flag"));
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 4;
                    //将消息对象发送给UI线程
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //主线程接收子线程消息，设置UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 4){
                    user_name_tv.setText(user_info.get("user_name").toString());
                    user_student_no_tv.setText("学号："+user_info.get("user_student_no").toString());
                }
            }
        };

        //设置个人信息线性布局点击监听器
        person_info_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),PersonInfoActivity.class);
                intent.putExtra("user_info",(Serializable) user_info);
                startActivity(intent);
            }
        });
        //设置借阅记录线性布局点击监听器
        borrow_record_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),HistoryBorrowRecordActivity.class);
                startActivity(intent);
            }
        });
        //设置已借书籍线性布局点击监听器
        have_borrowed_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),AllBorrowBooksActivity.class);
                startActivity(intent);
            }
        });
        //设置我的预约线性布局点击监听器
        my_order_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),MyOrderActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
        //设置资源收藏线性布局点击监听器
        resource_collect_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),MyFavoriteActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
        //设置意见反馈线性布局点击监听器
        advice_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),GiveYourAdviceActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
        //设置退出登录线性布局点击监听器
        logout_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                        .setMessage("您确定要退出登录吗？")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                        Log.i("alertdialog"," 请保存数据！");
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
        return rootView;
    }
}