package smjj.pureclass_pad.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.IQDetailsBean;
import smjj.pureclass_pad.beans.PrepareUrlBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.util.StatusBarUtils;
import smjj.pureclass_pad.view.VerticalChart1;

//播放HTML5界面（备课查看课件，上课情景导入和深化应用）
public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_tv;
    private TextView onBack;
    private ImageView iv_home;
    private WebView webView;
    private Activity activity;
    private boolean is;
    private Context context;
    private String url;
    private String enterMark = "";//1 代表情景导入 2代表学习新知 0 代表备课查看课件

    private FloatingActionButton rightLowerButton;

    private String userCode;
    private String speakId;
    private SharedPreferences spConfig;

    //即时互动开始答题、停止答题、查看详情标识 0代表可以开始答题 1代表一开始要停止答题 2已停止可以查看答案详情
    private int iqMarker = 0;
    private String iq_test_no = "";

    public String classId;
    public String classNo, packageNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ActivityManage.addActivity(this);
        context = this;
        activity = this;
        is = true;
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        enterMark = getIntent().getStringExtra("EnterMark");
        classId = spConfig.getString(SPCommonInfoBean.classId, "");
        classNo = getIntent().getStringExtra("ClassNo");

        StatusBarUtils.setWindowStatusBarColor(this,R.color.black);

        if (enterMark != null & enterMark.equals("0")){
            final ImageView fabIconNew = new ImageView(this);
            fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.down));
            rightLowerButton = new FloatingActionButton.Builder(this)
                    .setBackgroundDrawable(R.drawable.finsh2)
                    .build();

            rightLowerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }else {
            int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
            int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
            int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
            int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
            int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
            int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
            int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

            final ImageView fabIconNew = new ImageView(this);
            fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.dakai));

            FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
            starParams.setMargins(redActionButtonMargin,
                    redActionButtonMargin,
                    redActionButtonMargin,
                    redActionButtonMargin);
            fabIconNew.setLayoutParams(starParams);


            FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
            fabIconStarParams.setMargins(redActionButtonContentMargin,
                    redActionButtonContentMargin,
                    redActionButtonContentMargin,
                    redActionButtonContentMargin);


            rightLowerButton = new FloatingActionButton.Builder(this)
                    .setContentView(fabIconNew, fabIconStarParams)
                    .setBackgroundDrawable(R.drawable.lucency)
                    .setLayoutParams(starParams)
                    .build();

            SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
            rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.lucency));

            FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            blueContentParams.setMargins(blueSubActionButtonContentMargin,
                    blueSubActionButtonContentMargin,
                    blueSubActionButtonContentMargin,
                    blueSubActionButtonContentMargin);
            rLSubBuilder.setLayoutParams(blueContentParams);
            FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
            rLSubBuilder.setLayoutParams(blueParams);

            ImageView rlIcon1 = new ImageView(this);
            ImageView rlIcon2 = new ImageView(this);
            ImageView rlIcon3 = new ImageView(this);
            ImageView rlIcon4 = new ImageView(this);
            ImageView rlIcon5 = new ImageView(this);


            rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.xiayibu));
            rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.diaochujiangyi));
            rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.timer1));
            rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.release1));
            rlIcon5.setImageDrawable(getResources().getDrawable(R.drawable.quiz1));

            final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                    .addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
                    .setRadius(redActionMenuRadius)
                    .attachTo(rightLowerButton)
                    .build();

            rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingActionMenu menu) {
                    fabIconNew.setRotation(0);
                    PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                    ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                    animation.start();
                }

                @Override
                public void onMenuClosed(FloatingActionMenu menu) {
                    fabIconNew.setRotation(45);
                    PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                    ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                    animation.start();
                }
            });

            rlIcon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rightLowerMenu.close(true);

                    Intent intent = new Intent();

                    if (enterMark != null && enterMark.equals("1")){
                        setResult(1, intent);
                    }else if (enterMark != null && enterMark.equals("2")){
                        setResult(2, intent);
                    }
                    finish();
                }
            });

            rlIcon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    speakId = getIntent().getStringExtra("SpeakId");
                    if (speakId != null && !speakId.equals("")){
                        getContentUrl();
                    }
                }
            });


            rlIcon5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPopWinIQ();
                }
            });

        }




        findView();
    }


    private void findView(){

        webView = (WebView) findViewById(R.id.web);
        webView.loadUrl("about:blank");

        url = getIntent().getStringExtra("fileUrl");
        if (url != null && !url.equals("")){
            initWebView();
        }else {
//            url = "http://47.92.81.135:8001/Courseware/CoursewareUpLoad/20170908113254077_12312321/index.html";
            initWebView();
        }

    }



    private void initWebView() {

// 清除cookie即可彻底清除缓存

//        CookieSyncManager.createInstance(context);
//        CookieManager.getInstance().removeAllCookie();

        if (Build.VERSION.SDK_INT >= 19) {
//            webView.getSettings().setCacheMode(
//                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //不使用缓存：
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.setWebViewClient(new GameWebViewClient());
        WebSettings s = webView.getSettings();
        s.setDomStorageEnabled(true);
        s.setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        webView.loadUrl(url);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_home:
                ActivityManage.backToNewCheckCode(context);
                break;
            case R.id.onBack:
                finish();
                break;
        }

    }


    private Button iq_start_bt;
    private Button iq_stop_bt;
    private Button iq_release_bt;
    private VerticalChart1 verticalChart1;
    private TextView textView;
    private TextView tv_null;

    /**
     * 弹出即时互动开始答题弹框
     */
    private void showPopWinIQ(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_iq, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow2(context,view, 2, 10,0.5f,-1);

        iq_start_bt = (Button) view.findViewById(R.id.iq_start_bt);
        iq_stop_bt = (Button) view.findViewById(R.id.iq_stop_bt);
        iq_release_bt = (Button) view.findViewById(R.id.iq_release_bt);
        verticalChart1 = (VerticalChart1) view.findViewById(R.id.vc);
        textView = (TextView) view.findViewById(R.id.tv_select_correct);
        tv_null = (TextView) view.findViewById(R.id.tv_null);

        if (iqMarker == 0){
            iq_start_bt.setEnabled(true);
            iq_start_bt.setBackgroundResource(R.drawable.shape_bg);
            iq_release_bt.setEnabled(false);
            iq_release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            iq_stop_bt.setEnabled(false);
            iq_stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            verticalChart1.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);

        }else if (iqMarker == 1){
            iq_start_bt.setEnabled(false);
            iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            iq_stop_bt.setEnabled(true);
            iq_stop_bt.setBackgroundResource(R.drawable.shape_bg);
            iq_release_bt.setEnabled(false);
            iq_release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            verticalChart1.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }else {
            iq_start_bt.setEnabled(false);
            iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
            iq_release_bt.setEnabled(true);
            iq_release_bt.setBackgroundResource(R.drawable.shape_bg);
            iq_stop_bt.setEnabled(false);
            iq_stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
        }


        iq_start_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 1;

                startAnswerIQ();

            }
        });

        iq_stop_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 2;
                stopAnsweIQ();
            }
        });

        //查看试题详情
        iq_release_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 0;
                popupWindow.dismiss();
