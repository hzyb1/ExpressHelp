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

import com.example.a14574.expresshelp.PayOrderActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.expresshelp.SubmitOrderActivity;

import java.util.List;

import model.Order;

public class RunnerOrderAdapter extends RecyclerView.Adapter<RunnerOrderAdapter.ViewHolder> {
    private List<Order> mOrderList;
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.runner_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        holder.handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //上传服务器
            }
        });
        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                holder.state.setText("待取货");
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
}

