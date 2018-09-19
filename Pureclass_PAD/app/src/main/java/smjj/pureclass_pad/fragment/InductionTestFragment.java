package smjj.pureclass_pad.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.adapter.ReleaseWorkeAdapter;
import smjj.pureclass_pad.beans.InductionTestExercisesBean;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/15.
 */
//入门测发布试卷碎片
public class InductionTestFragment extends Fragment implements View.OnClickListener{

    private XListView listView;
    private Button release_bt;

    private LinearLayout ll_release_bt;

    private Context context;

    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> listData;

//    private CommonAdapter adapter;
    private ReleaseWorkeAdapter adapter;
    private GoClassActivity activity;

    private Button start_bt;
    private Button stop_bt;
    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> listData1;
    private int testNO;
    //判断试题是否发布，在开始、停止答题时用来判断获取试题ID的字段
    private boolean isRelease;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private String grade, subject;
    private String schoolNo;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (GoClassActivity) getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        grade =spConfig.getString(SPCommonInfoBean.selectGrade, "");
        subject = spConfig.getString(SPCommonInfoBean.selectSubject, "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_induction_test, container, false);

        findView(contentView);

        return contentView;
    }

    private void findView(View view){
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        release_bt = (Button) view.findViewById(R.id.release_bt);
        start_bt = (Button) view.findViewById(R.id.start_bt);
        stop_bt = (Button) view.findViewById(R.id.stop_bt);
        ll_release_bt = (LinearLayout) view.findViewById(R.id.ll_release_bt);

        release_bt.setText("下一题");
        stop_bt.setEnabled(false);
        stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);

        listData1 = new ArrayList<>();
        testNO = 0;
        listData = new ArrayList<>();


        adapter = new ReleaseWorkeAdapter(context, listData, grade, "1");

        listView.setAdapter(adapter);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(false);
        release_bt.setOnClickListener(this);
        start_bt.setOnClickListener(this);
        stop_bt.setOnClickListener(this);

        //即是双师又是助教不能开始和结束答题
        if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
           start_bt.setVisibility(View.GONE);
           stop_bt.setVisibility(View.GONE);
        }

        checkedReleaseInductionTest();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.release_bt:
                //点击下一题，
                testNO = testNO + 1;
//                stopAnswer();
                Log.d("2222222222222", testNO + "      " + listData1.size());
                if (testNO == listData1.size()-1){
                    release_bt.setEnabled(false);
                    release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);

                }

                listData.clear();
                listData.add(listData1.get(testNO));
                adapter.setData(listData);
                adapter.notifyDataSetChanged();

                start_bt.setEnabled(true);
                start_bt.setBackgroundResource(R.drawable.shape_bg);
                stop_bt.setEnabled(false);
                stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);

                break;

            case R.id.start_bt:
                //开始答题
                startAnswer();
                break;
                case R.id.stop_bt:
                //结束答题
                stopAnswer();
                break;


        }

    }


    //请求入门测试题
    private void requestInductionTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("KnowledgeUID","962A721A-96EC-46ED-A6B2-3E426F0C7F06");
            jsonObject.put("pageindex","1");
            jsonObject.put("UserCode",userCode);
//            jsonObject.put("GradeName","初一");
//            jsonObject.put("Subject","数学");
            jsonObject.put("GradeName",grade);
            jsonObject.put("Subject",subject);
            jsonObject.put("LessonID",classId);
            jsonObject.put("TestType","1");
            jsonObject.put("PackageNo",activity.packageNo);
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.GetClassExercMN2;


            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            activity.showLoading1();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    activity.closeLoading();
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "本节课没有入门测试题");
                                } else {
                                    Gson gson = new Gson();
                                    InductionTestExercisesBean inductionTestExercisesBean = gson.fromJson(jsonObject.toString(), InductionTestExercisesBean.class);
                                    if (inductionTestExercisesBean != null) {
                                        listData1.addAll(inductionTestExercisesBean.getTables().getTable().getRows());
//                                        CommonWay.testSort(listData);

                                        if (listData1.size() == 0){
                                            //只有一题时
                                            release_bt.setEnabled(false);
                                            release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);

                                            start_bt.setEnabled(false);
                                            start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                            stop_bt.setEnabled(false);
                                            stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                            AlertDialogUtil.showAlertDialog(context, "提示", "本节课没有入门测试题");
                                            return;
                                        }
                                        if (listData1.size() == 1){
                                            //只有一题时
                                            release_bt.setEnabled(false);
                                            release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                        }
                                        listData.add(listData1.get(testNO));
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "本节课没有入门测试题");
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

    //发布入门测试题
    public void releaseInductionTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode",userCode);
            jsonObject.put("LessonID",classId);
