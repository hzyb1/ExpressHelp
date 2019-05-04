package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.a14574.expresshelp.R;
import com.example.a14574.practice.ChatService;

import java.util.ArrayList;
import java.util.List;

import Adapter.ChatListAdapter;


public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ChatUser>chatUserList = new ArrayList<>();
    private ChatService.ChatBinder chatBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        init();
        recyclerView = (RecyclerView)view.findViewById(R.id.chat_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ChatListAdapter adapter = new ChatListAdapter(chatUserList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void init(){
        for (int i = 0; i<10;i++) {
            ChatUser user = new ChatUser();
            user.setMessage("测试信息");
            user.setUsername("测试人员");
            chatUserList.add(user);
        }
    }
}
