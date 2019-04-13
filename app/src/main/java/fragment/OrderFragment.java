package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private List<Order> needorderList = new ArrayList<>();
    private ImageView nothing_image;
    private TextView nothing_title;
    private int state;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);
        init();
        find();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.orders_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        OrderAdapter adapter = new OrderAdapter(needorderList);
        recyclerView.setAdapter(adapter);
        if (needorderList.isEmpty()){
            nothing_image = (ImageView)view.findViewById(R.id.nothing_image);
            nothing_title = (TextView)view.findViewById(R.id.nothing_title);
            nothing_title.setVisibility(view.VISIBLE);
            nothing_image.setVisibility(view.VISIBLE);
        }
        return view;
    }

    private void find(){
        switch (state){
            case 1:
                needorderList = orderList;
                break;
            case 2:
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 0){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 3:
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 1){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 4:
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 3 || orderList.get(i).getState()== 2){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 5:
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 4){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;

        }
    }
    public void init(){
        Order order = new Order();
        order.setState(3);
        order.setMoney(2.1f);
        order.setExpressName("百事");
        order.setGetAddress("21栋");
        orderList.add(order);

        /*Order order2 = new Order();
        order.setState(1);
        order.setMoney(2.1f);
        order.setExpressName("tiantian");
        order.setGetAddress("22栋");
        orderList.add(order2);

        Order order3 = new Order();
        order.setState(3);
        order.setMoney(2.1f);
        order.setExpressName("shunfeng");
        order.setGetAddress("23栋");
        orderList.add(order3);

        Order order4 = new Order();
        order.setState(4);
        order.setMoney(2.1f);
        order.setExpressName("louxia");
        order.setGetAddress("24栋");
        orderList.add(order4);*/
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
