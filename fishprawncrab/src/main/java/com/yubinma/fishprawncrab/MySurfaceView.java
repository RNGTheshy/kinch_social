package com.yubinma.fishprawncrab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable.Callback;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private int x1 = 30, y1 = 100, w1 = 40, h1 = 40;
    private int x2 = 80, y2 = 100, w2 = 40, h2 = 40;    //定义两个矩形的左上角坐标和他们的宽高
    private SurfaceHolder holder;    //holder用于控制surfaceView
    private Canvas canvas;
    private Paint paint;    //画笔的实例，还用说么？
    private Thread th;    //定义一个新的线程，用来不断的执行ondraw
    private boolean flag = false;    //线程是否执行的标志位
    private boolean isCollsion;    //检测碰撞的标志位

    public MySurfaceView(Context context) {
        super(context);
        //在构造函数里初始化一些实例
        holder = this.getHolder();
        holder.addCallback(this);    //为holder添加监听器，监听surfaceView的改变
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);    //设置画笔的抗锯齿和颜色
        setFocusable(true);    //设置焦点，有了焦点，点击屏幕才会生效
        // TODO Auto-generated constructor stub
    }

    public void onDraw() {
        try {
            canvas = holder.lockCanvas();    //给surfaceView锁定一块画布
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);    //如果画布不等于空，就刷屏成黑色
                if (isCollsion) {    //如果发生碰撞
                    paint.setTextSize(20);
                    canvas.drawText("IsCollsion", 10, 50, paint);    //在屏幕左上角协商iscollsion
                } else {
                    paint.setColor(Color.WHITE);
                }
            }
            canvas.drawRect(x1, y1, x1 + w1, y1 + h1, paint);    //把矩形1和矩形2给画上
            canvas.drawRect(x2, y2, x2 + w2, y2 + h2, paint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //碰撞检测流程（一）：更新实体对象的位置
        x1 = (int) event.getX() - w2 / 2;    //取得点击处的坐标，进而求得矩形2的左上角坐标
        y1 = (int) event.getY() - h2 / 2;

        //碰撞检测流程（三）：碰到之后，进行相应处理
        if (IsCollsion(x1, x2, y1, y2, w1, w2, h1, h2)) {
            isCollsion = true;    //如果碰撞上了就把isCollsion改为true
        } else {
            isCollsion = false;
        }

        return true;
    }

    /*
     * x1,y1是矩形1左上角的坐标，w1,h1是矩形1的宽高，x2,y2,w2,h2同理，是h2的左上角坐标
     * 和宽高
     */
    //碰撞检测流程（二）：进行碰撞检测
    public boolean IsCollsion(int x1, int x2, int y1, int y2, int w1,
            int w2, int h1, int h2) {
        //满足以下四种情况之一，则不会发送碰撞，除此之外都会发送碰撞
        if (x1 < x2 && x1 + w1 <= x2) {
            return false;
        } else if (x1 > x2 && x1 >= x2 + w2) {
            return false;
        } else if (y1 < y2 && y1 + h1 <= y2) {
            return false;
        } else if (y1 > y2 && y1 >= y2 + h2) {
            return false;
        }
        return true;    //不满足以上四种情况的全是发生碰撞
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (flag) {
            long start = System.currentTimeMillis();
            onDraw();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        flag = true;
        th = new Thread(this);    //实例化线程
        th.start();    //执行线程
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

}

