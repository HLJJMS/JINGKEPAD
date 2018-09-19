package smjj.pureclass_pad.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.GroupStudentBean;
import smjj.pureclass_pad.beans.StudentInternalBean;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.view.AutoHeightGridView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/15.
 */
//签到分组碎片界面
public class SignGroupFragment extends Fragment implements View.OnClickListener{

    private AutoHeightGridView signGridView;
    private CheckBox signAllCheckBox;
    private LinearLayout ll_sign, ll_group;
    private LinearLayout ll_group1, ll_group2, ll_group3, ll_group4, ll_group5, ll_group6;
    private AutoHeightGridView gridview_group1, gridview_group2, gridview_group3, gridview_group4
            , gridview_group5, gridview_group6;

    private TextView tv_group, tv_group_sure;
    private TextView tv_sign;
    private LinearLayout ll_check_sign_total;
    private TextView tv_check_sign_total, tv_check_sign_total1;

    private EditText et_group1, et_group2, et_group3, et_group4, et_group5, et_group6;

    private Button sign_sure_bt;
    private Context context;

    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> signList;
    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> signStudentList;
    private List<GroupStudentBean.TablesBean.TableBean.RowsBean> groupList1, groupList2, groupList3, groupList4, groupList5, groupList6;
    private List<GroupStudentBean.TablesBean.TableBean.RowsBean> groupTotalsList;

    private CommonAdapter signAdapter;
    private CommonAdapter groupAdapter1, groupAdapter2, groupAdapter3, groupAdapter4, groupAdapter5, groupAdapter6;

    private String classId;
    private String typleUser;
    private String userCode;
    private String studentNo;
    private String groupName;

    private GoClassActivity activity;

    private boolean isAllCheck;
    private boolean isSign;

    private SharedPreferences spConfig;

