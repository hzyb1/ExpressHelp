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
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.OrderAdapter;
import Adapter.RunnerOrderAdapter;
import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.Response;
import util.SpaceItemDecoration;

public class RunnerActivity extends AppCompatActivity implements  View.OnClickListener{
    private Toolbar toolbar;
    private List<Order> orderList = new ArrayList<>();      //所有我的订单列表
    private List<Order> needorderList = new ArrayList<>();
    private RadioButton radioButton1;
    private RadioButton radioButton2 ;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RecyclerView recyclerView;
    private ImageView nothing_image;      //need订单列表为空的时候显示的图片
    private TextView nothing_title;     //need订单列表为空的时候显示的文字
    private int state;

    private ProgressDialog progressDialog;     //等待状态对话框
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            if(msg.arg1 == 1){      //第二次访问

            }else if(msg.arg1 == 2){        //第一次访问
                result = msg.obj.toString();
                Gson gson = new Gson();
                orderList = new ArrayList<>();
                orderList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            }
            find();     //查找想要显示的订单列表
            RunnerOrderAdapter adapter = new RunnerOrderAdapter(needorderList);     //适配器
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
        setContentView(R.layout.activity_runner);
        initView();
        initOrders();
    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.runner_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RunnerActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        OrderAdapter adapter = new OrderAdapter(needorderList);     //适配器
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        radioButton1 = (RadioButton)findViewById(R.id.wait_pick);
        radioButton2 = (RadioButton)findViewById(R.id.wait_give);
        radioButton3 = (RadioButton)findViewById(R.id.runner_finish);
        radioButton4 = (RadioButton)findViewById(R.id.renner_all);
        showItem();
        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
    }

    private void showItem() {
        Intent intent = getIntent();
        int style = intent.getIntExtra("style", 0);
        state = style;
        switch (style) {
            case 1:
                radioButton1.setChecked(true);
                ;
                break;
            case 2:
                radioButton2.setChecked(true);
                ;
                break;
            case 3:
                radioButton3.setChecked(true);
                ;
                break;
            case 4:
                radioButton4.setChecked(true);
                ;
                break;
            default:
                break;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wait_pick:
                state = 1;
                break;
            case R.id.wait_give:
                state = 2;
                break;
            case R.id.runner_finish:
                state = 3;
                break;
            case R.id.renner_all:
                state = 4;
                break;
        }
    }
    private void find(){
        switch (state){
            case 1:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){

                    if (orderList.get(i).getState() == 2){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 2:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){

                    if (orderList.get(i).getState() == 3){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 3:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 4){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 4:
                needorderList.clear();
                needorderList.addAll(orderList);
                break;
        }
    }
    public void test(){
        Order order = new Order();
        order.setMoney(1.2f);
        order.setGetAddress("123");
        order.setExpressName("321");
        order.setState(2);
        orderList.add(order);
        finish();
    }

    private void initOrders(){
        progressDialog = new ProgressDialog(RunnerActivity.this);
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
            String originAddress = this.getString(R.string.TheServer) +  "selectRunnerOrder";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("日志",compeletedURL);
            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(RunnerActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
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

}
