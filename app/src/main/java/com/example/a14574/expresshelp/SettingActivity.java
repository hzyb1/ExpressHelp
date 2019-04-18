package com.example.a14574.expresshelp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wildma.pictureselector.PictureSelector;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import http.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.ActivityCollector;

public class SettingActivity extends BaseActivity {

    private Button logout;
    private CircleImageView headImage;
    private TextView userName;
    private TextView balance;
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    public static final int CHOOSE_PHOTO = 2;
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
        if(LoginActivity.USER!=null){
            userName.setText(LoginActivity.USER.getUsername());
            balance.setText(LoginActivity.USER.getBalance()+"￥");
            String url = this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
            Glide.with(this).load(url).into(headImage);;
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
            default:
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
        String urlAddress = this.getString(R.string.TheServer)+"headImages";
        if (file != null && file.exists()) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("files", "img" + "_" + System.currentTimeMillis() + ".jpg",
                            RequestBody.create(MEDIA_TYPE_PNG, file));
            HttpUtil.sendMultipart(urlAddress, builder.build(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.e("---", "onResponse: 成功上传图片之后服务器的返回数据：" + result);
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
}
