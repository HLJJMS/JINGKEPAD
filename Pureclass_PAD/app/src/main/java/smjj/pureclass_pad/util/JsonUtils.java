package smjj.pureclass_pad.util;


import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

    public static boolean isGoodJson(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("code")) {
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
