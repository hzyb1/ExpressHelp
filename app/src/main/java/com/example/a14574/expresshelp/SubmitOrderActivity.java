package com.example.a14574.expresshelp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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

import java.sql.Timestamp;

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

            }
        });
    }
    private void submitOrder(){
        String expressNameS = expressName.getText().toString().trim();
        String getAddressS = getAddress.getText().toString().trim();
        String takeNameS = takeName.getText().toString().trim();
        String takeTelephoneS = takeTelephone.getText().toString().trim();
        String takeCodeS = takeCode.getText().toString().trim();
        String moneyS = money.getText().toString().trim();
        if(expressNameS.equals("") || getAddressS.equals("") || takeTelephoneS.equals("")
                || takeCodeS.equals("") || moneyS.equals("") || takeNameS.equals("")){
            Toast.makeText(this,"不能填空项哦！！！",Toast.LENGTH_LONG).show();
        }
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
                if (SubmitOrderActivity.this.minute < 10){
                    textView.setText(hour+":"+"0"+SubmitOrderActivity.this.minute);
                }else {
                    textView.setText(hour+":"+SubmitOrderActivity.this.minute);
                }
            }
        }, 0, 0, true).show();
    }
}
