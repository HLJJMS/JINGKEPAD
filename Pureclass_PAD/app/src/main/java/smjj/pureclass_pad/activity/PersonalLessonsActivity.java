package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.KnowledgeBean;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.fragment.PersonalLessonFragment;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.LoadingBox;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.view.AutoHeightListView;

//个性化备课
public class PersonalLessonsActivity extends BaseActivity implements View.OnClickListener{

    private TextView onBack;

    private ImageView iv_home, iv_back, iv_pulsh;
    private TextView title_tv;
    private RelativeLayout rl_back, rl_home, rl_add;
    private Context context;
    private Handler mHandler;

    private SharedPreferences spConfig;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    //声明碎片管理者
    private FragmentManager fragmentManager;
    private PersonalLessonFragment personalLessonFragment;

    private List<String> filtrateList = new ArrayList<>();
    private String PWSelectTV = "";
    private CommonAdapter filtrateAdapter;

    private TextView menu_title;
    private AutoHeightListView listView, listView1, listView2, listView3;

    final List<String> list3 = new ArrayList<>();
    final List<KnowledgeBean.TablesBean.TableBean.RowsBean> list = new ArrayList<>();
    final List<KnowledgeBean.TablesBean.TableBean.RowsBean> list1 = new ArrayList<>();
    final List<KnowledgeBean.TablesBean.TableBean.RowsBean> list2 = new ArrayList<>();

    final List<KnowledgeBean.TablesBean.TableBean.RowsBean> addList = new ArrayList<>();
    final List<KnowledgeBean.TablesBean.TableBean.RowsBean> addList1 = new ArrayList<>();
    final List<KnowledgeBean.TablesBean.TableBean.RowsBean> addList2 = new ArrayList<>();
    final List<String> addList3 = new ArrayList<>();

    private CommonAdapter adapter, adapter1, adapter2, adapter3;

    private String grade, subject, parentID, knowledgeID;
    public String speakId, materialName, materialNo, classNo, startTime, className;
    public String speakName = "";

    private String classId;
    private String typleUser;
    private String userCode;

