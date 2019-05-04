package Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
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
import com.example.a14574.expresshelp.RunnerActivity;
import com.example.a14574.expresshelp.RunnerOrderDatailsActivity;
import com.example.a14574.expresshelp.SubmitOrderActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RunnerOrderAdapter extends RecyclerView.Adapter<RunnerOrderAdapter.ViewHolder> {
    private List<Order> mOrderList;
    private ProgressDialog progressDialog;
    private  Context context = null;
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView state;
        private TextView address;
        private TextView express;
        private TextView money;
        private Button handle;
        private Button read_more;
        private Order need_order;
        public ViewHolder(View view){
            super (view);
            state = (TextView)view.findViewById(R.id.runner_state);
            address = (TextView)view.findViewById(R.id.runner_address);
            express = (TextView)view.findViewById(R.id.runner_express);
            money = (TextView)view.findViewById(R.id.runner_money);
            handle = (Button)view.findViewById(R.id.runner_handle);
            read_more = (Button)view.findViewById(R.id.runner_read_more);
        }
    }
    public RunnerOrderAdapter(List<Order> orderList){
        mOrderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.runner_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        context = viewGroup.getContext();
            holder.handle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.handle.getText().toString().equals("已送达")) {
                        // 上传服务服务器内容

                        updataOrder(holder.need_order);


                        //通知用户确认收货
                        Log.d("日志","已送达的监听事件");
                    }

                }
            });
        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toOrderInfoActivity(viewGroup.getContext(),holder.need_order);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.need_order = order;
        holder.address.setText(order.getGetAddress());
        holder.express.setText(order.getExpressName());
        holder.money.setText(Float.toString(order.getMoney()));
        switch (order.getState()) {
            case 2:
                holder.state.setText("送货中");
                holder.handle.setText("已送达");
                break;
            case 3:
                holder.state.setText("已送达");
                holder.handle.setVisibility(View.GONE);
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
        Intent intent = new Intent(context,RunnerOrderDatailsActivity.class);
        intent.putExtra("order",order);
        context.startActivity(intent);
    }

    private void updataOrder(Order order){
        order.setState(3);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            //构造完整URL
            String originAddress = context.getString(R.string.TheServer) + "updataOrder";
            Log.d("url:",originAddress);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();

            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(order));

            Log.d("日志:",gson.toJson(order)+"???");

            HttpUtil.sendPostOkHttpRequest(originAddress,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(context,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
                    String result = null;
                    result = response.body().string().trim();
                    Looper.prepare();
                    if(context.getString(R.string.HTTPSUCCESS).equals(result)){
                        Log.d("日志","111?");
                        Toast.makeText(context,"确认成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,RunnerActivity.class);
                        context.startActivity(intent);
                        progressDialog.dismiss();
                        //        finish();
                    }else{
                        Log.d("日志","222");
                        Toast.makeText(context,"确认失败", Toast.LENGTH_SHORT).show();
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

