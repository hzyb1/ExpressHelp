package com.example.a14574.expresshelp;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import Adapter.OrderAdapter;
import fragment.OrderFragment;
import model.Order;

public class MyOrderActivity extends AppCompatActivity {
    private List<Order> orderList = new ArrayList<>();
    private Toolbar toolbar;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showItem();
        replaceFragment(new OrderFragment());

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

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_fragment,fragment);
        transaction.commit();
    }
    public void showItem(){
        Intent intent = getIntent();
        int style = intent.getIntExtra("style",0);
        Log.d("MyOrderActivity", "showitem: "+style);
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
}
