package com.example.a14574.expresshelp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import model.Order;

public class PayOrderActivity extends BaseActivity {
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
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        Intent intent = getIntent();
        order = (Order)intent.getSerializableExtra("order");
        initView();
        address.setText("快递点："+order.getExpressName());
        receive.setText("收货地址："+order.getGetAddress());
        name.setText("收货人姓名："+order.getTakeName());
        phone.setText("收货人电话："+order.getTakeTelephone());
        number.setText("取货码："+order.getTakeCode());
        money.setText("金额："+order.getMoney()+" ￥");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        first_time.setText("第一次收货时间："+sdf.format(order.getFirstTakeTimeBegin())+"-"
                +sdf.format(order.getFirstTakeTimeEnd()));
       second.setText("第二次收货时间："+sdf.format(order.getSecondTakeTimeBegin())+"-"+sdf.format(order.getSecondTakeTimeEnd()));
        initEvents();
        admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setState(1);


                //上传服务器


                Toast.makeText(PayOrderActivity.this,"支付成功",Toast.LENGTH_LONG).show();
            }
        });
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
    public void initEvents(){
        change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PayOrderActivity.this,ModifyOrderActivity.class);
                intent1.putExtra("order",order);
                startActivity(intent1);
                finish();
            }
        });

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
