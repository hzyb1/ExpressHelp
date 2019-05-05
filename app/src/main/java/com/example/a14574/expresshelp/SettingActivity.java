package com.example.a14574.expresshelp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import http.HttpUtil;
import model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button logout;
    private CircleImageView headImage;
    private TextView userName;
    private TextView balance;
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private Button toModifyMyInfo;
    public static final int CHOOSE_PHOTO = 2;
    private RelativeLayout topUp;    //充值
    private ProgressDialog progressDialog;                   //上传状态对话框
    private float moneyF=0;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == 1){
                String result = "";
                result = msg.obj.toString();
                Log.d("日志",result);
                LoginActivity.USER.setHeadImage(result);
                String url = SettingActivity.this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
                Glide.with(SettingActivity.this).load(url).into(headImage);
            }else if(msg.arg1 == 2){
                String result = "";
                result = msg.obj.toString();
                if(SettingActivity.this.getString(R.string.HTTPERROR).equals(result)){
                    Toast.makeText(SettingActivity.this,"上传失败！！！",Toast.LENGTH_LONG).show();
                }else {
                //    LoginActivity.USER.setBalance(LoginActivity.USER.getBalance() + moneyF);
                    Toast.makeText(SettingActivity.this, "上传成功！！！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();
                }

                progressDialog.dismiss();
            }
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initEvents();
    }

    private void initViews(){
        logout = (Button) findViewById(R.id.logout);
        headImage = (CircleImageView) findViewById(R.id.head_image);
        userName = (TextView)findViewById(R.id.user_name);
        balance = (TextView)findViewById(R.id.balance);
        toModifyMyInfo = (Button)findViewById(R.id.to_modify_myinfo);
        topUp = (RelativeLayout) findViewById(R.id.top_up);
        if(LoginActivity.USER!=null){
            userName.setText(LoginActivity.USER.getUsername());
            balance.setText(LoginActivity.USER.getBalance()+"￥");
            String url = this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
            Glide.with(this).load(url).into(headImage);;
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



    }

    private void initEvents(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp= getSharedPreferences("loginSetting", 0);
                int userid = sp.getInt("userid",0);
                Log.d("seeuserid",userid+"");
                if(userid!=0){
                    if(LoginActivity.socket != null){
                        try{
                            LoginActivity.socket.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        LoginActivity.socket = null;
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("userid",0);
                    editor.commit();
                    Toast.makeText(SettingActivity.this,"成功注销，请重新登录！！！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        });
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SettingActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });
        toModifyMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ModifyInformationActivity.class);
                startActivity(intent);
            }
        });
        topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //充值方法
                //Toast.makeText(SettingActivity.this,"You clicked top_up",Toast.LENGTH_SHORT).show();
                showTopUpDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    String imagePath;
                    if(Build.VERSION.SDK_INT>=19){
                        imagePath=handleImageOnKitKat(data);
                    }else{
                        imagePath=handleImageBeforeKitKat(data);
                    }
                    //Log.d("imagePath",imagePath);
                    if(imagePath==null || imagePath.equals("")){
                        Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
                    }else {
                        uploadImage(new File(imagePath));
                    }
                }
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                } else {
                    Toast.makeText(this,"您拒绝了该项权限，软件无法访问相册",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    private void uploadImage(File file) {    //上传照片
        //接口地址
        String urlAddress = this.getString(R.string.TheServer)+"saveHeadImage";
        if (file != null && file.exists()) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("files", System.currentTimeMillis()+ ".jpg",
                            RequestBody.create(MEDIA_TYPE_PNG, file));
            builder.addFormDataPart("userId",LoginActivity.USER.getId()+"");
            HttpUtil.sendMultipart(urlAddress, builder.build(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        return ;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    message.arg1 = 1;
                    mHandler.sendMessage(message);

                    //result就是图片服务器返回的图片地址。
                }
            });
        }
    }

    @TargetApi(19)
    private String handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        return imagePath;
    }
    private String handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        return imagePath;
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showTopUpDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.top_up_layout,null,false);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        final EditText money = (EditText) view.findViewById(R.id.money);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moneyS = money.getText().toString().trim();
                try {
                    moneyF = Float.parseFloat(moneyS);
                }catch (Exception e){
                    Toast.makeText(SettingActivity.this,"金额需为数字哦！！！",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }
                //充值操作
                User user = LoginActivity.USER;
                user.setBalance(user.getBalance()+ moneyF);

                recharge(user);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void recharge(User user){

        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setTitle("正在上传，请稍后......");
        progressDialog.setMessage("上传中......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            //构造完整URL
            String originAddress = this.getString(R.string.TheServer) + "updataUser";
            Log.d("url:",originAddress);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ").create();
            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(user));

            Log.d("日志:",gson.toJson(user));
            HttpUtil.sendPostOkHttpRequest(originAddress,requestBody,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(SettingActivity.this,"网络错误,未能连上服务器", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Looper.prepare();
                        Toast.makeText(SettingActivity.this,"连接网络失败", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Looper.loop();
                        return;
                    }
                    Message message = new Message();
                    message.obj = response.body().string().trim();
                    message.arg1 = 2;
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
