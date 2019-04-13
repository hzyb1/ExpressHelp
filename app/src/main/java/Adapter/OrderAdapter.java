package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a14574.expresshelp.R;

import java.util.List;

import model.Order;

/**
 * Created by Administrator on 2019/3/29 0029.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> mOrderList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView state;
        private TextView address;
        private TextView express;
        private TextView money;
        private Button handle;
        public ViewHolder(View view){
            super (view);
            state = (TextView)view.findViewById(R.id.order_state);
            address = (TextView)view.findViewById(R.id.order_address);
            express = (TextView)view.findViewById(R.id.order_express);
            money = (TextView)view.findViewById(R.id.order_money);
            handle = (Button)view.findViewById(R.id.handle);
        }
    }
    public OrderAdapter(List<Order> orderList){
        mOrderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.address.setText(order.getGetAddress());
        holder.express.setText(order.getExpressName());
        holder.money.setText(Float.toString(order.getMoney()));
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
}
