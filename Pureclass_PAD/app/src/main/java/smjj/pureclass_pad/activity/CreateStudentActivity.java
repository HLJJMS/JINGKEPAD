package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.AreaInfoBean;
import smjj.pureclass_pad.beans.CityInfoBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.FormatUtils;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;

//添加学生界面
public class CreateStudentActivity extends BaseActivity implements View.OnClickListener{

    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private RadioGroup rg_sex;
    private RadioGroup rg_school_rank, rg_grade;
    private RadioButton rb_create_primary, rb_create_high, rb_create_university;
    private RadioButton rb_create_primary_first, rb_create_primary_second, rb_create_primary_three
            , rb_create_primary_four, rb_create_primary_five, rb_create_primary_six;
    private RadioButton rb_man, rb_woman;
    private LinearLayout ll_select_city, ll_select_area;
    private TextView tv_select_city, tv_select_area;
    private LinearLayout ll_birthday;
    private TextView tv_birthday;
    private EditText et_student_name, et_parent_phone, et_home_address, et_school_name;
    private Button saveBt;

    private Context context;
    private TimePickerView pvTime;

    private SharedPreferences spConfig;

    private RelativeLayout rl_back, rl_home, rl_add;


    private List<CityInfoBean.TablesBean.TableBean.RowsBean> cityList;
    private List<AreaInfoBean.TablesBean.TableBean.RowsBean> areaList;
    private boolean isFirstGetCity = true;
    private String grade, school_rank, student_name, parent_phone, sex;
    private String birthday, school_name, home_address;
    private int cityRegionID, areaRegionID;
    private String userCode, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);

        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        ActivityManage.addActivity(this);
        context = this;


        findView();
        setRgCheckedListener();



    }

    private void findView() {
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rg_school_rank = (RadioGroup) findViewById(R.id.rg_school_rank);
        rg_grade = (RadioGroup) findViewById(R.id.rg_grade);
        rb_create_primary = (RadioButton) findViewById(R.id.rb_create_primary);
        rb_create_high = (RadioButton) findViewById(R.id.rb_create_high);
        rb_create_university = (RadioButton) findViewById(R.id.rb_create_university);
        rb_create_primary_first = (RadioButton) findViewById(R.id.rb_create_primary_first);
        rb_create_primary_second = (RadioButton) findViewById(R.id.rb_create_primary_second);
        rb_create_primary_three = (RadioButton) findViewById(R.id.rb_create_primary_three);
        rb_create_primary_four = (RadioButton) findViewById(R.id.rb_create_primary_four);
        rb_create_primary_five = (RadioButton) findViewById(R.id.rb_create_primary_five);
        rb_create_primary_six = (RadioButton) findViewById(R.id.rb_create_primary_six);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        ll_select_city = (LinearLayout) findViewById(R.id.ll_select_city);
        ll_select_area = (LinearLayout) findViewById(R.id.ll_select_area);
        tv_select_city = (TextView) findViewById(R.id.tv_selext_city);
        tv_select_area = (TextView) findViewById(R.id.tv_selext_area);
        ll_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        et_school_name = (EditText) findViewById(R.id.et_school_name);
        et_student_name = (EditText) findViewById(R.id.et_student_name);
        et_parent_phone = (EditText) findViewById(R.id.et_parent_phone);
        et_home_address = (EditText) findViewById(R.id.et_home_address);
        saveBt = (Button) findViewById(R.id.log_bt);

        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);


        cityList = new ArrayList<>();
        areaList = new ArrayList<>();


        grade = "一年级";
        sex = "男";
        school_rank = "小学";
        title_tv.setText("创建学生");
        tv_birthday.setText(FormatUtils.getToday());
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        password = "000000";

        setTime();

        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        ll_select_city.setOnClickListener(this);
        et_school_name.setOnClickListener(this);
        ll_select_area.setOnClickListener(this);
        ll_birthday.setOnClickListener(this);
        saveBt.setOnClickListener(this);

        requestCityData();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                ActivityManage.backToNewCheckCode(context);
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.ll_select_city:
                if (cityList.size() == 0){
                    isFirstGetCity = false;
                    showLoading();
                    requestCityData();
                }else {
                    showCityPopupWindow();
                }
                break;
            case R.id.et_school_name:
