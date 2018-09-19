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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.Map;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.adapter.AppraisalBskAdapter;
import smjj.pureclass_pad.beans.StudentInternalBean;
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

/**
 * 测评试题篮
 */

public class AppraisalBskActivity extends BaseActivity implements View.OnClickListener{

    private XListView listView;
    private TextView onBack;
    private ImageView iv_home, iv_back;
    private TextView title_tv;
    private Button bt_create_work, bt_select_work, bt_back_scheduling, bt_clear_work;
    private EditText et_test_title, et_test_describe, et_total_score;

    private Context context;
    private Handler mHandler;

    private List<TestBasketBean.TablesBean.TableBean.RowsBean> listData;
    private AppraisalBskAdapter adapter;

    //1 代表布置作业， 2代表测评组卷
    private String enterMark;

    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> studentList;
    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> selectStudentList;

    private String grade, subject, parentID, knowledgeID;
    private int pageNum = 1;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String username;

    private String userCode;

    private String studentNo;
    private int exmCount;
    private String testID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal_bsk);
        ActivityManage.addActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = this;
        mHandler = new Handler();
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        username = spConfig.getString(SPCommonInfoBean.userName,"");
        grade = getIntent().getStringExtra("GradeName");
        subject = getIntent().getStringExtra("Subject");


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
        et_test_title = (EditText) findViewById(R.id.et_test_title);
        et_test_describe = (EditText) findViewById(R.id.et_test_describe);
        et_total_score = (EditText) findViewById(R.id.et_total_score);

        iv_home.setVisibility(View.GONE);
        bt_back_scheduling.setVisibility(View.GONE);

        title_tv.setText("组卷试题篮");
        et_total_score.setEnabled(false);

        listData = new ArrayList<>();
        studentList = new ArrayList<>();

        selectStudentList = new ArrayList<>();


        adapter = new AppraisalBskAdapter(context, listData, grade, subject);


        listView.setAdapter(adapter);
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
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
                //组卷
                creatTest();
                break;
            case R.id.bt_select_work:
                finish();
                break;
            case R.id.bt_back_scheduling:
                studentList.clear();
                checkSign();
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

        final Map<Integer, Boolean> isCheckedMap = new HashMap<>();
        for (int i = 0; i < studentList.size(); i ++){
            isCheckedMap.put(i, true);
        }
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
        final CommonAdapter adapter1 = new CommonAdapter<StudentInternalBean.TablesBean.TableBean.RowsBean>(context, studentList, R.layout.item_pop_student) {
            @Override
            public void convert(ViewHolder holder, final StudentInternalBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.getView(R.id.student_gridview_tv).setVisibility(View.GONE);
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setText(rowsBean.getUserName());
                checkBox.setTextColor(Color.WHITE);
                final int i = studentList.indexOf(rowsBean);
                checkBox.setChecked(isCheckedMap.get(i));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkBox.isChecked()){
                            checkBox.setTextColor(Color.WHITE);
                            selectStudentList.add(rowsBean);
                            isCheckedMap.put(i, true);
                        }else {
                            checkBox.setTextColor(Color.parseColor("#333333"));
                            selectStudentList.remove(rowsBean);
                            isCheckedMap.put(i, false);
                        }
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

    //请求试题篮试题列表（）
    private void requestBskList(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }

            jsonObject.put("UserCode", userCode);
            jsonObject.put("TestType", "10");
            jsonObject.put("GradeName", grade);
            jsonObject.put("Subject", subject);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAprBskMN;
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

    //检查学生是否签到
    private void checkSign(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAprStuMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该教师名下没有近90天上课的学生");
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
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该教师名下没有近90天上课的学生");
                                            return;
                                        }
                                        showStudentPopu();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该教师名下没有近90天上课的学生");
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

    //发布组卷（待修改）
    private void releaseTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            getStudentNO();

            if (testID == null || testID.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","试卷编号为空，请重新生成");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("TestID",testID);
            jsonObject.put("StudentNo",studentNo + ",");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);

            String methodName = Constants.AprReleaseTestMN;

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
//                                    ActivityManage.backToNewCheckCode(context);
                                    bt_back_scheduling.setVisibility(View.GONE);
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


    //生成组卷
    private void creatTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            getStudentNO();
            String title = et_test_title.getText().toString();
            String describe = et_test_describe.getText().toString();
            String score = et_total_score.getText().toString();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }

            if (title == null || title.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","标题不能为空，请填写");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject headJsonObject = new JSONObject();
            headJsonObject.put("UserCode",userCode);
            headJsonObject.put("UserName",username);
            headJsonObject.put("Title",title);
            headJsonObject.put("Des",describe);
            headJsonObject.put("Score",score);
            headJsonObject.put("grade",grade);
            headJsonObject.put("type","10");
            headJsonObject.put("subject",subject);
            headJsonObject.put("ExmCount",exmCount);

            com.alibaba.fastjson.JSONArray jsonArray = getListData();
            if (jsonArray == null || jsonArray.size() == 0){
                AlertDialogUtil.showAlertDialog(this,"提示", "有部分题没有设置分数，请去设置");
                return;
            }

            jsonObject.put("head", headJsonObject);
            jsonObject.put("Exercise", jsonArray);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);

            String methodName = Constants.CreatTestMN;

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "生成组卷失败");
                                } else {
                                    Toast.makeText(context, "生成组卷成功", Toast.LENGTH_SHORT).show();
                                    bt_create_work.setVisibility(View.GONE);
                                    bt_select_work.setVisibility(View.GONE);
                                    bt_clear_work.setVisibility(View.GONE);
                                    bt_back_scheduling.setVisibility(View.VISIBLE);
                                    et_test_title.setEnabled(false);
                                    et_test_describe.setEnabled(false);
                                    testID = jsonObject.getString("testID");
                                    adapter.isCreatSuccess();
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

    public void setTotalsScore(Map<Integer, String> scoreMap){
        //totalScore 总分数  itemScore是每一题的分数
        exmCount = scoreMap.size();
        int totalScore = 0;
        for (Map.Entry<Integer, String> entry : scoreMap.entrySet()) {
            String strScore = entry.getValue();

            if (strScore != null && !strScore.equals("")){
                Integer itemScore = Integer.valueOf(strScore);
                totalScore = totalScore + itemScore;
            }
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        et_total_score.setText(totalScore + "");

    }

    //获取要传的数组集合

    private com.alibaba.fastjson.JSONArray getListData(){

        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();

        Map<Integer, String> scoreMap = adapter.getScoreMap();
        Map<Integer, Integer> itemNoMap = adapter.getItemNoMap();

        if (adapter.sizeEquals() == 0){
            for (Map.Entry<Integer, String> entry : scoreMap.entrySet()) {
                JSONObject jsonObject = new JSONObject();
                String strScore = entry.getValue();
                int key = entry.getKey();
                if (strScore == null || strScore.equals("")){
                    jsonArray.clear();
                    return jsonArray;
                }else {

                    jsonObject.put("exercisesID", key);
                    jsonObject.put("scoreItem", strScore);

                    if (itemNoMap.containsKey(key)){
                        jsonObject.put("exercisesNum", itemNoMap.get(key) + "");
                    }
                    jsonArray.add(jsonObject);
                }

//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            }

        }else {
            jsonArray.clear();
            return jsonArray;
        }

        return jsonArray;
    }

}
