package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.LessonsBskActivity;
import smjj.pureclass_pad.activity.PersonalLessonsActivity;
import smjj.pureclass_pad.adapter.PersonalLessonAdapter;
import smjj.pureclass_pad.beans.A001002Bean;
import smjj.pureclass_pad.beans.InductionTestExercisesBean;
import smjj.pureclass_pad.beans.TestBasketBean;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.eventbus.EventBusFrist;
import smjj.pureclass_pad.eventbus.EventBusTwo;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/11/14.
 */
//个性化备课碎片
public class PersonalLessonFragment extends Fragment
        implements View.OnClickListener, XListView.IXListViewListener {
    private TextView tv_amonut, tv_totals;
    private LinearLayout ll_exercises_cat;
    private PersonalLessonAdapter recyclerAdapter = new PersonalLessonAdapter();
    private XListView listView;
    private TextView empty;
    private List<A001002Bean.TablesBean.TableBean.RowsBean> beanCopy = new ArrayList<A001002Bean.TablesBean.TableBean.RowsBean>();
    private Context context;
    private Handler mHandler;
    private PersonalLessonsActivity activity;

    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private RecyclerView recyclerView;
    private RadioGroup rg_grade;
//    private RadioButton rb_create_primary_first, rb_create_primary_second, rb_create_primary_three
//            , rb_create_primary_four;

    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> isVisibleList;
    private List<String> isAddBskList;

    private String exercisesSign;


    private String grade, subject, parentID, knowledgeID;
    private int pageNum = 1,index=0;
    private String mistakes_info,menuNumber;
    private int contentID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (PersonalLessonsActivity) getActivity();
        mHandler = new Handler();
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser, "");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        classId = spConfig.getString(SPCommonInfoBean.classId, "");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_personal_lesson, container, false);

        findView(contentView);

        return contentView;
    }

    private void findView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        tv_amonut = (TextView) view.findViewById(R.id.tv_amonut);
        tv_totals = (TextView) view.findViewById(R.id.tv_totals);
        ll_exercises_cat = (LinearLayout) view.findViewById(R.id.ll_exercises_cat);
        empty = (TextView) view.findViewById(R.id.empty);
        rg_grade = (RadioGroup) view.findViewById(R.id.rg_grade);
