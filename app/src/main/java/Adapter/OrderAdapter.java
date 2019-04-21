package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a14574.expresshelp.OrderDetailsActivity;
import com.example.a14574.expresshelp.PayOrderActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.expresshelp.SubmitOrderActivity;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Order;

/**
 * Created by Administrator on 2019/3/29 0029.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> mOrderList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    public OrderAdapter(List<Order> orderList){
        mOrderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item,parent,false);
        final Context context = parent.getContext();
        final ViewHolder holder = new ViewHolder(view);
        holder.handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Order order = mOrderList.get(position);
                if (holder.handle.getText().toString().trim().equals("确认付款")){
                    Intent intent = new Intent(context, PayOrderActivity.class);
                    Log.d("日志","needorder  "+ holder.needorder.getExpressName());
                    intent.putExtra("order",holder.needorder);
                    //
                    context.startActivity(intent);
                }else if (holder.handle.getText().toString().trim().equals("修改订单")){

                    Intent intent = new Intent(context,SubmitOrderActivity.class);
                    Log.d("日志","needorder  "+ holder.needorder.getExpressName());
                    intent.putExtra("order",holder.needorder);
                    Log.d("修改事件",holder.needorder.getSecondTakeTimeBegin().toString());
                    context.startActivity(intent);
                    //
                }else if (holder.handle.getText().toString().trim().equals("确认收货")){
                    //
                }

            }
        });
        holder.readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("日志","hdshgudshgudshgkjghs");
                toOrderInfoActivity(context,holder.needorder);
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
}
