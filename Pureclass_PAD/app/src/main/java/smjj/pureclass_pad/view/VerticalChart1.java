package smjj.pureclass_pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.util.UnitUtil;

/**
 * Created by wlm on 2018/6/1.
 * 即时问答选项柱状图
 */

public class VerticalChart1 extends View{

    private int mWidth;//整个视图的宽
    private int mHeight;//整个视图的高
    private int barWidth;//柱状条的宽度
    private float bottomHeight;//X轴距离视图底部的高度
    private int leftWidth;//Y轴距离视图左侧的宽度
    private int yHeight;//Y轴实际高度
    private int xWidth;//X轴的实际宽度
    private float topHeight;//Y轴距离顶部的高度
    private Context context;

    //yTextList和percentList大小必须一致，且相对应
    private List<Integer> yTextList = new ArrayList<>();//知识点名字
    private List<Integer> percentList = new ArrayList<>();//知识点相应的分数
    public String mRightAnswers;//正确答案

    private Paint myPaint;
    private Path path;

    public VerticalChart1(Context context) {

        super(context);
        context = context;
    }

    public VerticalChart1(Context context, AttributeSet attrs) {
        super(context, attrs);
        context = context;
    }

    public VerticalChart1(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        context = context;

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
        barWidth = 30;
        bottomHeight = UnitUtil.dip2px(getContext(), 35);
        leftWidth = 80;
        topHeight = 40;
        yHeight = (int) (mHeight - bottomHeight);
        xWidth = mWidth - leftWidth;

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
        myPaint.setColor(Color.parseColor("#666666")); //设置画笔颜色
        myPaint.setTextSize(30);//设置文字大小

        canvas.save();
        //绘制坐标轴
        myPaint.setStrokeWidth(1);
//        canvas.drawLine(leftWidth, yHeight, mWidth, yHeight, myPaint);
        int[] array1 = new int[]{0,10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("f");

        for (int i = 0; i < 5; i++) {//绘制横坐标文字
            myPaint.setColor(Color.parseColor("#000000")); //设置画笔颜色
            myPaint.setTextSize(30);//设置文字大小
            int leftX = (xWidth/6)*(i+1)+leftWidth-20;
            int topY = (int) yHeight + 10;
            int rightX = (xWidth/6)*(i+1)+leftWidth+35;
            int bottomY = yHeight + 40;

            if (i == 4){
                myPaint.setTextSize(23);//设置文字大小
                myPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(new RectF(leftX,topY,rightX + 23,bottomY),5,5,myPaint);
                myPaint.setColor(Color.parseColor("#000000")); //设置画笔颜色
                canvas.drawText("未提交", (xWidth/6)*(i+1)+leftWidth-15, yHeight+34, myPaint);

            }else {
                String answers = list.get(i);
                if (answers.equals(mRightAnswers)){

                    myPaint.setColor(Color.parseColor("#009f39")); //设置画笔颜色
                    canvas.drawRoundRect(new RectF(leftX,topY,rightX,bottomY),5,5,myPaint);

                    myPaint.setColor(Color.WHITE);
                    canvas.drawText(list.get(i), (xWidth/6)*(i+1)+leftWidth, yHeight+36, myPaint);
                }else {
                    myPaint.setColor(Color.WHITE);
                    canvas.drawRoundRect(new RectF(leftX,topY,rightX,bottomY),5,5,myPaint);
                    myPaint.setColor(Color.parseColor("#000000")); //设置画笔颜色
                    canvas.drawText(list.get(i), (xWidth/6)*(i+1)+leftWidth, yHeight+36, myPaint);
                }
            }
        }

//
        for (int i = 0; i < percentList.size(); i ++){
            int percent = percentList.get(i);
            String answers1 = list.get(i);
            if (answers1.equals(mRightAnswers)){
                myPaint.setColor(Color.parseColor("#009f39"));
            }else {
                myPaint.setColor(Color.parseColor("#d8d8d8"));
            }

            int leftX = (xWidth/6)*(i+1)+leftWidth-10;
            int topY = (int) (topHeight+((100-percent)*((yHeight-topHeight)/100)));
            int rightX = (xWidth/6)*(i+1)+leftWidth+25;
            int bottomY = yHeight;
            canvas.drawRect(new Rect(leftX,topY,rightX,bottomY),myPaint);

            myPaint.setColor(Color.parseColor("#000000")); //设置画笔颜色
            myPaint.setTextSize(25);//设置文字大小
            canvas.drawText(yTextList.get(i) + "人", leftX, topY-10,myPaint);

        }

    }

    public void initView(List<Integer> yList, List<Integer> percentList1, String mRightAnswers1){
        yTextList.clear();
        percentList.clear();
        yTextList.addAll(yList);
        percentList.addAll(percentList1);
        mRightAnswers = mRightAnswers1;

        postInvalidate();

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (y > yHeight && y < yHeight+36 + 100){
                    if (x >(xWidth/6)+leftWidth-30 && x < (xWidth/6)+leftWidth+ 30){
                        mRightAnswers = "A";
                        postInvalidate();
                        return true;

                    }

                    if (x >(xWidth/6)*2+leftWidth-30 && x < (xWidth/6)*2+leftWidth+ 30){
                        mRightAnswers = "B";
                        postInvalidate();
                        return true;

                    }

                    if (x >(xWidth/6)*3+leftWidth-30 && x < (xWidth/6)*3+leftWidth+ 30){
                        mRightAnswers = "C";
                        postInvalidate();
                        return true;

                    }

                    if (x >(xWidth/6)*4+leftWidth-30 && x < (xWidth/6)*4+leftWidth+ 30){
                        mRightAnswers = "D";
                        postInvalidate();
                        return true;

                    }

                }


                break;
            default:
                break;
        }

        return super.onTouchEvent(event);

    }
}
