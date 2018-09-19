package smjj.pureclass_pad.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.StuSetWorkBean;
import smjj.pureclass_pad.beans.StuWorkAnswerBean;
import smjj.pureclass_pad.beans.StudentRankingBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

/**
 * 课后部分查看作业界面（即是作业完成情况）
 * 统测部分查看试题界面
 */
public class WorkConditionActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_ranking_list1, ll_ranking_list;
    private Button sureBt;
    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;

    private Context context;
    private Handler mHandler;

    private SharedPreferences spConfig;
    private Activity activity;
    private ScrollView scrollView;

    private GridView gridView;
    private CommonAdapter gridViewAdapter;
    private List<StuSetWorkBean.RowsBean> gridViewList;
    private List<StuSetWorkBean.RowsBean> checkStudentList = new ArrayList<>();

    private XListView listView;
    private List<StudentRankingBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter listAdapter;

    private XListView listView1;
    private List<StuWorkAnswerBean.TablesBean.TableBean.RowsBean> listData1;
    private CommonAdapter listAdapter1;

    //1代表课后查看作业   2代表测评查看试题
    private String enterMark;

    private String classId;
    private String typleUser;
    private String userCode;

    private String studentNo;
    private String isDualTeacher;//是否双师 0是双师 1非双师
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_condition);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        activity = this;
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate,"");

        findView();

    }

    private void findView(){
        gridView = (GridView) findViewById(R.id.gridview);
        listView = (XListView) findViewById(R.id.scheduling_lv);
        listView1 = (XListView) findViewById(R.id.scheduling_lv1);
        ll_ranking_list = (LinearLayout) findViewById(R.id.ll_ranking_list);
        ll_ranking_list1 = (LinearLayout) findViewById(R.id.ll_ranking_list1);
        sureBt = (Button) findViewById(R.id.log_bt);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        scrollView = (ScrollView) findViewById(R.id.scrollview);

        enterMark = getIntent().getStringExtra("enterMark");

        initAdapter();

        if (enterMark.equals("1")){
            title_tv.setText("查看作业");
        }else if (enterMark.equals("2")){
            title_tv.setText("测评");
        }

        ll_ranking_list.setVisibility(View.VISIBLE);
        ll_ranking_list1.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

        sureBt.setOnClickListener(this);
        onBack.setOnClickListener(this);

//        if (enterMark.equals("1")){
//            getSetWorkStu();
//        }
        getSetWorkStu();

    }


    private void initAdapter(){

        gridViewList = new ArrayList<>();
        checkStudentList.addAll(gridViewList);
        gridViewAdapter = new CommonAdapter<StuSetWorkBean.RowsBean>(context, gridViewList, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final StuSetWorkBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setTextColor(Color.WHITE);
                checkBox.setChecked(true);

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkStudentList.contains(rowsBean)){
                            checkStudentList.remove(rowsBean);
                            checkBox.setTextColor(Color.parseColor("#666666"));

                        }else {
                            checkStudentList.add(rowsBean);
                            checkBox.setTextColor(Color.WHITE);

                        }

                    }
                });

            }
        };


        listData = new ArrayList<>();

        listAdapter = new CommonAdapter<StudentRankingBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_module_ranking_lv) {
            @Override
            public void convert(ViewHolder holder, final StudentRankingBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                if (i % 2 == 0) {
                    ll_item.setBackgroundColor(Color.parseColor("#d1ecd8"));
                } else {
                    ll_item.setBackgroundColor(Color.parseColor("#bae2c4"));
                }
                TextView rankingTextView = holder.getView(R.id.ranking_tv);
                ImageView rankingImageView = holder.getView(R.id.ranking_iv);
                if (i == 0){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.first);
                }else if (i ==1){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.second);
                }else if (i == 2){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.third);
                }else {
                    rankingImageView.setVisibility(View.GONE);
                    rankingTextView.setVisibility(View.VISIBLE);
                    rankingTextView.setText("" + (i + 1));
                }

                holder.setText(R.id.name_tv, rowsBean.getStudentName());
                holder.setText(R.id.score_tv, rowsBean.getScore() + "");
                holder.setText(R.id.percent_tv, rowsBean.getCurrCount() +"");

