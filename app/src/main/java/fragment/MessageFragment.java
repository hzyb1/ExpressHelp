package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a14574.expresshelp.HomeActivity;
import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.practice.ChatService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.ChatListAdapter;
import model.ChatRecord;
import http.HttpUtil;
import model.ConversationVo;
import model.Order;
import okhttp3.Call;
import okhttp3.Response;
import util.SpaceItemDecoration;

public class MessageFragment extends Fragment {
    static private RecyclerView recyclerView;
    static private ChatListAdapter adapter;
    static private List<ConversationVo> conversationList = new ArrayList<>();
    private ChatListReceiver chatListReceiver;
    private int unReadMessage = 0;
    private SwipeRefreshLayout refresh;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            if (msg == null || msg.obj == null) return;
            result = msg.obj.toString();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
            conversationList = gson.fromJson(result, new TypeToken<List<ConversationVo>>() {
            }.getType());
            adapter = new ChatListAdapter(conversationList, getContext());
            unReadMessage = 0;
            for (int i = 0; i < conversationList.size(); i++) {
                if (LoginActivity.USER.getId() == conversationList.get(i).getUserId1()) {
                    unReadMessage += conversationList.get(i).getUser1UnRead();
                } else {
                    unReadMessage += conversationList.get(i).getUser2UnRead();
                }
                HomeActivity.badgeView.setBadgeCount(unReadMessage);
            }
            adapter = new ChatListAdapter(conversationList, getContext());
            //Toast.makeText(getActivity(),"最后的对象"+chatUserList.get(chatUserList.size()-1).getMessage(),Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(adapter);
            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            }
        }
    };
    private Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Log.d("测试","han");
            unReadMessage = 0;
            for (int i = 0; i < conversationList.size(); i++) {
                if (LoginActivity.USER.getId() == conversationList.get(i).getUserId1()) {
                    unReadMessage += conversationList.get(i).getUser1UnRead();
                } else {
                    unReadMessage += conversationList.get(i).getUser2UnRead();
                }
            }
//            unReadMessage = unReadMessage + 1;
            HomeActivity.badgeView.setBadgeCount(unReadMessage);
            adapter = new ChatListAdapter(conversationList, getContext());
            recyclerView.setAdapter(adapter);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
//        Intent startIntent = new Intent(container.getContext(),ChatService.class);
//        container.getContext().startService(startIntent);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(conversationList, getContext());
        recyclerView.setAdapter(adapter);
        chatListReceiver = new ChatListReceiver();
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                init();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeRefresh.setRefreshing(false);
                refresh.setRefreshing(true);
                init();
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("日志","我不信???");
        if (hidden) {
            Log.d("日志","啥情况???");
            //不在最前端界面显示，相当于调用了 onPause()
        } else {
            Log.d("日志","调用了吗");
            init();
            //重新显示到最前端,相当于调用了 onResume()
            // 进行网络数据刷新此处执行必须要在 Fragment与 Activity绑定了即需要添加判断是否完成绑定，否则将会报空（即非第一个显示出来的 fragment，虽然 onCreateView没有被调用,
            // 但是 onHiddenChanged也会被调用，所以如果你尝试去获取活动的话，注意防止出现空指针）}}
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("日志","什么时候调用????????");
        init();
    }

    public void update(ChatRecord record){
        for (int i = 0;i<conversationList.size()-1;i++){
            if (record.getConversationId() == conversationList.get(i).getId()){
                conversationList.get(i).setLastMessage(record.getMessage());
                conversationList.get(i).setLastTime(record.getSendTime());
                if(record.getGeterId() == conversationList.get(i).getUserId1()){
                    conversationList.get(i).setUser1UnRead(conversationList.get(i).getUser1UnRead() + 1);
                }else{
                    conversationList.get(i).setUser2UnRead(conversationList.get(i).getUser2UnRead() + 1);
                }
                conversationList.add(0,conversationList.remove(i));
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                hand.sendMessage(message);
            }
        }).start();
    }


    public void init(){
        HashMap<String, String> params = new HashMap<String, String>();
        if(LoginActivity.USER == null){
            Log.d("日志","USER为空tt");
            return ;
        }else{

        }
        params.put("id", LoginActivity.USER.getId()+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) +  "selectConversation";
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
                    handler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class ChatListReceiver extends BroadcastReceiver{
        public ChatListReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ChatRecord record = (ChatRecord) intent.getSerializableExtra("record");
            if (record.getGeterId() == LoginActivity.USER.getId()){
                //    update(record);
                Log.d("测试","发来了信息"+record);
                // init();
                update(record);
            }
        }
    }
}
