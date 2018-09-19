package smjj.pureclass_pad.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.adapter.GoClassAdapter;
import smjj.pureclass_pad.beans.A001002Bean;
import smjj.pureclass_pad.beans.IQDetailsBean;
import smjj.pureclass_pad.beans.PrepareUrlBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.fragment.ClassConclusionFragment;
import smjj.pureclass_pad.fragment.ClassRecordFragment;
import smjj.pureclass_pad.fragment.DeepenUseFragment;
import smjj.pureclass_pad.fragment.ExercisesAnswerFragment;
import smjj.pureclass_pad.fragment.GoOutTestFragment;
import smjj.pureclass_pad.fragment.HappyFragment;
import smjj.pureclass_pad.fragment.InductionTestFragment;
import smjj.pureclass_pad.fragment.RankingFragment;
import smjj.pureclass_pad.fragment.RankingFragment1;
import smjj.pureclass_pad.fragment.SceneImportFragment;
import smjj.pureclass_pad.fragment.SignFragment;
import smjj.pureclass_pad.fragment.SignGroupFragment;
import smjj.pureclass_pad.fragment.SolidifyPracticeFragment;
import smjj.pureclass_pad.fragment.StudyNewFragment;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.LoadingBox;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.util.StatusBarUtils;
import smjj.pureclass_pad.view.VerticalChart1;
import smjj.pureclass_pad.view.redpacketview.RedPacketsLayout;
import smjj.pureclass_pad.view.redpacketview.RedPacketsSurfaceVew;

import static android.view.View.VISIBLE;
import static smjj.pureclass_pad.R.id.lv;

//上课界面
public class GoClassActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView endslid , startslid;
    private TextView onBack;
    private ImageView iv_home, iv_back;
    private TextView title_tv, tv_time;
    private Context context;
    private RelativeLayout rl_back, rl_home, rl_add;
    private GoClassAdapter endAdapter;
    private Handler mHandler;
    private SharedPreferences spConfig;
    //声明UI控件 及布局容器
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;//左侧滑菜单
    private NavigationView navigationView1;//右侧滑菜单
    //声明碎片管理者
    private FragmentManager fragmentManager;

    //声明碎片集合（优化）
    private List<Fragment> fragmentList;

    private List<String> titleList;

    private LinearLayout ll_sign_group, ll_induction_test, ll_scene_import, ll_study_new,ll_sign_groupstart,ll_ranking_start,ll_class_recordstart
           , ll_go_out_test, ll_class_record, ll_happy, ll_back, ll_responder, ll_red_packet,ll_happystart,ll_responderstart,ll_red_packetstart,ll_backstart;


    private Fragment signGroupFragment, sceneImportFragmetn, studyNewFragment, classConclusionFragment, rankingFragment, classRecordFragment, happyFragment, signFragment;

    private InductionTestFragment inductionTestFragment;
    private DeepenUseFragment deepenUseFragment;
    private SolidifyPracticeFragment solidifyPracticeFragment;
    private GoOutTestFragment goOutTestFragment;

    private Fragment moduleRankingFragment, moduleRankingFragment1, exercisesAnswerFragment, rankingFragment1;
    private List<A001002Bean.TablesBean.TableBean.RowsBean> listBean = new ArrayList<A001002Bean.TablesBean.TableBean.RowsBean>();
    private String title = "入门测";
    private String currentTitle = "1";
    private List<String> filtrateList = new ArrayList<>();
    private String PWSelectTV = "";
    private CommonAdapter filtrateAdapter;
    private String enterAnswerMark;

    public String classId;
    public String typleUser;
    public String userCode;
    private LoadingBox loadingBox;

    public String speakId, classNo, packageNo;
    public boolean canReleaseTest = true;//判断入门测、出门考、深化应用、巩固练习是否可以发布试题。
    private String grade, subject;

    public FloatingActionButton rightLowerButton;
    public FloatingActionMenu rightLowerMenu;
    public ImageView rlIcon1, rlIcon2, rlIcon3, rlIcon4, rlIcon5;
    public String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    public String isDualTeacher;//是否双师 0是双师 1非双师

    //红包布局
    private RedPacketsLayout redPacketsLayout;
    private RedPacketsSurfaceVew redPacketsSurfaceVew;

    private Timer timer1;
    private TimerTask timerTask;
    //是否展示计时 0不展示 1弹出框展示（大） 2小框展示
    private String strTimer = "0";
    private TextView tv_timer;
    //是否停止答题 0不是停止答题  1入门考停止代替 2出门考停止答题 3深化应用停止答题 4巩固练习停止答题
    public String isStopeAnswer = "0";

    //即时互动开始答题、停止答题、查看详情标识 0代表可以开始答题 1代表一开始要停止答题 2已停止可以查看答案详情
    private int iqMarker = 0;
    private String iq_test_no = "";

    //抢答开始答题、停止答题、查看详情标识 0代表可以开始抢答 1代表一开始要停止抢答 2已停止可以查看抢答详情
    private int responderMarker = 0;
    //判断是临时互动答题还是抢答题目 0代表临时答题， 1代表抢答， 2代表抢红包
    private int isIQ = 0;
    private String responder_test_no = "";

    //倒计时
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_class);
        context = this;
        ActivityManage.addActivity(this);
        endAdapter = new GoClassAdapter(GoClassActivity.this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate, "");
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher, "");

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor1);

//        redPacketsLayout = (RedPacketsLayout) findViewById(R.id.packets_layout);
        redPacketsSurfaceVew = (RedPacketsSurfaceVew) findViewById(R.id.bezier_surface);
        redPacketsSurfaceVew.setVisibility(View.GONE);


        grade = getIntent().getStringExtra("grade");
        subject = getIntent().getStringExtra("subject");
        speakId = getIntent().getStringExtra("SpeakId");
        classNo = getIntent().getStringExtra("ClassNo");
        packageNo = getIntent().getStringExtra("PackageNo");

        spConfig.edit().putString(SPCommonInfoBean.selectGrade, grade).commit();
        spConfig.edit().putString(SPCommonInfoBean.selectSubject, subject).commit();

        timer1 = new Timer();

        autoSkipButton();

        addTitleList();

        //初始化菜单
        initView();

        initFragment();

        startTimer();
    }

    //初始化菜单栏
    private void initView() {
        //初始化控件 及布局容器
        endslid = (RecyclerView) findViewById(R.id.endslid);
        endslid.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        endslid.setAdapter(endAdapter);
        startslid = (RecyclerView) findViewById(R.id.startslid);
        startslid.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        startslid.setAdapter(endAdapter);
        drawerLayout = (DrawerLayout) findViewById(R.id.class_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.class_navigationview);
//        navigationView1 = (NavigationView) findViewById(R.id.class_navigationview1);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        tv_time = (TextView) findViewById(R.id.tv_cancle_edit);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);






        typleUser = spConfig.getString(SPCommonInfoBean.typeUser, "");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        classId = spConfig.getString(SPCommonInfoBean.classId, "");

        iv_home.setImageResource(R.drawable.function);
        iv_back.setImageResource(R.drawable.menu_bt);
        onBack.setVisibility(View.GONE);
        rl_back.setVisibility(VISIBLE);
        rl_home.setVisibility(View.INVISIBLE);

        click();
        //设置标题加粗
        TextPaint tp = title_tv.getPaint();
        tp.setFakeBoldText(true);
        tp.setTextSize(30);
        title_tv.setText(titleList.get(0));

        TextPaint tp1 = tv_time.getPaint();
        tp1.setFakeBoldText(true);
        tp1.setTextSize(40);

        rl_back.setOnClickListener(this);
        rl_home.setOnClickListener(this);


        //设置点击登录 跳转登录界面
//        View view = navigationView.getHeaderView(0);
//        View view1 = navigationView1.getHeaderView(0);
//        findView(view);
//        findView1(view1);
        initAdapter();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (!currentTitle.equals(titleList.get(8)) && !currentTitle.equals(titleList.get(10))) {
                    closeFloatingMenu();
                    rightLowerButton.setVisibility(View.GONE);
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (!currentTitle.equals(titleList.get(8)) && !currentTitle.equals(titleList.get(10))) {
                    rightLowerButton.setVisibility(View.VISIBLE);
                }
                if (currentTitle.equals(titleList.get(7)) || currentTitle.equals(titleList.get(8))) {
                    rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.xiayibu1));
                } else {
                    rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.xiayibu));
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        openOrCloseDrawerLayout1();
        recycler();
    }

    //查找菜单栏中的内容
