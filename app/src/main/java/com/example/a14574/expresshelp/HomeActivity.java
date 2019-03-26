package com.example.a14574.expresshelp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import fragment.HomePageFragment;
import fragment.MessageFragment;
import fragment.MyInfoFragment;


public class HomeActivity extends AppCompatActivity {           //主界面活动

    private Fragment fragment[] = new Fragment[3];
    RadioButton[ ] rbs = new RadioButton[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                /*//去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        /*if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
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
}
