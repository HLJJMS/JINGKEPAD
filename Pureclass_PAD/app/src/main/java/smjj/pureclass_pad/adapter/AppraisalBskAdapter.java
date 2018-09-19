package smjj.pureclass_pad.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.AppraisalBskActivity;
import smjj.pureclass_pad.beans.TestBasketBean;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.GameWebViewClient;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.LoadingBox;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.xorzlistview.zlistview.BaseSwipeAdapter;
import smjj.pureclass_pad.xorzlistview.zlistview.DragEdge;
import smjj.pureclass_pad.xorzlistview.zlistview.ShowMode;
import smjj.pureclass_pad.xorzlistview.zlistview.SimpleSwipeListener;
import smjj.pureclass_pad.xorzlistview.zlistview.ZSwipeItem;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/9/27.
 */

public class AppraisalBskAdapter extends BaseSwipeAdapter {

    private AppraisalBskActivity context;
    private List<TestBasketBean.TablesBean.TableBean.RowsBean> datas;
    private Map<Integer, String> scoreMap;
    private Map<Integer, Integer> itemNoMap;
    private LayoutInflater inflater;
    private SharedPreferences spConfig;
    private String classId;
    private String grade;
    private String userCode;
    private String subject;
    private LoadingBox loadingBox;
    private int setPostion;
    //判断有没有生成试题
    private boolean isSuccess;


    public Map<Integer, Integer> getItemNoMap() {
        return itemNoMap;
    }

    public void setItemNoMap(Map<Integer, Integer> itemNoMap) {
        this.itemNoMap = itemNoMap;
    }

    public Map<Integer, String> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<Integer, String> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public AppraisalBskAdapter(Context context, List<TestBasketBean.TablesBean.TableBean.RowsBean> datas, String gradeName, String subject1) {
        this.context = (AppraisalBskActivity) context;
        inflater = LayoutInflater.from(context);
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        grade = gradeName;
        subject = subject1;
        scoreMap = new HashMap<>();
        itemNoMap = new HashMap<>();
        isSuccess = false;
        setData(datas);
    }

    public void setData(List<TestBasketBean.TablesBean.TableBean.RowsBean> data) {
        if (data == null) {
            datas = new ArrayList<>();
        } else {
            this.datas = data;
        }

        this.notifyDataSetChanged();


    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_item;
    }


