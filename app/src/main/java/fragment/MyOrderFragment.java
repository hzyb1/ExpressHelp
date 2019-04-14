package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.OrderAdapter;
import Adapter.OrderBriefAdapter;
import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.Response;
import util.SpaceItemDecoration;

/**
 * Created by Administrator on 2019/3/28 0028.
 */

public class MyOrderFragment extends Fragment {
    private List<Order> orderList = new ArrayList<>();
    private List<Order> needorderList = new ArrayList<>();
    private ImageView nothing_image;
    private TextView nothing_title;
    private RecyclerView recyclerView;
    private int state;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("日志","来没来啊");
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            Gson gson = new Gson();
            Log.d("日志",result+"222");
            orderList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            OrderBriefAdapter adapter = new OrderBriefAdapter(orderList);
            recyclerView.setAdapter(adapter);
            Log.d("日志",orderList.size()+"   333 ");
        }
    };





    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders,container,false);
        init();
        find();
        recyclerView = (RecyclerView)view.findViewById(R.id.orders_recycler);
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
                needorderList.clear();
                needorderList = orderList;
                break;
            case 2:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){

                    if (orderList.get(i).getState() == 0){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 3:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 1){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 4:
                needorderList.clear();
                for(int i = 0;i<orderList.size();i++){
                    if (orderList.get(i).getState()== 3 || orderList.get(i).getState()== 2){
                        needorderList.add(orderList.get(i));
                    }
                }
                break;
            case 5:
                needorderList.clear();
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


        HashMap<String, String> params = new HashMap<String, String>();
        if(LoginActivity.USER == null){
            Log.d("日志","USER为空tt");
            return ;
        }else{
            Log.d("日志",LoginActivity.USER.getPassword());
        }
        params.put("id", LoginActivity.USER.getId()+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) +  "selectMyOrder";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("日志",compeletedURL);

            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(getActivity(),"未能连接到网络", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        return ;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
