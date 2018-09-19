package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.FormatUtils;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

//已填写课堂反馈列表,测评的排课列表

public class FeedbackListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private XListView listView;
    private TextView tv_school_name;
    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private TextView tv_startTime, tv_endTime;

    private Context context;

    private List<String> listData;
    private CommonAdapter adapter;
    private String typleUser;
    private SharedPreferences spConfig;
    private Handler mHandler;

    private boolean isStartTime;
    private TimePickerView pvTime;
    private Calendar calendar;
    private String startTime, endTime;
    //1代表课堂反馈列表，2代表测评列表
    private String enterMark1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        context = this;
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);

        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        if (startTime == null || startTime.equals("")){
            startTime = FormatUtils.getPastDate(7);
        }
        if (endTime == null || endTime.equals("")){
            endTime = FormatUtils.getFetureDate(30);
        }
        setTime();

        findView();


    }

    private RelativeLayout rl_back, rl_home, rl_add;
    private void findView() {
        listView = (XListView) findViewById(R.id.scheduling_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);

        iv_home.setImageResource(R.drawable.search);
        title_tv.setText("课堂反馈");

        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        if (typleUser.equals("0")){
            tv_school_name.setText(R.string.school_name);
        }else {
            tv_school_name.setText(R.string.class_name);
        }

        listData = new ArrayList<>();
        listData.add("wwwwwwwwww");

        adapter = new CommonAdapter<String>(context, listData, R.layout.item_scheduling_lv) {
            @Override
            public void convert(ViewHolder holder, final String rowsBean) {
                int i = listData.indexOf(rowsBean);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                if (i % 2 == 0) {
                    ll_item.setBackgroundColor(Color.parseColor("#d1ecd8"));
                } else {
                    ll_item.setBackgroundColor(Color.parseColor("#bae2c4"));
                }
//                holder.setText(R.id.sd_no_tv, rowsBean.getClassId());
//                holder.setText(R.id.sd_schgegin_tv, rowsBean.getSchgegin());
//                holder.setText(R.id.sd_class_type_tv, rowsBean.getClassType());
//                holder.setText(R.id.sd_subjects_tv, rowsBean.getSubjects());
//                holder.setText(R.id.sd_grade_tv, rowsBean.getGname());
//                if (rowsBean.getTimeStar() != null && rowsBean.getTimeEnd() != null){
//                    holder.setText(R.id.sd_class_date_tv, rowsBean.getTimeStar().substring(0,10));
//                    holder.setText(R.id.sd_start_date_tv, rowsBean.getTimeStar().substring(11,16)
//                            + "-" + rowsBean.getTimeEnd().substring(11,16));
//                }

                holder.getView(R.id.sd_student_list).setVisibility(View.VISIBLE);

                holder.getView(R.id.sd_student_list).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialogUtil.showAlertDialog(context, "tisi", "wuxinxi");
//                        showStudentPopu();
//                        requestStudentData();//请求学生数据
                    }
                });

            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(false);
        rl_home.setOnClickListener(this);
        onBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                showSearchPopup();
                break;
            case R.id.onBack:
                finish();
                break;

        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra("enterMark", "2");
        startActivity(intent);

    }

    private void showSearchPopup(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_search, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,7,3,0.5f,R.style.popWindow_animation);

        tv_startTime = (TextView) view.findViewById(R.id.tv_start_search_tiem);
        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartTime = true;
                calendar = FormatUtils.getCalendarFromString(startTime);
                pvTime.show();

            }
        });

        tv_endTime  = (TextView) view.findViewById(R.id.tv_end_search_tiem);
        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartTime = false;
                calendar = FormatUtils.getCalendarFromString(endTime);
                pvTime.show();

            }
        });

        tv_startTime.setText(startTime);
        tv_endTime.setText(endTime);
        Button searchBt = (Button) view.findViewById(R.id.search_bt);
        searchBt.setText("查询已填");
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询
                popupWindow.dismiss();
            }
        });

        PopupWindowUtil.showAtLoactionRightAndBottom(iv_home);

    }


    private void setTime(){
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String stringTime = FormatUtils.getStringTime(date, "yyyy-MM-dd");
                if (isStartTime){
                    tv_startTime.setText(stringTime);
                    startTime = stringTime;
                }else {
                    tv_endTime.setText(stringTime);
                    endTime = stringTime;
                }
            }
        })

                .setLayoutRes(R.layout.pickerview_custom_time1, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true,true,true,false,false,false})
                .isDialog(true)
                .isCenterLabel(false)
                .setDividerColor(Color.RED)
                .setDate(calendar)
                .build();
    }



}
