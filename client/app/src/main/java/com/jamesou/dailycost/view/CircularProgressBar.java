package com.jamesou.dailycost.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressBar extends View {
    private Paint backgroundPaint; // 背景大圆（底色）画笔
    private Paint progressPaint;   // 进度小圆（前景）画笔
    private Paint textPaint;       // 文字画笔
    private RectF rectF;           // 用于绘制圆弧的矩形区域
    private float strokeWidth = 20; // 圆环的宽度
    private float progress = 0;     // 当前进度
    private int max = 100;          // 最大进度值

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(0xFFC0C0C0); // 设置背景圆颜色
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(0xFFFF0000); // 设置进度圆颜色
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF000000); // 设置文字颜色
        textPaint.setTextSize(40); // 设置文字大小
        textPaint.setTextAlign(Paint.Align.CENTER); // 设置文字居中对齐

        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) - strokeWidth / 2f;

        // 绘制背景圆 (大圆)
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        // 使用 rectF 确定小圆（进度圆）的位置和大小
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // 计算当前进度对应的角度
        float angle = 360 * progress / max;

        // 绘制进度圆（小圆）
        canvas.drawArc(rectF, -90, angle, false, progressPaint);

        // 绘制进度文字
        String progressText = String.format("%d%%", (int) (progress / max * 100));
        // 计算Baseline绘制起点Y坐标
        float textY = centerY - (textPaint.descent() + textPaint.ascent()) / 2f;
        canvas.drawText(progressText, centerX, textY, textPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate(); // 请求重绘视图
    }


}
