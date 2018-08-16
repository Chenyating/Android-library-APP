package com.example.xiaobaixiaoda.buttom3;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

//import android.view.View.MeasureSpec;

public class SearchListView extends ListView{

    public SearchListView(Context context) {
        super(context);
    }

    public SearchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}