//                holder.setText(R.id.name_tv, listData.get(i));
//                holder.setText(R.id.score_tv, list.get(i));
//                holder.setText(R.id.percent_tv, list1.get(i));
            }
        };


        listData1 = new ArrayList<>();
        listData1.add(new StuWorkAnswerBean.TablesBean.TableBean.RowsBean());
        listData1.add(new StuWorkAnswerBean.TablesBean.TableBean.RowsBean());

        listAdapter1 = new CommonAdapter<StuWorkAnswerBean.TablesBean.TableBean.RowsBean>(context, listData1, R.layout.item_exercises_webview) {
            @Override
            public void convert(ViewHolder holder, final StuWorkAnswerBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData1.indexOf(rowsBean);

                holder.setText(R.id.tv_item_no, (i + 1) + ".");
                holder.getView(R.id.tv_item_no).setVisibility(View.VISIBLE);

                LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
                LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
                LinearLayout ll_answer7 = holder.getView(R.id.ll_exercises_answer7);
                ll_answer5.setVisibility(View.VISIBLE);
                ll_answer6.setVisibility(View.VISIBLE);
                ll_answer7.setVisibility(View.VISIBLE);

                initWebView(holder, rowsBean);


            }
        };

        gridView.setAdapter(gridViewAdapter);
        listView.setAdapter(listAdapter);
        listView1.setAdapter(listAdapter1);
        listView1.setPullLoadEnable(false);
        listView1.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);

    }


    private void initWebView(ViewHolder holder, StuWorkAnswerBean.TablesBean.TableBean.RowsBean rowsBean) {
        WebView webView1 = holder.getView(R.id.wb_exercises1);
        WebView webView2 = holder.getView(R.id.wb_exercises2);
        WebView webView3 = holder.getView(R.id.wb_exercises3);
        WebView webView4 = holder.getView(R.id.wb_exercises4);

        if (Build.VERSION.SDK_INT >= 19) {
            webView1.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView2.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView3.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView4.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView1.setWebViewClient(new GameWebViewClient());
        WebSettings s = webView1.getSettings();
        s.setJavaScriptEnabled(true);

        webView2.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView2.setWebViewClient(new GameWebViewClient());
        WebSettings s1 = webView2.getSettings();
        s1.setJavaScriptEnabled(true);
        webView3.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView3.setWebViewClient(new GameWebViewClient());
        WebSettings s2 = webView3.getSettings();
        s2.setJavaScriptEnabled(true);
        webView4.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView4.setWebViewClient(new GameWebViewClient());
        WebSettings s3 = webView4.getSettings();
        s3.setJavaScriptEnabled(true);


        String stem = rowsBean.getStem();
        String choses = rowsBean.getChoses();
        if (stem != null && !stem.equals("")) {
            String str1 = CommonWay.jsoup(stem + choses);
            webView1.loadData(str1, "text/html; charset=UTF-8", null);
        } else {
            webView1.setVisibility(View.GONE);
        }
        String correct = rowsBean.getCorrect();
        if (correct != null && !correct.equals("")) {
            String str2 = CommonWay.jsoup1(correct);
            webView2.loadData(str2, "text/html; charset=UTF-8", null);
        } else {
            webView2.setVisibility(View.GONE);
        }
        String analysis = rowsBean.getAnalysis();
        if (analysis != null && !analysis.equals("")) {
            String str3 = CommonWay.jsoup2(analysis);
            webView3.loadData(str3, "text/html; charset=UTF-8", null);
        } else {
            webView3.setVisibility(View.GONE);
        }
        String stuAnswer = rowsBean.getQusAnswer();
        if (stuAnswer != null && !stuAnswer.equals("")) {
            String str4 = CommonWay.jsoup1(stuAnswer);
            webView4.loadData(str4, "text/html; charset=UTF-8", null);
        } else {
            webView4.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.onBack:
                finish();
                break;
            case R.id.log_bt:
                if (checkStudentList.size() == 1){
                    ll_ranking_list.setVisibility(View.GONE);
                    ll_ranking_list1.setVisibility(View.VISIBLE);
                    if (enterMark.equals("1")){
                        //课后作业
                        listData1.clear();
                        checkPersonAns();
                    }else {
                        //测评
                        listData1.clear();
                        checkPersonAns();
                    }
                }else if (checkStudentList.size() > 1){
                    ll_ranking_list.setVisibility(View.VISIBLE);
                    ll_ranking_list1.setVisibility(View.GONE);
                    if (enterMark.equals("1")){
                        //课后作业
                        listData.clear();
                        requestPersonRk();
                    }else {
                        //测评
                        listData.clear();
                        requestPersonRk();
                    }
                }else {
                    AlertDialogUtil.showAlertDialog(context, "提示", "选择的学生不能为空");
                }
                //请求数据刷新适配器
                break;
        }

    }

    //获取已布置作业学生
    private void getSetWorkStu(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }
            jsonObject.put("LessonID", classId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("TestType", "3");
            jsonObject.put("Flag", "2");
            if (isDualTeacher.equals("0") && isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetStuSetWorkMN;
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
                                if (resultvalue.equals("1")) {
                                    //未签到请求签到
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有已发布作业的学生");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    StuSetWorkBean setWorkBean = gson.fromJson(jsonObject.toString(), StuSetWorkBean.class);
                                    if (setWorkBean != null) {
                                        gridViewList.addAll(setWorkBean.getRows());
                                        if (gridViewList.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有已发布作业的学生");
                                            return;
                                        }
                                        //默认请求排名
                                        checkStudentList.addAll(gridViewList);
                                        requestPersonRk();
                                        gridViewAdapter.setData(gridViewList);
                                        gridViewAdapter.notifyDataSetChanged();

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有已发布作业的学生");
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

    //获取个人排名
    private void requestPersonRk() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            getStudentNO();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("LessonID",classId);
            jsonObject.put("StudentNo",studentNo + ",");
            jsonObject.put("TestType","3");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.PersonageRankingMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "获取排名失败，请等学生提交后再获取");
                                } else {

                                    Gson gson = new Gson();
                                    StudentRankingBean studentRankingBean = gson.fromJson(jsonObject.toString(), StudentRankingBean.class);
                                    if (studentRankingBean != null) {
                                        listData.clear();
                                        listData.addAll(studentRankingBean.getTables().getTable().getRows());
                                        listAdapter.setData(listData);
                                        listAdapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无排名信息，请等学生提交后再获取");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "获取排名失败，请等学生提交后再获取");
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


    //查看个人答题请情况
    private void checkPersonAns() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            getStudentNO();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("LessonID",classId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("StudentNo",studentNo);
            jsonObject.put("TestType","3");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetStuWorkExeMN;
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
                                if (resultvalue.equals("1")) {
                                    //未签到请求签到
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂无答题情况，请等学生提交后在查看");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                } else {

                                    Gson gson = new Gson();
                                    StuWorkAnswerBean stuWorkAnswerBean = gson.fromJson(jsonObject.toString(), StuWorkAnswerBean.class);
                                    if (stuWorkAnswerBean != null) {
                                        listData1.addAll(stuWorkAnswerBean.getTables().getTable().getRows());
                                        listAdapter1.setData(listData1);
                                        listAdapter1.notifyDataSetChanged();
                                        if (listData1.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无排名信息，请等学生提交后再获取");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "获取排名失败，请等学生提交后再获取");
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

    private void getStudentNO(){
        studentNo = "";
        for (int i = 0 ; i < checkStudentList.size(); i ++){
            if (i == 0){
                studentNo = checkStudentList.get(i).getStudentNo();
            }else {
                studentNo = studentNo + "," + checkStudentList.get(i).getStudentNo();
            }
        }
    }

}
