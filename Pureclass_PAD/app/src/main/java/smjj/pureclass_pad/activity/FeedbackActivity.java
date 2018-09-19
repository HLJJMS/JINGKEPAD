package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.ErrorKPBean;
import smjj.pureclass_pad.beans.FeedbackStuConBean1;
import smjj.pureclass_pad.beans.FeedbackStuConBean2;
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


//课堂反馈展示填写界面
public class FeedbackActivity extends BaseActivity implements View.OnClickListener{
    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private Context context;
    private EditText et_teacher_content;
    private Button saveBt;
    private RelativeLayout rl_back, rl_home, rl_add;

    private LinearLayout ll_five_star, ll_five_star1, ll_four_star, ll_four_star1, ll_three_star
            , ll_three_star1, ll_two_star, ll_two_star1, ll_one_star, ll_one_star1;
    private LinearLayout ll_last_five_star, ll_last_five_star1, ll_last_four_star, ll_last_four_star1
            ,ll_last_three_star, ll_last_three_star1, ll_last_two_star, ll_last_two_star1
            , ll_last_one_star, ll_last_one_star1;

    private LinearLayout ll_last_work3, ll_last_work2, ll_last_work1, ll_last_work;

    private TextView tv_into_correct, tv_into_error, tv_into_kp, tv_out_correct, tv_out_error, tv_out_kp;

    private EditText et_stu_comment, et_last_comment, et_current_comment, et_other;

    private TextView tv_last_total, tv_last_correct, tv_last_error, tv_last_kp
            , tv_current_kp, tv_current_item, tv_current_total;


    private TextView tv_stu_name, tv_time;

    private Handler mHandler;
    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;

    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> studentList;
    private String selectStudent;
    private boolean isFirstSelect ;

    private List<ErrorKPBean.TablesBean.TableBean.RowsBean> knowledgeList;

    //1代表添加课堂反馈，2代表查看课堂反馈
    private String enterMark;
    private String className;
    private String time;
    private String subject;

    private String  studentNo;
    private String workType;

