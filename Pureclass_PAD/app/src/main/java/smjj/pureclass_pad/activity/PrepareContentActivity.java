package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.SpeachBean;
import smjj.pureclass_pad.beans.SpeachKonwBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

//备课内容界面
public class PrepareContentActivity extends BaseActivity
        implements View.OnClickListener,XListView.IXListViewListener, AdapterView.OnItemClickListener{

    private TextView title_tv;
    private XListView listView;
    private TextView onBack;
    private ImageView iv_home;
    private RadioGroup rg_season;
    private TextView tv_grade, tv_subjects;
    private RelativeLayout rl_back, rl_home, rl_add, rl_back1;

    private Context context;
    private Handler mHandler;
    private List<SpeachBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private List<SpeachKonwBean.TablesBean.TableBean.RowsBean> knowledgeList;
    private List<SpeachKonwBean.TablesBean.TableBean.RowsBean> list1;

    private String season;
    private String bookID;
    private String grade, subject;
    private String speakId;

    private String userCode;
    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String speakName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_content);
        ActivityManage.addActivity(this);
        context = this;
        mHandler =new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");

        findView();
    }


    private void findView(){
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        listView = (XListView) findViewById(R.id.scheduling_content_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        rg_season = (RadioGroup) findViewById(R.id.rg_season);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_subjects = (TextView) findViewById(R.id.tv_subject);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back1 = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);

        rg_season.check(R.id.rb_autumn);

        bookID = getIntent().getStringExtra("bookId");
        grade = getIntent().getStringExtra("grade");
        subject = getIntent().getStringExtra("subject");
        rl_back1.setVisibility(View.VISIBLE);

        tv_grade.setText(grade);
        tv_subjects.setText(subject);
        title_tv.setText("备课内容");
        season = "秋";

        listData = new ArrayList<>();
        list1 = new ArrayList<>();

        knowledgeList = new ArrayList<>();

        adapter = new CommonAdapter<SpeachBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_scheduling_content_lv) {
            @Override
            public void convert(ViewHolder holder, SpeachBean.TablesBean.TableBean.RowsBean rowsBean) {
                String str = rowsBean.getSpeakName();
                int i = listData.indexOf(rowsBean);
//                holder.getView(R.id.tv_speach).setVisibility(View.GONE);
//                holder.setText(R.id.tv_knowledge_point, str);
                if (str != null && str.length() > 3){
                    if (i >= 10){
                        holder.setText(R.id.tv_speach, str.substring(0,4));
                        holder.setText(R.id.tv_knowledge_point, str.substring(4));
                    }else {
                        holder.setText(R.id.tv_speach, str.substring(0,3));
                        holder.setText(R.id.tv_knowledge_point, str.substring(3));
                    }
                }else {
                    holder.setText(R.id.tv_speach, str);
                    holder.setText(R.id.tv_knowledge_point, "");
                }

            }
        };


        rg_season.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_spring:
                        season = "春";
                        requestSpeachData();
                        break;
                    case R.id.rb_autumn:
                        season = "秋";
                        requestSpeachData();
                        break;
                    case R.id.rb_heat:
                        season = "暑";
                        requestSpeachData();
                        break;
                    case R.id.rb_cold:
                        season = "寒";
                        requestSpeachData();
                        break;
                }

            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(true);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);

        requestSpeachData();
    }


    @Override
    public void onClick(View view) {switch (view.getId()){
        case R.id.rl_home:
            ActivityManage.backToNewCheckCode(context);
            break;
        case R.id.onBack:
            finish();
            break;
    }

    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        list1.clear();
        speakId = listData.get(i - 1).getSpeakID();
        speakName = listData.get(i - 1).getSpeakName();
        requestKnowledgeData();


    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //下拉刷新
                onLoad();
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
        listView.setRefreshTime( df.format(new Date()));
    }

    private void showPopuWindow(){

        knowledgeList.clear();
        View view = LayoutInflater.from(context).inflate(R.layout.pop_knowledge_point_view, null);

        final Map<Integer, Boolean> isCheckedMap = new HashMap<>();
        for (int i = 0; i < list1.size(); i ++){
            isCheckedMap.put(i, false);
        }

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        ListView listView1 = (ListView) view.findViewById(R.id.lv);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        TextView ensureTV = (TextView) view.findViewById(R.id.tv_ensure);
        CommonAdapter adapter = new CommonAdapter<SpeachKonwBean.TablesBean.TableBean.RowsBean>(context, list1, R.layout.item_knowledge_point_lv) {
            @Override
            public void convert(ViewHolder holder, final SpeachKonwBean.TablesBean.TableBean.RowsBean rowsBean) {
                final CheckBox checkBox = holder.getView(R.id.scheduling_gridview_checkbox);
                checkBox.setText(rowsBean.getChaptername());
                final int i = list1.indexOf(rowsBean);
                checkBox.setChecked(isCheckedMap.get(i));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkBox.isChecked()){
                            knowledgeList.add(rowsBean);
                            isCheckedMap.put(i, true);
                        }else {
                            knowledgeList.remove(rowsBean);
                            isCheckedMap.put(i, false);
                        }
                    }
                });
            }
        };
        listView1.setAdapter(adapter);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        ensureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (knowledgeList.size() == 0){
                    AlertDialogUtil.showAlertDialog(context,"提示","选择的知识点为空，请重新选择知识点");
                }else {

                    contentIDAndSpeach();
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(iv_home, Gravity.CENTER,0,0);

    }

    /**
     * 请求讲
     */
    private void requestSpeachData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (subject == null || subject.equals("")) {
                Toast.makeText(context, "该排课信息不完善，请重新查看排课列表！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (grade == null || grade.equals("")) {
                Toast.makeText(context, "获取年级异常，请重新查看排课列表！", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("subject", subject);
            jsonObject.put("Grade", grade);
            jsonObject.put("SeasonDate", season);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetSpeachMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂无数据！");
                                } else {
                                    Gson gson = new Gson();
                                    SpeachBean speachBean = gson.fromJson(jsonObject.toString(), SpeachBean.class);
                                    if (speachBean != null) {
                                        listData.clear();
                                        listData.addAll(speachBean.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无数据！");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "暂无数据！");
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

    /**
     * 请求讲下的知识点
     */
    private void requestKnowledgeData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("SpeakID", speakId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetKnowledgeMethodName;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "没有知识点数据");
                                } else {
                                    Gson gson = new Gson();
                                    SpeachKonwBean speachBean = gson.fromJson(jsonObject.toString(), SpeachKonwBean.class);
                                    if (speachBean != null) {
                                        list1.addAll(speachBean.getTables().getTable().getRows());
                                        if (list1.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无知识点数据");
                                        }else {
                                            showPopuWindow();
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "知识点获取异常，请重试");
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


    /**
     * 讲排课和讲链接起来
     */
    private void contentIDAndSpeach() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (classId == null || classId.equals("")) {
                Toast.makeText(context, "该排课信息不完善，请重新选择排课列表！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("classId", classId);
            jsonObject.put("SpeakID", speakId);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.ContentIDAndSpeach;
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
                                    Intent intent = new Intent(context, PrepareDetailsActivity.class);
                                    intent.putExtra("SpeakId", speakId);
                                    intent.putExtra("SpeakName", speakName);
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






}
