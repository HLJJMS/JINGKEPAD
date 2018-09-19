package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import smjj.pureclass_pad.fragment.FeedbackFragment;
import smjj.pureclass_pad.fragment.SettingWorkFragment;
import smjj.pureclass_pad.fragment.StudentAnalysisFragment;
import smjj.pureclass_pad.fragment.TeachingReflectionFragment;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.FormatUtils;
import smjj.pureclass_pad.util.LoadingBox;
import smjj.pureclass_pad.util.PopupWindowUtil;
//课后界面
public class ClassAfterActivity extends BaseActivity implements View.OnClickListener{

    private TextView onBack;
    private ImageView iv_home, iv_back;
    private TextView title_tv;
    private TextView tv_startTime, tv_endTime;
    private Context context;
    private RelativeLayout rl_back, rl_home, rl_add;

    private Handler mHandler;
    private SharedPreferences spConfig;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RelativeLayout rl_setting_work, rl_teaching_reflection, rl_feedback, rl_student_analysis;
    private TextView tv_setting_work, tv_teaching_reflection, tv_feedback, tv_student_analysis, tv_onBack;
    private ImageView iv_teaching_reflection1, iv_feedback1;

    //声明碎片管理者
    private FragmentManager fragmentManager;
    private StudentAnalysisFragment studentAnalysisFragment;
    private FeedbackFragment feedbackFragment;
    private SettingWorkFragment settingWorkFragment;
    private TeachingReflectionFragment teachingReflectionFragment;

    private List<String> titleList;

    private String title = "布置作业";
    private String currentTitle = "";
    private List<String> filtrateList = new ArrayList<>();
    private String PWSelectTV = "";
    private CommonAdapter filtrateAdapter;

    //弹出popuwindow的标识，1代表布置作业，3课堂反馈， 4 学情分析
    private int popSign = 1;
    private boolean isStartTime;
    private TimePickerView pvTime;
    private Calendar calendar;
    private String startTime, endTime;

