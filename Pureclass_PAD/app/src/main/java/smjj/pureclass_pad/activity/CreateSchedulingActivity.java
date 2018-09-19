package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.ClassListBean;
import smjj.pureclass_pad.beans.ScdchoolBean;
import smjj.pureclass_pad.beans.StudentListBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.FormatUtils;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.view.AutoHeightGridView;
//自建排课界面
public class CreateSchedulingActivity extends BaseActivity implements View.OnClickListener{

    private AutoHeightGridView gridView;
    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private LinearLayout ll_go_class_data, ll_class_start_time, ll_class_end_time;
    private TextView tv_go_class_data, tv_class_start_time, tv_class_end_time, tv_class_hour;
    private LinearLayout ll_class_name, ll_school_name;
    private TextView tv_teacher_name, tv_grade_subjects, tv_class_name, tv_school_name;
    private Button save_bt;
    private RelativeLayout rl_back, rl_home, rl_add;

    private SharedPreferences spConfig;
    private TimePickerView pvTime;
    private TimePickerView pvTime1;
    private Context context;
    private Handler mHandler;
    private CommonAdapter adapter;


    private List<StudentListBean.TablesBean.TableBean.RowsBean> listData;
    private boolean isStartTime;
    private List<ClassListBean.TablesBean.TableBean.RowsBean> classNameLiset;
    private List<ScdchoolBean.TablesBean.TableBean.RowsBean> schoolNameLiset;
    private List<StudentListBean.TablesBean.TableBean.RowsBean> schedulingStudentList;

