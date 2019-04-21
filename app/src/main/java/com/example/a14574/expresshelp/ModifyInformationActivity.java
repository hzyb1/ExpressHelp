package com.example.a14574.expresshelp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import http.HttpUtil;
import model.Order;
import model.User;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyInformationActivity extends AppCompatActivity {
    private EditText username;
    private EditText sex;
    private EditText phone;
    private EditText ID;
    private EditText name;
    private EditText schoolNumber;
    private EditText bedroomBuild;
    private EditText bedroomNumber;
    private Button admit;
    private User user;
    private boolean flag;
    private CircleImageView headImage;
    private Toolbar toolbar;

    private ProgressDialog progressDialog;                   //上传状态对话框

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if(ModifyInformationActivity.this.getString(R.string.HTTPERROR).equals(result)){
                Toast.makeText(ModifyInformationActivity.this,"上传失败！！！",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ModifyInformationActivity.this,"上传成功！！！",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ModifyInformationActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        initView();
        admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()){
                    user.setUsername(username.getText().toString());
                    user.setSex(sex.getText().toString());
                    user.setTelephone(phone.getText().toString());
                    user.setIdCard(ID.getText().toString());
                    user.setSchoolNumber(schoolNumber.getText().toString());
                    user.setTrueName(name.getText().toString().trim());
                    user.setSchoolNumber(schoolNumber.getText().toString().trim());
                    if (bedroomBuild.getText().toString().isEmpty()){
                        user.setBedroomBuild(0);
                    }else{
                        user.setBedroomBuild(Integer.parseInt(bedroomBuild.getText().toString()));
                        }
                    if (bedroomNumber.getText().toString().isEmpty()){
                        user.setBedroomNumber(0);
                    }else{
                        user.setBedroomNumber(Integer.parseInt(bedroomNumber.getText().toString()));
                    }
                    //上传服务器

                    updataUser(user);
                }
            }
        });
    }
    private void initView(){
     username = (EditText)findViewById(R.id.modify_username);
     sex = (EditText)findViewById(R.id.modify_sex);
     sex.setEnabled(false);
     phone = (EditText)findViewById(R.id.modify_telephone);
     phone.setEnabled(false);
     ID = (EditText)findViewById(R.id.modify_ID);
     name = (EditText)findViewById(R.id.modify_name);
     schoolNumber = (EditText)findViewById(R.id.modify_schoolNumber);
     bedroomBuild = (EditText)findViewById(R.id.modify_bedroomBuild);
     bedroomNumber = (EditText)findViewById(R.id.modify_bedroomNumber);
     admit = (Button)findViewById(R.id.modify_admit);
     user = LoginActivity.USER;
     username.setText(user.getUsername());
     sex.setText(user.getSex());
     phone.setText(user.getTelephone());
     ID.setText(user.getIdCard());
     name.setText(user.getTrueName());
     schoolNumber.setText(user.getSchoolNumber());
     headImage = (CircleImageView) findViewById(R.id.head_image);
     toolbar = (Toolbar) findViewById(R.id.toolbar);
     toolbar.setTitle("");
     setSupportActionBar(toolbar);
     ActionBar actionBar = getSupportActionBar();
     if(actionBar!=null){
         actionBar.setDisplayHomeAsUpEnabled(true);
     }
     if (user.getBedroomBuild() == 0){
         bedroomBuild.setText("");
     }else{
         bedroomBuild.setText(user.getBedroomBuild()+"");
     }
     if (user.getBedroomNumber() == 0){
         bedroomNumber.setText("");
     }else{
         bedroomNumber.setText(user.getBedroomNumber()+"");
     }
     Log.d("日志","目前的user状态"+user.getTrueName());
     if(LoginActivity.USER!=null){
            String url = this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
            Glide.with(this).load(url).into(headImage);
        }
    }

    private boolean check(){
        if (name.getText().toString().isEmpty() && ID.getText().toString().isEmpty() && schoolNumber.getText().toString().isEmpty()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyInformationActivity.this);
            dialog.setTitle("信息不完善");
            dialog.setMessage("跑手信息未注册，不能成为跑手，是否继续注册");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    flag = false;
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    flag = true;
                }
            });
            dialog.show();
            return flag;
        }else if (name.getText().toString().isEmpty() || ID.getText().toString().isEmpty() || schoolNumber.getText().toString().isEmpty()){
           Toast.makeText(ModifyInformationActivity.this,"注册跑手信息要填写完善",Toast.LENGTH_SHORT).show();
           return false;
       }/*else if(ID.getText().toString().matches("\\d{18}")){
            Toast.makeText(ModifyInformationActivity.this,"请输入正确的身份证号",Toast.LENGTH_SHORT).show();
            return false;
        }*/else {
           return true;
       }
    }
    private void updataUser(User user){
        progressDialog = new ProgressDialog(ModifyInformationActivity.this);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "updataUser";
            Log.d("url:",originAddress);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();

            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(user));

            Log.d("日志:",gson.toJson(user));
            HttpUtil.sendPostOkHttpRequest(originAddress,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(ModifyInformationActivity.this,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(ModifyInformationActivity.this,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    mHandler.sendMessage(message);
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
