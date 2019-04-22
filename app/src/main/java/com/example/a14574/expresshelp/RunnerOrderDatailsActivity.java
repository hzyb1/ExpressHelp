package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import http.HttpUtil;
import model.Order;
import model.User;
import okhttp3.Call;
import okhttp3.Response;

public class RunnerOrderDatailsActivity extends AppCompatActivity {
    private Order order;        //订单
    private TextView orderStatus;   //订单状态显示
    private ImageView statusImage;  //订单状态图片显示
    private LinearLayout accomplishedOrders;  //完成订单时显示
    private TextView finishTime;     //订单完成交易的时间
    private TextView takeName;       //收货人姓名
    private TextView takeTelephone; //收货人电话
    private TextView takeGetAddress; //收货人地址
    private TextView takeCode;        //取货码
    private TextView expressName;     //快递名
    private TextView firstTakeTime;   //第一个取货时间段
    private TextView secondTakeTime;   //第二个取货时间段
    private CircleImageView userImage;       //用户头像
    private TextView userName;            //用户名
    private Button readMore;               //查看用户详情
    private TextView ordeId;               //订单id
    private TextView submitTime;           //订单创建时间
    private TextView money;                //订单价格
    private User user;
    private Toolbar toolbar;

    private SimpleDateFormat dateFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat02 = new SimpleDateFormat("HH:mm");//时分格式

    private ProgressDialog progressDialog;                   //等待对话框

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if (RunnerOrderDatailsActivity.this.getString(R.string.HTTPERROR).equals(result)){

            }else{
                if(result == null || result.equals("")){

                }
                user = new Gson().fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
                String url = RunnerOrderDatailsActivity.this.getString(R.string.TheServer)+"headImages/"+ user.getHeadImage();
                Glide.with(RunnerOrderDatailsActivity.this).load(url).into(userImage);
                userName.setText(user.getUsername());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner_order_datails);
        initView();
        initEvents();
    }

    private void initView(){
        orderStatus = (TextView) findViewById(R.id.order_status);
        statusImage = (ImageView) findViewById(R.id.status_image);
        accomplishedOrders = (LinearLayout) findViewById(R.id.accomplished_orders);
        finishTime = (TextView) findViewById(R.id.finish_time);
        takeName = (TextView) findViewById(R.id.take_name);
        takeTelephone = (TextView) findViewById(R.id.take_telephone);
        takeGetAddress = (TextView) findViewById(R.id.take_get_address);
        takeCode = (TextView) findViewById(R.id.take_code);
        expressName = (TextView) findViewById(R.id.express_name);
        firstTakeTime = (TextView) findViewById(R.id.first_take_time);
        secondTakeTime = (TextView) findViewById(R.id.second_take_time);
        userImage = (CircleImageView) findViewById(R.id.user_image);
        userName = (TextView) findViewById(R.id.user_name);
        readMore = (Button) findViewById(R.id.read_more);
        ordeId = (TextView) findViewById(R.id.order_id);
        submitTime = (TextView) findViewById(R.id.submit_time);
        money = (TextView) findViewById(R.id.money);

        accomplishedOrders.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        order = (Order) getIntent().getSerializableExtra("order");
        if(order!=null){
            initOrderView();
        }
    }
    private void initOrderView(){
        switch (order.getState()){
            case 2:
                orderStatus.setText(getResources().getString(R.string.delivering_status));
                Glide.with(this).load(R.drawable.delivering).into(statusImage);
                break;
            case 3:
                orderStatus.setText(getResources().getString(R.string.arrived_status));
                Glide.with(this).load(R.drawable.arrived).into(statusImage);
                break;
            case 4:
                orderStatus.setText(getResources().getString(R.string.accomplish_order_status));
                Glide.with(this).load(R.drawable.accomplish_order_status).into(statusImage);
                accomplishedOrdersInit();
                break;
        }
        takeName.setText(order.getTakeName());
        takeTelephone.setText(order.getTakeTelephone());
        takeGetAddress.setText(order.getGetAddress());
        takeCode.setText(order.getTakeCode());
        expressName.setText(order.getExpressName());
        firstTakeTime.setText(dateFormat02.format(order.getFirstTakeTimeBegin())+" - "+dateFormat02.format(order.getFirstTakeTimeEnd()));
        secondTakeTime.setText(dateFormat02.format(order.getSecondTakeTimeBegin())+" - "+dateFormat02.format(order.getSecondTakeTimeEnd()));
        ordeId.setText(order.getId()+"");
        submitTime.setText(dateFormat01.format(order.getSubmitTime()));
        money.setText("￥"+order.getMoney());
        selectUser(order.getSendId());
    }

    private void accomplishedOrdersInit(){   //初始化完成信息布局
        accomplishedOrders.setVisibility(View.VISIBLE);
        finishTime.setText(dateFormat01.format(order.getFinishTime()));

    }
    private void initEvents(){
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到用户详情界面
                Intent intent = new Intent(RunnerOrderDatailsActivity.this,SpecificUserInfoActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }

    private void selectUser(int id){
        User runner= new User();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", id+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "selectUserById";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("URL:",compeletedURL);

            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(RunnerOrderDatailsActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //访问异常，返回值不是200
                    if(!response.isSuccessful()){
                        return;
                    }else{
                        Message message = new Message();
                        message.obj = response.body().string().trim();
                        mHandler.sendMessage(message);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
}
