package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a14574.expresshelp.ChatActivity;
import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.ConversationVo;
import model.User;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<ConversationVo> mConversationVo;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView message;
        private LinearLayout chat;
        private CircleImageView conversationHeadImage;
        private ConversationVo conversationVo;
        private LinearLayout newMessage;
        private TextView messageNum;
        public ViewHolder(View view){
            super (view);
            id = (TextView)view.findViewById(R.id.chat_list_id);
            message = (TextView)view.findViewById(R.id.chat_list_message);
            chat = (LinearLayout)view.findViewById(R.id.chat);
            conversationHeadImage = (CircleImageView)view.findViewById(R.id.conversationHeadImage);
            newMessage = (LinearLayout) view.findViewById(R.id.new_message);
            messageNum = (TextView) view.findViewById(R.id.message_num);
        }
    }
    public ChatListAdapter(List<ConversationVo> List,Context context){
        this.context = context;
        mConversationVo = List;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        final Context context = viewGroup.getContext();
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id1",holder.conversationVo.getUserId1());
                intent.putExtra("id2",holder.conversationVo.getUserId2());
                intent.putExtra("name",holder.conversationVo.getName());
                Drawable drawable = holder.conversationHeadImage.getDrawable();
                holder.conversationHeadImage.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(holder.conversationHeadImage.getDrawingCache());
                holder.conversationHeadImage.setDrawingCacheEnabled(false);
                intent.putExtra("photo",bitmap);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        ConversationVo conversation = mConversationVo.get(i);
        holder.conversationVo = conversation;
        User user = LoginActivity.USER;
        holder.id.setText(conversation.getName());
        holder.message.setText(conversation.getLastMessage());
        String url = context.getString(R.string.TheServer)+"headImages/"+ conversation.getPhoto();
        Log.d("userimage",LoginActivity.USER.getHeadImage());
        Glide.with(context).load(url).into(holder.conversationHeadImage);
    }

    @Override
    public int getItemCount() {
        return mConversationVo.size();
    }
}
