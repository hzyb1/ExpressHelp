package com.example.a14574.expresshelp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import http.HttpCallbackListener;
import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {      //登录活动

    public static User USER = null;
    private static String LOGINFIELD = "Login failed";
    private EditText telephoneEditText;     //电话号编辑框
    private ImageView passwordImageView;
    private Button adminLoginButton;        //切换到客服登录的按钮
    private boolean passwordStatus = false;     //密码状态
    private EditText passwordEditText;      //密码编辑框
    private Button normalLogin;     //登录按钮
    private Toolbar toolbar;        //
    private String originAddress = "loginServlet";          //登录所访问的服务器url
    private ProgressDialog progressDialog;                   //登录状态对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();     //初始化各种属性
        //对登录按钮设置监听事件
       initEvents();    //初始化监听器
    }
    private void initViews(){
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordImageView = (ImageView) findViewById(R.id.pwd_image);
        normalLogin = (Button)findViewById(R.id.normal_login);
        telephoneEditText = (EditText)findViewById(R.id.telephone);
        passwordEditText = (EditText)findViewById(R.id.password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        adminLoginButton =  (Button) findViewById(R.id.admin_login_button);
        normalLogin = (Button) findViewById(R.id.normal_login);
    }

    private void initEvents(){
        normalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();            //相应监听事件，调用登录方法
                /*Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);*/
            }
        });
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });
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
    private void login() {
        originAddress = this.getString(R.string.TheServer) + originAddress;

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
        Log.d("用户名，密码",telephoneEditText.getText().toString()+","+passwordEditText.getText().toString());

        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);

           HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
               @Override
               public void onFailure(Call call, IOException e) {
                   Looper.prepare();
                   Toast.makeText(LoginActivity.this,"登录失败,未能连上服务器", Toast.LENGTH_SHORT).show();
                   Log.d("连接服务器失败",e.toString());
                   progressDialog.dismiss();
                   Looper.loop();
               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   Looper.prepare();
                   String result = response.body().string().trim();
                   if (LOGINFIELD.equals(result)){
                       result = "验证失败";
                   }else{
                       if(result == null || result.equals("")){
                           return ;
                       }
                       USER = new Gson().fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
                       Intent intent = new Intent();
                       intent.setClass(LoginActivity.this,HomeActivity.class);     //登录成功跳转到主界面
                       LoginActivity.this.startActivity(intent);
                       result = "验证成功";
                       SharedPreferences sp= getSharedPreferences("loginSetting", 0);
                       SharedPreferences.Editor editor = sp.edit();
                       editor.putInt("userid",USER.getId());
                       editor.commit();
                       progressDialog.dismiss();
                       LoginActivity.this.finish();
                   }
                   Toast.makeText(LoginActivity.this,result, Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
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
}