//                showPopWinIQ1();
                getIQDetails();

            }
        });

        popupWindow.showAtLocation(webView, Gravity.CENTER,0,0);

    }


    private List<Integer> percentList;
    private List<Integer> yList;

    /**
     * 展示即时问答详情弹窗
     */
    private void showPopWinIQ1(){

        final View view = LayoutInflater.from(context).inflate(R.layout.pop_iq, null);

        final PopupWindow popupWindow  = PopupWindowUtil.getPopuWindow2(context,view, (float) 1.3, (float) 1.5,0.5f,-1);


        iq_start_bt = (Button) view.findViewById(R.id.iq_start_bt);
        iq_stop_bt = (Button) view.findViewById(R.id.iq_stop_bt);
        iq_release_bt = (Button) view.findViewById(R.id.iq_release_bt);
        verticalChart1 = (VerticalChart1) view.findViewById(R.id.vc);
        textView = (TextView) view.findViewById(R.id.tv_select_correct);
        tv_null = (TextView) view.findViewById(R.id.tv_null);


        iq_release_bt.setEnabled(true);
        iq_release_bt.setBackgroundResource(R.drawable.shape_bg);
        iq_release_bt.setText("关    闭");
        iq_start_bt.setVisibility(View.INVISIBLE);
        iq_stop_bt.setVisibility(View.INVISIBLE);
        verticalChart1.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        tv_null.setVisibility(View.GONE);

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);


        iq_release_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iqMarker = 0;
                popupWindow.dismiss();

                techSelectAnswerIQ(verticalChart1);
            }
        });


        Log.d("ssssssssssssss", yList.size() + "      " + percentList.size()+ "      " + percentList.get(0)+ "      " + percentList.get(1)+ "      " + percentList.get(2)+ "      " + percentList.get(3));
        verticalChart1.initView(yList, percentList, "");

        popupWindow.showAtLocation(webView, Gravity.CENTER,0,0);

    }



    //临时互动开始答题
    public void startAnswerIQ(){

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

            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("TestNo", "");
            jsonObject.put("Releaser", userCode);
            jsonObject.put("TrunkNo", classNo);
            jsonObject.put("Period", "1");
            jsonObject.put("ReplyStatus", "2");//1代表题库代替 2代表临时答题


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LCllidkerStartMN2;
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
                                    Toast.makeText(context, "开始答题", Toast.LENGTH_SHORT).show();

                                    iq_start_bt.setEnabled(false);
                                    iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                    iq_release_bt.setEnabled(false);
                                    iq_release_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                    iq_stop_bt.setEnabled(true);
                                    iq_stop_bt.setBackgroundResource(R.drawable.shape_bg);

                                    iq_test_no = jsonObject.getString("TestNo");

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

    //临时互动停止答题下
    public void stopAnsweIQ(){

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

            if (iq_test_no == null || iq_test_no.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("TestNo", iq_test_no);
            jsonObject.put("Releaser", userCode);
            jsonObject.put("TrunkNo", classNo);
            jsonObject.put("Period", "1");
            jsonObject.put("PeriodText", "");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LCllidkerStopMN2;
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
                                    Toast.makeText(context, "答题已停止", Toast.LENGTH_SHORT).show();
                                    iq_start_bt.setEnabled(false);
                                    iq_start_bt.setBackgroundResource(R.drawable.shape_gray_bg3);
                                    iq_release_bt.setEnabled(true);
                                    iq_release_bt.setBackgroundResource(R.drawable.shape_bg);
                                    iq_stop_bt.setEnabled(false);
                                    iq_stop_bt.setBackgroundResource(R.drawable.shape_gray_bg3);

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


    //临时互动获取答题详情
    public void getIQDetails(){

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

            if (iq_test_no == null || iq_test_no.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("classId", classId);
            jsonObject.put("TestNo", iq_test_no);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetIQDetailsMN2;
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

                                if (resultvalue.equals("-1") ||resultvalue.equals("1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                }else {
                                    Gson gson = new Gson();
                                    IQDetailsBean iqDetailsBean = gson.fromJson(jsonObject.toString(), IQDetailsBean.class);
                                    if (iqDetailsBean != null){

                                        initIQVerChart(iqDetailsBean);

                                    }else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "未获取到学生答题数据!");
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


    //初始化临时答题柱状图
    private void initIQVerChart(IQDetailsBean iqDetailsBean){

        percentList = new ArrayList<>();
        yList = new ArrayList<>();
        //总人数呢
        double total = 0;
        double selectA = iqDetailsBean.getTables().getTable().getRows().get(0).getNumber();
        double selectB = iqDetailsBean.getTables().getTable().getRows().get(1).getNumber();
        double selectC = iqDetailsBean.getTables().getTable().getRows().get(2).getNumber();
        double selectD = iqDetailsBean.getTables().getTable().getRows().get(3).getNumber();
        double selectW = iqDetailsBean.getTables().getTable().getRows().get(4).getNumber();//代表未答题的人数

        total = selectA + selectB + selectC + selectD + selectW;

        if (total != 0){
            percentList.add((int) ((selectA/total)*100));
            percentList.add((int) ((selectB/total)*100));
            percentList.add((int) ((selectC/total)*100));
            percentList.add((int) ((selectD/total)*100));
            percentList.add((int) ((selectW/total)*100));

        }

        yList.add((int) selectA);
        yList.add((int) selectB);
        yList.add((int) selectC);
        yList.add((int) selectD);
        yList.add((int) selectW);

        showPopWinIQ1();



    }

    //临时答题教师选择正确答案
    private void techSelectAnswerIQ(VerticalChart1 verticalChart1){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }

            if (iq_test_no == null || iq_test_no.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","未获取到当前试题的ID，请重试");
                return;
            }

            jsonObject.put("Correct", verticalChart1.mRightAnswers);
            jsonObject.put("TestNo", iq_test_no);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.IQTechSelectMN2;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
                                } else if (resultvalue.equals("-1") ||resultvalue.equals("1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    Log.d("FAILURE_RESULT", errinfo);
                                    Toast.makeText(context, errinfo, Toast.LENGTH_SHORT).show();
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


    //获取讲义路径
    private void getContentUrl() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonType", "1");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetSpeachDetailMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "获取内容失败！");
                                } else {
                                    Gson gson = new Gson();
                                    PrepareUrlBean prepareUrlBean = gson.fromJson(jsonObject.toString(), PrepareUrlBean.class);
                                    if (prepareUrlBean != null) {
                                        String handoutsUrl = prepareUrlBean.getTables().getTable().getRows().get(0).getBodyLink();
                                        if (handoutsUrl != null && !handoutsUrl.equals("")){
                                            Intent intent = new Intent(context, HandoutsWebActivity.class);
                                            intent.putExtra("fileUrl", handoutsUrl);
                                            intent.putExtra("EnterMark", "2");
                                            startActivity(intent);
                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "讲义地址获取异常");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "讲义地址获取异常");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.setLayerType(View.LAYER_TYPE_NONE, null);
    }
}
