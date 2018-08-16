package com.example.xiaobaixiaoda.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaobaixiaoda on 2017/11/20.
 */

public class Utils {
    //进行网络访问
    public static String httpConntection(String url,String request_model){
        String str = "";
        BufferedReader bufferedReader = null;
        StringBuilder response = null;
        HttpURLConnection httpURLConnection = null;
        try {
            /*
             * 首先获取HttpURLConnection的实例。一般只需要new一个URL对象，并传入目标的网络地址，
             * 然后调用一下openConnection()方法即可
             */
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            /*
            * 设置HTTP请求使用的方法。常用的方法主要有两个：
            * GET表示希望从服务器哪里获取数据
            * POST表示希望提交数据给服务器
            * */
            httpURLConnection.setRequestMethod(request_model);
            //设置连接超时的毫秒数
            httpURLConnection.setConnectTimeout(60000);
            //设置读取超时的毫秒数
            httpURLConnection.setReadTimeout(60000);
            //获取服务器返回的输入流
            InputStream inputStream = httpURLConnection.getInputStream();
            //关闭HTTP连接
            //httpURLConnection.disconnect();
            //下面对获取的输入流进行读取
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            response = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                System.out.println(line);
                if (line != null) {
                    line = line.replaceAll("\ufeff", "");
                }

                response.append(line);
            }
            str = response.toString();
            bufferedReader.close();
            //开始获取数据
            /*BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            str = bos.toString("utf-8");*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("NET_ERROR","网络访问错误");
        }finally {
            //关闭HTTP连接
            httpURLConnection.disconnect();
            //return str.substring(2);
            return str;
        }
    }
}
