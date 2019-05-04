package util;

import android.content.Intent;
import android.os.Handler;

import com.example.a14574.expresshelp.MyApplication;
import com.example.a14574.practice.DeleteOrderService;

import java.io.Serializable;

import model.Order;

public class OrderTimer implements Serializable {
    public Order order;
    public OrderTimer(Order order){
        this.order=order;
        mhandle = new Handler();
        mhandle.postDelayed(timeRunable,1000);
    }

    public Runnable timeRunable = new Runnable() {
        @Override
        public void run() {

            currentSecond = currentSecond - 1000;
            if(currentSecond==0){
                mhandle.removeCallbacks(this);
                Intent intent = new Intent(MyApplication.getContext(),DeleteOrderService.class);
                intent.putExtra("orderId",order.getId());
                intent.putExtra("orderTimer",OrderTimer.this);
                MyApplication.getContext().startService(intent);
                return;
            }
            if (!isPause) {
                //递归调用本runable对象，实现每隔一秒一次执行任务
                mhandle.postDelayed(this, 1000);
            }
        }
    };
    //计时器
    public Handler mhandle = null;
    private static boolean isPause = false;//是否暂停
    public long currentSecond = 30*60*1000;//当前毫秒数
    /*****************计时器*******************/

    public long getCurrentSecond(){
        return currentSecond;
    }
}