//        rb_create_primary_first = (RadioButton) view.findViewById(R.id.rb_create_primary_first);
//        rb_create_primary_second = (RadioButton) view.findViewById(R.id.rb_create_primary_second);
//        rb_create_primary_three = (RadioButton) view.findViewById(R.id.rb_create_primary_three);
//        rb_create_primary_four = (RadioButton) view.findViewById(R.id.rb_create_primary_four);

        listData = new ArrayList<>();
        exercisesSign = "1";

        isVisibleList = new ArrayList<>();
        isAddBskList = new ArrayList<>();

        listView.setEmptyView(empty);

        ininAdapter();


        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(true);
        ll_exercises_cat.setOnClickListener(this);

        setRgCheckedListener();
        recyclerView.setAdapter(recyclerAdapter);
        recycler();
    }

    private void ininAdapter() {

        adapter = new CommonAdapter<InductionTestExercisesBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_exercises_webview1) {
            @Override
            public void convert(ViewHolder holder, final InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {

                final int i = listData.indexOf(rowsBean);

                holder.setText(R.id.tv_item_no, (i + 1) + ".");

                holder.getView(R.id.tv_item_no).setVisibility(View.VISIBLE);
                holder.getView(R.id.tv_stu_answer).setVisibility(View.GONE);

                initWebView(holder, rowsBean);

                final LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
                final LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
                final LinearLayout ll_answer7 = holder.getView(R.id.ll_exercises_answer7);
                LinearLayout ll_add_shop_cat = holder.getView(R.id.ll_add_shop_cat);
                LinearLayout ll_drop_down = holder.getView(R.id.ll_drop_down);
                LinearLayout ll_find_fault = holder.getView(R.id.ll_find_fault);
                final ImageView drop_dowmn = holder.getView(R.id.iv_drop_down);
                final ImageView iv_add_item_bank = holder.getView(R.id.iv_add_item_bank);


                if (isVisibleList.contains(rowsBean)) {
                    ll_answer5.setVisibility(View.VISIBLE);
                    ll_answer6.setVisibility(View.VISIBLE);
                    ll_answer7.setVisibility(View.VISIBLE);
                    drop_dowmn.setImageResource(R.drawable.pull_up);
                } else {
                    ll_answer5.setVisibility(View.GONE);
                    ll_answer6.setVisibility(View.GONE);
                    ll_answer7.setVisibility(View.GONE);
                    drop_dowmn.setImageResource(R.drawable.drop_down);
                }

                if (isAddBskList.contains(rowsBean.getID())) {
                    iv_add_item_bank.setImageResource(R.drawable.add2);
                } else {
                    iv_add_item_bank.setImageResource(R.drawable.add1);
                }

                ll_drop_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isVisibleList.contains(rowsBean)) {
                            ll_answer5.setVisibility(View.GONE);
                            ll_answer6.setVisibility(View.GONE);
                            ll_answer7.setVisibility(View.GONE);
                            drop_dowmn.setImageResource(R.drawable.drop_down);
                            isVisibleList.remove(rowsBean);
                        } else {
                            ll_answer5.setVisibility(View.VISIBLE);
                            ll_answer6.setVisibility(View.VISIBLE);
                            ll_answer7.setVisibility(View.VISIBLE);
                            drop_dowmn.setImageResource(R.drawable.pull_up);
                            isVisibleList.add(rowsBean);
                        }
                    }
                });

                ll_add_shop_cat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAddBskList.contains(rowsBean.getID())) {
                            //请求加入试题篮
                            addTestToBsk(rowsBean.getID());
                            tv_amonut.setText(isAddBskList.size() + "");
                        }
                    }
                });

                ll_find_fault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //挑错
                        contentID = rowsBean.getContentID();
                        showMistakesPopu();
                    }
                });

            }
        };
    }

    private void initWebView(ViewHolder holder, InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {
        WebView webView1 = holder.getView(R.id.wb_exercises1);
        WebView webView2 = holder.getView(R.id.wb_exercises2);
        WebView webView3 = holder.getView(R.id.wb_exercises3);
        WebView webView4 = holder.getView(R.id.wb_exercises4);
        webView1.loadUrl("about:blank");
        webView2.loadUrl("about:blank");
        webView3.loadUrl("about:blank");
        webView4.loadUrl("about:blank");

        if (Build.VERSION.SDK_INT >= 19) {
            webView1.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView2.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView3.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView4.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView1.setWebViewClient(new GameWebViewClient());
        WebSettings s = webView1.getSettings();
        s.setJavaScriptEnabled(true);

        webView2.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView2.setWebViewClient(new GameWebViewClient());
        WebSettings s1 = webView2.getSettings();
        s1.setJavaScriptEnabled(true);
        webView3.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView3.setWebViewClient(new GameWebViewClient());
        WebSettings s2 = webView3.getSettings();
        s2.setJavaScriptEnabled(true);
        webView4.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                activity.setTitle("Loading...");
                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView4.setWebViewClient(new GameWebViewClient());
        WebSettings s3 = webView4.getSettings();
        s3.setJavaScriptEnabled(true);

        String stem = rowsBean.getStem();
        if (stem != null && !stem.equals("")) {
//            String str1 = CommonWay.jsoup(stem + choses);
            String str1 = CommonWay.jsoup3(stem);
            webView1.loadData(str1, "text/html; charset=UTF-8", null);
        } else {
            webView1.setVisibility(View.GONE);
        }

        String choses = rowsBean.getChoses();
        if (choses != null && !choses.equals("")) {
//            String str1 = CommonWay.jsoup(stem + choses);
            String str4 = CommonWay.jsoup4(choses);
            webView4.loadData(str4, "text/html; charset=UTF-8", null);
        } else {
            webView4.setVisibility(View.GONE);
        }


        String correct = rowsBean.getCorrect();
        if (correct != null && !correct.equals("")) {
//            String str2 = CommonWay.jsoup1(correct);
            String str2 = CommonWay.getHtmlData1(correct);
            webView2.loadData(str2, "text/html; charset=UTF-8", null);
        } else {
            webView2.setVisibility(View.GONE);
        }
        String analysis = rowsBean.getAnalysis();
        if (analysis != null && !analysis.equals("")) {
//            String str3 = CommonWay.jsoup2(analysis);
            String str3 = CommonWay.getHtmlData1(analysis);
            webView3.loadData(str3, "text/html; charset=UTF-8", null);
        } else {
            webView3.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_exercises_cat:
                if (isAddBskList.size() != 0) {
                    Intent intent = new Intent(context, LessonsBskActivity.class);
                    intent.putExtra("GradeName", grade);
                    intent.putExtra("SelectDataGram", "2");
                    intent.putExtra("SpeakId", activity.speakId);
                    intent.putExtra("SpeakName", activity.speakName);
                    intent.putExtra("MaterialName", activity.materialName);
                    intent.putExtra("MaterialNo", activity.materialNo);
                    intent.putExtra("ClassNo", activity.classNo);
                    intent.putExtra("StartTime", activity.startTime);
                    intent.putExtra("ClassName", activity.className);
                    startActivity(intent);
                } else {
                    AlertDialogUtil.showAlertDialog(context, "提示", "试题篮为空，请先加入试题篮");
                }
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
                pageNum = 1;
                requestTest();
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
                pageNum++;
//                requestTest();
//                adapter.setData(listData);
//                adapter.notifyDataSetChanged();

            }
        }, 500);
    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        listView.setRefreshTime(df.format(new Date()));
    }

    private void showMistakesPopu() {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_mistakes_view, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context, view, 2, 2, 0.5f, -1);

        TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        textView.setText("挑错");
        final EditText et_mistakes_info = (EditText) view.findViewById(R.id.et_mistakes_info);
        ImageView iv_close_pop = (ImageView) view.findViewById(R.id.iv_close_pop);
        Button saveBt = (Button) view.findViewById(R.id.log_bt);

        iv_close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mistakes_info = et_mistakes_info.getText().toString();
                saveMistakesInfo();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(ll_exercises_cat, Gravity.CENTER, 0, 0);

    }


    public void refreshData(String gradeName, String subject1, String knowledgeID1) {
        //刷新题目
        listData.clear();
        grade = gradeName;
        subject = subject1;
        knowledgeID = knowledgeID1;
        listView.setPullLoadEnable(true);//激活加载更多
        listView.setPullRefreshEnable(true);
        requestBskList();
        onRefresh();

    }

    //请求知识点试题
    private void requestTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("KnowledgeUID", knowledgeID);
            jsonObject.put("pageindex", pageNum + "");
            jsonObject.put("UserCode", userCode);
