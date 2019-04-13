package com.example.a14574.expresshelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import model.Order;

public class ModifyOrderActivity extends AppCompatActivity {

    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_order);
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
    }
}