    private LoadingBox loadingBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_after);

        context = this;
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);

        startTime = FormatUtils.getPastDate(7);
        endTime = FormatUtils.getFetureDate(30);
        setTime();

        addTitleList();
        initFragment();
        initView();


    }


    //初始化菜单栏
    private void initView() {
        //初始化控件 及布局容器
        drawerLayout = (DrawerLayout) findViewById(R.id.class_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.class_navigationview);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);


        iv_home.setImageResource(R.drawable.function);
        iv_back.setImageResource(R.drawable.menu_bt);
        onBack.setVisibility(View.GONE);
        rl_back.setVisibility(View.VISIBLE);

        title_tv.setText(titleList.get(0));

        rl_back.setOnClickListener(this);
        rl_home.setOnClickListener(this);


        //设置点击登录 跳转登录界面
        View view = navigationView.getHeaderView(0);
        findView(view);
        initAdapter();

    }

    //查找菜单栏中的内容
    private void findView(View view){
        rl_setting_work = (RelativeLayout) view.findViewById(R.id.rl_setting_work);
        rl_teaching_reflection = (RelativeLayout) view.findViewById(R.id.rl_teaching_reflection);
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        rl_student_analysis = (RelativeLayout) view.findViewById(R.id.rl_student_analysis);
        tv_setting_work = (TextView) view.findViewById(R.id.tv_setting_work);
        tv_teaching_reflection = (TextView) view.findViewById(R.id.tv_teaching_reflection);
        tv_feedback = (TextView) view.findViewById(R.id.tv_feedback);
        tv_student_analysis = (TextView) view.findViewById(R.id.tv_student_analysis);
        iv_teaching_reflection1 = (ImageView) view.findViewById(R.id.iv_teaching_reflection1);
        iv_feedback1 = (ImageView) view.findViewById(R.id.iv_feedback1);
        tv_onBack = (TextView) view.findViewById(R.id.tv_onBack);



        rl_setting_work.setOnClickListener(this);
        rl_teaching_reflection.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_student_analysis.setOnClickListener(this);
        tv_onBack.setOnClickListener(this);

    }


    //初始化碎片
    private void initFragment() {
        //获取碎片管理者
        fragmentManager = getSupportFragmentManager();
        //开启碎片事物
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //处理事务 最后一次添加的为默认展示的碎片
        if (settingWorkFragment == null){
            settingWorkFragment = new SettingWorkFragment();
        }
        ft.add(R.id.class_framelayout, settingWorkFragment);

        //提交事务(汇报工作)
        ft.commit();

    }


    //展示指定碎片
    private void showFragmentAtPosition1(Fragment fragment) {
        //开启碎片事物
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //提交事物
        ft.replace(R.id.class_framelayout, fragment);
        ft.commit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_back:
                openOrCloseDrawerLayout();
                break;
            case R.id.rl_home:
                if (popSign == 1){
                    showPopupWindow();
                }else if (popSign == 3){
                    showSearchPopup();
                }else if (popSign == 2){

                    showSearchPopup();
                }
                break;
            case R.id.rl_setting_work:
                popSign = 1;
                PWSelectTV = "";
                setTextColor();
                title_tv.setText(titleList.get(0));
                rl_back.setVisibility(View.VISIBLE);
                iv_home.setImageResource(R.drawable.function);
                rl_home.setVisibility(View.VISIBLE);
                tv_setting_work.setTextColor(Color.parseColor("#f6aa00"));
                if (settingWorkFragment == null){
                    settingWorkFragment = new SettingWorkFragment();
                }
                showFragmentAtPosition1(settingWorkFragment);
                openOrCloseDrawerLayout();

                break;
            case R.id.rl_teaching_reflection:
                popSign = 2;
                setTextColor();
                title_tv.setText(titleList.get(1));
                rl_back.setVisibility(View.VISIBLE);
                iv_home.setImageResource(R.drawable.search);
                rl_home.setVisibility(View.VISIBLE);
                tv_teaching_reflection.setTextColor(Color.parseColor("#f6aa00"));
                if (teachingReflectionFragment == null){
                    teachingReflectionFragment = new TeachingReflectionFragment();
                }
                showFragmentAtPosition1(teachingReflectionFragment);
                openOrCloseDrawerLayout();
                break;
            case R.id.rl_feedback:
                popSign = 3;
                setTextColor();
                title_tv.setText(titleList.get(2));
                rl_back.setVisibility(View.VISIBLE);
                iv_home.setImageResource(R.drawable.search);
                rl_home.setVisibility(View.VISIBLE);
                tv_feedback.setTextColor(Color.parseColor("#f6aa00"));
                if (feedbackFragment == null){
                    feedbackFragment = new FeedbackFragment();
                }
                showFragmentAtPosition1(feedbackFragment);
                openOrCloseDrawerLayout();
                break;
            case R.id.rl_student_analysis:
                popSign = 4;
                setTextColor();
                title_tv.setText(titleList.get(3));
                rl_back.setVisibility(View.VISIBLE);
                iv_home.setImageResource(R.drawable.function);
                rl_home.setVisibility(View.INVISIBLE);
                tv_student_analysis.setTextColor(Color.parseColor("#f6aa00"));
                if (studentAnalysisFragment == null){
                    studentAnalysisFragment = new StudentAnalysisFragment();
                }
                showFragmentAtPosition1(studentAnalysisFragment);
                openOrCloseDrawerLayout();

                break;
            case R.id.tv_onBack:
                finish();
                break;
        }
    }


    private void showPopupWindow(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow1(context,view,7,3,0.5f,R.style.popWindow_animation);

        ListView listView = (ListView) view.findViewById(R.id.lv);


        listView.setAdapter(filtrateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!PWSelectTV.equals(filtrateList.get(i))){
                    PWSelectTV = filtrateList.get(i);
//                    fragmentCut(i);
                    if (settingWorkFragment != null){
                        if (PWSelectTV.equals(filtrateList.get(0))){
                            settingWorkFragment.refreshData(true);
                        }else {
                            settingWorkFragment.refreshData(false);
                        }
                    }
                }
                filtrateAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
        PopupWindowUtil.showAtLoactionRightAndBottom(iv_home);

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
//                requestData();
//                Intent intent = new Intent(context, FeedbackListActivity.class);
//                intent.putExtra("startTime", startTime);
//                intent.putExtra("endTime", endTime);
//                startActivity(intent);
                if (popSign == 3){
                    if (feedbackFragment != null){
                        feedbackFragment.refreshData(false, startTime, endTime);
                    }
                }else if (popSign == 2){
                    if (teachingReflectionFragment != null){
                        teachingReflectionFragment.refreshData(false, startTime, endTime);
                    }
                }

                popupWindow.dismiss();
            }
        });

        PopupWindowUtil.showAtLoactionRightAndBottom(iv_home);

    }


    /**
     * 打开关闭侧滑菜单
     */
    private void openOrCloseDrawerLayout(){
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void addTitleList(){
        titleList = new ArrayList<>();
        titleList.add("布置作业");
        titleList.add("教学反思");
        titleList.add("课堂反馈");
        titleList.add("学情分析");

    }

    private void initAdapter(){
        filtrateList.add("发布作业");
        filtrateList.add("查看作业");
        PWSelectTV = filtrateList.get(0);

        filtrateAdapter = new CommonAdapter<String>(context, filtrateList, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, String s) {
                TextView tv = holder.getView(R.id.item_pop_tv);
                tv.setText(s);
                if (PWSelectTV.equals("")){
                    PWSelectTV = filtrateList.get(0);
                }
                if (PWSelectTV.equals(s)) {
                    tv.setTextColor(Color.parseColor("#f6aa00"));
                } else {
                    tv.setTextColor(Color.parseColor("#666666"));
                }

            }
        };
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

    private void setTextColor(){
        tv_setting_work.setTextColor(Color.parseColor("#666666"));
        tv_teaching_reflection.setTextColor(Color.parseColor("#666666"));
        tv_feedback.setTextColor(Color.parseColor("#666666"));
        tv_student_analysis.setTextColor(Color.parseColor("#666666"));
    }


    public void showLoading() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this,null);
        }
        loadingBox.Show();
    }

    public void closeLoading() {
        if (loadingBox != null) {
            loadingBox.close();
            loadingBox = null;
        }
    }


    public void showLoading1() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this,null);
        }
        loadingBox.Show1();
    }

}
