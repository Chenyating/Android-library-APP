package com.example.xiaobaixiaoda.buttom3;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xiaobaixiaoda on 2017/10/31.
 */

public class PopupAdapter extends BaseAdapter {
    private Context myContext;
    private LayoutInflater inflater;
    private ArrayList<String> myItems;
    private int myType;
    int width,height;

    //构造方法
    public PopupAdapter(Context context, ArrayList<String> items, int type,int width, int height) {
        this.myContext = context;
        this.myItems = items;
        this.myType = type;
        this.width = width;
        this.height = height;
        inflater = LayoutInflater.from(myContext);

    }
    @Override
    public int getCount() {     //获取Item数量
        return myItems.size();
    }

    @Override
    public String getItem(int position) {   //获取Item值
        return myItems.get(position);
    }

    @Override
    public long getItemId(int position) {   //获取ItemID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopupHolder holder = null;
        if (convertView == null) {
            holder = new PopupHolder();
            convertView = inflater.inflate(R.layout.sub_classify_top_popup_item, null);
            holder.itemNameTv = (TextView) convertView
                    .findViewById(R.id.popup_tv);
            holder.itemNameTv.setGravity(Gravity.CENTER);
            convertView.setTag(holder);
        } else {
            holder = (PopupHolder) convertView.getTag();
        }
        String itemName = getItem(position);
        holder.itemNameTv.setText(itemName);
        return convertView;
    }

    private class PopupHolder {
        TextView itemNameTv;
    }
}
