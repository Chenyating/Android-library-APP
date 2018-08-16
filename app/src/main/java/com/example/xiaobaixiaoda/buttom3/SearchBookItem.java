package com.example.xiaobaixiaoda.buttom3;

/**
 * Created by xiaobaixiaoda on 2017/10/26.
 */

public class SearchBookItem {

    private String search_book_name;
    private int layout_type;

    public SearchBookItem(String search_book_name, int layout_type){
        this.setSearch_book_name(search_book_name);
        this.setLayout_type(layout_type);
    }

    public String getSearch_book_name() {
        return search_book_name;
    }

    public void setSearch_book_name(String search_book_name) {
        this.search_book_name = search_book_name;
    }

    public int getLayout_type() {
        return layout_type;
    }

    public void setLayout_type(int layout_type) {
        this.layout_type = layout_type;
    }

}
