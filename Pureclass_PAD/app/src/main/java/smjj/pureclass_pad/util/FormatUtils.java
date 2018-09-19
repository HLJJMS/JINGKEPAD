package smjj.pureclass_pad.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import smjj.pureclass_pad.beans.SchedulingTableBean;

/**
 * Created by wlm on 2017/7/4.
 */

public class FormatUtils {

    /**
     * 字符串时间转化为Calendar对象
     * @param timeStr "yyyy-MM-dd"
     * @return
     */
    public static Calendar getCalendarFromString(String timeStr) {
        Log.i("TAGDATE", timeStr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar mycalendar1 = Calendar.getInstance();
        try {
            Date parse = sdf.parse(timeStr);
            mycalendar1.setTime(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mycalendar1;
    }

    /**
     * 字符串时间转化为Calendar对象
     *
     * @param timeStr "yyyy-MM-dd HH:mm"
     * @return
     */
    public static Calendar getCalendarFromString1(String timeStr, String formatstr) {
        Log.i("TAGDATE", timeStr);
        SimpleDateFormat sdf = new SimpleDateFormat(formatstr);
        Calendar mycalendar1 = Calendar.getInstance();
        try {
            Date parse = sdf.parse(timeStr);
            mycalendar1.setTime(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mycalendar1;
    }
//    mycalendar1.get(Calendar.DAY_OF_WEEK) == 1

    public static String getWeekDay(Calendar calendar){
        String week = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                week = "周日";
                break;
            case 2:
                week += "周一";
                break;
            case 3:
                week += "周二";
                break;
            case 4:
                week += "周三";
                break;
            case 5:
                week += "周四";
                break;
            case 6:
                week += "周五";
                break;
            case 7:
                week += "周六";
                break;
            default:
                break;
        }
        return week;
    }


    /**
     * 获取当天时间
     * @return
     */
    public static String getToday() {
        final long times = System.currentTimeMillis();
        Date mydate = new Date(times); // 获取当前日期Date对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String buy_date = sdf.format(mydate);
        return buy_date;
    }


    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime() {
        final long times = System.currentTimeMillis();
        Date mydate = new Date(times); // 获取当前日期Date对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String buy_date = sdf.format(mydate);
        return buy_date;
    }

    /**
     * 格式化时间字符串
     * @return
     */
    public static String getStringTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format); //设置时间格式
        String buy_date = sdf.format(date);
        return buy_date;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return formatter.format(new Date(time));
    }

    /**
     * 获取过去第几天的日期
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     *对集合按日期排序
     */
    public static void getListRank(List<SchedulingTableBean.TablesBean.TableBean.RowsBean> list) {


        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Collections.sort(list, new Comparator<SchedulingTableBean.TablesBean.TableBean.RowsBean>() {
            @Override
            public int compare(SchedulingTableBean.TablesBean.TableBean.RowsBean rowsBean, SchedulingTableBean.TablesBean.TableBean.RowsBean rowsBean1) {
                int mark = -1;
                try {

                    if (rowsBean.getTimeStar() == null && rowsBean.getTimeStar().equals("")){
                        return mark;
                    }
                    if (rowsBean.getTimeStar() == null && rowsBean.getTimeStar().equals("")){
                        return mark;
                    }
                    Date date0 = sdf.parse(rowsBean.getTimeStar());
                    Date date1 = sdf.parse(rowsBean1.getTimeStar());
                    if(date0.getTime() > date1.getTime()){
                        mark =  1;
                    }
                    if(rowsBean.getTimeStar().equals(rowsBean1.getTimeStar())){
                        mark =  0;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return mark;
             //compare
            }
        });
    }
}
