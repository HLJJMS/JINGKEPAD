package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.StatusBarUtils;
//主界面
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private LinearLayout ll_prepare_class, ll_go_class, ll_after_class, ll_appraisal;
    private LinearLayout ll_exit;
    private RelativeLayout rl_bottom_actionbar;

    private boolean okToExit;//是否退出

    private SharedPreferences spConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManage.addActivity(this);
        context = this;
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        okToExit = false;

        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        rl_bottom_actionbar = (RelativeLayout) findViewById(R.id.rl_bottom_actionbar);
        rl_bottom_actionbar.setBackgroundColor(Color.parseColor("#004559"));

        ll_prepare_class = (LinearLayout) findViewById(R.id.ll_prepare_class);
        ll_go_class = (LinearLayout) findViewById(R.id.ll_go_class);
        ll_after_class = (LinearLayout) findViewById(R.id.ll_after_class);
        ll_appraisal = (LinearLayout) findViewById(R.id.ll_appraisal);
        ll_exit = (LinearLayout) findViewById(R.id.ll_exit);



        ll_prepare_class.setOnClickListener(this);
        ll_go_class.setOnClickListener(this);
        ll_after_class.setOnClickListener(this);
        ll_appraisal.setOnClickListener(this);
        ll_exit.setOnClickListener(this);

        
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;
        switch (view.getId()){
            case R.id.ll_prepare_class:
                intent = new Intent(this, PrepareClassActivity.class);
                intent.putExtra("enterMark","1");
                startActivity(intent);
                break;
            case R.id.ll_go_class:
                intent = new Intent(this, PrepareClassActivity.class);
                intent.putExtra("enterMark","2");
                startActivity(intent);

                break;
            case R.id.ll_after_class:
                intent = new Intent(this, ClassAfterActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_appraisal:
//                intent = new Intent(this, AppraisalActivity.class);
//                startActivity(intent);
                break;
            case R.id.ll_exit:
                initUser();
                break;
        }


    }

    /**
     * 注销登录
     */
    private void initUser(){
        AlertDialogUtil.showAlertDialog(context, "提示", "是否注销当前账号", new AlertDialogUtil.ClickListener() {
            @Override
            public void positionClick() {
                spConfig.edit().clear().commit();
                System.exit(0);
            }

            @Override
            public void positionClick(String content) {

            }
            @Override
            public void negetiveClick() {

            }
        });
    }

    /**
     * 双击返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            if (okToExit) {
                System.exit(0);
            } else {
                okToExit = true;
                Toast.makeText(this, "再按一次退出-乐课堂", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        okToExit = false;
                    }
                }, 2000); // 2秒后重置
            }
        }
        return true;
    }

}
