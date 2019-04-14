package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.expresshelp.SubmitOrderActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.OrderBriefAdapter;
import http.HttpCallbackListener;
import http.HttpUtil;
import model.Order;
import okhttp3.Call;
import okhttp3.Response;
import util.SpaceItemDecoration;


public class HomePageFragment extends Fragment {
    private List<Order> orderBriefList = new ArrayList<Order>();
    private Button submitOrder;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("日志","来没来啊");
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            Gson gson = new Gson();
            Log.d("日志",result+"222");
            orderBriefList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            OrderBriefAdapter adapter = new OrderBriefAdapter(orderBriefList);
            recyclerView.setAdapter(adapter);
            Log.d("日志",orderBriefList.size()+"   333 ");
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initOrders();
        View view = inflater.inflate(R.layout.fragment_home_page,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.order_brief_recycler);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        Log.d("日志","   在哪啊 ");
       OrderBriefAdapter adapter = new OrderBriefAdapter(orderBriefList);
       recyclerView.setAdapter(adapter);
        submitOrder = (Button) view.findViewById(R.id.submit_order);
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("日志","相应点击");
                Intent intent = new Intent(getActivity(),SubmitOrderActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }



    private void initOrders(){
//        for(int i=0;i<20;i++){
//            Order order = new Order();
//            order.setGetAddress("21-103");
//            order.setExpressName("中通快递");
//            order.setTakeCode("3");
//            Date date = new Date();//获得系统时间. 
//            String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//            Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换 
//            order.setSubmitTime(goodsC_date);
//            Time time = new Time(10,20,1);
////            order.setFirstTakeTimeBegin(time);
////            order.setFirstTakeTimeEnd(time);
////            order.setSecondTakeTimeBegin(time);
////            order.setSecondTakeTimeEnd(time);
//            orderBriefList.add(order);
//        }

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
            String originAddress = this.getString(R.string.VirtualTheServer) +  "selectMainOrder";
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
                String result ;
                Gson gson = new Gson();
                result = message.obj.toString();
                orderBriefList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());

            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }




//        try {
//            //构造完整URL
//            Log.d("url:",originAddress);
//            //发送请求
//            HttpUtil.sendHttpRequest(originAddress, new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    Message message = new Message();
//                    message.obj = response;
//                    mHandler.sendMessage(message);
//                    Log.d("信息是什么",message.toString());
////                    String result = "";
////                    Gson gson = new Gson();
////                    result = message.obj.toString();
////                    orderBriefList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
////                    flag = true;
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Message message = new Message();
//                    message.obj = e.toString();
//                    mHandler.sendMessage(message);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
