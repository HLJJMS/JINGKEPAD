package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.AnalyContentBean;
import smjj.pureclass_pad.beans.AnalyStuCondBean;
import smjj.pureclass_pad.beans.AnalyStuCondBean1;
import smjj.pureclass_pad.beans.AnalyStuScoreBean;
import smjj.pureclass_pad.beans.StudentInternalBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.view.AutoHeightGridView;
import smjj.pureclass_pad.view.AutoHeightListView;
import smjj.pureclass_pad.view.HorizontalBarChart;

//学情分析activity
public class StudentAnalysisActivity extends BaseActivity implements View.OnClickListener{

    private AutoHeightListView listView;

    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private Button saveBt;
    private Context context;
    private RelativeLayout rl_back, rl_home, rl_add;
    private TextView tv_stu_name, tv_date, tv_stu_name1;
    private TextView tv_stu_total, tv_stu_ranking, tv_class_ave;
    private EditText et_study_advice;

    private Handler mHandler;
    private SharedPreferences spConfig;

    private List<String> listData;
    private CommonAdapter adapter;

    private HorizontalBarChart horizontalBarChart;

    private String classId;
    private String typleUser;
    private String userCode;
    private String userName;

    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> studentList;
    private String selectStudent;
    private String  studentNo;
    private boolean isFirstSelect ;
    private String studyAdvice;

    private Map<Integer, List<String>> map;

