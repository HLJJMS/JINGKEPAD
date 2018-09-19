package smjj.pureclass_pad.common;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import smjj.pureclass_pad.util.LoadingBox;

public class BaseActivity extends AppCompatActivity {
//    public static HomeListenerReceiver homeListenerReceiver = null;
    protected Context mContext;
    private LoadingBox loadingBox;
//
//    public static ImageLoader imageLoader = ImageLoader.getInstance();
//    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageForEmptyUri(R.drawable.launcher1)
//            .showImageOnFail(R.drawable.launcher1)
//            .cacheInMemory(true)
//            .cacheOnDisc(true)
//            .displayer(new RoundedBitmapDisplayer(0)).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
//        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
//        //沉浸式状态栏
//        TranslucentUtils.setTranslucentStatusTwo(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    @Override
    protected void onResume() {
//        JPushInterface.onResume(this);
//        homeListenerReceiver = new HomeListenerReceiver();
//        registerReceiver(homeListenerReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        super.onResume();
    }

    @Override
    protected void onPause() {
//        JPushInterface.onPause(this);
//        unregisterReceiver(homeListenerReceiver);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        if (FormatUtil.isThanTime(this)){
////            Intent intent = new Intent(BaseActivity.this, Login.class);
////            startActivity(intent);
////            ActivityManage.finishAll();
//            //超过三天注销当前账号
//            LogoutManage.instance(this).exit();
//        }
//        FormatUtil.saveExitTime(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 屏蔽菜单按键
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    protected void showLoading() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this,null);
        }
        loadingBox.Show();
    }

    protected void closeLoading() {
        if (loadingBox != null) {
            loadingBox.close();
            loadingBox = null;
        }
    }

    public void showLoading1() {

        if (loadingBox == null) {
            loadingBox = new LoadingBox(this,null);
        }
        loadingBox.Show1();
    }


}
