package com.example.xiaobaixiaoda.buttom3;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaobaixiaoda on 2017/10/26.
 */

public class SearchBookAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<SearchBookItem> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据
    List<SearchBookItem> backData;//用来备份原始数据
    MyFilter mFilter ;

    public SearchBookAdapter(Context context, List<SearchBookItem> data) {  //构造方法
        this.context = context;
        this.data = data;
        this.backData = data;
    }

    @Override
    public int getCount() { //返回多少，显示多少个列表项
        return data.size();
    }

    @Override
    public Object getItem(int position) {   //该方法的返回值决定第position处列表项的内容
        return null;
    }

    @Override
    public long getItemId(int position) {   ////该方法的返回值决定 第 i 处的列表项的ID
        return position;
    }

    /*
    *  position   表示第几项<item>的id（从 0 开始）
    *  view  表示每个item的view
    *  parent  表示父容器(例如:ListView,GridView,....)
    * */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //if (view ==null){
            switch (getItemViewType(position)){
                case 0:     //查看更多搜索历史提示项
                    view = LayoutInflater.from(context).inflate(R.layout.search_book0,null);
                    break;
                case 1:     //搜索资源过滤项
                    view = LayoutInflater.from(context).inflate(R.layout.search_book1,null);
                    ImageView icon_image = (ImageView)view.findViewById(R.id.icon_image);
                    TextView search_book_name = (TextView)view.findViewById(R.id.search_book_name);
                    icon_image.setImageResource(R.drawable.search_book5);
                    search_book_name.setText(data.get(position).getSearch_book_name());
                    search_book_name.setTextSize(18);
                    break;
                case 2:     //历史搜索和热门搜索显示项
                    view = LayoutInflater.from(context).inflate(R.layout.search_book2,null);
                    ImageView icon_image_history = (ImageView)view.findViewById(R.id.icon_image_history);
                    TextView search_book_name_history = (TextView)view.findViewById(R.id.search_book_name_history);
                    icon_image_history.setImageResource(R.drawable.search32);
                    search_book_name_history.setText(data.get(position).getSearch_book_name());
                    search_book_name_history.setTextSize(18);
                    break;
                case 3:     //热门搜索、换一批提示项
                    view = LayoutInflater.from(context).inflate(R.layout.search_book3,null);
                    TextView hot_search_textView = (TextView)view.findViewById(R.id.hot_search_textView);
                    hot_search_textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "点击换一批热门搜索", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        //}
        return view;
    }

    @Override
    public Filter getFilter() { //当ListView调用setTextFilter()方法的时候，便会调用该方法
        if (mFilter ==null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    class MyFilter extends Filter {     //我们需要定义一个过滤器的类来定义过滤规则
        //在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<SearchBookItem> list ;
            if (TextUtils.isEmpty(charSequence)){//当过滤的关键字为空的时候，我们则显示所有的数据
                list  = backData;
            }else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (SearchBookItem book:backData){
                    if (book.getSearch_book_name().contains(charSequence)){
                        Log.d("performFiltering:",book.getSearch_book_name());
                        list.add(book);
                    }

                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }

        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            data = (List<SearchBookItem>)filterResults.values;
            Log.d("publishResults:",filterResults.count+"");
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
                Log.d("TIPS","publishResults:notifyDataSetChanged");
            }else {
                notifyDataSetInvalidated();//通知数据失效
                Log.d("TIPS","publishResults:notifyDataSetInvalidated");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {      //表示不同布局的item的类型
        return data.get(position).getLayout_type();
    }

    @Override
    public int getViewTypeCount() {     //表示不同的item布局类型的数量
        return 4;
    }

}
