package com.example.a14574.expresshelp;

import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import fragment.HomePageFragment;
import fragment.MessageFragment;
import fragment.MyInfoFragment;

public class HomeActivity extends BaseActivity {           //主界面活动

    private Fragment fragment[] = new Fragment[3];
    private Fragment currentFragment;     //当前fragment
    RadioButton[ ] rbs = new RadioButton[3];
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private IntentFilter intentFilter;
    private MessageFragment.ChatListReceiver chatListReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("日志","home???");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragment[0] = new HomePageFragment();
        fragment[1] = new MessageFragment();
        fragment[2] = new MyInfoFragment();

        rbs[0] = (RadioButton) findViewById(R.id.home_page);
        rbs[1] = (RadioButton) findViewById(R.id.message);
        rbs[2] = (RadioButton) findViewById(R.id.my_info);
        fragmentManager.beginTransaction().add(R.id.container,fragment[0]).commit();
        currentFragment=fragment[0];
        homePageFragment(null);
        initView();
        intentFilter = new IntentFilter();
        intentFilter.addAction("CHAT_LIST");
        chatListReceiver = new MessageFragment().new ChatListReceiver();
        registerReceiver(chatListReceiver,intentFilter);
    }
    public void homePageFragment(View view){
        if(currentFragment!=fragment[0]){
            if(!fragment[0].isAdded()){
                fragmentManager.beginTransaction().hide(currentFragment).add(R.id.container,fragment[0]).commit();
            }else {
                fragmentManager.beginTransaction().hide(currentFragment).show(fragment[0]).commit();
            }
            currentFragment=fragment[0];
        }
    }
    public void messageFragment(View view){
        if(currentFragment!=fragment[1]){
            if(!fragment[1].isAdded()){
                fragmentManager.beginTransaction().hide(currentFragment).add(R.id.container,fragment[1]).commit();
            }else {
                fragmentManager.beginTransaction().hide(currentFragment).show(fragment[1]).commit();
            }
            currentFragment=fragment[1];
        }
    }
    public void myInfoFragment(View view){
        if(currentFragment!=fragment[2]){
            if(!fragment[2].isAdded()){
                fragmentManager.beginTransaction().hide(currentFragment).add(R.id.container,fragment[2]).commit();
            }else {
                fragmentManager.beginTransaction().hide(currentFragment).show(fragment[2]).commit();
            }
            currentFragment=fragment[2];
        }
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