    private int groupAmonut;
    private String schoolNo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GoClassActivity) getActivity();
        context = getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_sign_group, container, false);

        findView(contentView);

        return contentView;
    }

    private void findView(View view){
        signGridView = (AutoHeightGridView) view.findViewById(R.id.gridview);
        signAllCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        sign_sure_bt = (Button) view.findViewById(R.id.log_bt);
        ll_sign = (LinearLayout) view.findViewById(R.id.ll_sign);
        ll_check_sign_total = (LinearLayout) view.findViewById(R.id.ll_check_sign_total);
        ll_group = (LinearLayout) view.findViewById(R.id.ll_group);
        ll_group1 = (LinearLayout) view.findViewById(R.id.ll_group1);
        ll_group2 = (LinearLayout) view.findViewById(R.id.ll_group2);
        ll_group3 = (LinearLayout) view.findViewById(R.id.ll_group3);
        ll_group4 = (LinearLayout) view.findViewById(R.id.ll_group4);
        ll_group5 = (LinearLayout) view.findViewById(R.id.ll_group5);
        ll_group6 = (LinearLayout) view.findViewById(R.id.ll_group6);
        gridview_group1 = (AutoHeightGridView) view.findViewById(R.id.gridview_group1);
        gridview_group2 = (AutoHeightGridView) view.findViewById(R.id.gridview_group2);
        gridview_group3 = (AutoHeightGridView) view.findViewById(R.id.gridview_group3);
        gridview_group4 = (AutoHeightGridView) view.findViewById(R.id.gridview_group4);
        gridview_group5 = (AutoHeightGridView) view.findViewById(R.id.gridview_group5);
        gridview_group6 = (AutoHeightGridView) view.findViewById(R.id.gridview_group6);
        et_group6 = (EditText) view.findViewById(R.id.et_group6);
        et_group5 = (EditText) view.findViewById(R.id.et_group5);
        et_group4 = (EditText) view.findViewById(R.id.et_group4);
        et_group3 = (EditText) view.findViewById(R.id.et_group3);
        et_group2 = (EditText) view.findViewById(R.id.et_group2);
        et_group1 = (EditText) view.findViewById(R.id.et_group1);

        tv_group = (TextView) view.findViewById(R.id.tv_group);
        tv_group_sure = (TextView) view.findViewById(R.id.tv_group_sure);
        tv_sign = (TextView) view.findViewById(R.id.tv_sign);
        tv_check_sign_total = (TextView) view.findViewById(R.id.tv_check_sign_total);
        tv_check_sign_total1 = (TextView) view.findViewById(R.id.tv_check_sign_total1);


        ll_group.setVisibility(View.GONE);
        signAllCheckBox.setChecked(true);
        isAllCheck = true;
        initAdapter();

        signAllCheckBox.setOnClickListener(this);
        sign_sure_bt.setOnClickListener(this);
        tv_group.setOnClickListener(this);
        tv_group_sure.setOnClickListener(this);

        if (activity.isDualTeacher.equals("0")){
            //双师  获取签到人数应到实到并展示
            checkSignTotal();
            //检查本节课是否已经上课
            checkGoClass();
            ll_check_sign_total.setVisibility(View.VISIBLE);
        }else {
            //非双师  老师检查是否签到如果已签到则返回签到学生，否则请求排课学生
            checkSign();
            //非双师 应到实到人数隐藏 展示学生姓名
            ll_check_sign_total.setVisibility(View.GONE);
            signGridView.setVisibility(View.VISIBLE);
        }

    }


    private void initAdapter(){

        signList = new ArrayList<>();
        signStudentList = new ArrayList<>();

        groupTotalsList = new ArrayList<>();
        groupList1 = new ArrayList<>();
        groupList2 = new ArrayList<>();
        groupList3 = new ArrayList<>();
        groupList4 = new ArrayList<>();
        groupList5 = new ArrayList<>();
        groupList6 = new ArrayList<>();


        //签到展示学生姓名的适配器
        signAdapter = new CommonAdapter<StudentInternalBean.TablesBean.TableBean.RowsBean>(context, signList, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final StudentInternalBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                int i = signList.indexOf(rowsBean);
                checkBox.setText(rowsBean.getStName());
                if (isSign){
                    //如果已签到返回的实体类学生姓名
                    checkBox.setText(rowsBean.getStudentName());
                }else {
                    //如果未签到返回的实体类学生姓名
                    checkBox.setText(rowsBean.getStName());
                }
                if (isAllCheck){
                    checkBox.setChecked(true);
                    checkBox.setTextColor(Color.WHITE);
                }else {
                    checkBox.setChecked(false);
                    checkBox.setTextColor(Color.parseColor("#666666"));

                }
                //默认全选
                if (isAllCheck){
                    signAllCheckBox.setChecked(true);
                    if (!signStudentList.contains(rowsBean)){
                        signStudentList.add(rowsBean);
                    }
                }

                if (isSign){//如果已签到则不能在选择学生
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
                    signAllCheckBox.setChecked(false);
                    signAllCheckBox.setEnabled(false);
                    checkBox.setTextColor(Color.WHITE);
                }


                if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                    //双师助教 需要判断该学生有没有签到
                    if (("1").equals(rowsBean.getStatus())){
                        checkBox.setTextColor(Color.WHITE);
                        checkBox.setChecked(true);
                    }else {
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setChecked(false);
                    }
                }

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * 当点击该checkbox时先走checkbox的状态改变方法（OnCheckedChange），然后才走onclick方法
                         * 即当点击checkbox走onclick方法时，该CheckBox的选中状态已经发生过变化了
                         *
                         */
                        if (checkBox.isChecked()){
                            if (!signStudentList.contains(rowsBean)){
                                signStudentList.add(rowsBean);
                            }
                            checkBox.setTextColor(Color.WHITE);
                        }else {
                            signStudentList.remove(rowsBean);
                            checkBox.setTextColor(Color.parseColor("#666666"));

                        }
                        if (signStudentList.size() == signList.size()){
                            signAllCheckBox.setChecked(true);
                            isAllCheck = true;
                        }else {
                            signAllCheckBox.setChecked(false);
                            isAllCheck = false;
                        }

                    }
                });

            }
        };

        //小组1适配器
        groupAdapter1 = new CommonAdapter<GroupStudentBean.TablesBean.TableBean.RowsBean>(context, groupList1, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final GroupStudentBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setChecked(true);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setClickable(false);
            }
        };
        //小组2适配器
        groupAdapter2 = new CommonAdapter<GroupStudentBean.TablesBean.TableBean.RowsBean>(context, groupList2, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final GroupStudentBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setChecked(true);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setClickable(false);
            }
        };

        groupAdapter3 = new CommonAdapter<GroupStudentBean.TablesBean.TableBean.RowsBean>(context, groupList3, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final GroupStudentBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setChecked(true);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setClickable(false);
            }
        };
        groupAdapter4 = new CommonAdapter<GroupStudentBean.TablesBean.TableBean.RowsBean>(context, groupList4, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final GroupStudentBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setChecked(true);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setClickable(false);
            }
        };
        groupAdapter5 = new CommonAdapter<GroupStudentBean.TablesBean.TableBean.RowsBean>(context, groupList5, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final GroupStudentBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setChecked(true);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setClickable(false);
            }
        };

        groupAdapter6 = new CommonAdapter<GroupStudentBean.TablesBean.TableBean.RowsBean>(context, groupList6, R.layout.item_schedling_gridview) {
            @Override
            public void convert(ViewHolder holder, final GroupStudentBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getStudentName());
                checkBox.setChecked(true);
                checkBox.setTextColor(Color.WHITE);
                checkBox.setClickable(false);
            }
        };


        signGridView.setAdapter(signAdapter);
        gridview_group1.setAdapter(groupAdapter1);
        gridview_group2.setAdapter(groupAdapter2);
        gridview_group3.setAdapter(groupAdapter3);
        gridview_group4.setAdapter(groupAdapter4);
        gridview_group5.setAdapter(groupAdapter5);
        gridview_group6.setAdapter(groupAdapter6);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkbox_all:
                if (signAllCheckBox.isChecked()){
                    isAllCheck = true;
                }else {
                    isAllCheck = false;
                    signStudentList.clear();
                }
                signAdapter.setData(signList);
                break;
            case R.id.log_bt:
                //点击确认按钮
                if (activity.isDualTeacher.equals("0")){
                    //双师且没有上课则则确认上课
                    if (activity.isCertificate.equals("0")){
                        sureGoClass();
                    }
                }else {
                    //非双师
                    if (isSign){
                        //已签到  检查分组如果已分组则展示分组信息
                        ll_sign.setVisibility(View.GONE);
                        ll_group.setVisibility(View.VISIBLE);
                        checkGroup();
                    }else {
                        //未签到
                        //请求网络保存签到数据
                        requestSign();
                    }
                }

                break;
            case R.id.tv_group:
                showPopupWindow();
                break;
            case R.id.tv_group_sure:
                //请求网络保存组名
                AmendGroupName();
                break;

        }

    }


    private void showPopupWindow(){
        View view = LayoutInflater.from(context).inflate(R.layout.pop_view2_right, null);
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,6,2,0.5f,-1);
        List<String> list = new ArrayList<>();
        list.add("分一组");
        list.add("分二组");
        list.add("分三组");
        list.add("分四组");
        list.add("分五组");
        list.add("分六组");

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<String>(context, list, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.item_pop_tv, s);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                groupAmonut = i + 1;
                //请求分组
                requestGroup();
                popupWindow.dismiss();
            }
        });

        PopupWindowUtil.showAtLoactionRight(tv_group);

    }

    //展示分组
    private void showGroup(){

        if (groupAmonut == 1){
            ll_group1.setVisibility(View.VISIBLE);
            ll_group2.setVisibility(View.GONE);
            ll_group3.setVisibility(View.GONE);
            ll_group4.setVisibility(View.GONE);
            ll_group5.setVisibility(View.GONE);
            ll_group6.setVisibility(View.GONE);
        }else if (groupAmonut == 2){
            ll_group1.setVisibility(View.VISIBLE);
            ll_group2.setVisibility(View.VISIBLE);
            ll_group3.setVisibility(View.GONE);
            ll_group4.setVisibility(View.GONE);
            ll_group5.setVisibility(View.GONE);
            ll_group6.setVisibility(View.GONE);
        }else if (groupAmonut == 3){
            ll_group1.setVisibility(View.VISIBLE);
            ll_group2.setVisibility(View.VISIBLE);
            ll_group3.setVisibility(View.VISIBLE);
            ll_group4.setVisibility(View.GONE);
            ll_group5.setVisibility(View.GONE);
            ll_group6.setVisibility(View.GONE);
        }else if (groupAmonut == 4){
            ll_group1.setVisibility(View.VISIBLE);
            ll_group2.setVisibility(View.VISIBLE);
            ll_group3.setVisibility(View.VISIBLE);
            ll_group4.setVisibility(View.VISIBLE);
            ll_group5.setVisibility(View.GONE);
            ll_group6.setVisibility(View.GONE);
        }else if (groupAmonut == 5){
            ll_group1.setVisibility(View.VISIBLE);
            ll_group2.setVisibility(View.VISIBLE);
            ll_group3.setVisibility(View.VISIBLE);
            ll_group4.setVisibility(View.VISIBLE);
            ll_group5.setVisibility(View.VISIBLE);
            ll_group6.setVisibility(View.GONE);
        }else if (groupAmonut == 6){
            ll_group1.setVisibility(View.VISIBLE);
            ll_group2.setVisibility(View.VISIBLE);
            ll_group3.setVisibility(View.VISIBLE);
            ll_group4.setVisibility(View.VISIBLE);
            ll_group5.setVisibility(View.VISIBLE);
            ll_group6.setVisibility(View.VISIBLE);
        }
    }

    //请求班课学生
    private void requestStudentData() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "排课列表异常请重新查看排课！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("classid", classId);
            jsonObject.put("TypeUser",typleUser);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SchedulingGetStudentMethodName;;

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
                                } else {
                                    Gson gson = new Gson();
                                    StudentInternalBean studentInternalBean = gson.fromJson(jsonObject.toString(), StudentInternalBean.class);
                                    if (studentInternalBean != null) {
                                        signList.addAll(studentInternalBean.getTables().getTable().getRows());
                                        signAdapter.setData(signList);
                                        signAdapter.notifyDataSetChanged();
                                        if (signList.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
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

    //检查学生是否签到
    private void checkSign(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("ClassID", classId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckSignMethodName;
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
                                if (resultvalue.equals("1")) {
                                    //未签到请求签到
                                    isSign = false;
                                    requestStudentData();
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    isSign = true;
                                    sign_sure_bt.setText("查看分组");
                                    tv_sign.setText("已签到");

                                    Gson gson = new Gson();
                                    StudentInternalBean studentInternalBean = gson.fromJson(jsonObject.toString(), StudentInternalBean.class);
                                    if (studentInternalBean != null) {
                                        signList.addAll(studentInternalBean.getTables().getTable().getRows());
                                        signAdapter.setData(signList);
                                        signAdapter.notifyDataSetChanged();
                                        if (signList.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "本堂课签到学生为零");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
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

    //检查学生是否分组
    private void checkGroup(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("ClassID", classId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckGroupMethodName;
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
                                if (resultvalue.equals("0")) {
                                    //已分组
                                    getGroupStudent();
                                    tv_group.setText("已分组");
                                    tv_group.setEnabled(false);
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
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

    //学生签到
    private void requestSign(){
        getStudentNO();

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("UserCode", userCode);
            jsonObject.put("ClassID", classId);
            jsonObject.put("BanJiID", "");
            jsonObject.put("BanJiName", "");
            jsonObject.put("StudentNo", studentNo + ",");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GoClassSignMethodName;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "签到失败");
                                } else if (resultvalue.equals("0")){
                                    Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                    ll_sign.setVisibility(View.GONE);
                                    ll_group.setVisibility(View.VISIBLE);
                                    sureGoClass();
                                    //签到成功自动跳转按钮展示
                                    activity.rightLowerButton.setVisibility(View.VISIBLE);
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

    //学生分组
    private void requestGroup(){
        getGroupName();

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

            jsonObject.put("ClassID", classId);
            jsonObject.put("BanJiID", "");
            jsonObject.put("BanJiName", "");
            jsonObject.put("GroupingName", groupName);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GroupMethodName;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "分组失败，请重试");
                                } else if (resultvalue.equals("0")){
                                    Toast.makeText(context, "分组成功", Toast.LENGTH_SHORT).show();
                                    getGroupStudent();
                                    tv_group.setText("已分组");
                                    tv_group.setEnabled(false);
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

            jsonObject.put("classId", classId);
            jsonObject.put("classNo", activity.classNo);
            jsonObject.put("SpeakID", activity.speakId);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SureGoClassMN2;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "上课失败");
                                } else if (resultvalue.equals("0")){
                                    if (activity.isDualTeacher.equals("0")){
                                        sign_sure_bt.setText("已确认");
                                        sign_sure_bt.setEnabled(false);
                                        sign_sure_bt.setVisibility(View.GONE);
                                        tv_sign.setText("已签到");
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

    //获取分组下的学生
    private void getGroupStudent(){
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "排课列表异常请重新查看排课！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ClassID", classId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;
            methodName = Constants.GroupStudentMethodName;

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未获取到分组学生");
                                } else {
                                    Gson gson = new Gson();
                                    GroupStudentBean groupStudentBean = gson.fromJson(jsonObject.toString(), GroupStudentBean.class);
                                    if (groupStudentBean != null) {
                                        groupTotalsList.addAll(groupStudentBean.getTables().getTable().getRows());
                                        addStudentToGroupList(groupTotalsList);
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
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

    //双师模式下检查学生签到人数
    private void checkSignTotal(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }
            jsonObject.put("ScheduleNo", classId);
            jsonObject.put("UserCode", userCode);
            if (activity.isDualTeacher.equals("0")){
                jsonObject.put("IsCertificate", activity.isCertificate);
            }else {
                jsonObject.put("IsCertificate", "2");
            }
            if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckSignTotalMN2;
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
                                    isSign = true;
                                    StudentInternalBean studentInternalBean = gson.fromJson(jsonObject.toString(), StudentInternalBean.class);
                                    if (studentInternalBean != null) {
                                        int total = 0;
                                        int signTotal = 0;
                                        if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                                            int size = studentInternalBean.getTables().getTable().getRows().size();
                                            List<String> arrayList = new ArrayList();
                                            for (int i = 0; i < size; i ++){
                                                //去掉重复数据
                                                if (!arrayList.contains(studentInternalBean.getTables().getTable().getRows().get(i).getStudentNo())){
                                                    arrayList.add(studentInternalBean.getTables().getTable().getRows().get(i).getStudentNo());
                                                    signList.add(studentInternalBean.getTables().getTable().getRows().get(i));
                                                }
                                            }

                                            total = signList.size();
                                            signGridView.setVisibility(View.VISIBLE);

                                        }else {
                                            signList.addAll(studentInternalBean.getTables().getTable().getRows());
                                            total = total + signList.get(0).getNumber();
                                            signGridView.setVisibility(View.GONE);

                                        }

                                        signTotal = signTotal + signList.get(0).getSNumber();
                                        tv_check_sign_total.setText(total + "");
                                        tv_check_sign_total1.setText(signTotal + "");
                                        signAdapter.setData(signList);
                                        signAdapter.notifyDataSetChanged();
                                        if (signList.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "本堂课签到学生为零");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
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
                                    if (activity.isCertificate.equals("1")){
                                        //助教不能确认上课
                                        sign_sure_bt.setVisibility(View.GONE);
                                    }else {
                                        sign_sure_bt.setVisibility(View.VISIBLE);
                                    }
                                    tv_sign.setText("签到");
                                } else {
                                    //已上课
                                    sign_sure_bt.setVisibility(View.GONE);
                                    tv_sign.setText("已签到");
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


    private void getStudentNO(){
        studentNo = "";
        for (int i = 0 ; i < signStudentList.size(); i ++){
            if (i == 0){
                studentNo = signStudentList.get(i).getStuNo() ;
            }else {
                studentNo = studentNo + "," + signStudentList.get(i).getStuNo();
            }
        }

    }

    private void getGroupName(){
        if (groupAmonut == 1){
            groupName = et_group1.getText().toString() + ",";
        }else if (groupAmonut == 2){
            groupName = et_group1.getText().toString() + ","
                    + et_group2.getText().toString() + ",";
        }else if (groupAmonut == 3){
            groupName = et_group1.getText().toString() + ","
                    + et_group2.getText().toString()+ ","
                    + et_group3.getText().toString()+ ",";
        }else if (groupAmonut == 4){
            groupName = et_group1.getText().toString() + ","
                    + et_group2.getText().toString()+ ","
                    + et_group3.getText().toString()+ ","
                    + et_group4.getText().toString()+ ",";
        }else if (groupAmonut == 5){
            groupName = et_group1.getText().toString() + ","
                    + et_group2.getText().toString()+ ","
                    + et_group3.getText().toString()+ ","
                    + et_group4.getText().toString()+ ","
                    + et_group5.getText().toString()+ ",";
        }else if (groupAmonut == 6){
            groupName = et_group1.getText().toString() + ","
                    + et_group2.getText().toString()+ ","
                    + et_group3.getText().toString()+ ","
                    + et_group4.getText().toString()+ ","
                    + et_group5.getText().toString()+ ","
                    + et_group6.getText().toString()+ ",";
        }
    }

    //获取的分组学生加入到相应的集合中并刷新适配器
    private void addStudentToGroupList(List<GroupStudentBean.TablesBean.TableBean.RowsBean> list){
        int amount = list.size();
        for (int i = 0; i < amount; i ++){
            int groupID = list.get(i).getGroupingID();
            if (groupAmonut <= groupID){
                groupAmonut = groupID;
            }
            if (groupID == 1){
                groupList1.add(list.get(i));
                et_group1.setText(list.get(i).getGroupingName());
            }
            if (groupID == 2){
                groupList2.add(list.get(i));
                et_group2.setText(list.get(i).getGroupingName());
            }
            if (groupID == 3){
                groupList3.add(list.get(i));
                et_group3.setText(list.get(i).getGroupingName());
            }
            if (groupID == 4){
                groupList4.add(list.get(i));
                et_group4.setText(list.get(i).getGroupingName());
            }
            if (groupID == 5){
                groupList5.add(list.get(i));
                et_group5.setText(list.get(i).getGroupingName());
            }
            if (groupID == 6){
                groupList6.add(list.get(i));
                et_group6.setText(list.get(i).getGroupingName());
            }

        }
        groupAdapter1.notifyDataSetChanged();
        groupAdapter2.notifyDataSetChanged();
        groupAdapter3.notifyDataSetChanged();
        groupAdapter4.notifyDataSetChanged();
        groupAdapter5.notifyDataSetChanged();
        groupAdapter6.notifyDataSetChanged();
        showGroup();



    }

    //检查组名是否为空
    private boolean checkGroupName(){
        boolean isNUll = false;
        if (groupAmonut == 1){
            if (et_group1.getText().toString() == null || et_group1.getText().toString().equals("")){
                isNUll = true;
            }
        }else if (groupAmonut == 2){
            if (et_group2.getText().toString() == null || et_group2.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group1.getText().toString() == null || et_group1.getText().toString().equals("")){
                isNUll = true;
            }
        }else if (groupAmonut == 3){

            if (et_group1.getText().toString() == null || et_group1.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group2.getText().toString() == null || et_group2.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group3.getText().toString() == null || et_group3.getText().toString().equals("")){
                isNUll = true;
            }
        }else if (groupAmonut == 4){
            if (et_group1.getText().toString() == null || et_group1.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group2.getText().toString() == null || et_group2.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group3.getText().toString() == null || et_group3.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group4.getText().toString() == null || et_group4.getText().toString().equals("")){
                isNUll = true;
            }

        }else if (groupAmonut == 5){
            if (et_group1.getText().toString() == null || et_group1.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group2.getText().toString() == null || et_group2.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group3.getText().toString() == null || et_group3.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group4.getText().toString() == null || et_group4.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group5.getText().toString() == null || et_group5.getText().toString().equals("")){
                isNUll = true;
            }
        }else if (groupAmonut == 6){
            if (et_group1.getText().toString() == null || et_group1.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group2.getText().toString() == null || et_group2.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group3.getText().toString() == null || et_group3.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group4.getText().toString() == null || et_group4.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group5.getText().toString() == null || et_group5.getText().toString().equals("")){
                isNUll = true;
            }
            if (et_group6.getText().toString() == null || et_group6.getText().toString().equals("")){
                isNUll = true;
            }

        }

        return isNUll;

    }

    //修改组名
    private void AmendGroupName(){

        if (checkGroupName()){
            AlertDialogUtil.showAlertDialog(context, "提示", "所有小组的组名不能为空");
            return;
        }
        getGroupName();
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("ClassID", classId);
            jsonObject.put("GroupingName", groupName);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GroupNameAmendMethodName;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "修改失败，请重试");
                                } else if (resultvalue.equals("0")){
                                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                                    tv_group_sure.setText("完成");
                                    tv_group_sure.setEnabled(false);
                                    et_group1.setEnabled(false);
                                    et_group2.setEnabled(false);
                                    et_group3.setEnabled(false);
                                    et_group4.setEnabled(false);
                                    et_group5.setEnabled(false);
                                    et_group6.setEnabled(false);
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
