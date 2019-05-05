package com.example.a14574.expresshelp;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import http.ClientSender;
import http.HttpCallbackListener;
import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {      //登录活动

    public static Socket socket = null;
    public static User USER = null;
    private EditText telephoneEditText;     //电话号编辑框
    private ImageView passwordImageView;
    //private Button adminLoginButton;        //切换到客服登录的按钮
    private boolean passwordStatus = false;     //密码状态
    private EditText passwordEditText;      //密码编辑框
    private Button normalLogin;     //登录按钮
    private Toolbar toolbar;        //
    private ProgressDialog progressDialog;                   //登录状态对话框
    public static Bitmap myBitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();     //初始化各种属性
        initEvents();    //初始化监听器
    }
    //初始化各种属性
    private void initViews(){
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordImageView = (ImageView) findViewById(R.id.pwd_image);
        normalLogin = (Button)findViewById(R.id.normal_login);
        telephoneEditText = (EditText)findViewById(R.id.telephone);
        passwordEditText = (EditText)findViewById(R.id.password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //adminLoginButton =  (Button) findViewById(R.id.admin_login_button);
        normalLogin = (Button) findViewById(R.id.normal_login);
    }
    //初始化监听器
    private void initEvents(){
        normalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();            //相应监听事件，调用登录方法
                /*Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);*/
            }
        });
        /*adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });*/
        passwordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordStatus) {
                    passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.show_password));
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordStatus=true;
                }else{
                    passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.hide_password));
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    passwordStatus=false;
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });
        Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //登录操作
    private void login() {
        String originAddress = this.getString(R.string.TheServer) +"loginServlet";  //访问服务器的url

        //旋转等待框
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("正在登录，请稍后......");
        progressDialog.setMessage("登录中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
       //检查用户输入的账号和密码的合法性
        if (!isInputValid()){
            progressDialog.dismiss();
            return;
        }
        //构造HashMap     用于构造完整的url
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("telephone", telephoneEditText.getText().toString());
        params.put("password", passwordEditText.getText().toString());
        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
           HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
               @Override
               public void onFailure(Call call, IOException e) {
                   Looper.prepare();
                   Toast.makeText(LoginActivity.this,"登录失败,未能连上服务器", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
                   Looper.loop();
               }
               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   Looper.prepare();    //在子线程中显示toast;
                   if(!response.isSuccessful()){
                       Toast.makeText(LoginActivity.this,"连接网络失败", Toast.LENGTH_SHORT).show();
                       progressDialog.dismiss();
                       Looper.loop();
                       return;
                   }
                   String result = response.body().string().trim();
                   if (LoginActivity.this.getString(R.string.HTTPERROR).equals(result)){
                       result = "验证失败";
                       progressDialog.dismiss();
                   }else{
                       if(result == null || result.equals("")){
                           progressDialog.dismiss();
                           return ;
                       }
                       USER = new Gson().fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
                       Intent intent = new Intent();
                       intent.setClass(LoginActivity.this,HomeActivity.class);     //登录成功跳转到主界面
                       LoginActivity.this.startActivity(intent);
                       result = "验证成功";
                       SharedPreferences sp= getSharedPreferences("loginSetting", 0);   //生成持久化对象
                       SharedPreferences.Editor editor = sp.edit();
                       editor.putInt("userid",USER.getId());
                       editor.commit();     //提交持久化对象
                       progressDialog.dismiss();
                       LoginActivity.this.finish();
                   }
                   Toast.makeText(LoginActivity.this,result, Toast.LENGTH_SHORT).show();
                   Looper.loop();
               }
           });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isInputValid() {
        //检查用户输入的合法性，这里暂且默认用户输入合法
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

}
