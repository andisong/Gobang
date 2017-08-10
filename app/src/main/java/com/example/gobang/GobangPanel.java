package com.example.gobang;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by song_jj on 2017/08/10.
 */

public class GobangPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float radioPieceOfLineHeight = 3 * 1.0f / 4;


    public GobangPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;                                // 取得屏宽
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;    // 求取线间距
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int start = (int) (lineHeight / 2);
            int end = (int) (w - lineHeight / 2);
            int y = (int) ((i + 0.5) * lineHeight);
            canvas.drawLine(start, y, end, y, mPaint);      // 绘制第i条横线
            canvas.drawLine(y, start, y, end, mPaint);      // 绘制第i条竖线
        }
    }
}
