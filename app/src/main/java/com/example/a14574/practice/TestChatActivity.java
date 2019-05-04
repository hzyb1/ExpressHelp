package com.example.a14574.practice;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.MyAdapter;
import http.ClientSender;
import model.MyBean;

public class TestChatActivity extends AppCompatActivity {
    private RecyclerView rv;
    private EditText et;
    private Button btn;
    private Socket socket;
    private ArrayList<MyBean> list;
    private MyAdapter adapter;
    private int sendId;
    private String ip;
    public static int getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat);
        sendId = 2;
//        getId = 1;
        //      ip = "10.13.0.224";
        ip = "45.32.84.43";

        rv = (RecyclerView) findViewById(R.id.rv);
        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        list = new ArrayList<>();
        adapter = new MyAdapter(this);

        final Handler handler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, 10010);
                    //        new ClientSender(socket).send();
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, len);
                        // 发到主线程中 收到的数据
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
       //             new ClientSender(new Socket(ip, 10010)).send();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = et.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream outputStream = socket.getOutputStream();
                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                            MyBean myBean = new MyBean();
                            myBean.setData(df.format(new Date()).toString());
                            myBean.setSendId(LoginActivity.USER.getId());
                            myBean.setGetId(getId);
                            myBean.setName(LoginActivity.USER.getUsername());
                            myBean.setData(data);
                            String text = new Gson().toJson(myBean);
                            outputStream.write(text.getBytes("utf-8"));
                            //   outputStream.write((socket.getLocalPort() + "//" + data + "//" + df.format(new Date())).getBytes("utf-8"));
                            outputStream.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //
//                int localPort = socket.getLocalPort();
//                String[] split = ((String) msg.obj).split("//");
//                if (split[0].equals(localPort + "")) {
//                    MyBean bean = new MyBean(split[1],1,split[2],"我：");
//                    list.add(bean);
//                } else {
//                    MyBean bean = new MyBean(split[1],2,split[2],("来自：" + split[0]));
//                    list.add(bean);
//                }
                Log.d("日志", (String) msg.obj);
                MyBean myBean = new Gson().fromJson((String) msg.obj, MyBean.class);
                if (myBean.getSendId() == LoginActivity.USER.getId()) {
                    myBean.setName("我：");
                    myBean.setNumber(1);
                } else {
                    myBean.setName("来自：" + myBean.getName());
                    myBean.setNumber(2);
                }
                list.add(myBean);
                // 向适配器set数据
                adapter.setData(list);
                rv.setAdapter(adapter);
                LinearLayoutManager manager = new LinearLayoutManager(TestChatActivity.this, LinearLayoutManager.VERTICAL, false);
                rv.setLayoutManager(manager);
            }
        }
    }
}
