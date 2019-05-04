package com.example.a14574.practice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import Adapter.RunnerOrderAdapter;
import model.ChatRecord;
import model.Order;

public class ChatService extends Service {
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            result = msg.obj.toString();
            if(msg.what == 1){
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
                ChatRecord chatRecord = gson.fromJson(result, ChatRecord.class);
                if(chatRecord.getGeterId() == LoginActivity.USER.getId()){      //处理发送给我的信息
                    Intent intent1 = new Intent("CHAT_LIST");
                    intent1.putExtra("record", chatRecord);
                    sendBroadcast(intent1);
                }
            }
        }
    };


    public ChatService() {




    }

    @Override
    public void onCreate() {
        Log.d("日志","服务被启动了");

        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //                  socket = new Socket(ip, 10010);
                    //        new ClientSender(socket).send();
                    if(LoginActivity.socket != null){
                        return ;
                    }
                    InputStream inputStream = LoginActivity.socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, len);
                        // 发到主线程中 收到的数据
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
