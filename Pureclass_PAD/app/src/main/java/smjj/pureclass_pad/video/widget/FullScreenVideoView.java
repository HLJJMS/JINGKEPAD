package smjj.pureclass_pad.video.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.VideoView;

import smjj.pureclass_pad.R;
import smjj.pureclass_pad.util.UnitUtil;
import smjj.pureclass_pad.video.util.VolumnManager;


//支持以下功能：自动全屏、调节音量、收缩控制栏、设置背景
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FullScreenVideoView extends VideoView implements OnTouchListener {

	private Context mContext;
	private AudioManager mAudioManager;
	private VolumnManager mVolumnManager;
	private int screenWidth;
	private int screenHeight;
	private int videoWidth;
	private int videoHeight;
	private int realWidth;
	private int realHeight;

	private float mLastMotionX;
	private float mLastMotionY;
	private int startX;
	private int startY;
	private int threshold;
	private boolean isClick = true;
	// 自动隐藏顶部和底部View的时间
	public static final int HIDE_TIME = 3000;

	private View mTopView;
	private View mBottomView;
	private Handler mHandler = new Handler();

	public FullScreenVideoView(Context context) {
		this(context, null);
	}

	public FullScreenVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mVolumnManager = new VolumnManager(mContext);
		screenWidth = UnitUtil.getWidthInPx(mContext);
		screenHeight = UnitUtil.getHeightInPx(mContext);
		threshold = UnitUtil.dip2px(mContext, 18);
	}

	private void volumeDown(float delatY) {
		int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int down = (int) (delatY / screenHeight * max * 3);
		int volume = Math.max(current - down, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
		int transformatVolume = volume * 100 / max;
		mVolumnManager.show(transformatVolume);
	}

	private void volumeUp(float delatY) {
		int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int up = (int) ((delatY / screenHeight) * max * 3);
		int volume = Math.min(current + up, max);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
		int transformatVolume = volume * 100 / max;
		mVolumnManager.show(transformatVolume);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(realWidth, widthMeasureSpec);
		int height = getDefaultSize(realHeight, heightMeasureSpec);
		if (realWidth > 0 && realHeight > 0) {
			if (realWidth * height > width * realHeight) {
				height = width * realHeight / realWidth;
			} else if (realWidth * height < width * realHeight) {
				width = height * realWidth / realHeight;
			}
		}
		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			startX = (int) x;
			startY = (int) y;
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaX = x - mLastMotionX;
			float deltaY = y - mLastMotionY;
			float absDeltaX = Math.abs(deltaX);
			float absDeltaY = Math.abs(deltaY);
			// 声音调节标识
			boolean isAdjustAudio = false;
			if (absDeltaX > threshold && absDeltaY > threshold) {
				if (absDeltaX < absDeltaY) {
					isAdjustAudio = true;
				} else {
					isAdjustAudio = false;
				}
			} else if (absDeltaX < threshold && absDeltaY > threshold) {
				isAdjustAudio = true;
			} else if (absDeltaX > threshold && absDeltaY < threshold) {
				isAdjustAudio = false;
			} else {
				return true;
			}
			if (isAdjustAudio) {
				if (deltaY > 0) {
					volumeDown(absDeltaY);
				} else if (deltaY < 0) {
					volumeUp(absDeltaY);
				}
			}

			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
//			float deltaX1 = x - startX;
//			float deltaY1 = y - startY;
//			float absDeltaX1 = Math.abs(x - startX);
//			float absDeltaY1 = Math.abs(y - startY);
//			//快进快退标识
//			boolean isProgressAddOrReduce = false;
//			if (absDeltaX1 > threshold && absDeltaY1 > threshold) {
//				if (absDeltaX1 < absDeltaY1) {
//					isProgressAddOrReduce = false;
//				} else {
//					isProgressAddOrReduce = true;
//				}
//			} else if (absDeltaX1 < threshold && absDeltaY1 > threshold) {
//				isProgressAddOrReduce = false;
//			} else if (absDeltaX1 > threshold && absDeltaY1 < threshold) {
//				isProgressAddOrReduce = true;
//			}
//			if (isProgressAddOrReduce){
//				if (deltaX1 > 0) {
//					onSetProgressListener.onSetProgress((int) (absDeltaX1*100/screenWidth));
//				} else if (deltaX1 < 0) {
//					onSetProgressListener.onSetProgress((int) -(absDeltaX1*100/screenWidth));
//				}
//			}


			if (Math.abs(x - startX) > threshold || Math.abs(y - startY) > threshold) {
				isClick = false;
			}
			mLastMotionX = 0;
			mLastMotionY = 0;
			startX = (int) 0;
			if (isClick) {
				showOrHide();
			}
			isClick = true;
			break;
		default:
			break;
		}
		return true;
	}
	
	public void prepare(View topTiew, View bottomView) {
		mTopView = topTiew;
		mBottomView = bottomView;
		setBackgroundResource(R.drawable.video_bg1);
	}

	public void begin(MediaPlayer mp) {
		setBackground(null);
		if (mp != null) {
			videoWidth = mp.getVideoWidth();
			videoHeight = mp.getVideoHeight();
		}
		realWidth = videoWidth;
		realHeight = videoHeight;
		start();
	}

	public void end(MediaPlayer mp) {
		setBackgroundResource(R.drawable.video_bg3);
		realWidth = screenWidth;
		realHeight = screenHeight;
	}

	public void showOrHide() {
		if (mTopView==null || mBottomView==null) {
			return;
		}
		if (mTopView.getVisibility() == View.VISIBLE) {
			mTopView.clearAnimation();
			Animation animTop = AnimationUtils.loadAnimation(mContext, R.anim.leave_from_top);
			animTop.setAnimationListener(new AnimationImp() {
				@Override
				public void onAnimationEnd(Animation animation) {
					mTopView.setVisibility(View.GONE);
				}
			});
			mTopView.startAnimation(animTop);

			mBottomView.clearAnimation();
			Animation animBottom = AnimationUtils.loadAnimation(mContext, R.anim.leave_from_bottom);
			animBottom.setAnimationListener(new AnimationImp() {
				@Override
				public void onAnimationEnd(Animation animation) {
					mBottomView.setVisibility(View.GONE);
				}
			});
			mBottomView.startAnimation(animBottom);
		} else {
			mTopView.setVisibility(View.VISIBLE);
			mTopView.clearAnimation();
			Animation animTop = AnimationUtils.loadAnimation(mContext, R.anim.entry_from_top);
			mTopView.startAnimation(animTop);

			mBottomView.setVisibility(View.VISIBLE);
			mBottomView.clearAnimation();
			Animation animBottom = AnimationUtils.loadAnimation(mContext, R.anim.entry_from_bottom);
			mBottomView.startAnimation(animBottom);
			mHandler.removeCallbacks(hideRunnable);
			mHandler.postDelayed(hideRunnable, HIDE_TIME);
		}
	}

	private Runnable hideRunnable = new Runnable() {
		@Override
		public void run() {
			showOrHide();
		}
	};

	private class AnimationImp implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

	}

	/**
	 * 设置快进快退的接口
	 * @param onSetProgressListener
     */
	public void setOnSetProgressListener(FullScreenVideoView.onSetProgressListener onSetProgressListener) {
		this.onSetProgressListener = onSetProgressListener;
	}

	private FullScreenVideoView.onSetProgressListener onSetProgressListener;

	public static interface onSetProgressListener {
		public void onSetProgress(int progress);
	}

}
