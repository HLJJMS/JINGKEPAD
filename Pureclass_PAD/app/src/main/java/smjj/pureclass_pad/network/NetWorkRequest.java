package smjj.pureclass_pad.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.util.JsonUtils;

/**
 * Created by wlm on 2017/6/6.
 */

public class NetWorkRequest {

    private static String TAGUrl = "LOG_TWO_POST_Url";
    private static String TAGData = "LOG_TWO_POST_Data";
    private static String TAGResult = "LOG_TWO_POST_Result:";
    private static Handler handler;

    public static Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    //同步请求
    public static String twoPostRequest(final Context context, Handler handler, final String url, String data, String authentication, String token) {
        Log.d(TAGUrl, url);
        logLength(TAGData, data);
        String resultStr = "";
        if (!CommonWay.netWorkCheck(context)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
                }
            });
        }
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("authentication", authentication);
//        Log.i("authentication", authentication);
//        jsonObject.put("token", token);
        try {
            //加密data请求后台
//            String aesString  = AESCoder.encrypt(data, Constants.KEY);
            jsonObject.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //setConnectTimeout 是建立连接的超时时间;
        //setReadTimeout 是传递数据的超时时间;
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);
            OkHttpClient okHttpClient=builder.build();
            MediaType jsonType = MediaType.parse("application/json;charset=UTF-8");
            RequestBody requestBody = RequestBody.create(jsonType, jsonObject.toJSONString());
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resultStr = response.body().string();
                //判断服务器返回
                if (ResultCheck.check(resultStr)) {
                    JSONObject parseObject = JSONObject.parseObject(resultStr);
                    try {
                        //解密整个报文
//                        resultStr = AESCoder.decrypt(parseObject.getString("data"), Constants.KEY);
                        resultStr = parseObject.getString("data");
                        logLength(TAGResult + url, resultStr + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    //同步请求  data为空
    public static String twoPostRequestData(final Context context, String url, String authentication, String token) {
        Log.d(TAGUrl, url);
        String resultStr = "";
        if (!CommonWay.netWorkCheck(context)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
                }
            });
        }

        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("authentication", authentication);
//        jsonObject.put("token", token);
        //setConnectTimeout 是建立连接的超时时间;
        //setReadTimeout 是传递数据的超时时间;
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);
            OkHttpClient okHttpClient=builder.build();
            MediaType jsonType = MediaType.parse("application/json;charset=UTF-8");
            RequestBody requestBody = RequestBody.create(jsonType, jsonObject.toJSONString());
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resultStr = response.body().string();
                //判断服务器返回
                if (ResultCheck.check(resultStr)) {
                    JSONObject parseObject = JSONObject.parseObject(resultStr);
                    try {
                        //解密整个报文
//                        resultStr = AESCoder.decrypt(parseObject.getString("data"), Constants.KEY);
                        resultStr = parseObject.getString("data");
                        logLength(TAGResult + url, resultStr + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public interface RequestCallback {

        void onRequestSuccess(String successString);

        void onRequestFailed(String failedString);
    }

    //异步请求  只返回data
    public static void twoPostTaskRequest(final Context context, final Handler handler, final String url, String data, String authentication, String token, final RequestCallback requestCallback) {
        Log.d(TAGUrl, url);
        logLength(TAGData, data);
        if (!CommonWay.netWorkCheck(context)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    requestCallback.onRequestFailed("哎呀,无网络连接,请检查您的网络设置！");
                    Log.d(TAGResult, "onRequestFailed()");
                }
            });
        }
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("authentication", authentication);
//        jsonObject.put("token", token);
        try {
            //加密data请求后台
//            String aesString = AESCoder.encrypt(data, Constants.KEY);
//            jsonObject.put("data", aesString);
            jsonObject.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient okHttpClient=builder.build();
        MediaType jsonType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(jsonType, jsonObject.toJSONString());
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onRequestFailed("网络连接失败，请重试");
                        //e.toString()e.getMessage();//获取失败原因
                        Log.d(TAGResult, "onRequestFailed()");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String resultStr = "";
                    resultStr = response.body().string();
                    //判断服务器的返回情况
                    if (ResultCheck.check(resultStr)) {
                        JSONObject parseObject = JSONObject.parseObject(resultStr);
                        //解密整个报文
//                        resultStr = AESCoder.decrypt(parseObject.getString("data"), Constants.KEY);
                        resultStr = parseObject.getString("data");
                        logLength(TAGResult + url, resultStr + "");
                        final String finalResultStr = resultStr;
                        if (JsonUtils.isGoodJson(finalResultStr)) {
                            JSONObject jsonObject = JSONObject.parseObject(finalResultStr);
                            final String code = jsonObject.getString("code");
                            final String message = jsonObject.getString("message");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if ("0".equals(code)) {
                                        //有响应请求成功
                                        requestCallback.onRequestSuccess(finalResultStr);
                                        Log.d(TAGResult, "onRequestSuccess()");
                                    } else {
                                        //有响应请求失败,message失败原因
                                        requestCallback.onRequestFailed(message);
                                        Log.d(TAGResult, "onRequestFailed()");
                                    }

                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onRequestFailed("网络连接失败，请重试");
                                    Log.d(TAGResult, "onRequestFailed()");
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onRequestFailed("网络连接失败，请重试");
                                Log.d(TAGResult, "onRequestFailed()");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    //异步请求 直接返回请求成功的所有数据
    public static void twoPostTaskRequest1(final Context context, final Handler handler, final String url, String data, final RequestCallback requestCallback) {
        Log.d(TAGUrl, url);
        logLength(TAGData, data);
        if (!CommonWay.netWorkCheck(context)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    requestCallback.onRequestFailed("哎呀,无网络连接,请检查您的网络设置！");
                    Log.d(TAGResult, "onRequestFailed()");
                }
            });
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        OkHttpClient okHttpClient=builder.build();
        MediaType jsonType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(jsonType, jsonObject.toJSONString());
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onRequestFailed("网络连接失败，请重试");
                        //e.toString()e.getMessage();//获取失败原因
                        Log.d(TAGResult, "onRequestFailed()");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String resultStr = "";
                    resultStr = response.body().string();
                    final String finalResultStr = resultStr;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallback.onRequestSuccess(finalResultStr);
                            logLength(TAGResult + url, finalResultStr + "");
                        }
                    });
            }
        });
    }

    //get请求
    public static String twoGetRequest(final Context context, String url) {
        String resultStr = "";
        if (!CommonWay.netWorkCheck(context)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
                }
            });
        }
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);
            OkHttpClient okHttpClient=builder.build();
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resultStr = response.body().string();
                logLength(TAGResult + url, resultStr + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

//    public static String getToken(String username, String date, String url) {
//        String token = "";
//        String result = username + "|" + date + "|" + url + Constants.SALT;
//        try {
//            token = AESCoder.encrypt(result, Constants.KEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return token;
//    }
//
//    public static String getTime(long cha) {
//        String time = "";
//        time = String.valueOf(System.currentTimeMillis() - cha);
//        return time;
//    }
    //日志打印不全
    public static void logLength(String tag, String str) {
        if (str.length() > 3000) {
            int chunkCount = str.length() / 3000;
            for (int i = 0; i <= chunkCount; i++) {
                if ((i + 1) * 3000 >= str.length()) {
                    Log.d(tag, "result" + i + "~" + chunkCount + " : " + str.substring(3000 * i));
                } else {
                    Log.d(tag, "result" + i + "~" + chunkCount + " : " + str.substring(3000 * i, 3000 * (i + 1)));
                }
            }
        } else {
            Log.d(tag, str + "");
        }
    }


}
