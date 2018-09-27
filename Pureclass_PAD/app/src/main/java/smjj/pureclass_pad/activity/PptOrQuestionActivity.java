package smjj.pureclass_pad.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.UriToFliePath;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;

public class PptOrQuestionActivity extends AppCompatActivity {
    TextView path, common_title_tv;
    Button ok, ship;
    File file;

    private static final int FILE_SELECT_CODE = 0;
    private String grade, subject, parentID, knowledgeID, selectDataGram, speakId, speakName, materialName, materialNo, classNo, startTime, className, packageNo, url;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt_or_question);
        context = this;
        findView();
        click();
    }

    private void findView() {
        common_title_tv = (TextView) findViewById(R.id.common_title_tv);
        common_title_tv.setText("请选择上传的ppt");
        path = (TextView) findViewById(R.id.url);
        ok = (Button) findViewById(R.id.ok);
        ship = (Button) findViewById(R.id.select);
        subject = getIntent().getStringExtra("Subject");
        grade = getIntent().getStringExtra("Grade");
        selectDataGram = getIntent().getStringExtra("SelectDataGram");
        speakId = getIntent().getStringExtra("SpeakId");
        speakName = getIntent().getStringExtra("SpeakName");
        materialName = getIntent().getStringExtra("MaterialName");
        materialNo = getIntent().getStringExtra("MaterialNo");
        classNo = getIntent().getStringExtra("ClassNo");
        startTime = getIntent().getStringExtra("StartTime");
        className = getIntent().getStringExtra("ClassName");
        packageNo = getIntent().getStringExtra("PackageNo");
    }

    private void click() {
        path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(PptOrQuestionActivity.this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PptOrQuestionActivity.this, PersonalLessonsActivity.class);
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
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        updataOld();
                updataNew();
            }
        });
    }

    private void updataNew() {

        try{
            FileInputStream fis = new FileInputStream(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while((count = fis.read(buffer)) >= 0){
                baos.write(buffer, 0, count);
            }
            String uploadBuffer = new String(Base64.encode(buffer,count));  //进行Base64编码
            connectWebService(uploadBuffer);
            Log.e("connectWebService", "start");
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void connectWebService(String uploadBuffer) {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            String is = getBytesFromFile(file);
            jsonObject.put("abc", "a");
            jsonObject.put("file", uploadBuffer);
            final String jsonCode = jsonObject.toJSONString();
//                String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = "B002015";
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
//                map.put("token", MD5Result);

            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {

                    //关闭进度条
                    if (result != null) {

                        org.json.JSONObject jsonObject = CommonWay.parseSoapObjectUnicode(result);
                        if (jsonObject != null) {
                            try {
                                String resultvalue = "";
                                if (jsonObject.toString().contains("resultvalue")) {
                                    resultvalue = jsonObject.getString("resultvalue");
                                  ok.setText(jsonObject.toString());
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

    private void updataOld() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            File file = new File(url);
            String is = getBytesFromFile(file);
            jsonObject.put("abc", "a");
            jsonObject.put("file", is);
            final String jsonCode = jsonObject.toJSONString();
//                String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = "B002015";
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
//                map.put("token", MD5Result);

            //通过工具类调用WebService接口
            WebServiceUtils.callWebService(Constants.WEB_SERVER_URL, methodName, map, new WebServiceUtils.WebServiceCallBack() {
                //WebService接口返回的数据回调到这个方法中
                @Override
                public void callBackSuccess(SoapObject result) {

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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "添加失败");
                                } else if (resultvalue.equals("0")) {
                                    Toast.makeText(PptOrQuestionActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(context,ClassListActivity.class);
//                                    startActivity(intent);
                                    finish();
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

    public static String getBytesFromFile(File file) {
        String result = null;
        byte[] ret = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
            result = Base64.encodeToString(ret, 0, ret.length, Base64.DEFAULT);
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Log.e("shiabi", "onActivityResult() error, resultCode: " + resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            url = UriToFliePath.getPhotoPathFromContentUri(this, uri);
            path.setText(url);
            file=new File(url);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
