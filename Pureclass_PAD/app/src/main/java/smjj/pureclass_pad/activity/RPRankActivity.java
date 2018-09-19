package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.beans.RedPacketRankBean;
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

//红包排行榜
public class RPRankActivity extends BaseActivity {
    private XListView listView;
    private Handler mHandler;
    private SharedPreferences spConfig;
    private Context context;

    private List<RedPacketRankBean.TablesBean.TableBean.RowsBean> listData;
    private CommonAdapter adapter;
    private String testNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rprank);
        context = this;
        ActivityManage.addActivity(this);
        mHandler = new Handler();
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        testNo = getIntent().getStringExtra("TestNo");


        findView();

    }

    private void findView() {
        listView = (XListView) findViewById(R.id.rp_rank_lv);

        listData = new ArrayList<>();



        adapter = new CommonAdapter<RedPacketRankBean.TablesBean.TableBean.RowsBean>(context, listData, R.layout.item_rp_rank_lv) {
            @Override
            public void convert(ViewHolder holder, final RedPacketRankBean.TablesBean.TableBean.RowsBean rowsBean) {
                int i = listData.indexOf(rowsBean);

                holder.setText(R.id.name_tv, rowsBean.getF0011());

                holder.setText(R.id.tv_medal_total, rowsBean.getF0012() + "金币");
                holder.setText(R.id.tv_branch_name,  (i+1) + "");

                ImageView rankingImageView = holder.getView(R.id.ranking_iv);
                if (i == 0){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingImageView.setImageResource(R.drawable.rp_first);
                }else if (i ==1){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingImageView.setImageResource(R.drawable.rp_seconder);
                }else if (i == 2){
                    rankingImageView.setVisibility(View.VISIBLE);
                    rankingImageView.setImageResource(R.drawable.rp_third);
                }else {
                    rankingImageView.setVisibility(View.INVISIBLE);
                }

            }
        };

        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
//        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);//激活加载更多
        listView.setPullRefreshEnable(false);

        requestRedPacketRank();
    }


    //获取个人积分
    private void requestRedPacketRank() {

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入

            if (testNo == null || testNo.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","抢答试题编号不能为空，请重新开始抢答");
                return;
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("TestNo",testNo);


            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.RedPacketRankMN2;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "暂时无人抢到红包，请稍后再查看");
                                } else {

                                    Gson gson = new Gson();
                                    RedPacketRankBean redPacketRankBean = gson.fromJson(jsonObject.toString(), RedPacketRankBean.class);
                                    if (redPacketRankBean != null) {
                                        listData.clear();
                                        listData.addAll(redPacketRankBean.getTables().getTable().getRows());
                                        adapter.setData(listData);
                                        adapter.notifyDataSetChanged();
                                        if (listData.size() == 0){
                                            AlertDialogUtil.showAlertDialog(context, "提示", "暂无红包排行榜，请稍后再查看");
                                        }

                                    } else {
                                        AlertDialogUtil.showAlertDialog(context, "提示", "暂无红包排行榜，请稍后再查看");
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
