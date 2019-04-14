package com.example.a14574.expresshelp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import fragment.MyOrderFragment;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener{
    //private List<Order> orderList = new ArrayList<>();
    private Toolbar toolbar;
    private RadioButton radioButton;
    private int state;
    private MyOrderFragment fragment = new MyOrderFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initViews();
        showItem();


       // MyOrderFragment fragment = new MyOrderFragment();
        replaceFragment(fragment);
        RadioButton radioButton2 = (RadioButton)findViewById(R.id.orders);
        RadioButton radioButton3 = (RadioButton)findViewById(R.id.pending_payment);
        RadioButton radioButton4 = (RadioButton)findViewById(R.id.pending_order);
        RadioButton radioButton5 = (RadioButton)findViewById(R.id.pending_receive);
        RadioButton radioButton6 = (RadioButton)findViewById(R.id.accomplished);
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        radioButton6.setOnClickListener(this);

    }


    void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }



    @Override
    public void onClick(View view) {
       // MyOrderFragment fragment = new MyOrderFragment();
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
    }



    public void replaceFragment(MyOrderFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment.setState(state);
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
                ;break;
            case 2:
                radioButton = (RadioButton)findViewById(R.id.pending_payment);
                ;break;
            case 3:
                radioButton = (RadioButton)findViewById(R.id.pending_order);
                ;break;
            case 4:
                radioButton = (RadioButton)findViewById(R.id.pending_receive);
                ;break;
            case 5:
                radioButton = (RadioButton)findViewById(R.id.accomplished);
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

}
