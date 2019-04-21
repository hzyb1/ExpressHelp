package model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.MyOrderActivity;
import com.example.a14574.expresshelp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;

import http.HttpUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InformationDialog extends Dialog {
    private Context mContext;
    private String content;
    private String positiveName;
    private String negativeName;
    private String title;
    private Order order;

    private TextView order_time;
    private TextView order_id;
    private TextView order_place;
    private TextView order_money;
    private TextView order_first_time;
    private TextView order_second_time;
    private ProgressDialog progressDialog;                   //上传状态对话框
    public InformationDialog(Context context,Order order) {
       // super(context,R.style.dialog);
        super(context);
        this.mContext = context;
        this.order = order;
    }

    public InformationDialog(Context context, int themeResId, String content ,Order order) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.order = order;
    }

    public InformationDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public InformationDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public InformationDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_infromation);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
       // window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
       // setContentView(layoutResID);

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        initView();
    }
    private void initView(){
       order_time = (TextView)findViewById(R.id.order_time);
       order_id = (TextView)findViewById(R.id.order_id);
       order_place = (TextView)findViewById(R.id.order_place);
       order_money = (TextView)findViewById(R.id.order_money);
       order_first_time = (TextView)findViewById(R.id.order_first_time);
       order_second_time = (TextView)findViewById(R.id.order_second_time);
       String timeStr=order.getSubmitTime()
                .toString()
                .substring(0, order.getSubmitTime().toString().indexOf("."));
       order_time.setText("订单发布时间："+timeStr);
       order_id.setText("订单编号："+order.getId());
       order_place.setText("快递点："+order.getExpressName());
       order_money.setText("金额："+order.getMoney()+"");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
       order_first_time.setText("第一次收货时间："+sdf.format(order.getFirstTakeTimeBegin())+"-"+sdf.format(order.getFirstTakeTimeEnd()));
       order_second_time.setText("第二次收货时间："+sdf.format(order.getSecondTakeTimeBegin())+"-"+sdf.format(order.getSecondTakeTimeEnd()));
       Button  accept = (Button)findViewById(R.id.accept);
       accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
               builder.setTitle("提示");
               builder.setMessage("是否确认接单");
               builder.setCancelable(true);
               builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                //       Toast.makeText(mContext,"确认接单",Toast.LENGTH_SHORT).show();
                       order.setAcceptId(LoginActivity.USER.getId());
                       order.setState(2);

                       updataOrder(order);

                   }
               });
               builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       Toast.makeText(mContext,"取消接单",Toast.LENGTH_SHORT).show();
                   }
               });
               builder.show();
           }
       });
    }
    private void updataOrder(Order order){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            //构造完整URL
            String originAddress = mContext.getString(R.string.TheServer) + "updataOrder";
            Log.d("url:",originAddress);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(order));
            Log.d("日志:",gson.toJson(order));
            HttpUtil.sendPostOkHttpRequest(originAddress,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(mContext,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(mContext,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
//                    if(mContext.getString(R.string.HTTPERROR).equals(result)){
//                        Toast.makeText(mContext,"上传失败！！！",Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(mContext,"上传成功！！！",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(mContext,MyOrderActivity.class);
//                        intent.putExtra("style",1);     //修改完成，去我的订单界面看看
//                        mContext.startActivity(intent);
//                //        mContext.finish();
                //    }

                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


