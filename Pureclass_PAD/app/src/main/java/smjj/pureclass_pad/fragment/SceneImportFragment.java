package smjj.pureclass_pad.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.PrepareUrlBean;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/15.
 */
//情境导入碎片
public class SceneImportFragment extends Fragment {
    private Context context;
    private GoClassActivity activity;
    private WebView webView;

    private String userCode;
    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private Handler mHandler;

    private String fileUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (GoClassActivity) getActivity();

        mHandler =new Handler();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_scene_import, container, false);

        findView(contentView);
        return contentView;
    }

    private void findView(View view){
//        title_tv = (TextView) findViewById(R.id.common_title_tv);
//        onBack = (TextView) findViewById(R.id.onBack);
//        iv_home = (ImageView) findViewById(R.id.iv_home);
        webView = (WebView) view.findViewById(R.id.web);
//        initWebView();

        getContentUrl();

//        title_tv.setText("播放课件");
//
//        onBack.setOnClickListener(this);
//        iv_home.setOnClickListener(this);

    }

    private void initWebView() {


        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);


        webView.loadUrl(fileUrl);

    }



    //获取路径
    private void getContentUrl() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "该排课信息不完善，请重新选择排课列表！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("classId", classId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonType", "1");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetGoClassUrlMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "备课失败！");
                                } else {
                                    Gson gson = new Gson();
                                    PrepareUrlBean prepareUrlBean = gson.fromJson(jsonObject.toString(), PrepareUrlBean.class);
                                    if (prepareUrlBean != null) {
                                        fileUrl = prepareUrlBean.getTables().getTable().getRows().get(0).getBodyLink();
                                        if (fileUrl != null && !fileUrl.equals("")){
                                            activity.skipWebView(fileUrl, "1");
                                            initWebView();

                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "文件地址获取异常");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "文件地址获取异常");
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
    public void onDestroy() {
        super.onDestroy();
//        webView.setLayerType(View.LAYER_TYPE_NONE, null);
    }

}
