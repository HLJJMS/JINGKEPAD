package smjj.pureclass_pad.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;

import smjj.pureclass_pad.R;


public class LoadingBox {
    AsyncTask<String, Void, String> info;
    Activity activity;
    ProgressDialog loadingDialog;

    public LoadingBox(Activity activity,
                      AsyncTask<String, Void, String> Asynctask) {
        this.info = Asynctask;
        this.activity = activity;
    }

    public void Show() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
        if (activity.isFinishing()) {
            return;
        }
        loadingDialog = ProgressDialog.show(this.activity, "", "");


        if (Build.VERSION.SDK_INT > 19) {
            loadingDialog.setContentView(R.layout.loading5);
        } else {
            loadingDialog.setContentView(R.layout.loading);
        }
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setOnCancelListener(new Dialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (info != null)
                    info.cancel(true);
                loadingDialog.dismiss();
            }
        });
    }

    public void close() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

    }

    public void Show1() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
        if (activity.isFinishing()) {
            return;
        }
        loadingDialog = ProgressDialog.show(this.activity, "", "");


        if (Build.VERSION.SDK_INT > 19) {
            loadingDialog.setContentView(R.layout.loading6);
        } else {
            loadingDialog.setContentView(R.layout.loading);
        }
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setOnCancelListener(new Dialog.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (info != null)
                    info.cancel(true);
                loadingDialog.dismiss();
            }
        });
    }

}
