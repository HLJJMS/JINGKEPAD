package smjj.pureclass_pad.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.StatusBarUtils;

//播放讲义界面
public class HandoutsWebActivity extends BaseActivity implements View.OnClickListener {


    private TextView title_tv;
    private TextView onBack;
    private ImageView iv_home;
    private WebView webView;
    private Activity activity;
    private boolean is;
    private Context context;
    private String url;
    private String enterMark = "";//1 代表备课 2代表上课

    private FloatingActionButton rightLowerButton;

    //即时互动开始答题、停止答题、查看详情标识 0代表可以开始答题 1代表一开始要停止答题 2已停止可以查看答案详情
    private int iqMarker = 0;
    private String iq_test_no = "";

    public String classId;
    public String classNo, packageNo;
    private String userCode;
    private String speakId;
    private SharedPreferences spConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handouts_web);
        ActivityManage.addActivity(this);
        context = this;
        activity = this;
        is = true;
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        enterMark = getIntent().getStringExtra("EnterMark");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        enterMark = getIntent().getStringExtra("EnterMark");
        classId = spConfig.getString(SPCommonInfoBean.classId, "");
        classNo = getIntent().getStringExtra("ClassNo");

        StatusBarUtils.setWindowStatusBarColor(this,R.color.black);

        final ImageView fabIconNew = new ImageView(this);

        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.down));

        rightLowerButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.finsh2)
                .build();

        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                .attachTo(rightLowerButton)
                .build();

        rightLowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
//            initWebView();
            AlertDialogUtil.showAlertDialog(context,"提示", "获取讲义失败");
        }

    }


    private void initWebView() {

//        CookieSyncManager.createInstance(context);
//        CookieManager.getInstance().removeAllCookie();

        if (Build.VERSION.SDK_INT >= 19) {
//            webView.getSettings().setCacheMode(
//                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
        //缩放开关，设置此属性，仅支持双击缩放，不支持触摸缩放
        s.setSupportZoom(true);
        //设置是否可缩放，会出现缩放工具（若为true则上面的设值也默认为true）
        s.setBuiltInZoomControls(true);
        //隐藏缩放工具
        s.setDisplayZoomControls(false);


        s.setJavaScriptEnabled(true);

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

}
