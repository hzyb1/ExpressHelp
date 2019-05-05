package Adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fragment.HomePageFragment;
import fragment.MyInfoFragment;
import model.ChatRecord;

public class ChatAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    private List<ChatRecord> mChatRecord;
    private final int ITEM_TYPE_SEND = 1;
    private final int ITEM_TYPE_ACCEPT = 2;
    private Bitmap bitmap;
    class AcceptViewHolder extends RecyclerView.ViewHolder{
    private TextView message;
    private TextView time;
    private ChatRecord chatRecord;
    private CircleImageView image;
        public  AcceptViewHolder(View view){
            super(view);
            message = (TextView)view.findViewById(R.id.chat_accept_message);
            image = (CircleImageView)view.findViewById(R.id.chat_accept_photo);
            time = (TextView)view.findViewById(R.id.accept_time);
        }
    }

    class SendViewHolder extends RecyclerView.ViewHolder{
        private TextView message;
        private ChatRecord chatRecord;
        private TextView time;
        private CircleImageView image;
        public  SendViewHolder(View view){
            super(view);
            message = (TextView)view.findViewById(R.id.chat_send_message);
            time = (TextView)view.findViewById(R.id.send_time);
            image = (CircleImageView)view.findViewById(R.id.chat_send_photo);
        }
    }
    public ChatAdapter (List<ChatRecord> list ,Bitmap bitmap){
        mChatRecord = list;
        this.bitmap = bitmap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            ((SendViewHolder) holder).time.setText(sdf.format(record.getSendTime()));
            CircleImageView headImage = HomePageFragment.headImage;
            headImage.setDrawingCacheEnabled(true);
            Bitmap sendbitmap = Bitmap.createBitmap(headImage.getDrawingCache());
            headImage.setDrawingCacheEnabled(false);
            ((SendViewHolder) holder).image.setImageBitmap(sendbitmap);
            Log.d("测试","bitmap"+LoginActivity.photo);
        }else if (holder instanceof AcceptViewHolder){
            Log.d("日志","接收方");
            ((AcceptViewHolder) holder).message.setText(record.getMessage());
            ((AcceptViewHolder) holder).image.setImageBitmap(bitmap);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            ((AcceptViewHolder) holder).time.setText(sdf.format(record.getSendTime()));
        }
    }



    @Override
    public int getItemCount() {
        return mChatRecord.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatRecord.get(position).getSenderId() == LoginActivity.USER.getId()){
            return ITEM_TYPE_SEND;
        }else{
            return ITEM_TYPE_ACCEPT;
        }
    }
}
