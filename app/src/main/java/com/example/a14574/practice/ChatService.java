package com.example.a14574.practice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import model.ChatRecord;

public class ChatService extends Service {
    private ChatBinder mBinder = new ChatBinder();
    public class ChatBinder extends Binder{
        private void addMessage(){//发来信息

        }

        private void sendMessage(){//发送信息

        }
    }


    public ChatService() {
    }

    @Override
    public void onCreate() {
        Log.d("日志","服务被启动了");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent("CHAT_LIST");
        ChatRecord record = new ChatRecord();
        intent1.putExtra("record", record);
        sendBroadcast(intent1);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