//            jsonObject.put("GradeName", "初一");
//            jsonObject.put("Subject", "语文");
            jsonObject.put("GradeName", grade);
            jsonObject.put("Subject", subject);
            jsonObject.put("LessonID", classId);
            jsonObject.put("TestType", "3");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.GetExercisesMethodName;

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "当前知识点无试题");
                                } else {
                                    Gson gson = new Gson();
                                    InductionTestExercisesBean inductionTestExercisesBean = gson.fromJson(jsonObject.toString(), InductionTestExercisesBean.class);
                                    if (inductionTestExercisesBean != null) {
                                        if (inductionTestExercisesBean.getTables().getTable().getRows().size() != 0) {
                                            listData.addAll(inductionTestExercisesBean.getTables().getTable().getRows());
                                            adapter.setData(listData);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            if (pageNum == 1) {
                                                AlertDialogUtil.showAlertDialog(context, "提示", "当前知识点无试题");
                                            } else {
                                                AlertDialogUtil.showAlertDialog(context, "提示", "当前知识点无更多试题了");
                                            }
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "返回试题有误");
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


    //请求试题篮试题列表
    public void requestBskList() {

        isAddBskList.clear();

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "排课编号为空，请重新选择！");
                return;
            }
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录！");
                return;
            }
            if (grade == null || grade.equals("")) {
                return;
            }
            jsonObject.put("teacherNo", userCode);
            jsonObject.put("SpeakID", activity.speakId);
