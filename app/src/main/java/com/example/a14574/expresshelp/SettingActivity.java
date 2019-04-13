package com.example.a14574.expresshelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import util.ActivityCollector;

public class SettingActivity extends BaseActivity {

    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        logout = (Button) findViewById(R.id.logout);
        initEvents();
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
    }
}
