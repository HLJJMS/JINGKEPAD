package smjj.pureclass_pad.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by dhl on 2016/12/6.
 */
public class ResultCheck {
    public static boolean check(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("resultvalue")) {
            return false;
        }
        try {
            JSONObject.parseObject(str);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
