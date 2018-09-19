package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.adapter.LessonsBskAdapter;
import smjj.pureclass_pad.adapter.PersonalBskAdapter;
import smjj.pureclass_pad.adapter.PersonalLessonAdapter;
import smjj.pureclass_pad.beans.A001002Bean;
import smjj.pureclass_pad.beans.DataGramBean2;
import smjj.pureclass_pad.beans.TestBasketBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.eventbus.EventType;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.LoadingBox;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

//个性化备课试题篮
public class LessonsBskActivity extends BaseActivity implements View.OnClickListener {
    private XListView listView;
    private TextView onBack;
    private RecyclerView mRecyclerView;
    private ImageView iv_home, iv_back;
    private LessonsBskAdapter bskAdapter = new LessonsBskAdapter();
    private TextView title_tv;
    private RelativeLayout rl_back, rl_home, rl_add;
    private List<A001002Bean.TablesBean.TableBean.RowsBean> bean = new ArrayList<A001002Bean.TablesBean.TableBean.RowsBean>();
    private Context context;
    private Handler mHandler;
    private List<String> sumList = new ArrayList<String>();
    private SharedPreferences spConfig;
    private PersonalLessonAdapter perAdapter = new PersonalLessonAdapter();
    private String type;
    //    private RadioGroup rg_grade;
//    private RadioButton rb_create_primary_first, rb_create_primary_second, rb_create_primary_three
//            , rb_create_primary_four;
    private Button bt_create;

    private List<TestBasketBean.TablesBean.TableBean.RowsBean> listData;
    private List<TestBasketBean.TablesBean.TableBean.RowsBean> listDataChild = new ArrayList<TestBasketBean.TablesBean.TableBean.RowsBean>();
    //    private List<TestBasketBean.TablesBean.TableBean.RowsBean> inTestList;
//    private List<TestBasketBean.TablesBean.TableBean.RowsBean> outTestList;
//    private List<TestBasketBean.TablesBean.TableBean.RowsBean> deepenUseList;
//    private List<TestBasketBean.TablesBean.TableBean.RowsBean> solidfyList;
    private PersonalBskAdapter adapter;

    private String grade, subject, parentID, knowledgeID;
    private String exercisesSign;

    private String classId;
    private String typleUser;
    private String userCode;

