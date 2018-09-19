package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.TeacherRLBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;


//教学反思
public class TeachingRfActivity extends BaseActivity implements View.OnClickListener{

    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private RelativeLayout rl_back, rl_home, rl_add;
    private EditText et_teach_rl;
    private Button save_bt;

    private Context context;
    private Handler mHandler;

    private SharedPreferences spConfig;
    private String teach_rl;

    //1代表添加教学反思，2代表查看教学反思
    private String enterMark;

    private String classId;
    private String typleUser;
    private String userCode;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching_rf);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        enterMark = getIntent().getStringExtra("enterMark");

        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        userName = spConfig.getString(SPCommonInfoBean.userName,"");

        findView();

    }

    private void findView() {
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        et_teach_rl = (EditText) findViewById(R.id.et_teach_rl);
        save_bt = (Button) findViewById(R.id.log_bt);



        title_tv.setText("教学反思");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");


        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        save_bt.setOnClickListener(this);


        getTeacherRl();

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
            case R.id.log_bt:
                addTeacherRl();
                break;
        }


    }
    //获取教学反思
    private void getTeacherRl() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ClassID",classId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.GetTeachRLMN;


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
                                } else {
                                    Gson gson = new Gson();
                                    TeacherRLBean teacherRLBean = gson.fromJson(jsonObject.toString(), TeacherRLBean.class);
                                    if (teacherRLBean != null) {
                                        teach_rl = teacherRLBean.getTables().getTable().getRows().get(0).getReflectContent();
                                        if (teach_rl != null){
                                            et_teach_rl.setText(teach_rl);
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


    //保存教学反思
    private void addTeacherRl() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            teach_rl = et_teach_rl.getText().toString().trim();

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
            jsonObject.put("ReflectContent",teach_rl);
            jsonObject.put("UserCode",userCode);
            jsonObject.put("TeacherName",userName);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.AddTeachRLMN;

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
                                    Toast.makeText(context , "保存失败，请重试", Toast.LENGTH_SHORT).show();
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


}