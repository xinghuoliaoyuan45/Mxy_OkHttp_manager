package com.okhttp.mxy.okhttp_mxymanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private  final  static String TAG = MainActivity.class.getSimpleName();
    private String image_path = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png";
    private String form_path="http://192.168.56.1:8080/mxy_webproject/LoginAction";
    private OkHttpManager okHttpManager;
    private ImageView imageView;
    private  Button button;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button) findViewById(R.id.button);
        imageView=(ImageView)findViewById(R.id.img);
        button1=(Button) findViewById(R.id.button1);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        okHttpManager=OkHttpManager.getInstance();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.button:
                okHttpManager.asyncByteByUrl(image_path, new OkHttpManager.Func() {
                    @Override
                    public void onResponse(byte[] result) {
                        Log.i(TAG, String.valueOf(result));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                        imageView.setImageBitmap(bitmap);
                    }
                });
            case  R.id.button1:
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("username" , "admin");
                map.put("password" , "12345");
                okHttpManager.sendComplexForm(form_path, map , new OkHttpManager.Func1() {
                    @Override
                    public void onResponse(String values) {
                        Log.i(TAG,values);
                    }
                });
        }
    }
}

