package com.example.a14574.expresshelp;

import android.content.Intent;
import android.os.Handler;
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

import java.util.HashMap;

import http.HttpCallbackListener;
import http.HttpUtil;
import model.User;

public class LoginActivity extends AppCompatActivity {
//    private Button loginButton ;
 //   private EditText telephoneEditText;
  //  private EditText passwordEditText;
    private EditText telephoneEditText;
    private ImageView passwordImageView;
    private Button adminLoginButton;
    private boolean passwordStatus = false;
    private EditText passwordEditText;
    private Button normalLogin;
    private Toolbar toolbar;
    private String originAddress = "http://zuehfv.natappfree.cc/ExpressHelp/loginServlet";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";

            if ("Wrong".equals(msg.obj.toString())){
                result = "验证失败";
                Toast.makeText(LoginActivity.this,result, Toast.LENGTH_SHORT).show();
                return;
            }else {
                result = msg.obj.toString();
            }
            Log.d("日志",msg+"123");
            Gson gson = new Gson();
            Log.d("日志",result);
 //           List<User> users = gson.fromJson(result, new TypeToken<List<User>>(){}.getType());//把JSON格式的字符串转为List
            User user = gson.fromJson(result, User.class);
            Intent intent = new Intent();
            intent.putExtra("name", "诸葛亮");
            intent.putExtra("age", 50);
            intent.putExtra("IQ", 200.0f);
            intent.setClass(LoginActivity.this,HomeActivity.class);
            LoginActivity.this.startActivity(intent);
            Log.d("日志",user+"");
            Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        normalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("按钮点击","点击成功");
                login();
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
//        normalLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
//                startActivity(intent);
//            }
//        });



    }
    private void initView(){
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
    public void login() {
        //检查用户输入的账号和密码的合法性
        if (!isInputValid()){
            return;
        }
        //构造HashMap
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("telephone", telephoneEditText.getText().toString());
        params.put("password", passwordEditText.getText().toString());
        Log.d("用户名，密码",telephoneEditText.getText().toString()+","+passwordEditText.getText().toString());

        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("url:",compeletedURL);
            //发送请求
            HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    message.obj = response;
                    mHandler.sendMessage(message);
                }
                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
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
