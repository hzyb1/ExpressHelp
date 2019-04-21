package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import Adapter.OrderBriefAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import http.HttpUtil;
import model.Order;
import model.User;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderDetailsActivity extends AppCompatActivity {
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
    private LinearLayout runnerInfo;    //有跑手接单时显示
    private CircleImageView runnerImage;       //跑手头像
    private TextView runnerName;            //跑手id
    private Button readMore;               //查看跑手详情
    private TextView ordeId;               //订单id
    private TextView submitTime;           //订单创建时间
    private TextView money;                //订单价格
    private Button modifyOrder;           //修改订单
    private Button deleteOrder;           //删除订单
    private User runner;

    private SimpleDateFormat dateFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat02 = new SimpleDateFormat("HH:mm");//时分格式

    private ProgressDialog progressDialog;                   //等待对话框

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if (OrderDetailsActivity.this.getString(R.string.HTTPERROR).equals(result)){

            }else{
                if(result == null || result.equals("")){

                }
                runner = new Gson().fromJson(result, User.class);          //将服务器返回的用户信息转化为user类的对象
                String url = OrderDetailsActivity.this.getString(R.string.TheServer)+"headImages/"+ runner.getHeadImage();
                Glide.with(OrderDetailsActivity.this).load(url).into(runnerImage);
                runnerName.setText(runner.getUsername());
                Log.d("runneraaa",runner.getId()+"");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
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
        runnerInfo = (LinearLayout) findViewById(R.id.runner_info);
        runnerImage = (CircleImageView) findViewById(R.id.runner_image);
        runnerName = (TextView) findViewById(R.id.runner_name);
        readMore = (Button) findViewById(R.id.read_more);
        ordeId = (TextView) findViewById(R.id.order_id);
        submitTime = (TextView) findViewById(R.id.submit_time);
        money = (TextView) findViewById(R.id.money);
        modifyOrder = (Button) findViewById(R.id.modify_order);
        deleteOrder = (Button) findViewById(R.id.delete_order);

        accomplishedOrders.setVisibility(View.GONE);
        runnerInfo.setVisibility(View.GONE);

        order = (Order) getIntent().getSerializableExtra("order");
        if(order!=null){
            initOrderView();
        }
    }
    private void initOrderView(){
        switch (order.getState()){
            case 0:
                orderStatus.setText(getResources().getString(R.string.pending_payment_status));
                Glide.with(this).load(R.drawable.pending_payment_status).into(statusImage);
                break;
            case 1:
                orderStatus.setText(getResources().getString(R.string.pending_receive_status));
                Glide.with(this).load(R.drawable.pending_receive_status).into(statusImage);
                break;
            case 2:
            case 3:
                orderStatus.setText(getResources().getString(R.string.pending_take_status));
                Glide.with(this).load(R.drawable.pending_take_status).into(statusImage);
                runnerInfoInit();
                break;
            case 4:
                orderStatus.setText(getResources().getString(R.string.accomplish_order_status));
                Glide.with(this).load(R.drawable.accomplish_order_status).into(statusImage);
                runnerInfoInit();
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
    }

    private void runnerInfoInit(){  //初始化跑手信息布局
        runnerInfo.setVisibility(View.VISIBLE);
        selectRunner(order.getAcceptId());

    }
    private void accomplishedOrdersInit(){   //初始化完成信息布局
        accomplishedOrders.setVisibility(View.VISIBLE);
        finishTime.setText(dateFormat01.format(order.getFinishTime()));

    }
    private void initEvents(){
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看跑手更多信息界面
            }
        });
        modifyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderDetailsActivity.this,SubmitOrderActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);

                //修改订单
            }
        });
        deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除订单
                deleteOrder();


            }
        });
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到用户详情界面
                Intent intent = new Intent(OrderDetailsActivity.this,SpecificUserInfoActivity.class);
                intent.putExtra("user",runner);
                startActivity(intent);
            }
        });
    }

    private void selectRunner(int id){
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
                    Toast.makeText(OrderDetailsActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
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


    private void deleteOrder(){
        progressDialog = new ProgressDialog(OrderDetailsActivity.this);
        progressDialog.setTitle("正在注册，请稍后......");
        progressDialog.setMessage("注册中......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", order.getId()+"");

        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) +  "deleteOrder";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("url:",compeletedURL);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();

            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(OrderDetailsActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        return ;
                    }
                    String result = null;
                    result = response.body().string().trim();
                    Looper.prepare();
                    if(OrderDetailsActivity.this.getString(R.string.HTTPSUCCESS).equals(result)){
                        Toast.makeText(OrderDetailsActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderDetailsActivity.this,MyOrderActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }else{
                        Toast.makeText(OrderDetailsActivity.this,"删除失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
