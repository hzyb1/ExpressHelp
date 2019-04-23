package Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a14574.expresshelp.MyOrderActivity;
import com.example.a14574.expresshelp.OrderDetailsActivity;
import com.example.a14574.expresshelp.PayOrderActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.expresshelp.SubmitOrderActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/3/29 0029.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> mOrderList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ProgressDialog progressDialog;                   //等待对话框
    private  Context context = null;
    private int flag;
    private MyOrderActivity activity;
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView state;
        private TextView address;
        private TextView express;
        private TextView money;
        private Button handle;
        private Button readmore;  // 查看详情按钮
        private Order needorder;
        private TextView time;


        public ViewHolder(View view){
            super (view);
            state = (TextView)view.findViewById(R.id.order_state);
            address = (TextView)view.findViewById(R.id.order_address);
            express = (TextView)view.findViewById(R.id.order_express);
            money = (TextView)view.findViewById(R.id.order_money);
            handle = (Button)view.findViewById(R.id.handle);
            time = (TextView) view.findViewById(R.id.order_brief_time);
            readmore = (Button) view.findViewById(R.id.read_more);
        }
    }
    public OrderAdapter(List<Order> orderList,MyOrderActivity activity){

        mOrderList = orderList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item,parent,false);
        context = parent.getContext();
        final ViewHolder holder = new ViewHolder(view);
        holder.handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.handle.getText().toString().trim().equals("确认付款")){
                    Intent intent = new Intent(context, PayOrderActivity.class);
                    intent.putExtra("order",holder.needorder);
                    context.startActivity(intent);
                    flag =1;

                }else if (holder.handle.getText().toString().trim().equals("修改订单")){

                    Intent intent = new Intent(context,SubmitOrderActivity.class);
                    Log.d("日志","needorder  "+ holder.needorder.getExpressName());
                    intent.putExtra("order",holder.needorder);
                    Log.d("修改事件",holder.needorder.getSecondTakeTimeBegin().toString());
                    context.startActivity(intent);
                    flag = 2;
                    //
                }else if (holder.handle.getText().toString().trim().equals("确认收货")){
                    completeOrder(holder.needorder.getId());
                    activity.reflsh();


                }

            }
        });
        holder.readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("日志","hdshgudshgudshgkjghs");
                toOrderInfoActivity(context,holder.needorder);
                flag =4;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.needorder = order;
        holder.address.setText(order.getGetAddress());
        holder.express.setText(order.getExpressName());
        holder.money.setText(Float.toString(order.getMoney()));
        holder.time.setText(simpleDateFormat.format(order.getSubmitTime()));
        switch (order.getState()){
            case 0:
                holder.state.setText("未付款");
                holder.handle.setText("确认付款");
                break;
            case 1:
                holder.state.setText("待接单");
                holder.handle.setText("修改订单");
                break;
            case 2:
            case 3:
                holder.state.setText("待收货");
                holder.handle.setText("确认收货");
                break;
            case 4:
                holder.state.setText("已完成");
                holder.handle.setVisibility(View.GONE);
                break;
                default:
                    break;
        }
    }


    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    private void toOrderInfoActivity(Context context,Order order){
        Intent intent = new Intent(context,OrderDetailsActivity.class);
        intent.putExtra("order",order);
        context.startActivity(intent);
    }

    private void completeOrder(int id ){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id+"");

        try {
            //构造完整URL
            String originAddress = context.getString(R.string.TheServer) +  "completeOrder";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("url:",compeletedURL);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();

            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context,"未能连接到网络", Toast.LENGTH_SHORT).show();
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
                    if(context.getString(R.string.HTTPSUCCESS).equals(result)){
                        Toast.makeText(context,"确认成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,MyOrderActivity.class);
                        context.startActivity(intent);
                        progressDialog.dismiss();
                //        finish();
                    }else{
                        Toast.makeText(context,"确认失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        flag = 1;
    }

    public int getFlag() {
        return flag;
    }

}
