package smjj.pureclass_pad.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import smjj.pureclass_pad.R;

/**
 * Created by qian_fly on 2015/4/18 0018.
 */
public class AlertDialogUtil {
    private static View view;
    private static TextView tv_content;
    private static TextView mTv_title;
    private static AlertDialog alertDialog;
    private static AlertDialog inputDialog;
    private static EditText et_inputContent;

    public static class Title {
        public final static String WARN = "警告";
        public final static String PROMPT = "提示";
    }

    public static void makeText(Context context, String message, int titleType) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        showAlertDialog(context, Title.PROMPT, message);
    }

    public static void makeNewCheckCodeText(final Context context, String message) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }

        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        mTv_title.setText(Title.PROMPT);
        tv_content.setText(message);
        alertDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityManage.backToNewCheckCode(context);
                    }
                })
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }

    public static void makeContinueCheckCodeText(final Context context, String message) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        if(activity.isFinishing()){
            return;
        }

        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        mTv_title.setText(Title.PROMPT);
        tv_content.setText(message);
        alertDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityManage.backToContinueCheckCode(context);
                    }
                })
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }


    public static void showAlertDialog(Context context, String title, String message) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            return;
        }
        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        mTv_title.setText(title);
        tv_content.setText(message);
        alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("确定", null)
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }

    public static void showAlertDialog(Context context, String title, String message, final ClickListener_single clickListener) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        mTv_title.setText(title);
        tv_content.setText(message);
        alertDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.positionClick();
                        }

                    }
                })
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }


    public static void makeText(Context context, String message, final ClickListener clickListener) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        showAlertDialog(context, Title.PROMPT, message, clickListener);
    }


    public static void showAlertDialog(Context context, String title, String message, final ClickListener clickListener, String strComfir, boolean cancelable) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        tv_content.setText(message);
        mTv_title.setText(title);
        alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(strComfir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.positionClick();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.negetiveClick();
                        }

                    }
                })
                .setCancelable(false)
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);

        if (cancelable) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
        } else {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        }
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        //alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }


    public static void showAlertDialog(Context context, String title, String message, final ClickListener clickListener) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        tv_content.setText(message);
        mTv_title.setText(title);
        alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.positionClick();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.negetiveClick();
                        }

                    }
                })
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }

    public static void dissAlerDialog() {
        alertDialog.dismiss();
    }

    public interface ClickListener {
        void positionClick();

        void positionClick(String content);

        void negetiveClick();
    }

    public interface ClickListener_single {
        void positionClick();
    }

    public static void showEditDialog(Context context, String title, String message, String inputContent, final ClickListener clickListener) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        if (inputDialog != null && inputDialog.isShowing()) {
            inputDialog.dismiss();
            inputDialog = null;
        }
        view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_input_layout, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.tv_title);
        et_inputContent = (EditText) view.findViewById(R.id.et_inputContent);
        if (TextUtils.isEmpty(message)) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setText(message);
        }
        if (!TextUtils.isEmpty(inputContent)) {
            et_inputContent.setText(inputContent);
        }
        mTv_title.setText(title);
        inputDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.positionClick(et_inputContent.getText().toString().trim());
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.negetiveClick();
                        }

                    }
                })
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        inputDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        inputDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        inputDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }

    public static void showAlertDialogButton(Context context, String title, String message, String positiveString, String negativeString, final ClickListener clickListener) {
        Activity activity= (Activity) context;
        if(activity.isFinishing()){
            return;
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        String positiveButtonStr="确定";
        String negativeButtonStr="取消";
        positiveButtonStr= TextUtils.isEmpty(positiveString)?positiveButtonStr:positiveString;
        negativeButtonStr= TextUtils.isEmpty(negativeString)?negativeButtonStr:negativeString;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_msgshow, null, false);
        tv_content = (TextView) view.findViewById(R.id.tv_msg);
        mTv_title = (TextView) view.findViewById(R.id.mTv_title);
        tv_content.setText(message);
        mTv_title.setText(title);
        alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(positiveButtonStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.positionClick();
                        }

                    }
                })
                .setNegativeButton(negativeButtonStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (clickListener != null) {
                            clickListener.negetiveClick();
                        }

                    }
                })
                .show();
        final int color = context.getResources().getColor(R.color.sendpress);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color);
    }


}
