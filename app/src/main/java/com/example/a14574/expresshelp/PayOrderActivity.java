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

import com.example.a14574.practice.DeleteOrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;

import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.TimeUtil;

public class PayOrderActivity extends BaseActivity {
    private TextView address;
    private TextView receive;
    private TextView name;
    private TextView phone;
    private TextView number;
    private TextView money;
    private TextView first_time;
    private TextView second;
    private TextView validTime;       //计时
    private Button admit;       //支付按钮
    private Button change;      //修改订单按钮
    private Toolbar toolbar;
    private Order order;
    private ProgressDialog progressDialog;//上传状态对话框
    private boolean flag = false;
    private Gson gson;

    public Runnable timeRunable = new Runnable() {
        @Override
        public void run() {

            currentSecond = currentSecond - 1000;
            validTime.setText(PayOrderActivity.getFormatHMS(currentSecond));
            if (!isPause) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 1000);
            }
        }
    };
    //计时器
    public Handler mhandle = null;
    private static boolean isPause = false;//是否暂停
    public long currentSecond = 30*60*1000;//当前毫秒数
    private Thread timeThread=null;
    /*****************计时器*******************/

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
        setCurrentSecond();
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
        validTime = (TextView) findViewById(R.id.valid_time);
        mhandle = new Handler();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
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
                    flag = true;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("flag",flag);
        setResult(1,intent);
        Log.d("日志","zhixingle onbackpressed");
        finish();
    }
    public static String getFormatHMS(long time){
        time=time/1000;//总秒数
        int s= (int) (time%60);//秒
        int m= (int) (time/60);//分
        return String.format("%02d:%02d",m,s);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        mhandle.postDelayed(timeRunable,1000);
        super.onResume();
    }

    @Override
    protected void onStart() {
        mhandle.removeCallbacks(timeRunable);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        mhandle.removeCallbacks(timeRunable);
        super.onDestroy();
    }
    private void setCurrentSecond(){
        //currentSecond=DeleteOrderService.getTime(order);
        long diff = TimeUtil.compareTime(order);
        long maxStep = 30*60*1000;
        if(diff<=maxStep){
            currentSecond = maxStep-diff;
        }else{
            currentSecond = 0;
            Toast.makeText(this,"该任务已超出时限，请重新发单！！！",Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
}
