package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    private EditText username;
    private EditText first_password;
    private EditText second_password;
    private EditText telephone;
    private String sex;
    private ImageView man;
    private ImageView wuman;
    private ImageView pwd_image;
    private ImageView pwd_image_scond;
    private boolean passwordStatus = false;
    private boolean passwordStatus_second = false;
    private Button register;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;                   //等待对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initEvents();
    }


    /**
     * 检验输入的合法性
     */
    private boolean isInputValid(){
        boolean vaild = false;

        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
        dialog.setTitle("错误信息");
        dialog.setCancelable(true);
        if(telephone.getText().toString().equals("")) {
            dialog.setMessage("电话号码不能为空");
            dialog.show();
        }else if(!telephone.getText().toString().matches("[1][3578]\\d{9}")){
            dialog.setMessage("电话号码格式错误");
            dialog.show();
        } else if (username.getText().toString().equals("")){
            dialog.setMessage("用户名不能为空");
            dialog.show();
        }else if (!first_password.getText().toString().equals(second_password.getText().toString())){
            dialog.setMessage("两次密码不一致");
            dialog.show();
        }else if(first_password.getText().toString().equals("") || second_password.getText().toString().equals("") ){
            dialog.setMessage("密码不能为空");
            dialog.show();
        }else if(sex.equals("无")){
            dialog.setMessage("请选择性别");
            dialog.show();
        }else{
            vaild = true;
        }
        return vaild;
    }

    /**
     * 初始化控件
     */
    private void initViews (){
        username = (EditText)findViewById(R.id.register_username);
        first_password = (EditText)findViewById(R.id.register_password_first);
        second_password = (EditText)findViewById(R.id.register_password_second);
        telephone = (EditText)findViewById(R.id.register_telephone);
        sex = "无";
        man = (ImageView)findViewById(R.id.man);
        wuman = (ImageView)findViewById(R.id.wuman);
        pwd_image = (ImageView)findViewById(R.id.register_pwd_image);
        pwd_image_scond = (ImageView)findViewById(R.id.register_pwd_image_second);
        register = (Button)findViewById(R.id.register_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void initEvents(){
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                man.setImageResource(R.drawable.select_man);
                wuman.setImageResource(R.drawable.wuman);
                sex = "男";
            }
        });
        wuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                man.setImageResource(R.drawable.man);
                wuman.setImageResource(R.drawable.select_wuman);
                sex = "女";
            }
        });

        pwd_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordStatus) {
                    pwd_image.setImageDrawable(getResources().getDrawable(R.drawable.show_password));
                    first_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordStatus=true;
                }else{
                    pwd_image.setImageDrawable(getResources().getDrawable(R.drawable.hide_password));
                    first_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    passwordStatus=false;
                }
                first_password.setSelection( first_password.getText().length());
            }
        });
        pwd_image_scond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordStatus_second) {
                    pwd_image_scond.setImageDrawable(getResources().getDrawable(R.drawable.show_password));
                    second_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordStatus_second=true;
                }else{
                    pwd_image_scond.setImageDrawable(getResources().getDrawable(R.drawable.hide_password));
                    second_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);;
                    passwordStatus_second=false;
                }
                second_password.setSelection( second_password.getText().length());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInputValid()){
                    User user = saveUser();
                    submitUser(user);
                }
            }
        });
    }


    /**
     * 保存User对象
     *
     */
    private User saveUser(){
        User user = new User();
        user.setPassword(first_password.getText().toString());
        user.setUsername(username.getText().toString());
        user.setTelephone(telephone.getText().toString());
        user.setSex(sex);
        return user;
    }

    //上传到服务器端
    private void submitUser(User user){
        //旋转等待框
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("正在注册，请稍后......");
        progressDialog.setMessage("注册中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            //构造完整URL
            String originAddress = "register";
            String compeletedURL = this.getString(R.string.TheServer) +  originAddress;
            Log.d("url:",compeletedURL);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(user));
            Log.d("日志:",gson.toJson(user));

            HttpUtil.sendPostOkHttpRequest(compeletedURL,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"注册失败,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();    //在子线程中显示toast;
                    if(!response.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
                    String result = response.body().string().trim();
                    Log.d("日志","发布成功"+result);
                    if(RegisterActivity.this.getString(R.string.HTTPSUCCESS).equals(result)){
                        Toast.makeText(RegisterActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,"注册失败，该电话号码已注册", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
