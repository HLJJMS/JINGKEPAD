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

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
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

//创建班级界面1
public class CreateClassActivity1 extends BaseActivity implements View.OnClickListener{

    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private RadioGroup rg_subject, rg_subject1;
    private RadioGroup rg_school_rank, rg_grade;
    private RadioButton rb_create_primary, rb_create_high, rb_create_university;
    private RadioButton rb_create_primary_first, rb_create_primary_second, rb_create_primary_three
            , rb_create_primary_four, rb_create_primary_five, rb_create_primary_six;
    private RadioButton rb_create_chinese, rb_create_math, rb_create_english, rb_create_physics
            , rb_create_chemistry, rb_create_biology, rb_create_history, rb_create_geography
            , rb_create_political;
    private LinearLayout ll_start_class_date, ll_type;
    private TextView tv_start_class_date, tv_type, tv_class_no;
    private EditText et_class_name;
    private RelativeLayout rl_back, rl_home, rl_add;

    private Button saveBt;

    private Context context;
    private SharedPreferences spConfig;

    private List<String> list;
    private TimePickerView pvTime;
    private String grade, shcool_rank, subjects, class_no, class_name, class_type, class_start_data;
    private boolean isGetClassNO;
    private String userCode, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class1);
        ActivityManage.addActivity(this);
        context = this;
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);

        findView();
        setRgCheckedListener();


    }



    private void findView() {
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rg_subject = (RadioGroup) findViewById(R.id.rg_subject);
        rg_subject1 = (RadioGroup) findViewById(R.id.rg_subject1);
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
        rb_create_chinese = (RadioButton) findViewById(R.id.rb_create_chinese);
        rb_create_math = (RadioButton) findViewById(R.id.rb_create_math);
        rb_create_english = (RadioButton) findViewById(R.id.rb_create_english);
        rb_create_physics = (RadioButton) findViewById(R.id.rb_create_physics);
        rb_create_chemistry = (RadioButton) findViewById(R.id.rb_create_chemistry);
        rb_create_biology = (RadioButton) findViewById(R.id.rb_create_biology);
        rb_create_history = (RadioButton) findViewById(R.id.rb_create_history);
        rb_create_geography = (RadioButton) findViewById(R.id.rb_create_geography);
        rb_create_political = (RadioButton) findViewById(R.id.rb_create_political);
        ll_start_class_date = (LinearLayout) findViewById(R.id.ll_start_class_date);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        tv_start_class_date = (TextView) findViewById(R.id.tv_start_class_date);
        tv_type = (TextView) findViewById(R.id.tv_type);
        saveBt = (Button) findViewById(R.id.log_bt);
        et_class_name = (EditText) findViewById(R.id.et_class_name);
        tv_class_no = (TextView) findViewById(R.id.tv_class_no);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);

        list = new ArrayList<>();
        list.add("一对一");
        list.add("小组课");
        list.add("班课");

        shcool_rank = "小学";
        grade = "一年级";
        subjects = "语文";
        title_tv.setText("创建班级");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        userName = spConfig.getString(SPCommonInfoBean.userName, "");
        tv_start_class_date.setText(FormatUtils.getToday());

        setTime();


        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        ll_type.setOnClickListener(this);
        ll_start_class_date.setOnClickListener(this);
        saveBt.setOnClickListener(this);

        getClasssNO();


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
            case R.id.ll_type:
                showPopupWindow();
                break;
            case R.id.ll_start_class_date:
                pvTime.show();
                break;
            case R.id.log_bt:
                //检查信息完整，并保存服务器
                saveClassInfo();
                break;
            case R.id.tv_class_no:
                if (!isGetClassNO){
                    showLoading();
                    getClasssNO();
                }
                break;

        }
    }


    private void showPopupWindow(){
        View view;
        boolean isScreenTop = PopupWindowUtil.isScreenTOP(context, ll_type);
        if (isScreenTop){
            view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.pop_view, null);
        }
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,7,3,0.5f,-1);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<String>(context, list, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.item_pop_tv,s);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_type.setText(list.get(i));
                popupWindow.dismiss();
            }
        });

        if (isScreenTop){
            PopupWindowUtil.showAtLoactionBottom(ll_type);
        }else {
            PopupWindowUtil.showAtLoactionTop(ll_type);
        }

    }


    private void setTime(){
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String stringTime = FormatUtils.getStringTime(date, "yyyy-MM-dd");
                tv_start_class_date.setText(stringTime);


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


    private void saveClassInfo(){

        class_no = tv_class_no.getText().toString();
        class_name = et_class_name.getText().toString();
        class_type = tv_type.getText().toString();
        class_start_data = tv_start_class_date.getText().toString();

        if (class_no.equals("")){
            AlertDialogUtil.showAlertDialog(context, "提示", "班课编号不能为空");
            return;
        }
        if (class_name.equals("")){
            AlertDialogUtil.showAlertDialog(context, "提示", "班课名称不能为空");
            return;
        }
        if (class_type.equals("请选择班课类型")){
            AlertDialogUtil.showAlertDialog(context, "提示", "请选择班课类型");
            return;
        }

        /**
         * 保存班级信息，成功跳转界面
         */
        addClassInfo();
    }


    /**
     * RadioGroup设置改变监听器
     *
     */
    private void setRgCheckedListener(){

        rg_subject.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setSubjectTwxtColor();
                switch (checkedId){
                    case R.id.rb_create_chinese:
                        subjects = "语文";
                        rg_subject1.clearCheck();
                        rg_subject.check(R.id.rb_create_chinese);
                        rb_create_chinese.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_math:
                        subjects = "数学";
                        rg_subject1.clearCheck();
                        rg_subject.check(R.id.rb_create_math);
                        rb_create_math.setTextColor(Color.parseColor("#ffffff"));

                        break;
                    case R.id.rb_create_english:
                        subjects = "英语";
                        rg_subject1.clearCheck();
                        rg_subject.check(R.id.rb_create_english);
                        rb_create_english.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_physics:
                        subjects = "物理";
                        rg_subject1.clearCheck();
                        rg_subject.check(R.id.rb_create_physics);
                        rb_create_physics.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_chemistry:
                        subjects = "化学";
                        rg_subject1.clearCheck();
                        rg_subject.check(R.id.rb_create_chemistry);
                        rb_create_chemistry.setTextColor(Color.parseColor("#ffffff"));

                        break;
                    case R.id.rb_create_biology:
                        subjects = "生物";
                        rg_subject1.clearCheck();
                        rg_subject.check(R.id.rb_create_biology);
                        rb_create_biology.setTextColor(Color.parseColor("#ffffff"));

                        break;
                }

            }
        });

        rg_subject1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setSubjectTwxtColor();
                switch (checkedId){
                    case R.id.rb_create_history:
                        subjects = "历史";
                        rg_subject.clearCheck();
                        rg_subject1.check(R.id.rb_create_history);
                        rb_create_history.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.rb_create_geography:
                        subjects = "地理";
                        rg_subject.clearCheck();
                        rg_subject1.check(R.id.rb_create_geography);
                        rb_create_geography.setTextColor(Color.parseColor("#ffffff"));

                        break;
                    case R.id.rb_create_political:
                        subjects = "政治";
                        rg_subject.clearCheck();
                        rg_subject1.check(R.id.rb_create_political);
                        rb_create_political.setTextColor(Color.parseColor("#ffffff"));
                        break;
                }

            }
        });

        rg_school_rank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setPrimaryTextColor();
                switch (i){
                    case R.id.rb_create_primary:
                        shcool_rank = "小学";
                        rb_create_primary.setTextColor(Color.parseColor("#ffffff"));
                        rb_create_primary_four.setVisibility(View.VISIBLE);
                        rb_create_primary_five.setVisibility(View.VISIBLE);
                        rb_create_primary_six.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_create_high:
                        shcool_rank = "初中";
                        rb_create_high.setTextColor(Color.parseColor("#ffffff"));
                        rb_create_primary_four.setVisibility(View.INVISIBLE);
                        rb_create_primary_five.setVisibility(View.INVISIBLE);
                        rb_create_primary_six.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_create_university:
                        shcool_rank = "高中";
                        rb_create_university.setTextColor(Color.parseColor("#ffffff"));
                        rb_create_primary_four.setVisibility(View.INVISIBLE);
                        rb_create_primary_five.setVisibility(View.INVISIBLE);
                        rb_create_primary_six.setVisibility(View.INVISIBLE);
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

    private void setSubjectTwxtColor(){
        rb_create_chinese.setTextColor(Color.parseColor("#000000"));
        rb_create_math.setTextColor(Color.parseColor("#000000"));
        rb_create_english.setTextColor(Color.parseColor("#000000"));
        rb_create_physics.setTextColor(Color.parseColor("#000000"));
        rb_create_chemistry.setTextColor(Color.parseColor("#000000"));
        rb_create_biology.setTextColor(Color.parseColor("#000000"));
        rb_create_history.setTextColor(Color.parseColor("#000000"));
        rb_create_geography.setTextColor(Color.parseColor("#000000"));
        rb_create_political.setTextColor(Color.parseColor("#000000"));
    }

    private void getClasssNO(){
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
//            if (userCode == null || userCode.equals("")){
//                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
//                return;
//            }
//            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetClassNoMethodName;
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
                                    isGetClassNO = false;
                                    AlertDialogUtil.showAlertDialog(context, "提示", "获取排课编号失败，请重新获取");
                                } else if (resultvalue.equals("0")){
                                    String classNO = jsonObject.getString("classid");
                                    if (classNO != null) {
                                        tv_class_no.setText(classNO);
                                        isGetClassNO = true;
                                    } else {
                                        isGetClassNO = false;
                                        AlertDialogUtil.showAlertDialog(context, "提示", "获取排课编号失败，请重新获取");
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

    //添加班级
    private void addClassInfo() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
                return;
            }

            grade = shcool_rank.substring(0,1) + grade.substring(0,1);
            jsonObject.put("UserCode", userCode);
            jsonObject.put("UserName", userName);
            jsonObject.put("F0006", shcool_rank);
            jsonObject.put("F0007", grade);
            jsonObject.put("F0008", subjects);
            jsonObject.put("F0001", class_no);
            jsonObject.put("F0009", class_name);
            jsonObject.put("F0010", class_type);
            jsonObject.put("F0011", class_start_data);
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.AddClassInfoMethodName;
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
                                    Toast.makeText(CreateClassActivity1.this, "添加成功", Toast.LENGTH_SHORT).show();
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

}