//            jsonObject.put("GradeName", grade);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetBskExeMN2;
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
                                if (resultvalue.equals("1")) {
                                    //根据知识点请求题目 并关闭菜单
                                    tv_amonut.setText(isAddBskList.size() + "");
                                    adapter.setData(listData);
                                    adapter.notifyDataSetChanged();
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "查询试题篮效验失败");
                                } else {
                                    //试题篮中有试题
                                    Gson gson = new Gson();
                                    TestBasketBean testBasketBean = gson.fromJson(jsonObject.toString(), TestBasketBean.class);
                                    if (testBasketBean != null) {
                                        int lenth = testBasketBean.getTables().getTable().getRows().size();
                                        for (int i = 0; i < lenth; i++) {
                                            String QId = testBasketBean.getTables().getTable().getRows().get(i).getID();
                                            if (QId != null) {
                                                isAddBskList.add(QId);
                                            }
                                        }
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        tv_amonut.setText(isAddBskList.size() + "");

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

    //加入试题篮
    private void addTestToBsk(final String QId) {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录！");
                return;
            }

            jsonObject.put("teacherNo", userCode);
            jsonObject.put("TitleType", menuNumber);
            jsonObject.put("SpeakID", activity.speakId);
            jsonObject.put("QID", QId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.PersonalAddBskMN2;
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
                                if (resultvalue.equals("1")) {
                                    //根据知识点请求题目 并关闭菜单
                                    if (exercisesSign.equals("1")) {
                                        Toast.makeText(context, "入门测加入试题篮失败", Toast.LENGTH_SHORT).show();
                                    } else if (exercisesSign.equals("2")) {
                                        Toast.makeText(context, "出门考加入试题篮失败", Toast.LENGTH_SHORT).show();
                                    } else if (exercisesSign.equals("4")) {
                                        Toast.makeText(context, "深化应用加入试题篮失败", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "巩固练习加入试题篮失败", Toast.LENGTH_SHORT).show();
                                    }

                                    adapter.notifyDataSetChanged();

                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                } else {
                                    isAddBskList.add(QId);
                                    tv_amonut.setText(isAddBskList.size() + "");
                                    adapter.notifyDataSetChanged();
                                    if (exercisesSign.equals("1")) {
                                        Toast.makeText(context, "入门测添加成功", Toast.LENGTH_SHORT).show();
                                    } else if (exercisesSign.equals("2")) {
                                        Toast.makeText(context, "出门考添加成功", Toast.LENGTH_SHORT).show();

                                    } else if (exercisesSign.equals("4")) {
                                        Toast.makeText(context, "深化应用添加成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "巩固练习添加成功", Toast.LENGTH_SHORT).show();

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


    //挑错
    private void saveMistakesInfo() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }
            jsonObject.put("UserCode", userCode);
            jsonObject.put("ContentID", contentID);
            jsonObject.put("ErrInfo", mistakes_info);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.SaveMisInfoMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "保存失败");
                                } else if (resultvalue.equals("0")) {
                                    Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();

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
     * recycle设置改变监听器
     */
    private void setRgCheckedListener() {


//        rg_grade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                setGradeTextColor();
//                switch (i){
//                    case R.id.rb_create_primary_first:
//                        exercisesSign = "1";
//                        rb_create_primary_first.setTextColor(Color.parseColor("#ffffff"));
//                        break;
//                    case R.id.rb_create_primary_second:
//                        exercisesSign = "4";
//                        rb_create_primary_second.setTextColor(Color.parseColor("#ffffff"));
//                        break;
//                    case R.id.rb_create_primary_three:
//                        exercisesSign = "5";
//                        rb_create_primary_three.setTextColor(Color.parseColor("#ffffff"));
//                        break;
//                    case R.id.rb_create_primary_four:
//                        exercisesSign = "2";
//                        rb_create_primary_four.setTextColor(Color.parseColor("#ffffff"));
//                        break;
//
//                }
//            }
//        });

    }

    private void setGradeTextColor() {
//        rb_create_primary_first.setTextColor(Color.parseColor("#666666"));
//        rb_create_primary_second.setTextColor(Color.parseColor("#666666"));
//        rb_create_primary_three.setTextColor(Color.parseColor("#666666"));
//        rb_create_primary_four.setTextColor(Color.parseColor("#666666"));

    }

    //横向 的recycler
    private void recycler() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context, "提示", "教师编号为空，请重新登录");
                return;
            }
            jsonObject.put("UserCode", "US0001218");
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.A001002;
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

                            Gson gson = new Gson();
                            A001002Bean bean = gson.fromJson(jsonObject.toString(), A001002Bean.class);

                            if (bean != null) {
                                if (bean.getTables().getTable().getRows().size() != 0) {
                                    menuNumber = String.valueOf(bean.getTables().getTable().getRows().get(0).getMenuNumber());
                                        for(int i = 0 ; i<bean.getTables().getTable().getRows().size();i++){
                                            if(bean.getTables().getTable().getRows().get(i).getTypeCode().equals("0")){
                                                beanCopy.add(bean.getTables().getTable().getRows().get(i));
                                            }
                                        }
                                    recyclerAdapter.setData(beanCopy,recyclerView);
                                    sendMessage(bean.getTables().getTable().getRows());
                                }
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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveEventBus(EventBusFrist event){
        menuNumber=event.getMessage();
    }
    private void sendMessage(List<A001002Bean.TablesBean.TableBean.RowsBean> bean){
        EventBus.getDefault().post(new EventBusTwo(bean));
    }
}
