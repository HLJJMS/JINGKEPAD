package smjj.pureclass_pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wlm on 2018/1/8.
 * 环形图
 */

public class AnnularChart extends View{
    private int mWidth;//整个视图的宽
    private int mHeight;//整个视图的高
    private int mRadius;//半径
    private int centerX;//圆心的X坐标
    private int centerY;//圆心的Y坐标
//    private int yHeight;//Y轴实际高度
//    private int xWidth;//X轴的实际宽度
    private int mStrokeWidth;//线宽
    private int mCorrect = 78;//正确率 值最大是100
    private RectF mRectF;
    private Paint myPaint;
    private Path path;


    public AnnularChart(Context context) {
        super(context);
    }

    public AnnularChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnnularChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize;
        }
        mRadius = 130;
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        mStrokeWidth = 30;
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBarChart(canvas);

    }


    private void setBarChart(Canvas canvas){

        myPaint = new Paint();
        path = new Path();

        myPaint.setAntiAlias(true);
        myPaint.setColor(Color.parseColor("#ff9800")); //设置画笔颜色
        myPaint.setStrokeWidth(30);//设置边框宽度
        myPaint.setStyle(Paint.Style.STROKE);

        canvas.save();
        //绘制初始圆环
        canvas.drawCircle(centerX, centerY, mRadius, myPaint);

        myPaint.setStrokeWidth(40);
        myPaint.setColor(Color.parseColor("#009f39"));
//        mRectF = new RectF(mHalfStrokeWith,mHalfStrokeWith,mRadius*2 + mHalfStrokeWith,mRadius*2 + mHalfStrokeWith);

        mRectF = new RectF(centerX - mRadius - 5, centerY - mRadius - 5, centerX + mRadius + 5, centerY + mRadius + 5);

        float startAngle = (float) (-90 + (100 - mCorrect) * 3.6);
        float sweepAngle = 270 - startAngle;
        canvas.drawArc(mRectF,startAngle, sweepAngle,false,myPaint);

        myPaint.setStrokeWidth(1);
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(30);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText( "正确率 " + mCorrect+"%",centerX - 70,centerY + 5,myPaint);


    }


    public void initView(int correct){
        mCorrect = correct;
        postInvalidate();


    }



}
