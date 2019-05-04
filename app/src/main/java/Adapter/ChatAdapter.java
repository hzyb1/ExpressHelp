package Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;

import java.util.List;

import model.ChatRecord;

public class ChatAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private List<ChatRecord> mChatRecord;
    private final int ITEM_TYPE_SEND = 1;
    private final int ITEM_TYPE_ACCEPT = 2;
    class AcceptViewHolder extends RecyclerView.ViewHolder{
    private TextView message;
    private ChatRecord chatRecord;
        public  AcceptViewHolder(View view){
            super(view);
            message = (TextView)view.findViewById(R.id.chat_accept_message);
        }
    }

    class SendViewHolder extends RecyclerView.ViewHolder{
        private TextView message;
        private ChatRecord chatRecord;
        public  SendViewHolder(View view){
            super(view);
            message = (TextView)view.findViewById(R.id.chat_send_message);
        }
    }
    public ChatAdapter (List<ChatRecord> list){
        mChatRecord = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
        Log.d("日志","switch的类型"+i);
        switch (i){
            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_send_item,viewGroup,false);
                return new ChatAdapter.SendViewHolder(view);
            case 2:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_accept_item,viewGroup,false);
                return new ChatAdapter.AcceptViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        ChatRecord record = mChatRecord.get(i);
        if (holder instanceof SendViewHolder){
            Log.d("日志","发送方");
            ((SendViewHolder) holder).message.setText(record.getMessage());
        }else if (holder instanceof AcceptViewHolder){
            Log.d("日志","接收方");
            ((AcceptViewHolder) holder).message.setText(record.getMessage());
        }
    }



    @Override
    public int getItemCount() {
        return mChatRecord.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d("日志","mChatRecord.get(position).getSenderId()"+mChatRecord.get(position).getSenderId());
        if (mChatRecord.get(position).getSenderId() == LoginActivity.USER.getId()){
            return ITEM_TYPE_SEND;
        }else{
            return ITEM_TYPE_ACCEPT;
        }
    }
}
