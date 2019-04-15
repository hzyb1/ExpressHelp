package com.example.a14574.expresshelp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.OrderAdapter;
import fragment.MyOrderFragment;
import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.Response;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener{
    //private List<Order> orderList = new ArrayList<>();
    private Toolbar toolbar;
    private RadioButton radioButton;
    private int state;
    private List<Order> orderList = new ArrayList<>();
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private  RadioButton radioButton4;
    private RadioButton radioButton5 ;
    private RadioButton radioButton6;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("日志","来没来啊");
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            Gson gson = new Gson();
            Log.d("日志",result+"222");
            orderList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            Log.d("日志",orderList.size()+"   333 ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        //init();
        initViews();
        showItem();



    }


    private void initViews(){
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
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        radioButton6.setOnClickListener(this);
    }





    @Override
    public void onClick(View view) {

        MyOrderFragment fragment = new MyOrderFragment();
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
        replaceFragment(fragment);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
               // progressDialog.dismiss();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });*/
    }



    public void replaceFragment(MyOrderFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment.setState(state);
        //fragment.setOrderList(orderList);
        transaction.replace(R.id.order_fragment,fragment);
        transaction.commit();
    }
    public void showItem(){
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void init(){
//
//
//        HashMap<String, String> params = new HashMap<String, String>();
//        if(LoginActivity.USER == null){
//            Log.d("日志","USER为空tt");
//            return ;
//        }else{
//            Log.d("日志",LoginActivity.USER.getPassword());
//        }
//        params.put("id", LoginActivity.USER.getId()+"");
//        try {
//            //构造完整URL
//            String originAddress = this.getString(R.string.TheServer) +  "selectMyOrder";
//            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
//            Log.d("日志",compeletedURL);
//
//            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Looper.prepare();
//                    Toast.makeText(MyOrderActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if(!response.isSuccessful()){
//                        return ;
//                    }
//                    Message message = new Message();
//                    message.obj = response.body().string().trim();
//                    mHandler.sendMessage(message);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

}
