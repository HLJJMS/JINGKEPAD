package smjj.pureclass_pad.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.common.BaseActivity;
import smjj.pureclass_pad.common.CommonWay;
import smjj.pureclass_pad.util.ActivityManage;
import smjj.pureclass_pad.util.AlertDialogUtil;
import smjj.pureclass_pad.video.widget.FullScreenVideoView;
import smjj.pureclass_pad.video.widget.VideoController;
//视屏播放界面
public class VideoCustomActivity extends BaseActivity implements
        View.OnClickListener, VideoController.onSeekChangeListener {
    private static final String TAG = "VideoCustomActivity";

    private FullScreenVideoView fsvv_content;
    private TextView tv_open;
    private RelativeLayout rl_top;
    private VideoController mb_play;
    private Handler mHandler = new Handler();
    private String file_path = "";
    private ImageView onBack;
    private int enterMark;//1代表授课视屏 2 代表说课视频

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_custom);
        context = this;
        ActivityManage.addActivity(this);
        fsvv_content = (FullScreenVideoView) findViewById(R.id.fsvv_content);
        mb_play = (VideoController) findViewById(R.id.mb_play);
        tv_open = (TextView) findViewById(R.id.tv_open);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        onBack = (ImageView) findViewById(R.id.onBack);

        fsvv_content.prepare(rl_top, mb_play);
        tv_open.setOnClickListener(this);
        mb_play.setonSeekChangeListener(this);
        onBack.setOnClickListener(this);
        //设置快进快退监听器
//        fsvv_content.setOnSetProgressListener(this);
        file_path = getIntent().getStringExtra("fileUrl");
        if (file_path != null && !file_path.equals("")){
            checkPlay();
        }

    }


    private void checkPlay(){
        enterMark = getIntent().getIntExtra("EnterMark",1);
        if (enterMark == 1){
            tv_open.setText("第一讲授课视频");

        }else {
            tv_open.setText("第一讲说课视频");
        }

        if (!file_path.equals("")){
            if (CommonWay.netWorkCheck(context)){
                playVideo(file_path);
            }else {
                AlertDialogUtil.showAlertDialog(context, "温馨提示", "哎呀,无网络连接,请检查您的网络设置！");
            }
        }else {
            AlertDialogUtil.showAlertDialog(context, "温馨提示", "视频网址无效！");
        }
    }



    private void playVideo(String video_path) {
        //		/storage/emulated/0/Download/恋爱谈_hd.mp4
        if (enterMark == 1){
            //网络视频
            Uri uri = Uri.parse(video_path);
            fsvv_content.setVideoURI(uri);
        }else {
            //本地视频
            fsvv_content.setVideoPath(video_path);
        }
//        fsvv_content.setVideoPath(video_path);
        fsvv_content.requestFocus();
        fsvv_content.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                fsvv_content.begin(mp);
                mb_play.setVideoView(fsvv_content);
                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, FullScreenVideoView.HIDE_TIME);
                mHandler.post(refreshRunnable);
            }
        });
        fsvv_content.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                fsvv_content.end(mp);
                mb_play.setCurrentTime(0, 0);
                finish();

            }
        });
        fsvv_content.setOnTouchListener(fsvv_content);
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            fsvv_content.showOrHide();
        }
    };

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (fsvv_content.isPlaying()) {
                mb_play.setCurrentTime(fsvv_content.getCurrentPosition(), fsvv_content.getBufferPercentage());
            }
            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onBack:
//                fsvv_content.start();
//                fsvv_content.stopPlayback();
                finish();
                break;
        }

    }

    @Override
    public void onStartSeek() {
        mHandler.removeCallbacks(hideRunnable);
    }

    @Override
    public void onStopSeek() {
        mHandler.postDelayed(hideRunnable, FullScreenVideoView.HIDE_TIME);
    }


//    /**
//     * 实现快进的接口
//     * @param progress
//     */
//    @Override
//    public void onSetProgress(int progress) {
//        mHandler.removeCallbacks(refreshRunnable);
//        int currentPosition = fsvv_content.getCurrentPosition();
//        int duration = fsvv_content.getDuration();
//        int setProgress = currentPosition + progress*duration/300;
//        if (fsvv_content.isPlaying()){
//            if (setProgress <= 0){
//                fsvv_content.seekTo(0);
//                mb_play.setCurrentTime(0, 0);
//            }else if (setProgress >= duration){
//                fsvv_content.seekTo(duration - 5000);
//                mb_play.setCurrentTime(duration - 5000, 0);
//            }else {
//                fsvv_content.seekTo(setProgress);
//                mb_play.setCurrentTime(setProgress, 0);
//            }
//        }
//        mHandler.post(refreshRunnable);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


}