    private LoadingBox loadingBox;
    private int selectPoint;
    private String selectDataGram;//3代表查看资料包，2代表新建资料包
    private String speakId, materialName, materialNo, classNo, className;
    private String packageNo = "";
    private String startTime = "";
    private boolean isCreate; //是创建还是查看资料包
    public String speakName;
    private List<DataGramBean2.TablesBean.TableBean.RowsBean> dataGramList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_bsk);
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser, "");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        classId = spConfig.getString(SPCommonInfoBean.classId, "");

        subject = getIntent().getStringExtra("subject");
        grade = getIntent().getStringExtra("grade");
        selectDataGram = getIntent().getStringExtra("SelectDataGram");
        speakId = getIntent().getStringExtra("SpeakId");
        speakName = getIntent().getStringExtra("SpeakName");
        materialName = getIntent().getStringExtra("MaterialName");
        materialNo = getIntent().getStringExtra("MaterialNo");
        classNo = getIntent().getStringExtra("ClassNo");
        startTime = getIntent().getStringExtra("StartTime");
        className = getIntent().getStringExtra("ClassName");
        packageNo = getIntent().getStringExtra("PackageNo");
        EventBus.getDefault().register(this);
        exercisesSign = "入门测";
        findView();


    }


    //侧滑菜单中的视图处理
    private void findView() {
        listView = (XListView) findViewById(R.id.scheduling_lv);
//        rg_grade = (RadioGroup) findViewById(R.id.rg_grade);
//        rb_create_primary_first = (RadioButton) findViewById(R.id.rb_create_primary_first);
//        rb_create_primary_second = (RadioButton) findViewById(R.id.rb_create_primary_second);
//        rb_create_primary_three = (RadioButton) findViewById(R.id.rb_create_primary_three);
//        rb_create_primary_four = (RadioButton) findViewById(R.id.rb_create_primary_four);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        bt_create = (Button) findViewById(R.id.bt_create);
        mRecyclerView = (RecyclerView) findViewById(R.id.bsk_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler();
        iv_home.setImageResource(R.drawable.function);
        iv_back.setImageResource(R.drawable.menu_bt);
        onBack.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        rl_back.setVisibility(View.VISIBLE);

        mRecyclerView.setAdapter(bskAdapter);
        isCreate = true;

        if (selectDataGram.equals("3")) {
            bt_create.setVisibility(View.GONE);
            isCreate = false;
        }
        title_tv.setText("个性化备课试题篮");

        listData = new ArrayList<>();
        dataGramList = new ArrayList<>();
        adapter = new PersonalBskAdapter(this, listData, speakId, isCreate);

        listView.setAdapter(adapter);
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        rl_home.setOnClickListener(this);
        rl_add.setOnClickListener(this);
        onBack.setOnClickListener(this);
        bt_create.setOnClickListener(this);

//        setRgCheckedListener();

        if (selectDataGram != null && selectDataGram.equals("3")) {
            getDataGramExerc();
        } else {
            requestBskList();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.onBack:
                finish();
                break;
            case R.id.bt_create:
                getDataGram();
                break;
        }

    }


    private void showDataGramPopu() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_data_gram, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context, view, 2, 2, 0.5f, -1);

        TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        final EditText et_data_gram_name = (EditText) view.findViewById(R.id.et_data_gram_name);
        Button save_bt = (Button) view.findViewById(R.id.log_bt);
        Button cancle_bt = (Button) view.findViewById(R.id.cancle_bt);
        String strName = "";
        String strSpeak = "";
        if (speakName != null && speakName.length() > 3) {
            strSpeak = speakName.substring(0, 4).trim();
        }
        strName = className + strSpeak + "备课包" + dataGramList.size() + 1;
        et_data_gram_name.setText(strName.trim());
        et_data_gram_name.setSelection(strName.trim().length());
        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建并保存资料包
                String gramName = et_data_gram_name.getText().toString();
                createDataGram(gramName);
            }
        });

        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(iv_home, Gravity.CENTER, 0, 0);

    }

    //横向 的recycler
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
                                    for (int i = 0; i < Abean.getTables().getTable().getRows().size(); i++) {
                                        if (Abean.getTables().getTable().getRows().get(i).getTypeCode().equals("0")) {
                                            bean.add(Abean.getTables().getTable().getRows().get(i));
                                        }
                                    }
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

    //请求试题篮试题列表
    private void requestBskList() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录！");
                return;
            }

            jsonObject.put("teacherNo", userCode);
            jsonObject.put("SpeakID", speakId);


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetBskExeMN2;
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
                                } else {
                                    //试题篮中有试题
                                    Gson gson = new Gson();
                                    TestBasketBean testBasketBean = gson.fromJson(jsonObject.toString(), TestBasketBean.class);
                                    if (testBasketBean != null) {
                                        listData.addAll(testBasketBean.getTables().getTable().getRows());
                                        if (listData.size() == 0) {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "试题篮无试题");
                                        } else {

                                            setExercType(listData);
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

    //提取资料包中的试题
    private void getDataGramExerc() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录！");
                return;
            }

            jsonObject.put("PackageNo", packageNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetDataGramExercMN2;
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
                                } else {
                                    //试题篮中有试题
                                    Gson gson = new Gson();
                                    TestBasketBean testBasketBean = gson.fromJson(jsonObject.toString(), TestBasketBean.class);
                                    if (testBasketBean != null) {
                                        listData.addAll(testBasketBean.getTables().getTable().getRows());
                                        if (listData.size() == 0) {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "试题篮无试题");
                                        } else {
                                            setExercType(listData);
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


    //创建资料包
    private void createDataGram(String gramName) {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录！");
                return;
            }

            if (gramName == null || gramName.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "资料包名不能为空，请填写！");
                return;
            }

            jsonObject.put("teacherNo", userCode);
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("PackageName", gramName);
            jsonObject.put("courseNo", materialNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CreateDataGramMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "创建资料包失败");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "创建资料包失败");
                                } else {
                                    //资料包创建成功
                                    if (jsonObject.toString().contains("PackageNo")) {
                                        packageNo = jsonObject.getString("PackageNo");
                                    }
                                    //确认备课
                                    surePrepareClass();
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
                                    showDataGramPopu();
                                } else {
                                    Gson gson = new Gson();
                                    DataGramBean2 dataGramBean2 = gson.fromJson(jsonObject.toString(), DataGramBean2.class);
                                    if (dataGramBean2 != null) {
                                        //创建新建和基础包选项
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


    //二期确认备课
    private void surePrepareClass() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "该排课信息不完善，请重新选择！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startTime == null || startTime.equals("")) {
                Toast.makeText(context, "上课时间不能为空，请重新选择时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("PackageNo", packageNo);
            jsonObject.put("classId", classId);
            jsonObject.put("classNo", classNo);
            jsonObject.put("timeStar", startTime);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SurePreClassMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "备课失败！");
                                } else {
                                    Toast.makeText(LessonsBskActivity.this, "创建并使用成功", Toast.LENGTH_SHORT).show();
                                    ActivityManage.backToNewCheckCode(context);
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


    //将试题分类
    public void setExercType(List<TestBasketBean.TablesBean.TableBean.RowsBean> list) {
//
//        inTestList = new ArrayList<>();
//        outTestList = new ArrayList<>();
//        deepenUseList = new ArrayList<>();
//        solidfyList = new ArrayList<>();
        type = String.valueOf(bean.get(0).getMenuNumber());
        sumList.clear();
        for (int i = 0; i < bean.size(); i++) {
            sumList.add("0");

        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < bean.size(); j++) {
                if (list.get(i).getTitleType().equals(String.valueOf(bean.get(j).getMenuNumber()))) {
                    sumList.set(j, String.valueOf(Integer.valueOf(sumList.get(j)) + 1));
                }
            }
        }
        bskAdapter.setData(bean, sumList, mRecyclerView);
        listDataChild.clear();
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getTitleType().equals(type)) {
                listDataChild.add(listData.get(i));
            }
        }


