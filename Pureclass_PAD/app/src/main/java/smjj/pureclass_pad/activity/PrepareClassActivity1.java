package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.ClassBeanT;
import smjj.pureclass_pad.beans.CourseBean;
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
import smjj.pureclass_pad.view.HorizontalListView;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

//二期备课列表页
public class PrepareClassActivity1 extends BaseActivity
        implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private GridView gridView;
    private TextView onBack;
    private ImageView iv_home, iv_add;
    private TextView title_tv;
    private HorizontalListView horizontalListView;
    private CommonAdapter hAdapter;
    private List<String> hListData;

    private TextView empty;

    private TextView tv_startTime, tv_endTime;

    private RelativeLayout rl_back1, rl_home, rl_add;

    private Context context;
    private Handler mHandler;
    private List<ClassBeanT.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private List<ClassBeanT.TablesBean.TableBean.RowsBean> studentList;

    private List<CourseBean.TablesBean.TableBean.RowsBean> dataGramList;

    private SharedPreferences spConfig;

    private String typleUser;//用户类型 0代表内部教师 1代表外部教师
    private String userCode;
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;

    private String startTime, endTime;
    //1代表备课阶段，2代表上课阶段
    private String enterMark;
    //选择类型 一对一 班课
    private String selectType;

    private String className, schoolName, materialName, materialNo, stuName, classNo, grade, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_class1);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        enterMark = getIntent().getStringExtra("enterMark");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");
        isCertificate = spConfig.getString(SPCommonInfoBean.isCertificate,"");

        findView();

    }

    private void findView() {
        gridView = (GridView) findViewById(R.id.scheduling_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        empty = (TextView) findViewById(R.id.empty);
        horizontalListView = (HorizontalListView) findViewById(R.id.h_course_lv);


        //设置标题加粗
        TextPaint tp = title_tv.getPaint();
        tp.setFakeBoldText(true);
        tp.setTextSize(30);
        title_tv.setText("教师备课");
        iv_home.setImageResource(R.drawable.add);
        rl_back1.setVisibility(View.VISIBLE);

        startTime = FormatUtils.getPastDate(7);
        endTime = FormatUtils.getFetureDate(30);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");

        if (typleUser.equals("0")){
            empty.setVisibility(View.GONE);
            rl_home.setVisibility(View.GONE);
        }else {
//            rl_home.setVisibility(View.VISIBLE);
            rl_home.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
//            gridView.setEmptyView(empty);
        }
        if(enterMark.equals("2")){
            empty.setVisibility(View.GONE);
            rl_add.setVisibility(View.GONE);
            rl_home.setVisibility(View.GONE);
            title_tv.setText("教师上课");
        }

        hListData = new ArrayList<>();
        dataGramList = new ArrayList<>();
        hListData.add("班课");
        hListData.add("一对一");
        selectType = "班课";

        hAdapter = new CommonAdapter<String>(context, hListData, R.layout.item_h_lv) {
            @Override
            public void convert(ViewHolder holder, String str) {
                int i = hListData.indexOf(str);
                TextView textView = holder.getView(R.id.tv_h_lv);
                textView.setText(str);
                if (selectType.equals(str)){
                    textView.setBackgroundResource(R.drawable.shape_yellow_type_bg);
//                    textView.setBackgroundResource(R.drawable.shape_green_bg);
                    textView.setTextColor(Color.parseColor("#000000"));
                }else {
                    textView.setBackgroundResource(R.drawable.shape_type_bg);
//                    textView.setBackgroundResource(R.drawable.shape_greey_bg);
                    textView.setTextColor(Color.parseColor("#666666"));
                }
            }
        };

        listData = new ArrayList<>();

        studentList = new ArrayList<>();

        adapter = new CommonAdapter<ClassBeanT.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_schedu_gv) {
            @Override
            public void convert(ViewHolder holder, final ClassBeanT.TablesBean.TableBean.RowsBean rowsBean) {

                holder.setText(R.id.sd_no_tv, rowsBean.getClassName());
                holder.setText(R.id.tv_courser_name, rowsBean.getMaterialName());
//                holder.setText(R.id.tv_stu_name, "人数: " + rowsBean.getStnames() + "");

                String isDualTeach = rowsBean.getTchType();
                if (isDualTeach != null && isDualTeach.equals("双师")){
                    //双师模式
                    holder.setText(R.id.tv_stu_name, "人数: " + rowsBean.getStnames() + "");
                    holder.getView(R.id.ll_lv_stu_name).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_dual_teach).setVisibility(View.VISIBLE);
                    if (isCertificate.equals("0")){
                        holder.getView(R.id.ll_school_tv).setVisibility(View.GONE);
                    }else {
                        holder.getView(R.id.ll_school_tv).setVisibility(View.VISIBLE);
                        holder.setText(R.id.sd_school_tv, rowsBean.getTeacherName());
                    }

                }else {
                    holder.setText(R.id.tv_stu_name,rowsBean.getStnames() + "");
                    holder.setText(R.id.sd_school_tv, rowsBean.getSchgegin());
                    holder.getView(R.id.ll_school_tv).setVisibility(View.VISIBLE);
                    holder.getView(R.id.iv_dual_teach).setVisibility(View.GONE);
                    if (selectType.equals("一对一")){
                        holder.getView(R.id.ll_lv_stu_name).setVisibility(View.GONE);
                    }else {
                        holder.getView(R.id.ll_lv_stu_name).setVisibility(View.VISIBLE);
                    }
                }

            }
        };

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        horizontalListView.setAdapter(hAdapter);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_add.setOnClickListener(this);


        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!selectType.equals(hListData.get(i))){
                    selectType = hListData.get(i);
                    listData.clear();
                    getClassData();
                    hAdapter.notifyDataSetChanged();
                }
            }
        });

        getClassData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                showPopupWindow();
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.rl_add:

                break;
        }
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //下拉刷新
                onLoad();
                listData.clear();
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

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        className = listData.get(i).getClassName();
        schoolName = listData.get(i).getSchgegin();
        materialName = listData.get(i).getMaterialName();
        materialNo = listData.get(i).getMaterialNo();
        stuName = listData.get(i).getStnames();
        classNo = listData.get(i).getClassNo();
        grade = listData.get(i).getGradetype();
        subject = listData.get(i).getSubjects();
        String isDualTeach1 = listData.get(i).getTchType();

        if (isDualTeach1 != null && isDualTeach1.equals("双师")){
            spConfig.edit().putString(SPCommonInfoBean.isDualTeacher, "0").commit();
        }else {
            spConfig.edit().putString(SPCommonInfoBean.isDualTeacher, "1").commit();
        }

        if (enterMark.equals("1")){


            if (materialNo == null || materialNo.equals("")){
                if (isDualTeach1 == null || !isDualTeach1.equals("双师")){
                    getDataGram();
                    return;
                }
            }
            Intent intent = new Intent(this, PrepareContentActivity1.class);
            intent.putExtra("enterMark", enterMark);
            intent.putExtra("SelectType", selectType);
            intent.putExtra("ClassName", className);
            intent.putExtra("SchoolName", schoolName);
            intent.putExtra("MaterialName", materialName);
            intent.putExtra("MaterialNo", materialNo);
            intent.putExtra("Grade", grade);
            intent.putExtra("Subject", subject);
            intent.putExtra("StuName", stuName);
            intent.putExtra("ClassNo", classNo);
            startActivity(intent);
            finish();
        }else if (enterMark.equals("2")){
            Intent intent = new Intent(this, PrepareContentActivity1.class);
            intent.putExtra("enterMark", enterMark);
            intent.putExtra("SelectType", selectType);
            intent.putExtra("ClassName", className);
            intent.putExtra("SchoolName", schoolName);
            intent.putExtra("MaterialName", materialName);
            intent.putExtra("MaterialNo", materialNo);
            intent.putExtra("Grade", grade);
            intent.putExtra("Subject", subject);
            intent.putExtra("StuName", stuName);
            intent.putExtra("ClassNo", classNo);
            startActivity(intent);

        }

    }

    private void showPopupWindow(){
        List<String> addList = new ArrayList<>();
        addList.add("添加班级");
        addList.add("添加学生");
        addList.add("添加排课");

        View view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow1(context,view,7,3,0.5f,R.style.pop_up_down);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<String>(context, addList, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, String s) {
                TextView tv = holder.getView(R.id.item_pop_tv);
                tv.setText(s);
                tv.setTextColor(Color.parseColor("#00852f"));
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    Intent intent = new Intent(context, CreateClassActivity1.class);
                    startActivity(intent);
                }else if (i == 1){
                    Intent intent1 = new Intent(context, CreateStudentActivity.class);
                    startActivity(intent1);
                }else if (i == 2){
                    Intent intent2 = new Intent(context, CreateSchedulingActivity.class);
                    startActivityForResult(intent2,0);

                }
                popupWindow.dismiss();
            }
        });
        PopupWindowUtil.showAtLoactionRightAndBottom(iv_home);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0){
            if (resultCode == 1){
//                requestData();
            }
        }
    }


    //展示教材信息
    private void showCouresPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_select_gram, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        final TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        ListView lv = (ListView) view.findViewById(R.id.pop_lv);
        textView.setText("请选择教材");
        final CommonAdapter adapter1 = new CommonAdapter<CourseBean.TablesBean.TableBean.RowsBean>(context, dataGramList, R.layout.item_pop_select_gram) {
            @Override
            public void convert(ViewHolder holder, final CourseBean.TablesBean.TableBean.RowsBean rowsBean) {
                ImageView imageView = holder.getView(R.id.iv_data_gram);
                imageView.setVisibility(View.GONE);
                TextView textView1 = holder.getView(R.id.tv_data_gram_name);
                textView1.setText(rowsBean.getF0002());

                holder.getView(R.id.ll_pop_data_gram).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialName = rowsBean.getF0002();
                        materialNo = rowsBean.getF0001();
                        sureCourseClass();

                        popupWindow.dismiss();
                    }
                });

            }
        };

        lv.setAdapter(adapter1);

        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }

    //二期确认教材
    private void sureCourseClass() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("MaterialNo", materialNo);
            jsonObject.put("classNo", classNo);
            jsonObject.put("MaterialName", materialName);
            jsonObject.put("classType", selectType);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SureCourseMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "选择教材失败，请重新选择！");
                                } else {
                                    Intent intent = new Intent(context, PrepareContentActivity1.class);
                                    intent.putExtra("ClassName", className);
                                    intent.putExtra("enterMark", enterMark);
                                    intent.putExtra("SchoolName", schoolName);
                                    intent.putExtra("MaterialName", materialName);
                                    intent.putExtra("MaterialNo", materialNo);
                                    intent.putExtra("ClassNo", classNo);
                                    intent.putExtra("StuName", stuName);
                                    intent.putExtra("SelectType", selectType);
                                    intent.putExtra("Grade", grade);
                                    intent.putExtra("Subject", subject);
                                    intent.putExtra("StartTime", startTime);
                                    startActivity(intent);
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

    //获取教材
    public void getDataGram() {

        dataGramList.clear();

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gradetype", grade);
            jsonObject.put("subjects", subject);
//            jsonObject.put("gradetype", "初一");
//            jsonObject.put("subjects", "数学");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetCourseMN2;


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
                                    AlertDialogUtil.showAlertDialog(context, "提示","暂无教材信息");
                                } else {
                                    Gson gson = new Gson();
                                    CourseBean courseBean = gson.fromJson(jsonObject.toString(), CourseBean.class);
                                    if (courseBean != null) {
                                        //创建新建和基础包选项
                                        dataGramList.addAll(courseBean.getTables().getTable().getRows());
                                        showCouresPopu();
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

    //获取班级信息
    public void getClassData() {
        String userCode = spConfig.getString(SPCommonInfoBean.userCode,"");

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("classType", selectType);
            if (isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = "";
            if (enterMark.equals("1")){
                //备课
                methodName = Constants.GetClassMN2;
            }else {
                //上课
                methodName = Constants.GetGoClassMN2;
            }

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
                                    listData.clear();
                                    adapter.setData(listData);
                                    adapter.notifyDataSetChanged();
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该老师在固定时间内暂无可以用的班级");
                                } else {
                                    Gson gson = new Gson();
                                    ClassBeanT classBeanT = gson.fromJson(jsonObject.toString(), ClassBeanT.class);
                                    if (classBeanT != null) {
                                        listData.addAll(classBeanT.getTables().getTable().getRows());
//                                        FormatUtils.getListRank(listData);
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该老师在固定时间内暂无可以用的班级");

                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该老师在固定时间内暂无可以用的班级");
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

}

