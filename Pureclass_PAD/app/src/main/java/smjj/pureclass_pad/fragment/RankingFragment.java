package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.beans.PersonageScoreBean;
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
 * Created by wlm on 2017/7/15.
 */
//个人积分榜
public class RankingFragment extends Fragment implements XListView.IXListViewListener{

    private TextView tv_ranking, tv_name, tv_score;
    private XListView listView;
    private TextView empty;

    private Context context;

    private List<PersonageScoreBean.TablesBean.TableBean.RowsBean> listData;
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
        View contentView = inflater.inflate(R.layout.fragment_ranking, container, false);

        findView(contentView);
        return contentView;
    }

    private void findView(View view){
        listView = (XListView) view.findViewById(R.id.scheduling_lv);
        tv_ranking = (TextView) view.findViewById(R.id.tv_ranking);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        empty = (TextView) view.findViewById(R.id.empty);


        listData = new ArrayList<>();

        adapter = new CommonAdapter<PersonageScoreBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_medal_ranking_lv) {
            @Override
            public void convert(ViewHolder holder, final PersonageScoreBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);



                holder.setText(R.id.name_tv, rowsBean.getF0011());
                holder.setText(R.id.tv_medal, rowsBean.getF0006() + "");
                holder.setText(R.id.tv_medal_total, rowsBean.getF0007() + "");
                holder.setText(R.id.tv_branch_name, rowsBean.getF0009() + "");

                TextView rankingTextView = holder.getView(R.id.ranking_tv);
                rankingTextView.setText("第 " + (i + 1) + " 名");
                ImageView rankingImageView = holder.getView(R.id.ranking_iv);
                if (i == 0){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingImageView.setImageResource(R.drawable.ranking_first);
                }else if (i ==1){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingImageView.setImageResource(R.drawable.ranking_second);
                }else if (i == 2){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingImageView.setImageResource(R.drawable.ranking_third);
                }else {
                    rankingImageView.setVisibility(View.INVISIBLE);
                }

//                setScoreVisible(holder,rowsBean);

            }
        };

        listView.setEmptyView(empty);
        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(true);

        requestScore();


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
                requestScore();
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

    //获取个人积分
    private void requestScore() {
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (classId == null || classId.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","排课编号为空，请重新选择！");
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("classId",classId);
//            jsonObject.put("LessonID","EC1000000001");


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.IQGetRankMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "获取个人积分失败，请稍后再查看");
                                } else {

                                    Gson gson = new Gson();
                                    PersonageScoreBean personageScoreBean = gson.fromJson(jsonObject.toString(), PersonageScoreBean.class);
                                    if (personageScoreBean != null) {
                                        listData.clear();
                                        listData.addAll(personageScoreBean.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无个人积分信息，请稍后再查看");
                                        }
                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "获取个人积分失败，请稍后再查看");
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
