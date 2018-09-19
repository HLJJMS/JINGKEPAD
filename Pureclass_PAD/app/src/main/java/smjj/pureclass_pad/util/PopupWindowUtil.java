package smjj.pureclass_pad.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by wlm on 2017/7/3.
 */

public class PopupWindowUtil {

    private static PopupWindow myPopuWindow;
    private static Activity activity;
    private static int heightY;
    private static int widthX;


    /**
     * 获取一个PopuWindow实例
     * @param context
     * @param contentView
     * @param widthScale PopuWindow的宽是屏幕宽的多少
     * @param heightScale PopuWindow的高是屏幕宽的多少
     * @param backAlpha PopuWindow展示时背景透明度
     * @param animStyle PopuWindow展示时动画风格 -1 代表没有动画效果
     * @return
     */
    public static PopupWindow getPopuWindow(Context context, View contentView
            , int widthScale, int heightScale, float backAlpha, int animStyle) {
        activity = (Activity) context;
        Display display = activity.getWindowManager().getDefaultDisplay(); //得到屏幕
        int width = display.getWidth();//得到屏幕宽、高
        int height = display.getHeight();
        heightY = height / heightScale;
        widthX = width / widthScale;


        myPopuWindow = new PopupWindow(contentView,
                width / widthScale, height / heightScale,true);
        myPopuWindow.setTouchable(true);
        myPopuWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
//        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        myPopuWindow.setBackgroundDrawable(new BitmapDrawable());


        backgroundAlpha(backAlpha);
        myPopuWindow.setOnDismissListener(new poponDismissListener());

        if (animStyle != -1){
            myPopuWindow.setAnimationStyle(animStyle);//可以设置动画
        }
//
//        popupWindow.showAsDropDown(ll_select_city);//rl_title 是布局中的一个空间
        return myPopuWindow;
    }

    //高为包裹内容，宽固定
    public static PopupWindow getPopuWindow1(Context context, View contentView
            , int widthScale, int heightScale, float backAlpha, int animStyle) {
        activity = (Activity) context;
        Display display = activity.getWindowManager().getDefaultDisplay(); //得到屏幕
        int width = display.getWidth();//得到屏幕宽、高
        int height = display.getHeight();
        heightY = height / heightScale;
        widthX = width / widthScale;


        myPopuWindow = new PopupWindow(contentView,
                width / widthScale, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        myPopuWindow.setTouchable(true);
        myPopuWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
//        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        myPopuWindow.setBackgroundDrawable(new BitmapDrawable());


        backgroundAlpha(backAlpha);
        myPopuWindow.setOnDismissListener(new poponDismissListener());

        if (animStyle != -1){
            myPopuWindow.setAnimationStyle(animStyle);//可以设置动画
        }
//
//        popupWindow.showAsDropDown(ll_select_city);//rl_title 是布局中的一个空间
        return myPopuWindow;
    }




    //高为包裹内容，宽固定,传值类型为float
    public static PopupWindow getPopuWindow2(Context context, View contentView
            , float widthScale, float heightScale, float backAlpha, int animStyle) {
        activity = (Activity) context;
        Display display = activity.getWindowManager().getDefaultDisplay(); //得到屏幕
        float width = display.getWidth();//得到屏幕宽、高
        float height = display.getHeight();
        heightY = (int) (height / heightScale);
        widthX = (int) (width / widthScale);


        myPopuWindow = new PopupWindow(contentView,
                (int) (width / widthScale), (int) (height/heightScale),true);
        myPopuWindow.setTouchable(true);
        myPopuWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
//        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        myPopuWindow.setBackgroundDrawable(new BitmapDrawable());


        backgroundAlpha(backAlpha);
        myPopuWindow.setOnDismissListener(new poponDismissListener());

        if (animStyle != -1){
            myPopuWindow.setAnimationStyle(animStyle);//可以设置动画
        }

        return myPopuWindow;
    }





    private static void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    static class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    /**
     * 展示在摸个控件的下方
     * @param parent
     */
    public static void showAtLoactionBottom(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        myPopuWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1]+ parent.getHeight());

    }

    /**
     * 展示在某个件上方
     * @param parent
     */
    public static void showAtLoactionTop(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        myPopuWindow.showAtLocation(parent, Gravity.NO_GRAVITY
                ,location[0] , location[1] - heightY);
    }

    /**
     * 系统自动展示位置
     * @param parent
     */
    public static void showCustom(View parent) {
        myPopuWindow.showAsDropDown(parent);
    }


    /**
     * 展示在某个控件右下方
     * @param parent
     */
    public static void showAtLoactionRightAndBottom(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        myPopuWindow.showAtLocation(parent, Gravity.NO_GRAVITY
                ,location[0] + parent.getWidth(), location[1] + parent.getHeight());
    }

    /**
     * 展示在某个控件右方
     * @param parent
     */
    public static void showAtLoactionRight(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        myPopuWindow.showAtLocation(parent, Gravity.NO_GRAVITY
                ,location[0] + parent.getWidth(), location[1]);
    }

    /**
     * 展示在某个控件左下方
     * @param parent
     */
    public static void showAtLoactionLeftAndBottom(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        myPopuWindow.showAtLocation(parent, Gravity.NO_GRAVITY
                ,location[0] - parent.getWidth(), location[1] + parent.getHeight());
    }

    /**
     * 判断控件是否在屏幕的上半部分
     *
     */
    public static boolean isScreenTOP(Context context1, View parent){
        Activity activity1 = (Activity) context1;
        Display display = activity1.getWindowManager().getDefaultDisplay(); //得到屏幕
        int width = display.getWidth();//得到屏幕宽、高
        int height = display.getHeight();
        int screenCenter = height / 2;
        int[] location = new int[2];

        parent.getLocationOnScreen(location);
        if (location[1] >= screenCenter){
            return false;
        }
        return true;
    }
}
