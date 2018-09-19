package smjj.pureclass_pad.util;
/**
 * 网络连接监测
 **/

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    public static String getNetType(Context context) {
        String type = "";
        if (context == null) {
            return type;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return "wifi";
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    int subtype = activeNetworkInfo.getSubtype();
                    switch (subtype) {
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                            type = "CDMA";
                            break;
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            type = "1xRTT";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            type = "UNKNOWN";
                            break;
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                            type = "GPRS";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            type = "EDGE";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                            type = "UMTS";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            type = "EVDO_0";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            type = "EVDO_A";
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                            type = "HSUPA";
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                            type = "HSDPA";
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                            type = "HSPA";
                            break;
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            type = "IDEN";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            type = "EVDO_B";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            //联通4g
                            type = "LTE";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                            type = "EHRPD";
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            type = "HSPAP";
                            break;
                    }

                }
            }
        }


        return type;
    }

    /**
     * 获取运营商
     * @param context
     *
     */
    public static String getMobileOperator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperatorName = telephonyManager.getNetworkOperatorName();
        String simOperator = telephonyManager.getSimOperator();
        Log.d("TAG",simOperator+"");
        if (!TextUtils.isEmpty(simOperator)) {
            if (simOperator.equals("46000") || simOperator.equals("46002") || simOperator.equals("46007")) {

                return "中国移动";

            } else if (simOperator.equals("46001")) {

                return "中国联通";

            } else if (simOperator.equals("46003")) {

                return "中国电信";

            }
        }
        return networkOperatorName;
    }

    public static String getDeviceSoftWareVersion(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceSoftWareVersion = telephonyManager.getDeviceSoftwareVersion();
        return deviceSoftWareVersion;
    }


}