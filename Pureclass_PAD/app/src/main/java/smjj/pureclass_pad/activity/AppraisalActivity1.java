package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.util.ActivityManage;
//测评换一页
public class AppraisalActivity1 extends BaseActivity implements View.OnClickListener{


    private TextView title_tv;
    private RelativeLayout rl_back, rl_home, rl_add, rl_back1;
    private LinearLayout ll_seven;
    private TextView onBack;
    private ImageView iv_appraisal;


    private Context context;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal1);

        ActivityManage.addActivity(this);

        mHandler = new Handler();
        context = this;


        findView();

    }
    private void findView(){
        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        ll_seven = (LinearLayout) findViewById(R.id.ll_appraisal);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_appraisal = (ImageView) findViewById(R.id.iv_appraisal);


        title_tv.setText("精课测评");



        rl_home.setVisibility(View.GONE);
        rl_back1.setVisibility(View.VISIBLE);
        ll_seven.setOnClickListener(this);
        onBack.setOnClickListener(this);
        rl_back1.setOnClickListener(this);
        iv_appraisal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_appraisal:
//                Intent intent = new Intent(this, AppraisalActivity.class);
//                startActivity(intent);
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.rl_back1:
                finish();
                break;
            case R.id.iv_appraisal:
                Intent intent = new Intent(this, AppraisalActivity.class);
                startActivity(intent);
                break;
        }

    }


}
