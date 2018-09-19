package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import smjj.pureclass_pad.activity.ClassAfterActivity1;
import smjj.pureclass_pad.activity.FeedbackActivity;
import smjj.pureclass_pad.beans.SpeachBean2;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/11/23.
 */
//二期课堂反馈
public class FeedbackFragment1 extends Fragment implements AdapterView.OnItemClickListener{
    private GridView gridView;

    private TextView tv_courser, tv_class_name, tv_school_name, tv_stu_name;
    private ImageView iv_stu_name;
    private LinearLayout ll_sdu_time;
    private TextView tv_sdu_time;

    private Context context;
    private Handler mHandler;
    private SharedPreferences spConfig;

    private List<SpeachBean2.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private ClassAfterActivity1 activity;

    private String classId;
    private String grade, subject;

    private String userCode;
    private String typleUser;


    //1代表未布置作业， 2代表一布置作业
    private String status;

    private String startTime, entTime;
    private boolean isAddFeedback = true;
    private String isDualTeacher;//是否双师 0是双师 1非双师
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mHandler = new Handler();
        activity = (ClassAfterActivity1) getActivity();
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        isDualTeacher = spConfig.getString(SPCommonInfoBean.isDualTeacher,"");
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_feedback1, container, false);

        findView(contentView);
        return contentView;
    }

    private void findView(View view) {
        gridView = (GridView) view.findViewById(R.id.scheduling_lv);
        ll_sdu_time = (LinearLayout) view.findViewById(R.id.ll_sdu_time);
        tv_courser = (TextView) view.findViewById(R.id.tv_courser);
        tv_class_name = (TextView) view.findViewById(R.id.tv_class_name);
        tv_school_name = (TextView) view.findViewById(R.id.tv_school_name);
        tv_stu_name = (TextView) view.findViewById(R.id.tv_stu_name);
        iv_stu_name = (ImageView) view.findViewById(R.id.iv_stu_name);

        tv_courser.setText(activity.materialName);
        tv_class_name.setText(activity.className);
        tv_school_name.setText(activity.schoolName);
        tv_stu_name.setText(activity.stuName);

        ll_sdu_time.setVisibility(View.GONE);

        listData = new ArrayList<>();

        adapter = new CommonAdapter<SpeachBean2.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_speech_gv) {
            @Override
            public void convert(ViewHolder holder, final SpeachBean2.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);
                String str = rowsBean.getSpeakName();
                if (str != null && str.length() > 3){
                    if (i >= 10){
                        holder.setText(R.id.tv_speech1, str.substring(0,4));
                        holder.setText(R.id.tv_speech_name, str.substring(4));
                    }else {
                        holder.setText(R.id.tv_speech1, str.substring(0,3));
                        holder.setText(R.id.tv_speech_name, str.substring(3));
                    }
                }else {
                    holder.setText(R.id.tv_speech1, str);
                    holder.setText(R.id.tv_speech_name, "");
                }

                ImageView iv_speech = holder.getView(R.id.iv_speech);
                TextView tv_time_date = holder.getView(R.id.tv_time_date);
                TextView tv_time_hour = holder.getView(R.id.tv_time_hour);

                int classStatus = rowsBean.getBeginStatus();//上课状态
                int prepareStatus = rowsBean.getLessonStatus();//备课状态

                if (classStatus == 1){
                    iv_speech.setImageResource(R.drawable.yishangke);
                    tv_time_date.setVisibility(View.VISIBLE);
                    tv_time_hour.setVisibility(View.VISIBLE);
                    if (rowsBean.getClassTime() != null && !rowsBean.getClassTime().equals("")) {
                        tv_time_date.setText(rowsBean.getClassTime().substring(0, 10));
                        tv_time_hour.setText(rowsBean.getClassTime().substring(11,16));
                    }
                }else {
                    if (prepareStatus == 0){
                        iv_speech.setImageResource(R.drawable.weibeike);
                        tv_time_date.setVisibility(View.INVISIBLE);
                        tv_time_hour.setVisibility(View.INVISIBLE);
                    }else {

                        iv_speech.setImageResource(R.drawable.weishangke);

                        tv_time_date.setVisibility(View.VISIBLE);
                        tv_time_hour.setVisibility(View.VISIBLE);
                        if (rowsBean.getClassTime() != null && !rowsBean.getClassTime().equals("")) {
                            tv_time_date.setText(rowsBean.getClassTime().substring(0, 10));
                            tv_time_hour.setText(rowsBean.getClassTime().substring(11,16));
                        }
                    }
                }

            }
        };

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        isAddFeedback = true;
        startTime = "";
        entTime = "";
        refreshData(isAddFeedback, startTime, entTime);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        classId = listData.get(i).getClassID();

        spConfig.edit().putString(SPCommonInfoBean.classId, classId).commit();

        String time = listData.get(i).getClassTime();
        if (time != null && !time.equals("")){
            time = time.substring(0,10);
        }


        Intent intent = new Intent(context, FeedbackActivity.class);
        if (isAddFeedback){
            intent.putExtra("enterMark", "1");
            intent.putExtra("Time", time);
            intent.putExtra("ClassName", activity.className);
            intent.putExtra("Subject", activity.subject);
        }else {
            intent.putExtra("enterMark", "2");
            intent.putExtra("Time", time);
            intent.putExtra("ClassName", activity.className);
            intent.putExtra("Subject", activity.subject);
        }

        startActivity(intent);

    }

    public void refreshData(boolean isSettingWork1, String startDate, String endDate){
        isAddFeedback = isSettingWork1;
        startTime = startDate;
        entTime = endDate;
        if (activity.selectType.equals("一对一")){
            tv_stu_name.setVisibility(View.GONE);
            iv_stu_name.setVisibility(View.GONE);
        }
        listData.clear();
        if (isAddFeedback){
            status = "1";
        }else {
            status = "2";
        }
        requestScdData();
        adapter.setData(listData);
        adapter.notifyDataSetChanged();
    }


    //请求排课列表(教学反馈)
    public void requestScdData() {
        String UserCode = spConfig.getString(SPCommonInfoBean.userCode, "");

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (UserCode == null || UserCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startTime == null){
                startTime = "";
            }
            if (entTime == null){
                entTime = "";
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", UserCode);
            jsonObject.put("startdate", startTime);
            jsonObject.put("enddate", entTime);
            jsonObject.put("TypeUser",typleUser);
            jsonObject.put("ClassNo", activity.classNo);
            jsonObject.put("classType", activity.selectType);
            if (isDualTeacher.equals("0") && isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetCAFeedbackcMN2;

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
//                                    AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                                    if (isAddFeedback){
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该班级无需要添加的课堂反馈");
                                    }else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该班级无可以查看的课堂反馈");
                                    }

                                } else {
                                    Gson gson = new Gson();
                                    SpeachBean2 speachBean2 = gson.fromJson(jsonObject.toString(), SpeachBean2.class);
                                    if (speachBean2 != null) {
                                        if (speachBean2.getTables().getTable().getRows().size() != 0){
                                            listData.addAll(speachBean2.getTables().getTable().getRows());
//                                            FormatUtils.getListRank(listData);
                                            adapter.setData(listData);
                                            adapter.notifyDataSetChanged();
                                        }else {
                                            if (isAddFeedback){
                                                AlertDialogUtil.showAlertDialog(context, "提示", "该班级无需要添加的课堂反馈");
                                            }else {
                                                AlertDialogUtil.showAlertDialog(context, "提示", "该班级无可以查看的课堂反馈");
                                            }
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "网络出错");
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


}
