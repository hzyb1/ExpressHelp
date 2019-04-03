package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a14574.expresshelp.MyOrderActivity;
import com.example.a14574.expresshelp.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyInfoFragment extends Fragment implements View.OnClickListener{
    private Button setting;
    private CircleImageView headImage;
    private LinearLayout pendingPaymentLayout;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info,container,false);
        setting = (Button) view.findViewById(R.id.setting);
        pendingPaymentLayout = (LinearLayout) view.findViewById(R.id.pending_payment_layout);//待支付
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        headImage = (CircleImageView) view.findViewById(R.id.head_image);
        Glide.with(getContext()).load(R.drawable.temp_head2).into(headImage);
        return view;
    }
    private void initEvents(){
        pendingPaymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}
