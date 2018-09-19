package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.SchoolSignBean;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2018/5/30.
 */
//主讲老师签到碎片
public class SignFragment extends Fragment implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView listView;
    private TextView empty;
    private Context context;
    private GoClassActivity activity;

    private TextView tv_class_name, tv_class_attendance, tv_class_total, tv_class_rate;

    private Handler mHandler;
    private List<SchoolSignBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;

    private Button sign_sure_bt;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private String userName;
    private String grade, subject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        mHandler = new Handler();
        activity = (GoClassActivity) getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        userName = spConfig.getString(SPCommonInfoBean.userName,"");
        grade =spConfig.getString(SPCommonInfoBean.selectGrade, "");
        subject = spConfig.getString(SPCommonInfoBean.selectSubject, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_sign, container, false);

        findView(contentView);
        return contentView;
    }


    private void findView(View view){
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        empty = (TextView) view.findViewById(R.id.empty);
        tv_class_name = (TextView) view.findViewById(R.id.tv_class_name);
        tv_class_attendance = (TextView) view.findViewById(R.id.tv_class_attendance);
        tv_class_total = (TextView) view.findViewById(R.id.tv_class_total);
        tv_class_rate = (TextView) view.findViewById(R.id.tv_class_rate);
        sign_sure_bt = (Button) view.findViewById(R.id.log_bt);


        listData = new ArrayList<>();

        adapter = new CommonAdapter<SchoolSignBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_sign_show) {
            @Override
            public void convert(ViewHolder holder, final SchoolSignBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);
                holder.setText(R.id.tv_branch_name, rowsBean.getClassName());
                holder.setText(R.id.tv_branch_attendance, rowsBean.getSNumber()+ "");
                holder.setText(R.id.tv_branch_total, rowsBean.getNumber() + "");
                holder.setText(R.id.tv_branch_rate, rowsBean.getRate());

            }
        };
        listView.setEmptyView(empty);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(true);
        sign_sure_bt.setOnClickListener(this);

        checkGoClass();
        getSignTotal();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.log_bt:
                //确认上课签到
                sureGoClass();
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //下拉刷新
                onLoad();

                listData.clear();
                getSignTotal();
            }
        }, 500);

    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //上拉加载更多
                onLoad();
            }
        }, 500);
    }
    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        listView.setRefreshTime(df.format(new Date()));
    }


    //确认上课
    private void sureGoClass(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            jsonObject.put("LessonID", classId);
            jsonObject.put("classNo", activity.classNo);
            jsonObject.put("SpeakID", activity.speakId);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("GradeName", grade);
            jsonObject.put("Subject", subject);
            jsonObject.put("PackageNo",activity.packageNo);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SignAndRelMN2;
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
                                    Log.e("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "上课失败");
                                } else if (resultvalue.equals("0")){
                                    if (activity.isDualTeacher.equals("0")){
                                        sign_sure_bt.setText("已确认");
                                        sign_sure_bt.setEnabled(false);
                                        sign_sure_bt.setVisibility(View.GONE);
                                    }
                                }else {
                                    AlertDialogUtil.showAlertDialog(context, "提示", "上课确认失败");
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


    //检查是否已经上课
    private void checkGoClass(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("classId", classId);
            jsonObject.put("lessId", activity.speakId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckGoClassMN2;
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
                                if (resultvalue.equals("0") || resultvalue.equals("-1")) {
                                    //未签到请求签到
                                    sign_sure_bt.setVisibility(View.VISIBLE);

                                } else {
                                    //已上课
                                    sign_sure_bt.setVisibility(View.GONE);
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


    //双师模式下检查学生签到人数
    private void getSignTotal(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("classId", classId);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetSignStuMN2;
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
                                if (resultvalue.equals("-1") || resultvalue.equals("1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "本节课还没有学生签到");
                                }else {
                                    //获取学生
                                    Gson gson = new Gson();
                                    SchoolSignBean schoolSignBean = gson.fromJson(jsonObject.toString(), SchoolSignBean.class);

                                    if (schoolSignBean != null){
                                        if (schoolSignBean.getTables().getTable().getRows().size() != 0){
                                            listData.addAll(schoolSignBean.getTables().getTable().getRows());
                                            tv_class_name.setText("班级:  " + listData.get(0).getClassName());
                                            tv_class_attendance.setText(listData.get(0).getSNumber() + "");
                                            tv_class_total.setText(listData.get(0).getNumber() + "");
                                            tv_class_rate.setText(listData.get(0).getRate());

                                            listData.remove(0);
                                            adapter.setData(listData);
                                            adapter.notifyDataSetChanged();

                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无数据");

                                        }

                                    }else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
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