    @Override
    public View generateView(int position, ViewGroup parent) {
        return inflater.inflate(R.layout.item_bank_lv, parent, false);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final ZSwipeItem swipeItem = (ZSwipeItem) convertView.findViewById(R.id.swipe_item);
        LinearLayout ll_delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
        LinearLayout ll_side_delete = (LinearLayout) convertView.findViewById(R.id.ll_side_delete);
        ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
        ImageView iv_test_line = (ImageView) convertView.findViewById(R.id.iv_test_line);
        LinearLayout ll_set_score = (LinearLayout) convertView.findViewById(R.id.ll_set_score);
        ll_set_score.setVisibility(View.VISIBLE);
        iv_test_line.setVisibility(View.GONE);

        TextView textView = (TextView) convertView.findViewById(R.id.tv_item_no);
        textView.setText((position + 1) + ".");
        textView.setVisibility(View.VISIBLE);

        initWebView(convertView, datas.get(position));
        setEdListener(convertView, datas.get(position));

        if (isSuccess){
            ll_side_delete.setVisibility(View.GONE);
        }else {
            ll_side_delete.setVisibility(View.VISIBLE);
        }



        swipeItem.setShowMode(ShowMode.PullOut);
        swipeItem.setDragEdge(DragEdge.Right);
        swipeItem.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(ZSwipeItem layout) {
                Log.d(TAG, "打开:" + position);
            }

            @Override
            public void onClose(ZSwipeItem layout) {
                Log.d(TAG, "关闭:" + position);
            }

            @Override
            public void onStartOpen(ZSwipeItem layout) {
                Log.d(TAG, "准备打开:" + position);
            }

            @Override
            public void onStartClose(ZSwipeItem layout) {
                Log.d(TAG, "准备关闭:" + position);
            }

            @Override
            public void onHandRelease(ZSwipeItem layout, float xvel, float yvel) {
                Log.d(TAG, "手势释放");
            }

            @Override
            public void onUpdate(ZSwipeItem layout, int leftOffset,
                                 int topOffset) {
                Log.d(TAG, "位置更新");
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //联网删除
                deleteTest(0, position);
                swipeItem.close();
            }
        });


    }


    @Override
    public int getCount() {
        return datas.size();
    }


    @Override
    public Object getItem(int i) {
        return i;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    private void initWebView(View view, TestBasketBean.TablesBean.TableBean.RowsBean rowsBean) {
        WebView webView1 = (WebView) view.findViewById(R.id.wb_exercises1);


        if (Build.VERSION.SDK_INT >= 19) {
            webView1.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                activity.setTitle("Loading...");
//                activity.setProgress(newProgress * 100);
                if (newProgress == 100) {
//                    activity.setTitle(R.string.app_name);
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


        String stem = rowsBean.getStem();
        String choses = rowsBean.getChoses();
        if (stem != null && !stem.equals("") ){
            String str1 = CommonWay.getHtmlData2(stem + choses);
            webView1.loadData(str1, "text/html; charset=UTF-8", null);
        }else {
            webView1.setVisibility(View.GONE);
        }

    }


    /**
     * 删除试题篮
     * @param flag   标识 0代表删除一个， 1代表清空
     * @param position
     */
    public void deleteTest(final int flag, final int position){

        int basketID = 0;

        if (flag == 0){
            basketID = datas.get(position).getBasketID();
        }

        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();

            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }

            jsonObject.put("BasketID", basketID);
            jsonObject.put("Subject", subject);
            jsonObject.put("GradeName", grade);
            jsonObject.put("TestType", "10");
            jsonObject.put("Flag", flag);
            jsonObject.put("UserCode", userCode);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.DeleteAprBskMN;
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
                                    AlertDialogUtil.showAlertDialog(context, "提示", "删除失败");
                                } else if (resultvalue.equals("-1")) {
                                    String errinfo = jsonObject.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    Log.d("FAILURE_RESULT", errinfo);
                                    AlertDialogUtil.showAlertDialog(context, "提示", "效验失败");
                                }else {
                                    if (flag == 0){
                                        datas.remove(position);
                                        scoreMap.remove(datas.get(position).getBasketID());
                                        itemNoMap.remove(datas.get(position).getBasketID());
                                    }else {
                                        datas.clear();
                                        scoreMap.clear();
                                        itemNoMap.clear();
                                    }
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();

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

    public void setEdListener(View view, final TestBasketBean.TablesBean.TableBean.RowsBean rowsBean){
        final EditText et_score = (EditText) view.findViewById(R.id.et_set_score);

        if (isSuccess){
            et_score.setEnabled(false);
        }else {
            et_score.setEnabled(true);
        }

        final EdLisetener edLisetener = new EdLisetener(et_score, rowsBean);

        //给题目编号
        itemNoMap.put(rowsBean.getBasketID(), datas.indexOf(rowsBean) + 1);

        et_score.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    et_score.addTextChangedListener(edLisetener);

                }else {
                    et_score.removeTextChangedListener(edLisetener);
                }
            }
        });

        if (scoreMap.containsKey(rowsBean.getBasketID())){
            et_score.setText(scoreMap.get(rowsBean.getBasketID()));
        }else {
            et_score.setText("");
        }

    }

    class EdLisetener implements TextWatcher{

        TestBasketBean.TablesBean.TableBean.RowsBean rowsBean;
        EditText editText1;

        public EdLisetener(EditText editText ,TestBasketBean.TablesBean.TableBean.RowsBean rowsBean1){
            rowsBean = rowsBean1;
            editText1 = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String str = editText1.getText().toString().trim();
            scoreMap.put(rowsBean.getBasketID(), str);

            context.setTotalsScore(scoreMap);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    //判断是否全部设置分数
    public int sizeEquals(){
        if (scoreMap.size() == datas.size()){
            return 0;//相等
        }else {
            return -1;//不相等
        }
    }

    //当生成组卷之后 设置分数不能再修改
    public void isCreatSuccess(){

        isSuccess = true;
        notifyDataSetChanged();
    }




    public void showLoading() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox((Activity) context, null);
        }
        loadingBox.Show();
    }

    public void closeLoading() {
        if (loadingBox != null) {
            loadingBox.close();
            loadingBox = null;
        }
    }




}

