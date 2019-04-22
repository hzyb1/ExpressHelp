package Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a14574.expresshelp.R;

import java.util.List;

import model.ChatUser;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<ChatUser> mChatUserList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView message;
        public ViewHolder(View view){
            super (view);
            id = (TextView)view.findViewById(R.id.chat_list_id);
            message = (TextView)view.findViewById(R.id.chat_list_message);
        }
    }
    public ChatListAdapter(List<ChatUser> ChatUserList){
        mChatUserList = ChatUserList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d("日志","测试");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        ChatUser chatUser = mChatUserList.get(i);
        holder.id.setText(chatUser.getUsername());
        holder.message.setText(chatUser.getMessage());
        Log.d("日志","测试");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
