package Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a14574.expresshelp.R;

import java.util.List;

import model.ChatRecord;

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.ViewHolder>{
    private List<ChatRecord> mChatRecord;
    static class ViewHolder extends RecyclerView.ViewHolder{
    private TextView accept;
        public ViewHolder(View view){
            super(view);
            accept = (TextView)view.findViewById(R.id.chat_accept);
        }
    }

    public ChatAdapter (List<ChatRecord> list){
        mChatRecord = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_accept_item,viewGroup,false);
        ChatAdapter.ViewHolder holder = new ChatAdapter.ViewHolder(view);
        Log.d("测试","测试2");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        ChatRecord record = mChatRecord.get(i);
        holder.accept.setText(record.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChatRecord.size();
    }
}