//        for (int i = 0; i < list.size(); i++) {
//            String exercType = "";
//            exercType = list.get(i).getTitleType();
//            if (exercType.equals("1")) {
//                sumList.add(list.get(i));
//            } else if (exercType.equals("2")) {
//                sumList.add(list.get(i));
//            } else if (exercType.equals("4")) {
//                sumList.add(list.get(i));
//            } else if (exercType.equals("5")) {
//                sumList.add(list.get(i));
//            }
//
//        }


//        rb_create_primary_first.setText("入门测(" + inTestList.size() + ")");
//        rb_create_primary_second.setText("深化应用(" + deepenUseList.size() + ")");
//        rb_create_primary_three.setText("巩固练习(" + solidfyList.size() + ")");
//        rb_create_primary_four.setText("出门考(" + outTestList.size() + ")");

        if (listDataChild.size() == 0) {
            AlertDialogUtil.showAlertDialog(context, "提示", "试题篮中无入门测试题");
        } else {
            adapter.setData(listDataChild);
            adapter.notifyDataSetChanged();
        }
    }

    //删除试题成功后，同步更新试题数量
    public void setDelItem(TestBasketBean.TablesBean.TableBean.RowsBean item){
       listData.remove(item);
        bskAdapter.setSumList(item.getTitleType());
    }


    //删除试题成功后，同步更新试题数量
    public void setExercData(List<TestBasketBean.TablesBean.TableBean.RowsBean> list1) {

        bean.remove(list1.get(0));


//                if (exercisesSign.equals("入门测")) {
//            inTestList.clear();
//            inTestList.addAll(list1);
//            rb_create_primary_first.setText("入门测(" + inTestList.size() + ")");
//        } else if (exercisesSign.equals("深化应用")) {
//            deepenUseList.clear();
//            deepenUseList.addAll(list1);
//            rb_create_primary_second.setText("深化应用(" + deepenUseList.size() + ")");
//
//        } else if (exercisesSign.equals("巩固练习")) {
//            solidfyList.clear();
//            solidfyList.addAll(list1);
//            rb_create_primary_three.setText("巩固练习(" + solidfyList.size() + ")");
//
//        } else if (exercisesSign.equals("出门考")) {
//            outTestList.clear();
//            outTestList.addAll(list1);
//            rb_create_primary_four.setText("出门考(" + outTestList.size() + ")");
//
//        }
    }


    /**
     * RadioGroup设置改变监听器
     */
