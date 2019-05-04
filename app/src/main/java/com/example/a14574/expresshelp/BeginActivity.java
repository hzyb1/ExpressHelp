package com.example.a14574.expresshelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.Response;

public class BeginActivity extends BaseActivity {
    private ImageView beginImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_begin);
        beginImage = (ImageView) findViewById(R.id.begin_image);
     //   Glide.with(this).load(R.drawable.start_image).into(beginImage);
        spLogin();          //持久化登录

    }

    //持久化登录
    private boolean spLogin(){
        SharedPreferences sp = getSharedPreferences("loginSetting", 0);
        int id = sp.getInt("userid",0);
        Log.d("Id是",id+"");
        if(id == 0){
            goToLoginActivity();
            return false;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", id+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "selectUserById";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("URL:",compeletedURL);

            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    goToLoginActivity();
                    Log.d("连接服务器失败",e.toString());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    boolean flag = true;
                    //访问异常，返回值不是200
                    if(!response.isSuccessful()){
                        flag = false;
                    }else{
                        String result = response.body().string().trim();
                        if (BeginActivity.this.getString(R.string.HTTPERROR).equals(result)){
                            flag = false;
                        }else{
                            if(result == null || result.equals("")){
                                flag = false;
                            }
                            LoginActivity.USER = new Gson().fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
                            if( LoginActivity.USER == null){
                                flag = false;
                            }
                        }
                    }
                    Intent intent = new Intent();
                    if(flag == true){
                        intent.setClass(BeginActivity.this,HomeActivity.class);     //跳转到主界面
                    }else{
                        intent.setClass(BeginActivity.this,LoginActivity.class);    //跳转到登录界面
                    }
                    BeginActivity.this.startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private void goToLoginActivity(){
        Intent intent = new Intent();
        intent.setClass(BeginActivity.this,LoginActivity.class);     //跳转到登录界面
        BeginActivity.this.startActivity(intent);
        finish();
    }
}