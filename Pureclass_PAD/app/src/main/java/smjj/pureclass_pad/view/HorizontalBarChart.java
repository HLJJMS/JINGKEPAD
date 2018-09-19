package smjj.pureclass_pad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import smjj.pureclass_pad.util.UnitUtil;

/**
 * Created by wlm on 2017/8/10.
 */

public class HorizontalBarChart extends View {

    private int mWidth;//整个视图的宽
    private int mHeight;//整个视图的高
    private float barHeight;//柱状条的高度
    private float bottomHeight;//X轴距离视图底部的高度
    private int leftWidth;//Y轴距离视图左侧的宽度
    private int yHeight;//Y轴实际高度
    private int xWidth;//X轴的实际宽度

    //yTextList和percentList大小必须一致，且相对应
    private List<String> yTextList = new ArrayList<>();//知识点名字
    private List<Integer> percentList = new ArrayList<>();//知识点相应的分数

    private Paint myPaint;
    private Path path;

    public HorizontalBarChart(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public HorizontalBarChart(Context context, AttributeSet attr) {
        super(context,attr);
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
        barHeight = UnitUtil.dip2px(getContext(),25);
        bottomHeight = UnitUtil.dip2px(getContext(), 20);
        leftWidth = 150;
        yHeight = (int) (mHeight - barHeight);
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
        myPaint.setColor(Color.BLACK); //设置画笔颜色
        myPaint.setTextSize(18);//设置文字大小

        canvas.save();
        //绘制坐标轴
        myPaint.setStrokeWidth(1);
        canvas.drawLine(leftWidth, 0, leftWidth, yHeight, myPaint);//纵坐标轴
        canvas.drawLine(leftWidth, yHeight, mWidth, yHeight, myPaint);

        int[] array1 = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        int[] array3 = new int[]{5, 15, 25, 35, 45,55, 65, 75, 85, 95};
//        String[] array2 = new String[]{"知识点一知识点一"};

        //绘制横坐标文字
        for (int i = 0; i < array1.length; i++) {
            canvas.drawLine(((xWidth)/100) * array1[i] + leftWidth, yHeight, ((xWidth)/100) * array1[i] + leftWidth, yHeight - 10, myPaint);
            canvas.drawText(array1[i] + "", ((xWidth)/100) * array1[i] - 10 + leftWidth, mHeight, myPaint);
        }
        for (int i = 0; i < array3.length; i++) {
            canvas.drawLine(((xWidth)/100) * array3[i] + leftWidth, yHeight, ((xWidth)/100) * array3[i] + leftWidth, yHeight - 5, myPaint);
        }

        //绘制纵坐标文字
        myPaint.setTextSize(18);
        for (int i = 0; i < yTextList.size(); i++) {
            canvas.drawText(yTextList.get(i), 0, yHeight - barHeight * ((i + 1)*2 - 1) - 10, myPaint);
        }

        //画Y轴小三角形
        path.moveTo(leftWidth, 0);
        path.lineTo(leftWidth - 4, 5);
        path.lineTo(leftWidth + 4, 5);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, myPaint);

        path.reset();
        //画X轴小三角形
        path.moveTo(mWidth, yHeight);
        path.lineTo(mWidth - 5, yHeight -4);
        path.lineTo(mWidth - 5, yHeight + 4);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, myPaint);


        //绘制条形图
        myPaint.setStyle(Paint.Style.FILL); //设置填充

        for (int i = 0; i < percentList.size(); i ++){
            int percent = percentList.get(i);

            if (percent <= 50){
                myPaint.setColor(Color.parseColor("#ff0508")); //设置画笔颜色
            }else if ( percent >= 50 && percent < 70){
                myPaint.setColor(Color.parseColor("#ffdd00"));
            }else if (percent >= 70){
                myPaint.setColor(Color.parseColor("#00852f"));
            }
            canvas.drawRect(new Rect(leftWidth, (int) (yHeight - barHeight * 2 * (i + 1)), (xWidth/100) * percentList.get(i) + leftWidth, (int) (yHeight - barHeight * (2 * (i + 1) - 1))), myPaint);
        }

    }

    public void initView(List<String> yList, List<Integer> percentList1){
        yTextList.clear();
        percentList.clear();
        yTextList.addAll(yList);
        percentList.addAll(percentList1);

        mHeight = (int) ((percentList1.size() * 2 + 1)* barHeight + bottomHeight);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();;
        params.height = mHeight;
        this.setLayoutParams(params);
        postInvalidate();

    }

}
