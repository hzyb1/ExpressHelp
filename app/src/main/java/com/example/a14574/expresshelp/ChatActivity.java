package com.example.a14574.expresshelp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Adapter.ChatAdapter;
import Adapter.ChatListAdapter;
import model.ChatRecord;

public class ChatActivity extends AppCompatActivity {
    private EditText content;
    private ImageView send;
    private RecyclerView recyclerView;
    private List<ChatRecord> chatRecords = new ArrayList<>();
    private Bitmap bitmap;
    private ChatListReceiver chatListReceiver;
    private TextView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bitmap = getIntent().getParcelableExtra("photo");
        initView();
        init();
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    send.setImageResource(R.drawable.send);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final int id1 = getIntent().getIntExtra("id1",0);
        final int id2 = getIntent().getIntExtra("id2",0);
        recyclerView = (RecyclerView)findViewById(R.id.rv_chat_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ChatAdapter adapter = new ChatAdapter(chatRecords,bitmap);
        recyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatRecord record = new ChatRecord();
                record.setSenderId(LoginActivity.USER.getId());
                if (LoginActivity.USER.getId() == id1){
                    record.setGeterId(id2);
                }else{
                    record.setGeterId(id1);
                }
                record.setMessage(content.getText().toString());
                record.setSendTime(new Timestamp(System.currentTimeMillis()));
            }
        });
        chatListReceiver = new ChatListReceiver();
    }
    private void initView(){
        content = (EditText)findViewById(R.id.et_content);
        send = (ImageView)findViewById(R.id.ivAdd);
        logo = (TextView)findViewById(R.id.chat_logo);
        String name = getIntent().getStringExtra("name");
        logo.setText(name);
    }
    public void init(){
        for (int i = 0;i<2;i++) {
            ChatRecord record = new ChatRecord();
            record.setSenderId(10010);
            record.setMessage("测试聊天内容");
            chatRecords.add(record);

            ChatRecord record1 = new ChatRecord();
            record.setSenderId(10086);
            record.setMessage("测试聊天内容");
            chatRecords.add(record1);
        }
    }

    public class ChatListReceiver extends BroadcastReceiver {
        public ChatListReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ChatRecord record = (ChatRecord) intent.getSerializableExtra("record");
            Log.d("日志","在聊天界面收到了广播");

        }
    }
}
