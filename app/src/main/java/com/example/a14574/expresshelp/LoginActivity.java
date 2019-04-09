package com.example.a14574.expresshelp;
import android.app.ProgressDialog;
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

import java.io.IOException;
import java.util.HashMap;

import http.HttpCallbackListener;
import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {      //登录活动

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
   Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {        //异步访问数据库
            super.handleMessage(msg);   //访问服务器获取收到的信息

            if(msg.arg1 == 404){
                Log.d("日志",msg+"连接失败");
                Toast.makeText(LoginActivity.this,"连接失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            String result = msg.obj.toString();
            if (LOGINFIELD.equals(result)){
                result = "验证失败";
                Toast.makeText(LoginActivity.this,result, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }else {
                result = msg.obj.toString();
            }

            progressDialog.dismiss();
            Log.d("日志",msg+"123");
            Gson gson = new Gson();
            Log.d("日志",result);
            if(result == null || result.equals("")){
                return ;
            }
 //           List<User> users = gson.fromJson(result, new TypeToken<List<User>>(){}.getType());//把JSON格式的字符串转为List
            User user = gson.fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
            Intent intent = new Intent();
//            intent.putExtra("name", "诸葛亮");
//            intent.putExtra("age", 50);
//            intent.putExtra("IQ", 200.0f);
            intent.setClass(LoginActivity.this,HomeActivity.class);     //登录成功跳转到主界面
            LoginActivity.this.startActivity(intent);
            Log.d("日志",user+"");
            Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();     //初始化各种属性
        //对登录按钮设置监听事件
        normalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();            //相应监听事件，调用登录方法
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
        originAddress = this.getString(R.string.TheServer) + originAddress;
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
            Log.d("url:",compeletedURL);
            //发送请求
           /* HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
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
            });*/
           HttpUtil.sendOkHttpRequest(compeletedURL,new okhttp3.Callback(){
               @Override
               public void onFailure(Call call, IOException e) {
                   Log.d("登录失败：","aaaa");
                   Message message = new Message();
                   message.arg1 = 404;
                   mHandler.sendMessage(message);
               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   Message message = new Message();
                   message.obj = response.body().string().trim();
                   mHandler.sendMessage(message);
                   Log.d("登录成功：",message.obj.toString()+"aaaa");
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
