package com.example.a14574.expresshelp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import model.User;

public class SpecificUserInfoActivity extends BaseActivity {

    private Toolbar toolbar;
    private CircleImageView headImage;   //头像
    private TextView userName;           //用户名
    private TextView id;                 //id
    private TextView sex;                //性别
    private ImageView sexImage;         //性别图像
    private TextView submitNumber;     //订单发布次数
    private TextView credit;            //用户信誉度
    private LinearLayout runnerInfo;   //显示跑手相关信息布局
    private TextView acceptNumber;     //订单接受次数
    private TextView grade;             //跑手评分
    private TextView telephone;         //电话号码
    private LinearLayout sendMessage;   //发消息

    private User user;                   //当前显示用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_user_info);
        initView();
        initEvents();
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
    private void initView(){    //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("详细信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        headImage = (CircleImageView) findViewById(R.id.head_image);
        userName = (TextView) findViewById(R.id.user_name);
        id = (TextView) findViewById(R.id.id);
        sex = (TextView) findViewById(R.id.sex);
        sexImage = (ImageView) findViewById(R.id.sex_image);
        submitNumber = (TextView) findViewById(R.id.submit_number);
        credit = (TextView) findViewById(R.id.user_credit);

        runnerInfo = (LinearLayout) findViewById(R.id.runner_info);
        acceptNumber = (TextView) findViewById(R.id.accept_number);
        grade = (TextView) findViewById(R.id.grade);
        telephone = (TextView) findViewById(R.id.telephone);
        sendMessage = (LinearLayout) findViewById(R.id.send_message);

        runnerInfo.setVisibility(View.GONE);       //默认不显示，若当前用户为跑手，则显示

        user = (User)getIntent().getSerializableExtra("user");

        initUserInfoView();
    }
    private void initEvents(){
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到聊天界面
                Toast.makeText(SpecificUserInfoActivity.this,"点击发消息",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkRunner(){   //检查是否为跑手
        if(user.getIdCard()==null || user.getIdCard().trim().equals("")){
            return false;
        }
        return true;
    }

    private void initUserInfoView(){   //初始化界面数据
        if(checkRunner()){
            runnerInfo.setVisibility(View.VISIBLE);
            acceptNumber.setText(user.getAcceptNumber()+"");
            grade.setText(user.getGrade()+"");
            telephone.setText(user.getTelephone());
        }
        //头像显示
        String url = this.getString(R.string.TheServer)+"headImages/"+ user.getHeadImage();
        Glide.with(this).load(url).into(headImage);

        userName.setText(user.getUsername());
        id.setText(user.getId()+"");
        sex.setText(user.getSex());
        //性别图像
        if(user.getSex().trim().equals("男")){
            Glide.with(this).load(R.drawable.male).into(sexImage);
        }else{
            Glide.with(this).load(R.drawable.female).into(sexImage);
        }

        submitNumber.setText(user.getSendNumber()+"");
        credit.setText(user.getCredit()+"");
    }
}
