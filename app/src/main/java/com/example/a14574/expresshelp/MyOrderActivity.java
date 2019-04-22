package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.OrderAdapter;
import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.Response;
import util.SpaceItemDecoration;

public class MyOrderActivity extends AppCompatActivity implements  View.OnClickListener{
    private Toolbar toolbar;
    private RadioButton radioButton;
    private int state;      //我想看的订单状态
    private int flag;
    private List<Order> orderList = new ArrayList<>();      //所有我的订单列表
    private List<Order> needorderList = new ArrayList<>();      //我需要显示的订单列表
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5 ;
    private RadioButton radioButton6;
    private ImageView nothing_image;      //need订单列表为空的时候显示的图片
    private TextView nothing_title;     //need订单列表为空的时候显示的文字
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ProgressDialog  progressDialog;     //等待状态对话框
    private Gson gson;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            if(msg.arg1 == 1){      //第二次访问

            }else if(msg.arg1 == 2){        //第一次访问
                result = msg.obj.toString();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
                orderList = new ArrayList<>();
                orderList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            }
            find();     //查找想要显示的订单列表
            OrderAdapter adapter  = new OrderAdapter(needorderList);     //适配器
            recyclerView.setAdapter(adapter);
            if (needorderList.isEmpty()){       //订单列表为空的时候显示图片和文字
                nothing_image = (ImageView)findViewById(R.id.nothing_image);
                nothing_title = (TextView)findViewById(R.id.nothing_title);
                nothing_title.setVisibility(View.VISIBLE);
                nothing_image.setVisibility(View.VISIBLE);
            }else{
                nothing_image = (ImageView)findViewById(R.id.nothing_image);
                nothing_title = (TextView)findViewById(R.id.nothing_title);
                nothing_title.setVisibility(View.GONE);
                nothing_image.setVisibility(View.GONE);
            }
            progressDialog.dismiss();       //结束等待
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initViews();    //初始化控件
        initOrders();   //访问服务器初始化订单

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.orders :
                state = 1;
                break;
            case R.id.pending_payment :
                state = 2;
                break;
            case R.id.pending_order :
                state = 3;
                break;
            case R.id.pending_receive :
                state = 4;
                break;
            case R.id.accomplished :
                state = 5;
                break;

        }
        progressDialog = new ProgressDialog(MyOrderActivity.this);
        progressDialog.setTitle("正在登录，请稍后......");
        progressDialog.setMessage("登录中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Message message = new Message();
        message.arg1 = 1;
        mHandler.sendMessage(message);
    }

    private void showItem(){
        Intent intent = getIntent();
        int style = intent.getIntExtra("style",0);
        state = style;
        switch (style){
            case 1:
                radioButton = (RadioButton)findViewById(R.id.orders);
                radioButton2.setOnClickListener(this);
                ;break;
            case 2:
                radioButton = (RadioButton)findViewById(R.id.pending_payment);
                radioButton3.setOnClickListener(this);
                ;break;
            case 3:
                radioButton = (RadioButton)findViewById(R.id.pending_order);
                radioButton4.setOnClickListener(this);
                ;break;
            case 4:
                radioButton = (RadioButton)findViewById(R.id.pending_receive);
                radioButton5.setOnClickListener(this);
                ;break;
            case 5:
                radioButton = (RadioButton)findViewById(R.id.accomplished);
                radioButton6.setOnClickListener(this);
                ;break;
            default:
                break;
        }
        radioButton.setChecked(true);
    }
    //访问服务器初始化订单列表
    private void initOrders(){
        progressDialog = new ProgressDialog(MyOrderActivity.this);
        progressDialog.setTitle("请稍后......");
        progressDialog.setMessage("正在加载......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();
        if(LoginActivity.USER == null){
            progressDialog.dismiss();
            return ;
        }else{
        }
        params.put("id", LoginActivity.USER.getId()+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) +  "selectMyOrder";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("日志",compeletedURL);
            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(MyOrderActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        return ;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //查找想要显示的订单列表
    private void find(){
        switch (state){
            case 1:
                needorderList.clear();
                needorderList.addAll(orderList);
                break;
            case 2:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){

                    if (orderList.get(i).getState() == 0){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 3:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 1){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 4:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 3 || orderList.get(i).getState()== 2){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 5:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 4){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
        }
    }
    //初始化控件
    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.orders_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyOrderActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        radioButton2 = (RadioButton)findViewById(R.id.orders);
        radioButton3 = (RadioButton)findViewById(R.id.pending_payment);
        radioButton4 = (RadioButton)findViewById(R.id.pending_order);
        radioButton5 = (RadioButton)findViewById(R.id.pending_receive);
        radioButton6 = (RadioButton)findViewById(R.id.accomplished);
        showItem();
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        radioButton6.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*switch (adapter.getFlag()){
            case 1:
                initOrders();
                radioButton4.setChecked(true);
                //initOrders();
                break;
            case 2:
                initOrders();
                break;
            case 3:
                //initOrders();
                radioButton6.setChecked(true);
                //initOrders();
                break;
            case 4:
                initOrders();
                break;
        }*/
        initOrders();
    }


}
