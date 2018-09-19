package smjj.pureclass_pad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.common.Constants;
import smjj.pureclass_pad.common.SPCommonInfoBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.MD5;
import smjj.pureclass_pad.util.PopupWindowUtil;
//二期登录页
public class LogoActivity1 extends BaseActivity implements View.OnClickListener {

    private Button log_bt;
    private EditText user_name_et, user_password_et;
    private ImageView password_visible_iv;
    private TextView forget_password_tv;
    private Context context;
    private RelativeLayout rl_bottom_actionbar;
    private LinearLayout ll_stu, ll_parent;

    private SharedPreferences spConfig;

    private String userName;
    private String password;
    private int sing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo1);
        context = this;
        ActivityManage.addActivity(this);
        spConfig = getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);

        findView();

        isLog();
    }

    private void findView() {
        log_bt = (Button) findViewById(R.id.log_bt);
        user_name_et = (EditText) findViewById(R.id.user_name_et);
        user_password_et = (EditText) findViewById(R.id.password_et);
        password_visible_iv = (ImageView) findViewById(R.id.password_visible_iv);
        forget_password_tv = (TextView) findViewById(R.id.forget_password_tv);
        ll_stu = (LinearLayout) findViewById(R.id.ll_stu);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);


        log_bt.setOnClickListener(this);
        forget_password_tv.setOnClickListener(this);
        ll_stu.setOnClickListener(this);
        ll_parent.setOnClickListener(this);


        password_visible_iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        user_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        user_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    default:
                        break;
                }
                user_password_et.postInvalidate();

                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.log_bt:
                loging();
                break;

            case R.id.ll_stu:
                sing = 1;
                showStudentPopu();
                break;
            case R.id.ll_parent:
                sing = 2;
                showStudentPopu();
                break;
            case R.id.forget_password_tv:
                break;

        }
    }

    /**
     * 登录,点登录按钮运行
     */
    public void loging() {
        // 账号验证
        userName = user_name_et.getText().toString().trim();
        password = user_password_et.getText().toString().trim();
        if (CommonWay.netWorkCheck(context)) { // 在有网络的情况下登入
            if (userName.equals("") || password.equals("")) {
                Toast.makeText(context, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            // 只有验证正确的账号才能记录到本地
            log_bt.setText("正在登录……");
            log_bt.setClickable(false);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("pwd", password);
            jsonObject.put("userName", userName);

            String jsonCode = jsonObject.toJSONString();
            String MD5Result = MD5.stringMd5(jsonCode + Constants.key);
            String methodName = Constants.LogMethodName;
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
                        org.json.JSONObject jsonObject1 = CommonWay.parseSoapObject(result);
                        if (jsonObject1 != null) {
                            try {
                                String code = jsonObject1.getString("resultvalue");
                                if (code.equals("0")) {
                                    saveInfo(jsonObject1);
                                    Intent intent = new Intent(context, MainActivity1.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String errinfo = jsonObject1.getString("errinfo");
                                    //校验失败 是指token校验失败
                                    AlertDialogUtil.showAlertDialog(context, "提示", "用户名密码错误");
                                    log_bt.setText("登  录");
                                    log_bt.setClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                                log_bt.setText("登  录");
                                log_bt.setClickable(true);
                            }
                        } else {
                            AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                            log_bt.setText("登  录");
                            log_bt.setClickable(true);
                        }

                    } else {
                        AlertDialogUtil.showAlertDialog(context, "提示", "网络连接失败，请重试");
                        log_bt.setText("登  录");
                        log_bt.setClickable(true);
                    }
                }
            });
        } else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
        }
    }

    /**
     * 保存用户信息
     * @param jsonObject
     */
    private void saveInfo(org.json.JSONObject jsonObject) {
        SharedPreferences.Editor editor = spConfig.edit();
        try {
            editor.putString(SPCommonInfoBean.loginName, userName);
            editor.putString(SPCommonInfoBean.passWord, password);
            editor.putString(SPCommonInfoBean.userName, jsonObject.getString("UserName"));
            editor.putString(SPCommonInfoBean.userCode, jsonObject.getString("UserCode"));
            editor.putString(SPCommonInfoBean.userType, jsonObject.getString("UserType"));
            editor.putString(SPCommonInfoBean.userDepartNo, jsonObject.getString("UserDepartNo"));
            editor.putString(SPCommonInfoBean.departName, jsonObject.getString("DepartName"));
            editor.putString(SPCommonInfoBean.sex, jsonObject.getString("Sex"));
            editor.putString(SPCommonInfoBean.classYear, jsonObject.getString("ClassYear"));
            editor.putString(SPCommonInfoBean.xueDuan, jsonObject.getString("XueDuan"));
            editor.putString(SPCommonInfoBean.typeUser, jsonObject.getString("TypeUser"));
            editor.putString(SPCommonInfoBean.isCertificate, jsonObject.getString("IsCertificate"));
//            editor.putString(SPCommonInfoBean.isCertificate, "0");
            editor.putString(SPCommonInfoBean.isDualTeacher, "1");

            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //判断要不要再走登录页
    public void isLog(){
        userName = spConfig.getString(SPCommonInfoBean.loginName, "");
        password = spConfig.getString(SPCommonInfoBean.passWord, "");
        String userCode = spConfig.getString(SPCommonInfoBean.userCode, "");
        if (!userCode.equals("") && !userName.equals("") && !password.equals("")){
            Intent intent = new Intent(context, MainActivity1.class);
            startActivity(intent);
            finish();

        }
    }

    private void showStudentPopu(){

        View view = LayoutInflater.from(context).inflate(R.layout.pop_qr_view, null);

        final PopupWindow popupWindow = PopupWindowUtil.getPopuWindow(context,view,2,2,0.5f,-1);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_qr);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.iv_close_pop);
        TextView textView = (TextView) view.findViewById(R.id.tv_student_list);
        if (sing == 1){
            textView.setText("精课·学生端二维码");
            imageView.setImageResource(R.drawable.xuexi);
        }else if (sing == 2){
            textView.setText("精课·家长端二维码");
            imageView.setImageResource(R.drawable.jiaxiao);
        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });


        popupWindow.showAtLocation(ll_stu, Gravity.CENTER,0,0);

    }




}
