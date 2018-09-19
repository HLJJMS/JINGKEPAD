package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.ConClusionBean;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/15.
 */
//上课课堂小结
public class ClassConclusionFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private EditText et_class_conclusion;
    private Button save_bt;

    private GoClassActivity activity;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private String userName;

    private String conclusion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (GoClassActivity) getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        userName = spConfig.getString(SPCommonInfoBean.userName,"");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_class_conclusion, container, false);

        findView(contentView);

        return contentView;
    }

    private void findView(View view){
        et_class_conclusion = (EditText) view.findViewById(R.id.et_class_conclusion);
        save_bt = (Button) view.findViewById(R.id.log_bt);

        save_bt.setOnClickListener(this);

        getConclusion();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_bt:
                saveConclusion();
                break;
        }
    }

    //获取课堂小结
    private void getConclusion() {
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
            jsonObject.put("TeacherNo",userCode);
            jsonObject.put("ClassID",classId);
            jsonObject.put("SpeakID",activity.speakId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.GetConclusionMN;


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
                                } else {
                                    Gson gson = new Gson();
                                    ConClusionBean conClusionBean = gson.fromJson(jsonObject.toString(), ConClusionBean.class);
                                    if (conClusionBean != null) {
                                        conclusion = conClusionBean.getRows().get(0).getSummaryContent();
                                        if (conclusion != null){
                                            et_class_conclusion.setText(conclusion);
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


    //保存课堂小结
    private void saveConclusion() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            conclusion = et_class_conclusion.getText().toString().trim();
            if (conclusion == null || conclusion.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","请输入课堂小结内容");
            }

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
            jsonObject.put("SpeakID",activity.speakId);
            jsonObject.put("SpeakName","");
            jsonObject.put("SummaryContent",conclusion);
            jsonObject.put("TeacherNo",userCode);
            jsonObject.put("TeacherName",userName);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.SaveConclusionMN;


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
                                    Toast.makeText(context , "保存失败，请重试", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context , "保存成功", Toast.LENGTH_SHORT).show();
                                    et_class_conclusion.setEnabled(false);
                                    et_class_conclusion.setTextColor(Color.BLACK);

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
