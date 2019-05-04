package com.example.a14574.practice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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
      /*  Log.d("日志", "服务方法执行了");
        try {
            Log.d("日志", "开始睡眠");
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
                e1.printStackTrace();
        }
        Log.d("日志", "睡眠结束");
        Intent intent1 = new Intent("CHAT_LIST");
        ChatUser user = new ChatUser();
        user.setUsername("服务的");
        user.setMessage("服务发来的");
        intent1.putExtra("user", user);
        sendBroadcast(intent1);*/

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
