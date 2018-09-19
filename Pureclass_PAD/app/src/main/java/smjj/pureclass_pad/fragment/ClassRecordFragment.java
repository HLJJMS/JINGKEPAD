package smjj.pureclass_pad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.activity.GoClassActivity;
import smjj.pureclass_pad.common.SPCommonInfoBean;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wlm on 2017/7/15.
 */
//课堂记录碎片
public class ClassRecordFragment extends Fragment {

    private Context context;
    private EditText et_class_conclusion;
    private Button save_bt;

    private GoClassActivity activity;

    private SharedPreferences spConfig;
    private String classId;
    private String typleUser;
    private String userCode;
    private String userName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        activity = (GoClassActivity) getActivity();
        spConfig = activity.getSharedPreferences(SPCommonInfoBean.SPName, MODE_PRIVATE);
        typleUser = spConfig.getString(SPCommonInfoBean.typeUser,"");
        userCode = spConfig.getString(SPCommonInfoBean.userCode,"");
        classId = spConfig.getString(SPCommonInfoBean.classId,"");
        userName = spConfig.getString(SPCommonInfoBean.userName,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_class_record, container, false);


        return contentView;
    }


}
