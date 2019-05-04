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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private  ChatListAdapter adapter;
    private List<ConversationVo> conversationList = new ArrayList<>();
    private ChatService.ChatBinder chatBinder;
    private ChatListReceiver chatListReceiver;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
            conversationList = gson.fromJson(result, new TypeToken<List<ConversationVo>>(){}.getType());


            adapter = new ChatListAdapter(conversationList,getContext());
            //Toast.makeText(getActivity(),"最后的对象"+chatUserList.get(chatUserList.size()-1).getMessage(),Toast.LENGTH_SHORT).show()
            recyclerView.setAdapter(adapter);

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        Intent startIntent = new Intent(container.getContext(),ChatService.class);
        container.getContext().startService(startIntent);
        recyclerView = (RecyclerView)view.findViewById(R.id.chat_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(conversationList,getContext());
        recyclerView.setAdapter(adapter);

        chatListReceiver = new ChatListReceiver();



        return view;
    }

    public void update(ChatRecord record){

       Log.d("日志","update方法");
        Log.d("日志","update方法"+conversationList.size());
       for (int i = 0;i<conversationList.size()-1;i++){
           if (record.getConversationId() == conversationList.get(i).getId()){
               conversationList.get(i).setLastMessage(record.getMessage());
               conversationList.add(0,conversationList.remove(i));
           }
       }
       new Thread(new Runnable() {
           @Override
           public void run() {
               Log.d("日志","run方法");
               Message message = new Message();
               handler.sendMessage(message);
           }
       }).start();
    }


    public void init(){
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
                update(record);
            }
        }
    }
}
