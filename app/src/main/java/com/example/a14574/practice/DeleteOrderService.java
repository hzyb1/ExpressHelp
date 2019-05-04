package com.example.a14574.practice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import model.Order;
import util.OrderTimer;

public class DeleteOrderService extends Service {
    public static List<OrderTimer> orderTimerList = new ArrayList<>();
    public DeleteOrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int orderId=intent.getIntExtra("orderId",0);
                if(orderId!=0){
                    doDeleteOrder();
                    OrderTimer orderTimer = (OrderTimer) intent.getSerializableExtra("orderTimer");
                    orderTimerList.remove(orderTimer);
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void createOrderTimer(Order order){
        OrderTimer orderTimer = new OrderTimer(order);
        orderTimerList.add(orderTimer);
    }
    private void doDeleteOrder(){

    }
    public static long getTime(Order order){
        for (int i=0;i<=orderTimerList.size();i++){
            if(order.getId()==(orderTimerList.get(i).order.getId())){
                OrderTimer orderTimer = orderTimerList.get(i);
                return orderTimer.getCurrentSecond();
            }
        }
        return 0;
    }
}
