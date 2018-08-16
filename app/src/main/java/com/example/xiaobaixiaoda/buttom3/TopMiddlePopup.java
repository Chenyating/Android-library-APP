package com.example.xiaobaixiaoda.buttom3;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xiaobaixiaoda on 2017/10/31.
 */

public class TopMiddlePopup extends PopupWindow {

    private Context myContext;
    private ListView myLv;
    private TextView tv_null;
    private OnItemClickListener myOnItemClickListener;
    private ArrayList<String> myItems;
    private int myWidth;
    private int myHeight;
    private int myType;

    // 判断是否需要添加或更新列表子类项
    private boolean myIsDirty = true;

    private LayoutInflater inflater = null;
    private View myMenuView;

    private LinearLayout popupLL;

    private PopupAdapter adapter;

    public TopMiddlePopup(Context context) {
        // TODO Auto-generated constructor stub
    }

    public TopMiddlePopup(Context context, int width, int height,
                          OnItemClickListener onItemClickListener, ArrayList<String> items,
                          int type) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myMenuView = inflater.inflate(R.layout.sub_classify_top_popup, null);

        this.myContext = context;
        this.myItems = items;
        this.myOnItemClickListener = onItemClickListener;
        this.myType = type;
        this.myWidth = width;
        this.myHeight = height;

        System.out.println("--myWidth--:" + myWidth + "--myHeight--:"
                + myHeight);
        initWidget();
        setPopup();
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        myLv = (ListView) myMenuView.findViewById(R.id.popup_lv);
        popupLL = (LinearLayout) myMenuView.findViewById(R.id.popup_layout);
        tv_null = (TextView)myMenuView.findViewById(R.id.tv_null);
        myLv.setOnItemClickListener(myOnItemClickListener);
    }

    /**
     * 设置popup的样式
     */
    private void setPopup() {
        // 设置AccessoryPopup的view
        this.setContentView(myMenuView);
        // 设置AccessoryPopup弹出窗体的宽度
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置AccessoryPopup弹出窗体的高度：LayoutParams.MATCH_PARENT
        this.setHeight(myHeight);
        // 设置AccessoryPopup弹出窗体可点击
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimTopMiddle);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x33000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        myMenuView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("下拉菜单监听事件运行","");
                int height = popupLL.getBottom();
                int left = popupLL.getLeft();
                int right = popupLL.getRight();
                System.out.println("--popupLL.getBottom()--:"
                        + popupLL.getBottom());
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || x < left || x > right) {
                        System.out.println("---点击位置在列表下方--");
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 显示弹窗界面
     *
     * @param view
     */
    public void show(View view) {
        if (myIsDirty) {
            myIsDirty = false;
            adapter = new PopupAdapter(myContext, myItems, myType ,myWidth ,myHeight);
            View listItem = adapter.getView(0, null, myLv);
            listItem.measure(0, 0); // 计算子项View 的宽高
            int list_child_item_height = listItem.getMeasuredHeight() * adapter.getCount();
            if(list_child_item_height < myHeight){
                Log.d("谁大谁小？","item高度小于屏幕高度");
                tv_null.setVisibility(View.VISIBLE);
                tv_null.setHeight(myHeight - list_child_item_height);
            }else {
                Log.d("谁大谁小？","item高度大于屏幕高度");
                tv_null.setVisibility(View.GONE);
            }
            myLv.setAdapter(adapter);
        }

        showAsDropDown(view, 0, 0);
    }

}
