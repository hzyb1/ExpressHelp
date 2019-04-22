package com.example.a14574.practice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import model.ChatUser;

public class ChatService extends Service {
    private ChatBinder mBinder = new ChatBinder();

    class ChatBinder extends Binder{
        private ChatUser chat;

        private void addMessage(){//发来信息

        }

        private void sendMessage(){//发送信息

        }
    }


    public ChatService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
