package smjj.pureclass_pad.util;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by wlm on 2017/7/13.
 */

public class OpenFileUtil {

    //android获取一个用于打开PPT文件的intent
    public static Intent getPPTFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String Path)
    {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

//    //android获取一个用于打开PDF文件的intent
//    public static Intent getPdfFileIntent(String path)
//    {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//        Uri uri = Uri.fromFile(new File(path));
//        i.setDataAndType(uri, "application/pdf");
//        return i;
//    }


}
