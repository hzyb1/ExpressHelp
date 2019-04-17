package com.example.a14574.expresshelp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import com.wildma.pictureselector.PictureSelector;


import java.io.File;
import java.io.IOException;

import fragment.HomePageFragment;
import fragment.MessageFragment;
import fragment.MyInfoFragment;
import http.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends BaseActivity {           //主界面活动

    private Fragment fragment[] = new Fragment[3];
    RadioButton[ ] rbs = new RadioButton[3];
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("日志","home???");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragment[0] = new HomePageFragment();
        fragment[1] = new MessageFragment();
        fragment[2] = new MyInfoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,fragment[0])
                .add(R.id.container, fragment[1])
                .add(R.id.container, fragment[2]).commit();

        homePageFragment(null);
        rbs[0] = (RadioButton) findViewById(R.id.home_page);
        rbs[1] = (RadioButton) findViewById(R.id.message);
        rbs[2] = (RadioButton) findViewById(R.id.my_info);
        initView();
    }
    public void homePageFragment(View view){
        getSupportFragmentManager().beginTransaction()
                .hide(fragment[1])
                .hide(fragment[2])
                .show(fragment[0]).commit();
    }
    public void messageFragment(View view){
        getSupportFragmentManager().beginTransaction()
                .hide(fragment[0])
                .hide(fragment[2])
                .show(fragment[1]).commit();
    }
    public void myInfoFragment(View view){
        getSupportFragmentManager().beginTransaction()
                .hide(fragment[1])
                .hide(fragment[0])
                .show(fragment[2]).commit();
    }
    private void initView() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Log.d("Myheight",height+"");
        int realHeight = (int)(height/22.48);
        Log.d("Realheight",realHeight+"");
        //定义底部标签图片大小和位置
        Drawable drawable_home_page = getResources().getDrawable(R.drawable.selector_home_page);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_home_page.setBounds(0, 0,  realHeight,  realHeight);
        //设置图片在文字的哪个方向
        rbs[0].setCompoundDrawables(null, drawable_home_page, null, null);

        //定义底部标签图片大小和位置
        Drawable drawable_message = getResources().getDrawable(R.drawable.selector_message);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_message.setBounds(0, 0,  realHeight,  realHeight);
        //设置图片在文字的哪个方向
        rbs[1].setCompoundDrawables(null, drawable_message, null, null);

        //定义底部标签图片大小和位置
        Drawable drawable_my_info = getResources().getDrawable(R.drawable.selector_my_info);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_my_info.setBounds(0, 0,  realHeight,  realHeight);
        //设置图片在文字的哪个方向
        rbs[2].setCompoundDrawables(null, drawable_my_info, null, null);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                //Log.d("picturePath",picturePath);
                uploadImage(new File(picturePath));

            }
        }
    }
    private void uploadImage( File file) {    //上传照片
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

}
