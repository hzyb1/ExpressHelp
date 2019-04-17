package com.example.a14574.expresshelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        closeAndroidPDialog();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        spLogin();

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
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}