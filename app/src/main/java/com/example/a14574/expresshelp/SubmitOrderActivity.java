package com.example.a14574.expresshelp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import Adapter.OrderBriefAdapter;
import model.Order;

import http.HttpUtil;
import model.Order;
import model.User;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubmitOrderActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView firstStartTime;
    private TextView firstEndTime;
    private TextView secondStartTime;
    private TextView secondEndTime;
    private int hour,minute;
    private Button submitOrder;
    private EditText expressName;
    private EditText getAddress;
    private EditText takeName;
    private EditText takeTelephone;
    private EditText takeCode;
    private EditText money;
    private Timestamp submitTime;
    private Order orderModify;      //   从修改控件传来的Order
    private TextView toolBarTitle;
    private Order order;
    private ProgressDialog progressDialog;                   //上传状态对话框

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();

            if(msg.arg1 == 1){      //表示提交订单
                if("0".equals(result)){
                    Toast.makeText(SubmitOrderActivity.this,"上传失败！！！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SubmitOrderActivity.this,"上传成功！！！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SubmitOrderActivity.this,PayOrderActivity.class);
                    order.setId(Integer.parseInt(result));
                    intent.putExtra("order",order);
                    startActivity(intent);
                    finish();
                }
            }else{      //表示修改订单
                if(SubmitOrderActivity.this.getString(R.string.HTTPERROR).equals(result)){
                    Toast.makeText(SubmitOrderActivity.this,"上传失败！！！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SubmitOrderActivity.this,"上传成功！！！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SubmitOrderActivity.this,MyOrderActivity.class);
                    intent.putExtra("style",1);     //修改完成，去我的订单界面看看
                    startActivity(intent);
                    finish();
                }
            }
            progressDialog.dismiss();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("日志","跳转成功");
        super.onCreate(savedInstanceState);
        initViews();
        initEvents();
    }
    //初始化修改订单情况下控件显示内容
    private void initModifyOrder(){
        expressName.setText(orderModify.getExpressName());
        getAddress.setText(orderModify.getGetAddress());
        takeName.setText(orderModify.getTakeName());
        takeTelephone.setText(orderModify.getTakeTelephone());
        takeCode.setText(orderModify.getTakeCode());
        money.setText(orderModify.getMoney()+"");
        firstStartTime.setText(new SimpleDateFormat("HH:mm").format(orderModify.getFirstTakeTimeBegin()));
        firstEndTime.setText(new SimpleDateFormat("HH:mm").format(orderModify.getFirstTakeTimeEnd()));
        secondStartTime.setText(new SimpleDateFormat("HH:mm").format(orderModify.getSecondTakeTimeBegin()));
        secondEndTime.setText(new SimpleDateFormat("HH:mm").format(orderModify.getSecondTakeTimeEnd()));
    }

    private void initViews(){
        setContentView(R.layout.activity_submit_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        submitOrder = (Button) findViewById(R.id.submit_order_button);
        expressName = (EditText) findViewById(R.id.express_name);
        getAddress = (EditText) findViewById(R.id.get_address);
        takeName = (EditText) findViewById(R.id.take_name);
        takeTelephone = (EditText) findViewById(R.id.take_telephone);
        takeCode = (EditText) findViewById(R.id.take_code);
        money = (EditText) findViewById(R.id.money);

        firstStartTime = (TextView) findViewById(R.id.first_start_time);
        firstEndTime = (TextView) findViewById(R.id.first_end_time);
        secondStartTime = (TextView) findViewById(R.id.second_start_time);
        secondEndTime = (TextView) findViewById(R.id.second_end_time);
        orderModify = (Order)getIntent().getSerializableExtra("order");
        if(orderModify!=null){
            toolBarTitle.setText("修改订单");
            submitOrder.setText("修改订单");
            initModifyOrder();
        }
    }
    private void initEvents(){
        firstStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(firstStartTime);
            }
        });
        firstEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(firstEndTime);
            }
        });
        secondStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(secondStartTime);
            }
        });
        secondEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(secondEndTime);
            }
        });
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order=createOrder();
                if(order==null){
                    return;
                }
                if(orderModify!=null){  //进入修改事件
                    orderModify.setExpressName(order.getExpressName());
                    orderModify.setGetAddress(order.getGetAddress());
                    orderModify.setTakeName(order.getTakeName());
                    orderModify.setTakeTelephone(order.getTakeTelephone());
                    orderModify.setTakeCode(order.getTakeCode());
                    orderModify.setMoney(order.getMoney());
                    orderModify.setFirstTakeTimeBegin(order.getFirstTakeTimeBegin());
                    orderModify.setFirstTakeTimeEnd(order.getFirstTakeTimeEnd());
                    orderModify.setSecondTakeTimeBegin(order.getSecondTakeTimeBegin());
                    orderModify.setSecondTakeTimeEnd(order.getSecondTakeTimeEnd());
                    updataOrder(orderModify);
                }else{
                    order=createOrder();
                    submitOrder(order);     //上传服务器
                }
            }
        });
    }
    //生成订单对象
    private Order createOrder(){
        String expressNameS = expressName.getText().toString().trim();
        String getAddressS = getAddress.getText().toString().trim();
        String takeNameS = takeName.getText().toString().trim();
        String takeTelephoneS = takeTelephone.getText().toString().trim();
        String takeCodeS = takeCode.getText().toString().trim();
        String moneyS = money.getText().toString().trim();
        String firstStartTimeS = firstStartTime.getText().toString().trim();
        String firstEndTimeS = firstEndTime.getText().toString().trim();
        String secondStartTimeS = secondStartTime.getText().toString().trim();
        String secondEndTimeS = secondEndTime.getText().toString().trim();
        if(expressNameS.equals("") || getAddressS.equals("") || takeTelephoneS.equals("")
                || takeCodeS.equals("") || moneyS.equals("") || takeNameS.equals("")){
            Toast.makeText(this,"不能填空项哦！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        float moneyF=0;
        try {
            moneyF = Float.parseFloat(moneyS);
        }catch (Exception e){
            Toast.makeText(this,"金额需为数字哦！！！",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
        if(firstStartTimeS.equals("") || firstEndTimeS.equals("")|| secondStartTimeS.equals("")|| secondEndTimeS.equals("")){
            Toast.makeText(this,"请注意设置时间哦！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        Timestamp fst=null;Timestamp fet=null;Timestamp sst=null;Timestamp set=null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try{
            Log.d("日志",simpleDateFormat.format(simpleDateFormat.parse(firstStartTimeS) ) );

           fst = new Timestamp(simpleDateFormat.parse(firstStartTimeS).getTime());
            Log.d("日志",fst.toString());
            fet = new Timestamp(simpleDateFormat.parse(firstEndTimeS).getTime());
            sst = new Timestamp(simpleDateFormat.parse(secondStartTimeS).getTime());
            set = new Timestamp(simpleDateFormat.parse(secondEndTimeS).getTime());
            Log.d("显示时间",fst+"");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(fet.before(fst)){
            Toast.makeText(this,"时间设定存在冲突！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        if(set.before(sst)){
            Toast.makeText(this,"时间设定存在冲突！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        if(sst.before(fet)){
            Toast.makeText(this,"时间设定存在冲突！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        submitTime = new Timestamp(System.currentTimeMillis());
        Order order = new Order();
        if(LoginActivity.USER != null){
            Log.d("日志",LoginActivity.USER.getId()+" ");
        }else{
            Log.d("日志","user是空的");
            return null;
        }
        order.setSendId(LoginActivity.USER.getId());
        order.setExpressName(expressNameS);
        order.setGetAddress(getAddressS);
        order.setTakeName(takeNameS);
        order.setTakeTelephone(takeTelephoneS);
        order.setTakeCode(takeCodeS);
        order.setMoney(moneyF);

        order.setFirstTakeTimeBegin(fst);
        order.setFirstTakeTimeEnd(fet);
        order.setSecondTakeTimeBegin(sst);
        order.setSecondTakeTimeEnd(set);
        order.setState(0);
        order.setSubmitTime(submitTime);
        return order;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showTimePickerDialog(final TextView textView){
        new TimePickerDialog(SubmitOrderActivity.this,AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                SubmitOrderActivity.this.minute = minute;
                String hourS="";String minuteS="";
                if(hour<10){
                    hourS = "0"+hour;
                }else{
                    hourS = hour+"";
                }
                if(minute<10){
                    minuteS = "0"+SubmitOrderActivity.this.minute;
                }else {
                    minuteS = ""+SubmitOrderActivity.this.minute;
                }
                if (SubmitOrderActivity.this.minute < 10){
                    textView.setText(hourS+":"+minuteS);
                }else {
                    textView.setText(hourS+":"+minuteS);
                }
            }
        }, hour, minute, true).show();
    }
    //访问服务器提交订单
    private void submitOrder(Order order){
        progressDialog = new ProgressDialog(SubmitOrderActivity.this);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "submitOrder";

            Log.d("url:",originAddress);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();

            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(order));

            Log.d("日志:",gson.toJson(order));

            HttpUtil.sendPostOkHttpRequest(originAddress,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(SubmitOrderActivity.this,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(SubmitOrderActivity.this,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    message.arg1 = 1;
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //访问服务器更新订单
    private void updataOrder(Order order){
        progressDialog = new ProgressDialog(SubmitOrderActivity.this);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "updataOrder";
            Log.d("url:",originAddress);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();

            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(order));

            Log.d("日志:",gson.toJson(order));

            HttpUtil.sendPostOkHttpRequest(originAddress,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(SubmitOrderActivity.this,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(SubmitOrderActivity.this,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    message.arg1 = 2;
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

