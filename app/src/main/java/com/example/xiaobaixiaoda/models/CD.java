package com.example.xiaobaixiaoda.models;

import java.util.HashMap;

/**
 * Created by xiaobaixiaoda on 2017/11/20.
 * 馆藏资源光盘的模型类
 */

public class CD {
    //1、定义成员属性
    private int cd_id;
    private String cd_number;
    private String cd_tittle;
    private String cd_classify_number;
    private String cd_publish_identify;
    private String cd_publisher;
    private int cd_statue = 1;
    private String cd_location;
    private int cd_total_number = 10;
    private int cd_available_number = 10;
    //2、定义构造方法
    public CD(HashMap<String,Object> map){
        this.setCd_id((Integer)map.get("cd_id"));
        this.setCd_number((String)map.get("cd_number"));
        this.setCd_tittle((String)map.get("cd_tittle"));
        this.setCd_classify_number((String)map.get("cd_classify_number"));
        this.setCd_publish_identify((String)map.get("cd_publish_identify"));
        this.setCd_publisher((String)map.get("cd_publisher"));
        this.setCd_statue((Integer)map.get("cd_statue"));
        this.setCd_location((String)map.get("cd_location"));
        this.setCd_total_number((Integer)map.get("cd_total_number"));
        this.setCd_available_number((Integer)map.get("cd_available_number"));
    }
    //3、对成员变量进行封装
    public int getCd_available_number() {
        return cd_available_number;
    }

    public void setCd_available_number(int cd_available_number) {
        this.cd_available_number = cd_available_number;
    }

    public String getCd_classify_number() {
        return cd_classify_number;
    }

    public void setCd_classify_number(String cd_classify_number) {
        this.cd_classify_number = cd_classify_number;
    }

    public int getCd_id() {
        return cd_id;
    }

    public void setCd_id(int cd_id) {
        this.cd_id = cd_id;
    }

    public String getCd_location() {
        return cd_location;
    }

    public void setCd_location(String cd_location) {
        this.cd_location = cd_location;
    }

    public String getCd_number() {
        return cd_number;
    }

    public void setCd_number(String cd_number) {
        this.cd_number = cd_number;
    }

    public String getCd_publish_identify() {
        return cd_publish_identify;
    }

    public void setCd_publish_identify(String cd_publish_identify) {
        this.cd_publish_identify = cd_publish_identify;
    }

    public String getCd_publisher() {
        return cd_publisher;
    }

    public void setCd_publisher(String cd_publisher) {
        this.cd_publisher = cd_publisher;
    }

    public int getCd_statue() {
        return cd_statue;
    }

    public void setCd_statue(int cd_statue) {
        this.cd_statue = cd_statue;
    }

    public String getCd_tittle() {
        return cd_tittle;
    }

    public void setCd_tittle(String cd_tittle) {
        this.cd_tittle = cd_tittle;
    }

    public int getCd_total_number() {
        return cd_total_number;
    }

    public void setCd_total_number(int cd_total_number) {
        this.cd_total_number = cd_total_number;
    }
}
