package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.ExerAnswerDetailsBean;
import smjj.pureclass_pad.beans.InductionTestExercisesBean;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.view.AnnularChart;
import smjj.pureclass_pad.view.VerticalChart;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static android.content.Context.MODE_PRIVATE;
import static smjj.pureclass_pad.R.id.annular_chart;

/**
 * Created by wlm on 2017/7/19.
 */
//入门测，出门考，深化应用，巩固练习答案解析界面
public class ExercisesAnswerFragment extends Fragment {

    private XListView listView;
    private TextView tv_answer_title;

    private Context context;

    //跳转标记 1入门测答案解析，2出门考答案解析，3巩固练习答案解析，4深化应用答案解析
    private String enterAnswerMark;

    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private GoClassActivity activity;

    //图形是后可见
    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> isVisibleChartList;
    //试题详情是否可见
    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> isVisibleDeatilsList;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private String grade, subject;
    private String schoolNo;
    private String rightAnswers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (GoClassActivity) getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser, "");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        classId = spConfig.getString(SPCommonInfoBean.classId, "");
        schoolNo = spConfig.getString(SPCommonInfoBean.userDepartNo, "");

        Bundle bundle = getArguments();
        enterAnswerMark = bundle.getString("enterAnswerMark");
        grade =spConfig.getString(SPCommonInfoBean.selectGrade, "");
        subject = spConfig.getString(SPCommonInfoBean.selectSubject, "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_exercises_answer, container, false);

        findView(contentView);

        return contentView;
    }

    private void findView(View view) {
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        tv_answer_title = (TextView) view.findViewById(R.id.tv_answer_title);

        if (enterAnswerMark.equals("1")) {
            tv_answer_title.setText("入门测答案解析");
        } else if (enterAnswerMark.equals("3")) {
            tv_answer_title.setText("巩固练习答案解析");

        } else if (enterAnswerMark.equals("4")) {
            tv_answer_title.setText("深化应用答案解析");

        }else if (enterAnswerMark.equals("2")){
            tv_answer_title.setText("出门考答案解析");
        }

        tv_answer_title.setVisibility(View.GONE);
        listData = new ArrayList<>();
        isVisibleChartList = new ArrayList<>();
        isVisibleDeatilsList = new ArrayList<>();
//        adapter = new CommonAdapter<InductionTestExercisesBean.TablesBean.TableBean.RowsBean>(context, listData, R.item_lesson.item_exercises_webview) {
        adapter = new CommonAdapter<InductionTestExercisesBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_exercises_chart) {
            @Override
            public void convert(ViewHolder holder, final InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);

                holder.setText(R.id.tv_item_no, (i + 1) + ". ");
                holder.setText(R.id.tv_correct, "正确率:" + (int)(rowsBean.getCorrects()*100) + "%");
//                holder.setText(R.id.tv_correct, "正确率:" + (int)(100) + "%");
                holder.getView(R.id.tv_item_no).setVisibility(View.VISIBLE);

//                LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
//                LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
//                LinearLayout ll_answer7 = holder.getView(R.id.ll_exercises_answer7);
//                ll_answer5.setVisibility(View.VISIBLE);
//                ll_answer6.setVisibility(View.VISIBLE);
//                ll_answer7.setVisibility(View.GONE);

                initWebView(holder, rowsBean);
                checkDetails(holder, rowsBean);

            }
        };

        listView.setAdapter(adapter);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(false);

        requestTest();

    }

    private void initWebView(final ViewHolder holder, final InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {
        final WebView webView1 = holder.getView(R.id.wb_exercises1);
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
        //隐藏滚动条
        webView1.setHorizontalScrollBarEnabled(false);//水平不显示
        webView1.setVerticalScrollBarEnabled(false); //垂直不显示
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
        //隐藏滚动条
        webView2.setHorizontalScrollBarEnabled(false);//水平不显示
        webView2.setVerticalScrollBarEnabled(false); //垂直不显示
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
        //隐藏滚动条
        webView3.setHorizontalScrollBarEnabled(false);//水平不显示
        webView3.setVerticalScrollBarEnabled(false); //垂直不显示
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
        //隐藏滚动条
        webView4.setHorizontalScrollBarEnabled(false);//水平不显示
        webView4.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings s3 = webView4.getSettings();
        s3.setJavaScriptEnabled(true);

        final String stem = rowsBean.getStem();
        if (stem != null && !stem.equals("")) {
            String str1 = CommonWay.jsoup3(stem);
            webView1.loadData(str1, "text/html; charset=UTF-8", null);
        } else {
            webView1.setVisibility(View.GONE);
        }

        String choses = rowsBean.getChoses();
        if (stem != null && !stem.equals("")) {
            String str4 = CommonWay.getHtmlData(choses);
            webView4.loadData(str4, "text/html; charset=UTF-8", null);
        } else {
            webView4.setVisibility(View.GONE);
        }

        String correct = rowsBean.getCorrect();
        if (correct != null && !correct.equals("")) {
            String str2 = CommonWay.getHtmlData1(correct);
            webView2.loadData(str2, "text/html; charset=UTF-8", null);
        } else {
            webView2.setVisibility(View.GONE);
        }
        String analysis = rowsBean.getAnalysis();
        if (analysis != null && !analysis.equals("")) {
            String str3 = CommonWay.getHtmlData1(analysis);
            webView3.loadData(str3, "text/html; charset=UTF-8", null);
        } else {
            webView3.setVisibility(View.GONE);
        }


        final LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
        final LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
        final LinearLayout ll_answer7 = holder.getView(R.id.ll_exercises_answer7);
        LinearLayout ll_accuracy = holder.getView(R.id.ll_accuracy);
        final LinearLayout ll_details = holder.getView(R.id.ll_details);
        final LinearLayout ll_ranking = holder.getView(R.id.ll_ranking);
        final LinearLayout ll_chart = holder.getView(R.id.ll_chart);
        final TextView tv_line = holder.getView(R.id.tv_line);
        final ImageView drop_dowmn = holder.getView(R.id.iv_drop_down);
        final VerticalChart verticalChart = holder.getView(R.id.vertical_chart);
        final AnnularChart annularChart = holder.getView(annular_chart);

        if (isVisibleDeatilsList.contains(rowsBean)) {
            ll_answer5.setVisibility(View.VISIBLE);
            ll_answer6.setVisibility(View.VISIBLE);
            ll_answer7.setVisibility(View.VISIBLE);
            if (stem != null && !stem.equals("")) {
                String str1 = CommonWay.getHtmlData(stem);
                webView1.loadData(str1, "text/html; charset=UTF-8", null);
            } else {
                webView1.setVisibility(View.GONE);
            }
        } else {
            ll_answer5.setVisibility(View.GONE);
            ll_answer6.setVisibility(View.GONE);
            ll_answer7.setVisibility(View.GONE);
            if (stem != null && !stem.equals("")) {
                String str2 = CommonWay.jsoup3(stem);
                webView1.loadData(str2, "text/html; charset=UTF-8", null);
            } else {
                webView1.setVisibility(View.GONE);
            }
        }

        if (isVisibleChartList.contains(rowsBean)) {
            ll_ranking.setVisibility(View.VISIBLE);
            ll_chart.setVisibility(View.VISIBLE);
            tv_line.setVisibility(View.VISIBLE);
        } else {
            ll_ranking.setVisibility(View.GONE);
            ll_chart.setVisibility(View.GONE);
            tv_line.setVisibility(View.GONE);


            ll_answer5.setVisibility(View.GONE);
            ll_answer6.setVisibility(View.GONE);
            ll_answer7.setVisibility(View.GONE);
            if (stem != null && !stem.equals("")) {
                String str2 = CommonWay.jsoup3(stem);
                webView1.loadData(str2, "text/html; charset=UTF-8", null);
            } else {
                webView1.setVisibility(View.GONE);
            }
        }

        ll_accuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestAnswerDetailsTest(rowsBean.getQID(), verticalChart,holder);
                annularChart.initView((int) (rowsBean.getCorrects()*100));
                rightAnswers = rowsBean.getCorrect();

                if (isVisibleChartList.contains(rowsBean)) {
                    ll_ranking.setVisibility(View.GONE);
                    ll_chart.setVisibility(View.GONE);
                    tv_line.setVisibility(View.GONE);
                    isVisibleChartList.remove(rowsBean);
                } else {
                    ll_ranking.setVisibility(View.VISIBLE);
                    ll_chart.setVisibility(View.VISIBLE);
                    tv_line.setVisibility(View.VISIBLE);
//                    isVisibleChartList.add(rowsBean);
                }
            }

        });


        ll_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisibleDeatilsList.contains(rowsBean)) {
                    ll_answer5.setVisibility(View.GONE);
                    ll_answer6.setVisibility(View.GONE);
                    ll_answer7.setVisibility(View.GONE);
                    drop_dowmn.setImageResource(R.drawable.drop_down);

                    isVisibleDeatilsList.remove(rowsBean);
                    if (stem != null && !stem.equals("")) {
                        String str2 = CommonWay.jsoup3(stem);
                        webView1.loadData(str2, "text/html; charset=UTF-8", null);
                    } else {
                        webView1.setVisibility(View.GONE);
                    }
//                    if (isVisibleChartList.contains(rowsBean)) {
                        ll_ranking.setVisibility(View.GONE);
                        ll_chart.setVisibility(View.GONE);
                        tv_line.setVisibility(View.GONE);
//                        isVisibleChartList.remove(rowsBean);
//                    }

                } else {
                    ll_answer5.setVisibility(View.VISIBLE);
                    ll_answer6.setVisibility(View.VISIBLE);
                    ll_answer7.setVisibility(View.VISIBLE);
                    drop_dowmn.setImageResource(R.drawable.pull_up);
                    isVisibleDeatilsList.add(rowsBean);

                    if (stem != null && !stem.equals("")) {
                        String str1 = CommonWay.getHtmlData(stem);
                        webView1.loadData(str1, "text/html; charset=UTF-8", null);
                    } else {
                        webView1.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    //查看试题详情
    private void checkDetails(ViewHolder holder, final InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {

    }


    //请求入门测、出门考、巩固练习、深化应用已发布的试题
    private void requestTest() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonID", classId);
            if (enterAnswerMark.equals("1")) {
                jsonObject.put("TestType", "1");
            } else if (enterAnswerMark.equals("2")) {
                jsonObject.put("TestType", "2");
            } else if (enterAnswerMark.equals("3")) {
                jsonObject.put("TestType", "5");
            } else if (enterAnswerMark.equals("4")) {
                jsonObject.put("TestType", "4");
            }

            if (activity.isDualTeacher.equals("0") && activity.isCertificate.equals("1")){
                jsonObject.put("SchoolNo",schoolNo);
            }else {
                jsonObject.put("SchoolNo","");
            }


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

//            methodName = Constants.GetClassExercMN2;
            methodName = Constants.QueryAnswerMN2;


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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
                                } else {
                                    Gson gson = new Gson();
                                    InductionTestExercisesBean inductionTestExercisesBean = gson.fromJson(jsonObject.toString(), InductionTestExercisesBean.class);
                                    if (inductionTestExercisesBean != null) {
                                        listData.addAll(inductionTestExercisesBean.getTables().getTable().getRows());
//                                        initAdapter();
//                                        CommonWay.testSort(listData);
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
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

    //请求入门测、出门考、巩固练习、深化应用答题详情
    private void requestAnswerDetailsTest(String ID, final VerticalChart verticalChart, final ViewHolder holder) {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);
            jsonObject.put("LessonID", classId);
            jsonObject.put("SSH", activity.isDualTeacher);
            jsonObject.put("QID", ID);
            if (enterAnswerMark.equals("1")) {
                jsonObject.put("TestType", "1");
            } else if (enterAnswerMark.equals("2")) {
                jsonObject.put("TestType", "2");
            } else if (enterAnswerMark.equals("3")) {
                jsonObject.put("TestType", "5");
            } else if (enterAnswerMark.equals("4")) {
                jsonObject.put("TestType", "4");
            }
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
            String methodName;

//            methodName = Constants.GetClassExercMN2;
            methodName = Constants.QueryAnswerDetailsMN2;

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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂无答案解析");
                                } else {
                                    Gson gson = new Gson();
                                    ExerAnswerDetailsBean exerAnswerDetailsBean = gson.fromJson(jsonObject.toString(), ExerAnswerDetailsBean.class);
                                    if (exerAnswerDetailsBean != null) {
                                        initVerChart(exerAnswerDetailsBean, verticalChart);
                                        String stuNo1 = "";
                                        String stuNo2 = "";
                                        String stuNo3 = "";
                                        stuNo1 = exerAnswerDetailsBean.getStudentNo1();
                                        stuNo2 = exerAnswerDetailsBean.getStudentNo2();
                                        stuNo3 = exerAnswerDetailsBean.getStudentNo3();
                                        holder.setText(R.id.tv_first, exerAnswerDetailsBean.getStudentName1());
                                        if (stuNo2 != null && !stuNo2.equals(stuNo1)){
                                            holder.setText(R.id.tv_second, exerAnswerDetailsBean.getStudentName2());
                                        }
                                        if (stuNo3 != null && !stuNo3.equals(stuNo1) && !stuNo3.equals(stuNo2)){
                                            holder.setText(R.id.tv_three, exerAnswerDetailsBean.getStudentName3());
                                        }

                                        String time = exerAnswerDetailsBean.getAverage();
//                                        if (time != null && !time.equals("")){
//                                            long time1 = Long.valueOf(time)*1000;
//                                            if (time1 != 0){
//                                                String averageTime = FormatUtils.formatTime(time1);
//                                                holder.setText(R.id.tv_time, averageTime);
//                                                holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
//                                                holder.getView(R.id.tv_time1).setVisibility(View.VISIBLE);
//                                            }else {
//                                                holder.getView(R.id.tv_time).setVisibility(View.GONE);
//                                                holder.getView(R.id.tv_time1).setVisibility(View.GONE);
//                                            }
//
//                                        }else {
//                                            holder.getView(R.id.tv_time).setVisibility(View.GONE);
//                                            holder.getView(R.id.tv_time1).setVisibility(View.GONE);
//                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "暂未发布试卷");
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

    private void initVerChart(ExerAnswerDetailsBean exerAnswerDetailsBean, VerticalChart verticalChart){

        List<Integer> percentList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();
        //总人数呢
        double total = 0;
        double selectA = Double.valueOf(exerAnswerDetailsBean.getContNoA());
        double selectB = Double.valueOf(exerAnswerDetailsBean.getContNoB());
        double selectC = Double.valueOf(exerAnswerDetailsBean.getContNoC());
        double selectD = Double.valueOf(exerAnswerDetailsBean.getContNoD());
        double selectW = Double.valueOf(exerAnswerDetailsBean.getContNoW());//代表未答题的人数

        total = selectA + selectB + selectC + selectD + selectW;

        if (total != 0){
            percentList.add((int) ((selectA/total)*100));
            percentList.add((int) ((selectB/total)*100));
            percentList.add((int) ((selectC/total)*100));
            percentList.add((int) ((selectD/total)*100));
            percentList.add((int) ((selectW/total)*100));

        }

        yList.add((int) selectA);
        yList.add((int) selectB);
        yList.add((int) selectC);
        yList.add((int) selectD);
        yList.add((int) selectW);

        verticalChart.initView(yList, percentList, rightAnswers);

    }

//    private void initAdapter(){
//        adapter = new CommonAdapter<InductionTestExercisesBean.TablesBean.TableBean.RowsBean>(context, listData, R.item_lesson.item_exercises_webview) {
//            @Override
//            public void convert(ViewHolder holder, final InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {
//                int i = listData.indexOf(rowsBean);
//
//                LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
//                LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
//                ll_answer5.setVisibility(View.VISIBLE);
//                ll_answer6.setVisibility(View.VISIBLE);
//                Log.d("wwwwwwwwwwwwwwwwww", "yyyyyyyyyyyyyy ");
//                initWebView(holder, rowsBean);
//            }
//        };
//
//        listView.setAdapter(adapter);
//    }


}
