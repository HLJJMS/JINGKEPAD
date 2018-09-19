package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.ClassBeanT;
import smjj.pureclass_pad.beans.StudentInternalBean;
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
import smjj.pureclass_pad.view.HorizontalListView;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static smjj.pureclass_pad.common.SPCommonInfoBean.isDualTeacher;

//课后选择班级列表
public class PrepareCAActivity extends BaseActivity
        implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private GridView gridView;
    private TextView onBack;
    private ImageView iv_home, iv_add;
    private TextView title_tv;
    private HorizontalListView horizontalListView;
    private CommonAdapter hAdapter;
    private List<String> hListData;

    private TextView empty;

    private TextView tv_school_name;

    private TextView tv_startTime, tv_endTime;

    private RelativeLayout rl_back1, rl_home, rl_add;

    private Context context;
    private Handler mHandler;
    private List<ClassBeanT.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private List<StudentInternalBean.TablesBean.TableBean.RowsBean> studentList;

    private SharedPreferences spConfig;

    private TimePickerView pvTime;
    private Calendar calendar;

    private boolean isStartTime;
    private String typleUser;
    private String grade;
    private String classId;
    private String startTime, endTime;
    private String selectType;
    private String isCertificate;//教师身份 0代表主讲 1代表助教 2代表既不是主讲也不是助教
    private String schoolNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_ca);

        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        classId = spConfig.getString(SPCommonInfoBean.classId, "");
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

        iv_home.setImageResource(R.drawable.add);
        rl_back1.setVisibility(View.VISIBLE);

        startTime = FormatUtils.getPastDate(7);
        endTime = FormatUtils.getFetureDate(30);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser, "");

        //设置标题加粗
        TextPaint tp = title_tv.getPaint();
        tp.setFakeBoldText(true);
        tp.setTextSize(30);
        empty.setVisibility(View.GONE);
        rl_add.setVisibility(View.GONE);
        rl_home.setVisibility(View.GONE);
        title_tv.setText("教师课后");

        hListData = new ArrayList<>();
        hListData.add("班课");
        hListData.add("一对一");

        selectType = "班课";

        hAdapter = new CommonAdapter<String>(context, hListData, R.layout.item_h_lv) {
            @Override
            public void convert(ViewHolder holder, String str) {
                int i = hListData.indexOf(str);
                TextView textView = holder.getView(R.id.tv_h_lv);
                textView.setText(str);
                if (selectType.equals(str)) {
                    textView.setBackgroundResource(R.drawable.shape_yellow_type_bg);
                    textView.setTextColor(Color.parseColor("#000000"));
                } else {
                    textView.setBackgroundResource(R.drawable.shape_type_bg);
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

        String className = listData.get(i).getClassName();
        String schoolName = listData.get(i).getSchgegin();
        String materialName = listData.get(i).getMaterialName();
        String materialNo = listData.get(i).getMaterialNo();
        String stuName = listData.get(i).getStnames();
        String classNo = listData.get(i).getClassNo();
        String grade = listData.get(i).getGradetype();
        String subject = listData.get(i).getSubjects();
        String isDualTeach1 = listData.get(i).getTchType();

        if (isDualTeach1 != null && isDualTeach1.equals("双师")){
            spConfig.edit().putString(SPCommonInfoBean.isDualTeacher, "0").commit();
        }else {
            spConfig.edit().putString(SPCommonInfoBean.isDualTeacher, "1").commit();
        }

        Intent intent = new Intent(this, ClassAfterActivity1.class);
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


    //获取班级信息
    public void getClassData() {
        String userCode = spConfig.getString(SPCommonInfoBean.userCode, "");

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("classType", selectType);
//            if (isDualTeacher.equals("0")){
//                jsonObject.put("IsCertificate", isCertificate);
//            }
            if (isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;
            if (isDualTeacher.equals("0")){
                methodName = Constants.GetClassCAFMN2;
            }else {
                methodName = Constants.GetClassAfterMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该老师在固定时间内暂无排课的班级");
                                } else {
                                    Gson gson = new Gson();
                                    ClassBeanT classBeanT = gson.fromJson(jsonObject.toString(), ClassBeanT.class);
                                    if (classBeanT != null) {
                                        listData.addAll(classBeanT.getTables().getTable().getRows());
//                                        FormatUtils.getListRank(listData);
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该老师在固定时间内暂无排课的班级");

                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该老师在固定时间内暂无排课的班级");
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


    private void requestData() {
        listData.clear();
//        requestInternalData();

    }


}