//    private void findView(View view){
//        ll_sign_group = (LinearLayout) view.findViewById(R.id.ll_sign_group);
//        ll_induction_test = (LinearLayout) view.findViewById(R.id.ll_induction_test);
//        ll_scene_import = (LinearLayout) view.findViewById(R.id.ll_scene_import);
//        ll_study_new = (LinearLayout) view.findViewById(R.id.ll_study_new);
//        ll_deepen_use = (LinearLayout) view.findViewById(R.id.ll_deepen_use);
//        ll_solidify_practice = (LinearLayout) view.findViewById(R.id.ll_solidify_practice);
//        ll_class_conclusion = (LinearLayout) view.findViewById(R.id.ll_class_conclusion);
//        ll_ranking = (LinearLayout) view.findViewById(R.id.ll_ranking);
//        ll_class_record = (LinearLayout) view.findViewById(R.id.ll_class_record);
//        ll_happy = (LinearLayout) view.findViewById(R.id.ll_happy);
//        ll_go_out_test = (LinearLayout) view.findViewById(R.id.ll_go_out_test);
//        ll_back = (LinearLayout) view.findViewById(R.id.ll_back);
//        ll_responder = (LinearLayout) view.findViewById(R.id.ll_responder);
//        ll_red_packet = (LinearLayout) view.findViewById(R.id.ll_red_packet);
//
//        tv_sign_group = (TextView) view.findViewById(R.id.tv_sign_group);
//        tv_induction_test = (TextView) view.findViewById(R.id.tv_induction_test);
//        tv_scene_import = (TextView) view.findViewById(R.id.tv_scene_import);
//        tv_study_new = (TextView) view.findViewById(R.id.tv_study_new);
//        tv_deepen_use = (TextView) view.findViewById(R.id.tv_deepen_use);
//        tv_solidify_practice = (TextView) view.findViewById(R.id.tv_solidify_practice);
//        tv_class_conclusion = (TextView) view.findViewById(R.id.tv_class_conclusion);
//        tv_ranking = (TextView) view.findViewById(R.id.tv_ranking);
//        tv_class_record = (TextView) view.findViewById(R.id.tv_class_record);
//        tv_happy = (TextView) view.findViewById(R.id.tv_happy);
//        tv_go_out_test = (TextView) view.findViewById(R.id.tv_go_out_test);
//        tv_onback = (TextView) view.findViewById(R.id.tv_onBack);
//        tv_responder = (TextView) view.findViewById(R.id.tv_responder);
//        tv_red_packet = (TextView) view.findViewById(R.id.tv_red_packet);
//
//        tv_sign_group.setTextColor(Color.parseColor("#f6aa00"));
//        ll_class_record.setVisibility(View.GONE);
////        ll_ranking.setVisibility(View.GONE);
//
//        //即是双师又是助教不能开始抢红包和抢答
//        if (isDualTeacher.equals("0") && isCertificate.equals("1")){
//            ll_responder.setVisibility(View.GONE);
//            ll_red_packet.setVisibility(View.GONE);
//        }
//
//
//        ll_sign_group.setOnClickListener(this);
//        ll_induction_test.setOnClickListener(this);
//        ll_scene_import.setOnClickListener(this);
//        ll_study_new.setOnClickListener(this);
//        ll_deepen_use.setOnClickListener(this);
//        ll_solidify_practice.setOnClickListener(this);
//        ll_class_conclusion.setOnClickListener(this);
//        ll_ranking.setOnClickListener(this);
//        ll_happy.setOnClickListener(this);
//        ll_go_out_test.setOnClickListener(this);
//        ll_class_record.setOnClickListener(this);
//        ll_back.setOnClickListener(this);
//        ll_responder.setOnClickListener(this);
//        ll_red_packet.setOnClickListener(this);
//
//    }

    //查找菜单栏中的内容
