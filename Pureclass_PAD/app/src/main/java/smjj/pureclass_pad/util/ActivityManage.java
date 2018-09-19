package smjj.pureclass_pad.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import smjj.pureclass_pad.activity.ClassAfterActivity1;
import smjj.pureclass_pad.activity.MainActivity1;


public class ActivityManage {
    private static ArrayList<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);

    }


    public static void finishAll() {

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            if (activity != null) {
                activity.finish();
            }
        }
    }


    public static void backToMain(Context context) {
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            if (activity != null && (!(activity instanceof MainActivity1))) {
                activities.remove(activity);
                activity.finish();
            }
        }
        Intent intentMain = null;
        intentMain = new Intent(context, MainActivity1.class);
        context.startActivity(intentMain);
    }

    public static void backToNewCheckCode(Context context) {
        for (int i = activities.size()-1; i>=0; i--) {
            Activity activity = activities.get(i);
            if (activity != null && (!(activity instanceof MainActivity1))) {
                activities.remove(activity);
                activity.finish();
            }else{
                break;
            }
        }
        if(activities.size()==0){
            Intent intentMain = null;
            intentMain = new Intent(context, MainActivity1.class);
            context.startActivity(intentMain);
        }

    }
    public static void backToContinueCheckCode(Context context) {
        for (int i = activities.size()-1; i>=0; i--) {
            Activity activity = activities.get(i);
//            if (activity != null && (!(activity instanceof AutoContinue1Activity))) {
//                activities.remove(activity);
//                activity.finish();
//            }else{
//                break;
//            }
        }
        if(activities.size()==0){
            Intent intentMain = null;
            intentMain = new Intent(context, MainActivity1.class);
            context.startActivity(intentMain);
        }

    }

    public static void backToActivity() {
        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (activity != null && (!(activity instanceof ClassAfterActivity1))) {
                activities.remove(activity);
                activity.finish();
            }else if (activity != null && activity instanceof ClassAfterActivity1){
                //也可以先返回到主界面在跳转的课后界面
                return;
            }
        }
    }

}
