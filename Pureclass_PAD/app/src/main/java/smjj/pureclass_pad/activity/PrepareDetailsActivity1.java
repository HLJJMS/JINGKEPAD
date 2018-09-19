package smjj.pureclass_pad.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.DataGramBean2;
import smjj.pureclass_pad.beans.PrepareUrlBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.HttpDownloader;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.FileUtils;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;

import static smjj.pureclass_pad.util.OpenFileUtil.getPdfFileIntent;
//二期备课详情界面，讲义、课件\说授课视屏
public class PrepareDetailsActivity1 extends BaseActivity
        implements View.OnClickListener {

    private TextView onBack;
    private ImageView iv_home, iv_add;
    private TextView title_tv;

    private Context context;
    private Handler mHandler;
    private SharedPreferences spConfig;

    private RelativeLayout rl_back1, rl_home, rl_add;

    private LinearLayout ll_handouts, ll_ppt, ll_teaching_video, ll_speak_video, ll_create;

    private ImageView iv_create, iv_sure_pre_class;

    private String userCode;
    private String classId;
    private String typleUser;
    private String speakId;

    private String className, schoolName, materialName, stuName, materialNo;
    private String selectType;//班课类型

    private TextView tv_courser, tv_class_name, tv_school_name, tv_stu_name, tv_speach_name;
    private ImageView iv_stu_name;
    private LinearLayout ll_sdu_time;
    private TextView tv_sdu_time;

    private List<DataGramBean2.TablesBean.TableBean.RowsBean> dataGramList;

    //1 代表讲义，2代表ppt课件，3代表授课视频，4代表说课视屏
    private String sign;
    private String speakName = "";
    private String selectDataGram;//1代表基础包 2代表新建 3 代表选择其他包
    private String packageNo = "";
    private String startTime;
    private String classNo, grade, subject;
    private String isPrepare;//1代表已备课

    int result = 8;
    FileUtils futils;
    String fileUrl;
    String fileName;
    String subDir;
    String sdPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_details1);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        speakId = getIntent().getStringExtra("SpeakId");
        speakName = getIntent().getStringExtra("SpeakName");
        selectDataGram = getIntent().getStringExtra("SelectDataGram");
        className = getIntent().getStringExtra("ClassName");
        schoolName = getIntent().getStringExtra("SchoolName");
        materialName = getIntent().getStringExtra("MaterialName");
        materialNo = getIntent().getStringExtra("MaterialNo");
        stuName = getIntent().getStringExtra("StuName");
        selectType = getIntent().getStringExtra("SelectType");
        classNo = getIntent().getStringExtra("ClassNo");
        grade = getIntent().getStringExtra("Grade");
        subject = getIntent().getStringExtra("Subject");
        packageNo = getIntent().getStringExtra("PackageNo");
        startTime = getIntent().getStringExtra("StartTime");
        isPrepare = getIntent().getStringExtra("IsPrepare");

        findView();

    }

    private void findView() {
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_create = (ImageView) findViewById(R.id.iv_create);
        iv_sure_pre_class = (ImageView) findViewById(R.id.iv_sure_pre_class);
        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        ll_handouts = (LinearLayout) findViewById(R.id.ll_handouts);
        ll_ppt = (LinearLayout) findViewById(R.id.ll_ppt);
        ll_teaching_video = (LinearLayout) findViewById(R.id.ll_teaching_video);
        ll_speak_video = (LinearLayout) findViewById(R.id.ll_speak_video);
        ll_create = (LinearLayout) findViewById(R.id.ll_create);
        ll_sdu_time = (LinearLayout) findViewById(R.id.ll_sdu_time);
        tv_sdu_time = (TextView) findViewById(R.id.tv_sdu_time);
        tv_courser = (TextView) findViewById(R.id.tv_courser);
        tv_class_name = (TextView) findViewById(R.id.tv_class_name);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_speach_name = (TextView) findViewById(R.id.tv_speach_name);
        tv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        iv_stu_name = (ImageView) findViewById(R.id.iv_stu_name);


        title_tv.setText("备课详情");
        rl_back1.setVisibility(View.VISIBLE);
        iv_home.setImageResource(R.drawable.select_data_gram);

        tv_courser.setText(materialName);
        tv_class_name.setText(className);
        tv_school_name.setText(schoolName);
        tv_stu_name.setText(stuName);
        tv_speach_name.setText(speakName);


        dataGramList = new ArrayList<>();

        if (selectType.equals("一对一")){
            iv_stu_name.setVisibility(View.GONE);
            tv_stu_name.setVisibility(View.GONE);
        }

        if (selectDataGram.equals("1")) {
            iv_create.setImageResource(R.drawable.create2);
        } else if (selectDataGram.equals("2")) {
            iv_create.setImageResource(R.drawable.create1);
            iv_sure_pre_class.setVisibility(View.GONE);
        } else {
            if (packageNo.equals("0")){
                iv_create.setImageResource(R.drawable.create2);
                selectDataGram = "1";
            }else {
                iv_create.setImageResource(R.drawable.create3);
            }
        }

        if (isPrepare != null && isPrepare.equals("1")){
            iv_sure_pre_class.setVisibility(View.GONE);
            rl_home.setVisibility(View.INVISIBLE);
        }

        futils= new FileUtils();
        subDir = "wenxin_download/";

//        fileUrl = "http://erp.iwenxin.net/Lecture/LectureUpLoad/20151103165849080_877136.pdf";


        title_tv.setText(speakName);

        ll_handouts.setOnClickListener(this);
        ll_ppt.setOnClickListener(this);
        ll_speak_video.setOnClickListener(this);
        ll_teaching_video.setOnClickListener(this);
        ll_create.setOnClickListener(this);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_add.setOnClickListener(this);
        iv_sure_pre_class.setOnClickListener(this);

//        getDataGram();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                getDataGram();
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
            case R.id.ll_create:
                if (selectDataGram.equals("2")){

                    Intent intent = new Intent(context,  PptOrQuestionActivity.class);
                    intent.putExtra("SelectDataGram", selectDataGram);
                    intent.putExtra("SpeakId", speakId);
                    intent.putExtra("SpeakName", speakName);
                    intent.putExtra("MaterialName", materialName);
                    intent.putExtra("MaterialNo", materialNo);
                    intent.putExtra("ClassNo", classNo);
                    intent.putExtra("ClassName", className);
                    intent.putExtra("Grade", grade);
                    intent.putExtra("Subject", subject);
                    intent.putExtra("StartTime", startTime);
                    startActivity(intent);

//                    Intent intent = new Intent(context, PersonalLessonsActivity.class);
//                    intent.putExtra("SelectDataGram", selectDataGram);
//                    intent.putExtra("SpeakId", speakId);
//                    intent.putExtra("SpeakName", speakName);
//                    intent.putExtra("MaterialName", materialName);
//                    intent.putExtra("MaterialNo", materialNo);
//                    intent.putExtra("ClassNo", classNo);
//                    intent.putExtra("ClassName", className);
//                    intent.putExtra("Grade", grade);
//                    intent.putExtra("Subject", subject);
//                    intent.putExtra("StartTime", startTime);
//                    startActivity(intent);
                }else if (selectDataGram.equals("3")){
                    Intent intent = new Intent(context, LessonsBskActivity.class);
                    intent.putExtra("SelectDataGram", selectDataGram);
                    intent.putExtra("SpeakId", speakId);
                    intent.putExtra("MaterialName", materialName);
                    intent.putExtra("MaterialNo", materialNo);
                    intent.putExtra("ClassNo", classNo);
                    intent.putExtra("PackageNo", packageNo);
                    startActivity(intent);
                }
                break;

            case R.id.iv_sure_pre_class:
                if (selectDataGram.equals("1")){
                    packageNo = "0";
                }
                surePrepareClass();
                break;
        }

    }

    private void showPopupWindow(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow1(context,view,7,3,0.5f,R.style.pop_up_down);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<DataGramBean2.TablesBean.TableBean.RowsBean>(context, dataGramList, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, DataGramBean2.TablesBean.TableBean.RowsBean rowsBean) {
                TextView tv = holder.getView(R.id.item_pop_tv);
                tv.setText(rowsBean.getPackageName());
                tv.setTextColor(Color.parseColor("#00852f"));
                if (packageNo.equals("")){
                    packageNo = dataGramList.get(0).getPackageNo();
                }
                if (packageNo.equals(rowsBean.getPackageNo())) {
                    tv.setTextColor(Color.parseColor("#f6aa00"));
                } else {
                    tv.setTextColor(Color.parseColor("#666666"));
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!packageNo.equals(dataGramList.get(i).getPackageNo())){
                    packageNo = dataGramList.get(i).getPackageNo();
                    if (i == 0){
                        iv_create.setImageResource(R.drawable.create2);
                        iv_sure_pre_class.setVisibility(View.VISIBLE);
                        selectDataGram = "1";
                    }else if (i == 1){
                        iv_create.setImageResource(R.drawable.create1);
                        iv_sure_pre_class.setVisibility(View.GONE);
                        selectDataGram = "2";
                    }else {
                        iv_create.setImageResource(R.drawable.create3);
                        iv_sure_pre_class.setVisibility(View.VISIBLE);
                        selectDataGram = "3";
                    }
                }
                popupWindow.dismiss();
            }
        });
        PopupWindowUtil.showAtLoactionRightAndBottom(iv_home);
    }



    private void showDataGramPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_select_gram, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        final TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        ListView lv = (ListView) view.findViewById(R.id.pop_lv);

        final CommonAdapter adapter1 = new CommonAdapter<DataGramBean2.TablesBean.TableBean.RowsBean>(context, dataGramList, R.layout.item_pop_select_gram) {
            @Override
            public void convert(ViewHolder holder, final DataGramBean2.TablesBean.TableBean.RowsBean rowsBean) {
                ImageView imageView = holder.getView(R.id.iv_data_gram);
                TextView textView1 = holder.getView(R.id.tv_data_gram_name);
                textView1.setText(rowsBean.getPackageName());
                imageView.setVisibility(View.GONE);
                if (packageNo == null ||packageNo.equals("")){
                    packageNo = dataGramList.get(0).getPackageNo();
                }
                if (packageNo.equals(rowsBean.getPackageNo())) {
                    textView1.setTextColor(Color.parseColor("#f6aa00"));
                } else {
                    textView1.setTextColor(Color.parseColor("#666666"));
                }
            }
        };

        lv.setAdapter(adapter1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!packageNo.equals(dataGramList.get(i).getPackageNo())){
                    packageNo = dataGramList.get(i).getPackageNo();
                    if (i == 0){
                        iv_create.setImageResource(R.drawable.create2);
                        iv_sure_pre_class.setVisibility(View.VISIBLE);
                        selectDataGram = "1";
                    }else if (i == 1){
                        iv_create.setImageResource(R.drawable.create1);
                        iv_sure_pre_class.setVisibility(View.GONE);
                        selectDataGram = "2";
                    }else {
                        iv_create.setImageResource(R.drawable.create3);
                        iv_sure_pre_class.setVisibility(View.VISIBLE);
                        selectDataGram = "3";
                    }
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

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

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonType", sign);

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
                                        fileUrl = prepareUrlBean.getTables().getTable().getRows().get(0).getBodyLink();
                                        if (fileUrl != null && !fileUrl.equals("")){
                                            if (sign.equals("1")){
                                                Intent intent = new Intent(context, HandoutsWebActivity.class);
                                                intent.putExtra("fileUrl", fileUrl);
                                                intent.putExtra("EnterMark", "1");
                                                startActivity(intent);

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
                                                intent.putExtra("EnterMark", 2);
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

    //二期确认备课
    private void surePrepareClass() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "该排课信息不完善，请重新选择排课列表！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (packageNo == null || packageNo.equals("")) {
                Toast.makeText(context, "资料包编号为空，无法确认备课！", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(PrepareDetailsActivity1.this, "确认备课成功", Toast.LENGTH_SHORT).show();
                                    iv_sure_pre_class.setVisibility(View.GONE);
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
                                    DataGramBean2.TablesBean.TableBean.RowsBean rowsBean = new DataGramBean2.TablesBean.TableBean.RowsBean();
                                    rowsBean.setPackageName("基础包");
                                    rowsBean.setPackageNo("0");
                                    DataGramBean2.TablesBean.TableBean.RowsBean rowsBean1 = new DataGramBean2.TablesBean.TableBean.RowsBean();
                                    rowsBean1.setPackageName("新建");
                                    rowsBean1.setPackageNo("1");
                                    dataGramList.add(rowsBean);
                                    dataGramList.add(rowsBean1);
                                    showDataGramPopu();
                                } else {
                                    Gson gson = new Gson();
                                    DataGramBean2 dataGramBean2 = gson.fromJson(jsonObject.toString(), DataGramBean2.class);
                                    if (dataGramBean2 != null) {
                                        //创建新建和基础包选项
                                        DataGramBean2.TablesBean.TableBean.RowsBean rowsBean = new DataGramBean2.TablesBean.TableBean.RowsBean();
                                        rowsBean.setPackageName("基础包");
                                        rowsBean.setPackageNo("0");
                                        DataGramBean2.TablesBean.TableBean.RowsBean rowsBean1 = new DataGramBean2.TablesBean.TableBean.RowsBean();
                                        rowsBean1.setPackageName("新建");
                                        rowsBean1.setPackageNo("1");
                                        dataGramList.add(rowsBean);
                                        dataGramList.add(rowsBean1);
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

    //activity销毁时删除下载的全部文件
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String filesDir = futils.getSDPATH() + subDir;
        CommonWay.deleteDir(filesDir);

    }


}
