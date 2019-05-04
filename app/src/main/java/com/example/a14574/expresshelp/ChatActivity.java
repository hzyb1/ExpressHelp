package com.example.a14574.expresshelp;

import android.app.ProgressDialog;
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
import Adapter.ChatListAdapter;
import Adapter.OrderBriefAdapter;
import Adapter.RunnerOrderAdapter;
import http.HttpUtil;
import model.ChatRecord;
import model.MyBean;
import model.Order;
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
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if(msg.arg1 == 1){      //初始化record；
                if(result.length() <= 5){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
                    chatRecords = gson.fromJson(result, new TypeToken<List<ChatRecord>>(){}.getType());
                    conversationId = chatRecords.get(0).getConversationId();
                    ChatAdapter adapter = new ChatAdapter(chatRecords,bitmap);
                    recyclerView.setAdapter(adapter);
                }else{
                    conversationId = Integer.parseInt(result);
                }

            }
            progressDialog.dismiss();       //结束等待
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bitmap = getIntent().getParcelableExtra("photo");
        initView();
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
        id1 = getIntent().getIntExtra("id1",0);
        id2 = getIntent().getIntExtra("id2",0);
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
                                String text = new Gson().toJson(record);
                                outputStream.write(text.getBytes("utf-8"));
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

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
}
