package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.a14574.expresshelp.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.ChatListAdapter;
import model.ChatUser;


public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ChatUser>chatUserList = new ArrayList<>();
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
        Log.d("日志","是否来到这个界面了"+adapter);
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