//    private void findView1(View view){
//        ll_sign_group1 = (LinearLayout) view.findViewById(R.id.ll_sign_group1);
//        ll_induction_test1 = (LinearLayout) view.findViewById(R.id.ll_induction_test1);
//        ll_scene_import1 = (LinearLayout) view.findViewById(R.id.ll_scene_import1);
//        ll_study_new1 = (LinearLayout) view.findViewById(R.id.ll_study_new1);
//        ll_deepen_use1 = (LinearLayout) view.findViewById(R.id.ll_deepen_use1);
//        ll_solidify_practice1 = (LinearLayout) view.findViewById(R.id.ll_solidify_practice1);
//        ll_class_conclusion1 = (LinearLayout) view.findViewById(R.id.ll_class_conclusion1);
//        ll_ranking1 = (LinearLayout) view.findViewById(R.id.ll_ranking1);
//        ll_class_record1 = (LinearLayout) view.findViewById(R.id.ll_class_record1);
//        ll_happy1 = (LinearLayout) view.findViewById(R.id.ll_happy1);
//        ll_go_out_test1 = (LinearLayout) view.findViewById(R.id.ll_go_out_test1);
//        ll_back1 = (LinearLayout) view.findViewById(R.id.ll_back1);
//        ll_responder1 = (LinearLayout) view.findViewById(R.id.ll_responder1);
//        ll_red_packet1 = (LinearLayout) view.findViewById(R.id.ll_red_packet1);
//
//        tv_sign_group1 = (TextView) view.findViewById(R.id.tv_sign_group1);
//        tv_induction_test1 = (TextView) view.findViewById(R.id.tv_induction_test1);
//        tv_scene_import1 = (TextView) view.findViewById(R.id.tv_scene_import1);
//        tv_study_new1 = (TextView) view.findViewById(R.id.tv_study_new1);
//        tv_deepen_use1 = (TextView) view.findViewById(R.id.tv_deepen_use1);
//        tv_solidify_practice1 = (TextView) view.findViewById(R.id.tv_solidify_practice1);
//        tv_class_conclusion1 = (TextView) view.findViewById(R.id.tv_class_conclusion1);
//        tv_ranking1 = (TextView) view.findViewById(R.id.tv_ranking1);
//        tv_class_record1 = (TextView) view.findViewById(R.id.tv_class_record1);
//        tv_happy1 = (TextView) view.findViewById(R.id.tv_happy1);
//        tv_go_out_test1 = (TextView) view.findViewById(R.id.tv_go_out_test1);
//        tv_onback1 = (TextView) view.findViewById(R.id.tv_onBack1);
//        tv_responder1 = (TextView) view.findViewById(R.id.tv_responder1);
//        tv_red_packet1 = (TextView) view.findViewById(R.id.tv_red_packet1);
//
//        tv_sign_group1.setTextColor(Color.parseColor("#f6aa00"));
//        ll_class_record1.setVisibility(View.GONE);
////        ll_ranking1.setVisibility(View.GONE);
//
//        //即是双师又是助教不能开始抢红包和抢答
//        if (isDualTeacher.equals("0") && isCertificate.equals("1")){
//            ll_responder1.setVisibility(View.GONE);
//            ll_red_packet1.setVisibility(View.GONE);
//        }
//
//        ll_sign_group1.setOnClickListener(this);
//        ll_induction_test1.setOnClickListener(this);
//        ll_scene_import1.setOnClickListener(this);
//        ll_study_new1.setOnClickListener(this);
//        ll_deepen_use1.setOnClickListener(this);
//        ll_solidify_practice1.setOnClickListener(this);
//        ll_class_conclusion1.setOnClickListener(this);
//        ll_ranking1.setOnClickListener(this);
//        ll_happy1.setOnClickListener(this);
//        ll_go_out_test1.setOnClickListener(this);
//        ll_class_record1.setOnClickListener(this);
//        ll_back1.setOnClickListener(this);
//        ll_responder1.setOnClickListener(this);
//        ll_red_packet1.setOnClickListener(this);
//
//    }


    //初始化碎片
    private void initFragment() {
        //获取碎片管理者
        fragmentManager = getSupportFragmentManager();
        //开启碎片事物
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //处理事务 最后一次添加的为默认展示的碎片
        if (isDualTeacher.equals("0") && isCertificate.equals("0")) {
            //双师主教
            ft.add(R.id.class_framelayout, new SignFragment());
        } else {
            ft.add(R.id.class_framelayout, new SignGroupFragment());
        }


        //提交事务(汇报工作)
        ft.commit();

    }

    //老板   点击事件侧滑
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.rl_back:
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.rl_home:
//                showPopupWindow();
//
//                break;
//            case R.id.ll_sign_group:
//                commonSet(0);
////                tv_sign_group.setTextColor(Color.parseColor("#f6aa00"));
////                tv_sign_group1.setTextColor(Color.parseColor("#f6aa00"));
//                if (isDualTeacher.equals("0") && isCertificate.equals("0")) {
//                    //双师主教
//                    if (signFragment == null) {
//                        signFragment = new SignFragment();
//                    }
//                    showFragmentAtPosition1(signFragment);
//                } else {
//                    if (signGroupFragment == null) {
//                        signGroupFragment = new SignGroupFragment();
//                    }
//                    showFragmentAtPosition1(signGroupFragment);
//                }
//
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_induction_test:
//                commonSet(1);
////                tv_induction_test.setTextColor(Color.parseColor("#f6aa00"));
////                tv_induction_test1.setTextColor(Color.parseColor("#f6aa00"));
//                if (inductionTestFragment == null) {
//                    inductionTestFragment = new InductionTestFragment();
//                }
//                showFragmentAtPosition1(inductionTestFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_scene_import:
//                commonSet(2);
////                tv_scene_import.setTextColor(Color.parseColor("#f6aa00"));
////                tv_scene_import1.setTextColor(Color.parseColor("#f6aa00"));
//                if (sceneImportFragmetn == null) {
//                    sceneImportFragmetn = new SceneImportFragment();
//                }
//
//                showFragmentAtPosition1(sceneImportFragmetn);
//                openOrCloseDrawerLayout();
//
//                break;
//            case R.id.ll_study_new:
//                commonSet(3);
////                tv_study_new.setTextColor(Color.parseColor("#f6aa00"));
////                tv_study_new1.setTextColor(Color.parseColor("#f6aa00"));
//                if (studyNewFragment == null) {
//                    studyNewFragment = new StudyNewFragment();
//                }
//                showFragmentAtPosition1(studyNewFragment);
//                openOrCloseDrawerLayout();
//
//                break;
//            case R.id.ll_deepen_use:
//                commonSet(4);
////                tv_deepen_use.setTextColor(Color.parseColor("#f6aa00"));
////                tv_deepen_use1.setTextColor(Color.parseColor("#f6aa00"));
//                if (deepenUseFragment == null) {
//                    deepenUseFragment = new DeepenUseFragment();
//                }
//
//                showFragmentAtPosition1(deepenUseFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_solidify_practice:
//                commonSet(5);
////                tv_solidify_practice.setTextColor(Color.parseColor("#f6aa00"));
////                tv_solidify_practice1.setTextColor(Color.parseColor("#f6aa00"));
//                if (solidifyPracticeFragment == null) {
//                    solidifyPracticeFragment = new SolidifyPracticeFragment();
//
//                }
//
//                showFragmentAtPosition1(solidifyPracticeFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_class_conclusion:
//                commonSet(6);
////                tv_class_conclusion.(Color.parseColor("#f6aa00"));
////                tv_class_conclusion1.setTextColor(Color.parseColor("#f6aa00"));
//                if (classConclusionFragment == null) {
//                    classConclusionFragment = new ClassConclusionFragment();
//                }
//
//                showFragmentAtPosition1(classConclusionFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_go_out_test:
//                commonSet(7);
//
////                tv_go_out_test.setTextColor(Color.parseColor("#f6aa00"));
////                tv_go_out_test1.setTextColor(Color.parseColor("#f6aa00"));
//                if (goOutTestFragment == null) {
//                    goOutTestFragment = new GoOutTestFragment();
//                }
//                showFragmentAtPosition1(goOutTestFragment);
//                openOrCloseDrawerLayout();
//
//                break;
//            case R.id.ll_ranking:
//                commonSet(8);
//
////                tv_ranking.setTextColor(Color.parseColor("#f6aa00"));
////                tv_ranking1.setTextColor(Color.parseColor("#f6aa00"));
//                if (rankingFragment == null) {
//                    rankingFragment = new RankingFragment();
//                }
//
//                showFragmentAtPosition1(rankingFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_class_record:
//                commonSet(9);
//
////                tv_class_record.setTextColor(Color.parseColor("#f6aa00"));
////                tv_class_record1.setTextColor(Color.parseColor("#f6aa00"));
//                if (classRecordFragment == null) {
//                    classRecordFragment = new ClassRecordFragment();
//                }
//                showFragmentAtPosition1(classRecordFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_happy:
//                commonSet(10);
////                tv_happy.setTextColor(Color.parseColor("#f6aa00"));
////                tv_happy1.setTextColor(Color.parseColor("#f6aa00"));
//                if (happyFragment == null) {
//                    happyFragment = new HappyFragment();
//                }
//
//                showFragmentAtPosition1(happyFragment);
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_sign_group1:
//                commonSet(0);
////                tv_sign_group.setTextColor(Color.parseColor("#f6aa00"));
////                tv_sign_group1.setTextColor(Color.parseColor("#f6aa00"));
//                if (isDualTeacher.equals("0") && isCertificate.equals("0")) {
//                    //双师主教
//                    if (signFragment == null) {
//                        signFragment = new SignFragment();
//                    }
//                    showFragmentAtPosition1(signFragment);
//                } else {
//                    if (signGroupFragment == null) {
//                        signGroupFragment = new SignGroupFragment();
//                    }
//                    showFragmentAtPosition1(signGroupFragment);
//                }
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_induction_test1:
//                commonSet(1);
////                tv_induction_test.setTextColor(Color.parseColor("#f6aa00"));
////                tv_induction_test1.setTextColor(Color.parseColor("#f6aa00"));
//                if (inductionTestFragment == null) {
//                    inductionTestFragment = new InductionTestFragment();
//                }
//                showFragmentAtPosition1(inductionTestFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_scene_import1:
//                commonSet(2);
////                tv_scene_import.setTextColor(Color.parseColor("#f6aa00"));
////                tv_scene_import1.setTextColor(Color.parseColor("#f6aa00"));
//                if (sceneImportFragmetn == null) {
//                    sceneImportFragmetn = new SceneImportFragment();
//                }
//
//                showFragmentAtPosition1(sceneImportFragmetn);
//                openOrCloseDrawerLayout1();
//
//                break;
//            case R.id.ll_study_new1:
//                commonSet(3);
////                tv_study_new.setTextColor(Color.parseColor("#f6aa00"));
////                tv_study_new1.setTextColor(Color.parseColor("#f6aa00"));
//                if (studyNewFragment == null) {
//                    studyNewFragment = new StudyNewFragment();
//                }
//                showFragmentAtPosition1(studyNewFragment);
//                openOrCloseDrawerLayout1();
//
//                break;
//            case R.id.ll_deepen_use1:
//                commonSet(4);
////                tv_deepen_use.setTextColor(Color.parseColor("#f6aa00"));
////                tv_deepen_use1.setTextColor(Color.parseColor("#f6aa00"));
//                if (deepenUseFragment == null) {
//                    deepenUseFragment = new DeepenUseFragment();
//                }
//
//                showFragmentAtPosition1(deepenUseFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_solidify_practice1:
//                commonSet(5);
////                tv_solidify_practice.setTextColor(Color.parseColor("#f6aa00"));
////                tv_solidify_practice1.setTextColor(Color.parseColor("#f6aa00"));
//                if (solidifyPracticeFragment == null) {
//                    solidifyPracticeFragment = new SolidifyPracticeFragment();
//                }
//
//                showFragmentAtPosition1(solidifyPracticeFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_class_conclusion1:
//                commonSet(6);
////                tv_class_conclusion.setTextColor(Color.parseColor("#f6aa00"));
////                tv_class_conclusion1.setTextColor(Color.parseColor("#f6aa00"));
//                if (classConclusionFragment == null) {
//                    classConclusionFragment = new ClassConclusionFragment();
//                }
//
//                showFragmentAtPosition1(classConclusionFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_go_out_test1:
//                commonSet(7);
////                tv_go_out_test.setTextColor(Color.parseColor("#f6aa00"));
////                tv_go_out_test1.setTextColor(Color.parseColor("#f6aa00"));
//                if (goOutTestFragment == null) {
//                    goOutTestFragment = new GoOutTestFragment();
//                }
//                showFragmentAtPosition1(goOutTestFragment);
//                openOrCloseDrawerLayout1();
//
//                break;
//            case R.id.ll_ranking1:
//                commonSet(8);
////                tv_ranking.setTextColor(Color.parseColor("#f6aa00"));
////                tv_ranking1.setTextColor(Color.parseColor("#f6aa00"));
//                if (rankingFragment == null) {
//                    rankingFragment = new RankingFragment();
//                }
//
//                showFragmentAtPosition1(rankingFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_class_record1:
//                commonSet(9);
////                tv_class_record.setTextColor(Color.parseColor("#f6aa00"));
////                tv_class_record1.setTextColor(Color.parseColor("#f6aa00"));
//                if (classRecordFragment == null) {
//                    classRecordFragment = new ClassRecordFragment();
//                }
//                showFragmentAtPosition1(classRecordFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_happy1:
//                commonSet(10);
////                tv_happy.setTextColor(Color.parseColor("#f6aa00"));
////                tv_happy1.setTextColor(Color.parseColor("#f6aa00"));
//                if (happyFragment == null) {
//                    happyFragment = new HappyFragment();
//                }
//                showFragmentAtPosition1(happyFragment);
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_back:
//                finish();
//                break;
//            case R.id.ll_back1:
//                finish();
//                ll_responder1.setOnClickListener(this);
//                ll_red_packet1.setOnClickListener(this);
//                break;
//            case R.id.ll_responder:
//                isIQ = 1;
//                showPopWinResponder();
//
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_responder1:
//                isIQ = 1;
//                showPopWinResponder();
//                openOrCloseDrawerLayout1();
//                break;
//            case R.id.ll_red_packet:
//                isIQ = 2;
//                startAnswerIQ();
//
//                openOrCloseDrawerLayout();
//                break;
//            case R.id.ll_red_packet1:
//                isIQ = 2;
//                startAnswerIQ();
//
//                openOrCloseDrawerLayout1();
//                break;
//        }
//    }


    /**
     * 抢答弹出框
     */

    private Button responder_start_bt;
    private Button responder_stop_bt;
    private Button responder_release_bt;

    private void showPopWinResponder() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_respinder, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow2(context, view, 2, 5, 0.5f, -1);

        responder_start_bt = (Button) view.findViewById(R.id.responder_start_bt);
        responder_stop_bt = (Button) view.findViewById(R.id.responder_stop_bt);
        responder_release_bt = (Button) view.findViewById(R.id.responder_release_bt);

        if (responderMarker == 0) {
            responder_start_bt.setEnabled(true);
            responder_start_bt.setBackgroundResource(R.drawable.responder_select);
            responder_release_bt.setEnabled(false);
            responder_release_bt.setBackgroundResource(R.drawable.responder_select_no);
            responder_stop_bt.setEnabled(false);
            responder_stop_bt.setBackgroundResource(R.drawable.responder_select_no);


        } else if (responderMarker == 1) {
            responder_start_bt.setEnabled(false);
            responder_start_bt.setBackgroundResource(R.drawable.responder_select_no);
            responder_stop_bt.setEnabled(true);
            responder_stop_bt.setBackgroundResource(R.drawable.responder_select);
            responder_release_bt.setEnabled(false);
            responder_release_bt.setBackgroundResource(R.drawable.responder_select_no);

        } else {
            responder_start_bt.setEnabled(false);
            responder_start_bt.setBackgroundResource(R.drawable.responder_select_no);
            responder_release_bt.setEnabled(true);
            responder_release_bt.setBackgroundResource(R.drawable.responder_select);
            responder_stop_bt.setEnabled(false);
            responder_stop_bt.setBackgroundResource(R.drawable.responder_select_no);
        }

        responder_start_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responderMarker = 1;

                startAnswerIQ();

            }
        });

        responder_stop_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responderMarker = 2;
                stopAnsweIQ();
            }
        });

        //查看试题详情
        responder_release_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responderMarker = 0;
                popupWindow.dismiss();
