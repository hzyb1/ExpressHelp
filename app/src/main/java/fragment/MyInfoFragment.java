package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a14574.expresshelp.HomeActivity;
import com.example.a14574.expresshelp.LoginActivity;
import com.example.a14574.expresshelp.MyInfoSpecificActivity;
import com.example.a14574.expresshelp.MyOrderActivity;
import com.example.a14574.expresshelp.R;
import com.example.a14574.expresshelp.RunnerActivity;
import com.example.a14574.expresshelp.SettingActivity;
import com.example.a14574.expresshelp.SpecificUserInfoActivity;
import com.wildma.pictureselector.PictureSelector;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import util.Dialog;


public class MyInfoFragment extends Fragment implements View.OnClickListener{
    private Button setting;
    private CircleImageView headImage;   //头像
    private LinearLayout pendingPaymentLayout;

    private TextView userName;

    private TextView my_orders;
    private ImageView my_orders_image;

    private TextView wait_pay;
    private ImageView wait_pay_image;

    private TextView wait_accept;
    private ImageView wait_accept_image;

    private TextView wait_take;
    private ImageView wait_take_image;

    private TextView finish;
    private ImageView finish_image;

    private Button toSpecificInfo;//去查看详情页面

    private LinearLayout pick;
    private LinearLayout give;
    private LinearLayout runner;
    private LinearLayout all;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info,container,false);
        setting = (Button) view.findViewById(R.id.setting);
        userName = (TextView)view.findViewById(R.id.name);
        headImage = (CircleImageView) view.findViewById(R.id.head_image);
        toSpecificInfo = (Button) view.findViewById(R.id.to_specific_info);
        pendingPaymentLayout = (LinearLayout) view.findViewById(R.id.pending_payment_layout);//待支付
        initEvents();
        return view;
    }
    private void initEvents(){
        pendingPaymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
        toSpecificInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfoFragment.this.getActivity(),MyInfoSpecificActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        my_orders = (TextView)getActivity().findViewById(R.id.my_orders);
        my_orders_image = (ImageView)getActivity().findViewById(R.id.my_orders_image);

        wait_pay = (TextView)getActivity().findViewById(R.id.wait_pay);
        wait_pay_image = (ImageView)getActivity().findViewById(R.id.wait_pay_image);

        wait_accept = (TextView)getActivity().findViewById(R.id.wait_accept);
        wait_accept_image = (ImageView)getActivity().findViewById(R.id.wait_accept_image);

        wait_take = (TextView)getActivity().findViewById(R.id.wait_take);
        wait_take_image = (ImageView)getActivity().findViewById(R.id.wait_take_image);

        finish = (TextView)getActivity().findViewById(R.id.finish);
        finish_image = (ImageView)getActivity().findViewById(R.id.finish_image);

        pick = (LinearLayout)getActivity().findViewById(R.id.pick);
        give = (LinearLayout)getActivity().findViewById(R.id.give) ;
        runner = (LinearLayout)getActivity().findViewById(R.id.runner) ;
        all = (LinearLayout)getActivity().findViewById(R.id.runner_all);

        my_orders.setOnClickListener(this);
        my_orders_image.setOnClickListener(this);
        wait_accept.setOnClickListener(this);
        wait_accept_image.setOnClickListener(this);
        wait_pay.setOnClickListener(this);
        wait_pay_image.setOnClickListener(this);
        wait_take.setOnClickListener(this);
        wait_take_image.setOnClickListener(this);
        finish.setOnClickListener(this);
        finish_image.setOnClickListener(this);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRunnerOrder(1);
            }
        });
        give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRunnerOrder(2);
            }
        });
        runner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRunnerOrder(3);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRunnerOrder(4);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        switch (view.getId()){
            case R.id.my_orders:
                ;
            case R.id.my_orders_image:
                intent.putExtra("style",1);
                break;
            case R.id.wait_accept :
                ;
            case R.id.wait_accept_image :
                intent.putExtra("style",3);
                break;
            case R.id.wait_pay:
                ;
            case R.id.wait_pay_image:
                intent.putExtra("style",2);
                break;
            case R.id.wait_take_image:
                ;
            case R.id.wait_take:
                intent.putExtra("style",4);
                break;
            case R.id.finish:
                ;
            case R.id.finish_image:
                intent.putExtra("style",5);
                break;

                default:
                    break;
        }
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("日志","启动！");
        if(LoginActivity.USER != null){
            userName.setText(LoginActivity.USER.getUsername());
            String url = this.getString(R.string.TheServer)+"headImages/"+ LoginActivity.USER.getHeadImage();
            Log.d("userimage",LoginActivity.USER.getHeadImage());
            Log.d("testImageUrl",url);
            Glide.with(getContext()).load(url).into(headImage);
        }
    }

    private void toRunnerOrder(int style){
        if (LoginActivity.USER.getIdCard() == null){
            Dialog.showToBecomeRunnerDialog(this.getActivity());
        }else {
            Intent intent = new Intent(getActivity(), RunnerActivity.class);
            intent.putExtra("style",style);
            startActivity(intent);
        }
    }
}
