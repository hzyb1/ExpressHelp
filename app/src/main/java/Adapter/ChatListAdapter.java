package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a14574.expresshelp.ChatActivity;
import com.example.a14574.expresshelp.R;

import java.util.List;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<ChatUser> mChatUserList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView message;
        private LinearLayout chat;
        public ViewHolder(View view){
            super (view);
            id = (TextView)view.findViewById(R.id.chat_list_id);
            message = (TextView)view.findViewById(R.id.chat_list_message);
            chat = (LinearLayout)view.findViewById(R.id.chat);
        }
    }
    public ChatListAdapter(List<ChatUser> ChatUserList){
        mChatUserList = ChatUserList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        final Context context = viewGroup.getContext();
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        ChatUser chatUser = mChatUserList.get(i);
       holder.id.setText(chatUser.getUsername());
      holder.message.setText(chatUser.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChatUserList.size();
    }
}
