package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import smjj.pureclass_pad.beans.DataGramBean2;
import smjj.pureclass_pad.beans.SchedulingTableBean;
import smjj.pureclass_pad.beans.SpeachBean2;
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

//二期备课内容页
public class PrepareContentActivity1 extends BaseActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    private GridView gridView;
    private TextView onBack;
    private ImageView iv_home, iv_add;
    private TextView title_tv;

    private TextView empty;
    private RelativeLayout rl_back1, rl_home, rl_add;

    private TextView tv_courser, tv_class_name, tv_school_name, tv_stu_name;
    private ImageView iv_stu_name;
    private LinearLayout ll_sdu_time;
    private TextView tv_sdu_time;

    private Context context;
    private Handler mHandler;
    private SharedPreferences spConfig;

    private List<SpeachBean2.TablesBean.TableBean.RowsBean> listData;
    private List<SchedulingTableBean.TablesBean.TableBean.RowsBean> sduListData;
    private CommonAdapter adapter;

    private List<DataGramBean2.TablesBean.TableBean.RowsBean> dataGramList;
    private int selectGram = 0;

    private String classId, startTime;
    private String selectClassId, selectTime;
    private String season;
    private String bookID;
    private String grade, subject;
    private String speakId;

    private String userCode;
    private String userName;
    private String typleUser;
    private String speakName;

    //1代表备课阶段，2代表上课阶段
    private String enterMark;
    private String selectType;//班课类型
    private String isDualTeacher;//是否双师 0是双师 1非双师
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;

    private String className, schoolName, materialName, stuName, classNo, materialNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_content1);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        userName = spConfig.getString(SPCommonInfoBean.userName,"");
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate,"");
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        enterMark = getIntent().getStringExtra("enterMark");
        selectType = getIntent().getStringExtra("SelectType");
        className = getIntent().getStringExtra("ClassName");
        schoolName = getIntent().getStringExtra("SchoolName");
        materialName = getIntent().getStringExtra("MaterialName");
        materialNo = getIntent().getStringExtra("MaterialNo");
        stuName = getIntent().getStringExtra("StuName");
        classNo = getIntent().getStringExtra("ClassNo");
        grade = getIntent().getStringExtra("Grade");
        subject = getIntent().getStringExtra("Subject");

        findView();
    }

    private void findView() {
        gridView = (GridView) findViewById(R.id.scheduling_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        empty = (TextView) findViewById(R.id.empty);
        ll_sdu_time = (LinearLayout) findViewById(R.id.ll_sdu_time);
        tv_sdu_time = (TextView) findViewById(R.id.tv_sdu_time);
        tv_courser = (TextView) findViewById(R.id.tv_courser);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        iv_stu_name = (ImageView) findViewById(R.id.iv_stu_name);


        tv_courser.setText(materialName);
        tv_class_name.setText(className);
        tv_school_name.setText(schoolName);
        tv_stu_name.setText(stuName);
        tv_sdu_time.setText("");


        //设置标题加粗
        TextPaint tp = title_tv.getPaint();
        tp.setFakeBoldText(true);
        tp.setTextSize(30);
        if (enterMark.equals("2")){
            title_tv.setText("上课内容");
            ll_sdu_time.setVisibility(View.GONE);
        }else {
            title_tv.setText("备课内容");
        }
        if (selectType.equals("一对一")){
            iv_stu_name.setVisibility(View.GONE);
            tv_stu_name.setVisibility(View.GONE);
        }
        rl_back1.setVisibility(View.VISIBLE);

        listData = new ArrayList<>();
        sduListData = new ArrayList<>();
        dataGramList = new ArrayList<>();

        adapter = new CommonAdapter<SpeachBean2.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_speech_gv) {
            @Override
            public void convert(ViewHolder holder, final SpeachBean2.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);
                String str = rowsBean.getSpeakName();
                if (str != null && str.length() > 3){
                    if (i >= 10){
                        holder.setText(R.id.tv_speech1, str.substring(0,4));
                        holder.setText(R.id.tv_speech_name, str.substring(4));
                    }else {
                        holder.setText(R.id.tv_speech1, str.substring(0,3));
                        holder.setText(R.id.tv_speech_name, str.substring(3));
                    }
                }else {
                    holder.setText(R.id.tv_speech1, str);
                    holder.setText(R.id.tv_speech_name, "");
                }

                ImageView iv_speech = holder.getView(R.id.iv_speech);
                TextView tv_time_date = holder.getView(R.id.tv_time_date);
                TextView tv_time_hour = holder.getView(R.id.tv_time_hour);

                int classStatus = rowsBean.getBeginStatus();//上课状态
                int prepareStatus = rowsBean.getLessonStatus();//备课状态

                if (classStatus == 1){
                    iv_speech.setImageResource(R.drawable.yishangke);
                    tv_time_date.setVisibility(View.VISIBLE);
                    tv_time_hour.setVisibility(View.VISIBLE);
                    if (rowsBean.getClassTime() != null && !rowsBean.getClassTime().equals("")) {
                        tv_time_date.setText(rowsBean.getClassTime().substring(0, 10));
                        tv_time_hour.setText(rowsBean.getClassTime().substring(11,16));
                    }
                }else {
                    if (prepareStatus == 0){
                        iv_speech.setImageResource(R.drawable.weibeike);
                        tv_time_date.setVisibility(View.INVISIBLE);
                        tv_time_hour.setVisibility(View.INVISIBLE);
                    }else {
                        if (enterMark.equals("2")){//上课流程显示未上课
                            iv_speech.setImageResource(R.drawable.weishangke);
                        }else {
                            iv_speech.setImageResource(R.drawable.yibeike);
                        }
                        tv_time_date.setVisibility(View.VISIBLE);
                        tv_time_hour.setVisibility(View.VISIBLE);
                        if (rowsBean.getClassTime() != null && !rowsBean.getClassTime().equals("")) {
                            tv_time_date.setText(rowsBean.getClassTime().substring(0, 10));
                            tv_time_hour.setText(rowsBean.getClassTime().substring(11,16));
                        }
                    }
                }

            }
        };

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_add.setOnClickListener(this);
        ll_sdu_time.setOnClickListener(this);


        if (enterMark.equals("1") && isCertificate.equals("0")){
            //主讲老师备课查看讲，获取该班级下所有的讲
            getAllSpeachData();
        }else {
            getSpeachData();
        }

        if (enterMark.equals("1")){
            if (!isCertificate.equals("1")){
                requestInternalData();
                ll_sdu_time.setVisibility(View.VISIBLE);
            }else {
                ll_sdu_time.setVisibility(View.GONE);
            }
        }
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
            case R.id.ll_sdu_time:
                if (sduListData.size() == 0 ){
                   AlertDialogUtil.showAlertDialog(context, "提示", "近期内没有需要备课的排课");
                }else {
                    showPopupWindow();
                }

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int prepareStatus = listData.get(i).getLessonStatus();//备课状态
        if (prepareStatus == 1 ){//已备课
            classId = listData.get(i).getClassID();
            startTime = listData.get(i).getClassTime();
        }else {
            if (selectClassId == null || selectClassId.equals("")){
                AlertDialogUtil.showAlertDialog(context, "提示", "近期内没有需要备课的排课，无需备课");
                return;
            }
            classId = selectClassId;
            startTime = selectTime;
        }
        spConfig.edit().putString(SPCommonInfoBean.classId, classId).commit();
        String packageNo = listData.get(i).getPackageNo();

        speakId = listData.get(i).getSpeakID();
        speakName = listData.get(i).getSpeakName();
        if (enterMark.equals("1")){
            if (prepareStatus == 1 ){
                //校验失败 是指token校验失败
                Intent intent = new Intent(context, PrepareDetailsActivity1.class);
                intent.putExtra("SpeakId", speakId);
                intent.putExtra("SpeakName", speakName);
                intent.putExtra("SelectDataGram", "3");
                intent.putExtra("ClassName", className);
                intent.putExtra("SchoolName", schoolName);
                intent.putExtra("MaterialName", materialName);
                intent.putExtra("MaterialNo", materialNo);
                intent.putExtra("StuName", stuName);
                intent.putExtra("SelectType", selectType);
                intent.putExtra("ClassNo", classNo);
                intent.putExtra("Grade", grade);
                intent.putExtra("Subject", subject);
                intent.putExtra("PackageNo", packageNo);
                intent.putExtra("StartTime", startTime);
                intent.putExtra("IsPrepare", "1");
                startActivity(intent);
                finish();
            }else {
                getDataGram();
            }


        }else if (enterMark.equals("2")){

            Intent intent = new Intent(this, GoClassActivity.class);
            intent.putExtra("grade", grade);
            intent.putExtra("subject", subject);
            intent.putExtra("ClassNo", classNo);
            intent.putExtra("SpeakId", speakId);
            intent.putExtra("PackageNo", packageNo);
            startActivity(intent);

        }

    }


    private void showDataGramPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_select_gram, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        final TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        ListView lv = (ListView) view.findViewById(R.id.pop_lv);

        final CommonAdapter adapter1 = new CommonAdapter<DataGramBean2.TablesBean.TableBean.RowsBean>(context, dataGramList, R.layout.item_pop_select_gram) {
            @Override
            public void convert(ViewHolder holder, final DataGramBean2.TablesBean.TableBean.RowsBean rowsBean) {
                ImageView imageView = holder.getView(R.id.iv_data_gram);
                TextView textView1 = holder.getView(R.id.tv_data_gram_name);
                textView1.setText(rowsBean.getPackageName());
                final int i = dataGramList.indexOf(rowsBean);
                if (selectGram == i){
                    imageView.setVisibility(View.INVISIBLE);
                    textView1.setTextColor(Color.parseColor("#f6aa00"));
                }else {
                    imageView.setVisibility(View.INVISIBLE);
                    textView1.setTextColor(Color.parseColor("#888888"));
                }

                holder.getView(R.id.ll_pop_data_gram).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectGram = i;
                        notifyDataSetChanged();
                        Intent intent = new Intent(context, PrepareDetailsActivity1.class);
                        intent.putExtra("SpeakId", speakId);
                        intent.putExtra("SpeakName", speakName);
                        intent.putExtra("SelectDataGram", (i + 1) + "");
                        intent.putExtra("ClassName", className);
                        intent.putExtra("SchoolName", schoolName);
                        intent.putExtra("MaterialName", materialName);
                        intent.putExtra("MaterialNo", materialNo);
                        intent.putExtra("ClassNo", classNo);
                        intent.putExtra("StuName", stuName);
                        intent.putExtra("SelectType", selectType);
                        intent.putExtra("Grade", grade);
                        intent.putExtra("Subject", subject);
                        intent.putExtra("PackageNo",rowsBean.getPackageNo());
                        intent.putExtra("StartTime", startTime);
                        startActivity(intent);
                        popupWindow.dismiss();
                        finish();
                    }
                });

            }
        };

        lv.setAdapter(adapter1);

        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }

    //获该班级下所有的讲
    public void getAllSpeachData() {
        String UserCode = spConfig.getString(SPCommonInfoBean.userCode, "");

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (UserCode == null || UserCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
//            if (enterMark.equals("1")){
                jsonObject.put("UserName", userName);
//            }else {
//                jsonObject.put("SSH", isDualTeacher);
//            }
            jsonObject.put("ClassNo", classNo);
            if (isDualTeacher.equals("0")){
                jsonObject.put("IsCertificate", isCertificate);
            }else {
                jsonObject.put("IsCertificate", "2");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = "";
//            if (enterMark.equals("1")){
                methodName = Constants.GetSpeachMN2;
//            }else {
//                methodName = Constants.GetSpeachClassMN2;
//            }

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
                                    if (isCertificate.equals("1")){
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该班级主讲老师还未进行过备课");
                                    }else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该班级获取不到讲信息");
                                    }
                                } else {
                                    Gson gson = new Gson();
                                    SpeachBean2 speachBean2 = gson.fromJson(jsonObject.toString(), SpeachBean2.class);
                                    if (speachBean2 != null) {
                                        listData.addAll(speachBean2.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            if (isCertificate.equals("1")){
                                                AlertDialogUtil.showAlertDialog(context, "提示", "该班级主讲老师还未进行过备课");
                                            }else {
                                                AlertDialogUtil.showAlertDialog(context, "提示", "该课程下没有章节信息");
                                            }
                                        }
                                    } else {
                                        if (isCertificate.equals("1")){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该班级主讲老师还未进行过备课");
                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该课程下没有章节信息");
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");

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


    //获取讲名
    public void getSpeachData() {
        String UserCode = spConfig.getString(SPCommonInfoBean.userCode, "");

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (UserCode == null || UserCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
//            if (enterMark.equals("1")){
//                jsonObject.put("UserName", userName);
//            }else {
                jsonObject.put("SSH", isDualTeacher);
//            }
            jsonObject.put("ClassNo", classNo);
            if (isDualTeacher.equals("0")){
                jsonObject.put("IsCertificate", isCertificate);
            }else {
                jsonObject.put("IsCertificate", "2");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = "";
//            if (enterMark.equals("1")){
//                methodName = Constants.GetSpeachMN2;
//            }else {
                methodName = Constants.GetSpeachClassMN2;
//            }

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
                                    if (isCertificate.equals("1")){
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该班级主讲老师还未进行过备课");
                                    }else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该班级获取不到讲信息");
                                    }
                                } else {
                                    Gson gson = new Gson();
                                    SpeachBean2 speachBean2 = gson.fromJson(jsonObject.toString(), SpeachBean2.class);
                                    if (speachBean2 != null) {
                                        listData.addAll(speachBean2.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            if (isCertificate.equals("1")){
                                                AlertDialogUtil.showAlertDialog(context, "提示", "该班级主讲老师还未进行过备课");
                                            }else {
                                                AlertDialogUtil.showAlertDialog(context, "提示", "该课程下没有章节信息");
                                            }
                                        }
                                    } else {
                                        if (isCertificate.equals("1")){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该班级主讲老师还未进行过备课");
                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该课程下没有章节信息");
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");

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









    //获取资料包
    public void getDataGram() {

        dataGramList.clear();

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("teacherNo", userCode);
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("courseNo", materialNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetGramNameMN2;

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
                                    Intent intent = new Intent(context, PrepareDetailsActivity1.class);
                                    intent.putExtra("SpeakId", speakId);
                                    intent.putExtra("SpeakName", speakName);
                                    intent.putExtra("SelectDataGram", "1");
                                    intent.putExtra("ClassName", className);
                                    intent.putExtra("SchoolName", schoolName);
                                    intent.putExtra("MaterialName", materialName);
                                    intent.putExtra("MaterialNo", materialNo);
                                    intent.putExtra("ClassNo", classNo);
                                    intent.putExtra("StuName", stuName);
                                    intent.putExtra("SelectType", selectType);
                                    intent.putExtra("Grade", grade);
                                    intent.putExtra("Subject", subject);
                                    intent.putExtra("StartTime", startTime);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Gson gson = new Gson();
                                    DataGramBean2 dataGramBean2 = gson.fromJson(jsonObject.toString(), DataGramBean2.class);
                                    if (dataGramBean2 != null) {
                                        //创建新建和基础包选项
                                        DataGramBean2.TablesBean.TableBean.RowsBean rowsBean = new DataGramBean2.TablesBean.TableBean.RowsBean();
                                        rowsBean.setPackageName("基础包");
                                        rowsBean.setPackageNo("0");
                                        DataGramBean2.TablesBean.TableBean.RowsBean rowsBean1 = new DataGramBean2.TablesBean.TableBean.RowsBean();
                                        rowsBean1.setPackageName("新建");
                                        rowsBean1.setPackageNo("1");
                                        dataGramList.add(rowsBean);
                                        dataGramList.add(rowsBean1);
                                        dataGramList.addAll(dataGramBean2.getTables().getTable().getRows());
                                        showDataGramPopu();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该章节无备课包");
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");

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

    //请求未备课排课列表
    public void requestInternalData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("teacherNo", userCode);
            jsonObject.put("classNo", classNo);
//            jsonObject.put("teacherNo", "US0002866");
//            jsonObject.put("classNo", "CL000026896");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetNoPreSduMN2;

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
                                    tv_sdu_time.setText("");
                                    selectClassId = "";
                                    selectTime = "";
                                    AlertDialogUtil.showAlertDialog(context, "提示", "在近期内您没有需要备课的排课记录");
                                } else {
                                    Gson gson = new Gson();
                                    SchedulingTableBean schedulingTableBean = gson.fromJson(jsonObject.toString(), SchedulingTableBean.class);
                                    if (schedulingTableBean != null) {
                                        sduListData.addAll(schedulingTableBean.getTables().getTable().getRows());
                                        FormatUtils.getListRank(sduListData);
                                        if (sduListData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "在近期内您没有需要备课的排课记录，无需备课");
                                            ll_sdu_time.setClickable(false);
                                            tv_sdu_time.setText("");
                                            selectClassId = "";
                                            selectTime = "";
                                        }else {
                                            String classTime = sduListData.get(0).getTimeStar();
                                            if (classTime != null && classTime.length() > 16){
                                                tv_sdu_time.setText(classTime.substring(0, 16));
                                            }else {
                                                tv_stu_name.setText(classTime);
                                            }
                                            selectClassId = sduListData.get(0).getClassId();
                                            selectTime = classTime;
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "在近期内您没有需要备课的排课记录");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");

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


    private void showPopupWindow(){
        View view;
        boolean isScreenTop = PopupWindowUtil.isScreenTOP(context, ll_sdu_time);
        if (isScreenTop){
            view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        }
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,5,2,0.5f,-1);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<SchedulingTableBean.TablesBean.TableBean.RowsBean>(context, sduListData, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, SchedulingTableBean.TablesBean.TableBean.RowsBean rowsBean) {

                TextView textView = holder.getView(R.id.item_pop_tv);
                String classTime = rowsBean.getTimeStar();
                if (classTime != null && classTime.length() > 16){
                    classTime = classTime.substring(0,16);
                }

                textView.setText(classTime);

                if (classTime.equals(tv_sdu_time.getText().toString())){
                    textView.setTextColor(Color.parseColor("#f6aa00"));
                }else {
                    textView.setTextColor(Color.parseColor("#666666"));
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String classTime = sduListData.get(i).getTimeStar();
                if (classTime != null && classTime.length() > 16){
                    tv_sdu_time.setText(classTime.substring(0, 16));
                }else {
                    tv_stu_name.setText(classTime);
                }
                selectClassId =sduListData.get(i).getClassId();
                selectTime = classTime;
                popupWindow.dismiss();
            }
        });

        if (isScreenTop){
            PopupWindowUtil.showAtLoactionBottom(ll_sdu_time);
        }else {
            PopupWindowUtil.showAtLoactionTop(ll_sdu_time);
        }
    }

}
