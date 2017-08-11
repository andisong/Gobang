package com.example.gobang;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song_jj on 2017/08/10.
 */

public class GobangPanel extends View {
    private final String TAG = "TAG";

    private int mPanelWidth;
    private float mLineHeight;
    private final int FIVE = 5;
    private final int MAX_LINE = 10;
    private final int MAX_CNT = (FIVE-1) * (int) Math.pow(MAX_LINE, 2);

    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float radioPieceOfLineHeight = 3 * 1.0f / 4;

    //白棋先下，当前轮到白棋
    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<Point>();
    private List<Point> mBlackArray = new ArrayList<Point>();

    //赢法数组
    int[][][] wins = new int[MAX_LINE][MAX_LINE][MAX_CNT];

    //赢法统计数组
    int[] myWin = new int[MAX_CNT];
    int[] pcWin = new int[MAX_CNT];
    private int count;
    //游戏是否结束over
    private boolean over = false;

    //棋盘上两方棋子的标志  0 无子  ；           1  我方 ；              2   电脑
    private int[][] GobangBoard = new int[MAX_LINE][MAX_LINE];

    //保存最高得分的i，j值
    int u = 0;
    int v = 0;

    public GobangPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
        WinInit();
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

    public void WinInit() {
        count = 0;
        // 通过以下运算，给每个点分配权重价值，越靠近中心点的价值越高，统计点P=p(x,y)的累积价值ΣP(z)
        Log.d(TAG, "WinInit ----run");

        Log.d(TAG, "MAX_CNT = " + MAX_CNT);
        //横向赢法统计
        for (int i = 0; i < MAX_LINE; i++) {
            for (int j = 0; j < MAX_LINE + 1 - FIVE; j++) {
                for (int k = 0; k < FIVE; k++) {
                    // Log.d(TAG, "i,j,k:  " + i + ", " + j + ", " + k + "  cnt=" + count);
                    wins[i][j + k][count] = 1;  // 金字塔堆积,三维立体坐标，第3个坐标为棋子累积价值 |/_(x,y,z)
                }
                count++;
            }
        }
        //纵向赢法统计
        for (int i = 0; i < MAX_LINE; i++) {
            for (int j = 0; j < MAX_LINE + 1 - FIVE; j++) {
                for (int k = 0; k < FIVE; k++) {
                    // Log.d(TAG, "i,j,k:  " + i + ", " + j + ", " + k + "  cnt=" + count);
                    wins[j + k][i][count] = 1;
                }
                count++;
            }
        }

        //左上到右下斜线赢法统计
        for (int i = 0; i < MAX_LINE + 1 - FIVE; i++) {
            for (int j = 0; j < MAX_LINE + 1 - FIVE; j++) {
                for (int k = 0; k < FIVE; k++) {
                    // Log.d(TAG, "i,j,k:  " + i + ", " + j + ", " + k + "  cnt=" + count);
                    wins[i + k][j + k][count] = 1;
                }
                count++;
            }
        }

        //右上到左下斜线赢法统计
        for (int i = 0; i < MAX_LINE + 1 - FIVE; i++) {
            for (int j = MAX_LINE - 1; j > MAX_LINE - 1 - (FIVE + 1); j--) {
                for (int k = 0; k < FIVE; k++) {
                    // Log.d(TAG, "i,j,k:  " + i + ", " + j + ", " + k + "  cnt=" + count);
                    wins[i + k][j - k][count] = 1;
                }
                count++;
            }
        }

        for (int i = 0; i < count; i++) {
            myWin[i] = 0;
            pcWin[i] = 0;
        }


        /*###################################################################################
        以下为打印对应坐标的权值：
        /com.example.gobang I/System.out: 03   04   05   06   08   08   06   05   04   03
        /com.example.gobang I/System.out: 04   06   07   09   11   11   09   07   06   04
        /com.example.gobang I/System.out: 05   07   10   12   14   14   12   10   07   05
        /com.example.gobang I/System.out: 06   09   12   15   17   17   15   12   09   06
        /com.example.gobang I/System.out: 08   11   14   17   20   20   17   14   11   08
        /com.example.gobang I/System.out: 08   11   14   17   20   20   17   14   11   08
        /com.example.gobang I/System.out: 06   09   12   15   17   17   15   12   09   06
        /com.example.gobang I/System.out: 05   07   10   12   14   14   12   10   07   05
        /com.example.gobang I/System.out: 04   06   07   09   11   11   09   07   06   04
        /com.example.gobang I/System.out: 03   04   05   06   08   08   06   05   04   03
        ###################################################################################*/
        int n = 0;
        for (int i = 0; i < MAX_LINE; i++) {
            for (int j = 0; j < MAX_LINE; j++) {
                for (int k = 0; k < MAX_CNT; k++) {
                    n = wins[i][j][k] + n;
                }
                System.out.printf("%02d   ", n);
                n = 0;
            }
            System.out.printf("\n", n);
        }
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
            int y = (int) ((0.5 + i) * lineHeight);
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
                    (blackPoint.x + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (over) {
            return false;   // 结束后不作为
        }
        if (!mIsWhite) {
            return false;   // 计算机执行时不作为
        }
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);
            int m = p.x;
            int n = p.y;

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;   // 棋子已存在时不作为
            }