//    private void setRgCheckedListener(){
//
//        rg_grade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                setGradeTextColor();
//                switch (i){
//                    case R.id.rb_create_primary_first:
//                        exercisesSign = "入门测";
//                        rb_create_primary_first.setTextColor(Color.parseColor("#ffffff"));
//                        listData.clear();
//                        listData.addAll(inTestList);
//                        adapter.setData(listData);
//                        adapter.notifyDataSetChanged();
//                        if (inTestList.size() == 0){
//                            AlertDialogUtil.showAlertDialog(context,"提示", "试题篮中无入门测试题");
//                        }
//                        break;
//                    case R.id.rb_create_primary_second:
//                        exercisesSign = "深化应用";
//                        rb_create_primary_second.setTextColor(Color.parseColor("#ffffff"));
//                        listData.clear();
//                        listData.addAll(deepenUseList);
//                        adapter.setData(listData);
//                        adapter.notifyDataSetChanged();
//                        if (deepenUseList.size() == 0){
//                            AlertDialogUtil.showAlertDialog(context,"提示", "试题篮中无深化应用试题");
//                        }
//                        break;
//                    case R.id.rb_create_primary_three:
//                        exercisesSign = "巩固练习";
//                        rb_create_primary_three.setTextColor(Color.parseColor("#ffffff"));
//                        listData.clear();
//                        listData.addAll(solidfyList);
//                        adapter.setData(listData);
//                        adapter.notifyDataSetChanged();
//                        if (solidfyList.size() == 0){
//                            AlertDialogUtil.showAlertDialog(context,"提示", "试题篮中无巩固练习试题");
//                        }
//
//                        break;
//                    case R.id.rb_create_primary_four:
//                        exercisesSign = "出门考";
//                        rb_create_primary_four.setTextColor(Color.parseColor("#ffffff"));
//                        listData.clear();
//                        listData.addAll(outTestList);
//                        adapter.setData(listData);
//                        adapter.notifyDataSetChanged();
//                        if (outTestList.size() == 0){
//                            AlertDialogUtil.showAlertDialog(context,"提示", "试题篮中无出门考试题");
//                        }
//                        break;
//                }
//            }
//        });
//
//    }

//    private void setGradeTextColor(){
//        rb_create_primary_first.setTextColor(Color.parseColor("#666666"));
//        rb_create_primary_second.setTextColor(Color.parseColor("#666666"));
//        rb_create_primary_three.setTextColor(Color.parseColor("#666666"));
//        rb_create_primary_four.setTextColor(Color.parseColor("#666666"));
//
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventType messageEvent) {
        type = String.valueOf(bean.get(Integer.valueOf(messageEvent.getType())).getMenuNumber());
//        type = messageEvent.getType();
        listDataChild.clear();
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getTitleType().equals(type)) {
                listDataChild.add(listData.get(i));
            }
        }
        if (listDataChild.size() == 0) {
            AlertDialogUtil.showAlertDialog(context, "提示", "试题篮中无入门测试题");
        }
            adapter.setDataAll(listDataChild);
            adapter.notifyDataSetChanged();

    }

}
