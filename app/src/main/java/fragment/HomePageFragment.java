package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a14574.expresshelp.ChatActivity;
import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.expresshelp.SubmitOrderActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private SwipeRefreshLayout swipeRefresh;
    private TextView search;           //搜索按钮
    private EditText searchKey;        //搜索内容
    private Gson gson;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            orderBriefList = gson.fromJson(result, new TypeToken<List<Order>>(){}.getType());
            OrderBriefAdapter adapter = new OrderBriefAdapter(orderBriefList);
            recyclerView.setAdapter(adapter);
            if(swipeRefresh.isRefreshing()){
                swipeRefresh.setRefreshing(false);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.order_brief_recycler);
        search = (TextView) view.findViewById(R.id.tv_search_conform);
        searchKey = (EditText) view.findViewById(R.id.et_search_key);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.selector_color);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
        submitOrder = (Button) view.findViewById(R.id.submit_order);
        initEvents();   //事件初始化代码
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
                initOrders();
            }
        });
        search = (TextView) view.findViewById(R.id.tv_search_conform);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private void refresh(){
        swipeRefresh.setRefreshing(true);
        initOrders();
    }

    private void initAdapter(){
        swipeRefresh.setRefreshing(true);
        initOrders();
        OrderBriefAdapter adapter = new OrderBriefAdapter(orderBriefList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
    }



    private void initOrders(){

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
            String originAddress = this.getString(R.string.TheServer) +  "selectMainOrder";
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

    private void initEvents(){   //事件处理初始化
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SubmitOrderActivity.class);
                startActivity(intent);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeRefresh.setRefreshing(false);
                refresh();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索事件
                doSearch();
            }
        });

    }

    private void doSearch(){    //搜索方法
        Toast.makeText(getActivity(),"执行搜索方法",Toast.LENGTH_SHORT).show();
    }
}
