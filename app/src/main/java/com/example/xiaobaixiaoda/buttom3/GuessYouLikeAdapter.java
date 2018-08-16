package com.example.xiaobaixiaoda.buttom3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiaobaixiaoda on 2017/10/27.
 */

public class GuessYouLikeAdapter extends ArrayAdapter<GuessYouLikeBook> {

    //定义资源ID属性
    private int resourceID;

    //构造方法
    public GuessYouLikeAdapter(Context context, int textViewResourceID, List<GuessYouLikeBook> objects){
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }

    //重写getView方法，设置每个下拉列表的item布局
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前项的猜你喜欢图书实例
        GuessYouLikeBook guessYouLikeBook = getItem(position);
        //获取item布局文件
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        //获取item布局中的控件
        ImageView guess_you_like_ImageView = (ImageView)view.findViewById(R.id.guess_you_like_ImageView);
        TextView guess_you_like_book_name = (TextView)view.findViewById(R.id.guess_you_like_book_name);
        TextView guess_you_like_author_name = (TextView)view.findViewById(R.id.guess_you_like_author_name);
        //设置ImageView的图片资源jlb1-256
        guess_you_like_ImageView.setImageResource(guessYouLikeBook.getGuess_you_like_imageID());
        //设置书名
        guess_you_like_book_name.setText(guessYouLikeBook.getGuess_you_like_bookName());
        //设置作者名
        guess_you_like_author_name.setText(guessYouLikeBook.getGuess_you_like_authorName());
        return view;
    }
}
