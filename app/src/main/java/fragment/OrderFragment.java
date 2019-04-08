package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a14574.expresshelp.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.OrderAdapter;
import model.Order;
import util.SpaceItemDecoration;

/**
 * Created by Administrator on 2019/3/28 0028.
 */

public class OrderFragment extends Fragment {
    private List<Order> orderList = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);
        init();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.orders_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        OrderAdapter adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void init(){
        for(int i=0;i<20;i++){
            Order order = new Order();
            order.setGetAddress("21-103");
            order.setExpressName("中通快递");
            order.setTakeCode("3");
        //    order.setTakeTime("2018-11-7 10:20");
            orderList.add(order);
        }
    }
}
