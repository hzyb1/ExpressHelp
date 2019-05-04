package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import Adapter.ChatAdapter;
import http.HttpUtil;
import model.ChatRecord;
import okhttp3.Call;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private EditText content;
    private ImageView send;
    private RecyclerView recyclerView;
    private List<ChatRecord> chatRecords = new ArrayList<>();
    private Bitmap bitmap;
    private ProgressDialog progressDialog;     //等待状态对话框
    private int id1,id2;
    private int conversationId;
    private ChatListReceiver chatListReceiver;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if(msg.arg1 == 1){      //初始化record；
                if(result.length() >= 5){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
                    chatRecords = gson.fromJson(result, new TypeToken<List<ChatRecord>>(){}.getType());
                    Log.d("日志","大小"+chatRecords.size());
                    conversationId = chatRecords.get(0).getConversationId();
                    ChatAdapter adapter = new ChatAdapter(chatRecords,bitmap);

                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(chatRecords.size()-1);

                }else{
                    conversationId = Integer.parseInt(result);
                }
                progressDialog.dismiss();       //结束等待
            }else if(msg.arg1 == 2){
                ChatRecord chatRecord = (ChatRecord) msg.obj;
                chatRecords.add(chatRecord);
                ChatAdapter adapter = new ChatAdapter(chatRecords,bitmap);
                if(recyclerView == null){
                    Log.d("日志","啥玩意");
      //              recyclerView = (RecyclerView)findViewById(R.id.rv_chat_list);
       //             LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        //            recyclerView.setLayoutManager(layoutManager);
                }else{
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(chatRecords.size()-1);
                }
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bitmap = getIntent().getParcelableExtra("photo");
        initView();
        id1 = getIntent().getIntExtra("id1",0);
        id2 = getIntent().getIntExtra("id2",0);
        initList();
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

        recyclerView = (RecyclerView)findViewById(R.id.rv_chat_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ChatAdapter adapter = new ChatAdapter(chatRecords,bitmap);
        recyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ChatRecord record = new ChatRecord();
                record.setSenderId(LoginActivity.USER.getId());
                if (LoginActivity.USER.getId() == id1){
                    record.setGeterId(id2);
                }else{
                    record.setGeterId(id1);
                }
                record.setMessage(content.getText().toString());
                record.setSendTime(new Timestamp(System.currentTimeMillis()));
                if(conversationId == 0){
                    initList();
                }else{
                    record.setConversationId(conversationId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OutputStream outputStream = LoginActivity.socket.getOutputStream();
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
                                String text = gson.toJson(record);
                                Log.d("日志",text);
                                outputStream.write(text.getBytes("utf-8"));
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                Message message = new Message();
                message.obj = record;
                message.arg1 = 2;
                mHandler.sendMessage(message);
                content.setText("");
            }
        });

        chatListReceiver = new ChatListReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CHAT_LIST");
        registerReceiver(chatListReceiver,intentFilter);
    }
    private void initView(){
        content = (EditText)findViewById(R.id.et_content);
        send = (ImageView)findViewById(R.id.ivAdd);
    }
    public void initList(){
        progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setTitle("请稍后......");
        progressDialog.setMessage("正在加载......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        HashMap<String, String> params = new HashMap<String, String>();
        if(LoginActivity.USER == null){
            progressDialog.dismiss();
            return ;
        }else{
        }
        params.put("id1", id1+"");
        params.put("id2", id2+"");
        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) +  "chatRecordsServlet";
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            Log.d("日志",compeletedURL);
            HttpUtil.sendGetOkHttpRequest(compeletedURL,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(ChatActivity.this,"未能连接到网络", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        return ;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    message.arg1 = 1;
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ChatListReceiver extends BroadcastReceiver {
        public ChatListReceiver() {
            super();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            ChatRecord record = (ChatRecord) intent.getSerializableExtra("record");
            if(record.getConversationId() == conversationId){
                Message message = new Message();
                message.obj = record;
                message.arg1 = 2;
                mHandler.sendMessage(message);
                Log.d("日志",record.getMessage());
            }else{

            }
        }
    }
}
