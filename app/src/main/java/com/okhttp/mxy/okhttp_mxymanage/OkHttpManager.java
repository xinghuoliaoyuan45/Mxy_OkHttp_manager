package com.okhttp.mxy.okhttp_mxymanage;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by mxy on 2016/9/14.
 * 封装的工具类
 */

public class OkHttpManager {
    private  volatile  static  OkHttpManager manager;
    private OkHttpClient client;
    private Handler handler;
    private final String TAG = OkHttpManager.class.getSimpleName();
    private OkHttpManager(){
        client=new OkHttpClient();
        handler=new Handler(Looper.getMainLooper());

    }
    public static  OkHttpManager getInstance(){
        OkHttpManager instance=null;
        if(manager==null){
            synchronized (OkHttpManager.class){
                if(instance==null){
                    instance=new OkHttpManager();
                    manager=instance;
                }
            }
        }
        return manager;
    }
    /**
     * 同步的请求
     */
    public  String syncGetByUrl(String url){
        //构建一个reques请求
        okhttp3.Request request=new Request.Builder().url(url).build();
        Response response=null;
        try{
            response = client.newCall(request).execute();//同步请求数据
            if (response.isSuccessful()) {
                return response.body().string();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据url异步请求
     * @param url
     */
    public void asyncByteByUrl(String url,final Func callBack){
        final Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG,"失败图片");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()){
                    Log.i(TAG,"图片数据返回来数据了");
                    onSuccessByteMethod(response.body().bytes(),callBack);
                }
            }
        });
    }

    public void sendComplexForm(String url,Map<String,String> params,final Func1 callBack ){
        FormBody.Builder form_builder=new FormBody.Builder();
        Log.i(TAG,"走到这里1");
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String,String> entry:params.entrySet()){
                form_builder.add(entry.getKey(),entry.getValue());
                Log.i(TAG,"走到这里2");
            }
        }
        final RequestBody requestBody=form_builder.build();
        final Request request=new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful()) {
                    Log.i(TAG,"tomcat返回来数据了");
                    onSuccessStringMethod(response.body().string(),callBack);
                }
            }
        });


    }

    /**
     * 从异步请求中拿到数据
     * 目的 放到主线程里面执行
     * 用传进来的callBack的onResponse处理
     * 声明借口(ps:类似JavaScript回调)
     *
     *
     */
    private void onSuccessByteMethod(final byte[] value, final Func callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack!=null){
                    try{
                        callBack.onResponse(value);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void onSuccessStringMethod(final String value, final Func1 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack!=null){
                    try{
                        callBack.onResponse(value);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    
    interface  Func{
        void onResponse(byte[] result);
    }
    interface  Func1{
        void onResponse(String values);
    }








}
