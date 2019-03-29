package com.example.a14574.expresshelp;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.OrderAdapter;
import fragment.OrderFragment;
import model.Order;

public class MyOrderActivity extends AppCompatActivity {
    private List<Order> orderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        FragmentManager fragmentManager = getSupportFragmentManager();
       FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.order_fragment,new OrderFragment());
        transaction.commit();
    }
}