    private LoadingBox loadingBox;
    private int selectPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_lessons);
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        speakId = getIntent().getStringExtra("SpeakId");
        speakName = getIntent().getStringExtra("SpeakName");
        materialName = getIntent().getStringExtra("MaterialName");
        materialNo = getIntent().getStringExtra("MaterialNo");
        classNo = getIntent().getStringExtra("ClassNo");
        grade = getIntent().getStringExtra("Grade");
        subject = getIntent().getStringExtra("Subject");
        startTime = getIntent().getStringExtra("StartTime");
        className = getIntent().getStringExtra("ClassName");


        initFragment();
        initView();


    }


    //初始化菜单栏
    private void initView() {
        //初始化控件 及布局容器
        drawerLayout = (DrawerLayout) findViewById(R.id.class_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.class_navigationview);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back1);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        iv_pulsh = (ImageView) findViewById(R.id.iv_pulsh);

        iv_home.setImageResource(R.drawable.function);
        iv_back.setImageResource(R.drawable.menu_bt);
        onBack.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        rl_back.setVisibility(View.VISIBLE);

        title_tv.setText("个性化备课");

        list3.add("基础题");
        list3.add("中档题");
        list3.add("难题");
        list3.add("拔高题");

        rl_home.setOnClickListener(this);
        rl_add.setOnClickListener(this);
        onBack.setOnClickListener(this);
        iv_pulsh.setOnClickListener(this);


        //获取侧滑菜单的视图
        View view = navigationView.getHeaderView(0);
        findView(view);
        openOrCloseDrawerLayout();
    }

    //侧滑菜单中的视图处理
    private void findView(View view){
        listView = (AutoHeightListView) view.findViewById(R.id.listview);
        listView1 = (AutoHeightListView) view.findViewById(R.id.listview1);
        listView2 = (AutoHeightListView) view.findViewById(R.id.listview2);
        listView3 = (AutoHeightListView) view.findViewById(R.id.listview3);
        menu_title = (TextView) view.findViewById(R.id.grade_name);

        menu_title.setText(grade + subject);

        listView3.setVisibility(View.INVISIBLE);

        initAdapter();
        getKnowledgeData1();



    }

    //初始化碎片
    private void initFragment() {
        //获取碎片管理者
        fragmentManager = getSupportFragmentManager();
        //开启碎片事物
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //处理事务 最后一次添加的为默认展示的碎片
        if (personalLessonFragment == null){
            personalLessonFragment = new PersonalLessonFragment();
        }
        ft.add(R.id.class_framelayout, personalLessonFragment);

        //提交事务(汇报工作)
        ft.commit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.onBack:
                finish();
                break;
            case R.id.iv_pulsh:
                openOrCloseDrawerLayout();
                break;

        }

    }


    private void initAdapter(){

        adapter = new CommonAdapter<KnowledgeBean.TablesBean.TableBean.RowsBean>(context, list, R.layout.item_know_lv) {
            @Override
            public void convert(ViewHolder holder, final KnowledgeBean.TablesBean.TableBean.RowsBean rowsBean) {
                TextView tv = holder.getView(R.id.tv);
                tv.setText(rowsBean.getChaptername());
                if (addList.contains(rowsBean)){
                    tv.setTextColor(Color.parseColor("#ec5e20"));
                }else {
                    tv.setTextColor(Color.parseColor("#666666"));
                }
            }
        };
        adapter1 = new CommonAdapter<KnowledgeBean.TablesBean.TableBean.RowsBean>(context, list1, R.layout.item_know_lv) {
            @Override
            public void convert(ViewHolder holder, final KnowledgeBean.TablesBean.TableBean.RowsBean rowsBean) {
                TextView tv = holder.getView(R.id.tv);
                tv.setText(rowsBean.getChaptername());
                if (addList1.contains(rowsBean)){
                    tv.setTextColor(Color.parseColor("#ec5e20"));
                }else {
                    tv.setTextColor(Color.parseColor("#666666"));
                }
            }
        };

        adapter2 = new CommonAdapter<KnowledgeBean.TablesBean.TableBean.RowsBean>(context, list2, R.layout.item_know_lv) {
            @Override
            public void convert(ViewHolder holder, final KnowledgeBean.TablesBean.TableBean.RowsBean rowsBean) {
                TextView tv = holder.getView(R.id.tv);
                tv.setText(rowsBean.getChaptername());
                if (addList2.contains(rowsBean)){
                    tv.setTextColor(Color.parseColor("#ec5e20"));
                }else {
                    tv.setTextColor(Color.parseColor("#666666"));
                }
            }
        };

        adapter3 = new CommonAdapter<String>(context, list3, R.layout.item_know_lv) {
            @Override
            public void convert(ViewHolder holder, final String rowsBean) {
                TextView tv = holder.getView(R.id.tv);
                tv.setText(rowsBean);
                if (addList3.contains(rowsBean)){
                    tv.setTextColor(Color.parseColor("#ec5e20"));
                }else {
                    tv.setTextColor(Color.parseColor("#666666"));
                }

            }
        };



        listView.setAdapter(adapter);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addList.clear();
                addList.add(list.get(i));
                parentID = list.get(i).getSubID() + "";
                knowledgeID = list.get(i).getID() + "";
                adapter.setData(list);
                adapter.notifyDataSetChanged();
                list2.clear();
                adapter2.setData(list2);
                adapter2.notifyDataSetChanged();
                listView3.setVisibility(View.INVISIBLE);
                selectPoint = 1;
                getKnowledgeData2();
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addList1.clear();
                addList1.add(list1.get(i));
                knowledgeID = list1.get(i).getID() + "";
                parentID = list1.get(i).getSubID() + "";
                adapter1.setData(list1);
                adapter1.notifyDataSetChanged();
                listView3.setVisibility(View.INVISIBLE);
                selectPoint = 2;
                getKnowledgeData2();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addList2.clear();
                addList2.add(list2.get(i));
                knowledgeID = list2.get(i).getID() + "";
                adapter2.setData(list2);
                adapter2.notifyDataSetChanged();
                listView3.setVisibility(View.VISIBLE);
                refreshAdapter3();
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.item_pop_tv);
                addList3.clear();
                addList3.add(list3.get(i));
                adapter3.setData(list3);
                adapter3.notifyDataSetChanged();
                personalLessonFragment.refreshData(grade, subject, knowledgeID);
                openOrCloseDrawerLayout();
            }
        });

    }

    //获取一级知识点
    private void getKnowledgeData1(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("xueke", subject);
            jsonObject.put("YearID", grade);
//            jsonObject.put("xueke", "数学");
//            jsonObject.put("YearID", "初一");

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetKnowledge1MN;
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
                                if (resultvalue.equals("1")) {
                                    //未签到请求签到
                                    AlertDialogUtil.showAlertDialog(context, "提示", "未获取到知识点信息，请重新选择");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //已签到
                                    Gson gson = new Gson();
                                    KnowledgeBean knowledgeBean = gson.fromJson(jsonObject.toString(), KnowledgeBean.class);
                                    if (knowledgeBean != null) {
                                        list.addAll(knowledgeBean.getTables().getTable().getRows());
                                        if (list.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "未获取到知识点信息，请重新选择");
                                            return;
                                        }else {
                                            adapter.setData(list);
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "服务器返回数据格式有误，请重试");
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



    //获取子知识点
    private void getKnowledgeData2(){

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("xueke", subject);
            jsonObject.put("YearID", grade);
            jsonObject.put("ParentID", parentID);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetKnowledge2MN;
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
                                if (resultvalue.equals("1")) {
                                    //根据知识点请求题目 并关闭菜单
                                    if (selectPoint == 1){
                                        list1.clear();
                                        listView1.setVisibility(View.GONE);
                                        list2.clear();
                                        listView2.setVisibility(View.GONE);
                                    }else if (selectPoint == 2){
                                        list2.clear();
                                        listView2.setVisibility(View.GONE);
                                    }
                                    listView3.setVisibility(View.VISIBLE);
                                    refreshAdapter3();
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    //获取子知识点
                                    Gson gson = new Gson();
                                    KnowledgeBean knowledgeBean = gson.fromJson(jsonObject.toString(), KnowledgeBean.class);
                                    if (knowledgeBean != null) {
                                        if (knowledgeBean.getTables().getTable().getRows().size() != 0){
                                            if (selectPoint == 1){
                                                list1.clear();
                                                list1.addAll(knowledgeBean.getTables().getTable().getRows());
                                                adapter1.setData(list1);
                                                adapter1.notifyDataSetChanged();
                                                listView1.setVisibility(View.VISIBLE);
                                            }else if (selectPoint == 2){
                                                list2.clear();
                                                list2.addAll(knowledgeBean.getTables().getTable().getRows());
                                                adapter2.setData(list2);
                                                adapter2.notifyDataSetChanged();
                                                listView2.setVisibility(View.VISIBLE);
                                            }

                                        }else {
                                            AlertDialogUtil.showAlertDialog(context, "提示", "未获取到知识点信息，请重新选择");
                                            return;
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
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

    private void refreshAdapter3(){
        addList3.clear();
        adapter3.setData(list3);
        adapter3.notifyDataSetChanged();
    }


    /**
     * 打开关闭侧滑菜单
     */
    private void openOrCloseDrawerLayout(){
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    public void showLoading() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this,null);
        }
        loadingBox.Show();
    }

    public void closeLoading() {
        if (loadingBox != null) {
            loadingBox.close();
            loadingBox = null;
        }
    }


    public void showLoading1() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this,null);
        }
        loadingBox.Show1();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //使试题篮里的题目数和当前展示保持一致
        personalLessonFragment.requestBskList();
    }

}
