package smjj.pureclass_pad.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangliming on 2017/6/18.
 */

public class WebServiceUtils {

    // 含有3个线程的线程池
    private static final ExecutorService executorService = Executors
            .newFixedThreadPool(3);

    // 命名空间
    private static final String NAMESPACE = "http://tempuri.org/";
//    private static final String NAMESPACE = "http://WebXml.com.cn/";

    private static final String LOGMethodName = "LOGMethodName";
    private static final String LOGPostData = "LOGPostData";
    private static final String LOGResult = "LOGResult";

    /**
     *
     * @param url
     *            WebService服务器地址
     * @param methodName
     *            WebService的调用方法名
     * @param properties
     *            WebService的参数
     * @param webServiceCallBack
     *            回调接口
     */
    public static void callWebService(final String url, final String methodName,
                                      HashMap<String, String> properties,
                                      final WebServiceCallBack webServiceCallBack) {
        // 创建HttpTransportSE对象，传递WebService服务器地址
//        final HttpTransportSE httpTransportSE = new HttpTransportSE(url, );
        final HttpTransportSE httpTransportSE = new HttpTransportSE(url, 1000*60*2);
        // 创建SoapObject对象
        final SoapObject soapObject = new SoapObject(NAMESPACE, methodName);

        // SoapObject添加参数
        if (properties != null) {
            for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                    .iterator(); it.hasNext();) {
                Map.Entry<String, String> entry = it.next();
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }

        // 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        // 设置是否调用的是.Net开发的WebService
        soapEnvelope.setOutputSoapObject(soapObject);
        soapEnvelope.dotNet = true;
        httpTransportSE.debug = true;

        // 用于子线程与主线程通信的Handler
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 将返回值回调到callBack的参数中
                webServiceCallBack.callBackSuccess((SoapObject) msg.obj);
            }

        };

        // 开启线程去访问WebService
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                SoapObject resultSoapObject = null;
                Log.d(LOGMethodName,methodName + url);
                Log.d(LOGPostData,soapObject.toString());

                try {
                    httpTransportSE.call(NAMESPACE + methodName, soapEnvelope);
                    if (soapEnvelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                        logLength(LOGResult,resultSoapObject.toString());
                    }
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } finally {
                    // 将获取的消息利用Handler发送到主线程
                    mHandler.sendMessage(mHandler.obtainMessage(0,
                            resultSoapObject));
                }
            }
        });
    }

    public interface WebServiceCallBack {
        public void callBackSuccess(SoapObject result);
    }

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
