package com.example.a14574.expresshelp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapter.OrderAdapter;
import model.Order;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);
        initView();

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
}