//            jsonObject.put("LessonID","1000158645");
            jsonObject.put("TestType","1");
            jsonObject.put("StudentNo","");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = "";
            if (activity.isDualTeacher.equals("0")){
                methodName = Constants.ReleaseTestMN2;
            }else {
                methodName = Constants.ReleaseExercisesMethodName;
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            activity.showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    activity.closeLoading();
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "作业重复发布");
                               } else {
                                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                                    activity.rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
                                    activity.canReleaseTest = false;
                                    activity.startTimer();
                                    activity.isStopeAnswer = "1";
                                    adapter.IsReleaseTest(true);
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

    //请求入门测、出门考、巩固练习、深化应用已发布的试题
    private void requestTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonID", classId);

            jsonObject.put("TestType", "1");

            if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

//            methodName = Constants.GetClassExercMN2;
            methodName = Constants.QueryAnswerMN2;


            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            activity.showLoading1();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    activity.closeLoading();
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
                                } else {
                                    Gson gson = new Gson();
                                    InductionTestExercisesBean inductionTestExercisesBean = gson.fromJson(jsonObject.toString(), InductionTestExercisesBean.class);
                                    if (inductionTestExercisesBean != null) {
                                        listData1.addAll(inductionTestExercisesBean.getTables().getTable().getRows());
//                                        CommonWay.testSort(listData);
                                        if (listData1.size() == 0){
                                            release_bt.setEnabled(false);
                                            release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);

                                            start_bt.setEnabled(false);
                                            start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                            stop_bt.setEnabled(false);
                                            stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
                                            return;
                                        }
                                        if (listData1.size() == 1){
                                            //只有一题时

                                            release_bt.setEnabled(false);
                                            release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                        }
                                        listData.add(listData1.get(testNO));
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
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


    //检查入门测试题是否已经发布
    private void checkedReleaseInductionTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode",userCode);
            jsonObject.put("LessonID",classId);
            jsonObject.put("Type","1");
            if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.CheckedReleaseMethodName;

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

                                if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                                        //即是双师又是助教则不允许发布作业
                                        activity.rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
                                        activity.canReleaseTest = false;
                                        adapter.IsReleaseTest(true);
                                    }else {
                                        activity.rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release));
                                        activity.canReleaseTest = true;
                                        adapter.IsReleaseTest(false);
                                    }
                                    isRelease = false;
                                    requestInductionTest();

                                }else if (resultvalue.equals("1")) {
                                    //已经发布过了
                                    activity.rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
                                    activity.canReleaseTest = false;
                                    adapter.IsReleaseTest(true);
                                    isRelease = true;
                                    requestTest();
                                } else if (resultvalue.equals("0")){
                                    isRelease = false;
                                    requestInductionTest();
                                    if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                                        //即是双师又是助教则不允许发布作业
                                        activity.rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
                                        activity.canReleaseTest = false;
                                        adapter.IsReleaseTest(true);
                                    }else {
                                        activity.rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release));
                                        activity.canReleaseTest = true;
                                        adapter.IsReleaseTest(false);
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


    //检查学生是否签到
    public void checkSign(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("ClassID", classId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckSignMethodName;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            activity.showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    activity.closeLoading();
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "还未进行学生签到，请先签到再发布试题");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    releaseInductionTest();
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


    //开始答题有答题器的情况下
    public void startAnswer(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            String test_no = "";
            if (!isRelease){
                test_no = listData1.get(testNO).getID();
            }else {
                test_no = listData1.get(testNO).getQID();
            }



            if (test_no == null || test_no.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("TestNo", test_no);
            jsonObject.put("Releaser", userCode);
            jsonObject.put("TrunkNo", activity.classNo);
            jsonObject.put("Period", "1");
            jsonObject.put("ReplyStatus", "1");//1代表题库代替 2代表临时答题

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LCllidkerStartMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            activity.showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    activity.closeLoading();
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
                                    Toast.makeText(context, "开始答题", Toast.LENGTH_SHORT).show();

                                    stop_bt.setEnabled(true);
                                    stop_bt.setBackgroundResource(R.drawable.shape_bg);

                                    start_bt.setEnabled(false);
                                    start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                }else {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();                                }
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

    //停止答题有答题器的情况下
    public void stopAnswer(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }

            String test_no = "";
            if (!isRelease){
                test_no = listData1.get(testNO).getID();
            }else {
                test_no = listData1.get(testNO).getQID();
            }

            if (test_no == null || test_no.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("TestNo", test_no);
            jsonObject.put("Releaser", userCode);
            jsonObject.put("TrunkNo", activity.classNo);
            jsonObject.put("Period", "1");
            jsonObject.put("PeriodText", "入门测");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LCllidkerStopMN2;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
            activity.showLoading();
            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {
                    activity.closeLoading();
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
                                    stop_bt.setEnabled(false);
                                    stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, "停止答题失败", Toast.LENGTH_SHORT).show();
                                }else {
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

}