//                showPopWinIQ1();
//                getIQDetails();
                Intent intent = new Intent(GoClassActivity.this, ResponderRankingActivity.class);
                intent.putExtra("TestNo", iq_test_no);
                startActivity(intent);


            }
        });


        popupWindow.showAtLocation(iv_home, Gravity.CENTER, 0, 0);

    }

    //展示指定碎片
    private void showFragmentAtPosition1(Fragment fragment) {
        //开启碎片事物
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //提交事物
        ft.replace(R.id.class_framelayout, fragment);
        ft.commit();

    }


    private void showPopupWindow() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow1(context, view, 7, 3, 0.5f, R.style.popWindow_animation);

        ListView listView = (ListView) view.findViewById(lv);


        listView.setAdapter(filtrateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!PWSelectTV.equals(filtrateList.get(i))) {
                    PWSelectTV = filtrateList.get(i);
                    fragmentCut(i);
                }
                filtrateAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
        PopupWindowUtil.showAtLoactionRightAndBottom(iv_home);

    }

    //展示计时器
    private void showTimerPopu() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_timer, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context, view, 2, 2, 0.5f, -1);

        tv_timer = (TextView) view.findViewById(R.id.tv_timer);
        ImageView closeImage = (ImageView) view.findViewById(R.id.iv_close_pop);
        ImageView iv_least = (ImageView) view.findViewById(R.id.iv_least);
        ImageView iv_stop = (ImageView) view.findViewById(R.id.iv_stop);

        iv_least.setVisibility(View.GONE);

        iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStopeAnswer.equals("0")) {
                    startTimer();
                } else {
                    //停止答题如果成功
                    if (isDualTeacher.equals("0")) {
                        stopAnswer();
                    } else {
                        startTimer();
                    }

                }
            }
        });

        iv_least.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

            }
        });

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(iv_home, Gravity.CENTER, 0, 0);

    }

    /**
     * 选择popuwindow中的item进行界面跳转
     *
     * @param i
     */
    private void fragmentCut(int i) {
        if (currentTitle.equals(titleList.get(1))) {
            //入门测
            if (i == 0) {
                //发布试题
                if (inductionTestFragment == null) {
                    inductionTestFragment = new InductionTestFragment();
                }

                showFragmentAtPosition1(inductionTestFragment);
//            }else if (i == 1){
//                if (moduleRankingFragment == null){
//                    moduleRankingFragment = new ModuleRankingFragment();
//                }
//                Bundle bundle=new Bundle();
//                bundle.putString("enterMark","1");
//                moduleRankingFragment.setArguments(bundle);
//                showFragmentAtPosition1(moduleRankingFragment);
//            }else if (i == 2){
//                if (moduleRankingFragment1 == null){
//                    moduleRankingFragment1 = new ModuleRankingFragment1();
//                }
//                Bundle bundle=new Bundle();
//                bundle.putString("enterMark","2");
//                moduleRankingFragment1.setArguments(bundle);
//                showFragmentAtPosition1(moduleRankingFragment1);
            } else {
                if (exercisesAnswerFragment == null) {
                    exercisesAnswerFragment = new ExercisesAnswerFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("enterAnswerMark", "1");
                exercisesAnswerFragment.setArguments(bundle);
                showFragmentAtPosition1(exercisesAnswerFragment);

            }
        } else if (currentTitle.equals(titleList.get(4))) {
            //深化应用
            if (i == 0) {
                if (deepenUseFragment == null) {
                    deepenUseFragment = new DeepenUseFragment();
                }

                showFragmentAtPosition1(deepenUseFragment);
            } else if (i == 1) {
                if (exercisesAnswerFragment == null) {
                    exercisesAnswerFragment = new ExercisesAnswerFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("enterAnswerMark", "4");

                exercisesAnswerFragment.setArguments(bundle);
                showFragmentAtPosition1(exercisesAnswerFragment);
            }
        } else if (currentTitle.equals(titleList.get(5))) {
            //巩固练习
            if (i == 0) {
                if (solidifyPracticeFragment == null) {
                    solidifyPracticeFragment = new SolidifyPracticeFragment();
                }

                showFragmentAtPosition1(solidifyPracticeFragment);
            } else if (i == 1) {
                if (exercisesAnswerFragment == null) {
                    exercisesAnswerFragment = new ExercisesAnswerFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("enterAnswerMark", "3");
                exercisesAnswerFragment.setArguments(bundle);
                showFragmentAtPosition1(exercisesAnswerFragment);
            }
        } else if (currentTitle.equals(titleList.get(7))) {
            //出门考
            if (i == 0) {
                if (goOutTestFragment == null) {
                    goOutTestFragment = new GoOutTestFragment();
                }
                showFragmentAtPosition1(goOutTestFragment);
//            }else if (i == 1){
//                if (moduleRankingFragment == null){
//                    moduleRankingFragment = new ModuleRankingFragment();
//                }
//                Bundle bundle=new Bundle();
//                bundle.putString("enterMark","3");
//                bundle.putString("Grade",grade);
//                bundle.putString("Subject",subject);
//                moduleRankingFragment.setArguments(bundle);
//                showFragmentAtPosition1(moduleRankingFragment);
//            }else if (i == 2){
//                if (moduleRankingFragment1 == null){
//                    moduleRankingFragment1 = new ModuleRankingFragment1();
//                }
//                Bundle bundle=new Bundle();
//                bundle.putString("enterMark","4");
//                moduleRankingFragment1.setArguments(bundle);
//                showFragmentAtPosition1(moduleRankingFragment1);
            } else {
                if (exercisesAnswerFragment == null) {
                    exercisesAnswerFragment = new ExercisesAnswerFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("enterAnswerMark", "2");
                exercisesAnswerFragment.setArguments(bundle);
                showFragmentAtPosition1(exercisesAnswerFragment);
            }
        } else if (currentTitle.equals(titleList.get(8))) {
            //排名
            if (i == 0) {
                if (rankingFragment == null) {
                    rankingFragment = new RankingFragment();
                }
                showFragmentAtPosition1(rankingFragment);
            } else if (i == 1) {
                if (rankingFragment1 == null) {
                    rankingFragment1 = new RankingFragment1();
                }
                showFragmentAtPosition1(rankingFragment1);
            }
        }
    }

    private void initAdapter() {
        //第一次调用该方法时，默认入门测的jih
        filtrateList.add("查看试题");
//        filtrateList.add("学生排名");
//        filtrateList.add("小组排名");
        filtrateList.add("答案解析");
        PWSelectTV = filtrateList.get(0);

        filtrateAdapter = new CommonAdapter<String>(context, filtrateList, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, String s) {
                TextView tv = holder.getView(R.id.item_pop_tv);
                tv.setText(s);
                if (PWSelectTV.equals("")) {
                    PWSelectTV = filtrateList.get(0);
                }
                if (PWSelectTV.equals(s)) {
//                    tv.setTextColor(Color.parseColor("#f6aa00"));
                } else {
//                    tv.setTextColor(Color.parseColor("#666666"));
                }

            }
        };

    }

    /**
     * 悬浮按钮自动跳转
     */
    private void autoSkipButton() {

        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);


        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.dakai));


        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconNew.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);


        rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.lucency)
                .setLayoutParams(starParams)
                .build();
        rightLowerButton.setVisibility(View.GONE);

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.lucency));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        rLSubBuilder.setLayoutParams(blueContentParams);
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        rLSubBuilder.setLayoutParams(blueParams);

        rlIcon1 = new ImageView(this);
        rlIcon2 = new ImageView(this);
        rlIcon3 = new ImageView(this);
        rlIcon4 = new ImageView(this);
        rlIcon5 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.xiayibu));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.diaochujiangyi));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.timer));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
        rlIcon5.setImageDrawable(getResources().getDrawable(R.drawable.quiz1));

        rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
                .setRadius(redActionMenuRadius)
                .attachTo(rightLowerButton)
                .build();


        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });


        FMenuSetClick();

    }

    /**
     * 悬浮菜单设置点击事件
     */
    private void FMenuSetClick() {


        rlIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (currentTitle.equals(titleList.get(0))) {
                    commonSet(1);
//                    tv_induction_test.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_induction_test1.setTextColor(Color.parseColor("#f6aa00"));
                    if (inductionTestFragment == null) {
                        inductionTestFragment = new InductionTestFragment();
                    }
                    showFragmentAtPosition1(inductionTestFragment);
                } else if (currentTitle.equals(titleList.get(1))) {
                    commonSet(2);

//                    tv_scene_import.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_scene_import1.setTextColor(Color.parseColor("#f6aa00"));
                    if (sceneImportFragmetn == null) {
                        sceneImportFragmetn = new SceneImportFragment();
                    }

                    showFragmentAtPosition1(sceneImportFragmetn);

                } else if (currentTitle.equals(titleList.get(2))) {
                    commonSet(3);
//                    tv_study_new.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_study_new1.setTextColor(Color.parseColor("#f6aa00"));
                    if (studyNewFragment == null) {
                        studyNewFragment = new StudyNewFragment();
                    }

                    showFragmentAtPosition1(studyNewFragment);
                } else if (currentTitle.equals(titleList.get(3))) {
                    commonSet(4);
//                    tv_deepen_use.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_deepen_use1.setTextColor(Color.parseColor("#f6aa00"));
                    if (deepenUseFragment == null) {
                        deepenUseFragment = new DeepenUseFragment();

                    }

                    showFragmentAtPosition1(deepenUseFragment);
                } else if (currentTitle.equals(titleList.get(4))) {
                    commonSet(5);
//                    tv_solidify_practice.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_solidify_practice1.setTextColor(Color.parseColor("#f6aa00"));
                    if (solidifyPracticeFragment == null) {
                        solidifyPracticeFragment = new SolidifyPracticeFragment();

                    }

                    showFragmentAtPosition1(solidifyPracticeFragment);
                } else if (currentTitle.equals(titleList.get(5))) {
                    commonSet(6);
//                    tv_class_conclusion.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_class_conclusion1.setTextColor(Color.parseColor("#f6aa00"));
                    if (classConclusionFragment == null) {
                        classConclusionFragment = new ClassConclusionFragment();
                    }

                    showFragmentAtPosition1(classConclusionFragment);
                } else if (currentTitle.equals(titleList.get(6))) {
                    commonSet(7);
//                    tv_go_out_test.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_go_out_test1.setTextColor(Color.parseColor("#f6aa00"));
                    if (goOutTestFragment == null) {
                        goOutTestFragment = new GoOutTestFragment();

                    }

                    showFragmentAtPosition1(goOutTestFragment);
                } else if (currentTitle.equals("1")) {
                    currentTitle = titleList.get(0);
                    commonSet(1);
//                    tv_induction_test.setTextColor(Color.parseColor("#f6aa00"));
//                    tv_induction_test1.setTextColor(Color.parseColor("#f6aa00"));
                    if (inductionTestFragment == null) {
                        inductionTestFragment = new InductionTestFragment();

                    }
                    showFragmentAtPosition1(inductionTestFragment);
                }
            }
        });

        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentUrl();
            }
        });

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (canReleaseTest) {
                    if (currentTitle.equals(titleList.get(1))) {
                        if (inductionTestFragment != null) {
                            if (isDualTeacher.equals("0")) {
                                inductionTestFragment.releaseInductionTest();
                            } else {
                                inductionTestFragment.checkSign();
                            }


                        }

                    } else if (currentTitle.equals(titleList.get(4))) {
                        if (deepenUseFragment != null) {
                            if (isDualTeacher.equals("0")) {
                                deepenUseFragment.releaseDeepenUseTest();
                            } else {
                                deepenUseFragment.checkSign();
                            }
                        }

                    } else if (currentTitle.equals(titleList.get(5))) {
                        if (solidifyPracticeFragment != null) {
                            if (isDualTeacher.equals("0")) {
                                solidifyPracticeFragment.releaseSolidifyPracticeTest();
                            } else {
                                solidifyPracticeFragment.checkSign();
                            }

                        }

                    } else if (currentTitle.equals(titleList.get(7))) {
                        if (goOutTestFragment != null) {
                            if (isDualTeacher.equals("0")) {
                                goOutTestFragment.releaseGoOutTest();
                            } else {
                                goOutTestFragment.checkSign();
                            }

                        }
                    }
                }

            }
        });

        rlIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strTimer.equals("0")) {
                    strTimer = "1";
                    showTimerPopu();
                } else {
                    showTimerPopu();
                }

            }
        });


        rlIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "我问你答", Toast.LENGTH_SHORT).show();
                isIQ = 0;
                showPopWinIQ();
            }
        });


    }

    private Button iq_start_bt;
    private Button iq_stop_bt;
    private Button iq_release_bt;
    private VerticalChart1 verticalChart1;
    private TextView textView;
    private TextView tv_null;

    /**
     * 弹出即时互动开始答题弹框
     */
    private void showPopWinIQ() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_iq, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow2(context, view, 2, 10, 0.5f, -1);

        iq_start_bt = (Button) view.findViewById(R.id.iq_start_bt);
        iq_stop_bt = (Button) view.findViewById(R.id.iq_stop_bt);
        iq_release_bt = (Button) view.findViewById(R.id.iq_release_bt);
        verticalChart1 = (VerticalChart1) view.findViewById(R.id.vc);
        textView = (TextView) view.findViewById(R.id.tv_select_correct);
        tv_null = (TextView) view.findViewById(R.id.tv_null);

        if (iqMarker == 0) {
            iq_start_bt.setEnabled(true);
            iq_start_bt.setBackgroundResource(R.drawable.shape_bg);
            iq_release_bt.setEnabled(false);
            iq_release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            iq_stop_bt.setEnabled(false);
            iq_stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            verticalChart1.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);

        } else if (iqMarker == 1) {
            iq_start_bt.setEnabled(false);
            iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            iq_stop_bt.setEnabled(true);
            iq_stop_bt.setBackgroundResource(R.drawable.shape_bg);
            iq_release_bt.setEnabled(false);
            iq_release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            verticalChart1.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            iq_start_bt.setEnabled(false);
            iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            iq_release_bt.setEnabled(true);
            iq_release_bt.setBackgroundResource(R.drawable.shape_bg);
            iq_stop_bt.setEnabled(false);
            iq_stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
        }


        iq_start_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 1;

                startAnswerIQ();

            }
        });

        iq_stop_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 2;
                stopAnsweIQ();
            }
        });

        //查看试题详情
        iq_release_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 0;
                popupWindow.dismiss();
