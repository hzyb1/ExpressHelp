package com.example.a14574.expresshelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import fragment.HomePageFragment;
import fragment.MessageFragment;
import fragment.MyInfoFragment;
import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {           //主界面活动
    private static String LOGINFIELD = "Login failed";

    private Fragment fragment[] = new Fragment[3];
    RadioButton[ ] rbs = new RadioButton[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                /*//去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        /*if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        spLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        fragment[0] = new HomePageFragment();
        fragment[1] = new MessageFragment();
        fragment[2] = new MyInfoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,fragment[0])
                .add(R.id.container, fragment[1])
                .add(R.id.container, fragment[2]).commit();

        homePageFragment(null);
        rbs[0] = (RadioButton) findViewById(R.id.home_page);
        rbs[1] = (RadioButton) findViewById(R.id.message);
        rbs[2] = (RadioButton) findViewById(R.id.my_info);
        initView();
    }
    public void homePageFragment(View view){
        getSupportFragmentManager().beginTransaction()
                .hide(fragment[1])
                .hide(fragment[2])
                .show(fragment[0]).commit();
    }
    public void messageFragment(View view){
        getSupportFragmentManager().beginTransaction()
                .hide(fragment[0])
                .hide(fragment[2])
                .show(fragment[1]).commit();
    }
    public void myInfoFragment(View view){
        getSupportFragmentManager().beginTransaction()
                .hide(fragment[1])
                .hide(fragment[0])
                .show(fragment[2]).commit();
    }
    private void initView() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Log.d("Myheight",height+"");
        int realHeight = (int)(height/22.48);
        Log.d("Realheight",realHeight+"");
        //定义底部标签图片大小和位置
        Drawable drawable_home_page = getResources().getDrawable(R.drawable.selector_home_page);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_home_page.setBounds(0, 0,  realHeight,  realHeight);
        //设置图片在文字的哪个方向
        rbs[0].setCompoundDrawables(null, drawable_home_page, null, null);

        //定义底部标签图片大小和位置
        Drawable drawable_message = getResources().getDrawable(R.drawable.selector_message);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_message.setBounds(0, 0,  realHeight,  realHeight);
        //设置图片在文字的哪个方向
        rbs[1].setCompoundDrawables(null, drawable_message, null, null);

        //定义底部标签图片大小和位置
        Drawable drawable_my_info = getResources().getDrawable(R.drawable.selector_my_info);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_my_info.setBounds(0, 0,  realHeight,  realHeight);
        //设置图片在文字的哪个方向
        rbs[2].setCompoundDrawables(null, drawable_my_info, null, null);
    }

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
            String originAddress = "selectUserById";
            originAddress = this.getString(R.string.TheServer) + originAddress;
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
                    String result = response.body().string().trim();
                    if (LOGINFIELD.equals(result)){
                        goToLoginActivity();
                    }else{
                        if(result == null || result.equals("")){
                            return ;
                        }
                        LoginActivity.USER = new Gson().fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private void goToLoginActivity(){
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this,LoginActivity.class);     //跳转到登录界面
        HomeActivity.this.startActivity(intent);
        finish();
    }
}
