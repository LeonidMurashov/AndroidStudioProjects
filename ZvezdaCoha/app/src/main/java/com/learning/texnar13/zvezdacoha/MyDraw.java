package com.learning.texnar13.zvezdacoha;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyDraw extends View {

    int w;
    int h;
    Paint paint = new Paint();
    float currentX = 0;
    float currentY = 0;
    int level = 0;

    public MyDraw(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldW, oldH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        currentX = w / 4;
        currentY = h / 2 + ((w / 2) * 0.866f) / 3;
        canvas.rotate(-60, currentX, currentY);
        drawCurve(canvas, w / 2, level);
        canvas.rotate(120, currentX, currentY);
        drawCurve(canvas, w / 2, level);
        canvas.rotate(120, currentX, currentY);
        drawCurve(canvas, w / 2, level);
    }

    protected void redraw() {
        invalidate();
    }

    private void drawCurve(Canvas canvas, float size, float level) {
        float temp = currentX;
        if (level == 0) {
            canvas.drawLine(currentX, currentY, currentX + size, currentY, paint);
            currentX = currentX + size;
        } else {
            drawCurve(canvas, size / 3, level - 1);
            canvas.rotate(-60, currentX, currentY);
            drawCurve(canvas, size / 3, level - 1);
            canvas.rotate(-240, currentX, currentY);
            drawCurve(canvas, size / 3, level - 1);
            canvas.rotate(-60, currentX, currentY);
            drawCurve(canvas, size / 3, level - 1);
        }
    }
}
