package com.example.a14574.expresshelp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoSpecificActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView headImage;   //头像
    private TextView userName;           //用户名
    private TextView id;                 //id
    private TextView sex;                //性别
    private TextView submitNumber;     //订单发布次数
    private TextView credit;            //用户信誉度
    private TextView acceptNumber;     //订单接受次数
    private TextView grade;             //跑手评分
    private TextView telephone;         //电话号码
    private TextView balance;           //余额
    private TextView bedroomBuild;     //寝室楼栋
    private TextView bedroomNumber;     //寝室号

    private LinearLayout runnerInfo;    //跑手信息布局
    private TextView idCard;            //身份证号
    private TextView trueName;          //真实姓名
    private TextView schoolNumber;      //学号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_specific);
        initView();
    }
    private void initView(){
        headImage = (CircleImageView) findViewById(R.id.head_image);
        userName = (TextView) findViewById(R.id.user_name);
        id = (TextView) findViewById(R.id.id);
        sex = (TextView) findViewById(R.id.sex);
        submitNumber = (TextView) findViewById(R.id.submit_number);
        credit = (TextView) findViewById(R.id.credit);
        telephone = (TextView) findViewById(R.id.telephone);
        balance = (TextView) findViewById(R.id.balance);
        bedroomBuild = (TextView) findViewById(R.id.bedroom_number);
        bedroomNumber = (TextView) findViewById(R.id.bedroom_number);

        runnerInfo = (LinearLayout) findViewById(R.id.runner_info);
        idCard = (TextView) findViewById(R.id.idcard);
        trueName = (TextView) findViewById(R.id.true_name);
        grade = (TextView) findViewById(R.id.grade);
        acceptNumber = (TextView) findViewById(R.id.accept_number);
        schoolNumber = (TextView) findViewById(R.id.school_number);
        runnerInfo.setVisibility(View.GONE);       //除非为跑手，默认不显示跑手相关信息

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initViewData();
    }

    private void initViewData(){
        if(checkRunner()){
            runnerInfo.setVisibility(View.VISIBLE);
            idCard.setText(LoginActivity.USER.getIdCard()+"");
            grade.setText(LoginActivity.USER.getGrade()+"");
            acceptNumber.setText(LoginActivity.USER.getAcceptNumber()+"");
            if(LoginActivity.USER.getTrueName()!=null){
                trueName.setText(LoginActivity.USER.getTrueName()+"");
            }
            if(LoginActivity.USER.getSchoolNumber()!=null){
                schoolNumber.setText(LoginActivity.USER.getSchoolNumber()+"");
            }
        }
        //头像显示
        String url = this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
        Glide.with(this).load(url).into(headImage);

        id.setText(LoginActivity.USER.getId()+"");
        userName.setText(LoginActivity.USER.getUsername());
        sex.setText(LoginActivity.USER.getSex());
        telephone.setText(LoginActivity.USER.getTelephone());
        balance.setText(LoginActivity.USER.getBalance()+" ￥");
        credit.setText(LoginActivity.USER.getCredit()+"");
        submitNumber.setText(LoginActivity.USER.getSendNumber()+"");
        bedroomBuild.setText(LoginActivity.USER.getBedroomBuild()+"");
        Log.d("bedroomNumber",LoginActivity.USER.getBedroomNumber()+"");
        if(LoginActivity.USER.getBedroomNumber()!=0){
            bedroomNumber.setText(LoginActivity.USER.getBedroomNumber()+"");
        }
    }
    private boolean checkRunner(){   //检查是否为跑手
        if(LoginActivity.USER.getIdCard()==null || LoginActivity.USER.getIdCard().trim().equals("")){
            return false;
        }
        return true;
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
