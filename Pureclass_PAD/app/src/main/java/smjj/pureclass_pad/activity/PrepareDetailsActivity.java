package smjj.pureclass_pad.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.TimerTask;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.PrepareUrlBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.HttpDownloader;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.FileUtils;
import smjj.pureclass_pad.util.MD5;

import static smjj.pureclass_pad.util.OpenFileUtil.getPdfFileIntent;

//备课详情界面，讲义、课件\说授课视屏
public class PrepareDetailsActivity extends BaseActivity implements View.OnClickListener{

    private TextView title_tv;
    private TextView onBack;
    private ImageView iv_home;
    private LinearLayout ll_handouts, ll_ppt, ll_teaching_video, ll_speak_video;

    private RelativeLayout rl_back, rl_home, rl_add;
    private Context context;

    private String userCode;
    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private Handler mHandler;
    private String speakId;

    //1 代表讲义，2代表ppt课件，3代表授课视频，4代表说课视屏
    private String sign;
    private String speakName = "";


    int result = 8;
    FileUtils futils;
    String fileUrl;
    String fileName;
    String subDir;
    String sdPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_details);
        ActivityManage.addActivity(this);
        context = this;
        mHandler =new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        speakId = getIntent().getStringExtra("SpeakId");
        speakName = getIntent().getStringExtra("SpeakName");


        findView();
    }


    private void findView(){
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        ll_handouts = (LinearLayout) findViewById(R.id.ll_handouts);
        ll_ppt = (LinearLayout) findViewById(R.id.ll_ppt);
        ll_teaching_video = (LinearLayout) findViewById(R.id.ll_teaching_video);
        ll_speak_video = (LinearLayout) findViewById(R.id.ll_speak_video);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);

        futils= new FileUtils();
        subDir = "wenxin_download/";


//        fileUrl = "http://erp.iwenxin.net/Lecture/LectureUpLoad/20151103165849080_877136.pdf";


        title_tv.setText(speakName);

        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        ll_handouts.setOnClickListener(this);
        ll_ppt.setOnClickListener(this);
        ll_speak_video.setOnClickListener(this);
        ll_teaching_video.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_home:
                ActivityManage.backToNewCheckCode(context);
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.ll_handouts:
                sign = "1";
                getContentUrl();
                break;
            case R.id.ll_ppt:
                sign = "2";
                getContentUrl();

                break;
            case R.id.ll_teaching_video:
                sign = "4";
                getContentUrl();

                break;
            case R.id.ll_speak_video:
                sign = "3";
                getContentUrl();

                break;
        }

    }


    //下载pdf文件并打开
    private void downLoaderPDF(){
        if(futils.isFileExist(subDir + fileName)) {

            try{

                Intent intent1 =  getPdfFileIntent(sdPath);
                startActivity(intent1);
            }catch (Exception e){
                //没有安装第三方的软件会提示
                Toast toast = Toast.makeText(context, "没有找到打开Pdf文件的办公软件，请安装办公软件", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else {

            showLoading1();
            //开启线程下载文件
            // TODO Auto-generated method stub
            Thread t = new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    HttpDownloader httpDownLoader = new HttpDownloader();

                    result = httpDownLoader.downfile(fileUrl, subDir, fileName);

                    tt.run();
                }

            });
            t.start();

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            closeLoading();
            MessageShow();
        }
    };

    public void MessageShow() {

        if(result==0) {
            Toast.makeText(context, "下载成功！", Toast.LENGTH_SHORT).show();

            try{

                Intent intent1 =  getPdfFileIntent(sdPath);
                startActivity(intent1);
            }catch (Exception e){
                //没有安装第三方的软件会提示
                Toast toast = Toast.makeText(context, "没有找到打开Pdf文件的办公软件，请安装办公软件", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(result==1) {
            Toast.makeText(context, "已有文件！", Toast.LENGTH_SHORT).show();

            try{

                Intent intent1 =  getPdfFileIntent(sdPath);
                startActivity(intent1);
            }catch (Exception e){
                //没有安装第三方的软件会提示
                Toast toast = Toast.makeText(context, "没有找到打开Pdf文件的办公软件，请安装办公软件", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(result==-1){
            Toast.makeText(context, "下载失败！", Toast.LENGTH_SHORT).show();
        }

    }
    //定时器
    TimerTask tt = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what =1;

            handler.sendMessageDelayed(msg, 2000);
        }

    };


    //获取路径
    private void getContentUrl() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "该排课信息不完善，请重新选择排课列表！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("classId", classId);
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonType", sign);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetSpeach;
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
                                    Gson gson = new Gson();
                                    PrepareUrlBean prepareUrlBean = gson.fromJson(jsonObject.toString(), PrepareUrlBean.class);
                                    if (prepareUrlBean != null) {
                                        fileUrl = prepareUrlBean.getTables().getTable().getRows().get(0).getBodyLink();
                                        if (fileUrl != null && !fileUrl.equals("")){
                                            if (sign.equals("1")){
                                                //获取文件名字和路径
                                                fileName= CommonWay.getFileNameFromUrl(fileUrl);
                                                sdPath = futils.getSDPATH() + subDir + fileName;
                                                downLoaderPDF();

                                            }else if (sign.equals("2")){
                                                Intent intent = new Intent(context, WebViewActivity.class);
                                                intent.putExtra("fileUrl", fileUrl);
                                                intent.putExtra("EnterMark", "0");
                                                startActivity(intent);

                                            }else if (sign.equals("3")){
                                                Intent intent = new Intent(context, VideoCustomActivity.class);
                                                intent.putExtra("fileUrl", fileUrl);
                                                intent.putExtra("EnterMark", 1);
                                                startActivity(intent);
                                            }else if (sign.equals("4") ){
                                                Intent intent = new Intent(context, VideoCustomActivity.class);
                                                intent.putExtra("fileUrl", fileUrl);
                                                intent.putExtra("EnterMark", 4);
                                                startActivity(intent);
                                            }

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



    //activity销毁时删除下载的全部文件
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String filesDir = futils.getSDPATH() + subDir;
        CommonWay.deleteDir(filesDir);

    }
}