    private String time;
    private String isDualTeacher;//是否双师 0是双师 1非双师
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strudent_analysis);
        context = this;
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        userName = spConfig.getString(SPCommonInfoBean.userName,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate,"");
        isFirstSelect = true;
        time = getIntent().getStringExtra("Time");
        studyAdvice = "";

        findView();
    }


    private void findView() {
        listView = (AutoHeightListView) findViewById(R.id.auto_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        saveBt = (Button) findViewById(R.id.log_bt);
        horizontalBarChart = (HorizontalBarChart) findViewById(R.id.h_bar_chart);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        tv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        tv_stu_name1 = (TextView) findViewById(R.id.tv_stu_name1);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_stu_total = (TextView) findViewById(R.id.tv_stu_total);
        tv_stu_ranking = (TextView) findViewById(R.id.tv_stu_ranking);
        tv_class_ave = (TextView) findViewById(R.id.tv_class_ave);
        et_study_advice = (EditText) findViewById(R.id.et_study_advice);


        listData = new ArrayList<>();
        tv_date.setText(time);


        studentList = new ArrayList<>();

        title_tv.setText("学情分析");
        iv_home.setImageResource(R.drawable.select_stu);

        adapter = new CommonAdapter<String>(context, listData, R.layout.item_auto_lv) {
            @Override
            public void convert(ViewHolder holder, final String rowsBean) {
                final int i = listData.indexOf(rowsBean);
//                LinearLayout ll_item = holder.getView(R.id.ll_item);

                holder.setText(R.id.tv_auto_lv, rowsBean);
                AutoHeightGridView autoHeightGridView = holder.getView(R.id.auto_gv);

                final List<String> list = new ArrayList<>();
                list.addAll(map.get(i));
                final int[] item = {-3};
//
                CommonAdapter au_GV_Adapter = new CommonAdapter<String>(context, list, R.layout.item_auto_gv) {
                    @Override
                    public void convert(ViewHolder holder, String s) {
                        item[0] = item[0] + 1;
                        final CheckBox checkBox = holder.getView(R.id.cb_auto_gv);
                        checkBox.setEnabled(false);
                        checkBox.setText(item[0] + "");
                        checkBox.setTextColor(Color.parseColor("#666666"));
                        if (s.equals("1")){
                            checkBox.setChecked(false);
                        }else {
                            checkBox.setChecked(true);
                        }

                    }
                };

                autoHeightGridView.setAdapter(au_GV_Adapter);

            }
        };

        listView.setAdapter(adapter);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        saveBt.setOnClickListener(this);

        checkSign();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
//                ActivityManage.backToNewCheckCode(context);
                studentList.clear();
                checkSign();
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.log_bt:
                //提交反馈信息
                addAnalysis();
                break;

        }
    }

    //检查学生是否签到
    private void checkSign(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("ClassID", classId);
            if (isDualTeacher.equals("0") && isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckSignMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生签到");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    StudentInternalBean studentInternalBean = gson.fromJson(jsonObject.toString(), StudentInternalBean.class);
                                    if (studentInternalBean != null) {
                                        studentList.addAll(studentInternalBean.getTables().getTable().getRows());
                                        if (studentList.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生签到");
                                            return;
                                        }
                                        if (isFirstSelect){
                                            isFirstSelect = false;
                                            selectStudent = studentList.get(0).getStudentName();
                                            studentNo = studentList.get(0).getStudentNo();

                                            tv_stu_name.setText(selectStudent);
                                            tv_stu_name1.setText(selectStudent);

                                            getStuScore();
                                            getStuCond();
                                            getStuCond1();
                                            getAnalysis();

                                        }else {
                                            showStudentPopu();
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生签到");
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

    private void showStudentPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_student_view, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        final TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        Button saveBt = (Button) view.findViewById(R.id.log_bt);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close_pop);
        saveBt.setText("发  布");
        saveBt.setVisibility(View.GONE);
        iv_close.setVisibility(View.VISIBLE);
        textView.setText("学生列表");
        final CommonAdapter adapter1 = new CommonAdapter<StudentInternalBean.TablesBean.TableBean.RowsBean>(context, studentList, R.layout.item_pop_student) {
            @Override
            public void convert(ViewHolder holder, final StudentInternalBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.getView(R.id.student_gridview_tv).setVisibility(View.GONE);
                final TextView tv_stu = holder.getView(R.id.student_gridview_tv1);
                tv_stu.setVisibility(View.VISIBLE);
                tv_stu.setText(rowsBean.getStudentName());
                if (rowsBean.getStudentName().equals(selectStudent)){
                    tv_stu.setBackgroundResource(R.drawable.shape_solid_bg);
                    tv_stu.setTextColor(Color.WHITE);
                }

                tv_stu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //请求相应数据
                        selectStudent = rowsBean.getStudentName();
                        studentNo = rowsBean.getStudentNo();
                        getStuScore();
                        getStuCond();
                        getStuCond1();
                        getAnalysis();

                        tv_stu_name.setText(selectStudent);



                        popupWindow.dismiss();
                    }
                });
            }
        };
        gridView.setAdapter(adapter1);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }


    /**
     * 学情分析 （获取学生总分、排名）
     */
    private void getStuScore(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("StudentNo", studentNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAnalysisStuScoreMN;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
//            showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
//                    closeLoading();
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生成绩情况");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    AnalyStuScoreBean analyStuScoreBean = gson.fromJson(jsonObject.toString(), AnalyStuScoreBean.class);
                                    if (analyStuScoreBean != null) {
                                        int size = analyStuScoreBean.getTables().getTable().getRows().size();
                                        if (size > 0){
                                            tv_stu_total.setText(analyStuScoreBean.getTables().getTable().getRows().get(0).getScore() + "");
                                            tv_stu_ranking.setText(analyStuScoreBean.getTables().getTable().getRows().get(0).getRanking() + "");
                                            tv_class_ave.setText(analyStuScoreBean.getTables().getTable().getRows().get(0).getAveragescore() + "");

                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生成绩情况");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生成绩情况");
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

    /**
     * 学情分析 （获取学生知识点掌握情况）
     */
    private void getStuCond(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("StudentNo", studentNo);
//            jsonObject.put("", studentNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAnalyStuCondMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生知识点掌握情况情况");
                                    List<String> list = new ArrayList<>();
                                    List<Integer> list1 = new ArrayList<>();
                                    //画柱状图
                                    horizontalBarChart.initView(list, list1);
                                    tv_stu_name1.setText(selectStudent);

                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    AnalyStuCondBean analyStuCondBean = gson.fromJson(jsonObject.toString(), AnalyStuCondBean.class);
                                    if (analyStuCondBean != null) {
                                        int size = analyStuCondBean.getTables().getTable().getRows().size();
                                        if (size > 0){
                                            List<String> list = new ArrayList<>();
                                            List<Integer> list1 = new ArrayList<>();
                                            for (int i =  0; i < size ; i ++){

                                                list.add(analyStuCondBean.getTables().getTable().getRows().get(i).getChaptername());

                                                list1.add(analyStuCondBean.getTables().getTable().getRows().get(i).getPercentage());
                                            }

                                            //画柱状图
                                            horizontalBarChart.initView(list, list1);
                                            tv_stu_name1.setText(selectStudent);

                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生知识点掌握情况情况");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生知识点掌握情况情况");
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



    /**
     * 学情分析 （获取学生知识点答题情况）
     */
    private void getStuCond1(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("StudentNo", studentNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAnalyStuCondMN1;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
//            showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
//                    closeLoading();
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生知识点掌握情况情况");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    AnalyStuCondBean1 analyStuCondBean1 = gson.fromJson(jsonObject.toString(), AnalyStuCondBean1.class);
                                    if (analyStuCondBean1 != null) {
                                        listChange(analyStuCondBean1);
                                        int size = analyStuCondBean1.getTables().getTable().getRows().size();

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生知识点掌握情况情况");
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


    //保存学情分析
    private void addAnalysis() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            studyAdvice = et_study_advice.getText().toString().trim();

            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ClassID",classId);
            jsonObject.put("AnalysisContent",studyAdvice);
            jsonObject.put("StudentNo", studentNo);
            jsonObject.put("StudentName", selectStudent);
            jsonObject.put("UserCode",userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);

            String methodName = Constants.SaveAnalyMN;

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
                                if (resultvalue.equals("1") || resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Toast.makeText(context , "保存失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context , "保存成功", Toast.LENGTH_SHORT).show();

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


    //获取学情分析
    private void getAnalysis() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ClassID",classId);
            jsonObject.put("StudentNo", studentNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);

            String methodName = Constants.GetAnalyContentMN;

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
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                }
                                if (resultvalue.equals("1") || resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    et_study_advice.setText("");
                                } else {
                                    Gson gson = new Gson();
                                    AnalyContentBean analyContentBean = gson.fromJson(jsonObject.toString(), AnalyContentBean.class);
                                    if (analyContentBean != null) {
                                        studyAdvice = analyContentBean.getTables().getTable().getRows().get(0).getAnalysisContent();
                                        if (studyAdvice != null){
                                            et_study_advice.setText(studyAdvice);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
//                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
//                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                        }
                    } else {
//                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }
    }

    //知识点集合转换
    private void listChange(AnalyStuCondBean1 analyStuCondBean1) {
        listData.clear();
        int size = analyStuCondBean1.getTables().getTable().getRows().size();
        if (size > 0) {
            map = new HashMap<>();

            for (int i = 0; i < size; i++) {
                String knowledgeName = analyStuCondBean1.getTables().getTable().getRows().get(i).getChaptername();
                String status = analyStuCondBean1.getTables().getTable().getRows().get(i).getStatus() + "";
                if (listData.contains(knowledgeName)){
                    int position = listData.indexOf(knowledgeName);
                    map.get(position).add(status);
                }else {
                    listData.add(knowledgeName);
                    int position = listData.indexOf(knowledgeName);
                    List<String> konList = new ArrayList<>();
                    konList.add(status);
                    map.put(position, konList);
                }
            }

//            List<Integer> listPercent = new ArrayList<>();
//            for (int i = 0; i < map.size(); i ++){
//                List<String> list = map.get(i);
//                float satas = 0;
//                for (int a = 0; a < list.size(); a ++){
//                    if (list.get(a) != null && list.equals("1")){
//                        satas ++;
//                    }
//                }
//                listPercent.add((int) ((satas/list.size())*100));
//
//            }
//
//            horizontalBarChart.initView(listData, listPercent);


            adapter.setData(listData);
            adapter.notifyDataSetChanged();

        } else {
            AlertDialogUtil.showAlertDialog(context, "提示", "未查询到该学生知识点掌握情况情况");
        }

    }



}