//                showPopWinIQ1();
                getIQDetails();

            }
        });


        popupWindow.showAtLocation(iv_home, Gravity.CENTER, 0, 0);

    }

    /**
     * 展示即时问答详情弹窗
     */
    private void showPopWinIQ1() {

        final View view = LayoutInflater.from(context).inflate(R.layout.pop_iq, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow2(context, view, (float) 1.3, (float) 1.5, 0.5f, -1);


        iq_start_bt = (Button) view.findViewById(R.id.iq_start_bt);
        iq_stop_bt = (Button) view.findViewById(R.id.iq_stop_bt);
        iq_release_bt = (Button) view.findViewById(R.id.iq_release_bt);
        verticalChart1 = (VerticalChart1) view.findViewById(R.id.vc);
        textView = (TextView) view.findViewById(R.id.tv_select_correct);
        tv_null = (TextView) view.findViewById(R.id.tv_null);


        iq_release_bt.setEnabled(true);
        iq_release_bt.setBackgroundResource(R.drawable.shape_bg);
        iq_release_bt.setText("关    闭");
        iq_start_bt.setVisibility(View.INVISIBLE);
        iq_stop_bt.setVisibility(View.INVISIBLE);
        verticalChart1.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        tv_null.setVisibility(View.GONE);

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);


        iq_release_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 0;
                popupWindow.dismiss();

                techSelectAnswerIQ(verticalChart1);
            }
        });


        Log.d("ssssssssssssss", yList.size() + "      " + percentList.size() + "      " + percentList.get(0) + "      " + percentList.get(1) + "      " + percentList.get(2) + "      " + percentList.get(3));
        verticalChart1.initView(yList, percentList, "");

        popupWindow.showAtLocation(iv_home, Gravity.CENTER, 0, 0);

    }


    /**
     * 跳转到webview界面播放相应的课件
     *
     * @param fileUrl   课件地址
     * @param enterMark 1 代表情景导入  2 代表学习新知
     */

    public void skipWebView(String fileUrl, String enterMark) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("fileUrl", fileUrl);
        intent.putExtra("SpeakId", speakId);
        intent.putExtra("EnterMark", enterMark);
        intent.putExtra("ClassNo", classNo);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            //结果码 1代表情景导入  2代表学习新知
            if (resultCode == 1) {
                commonSet(3);
//                tv_study_new.setTextColor(Color.parseColor("#f6aa00"));
//                tv_study_new1.setTextColor(Color.parseColor("#f6aa00"));
                if (studyNewFragment == null) {
                    studyNewFragment = new StudyNewFragment();
                }

                showFragmentAtPosition1(studyNewFragment);
            }
            if (resultCode == 2) {

                commonSet(4);
//                tv_deepen_use.setTextColor(Color.parseColor("#f6aa00"));
//                tv_deepen_use1.setTextColor(Color.parseColor("#f6aa00"));
                if (deepenUseFragment == null) {
                    deepenUseFragment = new DeepenUseFragment();

                }

                showFragmentAtPosition1(deepenUseFragment);
            }
        }
    }


    //获取讲义路径
    private void getContentUrl() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonType", "1");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetSpeachDetailMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            showLoading1();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    closeLoading();
                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }
                                if (resultvalue.equals("1") || resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    AlertDialogUtil.showAlertDialog(context, "提示", "获取内容失败！");
                                } else {
                                    Gson gson = new Gson();
                                    PrepareUrlBean prepareUrlBean = gson.fromJson(jsonObject.toString(), PrepareUrlBean.class);
                                    if (prepareUrlBean != null) {
                                        String handoutsUrl = prepareUrlBean.getTables().getTable().getRows().get(0).getBodyLink();
                                        if (handoutsUrl != null && !handoutsUrl.equals("")) {
                                            Intent intent = new Intent(context, HandoutsWebActivity.class);
                                            intent.putExtra("fileUrl", handoutsUrl);
                                            intent.putExtra("EnterMark", "2");
                                            intent.putExtra("ClassNo", classNo);
                                            startActivity(intent);
                                        } else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "讲义地址获取异常");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "讲义地址获取异常");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }
    }


    //趣学习停止答题
    private void stopAnswer() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("classId", classId);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.StopAnswerMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            showLoading1();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    closeLoading();
                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }
                                if (resultvalue.equals("1") || resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    AlertDialogUtil.showAlertDialog(context, "提示", "获取内容失败！");
                                } else {
                                    Toast.makeText(context, "答题停止", Toast.LENGTH_SHORT).show();
                                    isStopeAnswer = "0";
                                    startTimer();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }
    }

    //临时互动开始答题
    public void startAnswerIQ() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }

            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("TestNo", "");
            jsonObject.put("Releaser", userCode);
            jsonObject.put("TrunkNo", classNo);
            jsonObject.put("Period", "1");
            if (isIQ == 0) {
                jsonObject.put("ReplyStatus", "2");//1代表题库代替 2代表临时答题 3抢答 4红包

            } else if (isIQ == 1) {
                jsonObject.put("ReplyStatus", "3");//1代表题库代替 2代表临时答题 3抢答 4红包

            } else {
                jsonObject.put("ReplyStatus", "4");//1代表题库代替 2代表临时答题 3抢答 4红包
            }


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LCllidkerStartMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    closeLoading();
                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }
                                if (resultvalue.equals("0")) {

                                    if (isIQ == 0) {
                                        iq_start_bt.setEnabled(false);
                                        iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                        iq_release_bt.setEnabled(false);
                                        iq_release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                        iq_stop_bt.setEnabled(true);
                                        iq_stop_bt.setBackgroundResource(R.drawable.shape_bg);
                                        Toast.makeText(context, "开始答题", Toast.LENGTH_SHORT).show();

                                    } else if (isIQ == 1) {
                                        responder_start_bt.setEnabled(false);
                                        responder_start_bt.setBackgroundResource(R.drawable.responder_select_no);
                                        responder_release_bt.setEnabled(false);
                                        responder_release_bt.setBackgroundResource(R.drawable.responder_select_no);
                                        responder_stop_bt.setEnabled(true);
                                        responder_stop_bt.setBackgroundResource(R.drawable.responder_select);
                                        Toast.makeText(context, "开始答题", Toast.LENGTH_SHORT).show();
                                    } else {
                                        startCountDownTimer();
                                    }

                                    iq_test_no = jsonObject.getString("TestNo");


                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                } else {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }

    }

    //临时互动停止答题下
    public void stopAnsweIQ() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }

            if (iq_test_no == null || iq_test_no.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("TestNo", iq_test_no);
            jsonObject.put("Releaser", userCode);
            jsonObject.put("TrunkNo", classNo);
            jsonObject.put("Period", "1");
            jsonObject.put("PeriodText", "");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LCllidkerStopMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    closeLoading();
                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }
                                if (resultvalue.equals("0")) {
                                    Toast.makeText(context, "答题已停止", Toast.LENGTH_SHORT).show();

                                    if (isIQ == 0) {
                                        iq_start_bt.setEnabled(false);
                                        iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                        iq_release_bt.setEnabled(true);
                                        iq_release_bt.setBackgroundResource(R.drawable.shape_bg);
                                        iq_stop_bt.setEnabled(false);
                                        iq_stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                    } else if (isIQ == 1) {
                                        responder_start_bt.setEnabled(false);
                                        responder_start_bt.setBackgroundResource(R.drawable.responder_select_no);
                                        responder_release_bt.setEnabled(true);
                                        responder_release_bt.setBackgroundResource(R.drawable.responder_select);
                                        responder_stop_bt.setEnabled(false);
                                        responder_stop_bt.setBackgroundResource(R.drawable.responder_select_no);
                                    } else {
                                        Toast.makeText(GoClassActivity.this, "抢红包已停止", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, "停止答题失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "停止答题失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }

    }

    //临时互动获取答题详情
    public void getIQDetails() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }

            if (iq_test_no == null || iq_test_no.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("TestNo", iq_test_no);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetIQDetailsMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    closeLoading();
                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }

                                if (resultvalue.equals("-1") || resultvalue.equals("1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                } else {
                                    Gson gson = new Gson();
                                    IQDetailsBean iqDetailsBean = gson.fromJson(jsonObject.toString(), IQDetailsBean.class);
                                    if (iqDetailsBean != null) {

                                        initIQVerChart(iqDetailsBean);

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未获取到学生答题数据!");
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }

    }

    private List<Integer> percentList;
    private List<Integer> yList;

    //初始化临时答题柱状图
    private void initIQVerChart(IQDetailsBean iqDetailsBean) {

        percentList = new ArrayList<>();
        yList = new ArrayList<>();
        //总人数呢
        double total = 0;
        double selectA = iqDetailsBean.getTables().getTable().getRows().get(0).getNumber();
        double selectB = iqDetailsBean.getTables().getTable().getRows().get(1).getNumber();
        double selectC = iqDetailsBean.getTables().getTable().getRows().get(2).getNumber();
        double selectD = iqDetailsBean.getTables().getTable().getRows().get(3).getNumber();
        double selectW = iqDetailsBean.getTables().getTable().getRows().get(4).getNumber();//代表未答题的人数

        total = selectA + selectB + selectC + selectD + selectW;

        if (total != 0) {
            percentList.add((int) ((selectA / total) * 100));
            percentList.add((int) ((selectB / total) * 100));
            percentList.add((int) ((selectC / total) * 100));
            percentList.add((int) ((selectD / total) * 100));
            percentList.add((int) ((selectW / total) * 100));

        }

        yList.add((int) selectA);
        yList.add((int) selectB);
        yList.add((int) selectC);
        yList.add((int) selectD);
        yList.add((int) selectW);

        showPopWinIQ1();


    }

    //临时答题教师选择正确答案
    private void techSelectAnswerIQ(VerticalChart1 verticalChart1) {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }

            if (iq_test_no == null || iq_test_no.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("Correct", verticalChart1.mRightAnswers);
            jsonObject.put("TestNo", iq_test_no);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.IQTechSelectMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    closeLoading();
                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }
                                if (resultvalue.equals("0")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                } else if (resultvalue.equals("-1") || resultvalue.equals("1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }
    }


    /**
     * 关闭悬浮按钮菜单
     */

    private void closeFloatingMenu() {
        if (rightLowerMenu.isOpen()) {
            rightLowerMenu.close(true);
        }
    }

    /**
     * 点击侧滑菜单获取跳转下一步时，公共设置
     */
    private void commonSet(int i) {
//        setTextColor();
        title_tv.setText(titleList.get(i));

        if (!currentTitle.equals(title_tv.getText().toString()) && !currentTitle.equals("1")) {
            if (isStopeAnswer.equals("0")) {
                startTimer();
            } else {
                //停止答题如果成功
                if (isDualTeacher.equals("0")) {
                    stopAnswer();
                } else {
                    startTimer();
                }
            }
        }

        currentTitle = titleList.get(i);
        initPopList();
        if (i == 0 || i == 2 || i == 3 || i == 6 || i == 9 || i == 10 || i == 8) {
            rl_home.setVisibility(View.INVISIBLE);
        } else {
            rl_home.setVisibility(View.VISIBLE);
        }

        if (i == 0 || i == 8 || i == 9 || i == 10) {
            closeFloatingMenu();
            rightLowerButton.setVisibility(View.GONE);
        } else {
            rightLowerButton.setVisibility(View.VISIBLE);
        }
        if (i == 7) {
            rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.xiayibu1));
        } else {
            rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.xiayibu));
        }

