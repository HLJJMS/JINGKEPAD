package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.common.CommonAdapter;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.common.ViewHolder;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.xorzlistview.xlistview.XListView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/19.
 */
//入门测出门考的小组排名排名碎片
public class ModuleRankingFragment1 extends Fragment implements XListView.IXListViewListener {

    private TextView tv_ranking, tv_name, tv_score, tv_percent;
    private XListView listView;

    private Context context;
    //跳转标记 2入门测小组排名，4出门考小组排名
    private String enterMark;

    private List<List<String>> listData;
    private CommonAdapter adapter;
    private GoClassActivity activity;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private Handler mHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (GoClassActivity) getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_module_ranking, container, false);

        Bundle bundle=getArguments();
        enterMark=bundle.getString("enterMark");

        findView(contentView);

        requestGroupRanking();

        return contentView;
    }

    private void findView(View view){
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        tv_ranking = (TextView) view.findViewById(R.id.tv_ranking);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        tv_percent = (TextView) view.findViewById(R.id.tv_percent);


        tv_name.setText("小组名");
        tv_score.setText("平均分数");
        tv_percent.setText("正确率");


        listData = new ArrayList<>();


        adapter = new CommonAdapter<List<String>>(context, listData, R.layout.item_module_ranking_lv) {
            @Override
            public void convert(ViewHolder holder, final List<String> rowsBean) {
                int i = listData.indexOf(rowsBean);
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                if (i % 2 == 0) {
                    ll_item.setBackgroundColor(Color.parseColor("#d1ecd8"));
                } else {
                    ll_item.setBackgroundColor(Color.parseColor("#bae2c4"));
                }
                TextView rankingTextView = holder.getView(R.id.ranking_tv);
                ImageView rankingImageView = holder.getView(R.id.ranking_iv);
                if (i == 0){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.first);
                }else if (i ==1){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.second);
                }else if (i == 2){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingTextView.setVisibility(View.GONE);
                    rankingImageView.setImageResource(R.drawable.third);
                }else {
                    rankingImageView.setVisibility(View.GONE);
                    rankingTextView.setVisibility(View.VISIBLE);
                    rankingTextView.setText("" + (i + 1));
                }

               if (rowsBean.size() == 3){
                   holder.setText(R.id.name_tv, rowsBean.get(0));
                   holder.setText(R.id.score_tv, rowsBean.get(1));
                   holder.setText(R.id.percent_tv, rowsBean.get(2));
               }
            }
        };


        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(true);


    }


    @Override
    public void onRefresh() {
        //下拉刷新
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //下拉刷新
                onLoad();
                listData.clear();
                requestGroupRanking();
            }
        }, 500);

    }

    @Override
    public void onLoadMore() {

    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        listView.setRefreshTime(df.format(new Date()));
    }

    //获取入门测、出门考小组排名排名
    private void requestGroupRanking() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("LessonID",classId);
//            jsonObject.put("LessonID","EC1000000001");

            if (enterMark.equals("2")){
//                jsonObject.put("TestType","1");
                jsonObject.put("TestType","1");
            }else if (enterMark.equals("4")){
                jsonObject.put("TestType","2");
            }
            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.GroupRankingMethodName;
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

                        String str = CommonWay.decode(result.getProperty(0).toString());
                        if (str.contains("resultvalue")){

                            AlertDialogUtil.showAlertDialog(context, "提示", "获取小组排名失败，请等学生提交后再获取");

                        }else {
                            try {
                                JSONArray jsonArray = new JSONArray(str);
                                int length = jsonArray.length();
                                if (length != 0){
                                    for (int i = 0 ; i < length ; i ++){
                                        JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                                        if (jsonArray1 != null && jsonArray1.length() != 0){
                                            int length1 = jsonArray1.length();
                                            List<String> list = new ArrayList<String>();
                                            for (int i1 = 0; i1 < length1; i1 ++){
                                                list.add(jsonArray1.get(i1) + "");

                                            }
                                            listData.add(list);
                                        }
                                    }
                                    adapter.setData(listData);
                                    adapter.notifyDataSetChanged();
                                    if (listData.size() == 0){
                                        AlertDialogUtil.showAlertDialog(context, "提示", "暂无小组排名，请等学生提交后再查看");
                                    }
                                }else {
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂无小组排名，请等学生提交后再查看");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
