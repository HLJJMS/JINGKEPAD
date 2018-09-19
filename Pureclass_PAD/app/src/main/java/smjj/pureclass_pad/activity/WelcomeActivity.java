package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.StatusBarUtils;

public class WelcomeActivity extends BaseActivity {

    private SharedPreferences spConfig;

    private String userName;
    private String password;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        context = this;
        ActivityManage.addActivity(this);
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        StatusBarUtils.setVoidStatusBar(this);
        Handler handler = new Handler();
        //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLog();
            }
        }, 2000);

    }

    //判断要不要再走登录页
    public void isLog(){
        userName = spConfig.getString(SPCommonInfoBean.loginName, "");
        password = spConfig.getString(SPCommonInfoBean.passWord, "");
        String userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        if (!userCode.equals("") && !userName.equals("") && !password.equals("")){
            Intent intent = new Intent(context, MainActivity1.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(context, LogoActivity1.class);
            startActivity(intent);
            finish();
        }
    }

}
