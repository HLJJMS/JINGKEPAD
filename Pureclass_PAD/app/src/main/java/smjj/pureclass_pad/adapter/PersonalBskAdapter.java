package smjj.pureclass_pad.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.LessonsBskActivity;
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
 * Created by wlm on 2017/11/29.
 */

public class PersonalBskAdapter extends BaseSwipeAdapter {

    private LessonsBskActivity context;
    private List<TestBasketBean.TablesBean.TableBean.RowsBean> datas = new ArrayList<TestBasketBean.TablesBean.TableBean.RowsBean>();
    private TestBasketBean.TablesBean.TableBean.RowsBean itemDatas = new TestBasketBean.TablesBean.TableBean.RowsBean();
    private LayoutInflater inflater;
    private SharedPreferences spConfig;
    private String classId;
    private String speakId;
    private String grade;
    private String userCode;
    private boolean isCreate;
    private LoadingBox loadingBox;

    public PersonalBskAdapter(LessonsBskActivity context, List<TestBasketBean.TablesBean.TableBean.RowsBean> datas, String speakId1, boolean isCreate1) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        spConfig = context.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        speakId = speakId1;
        isCreate = isCreate1;
        setData(datas);
    }

    public void setData(List<TestBasketBean.TablesBean.TableBean.RowsBean> data) {
        if (data == null) {
//            datas = new ArrayList<>();
            datas.clear();
        } else {
//            this.datas = data;
            this.datas.addAll(data);
        }
        this.notifyDataSetChanged();
    }
    public void setDataAll(List<TestBasketBean.TablesBean.TableBean.RowsBean> activityList) {
            this.datas.clear();
            datas.addAll(activityList);
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

        TextView textView = (TextView) convertView.findViewById(R.id.tv_item_no);
        textView.setText((position + 1) + ".");
        textView.setVisibility(View.VISIBLE);

        if (isCreate){
            ll_side_delete.setVisibility(View.VISIBLE);
        }else {
            ll_side_delete.setVisibility(View.GONE);
        }

        initWebView(convertView, datas.get(position));


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
                itemDatas = datas.get(position);
                deleteTest(position,itemDatas);
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
        WebView webView2 = (WebView) view.findViewById(R.id.wb_exercises2);
        webView1.loadUrl("about:blank");
        webView2.loadUrl("about:blank");

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


        if (Build.VERSION.SDK_INT >= 19) {
            webView2.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView2.setWebChromeClient(new WebChromeClient() {
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
        webView2.setWebViewClient(new GameWebViewClient());
        WebSettings s1 = webView2.getSettings();
        s1.setJavaScriptEnabled(true);

        String stem = rowsBean.getStem();
        String choses = rowsBean.getChoses();
        if (stem != null && !stem.equals("") ){
//            String str1 = CommonWay.getHtmlData2(stem + choses);
            String str1 = CommonWay.getHtmlData2(stem);
            webView1.loadData(str1, "text/html; charset=UTF-8", null);
        }else {
            webView1.setVisibility(View.GONE);
        }

        if (choses != null && !choses.equals("") ){
//            String str1 = CommonWay.getHtmlData2(stem + choses);
            String str2 = CommonWay.getHtmlData3(choses);
            webView2.loadData(str2, "text/html; charset=UTF-8", null);
        }else {
            webView2.setVisibility(View.GONE);
        }
    }


    /**
     * 删除试题篮
     * @param position
     */
    public void deleteTest(final int position, final TestBasketBean.TablesBean.TableBean.RowsBean itemDatas){


        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            JSONObject jsonObject = new JSONObject();
            if (userCode == null || userCode.equals("")) {
                AlertDialogUtil.showAlertDialog(context,"提示","教师编号为空，请重新登录！");
                return;
            }
            jsonObject.put("teacherNo", userCode);
            jsonObject.put("QID", datas.get(position).getID());
            jsonObject.put("SpeakID", speakId);

            final String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.DelePersonalBskMN2;
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
                                    datas.remove(position);
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
//                                   context.setExercType(datas);
//                                    context.setExercData(datas);
                                    context.setDelItem(itemDatas);
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
