package Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.a14574.expresshelp.R;

import java.util.List;

import model.Order;


public class OrderBriefAdapter extends RecyclerView.Adapter<OrderBriefAdapter.ViewHolder>{

    private List<Order> mOrderList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView address;
        TextView expressName;
        TextView time;
        TextView money;

        public ViewHolder(View view){
            super(view);
            address = (TextView) view.findViewById(R.id.order_brief_address);
            expressName = (TextView) view.findViewById(R.id.order_brief_express);
            time = (TextView) view.findViewById(R.id.order_brief_time);
            money = (TextView) view.findViewById(R.id.order_brief_money);
        }

    }

    public OrderBriefAdapter(List<Order> mOrderList) {
        this.mOrderList = mOrderList;
        Log.d("日志",mOrderList.size()+"???");
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_brief_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.address.setText(order.getGetAdress());
        holder.money.setText(""+order.getMoney());
        holder.time.setText(order.getTakeTime());
        holder.expressName.setText(order.getExpressName());
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
