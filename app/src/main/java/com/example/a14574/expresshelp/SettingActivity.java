package com.example.a14574.expresshelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import util.ActivityCollector;

public class SettingActivity extends BaseActivity {

    private Button logout;
    private CircleImageView headImage;
    private TextView userName;
    private TextView balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvents();
    }

    private void initViews(){
        logout = (Button) findViewById(R.id.logout);
        headImage = (CircleImageView) findViewById(R.id.head_image);
        userName = (TextView)findViewById(R.id.user_name);
        balance = (TextView)findViewById(R.id.balance);
        if(LoginActivity.USER!=null){
            userName.setText(LoginActivity.USER.getUsername());
            balance.setText(LoginActivity.USER.getBalance()+"￥");
            String url = this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
            Glide.with(this).load(url).into(headImage);;
        }




    }

    private void initEvents(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp= getSharedPreferences("loginSetting", 0);
                int userid = sp.getInt("userid",0);
                Log.d("seeuserid",userid+"");
                if(userid!=0){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("userid",0);
                    editor.commit();
                    Toast.makeText(SettingActivity.this,"成功注销，请重新登录！！！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        });
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });
    }
}
