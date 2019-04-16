package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayOrderActivity extends BaseActivity {
    private TextView address;
    private TextView receive;
    private TextView name;
    private TextView phone;
    private TextView number;
    private TextView money;
    private TextView first_time;
    private TextView second;
    private Button admit;       //支付按钮
    private Button change;      //修改订单按钮
    private Toolbar toolbar;
    private Order order;
    private ProgressDialog progressDialog;                   //上传状态对话框
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if(PayOrderActivity.this.getString(R.string.HTTPERROR).equals(result)){
                Toast.makeText(PayOrderActivity.this,"上传失败！！！",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(PayOrderActivity.this,"上传成功！！！",Toast.LENGTH_LONG).show();
                LoginActivity.USER.setBalance(LoginActivity.USER.getBalance() - order.getMoney());      //用户金额减少
                Intent intent = new Intent(PayOrderActivity.this,MyOrderActivity.class);
                intent.putExtra("style",3);     //进去我的订单查看刚待接单订单
                startActivity(intent);
                finish();
            }
            progressDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        Intent intent = getIntent();
        order = (Order)intent.getSerializableExtra("order");
        initView();

        initEvents();

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
    }
    public void initEvents(){
        change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PayOrderActivity.this,SubmitOrderActivity.class);
                intent1.putExtra("order",order);
                startActivity(intent1);
                finish();
            }
        });

        admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginActivity.USER.getBalance() >= order.getMoney()){
                    pay(order);
                    //上传服务器
                }else{
                    Toast.makeText(PayOrderActivity.this,"你的余额不足，请先进行充值",Toast.LENGTH_LONG).show();
                }
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
    //上传服务器，实现支付
    private void pay(Order order){
        progressDialog = new ProgressDialog(PayOrderActivity.this);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("orderId", order.getId()+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "payOrder";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("url:",compeletedURL);
            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(PayOrderActivity.this,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(PayOrderActivity.this,"连接失败", Toast.LENGTH_SHORT).show();
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


}
