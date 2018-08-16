package com.example.xiaobaixiaoda.buttom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

public class ShelfFragment extends Fragment {

    private SearchGridView my_books_shelf_GridView;
    private ArrayList<HashMap<String,Object>> new_books_list = new ArrayList<>();
    private View rootView = null;
    //private SimpleAdapter simpleAdapter_newbook;
    //private int[] new_boos_image = {R.drawable.dafenqimima_72_96,R.drawable.jieyouzahuopu_72_96,R.drawable.weinuershinian_72_96,R.drawable.santi_72_96,R.drawable.bingyuhuozhige_72_96,R.drawable.dunkeerke72_96,R.drawable.cikexintiao72_96,R.drawable.meinvyuyeshou_72_96};
    //private String[] new_boos_name = {"达芬奇密码","解忧杂货铺","为奴二十年","三体Ⅰ","冰与火之歌全集","敦刻尔克","刺客信条","美女与野兽"};

    //用户序号：user_id
    private int user_id = -1;

    //声明消息处理对象handler
    Handler handler;

    //书架图书网格布局适配器
    BaseAdapter baseAdapter;

    //图书移除书架标志位
    int if_flag = -1;

    //被移除的图书在集合中的下标
    int canncle_position = -1;

    public static ShelfFragment newInstance(int user_id){
        ShelfFragment fragment=new ShelfFragment();
        Bundle args=new Bundle();
        args.putInt("user_id",user_id);
        fragment.setArguments(args);
        return fragment;
    }
    public ShelfFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_shelf,container,false);

            //接收由MainActivity传递过来的user_id参数
            Bundle bundle=getArguments();
            user_id = bundle.getInt("user_id");
            Log.d("ShelfFragment：user_id=",user_id+"");

            //从服务器中获取书架图书资源,构建新书到馆模块GridView中的item数据,给适配器集合赋数据
            getNewBooksData();

            //获取新书到馆模块GridView控件
            my_books_shelf_GridView = (SearchGridView)rootView.findViewById(R.id.my_books_shelf_GridView);

            //主线程接收子线程消息，处理消息更新UI界面
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 66){    //获取某一用户的书架图书信息，更新UI
                        //新建适配器
                    /*String [] from_newbook ={"image","text"};
                    int [] to_newbook = {R.id.new_books_ImageView,R.id.new_books_name};
                    simpleAdapter_newbook = new SimpleAdapter(getActivity(), new_books_list, R.layout.new_books, from_newbook, to_newbook);
                    //配置适配器
                    my_books_shelf_GridView.setAdapter(simpleAdapter_newbook);
                    //添加列表项点击的监听器
                    my_books_shelf_GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(),ReadBookOnlineActivity.class);
                            startActivity(intent);
                        }
                    });*/
                        baseAdapter = new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return new_books_list.size();
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

                                viewHolder.new_books_name.setText(new_books_list.get(position).get("book_name").toString());
                                viewHolder.new_books_ImageView.setImageBitmap((Bitmap)new_books_list.get(position).get("book_list_image"));

                                return view;
                            }
                            class ViewHolder {
                                protected TextView new_books_name;
                                protected ImageView new_books_ImageView;
                            }
                        };
                        //配置适配器
                        my_books_shelf_GridView.setAdapter(baseAdapter);

                        //添加列表项点击的监听器,用户点击图书即可进行阅读
                        my_books_shelf_GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(),ReadBookOnlineActivity.class);
                                startActivity(intent);
                            }
                        });

                        //用户长按图书，即可将该本图书移除书架
                        my_books_shelf_GridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                new AlertDialog.Builder(getActivity()).setTitle("系统提示")//设置对话框标题
                                        .setMessage("您确定要将"+new_books_list.get(position).get("book_name")+"移除书架吗？")//设置显示的内容
                                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                // TODO Auto-generated method stub
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        canncle_position = position;
                                                        //获取json字符串
                                                        int book_id = (Integer) new_books_list.get(position).get("book_id");
                                                        String str = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/canncleMyShelfBook.php?user_id="+user_id+"&book_id="+book_id,"GET");
                                                        if_flag = Integer.valueOf(str);
                                                        //getActivity().finish();
                                                        //创建消息对象
                                                        Message message = new Message();
                                                        //为消息对象设置标识
                                                        message.what = 67;
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
                    }else if (msg.what == 67){      //主线程接收到来自子线程希望从书架中移除指定图书的消息，更新UI
                        if (if_flag > 0){   //说明取消移除该图书操作成功
                            Toast.makeText(getActivity(), "移除成功！", Toast.LENGTH_SHORT).show();
                            //将被移除的图书从集合中删除，并更新网格列表
                            new_books_list.remove(canncle_position);
                            baseAdapter.notifyDataSetChanged();
                        }else {     //说明取消移除该图书操作失败

                        }
                    }
                }
            };
        }else {
            Toast.makeText(getActivity(), "rootView不为空", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }
    //给新书到馆模块的GridView的适配器赋数据
    private void getNewBooksData(){
        /*for(int i=0;i<new_boos_image.length;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", new_boos_image[i]);
            map.put("text", new_boos_name[i]);
            list.add(map);
        }*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取json字符串
                String json = Utils.httpConntection("http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyShelfBookInfo.php?user_id="+user_id,"GET");
                Log.d("获取我的书架图书信息URL","http://"+getString(R.string.sever_ip)+"/TJPUSever/getMyShelfBookInfo.php?user_id="+user_id);
                //解析json字符串
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(json);
                    Log.d("我的书架图书的jsonArray数组长度",jsonArray.length()+"");
                    for (int i = 0;i<jsonArray.length();++i){
                        HashMap<String,Object> map = new HashMap<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //图片资源
                        String url = "http://"+getString(R.string.sever_ip)+"/TJPUSever/images/Books/"+jsonObject.getString("book_list_image");
                        //得到可用的图片
                        Bitmap bitmap = getHttpBitmap(url);
                        map.put("book_list_image",bitmap);
                        map.put("book_name",jsonObject.getString("book_name"));
                        map.put("book_id",jsonObject.getInt("book_id"));
                        new_books_list.add(map);
                    }
                    //创建消息对象
                    Message message = new Message();
                    //为消息对象设置标识
                    message.what = 66;
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
    }
}