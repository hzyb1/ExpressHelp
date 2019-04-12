package com.example.a14574.expresshelp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import model.Order;

public class SubmitOrderActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("日志","跳转成功");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        initEvents();
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
                Order order=submitOrder();
                if(order==null){
                    return;
                }
                Intent intent = new Intent(SubmitOrderActivity.this,PayOrderActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
                finish();
            }
        });
    }
    private Order submitOrder(){
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
        Time fst=null;Time fet=null;Time sst=null;Time set=null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try{
            fst = new Time(simpleDateFormat.parse(firstStartTimeS).getTime());
            fet = new Time(simpleDateFormat.parse(firstEndTimeS).getTime());
            sst = new Time(simpleDateFormat.parse(secondStartTimeS).getTime());
            set = new Time(simpleDateFormat.parse(secondEndTimeS).getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(fst.after(fet)){
            Toast.makeText(this,"时间设定存在冲突！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        if(sst.after(set)){
            Toast.makeText(this,"时间设定存在冲突！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        if(fet.after(sst)){
            Toast.makeText(this,"时间设定存在冲突！！！",Toast.LENGTH_LONG).show();
            return null;
        }
        Timestamp submitTime = new Timestamp(System.currentTimeMillis());
        Order order = new Order();
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
        }, 0, 0, true).show();
    }
}
