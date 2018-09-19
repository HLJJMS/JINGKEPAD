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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import smjj.pureclass_pad.adapter.ItemBankAdapter;
import smjj.pureclass_pad.beans.StuSetWorkBean;
import smjj.pureclass_pad.beans.TestBasketBean;
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
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

//试题篮界面
public class ItemBankActivity extends BaseActivity implements View.OnClickListener{

    private XListView listView;
    private TextView onBack;
    private ImageView iv_home, iv_back;
    private TextView title_tv;
    private Button bt_create_work, bt_select_work, bt_back_scheduling, bt_clear_work;

    private Context context;
    private Handler mHandler;

    private List<TestBasketBean.TablesBean.TableBean.RowsBean> listData;
    private ItemBankAdapter adapter;

    //1 代表布置作业， 2代表测评组卷
    private String enterMark;

    private List<StuSetWorkBean.RowsBean> studentList;
    private List<StuSetWorkBean.RowsBean> selectStudentList;

    private String isDualTeacher;//是否双师 0是双师 1非双师
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教

    private String grade, subject, parentID, knowledgeID;
    private int pageNum = 1;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;

    private String studentNo;
    private String schoolNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_bank);
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
        grade = getIntent().getStringExtra("GradeName");

        findView();
    }

    private void findView() {
        listView = (XListView) findViewById(R.id.scheduling_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        bt_create_work = (Button) findViewById(R.id.bt_create_work);
        bt_select_work = (Button) findViewById(R.id.bt_select_work);
        bt_back_scheduling = (Button) findViewById(R.id.bt_back_scheduling);
        bt_clear_work = (Button) findViewById(R.id.bt_clear_work);

        iv_home.setVisibility(View.GONE);

        title_tv.setText("试题篮");

        listData = new ArrayList<>();
        studentList = new ArrayList<>();

        selectStudentList = new ArrayList<>();

        adapter = new ItemBankAdapter(context, listData, grade);

        listView.setAdapter(adapter);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(false);
        onBack.setOnClickListener(this);
        bt_create_work.setOnClickListener(this);
        bt_select_work.setOnClickListener(this);
        bt_back_scheduling.setOnClickListener(this);
        bt_clear_work.setOnClickListener(this);

        requestBskList();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.onBack:
                finish();
                break;
            case R.id.bt_create_work:
                studentList.clear();
                getSetWorkStu();
                break;
            case R.id.bt_select_work:
                finish();
                break;
            case R.id.bt_back_scheduling:
                ActivityManage.backToActivity();
                break;
            case R.id.bt_clear_work:
                AlertDialogUtil.showAlertDialog(context, "提示", "是否确定要清空试题", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void positionClick() {
                        adapter.deleteTest(1, 0);
                    }

                    @Override
                    public void positionClick(String content) {

                    }

                    @Override
                    public void negetiveClick() {

                    }
                });
                break;
        }
    }



    private void showStudentPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_student_view, null);

//        final Map<Integer, Boolean> isCheckedMap = new HashMap<>();
//        for (int i = 0; i < studentList.size(); i ++){
//            isCheckedMap.put(i, true);
//        }
        selectStudentList.clear();
        selectStudentList.addAll(studentList);
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        final TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        Button saveBt = (Button) view.findViewById(R.id.log_bt);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close_pop);
        saveBt.setText("发  布");
        saveBt.setVisibility(View.VISIBLE);
        iv_close.setVisibility(View.VISIBLE);
        textView.setText("学生列表");
        final CommonAdapter adapter1 = new CommonAdapter<StuSetWorkBean.RowsBean>(context, studentList, R.layout.item_pop_student) {
            @Override
            public void convert(ViewHolder holder, final StuSetWorkBean.RowsBean rowsBean) {
                holder.getView(R.id.student_gridview_tv).setVisibility(View.GONE);
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setTextColor(Color.WHITE);
                checkBox.setChecked(true);
//                final int i = studentList.indexOf(rowsBean);
//                checkBox.setChecked(isCheckedMap.get(i));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectStudentList.contains(rowsBean)){
                            selectStudentList.remove(rowsBean);
                            checkBox.setTextColor(Color.parseColor("#333333"));
                        }else {
                            selectStudentList.add(rowsBean);
                            checkBox.setTextColor(Color.WHITE);
                        }
                        Log.d("wwwwwwwwwwww", "          " + selectStudentList.size());
                    }
                });
            }
        };
        gridView.setAdapter(adapter1);
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectStudentList.size() == 0){
                    AlertDialogUtil.showAlertDialog(context,"提示","选择的学生为空，请重新选择");
                }else {
                    //发布作业
                    releaseTest();
                }
                popupWindow.dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }


    //请求试题篮试题列表
    private void requestBskList(){

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
            jsonObject.put("GradeName", grade);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetBasketListMN;
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
                                    //根据知识点请求题目 并关闭菜单
                                    AlertDialogUtil.showAlertDialog(context, "提示", "试题篮无试题");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "查询试题篮效验失败");
                                }else {
                                    //试题篮中有试题
                                    Gson gson = new Gson();
                                    TestBasketBean testBasketBean = gson.fromJson(jsonObject.toString(), TestBasketBean.class);
                                    if (testBasketBean != null) {
                                        listData.addAll(testBasketBean.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "试题篮无试题");
                                        }
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


    //获取未布置作业学生
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
            jsonObject.put("Flag", "1");
            if (isDualTeacher.equals("0") && isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }
//

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有未发布作业的学生");
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
                                        studentList.addAll(setWorkBean.getRows());
                                        if (studentList.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有未发布作业的学生");
                                            return;
                                        }
                                        showStudentPopu();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有未发布作业的学生");
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


    //发布试题
    private void releaseTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            getStudentNO();
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
            jsonObject.put("TestType","3");
            jsonObject.put("StudentNo",studentNo + ",");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);

            String methodName = "";
            if (isDualTeacher.equals("0")){
                methodName = Constants.ReleaseTestMN2;
            }else {
                methodName = Constants.ReleaseExercisesMethodName;
            }

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "发布作业失败，请重试");
                                } else {
                                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
//                                    release_bt.setVisibility(View.GONE);
                                    adapter.deleteTest(1, 0);
                                    ActivityManage.backToActivity();
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
        for (int i = 0 ; i < selectStudentList.size(); i ++){
            if (i == 0){
                studentNo = selectStudentList.get(i).getStudentNo();
            }else {
                studentNo = studentNo + "," + selectStudentList.get(i).getStudentNo();
            }
        }

    }

}