//                if (cityList.size() == 0){
//                    isFirstGetCity = false;
//                    showLoading();
//                    requestCityData();
//                }else {
//                    showCityPopupWindow();
//                }
                break;
            case R.id.ll_select_area:
                if (!tv_select_city.getText().toString().equals("请选择城市")){
                    showAreaPopupWindow();
                }else {
                    AlertDialogUtil.showAlertDialog(context, "提示", "请先选择城市");
                }
                break;
            case R.id.ll_birthday:
                pvTime.show();
                break;
            case R.id.log_bt:
                //检查信息完整，并保存服务器
                saveStudentInfo();
                break;

        }
    }

    private void showCityPopupWindow(){
        View view;
        boolean isScreenTop = PopupWindowUtil.isScreenTOP(context, ll_select_city);
        if (isScreenTop){
            view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        }

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,7,2,0.5f,-1);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<CityInfoBean.TablesBean.TableBean.RowsBean>(context, cityList, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, CityInfoBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.setText(R.id.item_pop_tv,rowsBean.getShortName());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                tv_select_city.setText(cityList.get(i).getShortName());
                cityRegionID = cityList.get(i).getRegionID();
                areaList.clear();
                requestAreaData();
                popupWindow.dismiss();
            }
        });
        if (isScreenTop) {
            PopupWindowUtil.showAtLoactionBottom(ll_select_city);
        } else {
            PopupWindowUtil.showAtLoactionTop(ll_select_city);
        }

    }

    private void showAreaPopupWindow(){
        View view;
        boolean isScreenTop = PopupWindowUtil.isScreenTOP(context, ll_select_area);
        if (isScreenTop){
            view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        }

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,7,2,0.5f,-1);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<AreaInfoBean.TablesBean.TableBean.RowsBean>(context, areaList, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, AreaInfoBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.setText(R.id.item_pop_tv,rowsBean.getShortName());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_select_area.setText(areaList.get(i).getShortName());
                areaRegionID = areaList.get(i).getRegionID();
                popupWindow.dismiss();
            }
        });
        if (isScreenTop) {
            PopupWindowUtil.showAtLoactionBottom(ll_select_area);
        } else {
            PopupWindowUtil.showAtLoactionTop(ll_select_area);
        }

    }


    private void setTime(){
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String stringTime = FormatUtils.getStringTime(date, "yyyy-MM-dd");
                tv_birthday.setText(stringTime);

            }
        })

                .setLayoutRes(R.layout.pickerview_custom_time1, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true,true,true,false,false,false})
                .isDialog(true)
                .isCenterLabel(false)
                .setDividerColor(Color.RED)
                .setDate(Calendar.getInstance())
                .build();
    }

    private void saveStudentInfo(){
        student_name = et_student_name.getText().toString();
        parent_phone = et_parent_phone.getText().toString();
        birthday = tv_birthday.getText().toString();
        school_name = et_school_name.getText().toString();
        home_address = et_home_address.getText().toString();

        if (student_name.equals("")){
            AlertDialogUtil.showAlertDialog(context, "提示", "学生姓名不能为空， 带  *   为必填项，请检查");
            return;
        }
        if (parent_phone.equals("")){
            AlertDialogUtil.showAlertDialog(context, "提示", "学生账号不能为空， 带  *  为必填项，请检查");
            return;
        }
        if (parent_phone.length() < 11){
            AlertDialogUtil.showAlertDialog(context, "提示", "学生账号应为11位数字，请检查");
            return;
        }
        if (school_name.equals("")){
            AlertDialogUtil.showAlertDialog(context, "提示", "学校名称不能为空， 带  *  为必填项，请检查");
            return;
        }

        /**
         * 请求网络保存信息，成功跳转界面
         */
//        addStudentInfo();
        checkLoginName();
    }


    /**
     * RadioGroup设置改变监听器
     *
     */
    private void setRgCheckedListener(){

        rg_school_rank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setPrimaryTextColor();
                switch (i){
                    case R.id.rb_create_primary:
                        school_rank = "小学";
                        rb_create_primary.setTextColor(Color.parseColor("#ffffff"));
                        rb_create_primary_four.setVisibility(View.VISIBLE);
                        rb_create_primary_five.setVisibility(View.VISIBLE);
                        rb_create_primary_six.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_create_high:
                        school_rank = "初中";
                        rb_create_high.setTextColor(Color.parseColor("#ffffff"));
                        rb_create_primary_four.setVisibility(View.GONE);
                        rb_create_primary_five.setVisibility(View.GONE);
                        rb_create_primary_six.setVisibility(View.GONE);
                        break;
                    case R.id.rb_create_university:
                        school_rank = "高中";
                        rb_create_university.setTextColor(Color.parseColor("#ffffff"));
                        rb_create_primary_four.setVisibility(View.GONE);
                        rb_create_primary_five.setVisibility(View.GONE);
                        rb_create_primary_six.setVisibility(View.GONE);
                        break;
                }

            }
        });

        rg_grade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setGradeTextColor();
                switch (i){
                    case R.id.rb_create_primary_first:
                        grade = "一年级";
                        rb_create_primary_first.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_primary_second:
                        grade = "二年级";
                        rb_create_primary_second.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_primary_three:
                        grade = "三年级";
                        rb_create_primary_three.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_primary_four:
                        grade = "四年级";
                        rb_create_primary_four.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_primary_five:
                        grade = "五年级";
                        rb_create_primary_five.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_primary_six:
                        grade = "六年级";
                        rb_create_primary_six.setTextColor(Color.parseColor("#ffffff"));
                        break;
                }
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setSexTextColor();
                switch (i){
                    case R.id.rb_man:
                        sex = "男";
                        rb_man.setTextColor(Color.parseColor("#ffffff"));
                        break;

                    case R.id.rb_woman:
                        sex = "女";
                        rb_woman.setTextColor(Color.parseColor("#ffffff"));
                        break;

                }
            }
        });

    }

    private void setPrimaryTextColor(){
        rb_create_primary.setTextColor(Color.parseColor("#000000"));
        rb_create_high.setTextColor(Color.parseColor("#000000"));
        rb_create_university.setTextColor(Color.parseColor("#000000"));
    }

    private void setGradeTextColor(){
        rb_create_primary_first.setTextColor(Color.parseColor("#000000"));
        rb_create_primary_second.setTextColor(Color.parseColor("#000000"));
        rb_create_primary_three.setTextColor(Color.parseColor("#000000"));
        rb_create_primary_four.setTextColor(Color.parseColor("#000000"));
        rb_create_primary_five.setTextColor(Color.parseColor("#000000"));
        rb_create_primary_six.setTextColor(Color.parseColor("#000000"));
    }

    private void setSexTextColor(){
        rb_woman.setTextColor(Color.parseColor("#666666"));
        rb_man.setTextColor(Color.parseColor("#666666"));
    }

    //请求省份
    private void requestCityData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetCityMethodName;
            HashMap<String, String> map = new HashMap<>();
            map.put("jsoncode", jsonCode);
            map.put("token", MD5Result);
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
                                    if (!isFirstGetCity){
                                        AlertDialogUtil.showAlertDialog(context, "提示", "请求有误，请刷新");
                                    }
                                } else {
                                    Gson gson = new Gson();
                                    CityInfoBean cityInfoBean = gson.fromJson(jsonObject.toString(), CityInfoBean.class);
                                    if (cityInfoBean != null) {
                                        cityList.addAll(cityInfoBean.getTables().getTable().getRows());
                                        if (!isFirstGetCity){
                                            showCityPopupWindow();
                                        }
                                    } else {
                                        if (!isFirstGetCity){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "没有获取到省份信息，请重试");
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (!isFirstGetCity){
                                    AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                                }
                            }
                        } else {
                            if (!isFirstGetCity){
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            }
                        }
                    } else {
                        if (!isFirstGetCity){
                            AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                        }
                    }
                }
            });
        } else {
            if (!isFirstGetCity){
                AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
            }
        }
    }
    //请求城市
    private void requestAreaData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (cityRegionID == 0) {
                Toast.makeText(context, "省份选择异常，请重新选择！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("RegionID", cityRegionID);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAreaMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "省份选择有误，请重新选择");
                                } else {
                                    Gson gson = new Gson();
                                    AreaInfoBean areaInfoBean = gson.fromJson(jsonObject.toString(), AreaInfoBean.class);
                                    if (areaInfoBean != null) {
                                        areaList.addAll(areaInfoBean.getTables().getTable().getRows());
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "省份选择有误，请重新选择");
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

    private void checkLoginName(){


        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("LoginName", parent_phone);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.CheckLoginNameMethodName;
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
                                if (resultvalue.equals("1") || resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该账号已存在");
                                } else if (resultvalue.equals("0")){
                                    addStudentInfo();
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

    private void addStudentInfo() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }

            jsonObject.put("UserCode", userCode);
            jsonObject.put("UserName", student_name);
            jsonObject.put("LoginName", parent_phone);
            jsonObject.put("UserPWD", password);
            jsonObject.put("Sex", sex);
            jsonObject.put("CSdate", birthday);
            jsonObject.put("XueDuan", school_rank);
            jsonObject.put("ClassYear", grade);
            jsonObject.put("XueXiaoName", school_name);
            jsonObject.put("ShengFen", cityRegionID);
            jsonObject.put("ChenShi", areaRegionID);
            jsonObject.put("Address", home_address);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.AddStudentInfoMethodName;
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
                                if (resultvalue.equals("1") || resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "添加失败");
                                } else if (resultvalue.equals("0")){
                                    Toast.makeText(CreateStudentActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(context,StudentListActivity.class);
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

}
