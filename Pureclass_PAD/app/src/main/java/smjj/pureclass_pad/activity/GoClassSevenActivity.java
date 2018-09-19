package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.util.ActivityManage;
//上课教学七步法界面展示
public class GoClassSevenActivity extends BaseActivity implements View.OnClickListener{
    private TextView title_tv;
    private RelativeLayout rl_back, rl_home, rl_add, rl_back1;
    private LinearLayout ll_seven;
    private TextView onBack;

    private Context context;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_class_seven);
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        context = this;

        //当计时结束,跳转至主界面
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLog();
            }
        }, 1000);


//        findView();
    }


//    private void findView(){
//        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
//        title_tv = (TextView) findViewById(R.id.common_title_tv);
//        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
//        ll_seven = (LinearLayout) findViewById(R.id.ll_seven);
//        onBack = (TextView) findViewById(R.id.onBack);
//
//        title_tv.setText("精课教学七步法");
//
//        rl_home.setVisibility(View.GONE);
//        rl_back1.setVisibility(View.VISIBLE);
//        ll_seven.setOnClickListener(this);
//        onBack.setOnClickListener(this);
//        rl_back1.setOnClickListener(this);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_seven:
//                Intent intent = new Intent(this, PrepareClassActivity.class);
//                intent.putExtra("enterMark","2");
//                startActivity(intent);
//                finish();
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.rl_back1:
                finish();
                break;
        }

    }

    //时间到后自动跳转到上课选班界面
    public void isLog(){
        Intent intent = new Intent(this, PrepareClassActivity1.class);
        intent.putExtra("enterMark","2");
        startActivity(intent);
        finish();
    }

}