//        if (i == 3 || i == 2){
//            rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.timer1));
//        }else {
//            rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.timer));
//        }

        if (i == 3 || i == 2 || i == 6 || i == 8 || i == 10 || i == 0) {
            rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
        }

    }


//    /**
//     * 设置侧滑菜单字体颜色
//     */
//    private void setTextColor(){
//        tv_sign_group.setTextColor(Color.parseColor("#666666"));
//        tv_induction_test.setTextColor(Color.parseColor("#666666"));
//        tv_scene_import.setTextColor(Color.parseColor("#666666"));
//        tv_study_new.setTextColor(Color.parseColor("#666666"));
//        tv_deepen_use.setTextColor(Color.parseColor("#666666"));
//        tv_solidify_practice.setTextColor(Color.parseColor("#666666"));
//        tv_class_conclusion.setTextColor(Color.parseColor("#666666"));
//        tv_ranking.setTextColor(Color.parseColor("#666666"));
//        tv_class_record.setTextColor(Color.parseColor("#666666"));
//        tv_happy.setTextColor(Color.parseColor("#666666"));
//        tv_go_out_test.setTextColor(Color.parseColor("#666666"));
//
//        tv_sign_group1.setTextColor(Color.parseColor("#666666"));
//        tv_induction_test1.setTextColor(Color.parseColor("#666666"));
//        tv_scene_import1.setTextColor(Color.parseColor("#666666"));
//        tv_study_new1.setTextColor(Color.parseColor("#666666"));
//        tv_deepen_use1.setTextColor(Color.parseColor("#666666"));
//        tv_solidify_practice1.setTextColor(Color.parseColor("#666666"));
//        tv_class_conclusion1.setTextColor(Color.parseColor("#666666"));
//        tv_ranking1.setTextColor(Color.parseColor("#666666"));
//        tv_class_record1.setTextColor(Color.parseColor("#666666"));
//        tv_happy1.setTextColor(Color.parseColor("#666666"));
//        tv_go_out_test1.setTextColor(Color.parseColor("#666666"));
//    }

    /**
     * 打开关闭左侧滑菜单
     */
    private void openOrCloseDrawerLayout() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    /**
     * 打开关闭右侧滑菜单
     */
    private void openOrCloseDrawerLayout1() {
        if (drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.closeDrawer(Gravity.END);
        } else {
            drawerLayout.openDrawer(Gravity.END);
        }
    }

    /**
     * 当前标题和上次标题的变化,popuwindow中集合的初始化
     */

    private void initPopList() {

        if (!title.equals(currentTitle)) {
            title = currentTitle;
            PWSelectTV = "";
            filtrateList.clear();
            if (title.equals(titleList.get(1))) {
                filtrateList.add("查看试题");
//                filtrateList.add("学生排名");
//                filtrateList.add("小组排名");
                filtrateList.add("答案解析");
            } else if (title.equals(titleList.get(4))) {
                filtrateList.add("查看套题");
                filtrateList.add("答案解析");
            } else if (title.equals(titleList.get(5))) {
                filtrateList.add("查看练习");
                filtrateList.add("答案解析");

            } else if (title.equals(titleList.get(7))) {
                filtrateList.add("查看试题");
//                filtrateList.add("学生排名");
//                filtrateList.add("小组排名");
                filtrateList.add("答案解析");

            } else if (title.equals(titleList.get(8))) {
                filtrateList.add("个人积分榜");
                filtrateList.add("小组积分榜");
            }
        }
        filtrateAdapter.notifyDataSetChanged();

    }


    private void addTitleList() {
        titleList = new ArrayList<>();
        titleList.add("签到分组");
        titleList.add("入门测");
        titleList.add("情景导入");
        titleList.add("学习新知");
        titleList.add("深化应用");
        titleList.add("巩固练习");
        titleList.add("课堂小结");
        titleList.add("出门考");
        titleList.add("排名");
        titleList.add("课堂记录");
        titleList.add("开心一刻");

    }

    public void showLoading() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this, null);
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
            loadingBox = new LoadingBox(this, null);
        }
        loadingBox.Show1();
    }


    /**
     * 计时器 开始计时
     */

    public void startTimer() {

        if (timerTask != null) {
            stopTimer();
        }

        timer1 = new Timer();
        timerTask = new TimerTask() {
            int cnt = 0;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cnt = cnt + 1;
                        if (strTimer.equals("0") || strTimer.equals("1")) {
                            if (tv_timer != null) {
                                tv_timer.setText(getStringTime(cnt));
                            }
                        }
                    }
                });
            }
        };
        timer1.schedule(timerTask, 0, 1000);
    }

    /**
     * 倒计时器 开始倒计时
     */

    public void startCountDownTimer() {

        tv_time.setVisibility(VISIBLE);
        redPacketsSurfaceVew.setVisibility(View.VISIBLE);

        if (countDownTimer != null) {
//            redPacketsLayout.stopRain();
            redPacketsSurfaceVew.stopRain();
            countDownTimer.cancel();
        }
//
//        redPacketsLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                redPacketsLayout.startRain();
//            }
//        });


        redPacketsSurfaceVew.post(new Runnable() {
            @Override
            public void run() {
                redPacketsSurfaceVew.startRain();
            }
        });


        countDownTimer = new CountDownTimer(21000, 1000) {
            @Override
            public void onTick(long l) {
                tv_time.setText(l / 1000 + "");
            }

            @Override
            public void onFinish() {
                //倒计时结束
                tv_time.setVisibility(View.GONE);
//                redPacketsLayout.stopRain();
                redPacketsSurfaceVew.stopRain();
                redPacketsSurfaceVew.setVisibility(View.GONE);
                countDownTimer.cancel();
                stopAnsweIQ();
                Intent intent = new Intent(GoClassActivity.this, RPRankActivity.class);
                intent.putExtra("TestNo", iq_test_no);
                startActivity(intent);

            }
        };

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        countDownTimer.start();

    }

    /**
     * 格式化时间
     *
     * @param cnt
     * @return
     */
    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    /**
     * 取消定时器
     */
    public void stopTimer() {
        if (!timerTask.cancel()) {
            timerTask.cancel();
            timer1.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            redPacketsSurfaceVew.stopRain();
//            redPacketsLayout.stopRain();
        }
    }

    //活动的列表
    private void recycler() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }
            jsonObject.put("UserCode", "US0001218");
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.A001002;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);

            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {

                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            Gson gson = new Gson();
                            A001002Bean Abean = gson.fromJson(jsonObject.toString(), A001002Bean.class);
                            if (Abean != null) {
                                if (Abean.getTables().getTable().getRows().size() != 0) {
                                    endAdapter.setData(Abean.getTables().getTable().getRows());
//                                    setData(Abean.getTables().getTable().getRows());
                                }
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }
    }

    private void click() {
        //end 的 侧滑
        ll_sign_group = (LinearLayout) findViewById(R.id.ll_sign_group);
        ll_class_record = (LinearLayout) findViewById(R.id.ll_class_record);
        ll_happy = (LinearLayout) findViewById(R.id.ll_happy);
        ll_go_out_test = (LinearLayout) findViewById(R.id.ll_go_out_test);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_responder = (LinearLayout) findViewById(R.id.ll_responder);
        ll_red_packet = (LinearLayout) findViewById(R.id.ll_red_packet);

        //start  的 侧滑
        ll_sign_groupstart = (LinearLayout) findViewById(R.id.ll_sign_groupstart);
        ll_ranking_start = (LinearLayout) findViewById(R.id.ll_ranking_start);
        ll_class_recordstart = (LinearLayout) findViewById(R.id.ll_class_recordstart);
        ll_happystart = (LinearLayout) findViewById(R.id.ll_happystart);
        ll_responderstart = (LinearLayout) findViewById(R.id.ll_responderstart);
        ll_red_packetstart = (LinearLayout) findViewById(R.id.ll_red_packetstart);
        ll_backstart = (LinearLayout) findViewById(R.id.ll_backstart);
        //start的点击事件
        ll_sign_groupstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonSet(0);
                if (isDualTeacher.equals("0") && isCertificate.equals("0")) {
                    //双师主教
                    if (signFragment == null) {
                        signFragment = new SignFragment();
                    }
                    showFragmentAtPosition1(signFragment);
                } else {
                    if (signGroupFragment == null) {
                        signGroupFragment = new SignGroupFragment();
                    }
                    showFragmentAtPosition1(signGroupFragment);
                }


            }
        });
        ll_class_recordstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonSet(9);
                if (classRecordFragment == null) {
                    classRecordFragment = new ClassRecordFragment();
                }
                showFragmentAtPosition1(classRecordFragment);

            }
        });
        ll_happystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonSet(10);
                if (happyFragment == null) {
                    happyFragment = new HappyFragment();
                }
                showFragmentAtPosition1(happyFragment);

            }
        });
        ll_backstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_responderstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIQ = 1;
                showPopWinResponder();
            }
        });
        ll_red_packetstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIQ = 2;
                startAnswerIQ();
            }
        });
        //end的点击事件
        ll_sign_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonSet(0);
                if (isDualTeacher.equals("0") && isCertificate.equals("0")) {
                    //双师主教
                    if (signFragment == null) {
                        signFragment = new SignFragment();
                    }
                    showFragmentAtPosition1(signFragment);
                } else {
                    if (signGroupFragment == null) {
                        signGroupFragment = new SignGroupFragment();
                    }
                    showFragmentAtPosition1(signGroupFragment);
                }


            }
        });
        ll_class_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonSet(9);
                if (classRecordFragment == null) {
                    classRecordFragment = new ClassRecordFragment();
                }
                showFragmentAtPosition1(classRecordFragment);

            }
        });
        ll_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonSet(10);
                if (happyFragment == null) {
                    happyFragment = new HappyFragment();
                }
                showFragmentAtPosition1(happyFragment);

            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIQ = 1;
                showPopWinResponder();
            }
        });
        ll_red_packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIQ = 2;
                startAnswerIQ();
            }
        });
    }

    //获取  菜单类别
    public void getTypeCode(String code) {
        if (code.equals("0")) {
            Toast.makeText(this, "试题", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "PPT", Toast.LENGTH_LONG).show();
        }
    }

    //获取  菜单名称
    public void getTypeName(String name) {
        switch (name) {
            case "入门测":
                if (inductionTestFragment == null) {
                    inductionTestFragment = new InductionTestFragment();

                }
                showFragmentAtPosition1(inductionTestFragment);
                break;
            case "情景导入":
                if (sceneImportFragmetn == null) {
                    sceneImportFragmetn = new SceneImportFragment();
                }

                showFragmentAtPosition1(sceneImportFragmetn);
                break;
            case "学习新知":
                if (studyNewFragment == null) {
                    studyNewFragment = new StudyNewFragment();

                }
                showFragmentAtPosition1(studyNewFragment);
                break;
            case "深化应用":
                if (deepenUseFragment == null) {
                    deepenUseFragment = new DeepenUseFragment();

                }
                showFragmentAtPosition1(deepenUseFragment);
                break;
            case "课堂小结":
                if (classConclusionFragment == null) {
                    classConclusionFragment = new ClassConclusionFragment();

                }
                showFragmentAtPosition1(classConclusionFragment);
                break;
            case "巩固练习":
                if (solidifyPracticeFragment == null) {
                    solidifyPracticeFragment = new SolidifyPracticeFragment();

                }
                showFragmentAtPosition1(solidifyPracticeFragment);
                break;
            case "出门考":
                if (goOutTestFragment == null) {
                    goOutTestFragment = new GoOutTestFragment();

                }
                showFragmentAtPosition1(goOutTestFragment);
                break;
        }

    }

    @Override
    public void onClick(View view) {

    }
}
