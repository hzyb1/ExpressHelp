package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a14574.expresshelp.ChatActivity;
import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;

import java.util.List;

import model.ChatUser;
import model.Conversation;
import model.ConversationVo;
import model.User;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<ConversationVo> mConversationVo;
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
    public ChatListAdapter(List<ConversationVo> List){
        mConversationVo = List;
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
        ConversationVo conversation = mConversationVo.get(i);
        User user = LoginActivity.USER;
        holder.id.setText(conversation.getName());
        holder.message.setText(conversation.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return mConversationVo.size();
    }
}