            if (mIsWhite) {
                mWhiteArray.add(p);
                GobangBoard[m][n] = 1;      //  0：无子 1:我方 2：对方
                for (int k = 0; k < count; k++) {
                    if (wins[m][n][k] == 1) {
                        myWin[k]++;             // 胜算
                        pcWin[k] = FIVE + 1;
                        if (myWin[k] == FIVE) {
                            Toast.makeText(this.getContext(), "你赢了", Toast.LENGTH_SHORT).show();
                            over = true;
                        }
                    }
                }

                if (!over) {
                    mIsWhite = !mIsWhite;
                    computerAI();
                }
            }/* else {
                mBlackArray.add(p);
                if (!over) {
                    mIsWhite = !mIsWhite;
                }
            }*/

            invalidate();
            return true;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    private void computerAI() {

        //保存最高得分
        int max = 0;

        int[][] myScore = new int[MAX_LINE][MAX_LINE];
        int[][] pcScore = new int[MAX_LINE][MAX_LINE];
        //初始化分数值
        for (int i = 0; i < MAX_LINE; i++) {
            for (int j = 0; j < MAX_LINE; j++) {
                myScore[i][j] = 0;
                pcScore[i][j] = 0;
            }
        }

        for (int i = 0; i < MAX_LINE; i++) {
            for (int j = 0; j < MAX_LINE; j++) {
                if (GobangBoard[i][j] == 0) {       // 此处空白，PC可落子
                    for (int k = 0; k < count; k++) {
                        if (wins[i][j][k] == 1) {
                            //我方得分，计算机拦截
                            if (myWin[k] == 1) {
                                myScore[i][j] += 200;
                            } else if (myWin[k] == 2) {
                                myScore[i][j] += 400;
                            } else if (myWin[k] == 3) {
                                myScore[i][j] += 2000;
                            } else if (myWin[k] == 4) {
                                myScore[i][j] += 10000;
                            }
                           /* for (int n = 1; i < FIVE; n++) {
                                if (myWin[k] == n) {
                                    myScore[i][j] += MAX_CNT * (1 + (int) Math.pow(FIVE, (n - 1)));
                                }
                            }*/

                            //计算机走法 得分
                            if (pcWin[k] == 1) {
                                pcScore[i][j] += 220;
                            } else if (pcWin[k] == 2) {
                                pcScore[i][j] += 420;
                            } else if (pcWin[k] == 3) {
                                pcScore[i][j] += 2100;
                            } else if (pcWin[k] == 4) {
                                pcScore[i][j] += 20000;
                            }
                           /* for (int n = 1; i < FIVE; n++) {
                                if (myWin[k] == n) {
                                    myScore[i][j] += MAX_CNT * (1 + (int) Math.pow(FIVE, (n - 1))) + 2;
                                }
                            }*/
                        }
                    }

                    //判断我方最高得分，将最高分数的点获取出来, u，v为计算机要落下的子的坐标
                    if (myScore[i][j] > max) {
                        max = myScore[i][j];
                        u = i;
                        v = j;
                    } else if (myScore[i][j] == max) {
                        if (pcScore[i][j] > pcScore[u][v]) {
                            //认为i，j点比u，v点好
                            u = i;
                            v = j;
                        }
                    }

                    //判断电脑方最高得分，将最高分数的点获取出来
                    if (pcScore[i][j] > max) {
                        max = pcScore[i][j];
                        u = i;
                        v = j;
                    } else if (pcScore[i][j] == max) {
                        if (myScore[i][j] > myScore[u][v]) {
                            //认为i，j点比u，v点好
                            u = i;
                            v = j;
                        }
                    }

                }
            }
        }

        GobangBoard[u][v] = 2;  // PC落子
        mBlackArray.add(new Point(u, v));
        invalidate();

        for (int k = 0; k < count; k++) {
            if (wins[u][v][k] == 1) {
                pcWin[k]++;
                myWin[k] = FIVE + 1;
                if (pcWin[k] == FIVE) {
                    Toast.makeText(this.getContext(), "计算机赢了", Toast.LENGTH_SHORT).show();
                    over = true;
                }
            }
        }

        if (!over) {
            mIsWhite = !mIsWhite;

        }

    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }
}