    private String  stu_comment, stu_condition, last_condition, last_comment, current_comment, st_other;
    private String isDualTeacher;//是否双师 0是双师 1非双师
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ActivityManage.addActivity(this);
        context = this;
        mHandler = new Handler();
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate,"");
        isFirstSelect = true;

        stu_condition = "4";
        last_condition = "4";

        enterMark = getIntent().getStringExtra("enterMark");
        className = getIntent().getStringExtra("ClassName");
        time = getIntent().getStringExtra("Time");
        subject = getIntent().getStringExtra("Subject");

        findView();

    }

    private void findView() {
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        tv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        et_teacher_content = (EditText) findViewById(R.id.et_teacher_content);
        saveBt = (Button) findViewById(R.id.log_bt);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        ll_five_star = (LinearLayout) findViewById(R.id.ll_five_star);
        ll_five_star1 = (LinearLayout) findViewById(R.id.ll_five_star1);
        ll_four_star = (LinearLayout) findViewById(R.id.ll_four_star);
        ll_four_star1 = (LinearLayout) findViewById(R.id.ll_four_star1);
        ll_three_star = (LinearLayout) findViewById(R.id.ll_three_star);
        ll_three_star1 = (LinearLayout) findViewById(R.id.ll_three_star1);
        ll_two_star = (LinearLayout) findViewById(R.id.ll_two_star);
        ll_two_star1 = (LinearLayout) findViewById(R.id.ll_two_star1);
        ll_one_star = (LinearLayout) findViewById(R.id.ll_one_star);
        ll_one_star1 = (LinearLayout) findViewById(R.id.ll_one_star1);
        ll_last_five_star = (LinearLayout) findViewById(R.id.ll_last_five_star);
        ll_last_five_star1 = (LinearLayout) findViewById(R.id.ll_last_five_star1);
        ll_last_four_star = (LinearLayout) findViewById(R.id.ll_last_four_star);
        ll_last_four_star1 = (LinearLayout) findViewById(R.id.ll_last_four_star1);
        ll_last_three_star = (LinearLayout) findViewById(R.id.ll_last_three_star);
        ll_last_three_star1 = (LinearLayout) findViewById(R.id.ll_last_three_star1);
        ll_last_two_star = (LinearLayout) findViewById(R.id.ll_last_two_star);
        ll_last_two_star1 = (LinearLayout) findViewById(R.id.ll_last_two_star1);
        ll_last_one_star = (LinearLayout) findViewById(R.id.ll_last_one_star);
        ll_last_one_star1 = (LinearLayout) findViewById(R.id.ll_last_one_star1);
        ll_last_work = (LinearLayout) findViewById(R.id.ll_last_work);
        ll_last_work1 = (LinearLayout) findViewById(R.id.ll_last_work1);
        ll_last_work2 = (LinearLayout) findViewById(R.id.ll_last_work2);
        ll_last_work3 = (LinearLayout) findViewById(R.id.ll_last_work3);


        tv_into_correct = (TextView) findViewById(R.id.tv_into_correct);
        tv_into_error = (TextView) findViewById(R.id.tv_into_error);
        tv_into_kp = (TextView) findViewById(R.id.tv_into_kp);
        tv_out_correct = (TextView) findViewById(R.id.tv_out_correct);
        tv_out_error = (TextView) findViewById(R.id.tv_out_error);
        tv_out_kp = (TextView) findViewById(R.id.tv_out_kp);
        tv_last_total = (TextView) findViewById(R.id.tv_last_total);
        tv_last_correct = (TextView) findViewById(R.id.tv_last_correct);
        tv_last_error = (TextView) findViewById(R.id.tv_last_error);
        tv_last_kp = (TextView) findViewById(R.id.tv_last_kp);
        tv_current_kp = (TextView) findViewById(R.id.tv_current_kp);
        tv_current_item = (TextView) findViewById(R.id.tv_current_item);
        tv_current_total = (TextView) findViewById(R.id.tv_current_total);
        et_stu_comment = (EditText) findViewById(R.id.et_stu_comment);
        et_last_comment = (EditText) findViewById(R.id.et_last_comment);
        et_current_comment = (EditText) findViewById(R.id.et_current_comment);
        et_other = (EditText) findViewById(R.id.et_other);



        studentList = new ArrayList<>();
        knowledgeList = new ArrayList<>();

        et_teacher_content.setText(className);
        tv_time.setText(time);
        et_teacher_content.setEnabled(false);


        title_tv.setText("课堂反馈");
        iv_home.setImageResource(R.drawable.select_stu);

        if (enterMark.equals("1")){
            //填写课堂反馈
            saveBt.setVisibility(View.VISIBLE);
        }else {
            //查看课堂反馈
            saveBt.setVisibility(View.GONE);
            //请求数据并填充课堂反馈
            setOnClickView();
        }
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        saveBt.setOnClickListener(this);
        ll_five_star.setOnClickListener(this);
        ll_four_star.setOnClickListener(this);
        ll_three_star.setOnClickListener(this);
        ll_two_star.setOnClickListener(this);
        ll_one_star.setOnClickListener(this);
        ll_last_five_star.setOnClickListener(this);
        ll_last_four_star.setOnClickListener(this);
        ll_last_three_star.setOnClickListener(this);
        ll_last_two_star.setOnClickListener(this);
        ll_last_one_star.setOnClickListener(this);
        tv_into_kp.setOnClickListener(this);
        tv_out_kp.setOnClickListener(this);
        tv_last_kp.setOnClickListener(this);

        requestFeedBackStu();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
//                ActivityManage.backToNewCheckCode(context);
                studentList.clear();
                requestFeedBackStu();
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.log_bt:
                //提交反馈信息
//                AlertDialogUtil.showAlertDialog(context, "提示", "保存成功");
                submitFeedBackInfo();
                break;

            case R.id.tv_into_kp:
                workType = "入门测";
                getErroeKP();
                break;
            case R.id.tv_out_kp:
                workType = "出门考";
                getErroeKP();
                break;
            case R.id.tv_last_kp:
                getErroeKP1();
                break;



            case R.id.ll_five_star:
                stu_condition = "5";
                starShow(stu_condition);
                break;
            case R.id.ll_four_star:
                stu_condition = "4";
                starShow(stu_condition);

                break;
            case R.id.ll_three_star:
                stu_condition = "3";
                starShow(stu_condition);
                break;
            case R.id.ll_two_star:
                stu_condition = "2";
                starShow(stu_condition);

                break;
            case R.id.ll_one_star:
                stu_condition = "1";
                starShow(stu_condition);

                break;
            case R.id.ll_last_five_star:
                last_condition = "5";
                lastStarShow(last_condition);

                break;
            case R.id.ll_last_four_star:
                last_condition = "4";
                lastStarShow(last_condition);

                break;
            case R.id.ll_last_three_star:
                last_condition = "3";
                lastStarShow(last_condition);
                break;
            case R.id.ll_last_two_star:
                last_condition = "2";
                lastStarShow(last_condition);
                break;
            case R.id.ll_last_one_star:
                last_condition = "1";
                lastStarShow(last_condition);
                break;


        }
    }

    /**
     * 知识点掌握情况星展示
     */
    private void starVisbile(){
        ll_five_star.setVisibility(View.VISIBLE);
        ll_five_star1.setVisibility(View.GONE);
        ll_four_star.setVisibility(View.VISIBLE);
        ll_four_star1.setVisibility(View.GONE);
        ll_three_star.setVisibility(View.VISIBLE);
        ll_three_star1.setVisibility(View.GONE);
        ll_two_star.setVisibility(View.VISIBLE);
        ll_two_star1.setVisibility(View.GONE);
        ll_one_star.setVisibility(View.VISIBLE);
        ll_one_star1.setVisibility(View.GONE);
    }


    /**
     * 上次作业完成情况星展示
     */
    private void lastStarVisbile(){
        ll_last_five_star.setVisibility(View.VISIBLE);
        ll_last_five_star1.setVisibility(View.GONE);
        ll_last_four_star.setVisibility(View.VISIBLE);
        ll_last_four_star1.setVisibility(View.GONE);
        ll_last_three_star.setVisibility(View.VISIBLE);
        ll_last_three_star1.setVisibility(View.GONE);
        ll_last_two_star.setVisibility(View.VISIBLE);
        ll_last_two_star1.setVisibility(View.GONE);
        ll_last_one_star.setVisibility(View.VISIBLE);
        ll_last_one_star1.setVisibility(View.GONE);

    }

    private void setOnClickView(){

        ll_five_star.setEnabled(false);
        ll_five_star1.setEnabled(false);
        ll_four_star.setEnabled(false);
        ll_four_star1.setEnabled(false);
        ll_three_star1.setEnabled(false);
        ll_three_star.setEnabled(false);
        ll_two_star.setEnabled(false);
        ll_two_star1.setEnabled(false);
        ll_one_star.setEnabled(false);
        ll_one_star1.setEnabled(false);
        ll_last_five_star1.setEnabled(false);
        ll_last_five_star.setEnabled(false);
        ll_last_four_star.setEnabled(false);
        ll_last_four_star1.setEnabled(false);
        ll_last_three_star.setEnabled(false);
        ll_last_three_star1.setEnabled(false);
        ll_last_two_star.setEnabled(false);
        ll_last_two_star1.setEnabled(false);
        ll_last_one_star.setEnabled(false);
        ll_last_one_star1.setEnabled(false);
        et_stu_comment.setEnabled(false);
        et_last_comment.setEnabled(false);
        et_current_comment.setEnabled(false);
        et_other.setEnabled(false);

    }

    //请求学生
    private void requestFeedBackStu(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("classId", classId);

            if (enterMark.equals("1")){
                jsonObject.put("status", "1");
            }else {
                jsonObject.put("status", "2");
            }
            if (isDualTeacher.equals("0") && isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetFeedBackStuMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有相对应的学生");
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
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有相对应的学生");
                                            return;
                                        }
                                        if (isFirstSelect){
                                            isFirstSelect = false;
                                            selectStudent = studentList.get(0).getStudentName();
                                            tv_stu_name.setText(selectStudent);
                                            studentNo = studentList.get(0).getStudentNo();
                                            //请求相应数据展示
                                            if (enterMark.equals("2")){
                                                //查看课堂反馈
                                                getStuFeedbackCond();
                                            }
                                            getStuGCCond();
                                            showStudentPopu();
                                        }else {
                                            showStudentPopu();
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有相对应的学生");
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
                        studentNo = rowsBean.getStudentNo();
                        if (!rowsBean.getStudentName().equals(selectStudent)){
                            if (enterMark.equals("2")){
                                //查看课堂反馈
                                getStuFeedbackCond();
                            }
                            getStuGCCond();
                        }
                        selectStudent = rowsBean.getStudentName();
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


    private void showPopuWindow(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_knowledge_point_view, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        ListView listView1 = (ListView) view.findViewById(R.id.lv);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        TextView ensureTV = (TextView) view.findViewById(R.id.tv_ensure);
        ensureTV.setVisibility(View.GONE);
        CommonAdapter adapter = new CommonAdapter<ErrorKPBean.TablesBean.TableBean.RowsBean>(context, knowledgeList, R.layout.item_knowledge_point_lv) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void convert(ViewHolder holder, final ErrorKPBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setVisibility(View.GONE);
                TextView textView = holder.getView(R.id.scheduling_gridview_tv);
                textView.setText(rowsBean.getChaptername());
                textView.setVisibility(View.VISIBLE);

            }
        };
        listView1.setAdapter(adapter);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }

    /**
     * 提交课堂反馈
     */
    private void submitFeedBackInfo() {

        stu_comment = et_stu_comment.getText().toString();
        last_comment = et_last_comment.getText().toString();
        current_comment = et_current_comment.getText().toString();
        st_other = et_other.getText().toString();

        if (stu_condition == null || stu_condition.equals("")) {
            AlertDialogUtil.showAlertDialog(context,"提示","知识点掌握情况不能为空，请选择！");
            return;
        }
        if (stu_comment == null || stu_comment.equals("")) {
            AlertDialogUtil.showAlertDialog(context,"提示","学生知识点掌握情况点评不能为空，请点评！");
            return;
        }

        if (current_comment == null || current_comment.equals("")) {
            AlertDialogUtil.showAlertDialog(context,"提示","对本次作业说明不能为空，请说明！");
            return;
        }

        String teacherContent = "";
        String studentCondition = "";
        String testCondition = "";
        String oth = "";
        teacherContent = className ;
        studentCondition = stu_condition + "|" + stu_comment;
        testCondition =  last_condition + "|" + last_comment;
        oth = current_comment + "|" + st_other;

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "班课名称不能为空，请重新选择！", Toast.LENGTH_SHORT).show();
                return;
            }

            jsonObject.put("UserCode", userCode);
            jsonObject.put("StudentNo", studentNo);
            jsonObject.put("LessonID", classId);
            jsonObject.put("TeachingRecords", teacherContent);
            jsonObject.put("Students", studentCondition);
            jsonObject.put("Tests", testCondition);
            jsonObject.put("Remark", oth);


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SubmitFeedBackInfoMN;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "添加失败");
                                } else if (resultvalue.equals("0")){
                                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                    finish();
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
     * 获取学生上课答题情况
     */
    private void getStuGCCond(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("StudentNo", studentNo);
            jsonObject.put("TeacherNo", userCode);
            jsonObject.put("subjects", subject);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetStuFeedbackMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未查询该学生作业情况");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    FeedbackStuConBean2 feedbackStuConBean2 = gson.fromJson(jsonObject.toString(), FeedbackStuConBean2.class);
                                    if (feedbackStuConBean2 != null) {
                                        parseData(feedbackStuConBean2);

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未查询该学生作业情况");
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
     * 获取学生本次课后作业情况
     */
    private void getStuFeedbackCond(){

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
            String methodName = Constants.GetStuFbContentMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未查询该学生反馈");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    FeedbackStuConBean1 feedbackStuConBean1 = gson.fromJson(jsonObject.toString(), FeedbackStuConBean1.class);
                                    if (feedbackStuConBean1 != null) {
                                        int size = feedbackStuConBean1.getTables().getTable().getRows().size();
                                        if (size > 0){
                                            et_stu_comment.setText(feedbackStuConBean1.getTables().getTable().getRows().get(0).getKnowledgeComment());
                                            et_last_comment.setText(feedbackStuConBean1.getTables().getTable().getRows().get(0).getTaskComment());
                                            et_current_comment.setText(feedbackStuConBean1.getTables().getTable().getRows().get(0).getThisTask());
                                            et_other.setText(feedbackStuConBean1.getTables().getTable().getRows().get(0).getOther());
                                            String starStatus = "";
                                            String lastStarStatus = "";
                                            starStatus = feedbackStuConBean1.getTables().getTable().getRows().get(0).getKnowledgeSituation();
                                            lastStarStatus = feedbackStuConBean1.getTables().getTable().getRows().get(0).getTaskSituation();

                                            starShow(starStatus);
                                            lastStarShow(lastStarStatus);

                                        }else {

                                            AlertDialogUtil.showAlertDialog(context, "提示", "未查询该学生反馈");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未查询该学生反馈");
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
     * 解析试题，知识点数据
     * @param feedbackStuConBean2
     */
    private void parseData(FeedbackStuConBean2 feedbackStuConBean2){

        //入门测、出门考情况
        int size = feedbackStuConBean2.getTables().getTable().getRows().size();
        if (size >= 1) {
            tv_into_correct.setText(feedbackStuConBean2.getTables().getTable().getRows().get(0).getCurrCount() + "");
            tv_into_error.setText(feedbackStuConBean2.getTables().getTable().getRows().get(0).getErrorCount() + "");
            tv_out_kp.setEnabled(false);
            tv_out_kp.setText("无");
            int error = feedbackStuConBean2.getTables().getTable().getRows().get(0).getErrorCount();
            if (error == 0){
                tv_into_kp.setText("无");
                tv_into_kp.setEnabled(false);
            }else {
                tv_into_kp.setText("查看");
                tv_into_kp.setEnabled(true);
            }
        }else {
            tv_out_kp.setEnabled(false);
            tv_into_kp.setEnabled(false);
            tv_out_kp.setText("无");
            tv_into_kp.setText("无");
        }

        if (size == 2) {
            tv_out_correct.setText(feedbackStuConBean2.getTables().getTable().getRows().get(1).getCurrCount() + "");
            tv_out_error.setText(feedbackStuConBean2.getTables().getTable().getRows().get(1).getErrorCount() + "");
            int error = feedbackStuConBean2.getTables().getTable().getRows().get(0).getErrorCount();
            if (error == 0){
                tv_into_kp.setText("无");
                tv_into_kp.setEnabled(false);
            }else {
                tv_into_kp.setText("查看");
                tv_into_kp.setEnabled(true);
            }

            int error1 = feedbackStuConBean2.getTables().getTable().getRows().get(1).getErrorCount();
            if (error1 == 0){
                tv_out_kp.setText("无");
                tv_out_kp.setEnabled(false);
            }else {
                tv_out_kp.setText("查看");
                tv_out_kp.setEnabled(true);
            }


        }

        //本次作业情况情况
        int size1 = feedbackStuConBean2.getTables().getTable1().getRows().size();
        if (size1 > 0){
            String str = "";
            int count = 0;
            for (int i = 0; i < size1; i ++){
                str = str + feedbackStuConBean2.getTables().getTable1().getRows().get(i).getChaptername() + ",";
                count = count + feedbackStuConBean2.getTables().getTable1().getRows().get(i).getQidcount();
            }
            tv_current_total.setText(count + "");
            tv_current_kp.setText(str);
        }else {
            tv_current_total.setText("0");
            tv_current_kp.setText("无");
            tv_current_kp.setEnabled(false);
        }

        //上次作业情况
        int size2 = feedbackStuConBean2.getTables().getTable2().getRows().size();
        if (size2 > 0){
            int totalCount = 0;
            int rightCount = 0;
            int errorCount = 0;
            for (int i = 0; i < size1; i ++){
                totalCount = totalCount + feedbackStuConBean2.getTables().getTable2().getRows().get(i).getECount();
                rightCount = rightCount + feedbackStuConBean2.getTables().getTable2().getRows().get(i).getCurrCount();
                errorCount = errorCount + feedbackStuConBean2.getTables().getTable2().getRows().get(i).getErrorCount();

            }
            tv_last_total.setText(totalCount + "");
            tv_last_correct.setText(rightCount + "");
            tv_last_error.setText(errorCount + "");
            tv_last_kp.setEnabled(true);
            tv_last_kp.setText("查看");
            ll_last_work.setVisibility(View.VISIBLE);
            ll_last_work1.setVisibility(View.VISIBLE);
            ll_last_work2.setVisibility(View.VISIBLE);
            ll_last_work3.setVisibility(View.VISIBLE);
            et_last_comment.setVisibility(View.VISIBLE);
        }else {
            tv_last_kp.setEnabled(false);
            tv_last_kp.setText("无");
            ll_last_work.setVisibility(View.GONE);
            ll_last_work1.setVisibility(View.GONE);
            ll_last_work2.setVisibility(View.GONE);
            ll_last_work3.setVisibility(View.GONE);
            et_last_comment.setVisibility(View.GONE);
        }
    }

    /**
     * 知识点掌握情况星级展示选择
     */
    private void starShow(String starStatu){
        starVisbile();


        if (starStatu.equals("5")){
            ll_five_star.setVisibility(View.GONE);
            ll_five_star1.setVisibility(View.VISIBLE);
        }else if (starStatu.equals("4")){
            ll_four_star.setVisibility(View.GONE);
            ll_four_star1.setVisibility(View.VISIBLE);
        }else if (starStatu.equals("3")){
            ll_three_star.setVisibility(View.GONE);
            ll_three_star1.setVisibility(View.VISIBLE);
        }else if (starStatu.equals("2")){
            ll_two_star.setVisibility(View.GONE);
            ll_two_star1.setVisibility(View.VISIBLE);
        }else if (starStatu.equals("1")){
            ll_one_star.setVisibility(View.GONE);
            ll_one_star1.setVisibility(View.VISIBLE);
        }else {
            ll_one_star.setVisibility(View.GONE);
            ll_one_star1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 上次作业完成情况星级展示选择
     */
    private void lastStarShow(String lastStarStatu){

        lastStarVisbile();

        if (lastStarStatu.equals("5")){
            ll_last_five_star.setVisibility(View.GONE);
            ll_last_five_star1.setVisibility(View.VISIBLE);
        }else if (lastStarStatu.equals("4")){
            ll_last_four_star.setVisibility(View.GONE);
            ll_last_four_star1.setVisibility(View.VISIBLE);
        }else if (lastStarStatu.equals("3")){
            ll_last_three_star.setVisibility(View.GONE);
            ll_last_three_star1.setVisibility(View.VISIBLE);
        }else if (lastStarStatu.equals("2")){
            ll_last_two_star.setVisibility(View.GONE);
            ll_last_two_star1.setVisibility(View.VISIBLE);
        }else if (lastStarStatu.equals("1")){
            ll_last_one_star.setVisibility(View.GONE);
            ll_last_one_star1.setVisibility(View.VISIBLE);
        }else {
            ll_last_one_star.setVisibility(View.GONE);
            ll_last_one_star1.setVisibility(View.VISIBLE);
        }
    }



    /**
     * 获取入门测、出门考错误知识点
     */
    private void getErroeKP(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("StudentNo", studentNo);
            jsonObject.put("title", workType);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetErroeKPMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未获取到错误知识点");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    ErrorKPBean errorKPBean = gson.fromJson(jsonObject.toString(), ErrorKPBean.class);
                                    if (errorKPBean != null) {
                                        knowledgeList.clear();
                                        knowledgeList.addAll(errorKPBean.getTables().getTable().getRows());
                                        showPopuWindow();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未获取到错误知识点");
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
     * 获取上次作业错误知识点
     */
    private void getErroeKP1(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("StudentNo", studentNo);
            jsonObject.put("TeacherNo", userCode);
            jsonObject.put("subjects", subject);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetErroeKPMN1;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未获取到错误知识点");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    ErrorKPBean errorKPBean = gson.fromJson(jsonObject.toString(), ErrorKPBean.class);
                                    if (errorKPBean != null) {
                                        knowledgeList.clear();
                                        knowledgeList.addAll(errorKPBean.getTables().getTable().getRows());
                                        showPopuWindow();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未获取到错误知识点");
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


}
