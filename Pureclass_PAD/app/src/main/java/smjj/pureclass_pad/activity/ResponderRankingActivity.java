package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.RadioGroup;
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
import smjj.pureclass_pad.beans.ResponderRankBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;

//抢答排行榜界面
public class ResponderRankingActivity extends BaseActivity {

    private Context context;
    private Handler mHandler;
    private SharedPreferences spConfig;
    private RadioGroup rg_responder;
    private TextView tv_responder_name2, tv_responder_name1, tv_responder_name3
            , tv_responder_name4, tv_responder_name5, tv_responder_name6;

    private TextView tv_responder_time1, tv_responder_time2, tv_responder_time3
            , tv_responder_time4, tv_responder_time5, tv_responder_time6;

    private TextView tv_responder_answer4, tv_responder_answer5, tv_responder_answer6;

    private List<ResponderRankBean.TablesBean.TableBean.RowsBean> listData;

    private String testNo = "";
    private String selectAnswer = "";
    public String userCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_ranking);
        context = this;
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        testNo = getIntent().getStringExtra("TestNo");

        findView();

    }


    private void findView(){
        rg_responder = (RadioGroup) findViewById(R.id.rg_responder);
        tv_responder_name2 = (TextView) findViewById(R.id.tv_responder_name2);
        tv_responder_name1 = (TextView) findViewById(R.id.tv_responder_name1);
        tv_responder_name3 = (TextView) findViewById(R.id.tv_responder_name3);
        tv_responder_name4 = (TextView) findViewById(R.id.tv_responder_name4);
        tv_responder_name5 = (TextView) findViewById(R.id.tv_responder_name5);
        tv_responder_name6 = (TextView) findViewById(R.id.tv_responder_name6);
        tv_responder_time1 = (TextView) findViewById(R.id.tv_responder_time1);
        tv_responder_time2 = (TextView) findViewById(R.id.tv_responder_time2);
        tv_responder_time3 = (TextView) findViewById(R.id.tv_responder_time3);
        tv_responder_time4 = (TextView) findViewById(R.id.tv_responder_time4);
        tv_responder_time5 = (TextView) findViewById(R.id.tv_responder_time5);
        tv_responder_time6 = (TextView) findViewById(R.id.tv_responder_time6);
        tv_responder_answer4 = (TextView) findViewById(R.id.tv_responder_answer4);
        tv_responder_answer5 = (TextView) findViewById(R.id.tv_responder_answer5);
        tv_responder_answer6 = (TextView) findViewById(R.id.tv_responder_answer6);


        listData = new ArrayList<>();

        rg_responder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){
                    case R.id.rb_responder_a:
                        selectAnswer = "A";
                        break;
                    case R.id.rb_responder_b:
                        selectAnswer = "B";
                        break;
                    case R.id.rb_responder_c:
                        selectAnswer = "C";
                        break;
                    case R.id.rb_responder_d:
                        selectAnswer = "D";
                        break;
                }
            }
        });

        requestResponderRank();
    }


    private void setRankInfo(){
        if (listData.size() >= 1){
            tv_responder_name1.setText(listData.get(0).getF0010());
            tv_responder_time1.setText(listData.get(0).getF0011() + "秒(" + listData.get(0).getF0012() + ")");
        }
        if (listData.size() >= 2){
            tv_responder_name2.setText(listData.get(1).getF0010());
            tv_responder_time2.setText(listData.get(1).getF0011() + "秒(" + listData.get(1).getF0012() + ")");
        }
        if (listData.size() >= 3){
            tv_responder_name3.setText(listData.get(2).getF0010());
            tv_responder_time3.setText(listData.get(2).getF0011() + "秒(" + listData.get(2).getF0012() + ")");
        }
        if (listData.size() >= 4){
            tv_responder_name4.setText(listData.get(3).getF0010());
            tv_responder_time4.setText(listData.get(3).getF0011() + "秒");
            tv_responder_answer4.setText("(" + listData.get(3).getF0012() + ")");
        }
        if (listData.size() >= 5){
            tv_responder_name5.setText(listData.get(4).getF0010());
            tv_responder_time5.setText(listData.get(4).getF0011() + "秒");
            tv_responder_answer5.setText("(" + listData.get(4).getF0012() + ")");
        }
        if (listData.size() >= 6){
            tv_responder_name6.setText(listData.get(5).getF0010());
            tv_responder_time6.setText(listData.get(5).getF0011() + "秒");
            tv_responder_answer6.setText("(" + listData.get(5).getF0012() + ")");
        }
    }


    //获取个人积分
    private void requestResponderRank() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (testNo == null || testNo.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","抢答试题编号不能为空，请重新开始抢答");
                return;
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("TestNo",testNo);


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.ResponderRankMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", errinfo);
                                } else {

                                    Gson gson = new Gson();
                                    ResponderRankBean responderRankBean = gson.fromJson(jsonObject.toString(), ResponderRankBean.class);
                                    if (responderRankBean != null) {
                                        listData.clear();
                                        listData.addAll(responderRankBean.getTables().getTable().getRows());
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无抢答排行榜，请稍后再查看");
                                        }
                                        setRankInfo();
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "获取抢到排行榜失败，请稍后再查看");
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


    //抢答教师选择正确答案
    private void techSelectAnswerIQ(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }

            if (testNo == null || testNo.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("Correct", selectAnswer);
            jsonObject.put("TestNo", testNo);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.ResponderSelectMN2;
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
                                if (resultvalue.equals("0")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                    finish();

                                } else if (resultvalue.equals("-1") ||resultvalue.equals("1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                                finish();
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据有误");
                            finish();
                        }
                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                        finish();
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
            finish();
        }
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            techSelectAnswerIQ();
        }

        return super.onKeyDown(keyCode, event);
    }

}
