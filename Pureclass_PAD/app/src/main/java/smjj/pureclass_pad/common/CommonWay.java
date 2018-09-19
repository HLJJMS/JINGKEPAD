package smjj.pureclass_pad.common;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import smjj.pureclass_pad.beans.InductionTestExercisesBean;
import smjj.pureclass_pad.network.WebServiceUtils;
import smjj.pureclass_pad.util.ConnectionDetector;

/**
 * Created by wlm on 2017/6/16.
 */

public class CommonWay {

    /* 网络检查 */
    public static boolean netWorkCheck(Context context) {// 封装一个联网监测类
        boolean isNetwork = (new ConnectionDetector(context)).isConnectingToInternet();
        return isNetwork;
    }
    /**
     * 解析SoapObject对象 成为json数据
     * @param result
     * @return
     */
    public static JSONObject parseSoapObject(SoapObject result){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.getProperty(0).toString());
            WebServiceUtils.logLength("JSONResult", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 解析SoapObject对象 成为json数据（包含unicode编码的汉字）
     * @param result
     * @return
     */
    public static JSONObject parseSoapObjectUnicode(SoapObject result){
        JSONObject jsonObject = null;
        try {
            String str = decode(result.getProperty(0).toString());
            jsonObject = new JSONObject(str);
            WebServiceUtils.logLength("JSONResult", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 将含有unicode编码的字符串解码并转换成字符串
     * @param unicodeStr
     * @return
     */

    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


    /**
     * 题目用的 将html代码块转成完整的HTML
     * @param bodyHTML
     * @return
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" + "<style>p *{font-size:25px !important;line-height:130%}</style>" +
                "</head>";
        return "<html>" + head + "<body style=\"margin: 0; padding: 0\">" + bodyHTML + "</body></html>";
    }

    /**
     * 题目用的 将html代码块转成完整的HTML上课环节使用
     * @param bodyHTML
     * @return
     */
    public static String getHtmlData2(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" + "<style>span,p,p *{font-size:25px !important;background:#fff48a !important;line-height:130%}</style>" +
                "</head>";
        return "<html>" + head + "<body style=\"margin: 0; padding: 0;background:#fff48a;\">" + bodyHTML + "</body></html>";
    }

    /**
     * 题目用的 将html代码块转成完整的HTML上课环节使用题目选项
     * @param bodyHTML
     * @return
     */
    public static String getHtmlData3(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" + "<style>span,p,p *{font-size:22px !important;background:#fff48a !important;line-height:130%}</style>" +
                "</head>";
        return "<html>" + head + "<body style=\"margin: 0; padding: 0;background:#fff48a;\">" + bodyHTML + "</body></html>";
    }


    /**
     * 题目用的 将html代码块转成完整的HTML
     * @param bodyHTML 带字体颜色
     * @return
     */
    public static String getHtmlData1(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" + "<style>p *{font-size:22px !important;color:#00852f;line-height:130%}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    /**
     * //题目用的 将html代码块解析并添加属性
     * @param str
     * @return
     */
    public static String jsoup(String str) {
        Document doc = (Document) Jsoup.parse(str);
        Elements Img = doc.getElementsByTag("img");  //从一段富文本信息中找到所有图片
        Elements p = doc.getElementsByTag("p"); //从一段富文本信息中找到所有文字
        if (Img.size() != 0) {
            for (Element e_Img : Img) {
                e_Img.attr("width", "auto");//设置图片的宽为100%，高度自适应
                e_Img.attr("height", "auto");
            }
        }
        if (p.size() != 0) {
            for (Element e_p : p) {
                Log.e("xiaoma", e_p.toString());
                e_p.attr("style", "font-size:14px;"); //设置字体的大小
            }
        }
        return doc.toString();
    }


    /**
     * //题目用的 将html代码块解析并添加属性
     * @param str
     * @return
     */
    public static String jsoup1(String str) {
        Document doc = (Document) Jsoup.parse(str);
        Elements Img = doc.getElementsByTag("img");  //从一段富文本信息中找到所有图片
        Elements p = doc.getElementsByTag("p"); //从一段富文本信息中找到所有文字
        if (Img.size() != 0) {
            for (Element e_Img : Img) {
                e_Img.attr("width", "auto");//设置图片的宽为100%，高度自适应
                e_Img.attr("height", "auto");
            }
        }
        if (p.size() != 0) {
            for (Element e_p : p) {
                Log.e("xiaoma", e_p.toString());
                e_p.attr("style", "font-size:14px;color:#f6aa00;line-height:8px;");//设置字体的大小颜色行间距
            }
        }
        return doc.toString();
    }


    /**
     * //题目用的 将html代码块解析并添加属性
     * @param str
     * @return
     */
    public static String jsoup2(String str) {
        Document doc = (Document) Jsoup.parse(str);
        Elements Img = doc.getElementsByTag("img");  //从一段富文本信息中找到所有图片
        Elements p = doc.getElementsByTag("p"); //从一段富文本信息中找到所有文字
        if (Img.size() != 0) {
            for (Element e_Img : Img) {
                e_Img.attr("width", "auto");//设置图片的宽为100%，高度自适应
                e_Img.attr("height", "auto");
            }
        }
        if (p.size() != 0) {
            for (Element e_p : p) {
                Log.e("xiaoma", e_p.toString());
                e_p.attr("style", "font-size:14px;color:#f6aa00;");//设置字体的大小颜色
            }
        }
        return doc.toString();
    }



    /**
     * //题目用的 将html代码块解析并添加属性
     * @param str
     * @return
     */
    public static String jsoup3(String str) {
        Document doc = (Document) Jsoup.parse(str);
        Elements Img = doc.getElementsByTag("img");  //从一段富文本信息中找到所有图片
        Elements p = doc.getElementsByTag("p"); //从一段富文本信息中找到所有文字
        if (Img.size() != 0) {
            for (Element e_Img : Img) {
                e_Img.attr("width", "auto");//设置图片的宽为100%，高度自适应
                e_Img.attr("height", "auto");
            }
        }
        if (p.size() != 0) {//
            for (Element e_p : p) {
                Log.e("xiaoma", e_p.toString());
                e_p.attr("onclick", "this.style.overflow='auto';this.style.whiteSpace='normal';this.style.width='auto';");//设置字体的大小颜色行间距
                e_p.attr("style", "font-size:25px;width:600px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin:0;padding:0;"); //设置字体的大小
                return getHtmlData(e_p.toString());
            }
        }
        return getHtmlData(doc.toString());
    }


    /**
     * //题目用的 将html代码块解析并添加属性 上课用
     * @param str
     * @return
     */
    public static String jsoup4(String str) {
        Document doc = (Document) Jsoup.parse(str);
        Elements Img = doc.getElementsByTag("img");  //从一段富文本信息中找到所有图片
        Elements p = doc.getElementsByTag("p"); //从一段富文本信息中找到所有文字
        if (Img.size() != 0) {
            for (Element e_Img : Img) {
                e_Img.attr("width", "auto");//设置图片的宽为100%，高度自适应
                e_Img.attr("height", "auto");
            }
        }
        if (p.size() != 0) {//
            for (Element e_p : p) {
                Log.e("xiaoma", e_p.toString());
                e_p.attr("onclick", "this.style.overflow='auto';this.style.whiteSpace='normal';this.style.width='auto';");//设置字体的大小颜色行间距
                e_p.attr("style", "font-size:25px;width:600px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;margin:0;padding:0;"); //设置字体的大小
            }
        }
        return getHtmlData(doc.toString());
    }




    /**
     * 入门测，出门考，深化应用，巩固练习排序
     * @param list
     */
    public static void testSort(List<InductionTestExercisesBean.TablesBean.TableBean.RowsBean> list){
        Collections.sort(list, new Comparator<InductionTestExercisesBean.TablesBean.TableBean.RowsBean>() {
            @Override
            public int compare(InductionTestExercisesBean.TablesBean.TableBean.RowsBean rowsBean, InductionTestExercisesBean.TablesBean.TableBean.RowsBean t1) {

                int i = rowsBean.getContentID() - t1.getContentID();
                return i;
            }
        });
    }

    /**
     * 截取文件名称
     * @param fileUrl
     * @return
     */
    @SuppressWarnings("null")
    public static String getFileNameFromUrl(String fileUrl)
    {
        String fileName="";
        int index;
        if(fileUrl!=null || fileUrl.trim()!="")
        {
            index = fileUrl.lastIndexOf("/");
            fileName = fileUrl.substring(index+1, fileUrl.length());
        }
        return fileName;
    }


    //删除文件
    public static void delFile(String fileName){
        File file = new File(fileName);
        if(file.isFile()){
            file.delete();
        }
        file.exists();
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir(String filesDir) {
        File dir = new File(filesDir);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
        }
        dir.delete();// 删除目录本身
    }

}
