package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.SchedulingTableBean;
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
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

//备课, 上课的排课列表界面
public class PrepareClassActivity extends BaseActivity
        implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView listView;
    private TextView onBack;
    private ImageView iv_home, iv_add;
    private TextView title_tv;

    private TextView empty;

    private TextView tv_school_name;

    private TextView tv_startTime, tv_endTime;

    private RelativeLayout rl_back1, rl_home, rl_add;


    private Context context;
    private Handler mHandler;
    private List<SchedulingTableBean.TablesBean.TableBean.RowsBean> listData;
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
    //1代表备课阶段，2代表上课阶段
    private String enterMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_class);
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        enterMark = getIntent().getStringExtra("enterMark");

        findView();
    }


    private void findView() {
        listView = (XListView) findViewById(R.id.scheduling_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        empty = (TextView) findViewById(R.id.empty);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);

        title_tv.setText("教师备课");
        iv_home.setImageResource(R.drawable.search);
        rl_back1.setVisibility(View.VISIBLE);

        startTime = FormatUtils.getPastDate(7);
        endTime = FormatUtils.getFetureDate(30);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");

        if (typleUser.equals("0")){
            empty.setVisibility(View.GONE);
            tv_school_name.setText(R.string.school_name);
        }else {
            rl_add.setVisibility(View.VISIBLE);
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            tv_school_name.setText(R.string.class_name);
        }
        if(enterMark.equals("2")){
            empty.setVisibility(View.GONE);
            rl_add.setVisibility(View.GONE);
            title_tv.setText("教师上课");
        }
        setTime();
        listData = new ArrayList<>();
        studentList = new ArrayList<>();

        adapter = new CommonAdapter<SchedulingTableBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_schedu_lv1) {
            @Override
            public void convert(ViewHolder holder, final SchedulingTableBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);
                holder.setText(R.id.sd_no_tv, rowsBean.getClassId());

                holder.setText(R.id.sd_schgegin_tv, rowsBean.getClassname());
                holder.setText(R.id.sd_school_tv, rowsBean.getSchgegin());

                holder.setText(R.id.sd_class_type_tv, rowsBean.getClassType());
                holder.setText(R.id.sd_subjects_tv, rowsBean.getGname() + rowsBean.getSubjects());
                if (rowsBean.getTimeStar() != null && rowsBean.getTimeEnd() != null) {
                    holder.setText(R.id.sd_class_date_tv, rowsBean.getTimeStar().substring(0, 10));
                    holder.setText(R.id.sd_start_date_tv, rowsBean.getTimeStar().substring(11, 16)
                            + "-" + rowsBean.getTimeEnd().substring(11, 16));
                }
                String weekDay = FormatUtils.getWeekDay(FormatUtils.getCalendarFromString(rowsBean.getTimeStar().substring(0,10)));
                if (weekDay != null && !weekDay.equals("")){
                    holder.setText(R.id.week_day, weekDay);
                }
                holder.getView(R.id.iv_map).setVisibility(View.VISIBLE);
                holder.getView(R.id.iv_stu_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        grade = rowsBean.getGname();
                        classId = rowsBean.getClassId();
                        studentList.clear();
                        requestStudentData();
                    }
                });

            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);//激活加载更多
        listView.setPullRefreshEnable(true);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);
        rl_add.setOnClickListener(this);


        requestData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                showSearchPopup();
                break;
            case R.id.onBack:
                finish();
                break;
            case R.id.rl_add:
                showPopupWindow();
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
                requestData();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        classId = listData.get(i - 1).getClassId();
        spConfig.edit().putString(SPCommonInfoBean.classId, classId).commit();

        if (enterMark.equals("1")){
            String bookID = listData.get(i - 1).getBookid();
            grade = listData.get(i - 1).getGname();
            String subject = listData.get(i - 1).getSubjects();
            Intent intent = new Intent(this, PrepareContentActivity.class);
            intent.putExtra("bookId",bookID);
            intent.putExtra("grade", grade);
            intent.putExtra("subject", subject);
            startActivity(intent);
        }else if (enterMark.equals("2")){
            grade = listData.get(i - 1).getGname();
            String subject = listData.get(i - 1).getSubjects();
            Intent intent = new Intent(this, GoClassActivity.class);
            intent.putExtra("grade", grade);
            intent.putExtra("subject", subject);
            startActivity(intent);
            finish();
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
                    Intent intent = new Intent(context, CreateClassActivity.class);
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
        PopupWindowUtil.showAtLoactionRightAndBottom(iv_add);

    }

    private void showStudentPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_student_view, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.iv_close_pop);
        imageView1.setVisibility(View.VISIBLE);
        textView.setText(grade + "学生列表");
        CommonAdapter adapter = new CommonAdapter<StudentInternalBean.TablesBean.TableBean.RowsBean>(context, studentList, R.layout.item_pop_student) {
            @Override
            public void convert(ViewHolder holder, StudentInternalBean.TablesBean.TableBean.RowsBean rowsBean) {
                holder.setText(R.id.student_gridview_tv,rowsBean.getStName());
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                popupWindow.dismiss();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }

    private void showSearchPopup(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_search, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,7,3,0.5f,R.style.popWindow_animation);

        tv_startTime = (TextView) view.findViewById(R.id.tv_start_search_tiem);
        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartTime = true;
                calendar = FormatUtils.getCalendarFromString(startTime);
                pvTime.show();

            }
        });

        tv_endTime  = (TextView) view.findViewById(R.id.tv_end_search_tiem);
        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartTime = false;
                calendar = FormatUtils.getCalendarFromString(endTime);
                pvTime.show();

            }
        });

        tv_startTime.setText(startTime);
        tv_endTime.setText(endTime);
        Button searchBt = (Button) view.findViewById(R.id.search_bt);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询
                requestData();
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
                requestData();
            }
        }
    }

    private void setTime(){
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String stringTime = FormatUtils.getStringTime(date, "yyyy-MM-dd");
                if (isStartTime){
                    tv_startTime.setText(stringTime);
                    startTime = stringTime;
                }else {
                    tv_endTime.setText(stringTime);
                    endTime = stringTime;
                }
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
                .setDate(calendar)
                .build();
    }

    //请求排课列表
    public void requestInternalData() {
        String UserCode = spConfig.getString(SPCommonInfoBean.userCode, "");

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (UserCode == null || UserCode.equals("")) {
                Toast.makeText(context, "账号异常请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", UserCode);
            jsonObject.put("startTime",startTime);
            jsonObject.put("endTime",endTime);
            jsonObject.put("TypeUser",typleUser);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SchedulingMethodName;

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该老师没有排课记录");
                                } else {
                                    Gson gson = new Gson();
                                    SchedulingTableBean schedulingTableBean = gson.fromJson(jsonObject.toString(), SchedulingTableBean.class);
                                    if (schedulingTableBean != null) {
                                        listData.addAll(schedulingTableBean.getTables().getTable().getRows());
//                                        FormatUtils.getListRank(listData);
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
//                                        if (listData.size() == 0){
//                                            AlertDialogUtil.showAlertDialog(context, "提示", "该老师在当前时间段内没有排课记录");
//
//                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该老师没有排课记录");
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



    private void requestData(){
        listData.clear();
        requestInternalData();

    }


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
            String methodName = Constants.SchedulingGetStudentMethodName;


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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
                                } else {
                                    Gson gson = new Gson();
                                    StudentInternalBean studentInternalBean = gson.fromJson(jsonObject.toString(), StudentInternalBean.class);
                                    if (studentInternalBean != null) {
                                        studentList.addAll(studentInternalBean.getTables().getTable().getRows());
//                                        if (listData.size() == 0){
//                                            AlertDialogUtil.showAlertDialog(context, "提示", "该排课没有学生");
//                                        }else {
//                                            showStudentPopu();
//                                        }
                                        showStudentPopu();
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


}
