package com.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import java.util.TimeZone;

/**
 * 自定义时钟01
 * 关于MeasureSpec确定过程，请点https://juejin.im/entry/5767a69e207703006bbd269f
 * Created by sunyan on 17/3/22.
 */

public class Aclock01 extends View {

    private Time mCalendar;

    private Drawable mdial;
    private Drawable mpoint;
    private Drawable mhour;
    private Drawable mminute;
    private Drawable msecond;

    private int dial;
    private int point;
    private int hour;
    private int minute;
    private int second;

    private int dialWidth;
    private int dialHeight;
    private int pointWidth;
    private int pointHeight;
    private int hourWidth;
    private int hourHeight;
    private int minuteWidth;
    private int minuteHeigth;
    private int secondWidth;
    private int secondheight;

    private float scale;
    private boolean mChanged;

    public Aclock01(Context context) {
        this(context,null);
    }

    public Aclock01(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Aclock01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义的几个属性
        if (attrs != null){
            TypedArray types = context.obtainStyledAttributes(attrs,R.styleable.Clock);
            dial =  types.getResourceId(R.styleable.Clock_dial,-1);//表盘
            point = types.getResourceId(R.styleable.Clock_dial_point,-1);//圆心
            hour = types.getResourceId(R.styleable.Clock_dial_hour,-1);//时针
            minute = types.getResourceId(R.styleable.Clock_dial_hour,-1);//分针
            second = types.getResourceId(R.styleable.Clock_dial_second,-1);//秒针
            scale = types.getFloat(R.styleable.Clock_scalenum,1.5f);

//            mdial = types.getDrawable(R.styleable.Clock_dial);
//            mpoint = types.getDrawable(R.styleable.Clock_dial_point);
//            mhour = types.getDrawable(R.styleable.Clock_dial_hour);
//            mminute = types.getDrawable(R.styleable.Clock_dial_minute);
//            msecond = types.getDrawable(R.styleable.Clock_dial_second);
            //调用此方法后就不需要重新获取以上属性
            types.recycle();
        }
        //获取自定义属性对应的具体值（在布局文件中传入的值）

            mdial = getResources().getDrawable(dial);
            dialWidth = mdial.getIntrinsicWidth();
            dialHeight = mdial.getIntrinsicHeight();

            mpoint = getResources().getDrawable(point);
            pointWidth = mpoint.getIntrinsicWidth();
            pointHeight = mpoint.getIntrinsicHeight();

            mhour = getResources().getDrawable(hour);
            hourWidth = mhour.getIntrinsicWidth();
            hourHeight = mhour.getIntrinsicHeight();

            mminute = getResources().getDrawable(minute);
            minuteWidth = mminute.getIntrinsicWidth();
            minuteHeigth = mminute.getIntrinsicHeight();

            msecond = getResources().getDrawable(second);
            secondWidth = msecond.getIntrinsicWidth();
            secondheight = msecond.getIntrinsicHeight();

        mCalendar = new Time();
    }

    /**
     * 重写这个方法的目的是为了给控件所在的画布设置大小，如果不重写
     * 则默认跟父母局相同大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        L.e("onMeasure1--"+MeasureSpec.UNSPECIFIED+"   "+widthMode+"  "+widthSize+"  "+dialWidth);
        float wscale = 1.0f;
        float hscale = 1.0f;
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < dialWidth) {
            wscale = widthSize/dialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < dialHeight){
            hscale = heightSize/dialHeight;
        }
        float scale = Math.min(wscale,hscale);
        L.e("onMeasure2--"+resolveSize((int) (dialWidth*scale),widthMeasureSpec));
        //设置canvas的大小，即时钟所在画布大小
        setMeasuredDimension(resolveSize((int) (dialWidth*scale),widthMeasureSpec),resolveSize((int) (dialHeight*scale),heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        L.e("onDraw--"+canvas.getWidth()+"  "+dialWidth);
        boolean changed = mChanged;

        int x = canvas.getWidth()/2;
        int y = canvas.getHeight()/2;
        //此处注意，如果直接 scaleWidth = cavas.getWidth()/dialWidth，那么scaleWidth = 0.0
        float scaleWidth = (float)canvas.getWidth()/(float)dialWidth;
        float scaleHeigth = (float)canvas.getHeight()/(float)dialHeight;
        float scale = Math.max(scaleWidth,scaleHeigth);
        L.e("scale--"+scaleWidth+"   "+scaleHeigth);
        //设置表盘位置
        if (changed){
            mChanged = false;
            mdial.setBounds(0,0,2*x,2*y);
        }
        mdial.draw(canvas);
        canvas.save();
        //设置圆点位置
        if (changed){
            int w = (int) ((pointWidth/2)*scale);
            int h = (int) ((pointHeight/2)*scale);
            mpoint.setBounds(x-w,y-h,x+w,y+h);
        }
        mpoint.draw(canvas);
        canvas.save();

        //设置时针位置(以(x,y)即圆心为圆点，旋转时针对应的角度)
        canvas.rotate(mHour*360/12,x,y);
        if (changed){
            int w = (int) (hourWidth/2*scale);
            int h = (int) (hourHeight/2*scale);
            L.e("hour:::"+(x-w)+"  "+(y-h)+"  "+(x+w)+"  "+(y+h));
            mhour.setBounds(x-w,y-h,x+w,y+h);
        }
        mhour.draw(canvas);
        canvas.restore();
        canvas.save();

        //设置分针位置
        canvas.rotate(mMinutes*360/60,x,y);
        if (changed){
            int w = (int) (minuteWidth/2*scale);
            int h = (int) (minuteHeigth/2*scale);
            mminute.setBounds(x-w,y-h,x+w,y+h);
        }
        mminute.draw(canvas);
        canvas.restore();
        canvas.save();

        //设置秒针的位置
        canvas.rotate(mSecond*360/60,x,y);
        if (changed && mSeconds){
            int w = (int) (secondWidth/2*scale);
            int h = (int) (secondheight/2*scale);
            msecond.setBounds(x-w,y-h,x+w,y+h);
        }
        msecond.draw(canvas);
        canvas.restore();
    }

    private BroadcastReceiver mIntentReceiver;
    private final Handler mHandler = new Handler();
    private float mMinutes;
    private float mHour;
    /**
     * 开启计数器及时间变化监听服务
     */
    public void start(){
        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        if(mIntentReceiver!=null){
            getContext().unregisterReceiver(mIntentReceiver);
            mIntentReceiver = null;
        }
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                    String tz = intent.getStringExtra("time-zone");
                    //当前时间对应的Time
                    mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
                }
                onTimeChanged();
                invalidate();
            }
        };
        getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        //开始时间对应的Time
        mCalendar = new Time();
        onTimeChanged();
        counter.start();
    }

    /**
     * 关闭计时器及时间监听服务
     */
    public void stop(){
        L.e("stop()------------------");
        if(mIntentReceiver == null)
            return;
        counter.cancel();
        getContext().unregisterReceiver(mIntentReceiver);
        mIntentReceiver = null;

    }

    private void onTimeChanged() {
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;

        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }

    boolean mSeconds=false;
    float mSecond=0;
    MyCount counter = new MyCount(Integer.MAX_VALUE, 1000);
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            counter.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            L.e("onTick---------------------");
            mCalendar.setToNow();
            mSecond= mCalendar.second;
            mSeconds = true;
            invalidate();
        }
    }
}
