package com.example.a14574.expresshelp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.User;

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
                    user.setTrueName(name.getText().toString().trim());
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
       }else{
           return true;
       }
    }
}
