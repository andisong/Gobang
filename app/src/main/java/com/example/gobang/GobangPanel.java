package com.example.gobang;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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

    //白棋先下，当前轮到白棋
    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<Point>();
    private List<Point> mBlackArray = new ArrayList<Point>();


    //棋盘上两方棋子的标志  0 无子  ；           1  我方 ；              2   电脑
    private int[][] GobangBoard = new int[10][10];

    public GobangPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    // 初始化
    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);
    }

    // 根据屏幕大小，获取屏幕中间正方形空间尺寸
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        // 设置正方形棋盘容器大小区域
        setMeasuredDimension(width, width);
    }

    //当宽高尺寸确定发生改变以后回调此函数
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;                                // 取得屏宽
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;    // 求取线间距
        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制棋盘
        drawBoard(canvas);

        //绘制棋子
        drawPieces(canvas);
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


    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    null);
        }

        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 - radioPieceOfLineHeight) / 2 * mLineHeight),
                    (blackPoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);
            int m = p.x;
            int n = p.y;

            if (mIsWhite) {
                mWhiteArray.add(p);
//                GobangBoard[m][n] = 1;      //  0：无子 1:我方 2：对方
            }
            invalidate();
            return true;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }
}
