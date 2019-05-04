package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.a14574.expresshelp.ModifyInformationActivity;

public class Dialog {

    public static void showToBecomeRunnerDialog(final Context a){
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        dialog.setTitle("还未成为跑手");
        dialog.setMessage("接单信息需要成为跑手才能查看，是否前往信息界面完善信息，成为跑手");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(a, ModifyInformationActivity.class);
                a.startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}
