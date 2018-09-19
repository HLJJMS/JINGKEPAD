package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.StatusBarUtils;

/**
 * Created by wlm on 2017/9/7.
 * 二期主界面
 */

public class MainActivity1 extends BaseActivity implements View.OnClickListener{
    private Context context;
    private ImageView iv_prepare_class, iv_go_class, iv_after_class, iv_appraisal;
    private ImageView iv_teaching, iv_dual_teacher;
    private LinearLayout ll_exit, ll_dual_teacher;
    private RelativeLayout rl_bottom_actionbar;

    private boolean okToExit;//是否退出

    private SharedPreferences spConfig;
    private String isDualTeacher;//是否双师 0是双师 1非双师

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ActivityManage.addActivity(this);
        context = this;
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
        okToExit = false;


        StatusBarUtils.setVoidStatusBar(this);

        iv_prepare_class = (ImageView) findViewById(R.id.iv_prepare_class);
        iv_go_class = (ImageView) findViewById(R.id.iv_go_class);
        iv_after_class = (ImageView) findViewById(R.id.iv_after_class);
        iv_appraisal = (ImageView) findViewById(R.id.iv_appraisal);
        iv_teaching = (ImageView) findViewById(R.id.iv_teaching);
        iv_dual_teacher = (ImageView) findViewById(R.id.iv_dual_teacher);
        ll_exit = (LinearLayout) findViewById(R.id.ll_exit);
        ll_dual_teacher = (LinearLayout) findViewById(R.id.ll_dual_teacher);
        ll_dual_teacher.setVisibility(View.GONE);

        if (isDualTeacher != null && isDualTeacher.equals("0")){
            iv_dual_teacher.setImageResource(R.drawable.dual_teacher);
        }else {
            iv_dual_teacher.setImageResource(R.drawable.no_dual_teacher);
        }

        iv_prepare_class.setOnClickListener(this);
        iv_go_class.setOnClickListener(this);
        iv_after_class.setOnClickListener(this);
        iv_appraisal.setOnClickListener(this);
        iv_teaching.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
        ll_dual_teacher.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        Intent intent = null;
        switch (view.getId()){
            case R.id.iv_prepare_class:
                intent = new Intent(this, PrepareClassActivity1.class);
                intent.putExtra("enterMark","1");
                startActivity(intent);
                break;
            case R.id.iv_go_class:
//                intent = new Intent(this, PrepareClassActivity.class);
                intent = new Intent(this, GoClassSevenActivity.class);
                intent.putExtra("enterMark","2");
                startActivity(intent);

                break;
            case R.id.iv_after_class:
                intent = new Intent(this, PrepareCAActivity.class);
//                intent = new Intent(this, ClassAfterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_appraisal:
                intent = new Intent(this, AppraisalActivity1.class);
                startActivity(intent);
                break;
            case R.id.iv_teaching:
                Toast.makeText(context, "了解精课", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_exit:
                initUser();
                break;
            case R.id.ll_dual_teacher:

                isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
                if (isDualTeacher.equals("0")){
                    iv_dual_teacher.setImageResource(R.drawable.no_dual_teacher);
                    spConfig.edit().putString(SPCommonInfoBean.isDualTeacher,"1").commit();

//                    AlertDialogUtil.showAlertDialog(context, "提示", "当前为双师模式，是否确定要切换成非双师模式", new AlertDialogUtil.ClickListener() {
//                        @Override
//                        public void positionClick() {
//                            iv_dual_teacher.setImageResource(R.drawable.no_dual_teacher);
//                            spConfig.edit().putString(SPCommonInfoBean.isDualTeacher,"1").commit();
//                        }
//
//                        @Override
//                        public void positionClick(String content) {
//
//                        }
//
//                        @Override
//                        public void negetiveClick() {
//
//                        }
//                    });
                }else {
                    iv_dual_teacher.setImageResource(R.drawable.dual_teacher);
                    spConfig.edit().putString(SPCommonInfoBean.isDualTeacher,"0").commit();
//                AlertDialogUtil.showAlertDialog(context, "提示", "当前为非双师模式，是否确定要切换成双师模式", new AlertDialogUtil.ClickListener() {
//                        @Override
//                        public void positionClick() {
//                            iv_dual_teacher.setImageResource(R.drawable.dual_teacher);
//                            spConfig.edit().putString(SPCommonInfoBean.isDualTeacher,"0").commit();
//                        }
//
//                        @Override
//                        public void positionClick(String content) {
//
//                        }
//
//                        @Override
//                        public void negetiveClick() {
//
//                        }
//                    });
                }
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
