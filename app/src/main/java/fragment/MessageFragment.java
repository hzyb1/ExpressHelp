package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.a14574.expresshelp.R;
import com.example.a14574.practice.ChatService;

import java.util.ArrayList;
import java.util.List;

import Adapter.ChatListAdapter;
import model.ChatUser;
import model.ConversationVo;


public class MessageFragment extends Fragment {
    static private RecyclerView recyclerView;
    private  ChatListAdapter adapter;
    static private List<ConversationVo>list = new ArrayList<>();
    private ChatService.ChatBinder chatBinder;
    private ChatListReceiver chatListReceiver;
    private Handler handler = new Handler(){
        public void handleMessage(Message message){

                Log.d("日志", "线程启动了");
                adapter = new ChatListAdapter(list);
                //Toast.makeText(getActivity(),"最后的对象"+chatUserList.get(chatUserList.size()-1).getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("日志","最后的对象"+list.size());
                recyclerView.setAdapter(adapter);

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        init();

        Intent startIntent = new Intent(container.getContext(),ChatService.class);
        container.getContext().startService(startIntent);


        recyclerView = (RecyclerView)view.findViewById(R.id.chat_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(list);
        recyclerView.setAdapter(adapter);

        chatListReceiver = new ChatListReceiver();



        return view;
    }

    public void update(ChatUser user){

       Log.d("日志","update方法");
        Log.d("日志","update方法"+list.size());
       //list.add(user);
       list.add(0,list.remove(list.size()-1));
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
        for (int i = 0; i<7;i++) {
            ConversationVo conversation = new ConversationVo();
            conversation.setLastMessage("最后一条信息");
            conversation.setName("测试人员");
            list.add(conversation);
        }
    }
    public  class ChatListReceiver extends BroadcastReceiver{
        public ChatListReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ChatUser user = (ChatUser) intent.getSerializableExtra("user");
            Toast.makeText(context,user.getMessage(),Toast.LENGTH_SHORT).show();;
            update(user);
        }
    }
}
