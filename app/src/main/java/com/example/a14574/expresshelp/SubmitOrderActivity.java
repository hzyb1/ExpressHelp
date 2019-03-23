package com.example.a14574.expresshelp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SubmitOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner startHour;
    private Spinner startMinute;
    private Spinner stopHour;
    private Spinner stopMinute;

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
        startHour = (Spinner) findViewById(R.id.start_hour);
        startMinute = (Spinner) findViewById(R.id.start_minute);
        stopHour = (Spinner) findViewById(R.id.stop_hour);
        stopMinute = (Spinner) findViewById(R.id.stop_minute);
        initSpinner(startHour,startMinute);
        initSpinner(stopHour,stopMinute);
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
    private void initSpinner(Spinner hour,Spinner minute){
        String hours[] = new String[24];
        String minutes[] = new String[60];
        for(int i=0;i<24;i++){
            if(i<10){
                hours[i]="0"+i;
            }else{
                hours[i]=i+"";
            }
        }
        for(int i=0;i<60;i++){
            if(i<10){
                minutes[i]="0"+i;
            }else{
                minutes[i]=i+"";
            }
        }
        ArrayAdapter<String> adapterHour = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,hours);
        hour.setAdapter(adapterHour);
        ArrayAdapter<String> adapteMinute = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,minutes);
        minute.setAdapter(adapteMinute);

    }
}
