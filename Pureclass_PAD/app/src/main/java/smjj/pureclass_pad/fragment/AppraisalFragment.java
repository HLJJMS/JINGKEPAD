package smjj.pureclass_pad.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.AppraisalActivity;
import smjj.pureclass_pad.activity.AppraisalBskActivity;
import smjj.pureclass_pad.beans.InductionTestExercisesBean;
import smjj.pureclass_pad.beans.TestBasketBean;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/27.
 */
//测评碎片
public class AppraisalFragment extends Fragment implements View.OnClickListener, XListView.IXListViewListener{

    private TextView tv_amonut, tv_totals;
    private LinearLayout ll_exercises_cat;
    private LinearLayout ll_ranking, ll_grade, ll_subject;
    private TextView tv_ranking, tv_grade, tv_subject;

    private XListView listView;
    private TextView empty;

    private Context context;
    private Handler mHandler;

    private AppraisalActivity activity;

    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;

    private List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> isVisibleList;
    private List<Integer> isAddBskList;


    private List<String> list;
    private String ranking, grade, subject, knowledgeID;
    private CallBackValue callBackValue;


    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;

    private int pageNum = 1;
    private String mistakes_info;
    private int contentID;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (AppraisalActivity) getActivity();
        mHandler = new Handler();
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_appraisal, container, false);

        findView(contentView);

        return contentView;
    }


    private void findView(View view){
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        tv_amonut = (TextView) view.findViewById(R.id.tv_amonut);
        tv_totals = (TextView) view.findViewById(R.id.tv_totals);
        ll_exercises_cat = (LinearLayout) view.findViewById(R.id.ll_exercises_cat);
        ll_ranking = (LinearLayout) view.findViewById(R.id.ll_ranking);
        ll_grade = (LinearLayout) view.findViewById(R.id.ll_grade);
        ll_subject = (LinearLayout) view.findViewById(R.id.ll_subject);
        tv_ranking = (TextView) view.findViewById(R.id.tv_ranking);
        tv_grade = (TextView) view.findViewById(R.id.tv_grade);
        tv_subject = (TextView) view.findViewById(R.id.tv_subject);
        empty = (TextView) view.findViewById(R.id.empty);



        listData = new ArrayList<>();

        isVisibleList = new ArrayList<>();
        isAddBskList = new ArrayList<>();

        list = new ArrayList<>();

        listView.setEmptyView(empty);



        //刷新适配器前要做的事
        ininAdapter();

        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(true);
        ll_exercises_cat.setOnClickListener(this);
        ll_grade.setOnClickListener(this);
        ll_ranking.setOnClickListener(this);
        ll_subject.setOnClickListener(this);


    }

    private void ininAdapter(){


        adapter = new CommonAdapter<InductionTestExercisesBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_exercises_webview1) {
            @Override
            public void convert(ViewHolder holder, final InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean) {
                final int i = listData.indexOf(rowsBean);

                holder.setText(R.id.tv_item_no, (i + 1) + ".");
                holder.getView(R.id.tv_item_no).setVisibility(View.VISIBLE);


                initWebView(holder, rowsBean);

                final LinearLayout ll_answer5 = holder.getView(R.id.ll_exercises_answer5);
                final LinearLayout ll_answer6 = holder.getView(R.id.ll_exercises_answer6);
                LinearLayout ll_add_shop_cat = holder.getView(R.id.ll_add_shop_cat);
                LinearLayout ll_drop_down = holder.getView(R.id.ll_drop_down);
                LinearLayout ll_find_fault = holder.getView(R.id.ll_find_fault);
                final ImageView drop_dowmn = holder.getView(R.id.iv_drop_down);
                final ImageView iv_add_item_bank = holder.getView(R.id.iv_add_item_bank);


                if (isVisibleList.contains(rowsBean)) {
                    ll_answer5.setVisibility(View.VISIBLE);
                    ll_answer6.setVisibility(View.VISIBLE);
                    drop_dowmn.setImageResource(R.drawable.pull_up);
                } else {
                    ll_answer5.setVisibility(View.GONE);
                    ll_answer6.setVisibility(View.GONE);
                    drop_dowmn.setImageResource(R.drawable.drop_down);
                }

                if (isAddBskList.contains(rowsBean.getContentID())) {
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
                            drop_dowmn.setImageResource(R.drawable.drop_down);
                            isVisibleList.remove(rowsBean);
                        } else {
                            ll_answer5.setVisibility(View.VISIBLE);
                            ll_answer6.setVisibility(View.VISIBLE);
                            drop_dowmn.setImageResource(R.drawable.pull_up);
                            isVisibleList.add(rowsBean);
                        }
                    }
                });

                ll_add_shop_cat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAddBskList.contains(rowsBean.getContentID())) {
                            //请求加入试题篮
                            addTestToBsk(rowsBean.getContentID());
                            tv_amonut.setText(isAddBskList.size() + "");
//                            iv_add_item_bank.setImageResource(R.drawable.add2);
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
        String choses = rowsBean.getChoses();
        if (stem != null && !stem.equals("")) {
            String str1 = CommonWay.jsoup(stem + choses);
            webView1.loadData(str1, "text/html; charset=UTF-8", null);
        } else {
            webView1.setVisibility(View.GONE);
        }
        String correct = rowsBean.getCorrect();
        if (correct != null && !correct.equals("")) {
            String str2 = CommonWay.jsoup1(correct);
            webView2.loadData(str2, "text/html; charset=UTF-8", null);
        } else {
            webView2.setVisibility(View.GONE);
        }
        String analysis = rowsBean.getAnalysis();
        if (analysis != null && !analysis.equals("")) {
            String str3 = CommonWay.jsoup2(analysis);
            webView3.loadData(str3, "text/html; charset=UTF-8", null);
        } else {
            webView3.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_exercises_cat:
                Intent intent = new Intent(context, AppraisalBskActivity.class);
                intent.putExtra("GradeName", grade);
                intent.putExtra("Subject", subject);
                startActivity(intent);
                break;
            case R.id.ll_ranking:
                list.clear();
                list.add("小学");
                list.add("初中");
                list.add("高中");
                showPopupWindow(0);
                break;
            case R.id.ll_grade:
                if (tv_ranking.getText().toString().equals("学段")){
                    AlertDialogUtil.showAlertDialog(context, "提示", "请先选择学段");
                    return;
                }
                addGrade();
                showPopupWindow(1);
                break;
            case R.id.ll_subject:
                if (tv_grade.getText().toString().equals("年级")){
                    AlertDialogUtil.showAlertDialog(context, "提示", "请先选择年级");
                    return;
                }
                addSubject();
                showPopupWindow(2);
                break;

        }

    }


    private void showMistakesPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_mistakes_view, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

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

        popupWindow.showAtLocation(ll_exercises_cat, Gravity.CENTER,0,0);

    }


    //选择学段、年级、科目的弹出框
    private void showPopupWindow(final int position){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_view1, null);
        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,6,2,0.5f,-1);

        final ListView listView = (ListView) view.findViewById(R.id.lv);
        CommonAdapter adapter = new CommonAdapter<String>(context, list, R.layout.item_pop_lv) {
            @Override
            public void convert(ViewHolder holder, String rowsBean) {
                holder.setText(R.id.item_pop_tv, rowsBean);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //请求分组
                if (position == 0 ){
                    if (!tv_ranking.getText().toString().equals(list.get(i))){
                        tv_ranking.setText(list.get(i));
                        ranking = list.get(i);
                        tv_grade.setText("年级");
                        tv_subject.setText("科目");
                        callBackValue.initKnowledgePoint();
                    }
                }
                if (position == 1 ){
                    if (!tv_grade.getText().toString().equals(list.get(i))){
                        tv_grade.setText(list.get(i));
                        grade = list.get(i);
                        tv_subject.setText("科目");
                        callBackValue.initKnowledgePoint();
                    }
                }
                if (position == 2 ){
                    if (!tv_subject.getText().toString().equals(list.get(i))){
                        tv_subject.setText(list.get(i));
                        subject = list.get(i);
                        callBackValue.initKnowledgePoint();
                        //接口回调打开菜单请求以及知识点
                        callBackValue.SendMessageValue(ranking.substring(0,1) + grade.substring(0,1), subject);
                    }
                }

                popupWindow.dismiss();
            }
        });

        if (position == 0 ){
            PopupWindowUtil.showAtLoactionBottom(ll_ranking);
        }
        if (position == 1 ){
            PopupWindowUtil.showAtLoactionBottom(ll_grade);
        }
        if (position == 2 ){
            PopupWindowUtil.showAtLoactionBottom(ll_subject);
        }

    }

    private void addGrade(){
        list.clear();
        if (ranking.equals("小学")){
            list.add("一年级");
            list.add("二年级");
            list.add("三年级");
            list.add("四年级");
            list.add("五年级");
            list.add("六年级");
        }else if (ranking.equals("初中") || ranking.equals("高中")){
            list.add("一年级");
            list.add("二年级");
            list.add("三年级");
        }
    }

    private void addSubject(){
        list.clear();
        list.add("语文");
        list.add("数学");
        list.add("英语");
        list.add("物理");
        list.add("化学");
        list.add("生物");
        list.add("历史");
        list.add("地理");
        list.add("政治");
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        onLoad();
        listData.clear();
        pageNum = 1;
        requestTest();
    }

    @Override
    public void onLoadMore() {
//        上拉加载更多
        onLoad();
        pageNum++;
//        requestTest();
//        adapter.setData(listData);
//        adapter.notifyDataSetChanged();
    }


    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        listView.setRefreshTime(df.format(new Date()));
    }


    //定义一个回调接口
    public interface CallBackValue{
        public void SendMessageValue(String strValue, String strSubject);
        public void initKnowledgePoint();
    }

    /**
     * 刷新题目
     * @param gradeName
     * @param subject1
     * @param knowledgeID1
     */
    public void refreshData(String gradeName, String subject1, String knowledgeID1){
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
            jsonObject.put("TestType","10");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName;

            methodName = Constants.GetAprExeMN;

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
                                        if (inductionTestExercisesBean.getTables().getTable().getRows().size() != 0){
                                            listData.addAll(inductionTestExercisesBean.getTables().getTable().getRows());
                                            adapter.setData(listData);
                                            adapter.notifyDataSetChanged();
                                        }else {
                                            if (pageNum == 1){
                                                AlertDialogUtil.showAlertDialog(context, "提示", "当前知识点无试题");
                                            }else {
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
    public void requestBskList(){

        isAddBskList.clear();

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }
            if (grade == null || grade.equals("")) {
                return;
            }

            jsonObject.put("UserCode", userCode);
            jsonObject.put("TestType", "10");
            jsonObject.put("GradeName", grade);
            jsonObject.put("Subject", subject);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetAprBskMN;
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
                                }else {
                                    //试题篮中有试题
                                    Gson gson = new Gson();
                                    TestBasketBean testBasketBean = gson.fromJson(jsonObject.toString(), TestBasketBean.class);
                                    if (testBasketBean != null) {
                                        int lenth = testBasketBean.getTables().getTable().getRows().size();
                                        for (int i = 0; i < lenth; i ++){
                                            Integer cententId = testBasketBean.getTables().getTable().getRows().get(i).getContentID();
                                            if (cententId != null){
                                                isAddBskList.add(cententId);
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
    private void addTestToBsk(final Integer contentId){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }

            jsonObject.put("UserCode", userCode);
            jsonObject.put("TestType", "10");
            jsonObject.put("GradeName", grade);
            jsonObject.put("ContentID", contentId);
            jsonObject.put("Subject", subject);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.AddAprBskMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "加入试题篮失败");
                                    adapter.notifyDataSetChanged();
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    isAddBskList.add(contentId);
                                    tv_amonut.setText(isAddBskList.size() + "");
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
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
            if (userCode == null || userCode.equals("")){
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录");
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
                                } else if (resultvalue.equals("0")){
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

}
