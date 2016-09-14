package com.okhttp.mxy.okhttp_mxymanage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private  final  static String TAG = MainActivity.class.getSimpleName();
    private String image_path = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png";
    private OkHttpManager okHttpManager;
    private ImageView imageView;
    private  Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button) findViewById(R.id.button);
        imageView=(ImageView)findViewById(R.id.img);

        button.setOnClickListener(this);
        okHttpManager=OkHttpManager.getInstance();

    }

    @Override
    public void onClick(View v) {
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
        }
    }
}

