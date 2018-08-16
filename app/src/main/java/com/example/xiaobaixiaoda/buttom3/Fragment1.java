package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaobaixiaoda.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by xiaobaixiaoda on 2017/11/7.
 */

public class Fragment1 extends Fragment {
    private View rootView = null;
    private SearchGridView my_favorite_books_GridView;
    private SimpleAdapter simpleAdapter;
    BaseAdapter baseAdapter;
    private ArrayList<HashMap<String,Object>> my_favorite_books_list = new ArrayList<>();
    //private int[] my_favorite_boos_image = {R.drawable.dafenqimima_72_96,R.drawable.jieyouzahuopu_72_96,R.drawable.weinuershinian_72_96,R.drawable.santi_72_96,R.drawable.bingyuhuozhige_72_96,R.drawable.dunkeerke72_96,R.drawable.cikexintiao72_96,R.drawable.meinvyuyeshou_72_96};
    //private String[] my_favorite_boos_name = {"达芬奇密码","解忧杂货铺","为奴二十年","三体Ⅰ","冰与火之歌全集","敦刻尔克","刺客信条","美女与野兽"};

    //用户序号：user_id
    private int user_id = -1;

    //用户想要取消收藏的图书位置
    int delete_book_position = -1;

    int collect_flag = -1;

    Handler handler;

    public static Fragment1 newInstance(int user_id){
        Fragment1 fragment=new Fragment1();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment1(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            //加载布局
            rootView = (View) inflater.inflate(R.layout.fragment1, container, false);
            Toast.makeText(getActivity(), "rootView为空", Toast.LENGTH_SHORT).show();

            //接收由MyFavouriteActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("Fragment1：user_id=",user_id+"");
            Toast.makeText(getActivity(), "Fragment1：user_id="+user_id, Toast.LENGTH_SHORT).show();

            //绑定网格布局控件
            my_favorite_books_GridView = (SearchGridView)rootView.findViewById(R.id.my_favorite_books_GridView);

            //开始接收子线程消息，根据消息内容，更改UI
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 10){
                        //新建适配器
                        baseAdapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return my_favorite_books_list.size();
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

                                viewHolder.new_books_name.setText(my_favorite_books_list.get(position).get("text").toString());
                                viewHolder.new_books_ImageView.setImageBitmap((Bitmap)my_favorite_books_list.get(position).get("image"));
                                //viewHolder.new_books_ImageView.setImageResource(R.drawable.javachengxuyuankaifazhinan);

                                return view;
                            }
                            class ViewHolder {
                                protected TextView new_books_name;
                                protected ImageView new_books_ImageView;
                            }
                        };
                        //配置适配器
                        my_favorite_books_GridView.setAdapter(baseAdapter);
                    }else if (msg.what == 11){
                        if (collect_flag > 0){
                            Log.d("BOOK_JSON_DATA","图书取消收藏成功！"+collect_flag+"\n"+delete_book_position);
                            my_favorite_books_list.remove(delete_book_position);
                            baseAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "图书取消收藏成功！", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d("BOOK_JSON_DATA","图书取消收藏失败！"+collect_flag);
                            Toast.makeText(getActivity(), "图书取消收藏失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

            //从服务器获取用户收藏的图书
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取json字符串
                    String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavortetBook.php?user_id="+user_id,"GET");
                    Log.d("NEWBOOKURL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyFavortetBook.php?user_id="+user_id);
                    //解析json字符串
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json);
                        Log.d("MyFavorteBookjsonArray",jsonArray.length()+"");
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
                            my_favorite_books_list.add(map);
                        }
                        //创建消息对象
                        Message message = new Message();
                        //为消息对象设置标识
                        message.what = 10;
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

            //添加列表项点击的监听器
            my_favorite_books_GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "你点击的书名为："+my_favorite_books_list.get(position).get("text"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),DetailBookInfoActivity.class);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("book_id",(Integer) (my_favorite_books_list.get(position).get("book_id")));
                    //startActivity(intent);
                    startActivityForResult(intent,1);
                }
            });
            //添加列表项长按移除收藏监听器
            my_favorite_books_GridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Toast.makeText(getActivity(), "取消收藏"+my_favorite_books_list.get(position).get("text"), Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                            .setMessage("您确定要取消收藏"+my_favorite_books_list.get(position).get("text")+"吗？")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            delete_book_position = position;
                                            //获取json字符串
                                            int book_id = (Integer) my_favorite_books_list.get(position).get("book_id");
                                            String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/cannleFavorteBook.php?user_id="+user_id+"&book_id="+book_id,"GET");
                                            collect_flag = Integer.valueOf(str);
                                            //getActivity().finish();
                                            //创建消息对象
                                            Message message = new Message();
                                            //为消息对象设置标识
                                            message.what = 11;
                                            //将消息对象发送给UI线程
                                            handler.sendMessage(message);
                                        }
                                    }).start();
                                    //getActivity().finish();
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
            Toast.makeText(getActivity(), "rootView不为空", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    //Log.d("爷爷","我又回来了！");
                    int delete_book_id = data.getIntExtra("data_return_book_id",-1);
                    int delete_book_position1 = -1;
                    for (int i = 0;i<my_favorite_books_list.size();i++){
                        if ((Integer)my_favorite_books_list.get(i).get("book_id") == delete_book_id){
                            delete_book_position1 = i;
                            break;
                        }
                    }
                    //Log.d("delete_paper_position1:",delete_paper_position1+"");
                    my_favorite_books_list.remove(delete_book_position1);
                    baseAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

}