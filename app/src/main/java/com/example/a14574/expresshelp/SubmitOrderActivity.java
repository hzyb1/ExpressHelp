package com.example.a14574.expresshelp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.Looper;

import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.ArrayList;
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
import view.WheelView;

public class SubmitOrderActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView firstStartTime;
    private TextView firstEndTime;
    private TextView secondStartTime;
    private TextView secondEndTime;
    private Button submitOrder;
    private TextView expressName;
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
    private SimpleDateFormat dateFormatHm = new SimpleDateFormat("HH:mm");//时分格式
    private Timestamp flagTime = new Timestamp(System.currentTimeMillis());  //进入发单页面的时间

    private List<String> expressList;

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
        expressName = (TextView) findViewById(R.id.express_name);
        getAddress = (EditText) findViewById(R.id.get_address);
        takeName = (EditText) findViewById(R.id.take_name);
        takeTelephone = (EditText) findViewById(R.id.take_telephone);
        takeCode = (EditText) findViewById(R.id.take_code);
        money = (EditText) findViewById(R.id.money);

        firstStartTime = (TextView) findViewById(R.id.first_start_time);
        firstEndTime = (TextView) findViewById(R.id.first_end_time);
        secondStartTime = (TextView) findViewById(R.id.second_start_time);
        secondEndTime = (TextView) findViewById(R.id.second_end_time);
        firstStartTime.setText(dateFormatHm.format(flagTime));
        firstEndTime.setText(dateFormatHm.format(flagTime));
        secondStartTime.setText(dateFormatHm.format(flagTime));
        secondEndTime.setText(dateFormatHm.format(flagTime));
        orderModify = (Order)getIntent().getSerializableExtra("order");
        if(orderModify!=null){
            toolBarTitle.setText("修改订单");
            submitOrder.setText("修改订单");
            initModifyOrder();
        }else{
            initSubmitOrder();
        }
        expressList = new ArrayList<>();
        String expressStringList[]=getResources().getStringArray(R.array.express_name_list);
        for(int i=0;i<expressStringList.length;i++){
            expressList.add(expressStringList[i]);
        }
    }
    private void initEvents(){
        firstStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String split[]=splitTime(firstStartTime.getText().toString().trim());
                showTimePickerDialog(firstStartTime,Integer.parseInt(split[0]),Integer.parseInt(split[1]));
            }
        });
        firstEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String split[]=splitTime(firstEndTime.getText().toString().trim());
                showTimePickerDialog(firstEndTime,Integer.parseInt(split[0]),Integer.parseInt(split[1]));
            }
        });
        secondStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String split[]=splitTime(secondStartTime.getText().toString().trim());
                showTimePickerDialog(secondStartTime,Integer.parseInt(split[0]),Integer.parseInt(split[1]));
            }
        });
        secondEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String split[]=splitTime(secondEndTime.getText().toString().trim());
                showTimePickerDialog(secondEndTime,Integer.parseInt(split[0]),Integer.parseInt(split[1]));
            }
        });
        expressName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getExpressPosition();
                if(position==-1){
                    position=2;
                }
                View outerView = LayoutInflater.from(SubmitOrderActivity.this).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(expressList);
                wv.setSeletion(position);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        //Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        expressName.setText(item);
                    }
                });
                new AlertDialog.Builder(SubmitOrderActivity.this)
                        .setTitle("选择快递名")
                        .setView(outerView)
                        .setPositiveButton("确认", null)
                        .show();
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
        /*if(!takeTelephoneS.matches("[1][3578]\\d{9}")){
            Toast.makeText(this,"电话号码格式不对哦！！！",Toast.LENGTH_LONG).show();
            return null;
        }*/
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
        submitTime = new Timestamp(System.currentTimeMillis());
        if(!checkTime(fst,fet,sst,set,submitTime)){
            showTimeAlertDialog();
            return null;
        }
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
    private void showTimePickerDialog(final TextView textView,int hour,int minute){
        new TimePickerDialog(SubmitOrderActivity.this,AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hourTemp = hourOfDay;
                int minuteTemp = minute;
                String hourS="";String minuteS="";
                if(hourTemp<10){
                    hourS = "0"+hourTemp;
                }else{
                    hourS = hourTemp+"";
                }
                if(minuteTemp<10){
                    minuteS = "0"+minuteTemp;
                }else {
                    minuteS = ""+minuteTemp;
                }
                textView.setText(hourS+":"+minuteS);
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

    private boolean checkTime(Timestamp fst,Timestamp fet,Timestamp sst,Timestamp set,Timestamp submitTime){
        //校验时间是否合理
        if(!checkTimeGap(submitTime,fst,0)){
            //初始时间不得早于发单时间
            return false;
        }
        if(!(checkTimeGap(fst,fet,30) && checkTimeGap(fet,sst,30) && checkTimeGap(sst,set,30))){
            //时间间隔最短不得低于30分钟
            return false;
        }
        return true;
    }
    private boolean checkTimeGap(Timestamp a,Timestamp b,int gap){
        int aHour = a.getHours();
        int bHour = b.getHours();
        int aMinute = a.getMinutes();
        int bMinute = b.getMinutes();
        Log.d("timeTest",aHour+":"+aMinute+" "+bHour+":"+bMinute);
        if(aHour<bHour){
            if(bHour-aHour==1){
                if((bMinute+60-aMinute)<=gap){
                    return false;
                }
            }
        }else if(aHour == bHour){
            if(bMinute-aMinute<gap){
                return false;
            }
        }
        return true;
    }

    private void showTimeAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("时间设定存在冲突");
        dialog.setMessage("请检查是否具有以下冲突：\n  1.设定时间不得早于发单时间 \n  2.各个时间间隔不得短于30分钟");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return ;
            }
        });
        dialog.show();
    }

    private String[] splitTime(String time){
        String[] split;
        split=time.split(":");
        return split;
    }
    private int getExpressPosition(){
        String a = expressName.getText().toString().trim();
        for(int i=0;i<expressList.size();i++){
            if(a.equals(expressList.get(i))){
                return i;
            }
        }
        return -1;
    }

    private void initSubmitOrder(){
        String getAddress="";
        if(LoginActivity.USER.getBedroomBuild()!=0){
            getAddress+=(LoginActivity.USER.getBedroomBuild()+" -");
        }
        if(LoginActivity.USER.getBedroomNumber()!=0){
            getAddress+=(" "+LoginActivity.USER.getBedroomNumber());
        }
        this.getAddress.setText(getAddress);
        if(LoginActivity.USER.getTrueName()!=null && !LoginActivity.USER.getTrueName().trim().equals("")){
            this.takeName.setText(LoginActivity.USER.getTrueName());
        }
        if(LoginActivity.USER.getTelephone()!=null && !LoginActivity.USER.getTelephone().trim().equals("")){
            this.takeTelephone.setText(LoginActivity.USER.getTelephone());
        }
    }



}

