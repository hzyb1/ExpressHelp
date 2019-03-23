package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.a14574.expresshelp.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyInfoFragment extends Fragment {
    private Button setting;
    private CircleImageView headImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info,container,false);
        setting = (Button) view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        headImage = (CircleImageView) view.findViewById(R.id.head_image);
        Glide.with(getContext()).load(R.drawable.temp_head2).into(headImage);
        return view;
    }
}
