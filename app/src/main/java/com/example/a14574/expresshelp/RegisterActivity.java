package com.example.a14574.expresshelp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import model.User;

public class RegisterActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
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
                    saveUser();
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView (){
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
    }

    /**
     * 保存User对象
     * 上传到服务器端
     */
    private void saveUser(){
        User user = new User();
        user.setPassword(first_password.getText().toString());
        user.setUsername(username.getText().toString());
        user.setTelephone(telephone.getText().toString());
        user.setSex(sex);
    }
}
