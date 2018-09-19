package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.ClassListBean;
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
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;
//班级列表界面
public class ClassListActivity extends BaseActivity
        implements View.OnClickListener, XListView.IXListViewListener{

    private XListView listView;
    private TextView onBack;
    private ImageView iv_home;
    private TextView title_tv;
    private RelativeLayout rl_back, rl_home, rl_add;

    private Context context;
    private Handler mHandler;
    private List<ClassListBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;

    private SharedPreferences spConfig;
    private String userCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        context = this;

        findView();


    }


    private void findView() {
        listView = (XListView) findViewById(R.id.scheduling_lv);
        onBack = (TextView) findViewById(R.id.onBack);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        title_tv = (TextView) findViewById(R.id.common_title_tv);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);

        title_tv.setText("班级列表");
        userCode = spConfig.getString(SPCommonInfoBean.userCode, "");


        listData = new ArrayList<>();

        adapter = new CommonAdapter<ClassListBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_class_list) {
            @Override
            public void convert(ViewHolder holder, ClassListBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                if (i % 2 == 0) {
                    ll_item.setBackgroundColor(Color.parseColor("#d1ecd8"));
                } else {
                    ll_item.setBackgroundColor(Color.parseColor("#bae2c4"));
                }
                holder.setText(R.id.sd_no_tv, rowsBean.getF0001());
                holder.setText(R.id.sd_schgegin_tv, rowsBean.getF0009());
                holder.setText(R.id.sd_class_type_tv, rowsBean.getF0006() + rowsBean.getF0007());
                holder.setText(R.id.sd_subjects_tv, rowsBean.getF0008());
                holder.setText(R.id.sd_grade_tv, rowsBean.getF0010());
                holder.setText(R.id.sd_class_date_tv, rowsBean.getF0011());
            }
        };

        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);//激活加载更多
        listView.setPullRefreshEnable(true);
        onBack.setOnClickListener(this);
        rl_home.setOnClickListener(this);

        requestClassListData();

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
        }


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
        listView.setRefreshTime(df.format(new Date()));
    }

    //查询班级列表
    private void requestClassListData() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userCode == null || userCode.equals("")) {
                Toast.makeText(context, "教师编号为空，请重新登陆！", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GetClassListMethodName;
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
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无班级");
                                } else {
                                    Gson gson = new Gson();
                                    ClassListBean classListBean = gson.fromJson(jsonObject.toString(), ClassListBean.class);
                                    if (classListBean != null) {
                                        listData.addAll(classListBean.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无班级");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "该教师暂无班级");
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