    private String teacherName;
    private String userCode;
    private String classId, subject, classType;
    private String F0006, F0007, grade, school;
    private String startTime, endTime, hours, studentNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scheduling);
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;


        findView();


    }

    private void findView() {
        gridView = (AutoHeightGridView) findViewById(R.id.gridview);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        ll_go_class_data = (LinearLayout) findViewById(R.id.ll_go_class_data);
        ll_class_start_time = (LinearLayout) findViewById(R.id.ll_class_start_time);
        ll_class_end_time = (LinearLayout) findViewById(R.id.ll_class_end_time);
        tv_go_class_data = (TextView) findViewById(R.id.tv_go_class_data);
        tv_class_start_time = (TextView) findViewById(R.id.tv_class_start_time);
        tv_class_end_time = (TextView) findViewById(R.id.tv_class_end_time);
        tv_class_hour = (TextView) findViewById(R.id.tv_class_hour);
        ll_class_name = (LinearLayout) findViewById(R.id.ll_class_name);
        ll_school_name = (LinearLayout) findViewById(R.id.ll_school_name);
        tv_teacher_name = (TextView) findViewById(R.id.tv_teacher_name);
        tv_grade_subjects = (TextView) findViewById(R.id.tv_grade_subjects);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        save_bt = (Button) findViewById(R.id.log_bt);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);


        title_tv.setText("自建排课");
        tv_go_class_data.setText(FormatUtils.getToday());
        teacherName = spConfig.getString(SPCommonInfoBean.userName, "");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        tv_teacher_name.setText(teacherName);

        setTime();
        setTime1();

        listData = new ArrayList<>();


        classNameLiset = new ArrayList<>();
        schoolNameLiset = new ArrayList<>();

        schedulingStudentList = new ArrayList<>();

        adapter = new CommonAdapter<StudentListBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final StudentListBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getUserName());
                checkBox.setTextColor(Color.WHITE);
                checkBox.setChecked(true);
                if (!schedulingStudentList.contains(rowsBean)){
                    schedulingStudentList.add(rowsBean);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            schedulingStudentList.add(rowsBean);
                            checkBox.setTextColor(Color.WHITE);
                        }else {
                            schedulingStudentList.remove(rowsBean);
                            checkBox.setTextColor(Color.BLACK);
                        }
                    }
                });


            }
        };


        gridView.setAdapter(adapter);
        ll_go_class_data.setOnClickListener(this);
        ll_class_end_time.setOnClickListener(this);
        ll_class_start_time.setOnClickListener(this);
        ll_class_name.setOnClickListener(this);
        ll_school_name.setOnClickListener(this);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        save_bt.setOnClickListener(this);

        requestClassListData();
        requestStudentListData();
        requestSchoolListData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                ActivityManage.backToNewCheckCode(context);
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.ll_go_class_data:
                pvTime.show();
                break;
            case R.id.ll_class_start_time:
                isStartTime = true;
                pvTime1.show();
                break;
            case R.id.ll_class_end_time:
                isStartTime = false;
                pvTime1.show();
                break;
            case R.id.ll_class_name:

                showPopupWindow();
                break;
            case R.id.ll_school_name:
                showPopupWindow1();
                break;
            case R.id.log_bt:
                //保存信息到服务器完成并关闭界面
                addSchedulingInfo();
                break;
        }
    }

    private void setTime(){
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String stringTime = FormatUtils.getStringTime(date, "yyyy-MM-dd");
                tv_go_class_data.setText(stringTime);

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
                .setDate(Calendar.getInstance())
                .build();
    }

    private void setTime1(){
        pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String stringTime = FormatUtils.getStringTime(date, "HH:mm");
                if (isStartTime){
                    tv_class_start_time.setText(stringTime);
                }else {
                    tv_class_end_time.setText(stringTime);
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
                                pvTime1.returnData();
                                pvTime1.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime1.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{false,false,false,true,true,false})
                .isDialog(true)
                .isCenterLabel(false)
                .setDividerColor(Color.RED)
                .setDate(Calendar.getInstance())
                .build();
    }

    //班级弹出框
    private void showPopupWindow(){
        View view;
        boolean isScreenTop = PopupWindowUtil.isScreenTOP(context, ll_class_name);
        if (isScreenTop){
            view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        }
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,6,2,0.5f,-1);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<ClassListBean.TablesBean.TableBean.RowsBean>(context, classNameLiset, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, ClassListBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.setText(R.id.item_pop_tv, rowsBean.getF0009());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_class_name.setText(classNameLiset.get(i).getF0009());
                F0006 = classNameLiset.get(i).getF0006();
                F0007 = classNameLiset.get(i).getF0007();
                tv_grade_subjects.setText(classNameLiset.get(i).getF0007() + classNameLiset.get(i).getF0008());
                classId = classNameLiset.get(i).getF0001();
                subject = classNameLiset.get(i).getF0008();
                classType = classNameLiset.get(i).getF0010();
                popupWindow.dismiss();
            }
        });

        if (isScreenTop){
            PopupWindowUtil.showAtLoactionBottom(ll_class_name);
        }else {
            PopupWindowUtil.showAtLoactionTop(ll_class_name);
        }

    }


    //校区弹出框
    private void showPopupWindow1(){
        View view;
        boolean isScreenTop = PopupWindowUtil.isScreenTOP(context, ll_school_name);
        if (isScreenTop){
            view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        }
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,6,3,0.5f,-1);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<ScdchoolBean.TablesBean.TableBean.RowsBean>(context, schoolNameLiset, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, ScdchoolBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.setText(R.id.item_pop_tv, rowsBean.getDepartName());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                school = schoolNameLiset.get(i).getDepartName();
                tv_school_name.setText(school);
                popupWindow.dismiss();
            }
        });

        if (isScreenTop){
            PopupWindowUtil.showAtLoactionBottom(ll_school_name);
        }else {
            PopupWindowUtil.showAtLoactionTop(ll_school_name);
        }

    }



    private void requestStudentListData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "教师编号为空，请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetStrdentListMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无学生");
                                } else {
                                    Gson gson = new Gson();
                                    StudentListBean studentListBean = gson.fromJson(jsonObject.toString(), StudentListBean.class);
                                    if (studentListBean != null) {
                                        listData.addAll(studentListBean.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无学生，请先添加学生");

                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无学生");
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


    //查询班级列表
    private void requestClassListData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "教师编号为空，请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetClassListMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无班级");
                                } else {
                                    Gson gson = new Gson();
                                    ClassListBean classListBean = gson.fromJson(jsonObject.toString(), ClassListBean.class);
                                    if (classListBean != null) {
                                        classNameLiset.addAll(classListBean.getTables().getTable().getRows());
                                        if (classNameLiset.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无班级，请先添加班级");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无班级");
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


    //查询校区
    private void requestSchoolListData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "教师编号为空，请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetSchoolListMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无校区");
                                } else {
                                    Gson gson = new Gson();
                                    ScdchoolBean scdchoolBean = gson.fromJson(jsonObject.toString(), ScdchoolBean.class);
                                    if (scdchoolBean != null) {
                                        schoolNameLiset.addAll(scdchoolBean.getTables().getTable().getRows());
                                        if (schoolNameLiset.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无校区，请先添加校区");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无校区");
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







    private void addSchedulingInfo() {

        getStudentNO();
        startTime = tv_go_class_data.getText().toString() + " " + tv_class_start_time.getText().toString();
        endTime = tv_go_class_data.getText().toString() + " " + tv_class_end_time.getText().toString();
        hours = tv_class_hour.getText().toString();

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
            if (F0007 == null && F0007.equals("")){
                return;
            }

            if (classType.equals("一对一")){
                if (schedulingStudentList.size()>1){
                    AlertDialogUtil.showAlertDialog(context,"提示","一对一最多只能选择一个学生");
                    return;
                }
            }
            if (classType.equals("小组课") || classType.equals("一对多")){
                if (schedulingStudentList.size()>10){
                    AlertDialogUtil.showAlertDialog(context,"提示","小组课最多只能选择十个学生");
                    return;
                }
            }

            grade = F0007;
            jsonObject.put("UserCode", userCode);
            jsonObject.put("UserName", teacherName);
            jsonObject.put("classid", classId);
            jsonObject.put("subjects", subject);
            jsonObject.put("timeStar", startTime);
            jsonObject.put("timeEnd", endTime);
            jsonObject.put("hours", hours);
            jsonObject.put("StuNo", studentNo + ",");
            jsonObject.put("classType", classType);
            jsonObject.put("Grade", grade);
            jsonObject.put("DepartName", school);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.AddSchedulingMethodName;
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
                                    Toast.makeText(CreateSchedulingActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    setResult(1, intent);
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

    private void getStudentNO(){
        studentNo = "";
        for (int i = 0 ; i < schedulingStudentList.size(); i ++){
            if (i == 0){
                studentNo = schedulingStudentList.get(i).getUserCode();
            }else {
                studentNo = studentNo + "," + schedulingStudentList.get(i).getUserCode();
            }
        }

    }

}
