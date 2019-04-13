package com.example.a14574.expresshelp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import model.Order;

public class PayOrderActivity extends AppCompatActivity {
    private TextView address;
    private TextView receive;
    private TextView name;
    private TextView phone;
    private TextView number;
    private TextView money;
    private TextView first_time;
    private TextView second;
    private Button admit;
    private Button change;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        Intent intent = getIntent();
        Order order = (Order)intent.getSerializableExtra("order");
        initView();
        address.setText("快递点："+order.getExpressName());
        receive.setText("收货地址："+order.getGetAddress());
        name.setText("收货人姓名："+order.getTakeName());
        phone.setText("收货人电话："+order.getTakeTelephone());
        number.setText("取货码："+order.getTakeCode());
        money.setText("金额："+order.getMoney()+" ￥");
        first_time.setText("第一次收货时间："+order.getFirstTakeTimeBegin().toString().substring(0,5)+"-"+order.getFirstTakeTimeEnd().toString().substring(0,5));
        second.setText("第二次收货时间："+order.getSecondTakeTimeBegin().toString().substring(0,5)+"-"+order.getSecondTakeTimeEnd().toString().substring(0,5));
    }

    private void initView (){
        address = (TextView)findViewById(R.id.pay_address);
        receive = (TextView)findViewById(R.id.pay_receive);
        name = (TextView)findViewById(R.id.pay_name);
        phone = (TextView)findViewById(R.id.pay_telephone);
        number = (TextView)findViewById(R.id.pay_number);
        money = (TextView)findViewById(R.id.pay_money);
        first_time = (TextView)findViewById(R.id.pay_time_first);
        second = (TextView)findViewById(R.id.pay_time_second);
        admit = (Button)findViewById(R.id.pay_admit);
        change = (Button)findViewById(R.id.pay_change);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
