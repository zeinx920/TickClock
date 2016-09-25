package com.Trasys.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class TickClock extends View {

	protected static final String TAG = "HeiMaClock";
	private int mCx;
	private int mCy;// 圆环的中心

	private int mRadius;// 圆环 的半径
	private Paint mPaint;

	private int mPadding = 5;

	private int mLongLineLength = 12;
	private int mShortLineLength = 5;

	private int mTextSize = 15;
	private Paint mNumberPaint;
	private Bitmap mLogo;
	private Path mSecondPath;

	private int mSecondArrowWidth = 8;
	private int mMinuteArrowWidth = 10;
	private int mHourArrowWidth = 12;

	private Paint mArrowPaint;
	private Path mMinutePath;
	private Path mHourPath;
	private Paint mBackgroundPaint;
	
	private int mSecondDegree;
	private int mMinuteDegree;
	private int mHourDegree;
	

	public TickClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);// 去锯齿

		mNumberPaint = new Paint();
		mNumberPaint.setAntiAlias(true);// 去锯齿
		mNumberPaint.setTextAlign(Align.CENTER);// 让字符居中

//		mLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

		mArrowPaint = new Paint();
		mArrowPaint.setAntiAlias(true);
		// initSecondArrowPath();

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAntiAlias(true);
		// mBackgroundPaint.setColor(Color.GRAY);
		
		startTick();

	}

	private void initShader() {
		// colors渐变颜色的种类
		int colors[] = { Color.BLACK, Color.LTGRAY, Color.WHITE, Color.LTGRAY };
//		float positions[] = {0, 0.25f, 0.5f, 0.75f, 1};
//		float positions[] = {0, 0.5f, 0.8f, 1};
		Shader shader = new RadialGradient(mCx, mCy, mRadius, colors, null,
				TileMode.CLAMP);
		mBackgroundPaint.setShader(shader);
	}

	private void initSecondArrowPath() {
		mSecondPath = new Path();
		int x1 = mCx, y1 = mPadding + mLongLineLength + 5;
		int x2 = mCx + mSecondArrowWidth / 2, y2 = mCy + 10;
		int x3 = mCx, y3 = mCy + 5;
		int x4 = mCx - mSecondArrowWidth / 2, y4 = y2;
		mSecondPath.moveTo(x1, y1);
		mSecondPath.lineTo(x2, y2);
		mSecondPath.lineTo(x3, y3);
		mSecondPath.lineTo(x4, y4);
		mSecondPath.lineTo(x1, y1);// mSecondPath.close()
	}

	private void initMinuteArrowPath() {
		mMinutePath = new Path();
		int x1 = mCx, y1 = mPadding + mLongLineLength + 15;
		int x2 = mCx + mMinuteArrowWidth / 2, y2 = mCy + 10;
		int x3 = mCx, y3 = mCy + 5;
		int x4 = mCx - mMinuteArrowWidth / 2, y4 = y2;
		mMinutePath.moveTo(x1, y1);
		mMinutePath.lineTo(x2, y2);
		mMinutePath.lineTo(x3, y3);
		mMinutePath.lineTo(x4, y4);
		mMinutePath.lineTo(x1, y1);// mSecondPath.close()
	}

	private void initHourArrowPath() {
		mHourPath = new Path();
		int x1 = mCx, y1 = mPadding + mLongLineLength + 25;
		int x2 = mCx + mHourArrowWidth / 2, y2 = mCy + 10;
		int x3 = mCx, y3 = mCy + 5;
		int x4 = mCx - mHourArrowWidth / 2, y4 = y2;
		mHourPath.moveTo(x1, y1);
		mHourPath.lineTo(x2, y2);
		mHourPath.lineTo(x3, y3);
		mHourPath.lineTo(x4, y4);
		mHourPath.lineTo(x1, y1);// mSecondPath.close()
	}

	/**
	 * 大小发生变化时调用 在布局完成
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCx = w / 2;
		mCy = h / 2;
		mRadius = w / 2 - mPadding;

		initSecondArrowPath();
		initMinuteArrowPath();
		initHourArrowPath();
		initShader();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 6. 画背景
		canvas.drawCircle(mCx, mCy, mRadius, mBackgroundPaint);
		// 1. 画圆环
		// cx = getWidth()/2
		canvas.drawCircle(mCx, mCy, mRadius, mPaint);
		// 2. 画刻度
		// //画一条刻度
		// canvas.rotate(30, mCx, mCy);
		// int startX = getWidth() / 2;
		// int startY = mPadding;
		// int stopX = startX;
		// int stopY = startY + mLongLineLength;
		// canvas.drawLine(startX, startY, stopX, stopY, mPaint);
		drawTimeLine(canvas);
		// 3. 画数字
		// int x = mCx;
		// int y = mPadding + mLongLineLength + mTextSize;
		// canvas.drawText("12", x, y, mNumberPaint);
		drawDigits(canvas);
		// 4. 画logo
//		drawLogo(canvas);
		// 5. 画指针
		// 画秒针
		drawArrows(canvas);

		// 7. 指针动起来
	}

	private void drawArrows(Canvas canvas) {
		canvas.rotate(mSecondDegree, mCx, mCy);
		mArrowPaint.setColor(Color.RED);
		canvas.drawPath(mSecondPath, mArrowPaint);
		canvas.rotate(-mSecondDegree, mCx, mCy);
		
		canvas.rotate(mMinuteDegree, mCx, mCy);
		mArrowPaint.setColor(Color.YELLOW);
		canvas.drawPath(mMinutePath, mArrowPaint);
		canvas.rotate(-mMinuteDegree, mCx, mCy);
		
		canvas.rotate(mHourDegree, mCx, mCy);
		mArrowPaint.setColor(Color.GREEN);
		canvas.drawPath(mHourPath, mArrowPaint);
		canvas.rotate(-mHourDegree, mCx, mCy);
	}

	//画Log
	private void drawLogo(Canvas canvas) {
		int left = mCx - mLogo.getWidth() / 2;
		int top = mPadding + mLongLineLength + mTextSize + 10;
		canvas.drawBitmap(mLogo, left, top, mPaint);
	}

	private void drawDigits(Canvas canvas) {
		for (int i = 1; i <= 12; i++) {
			// 第一次旋转30度画1
			canvas.rotate(30, mCx, mCy);
			int x = mCx;
			int y = mPadding + mLongLineLength + mTextSize;
			// 把字掰直
			int px = mCx;
			int py = mPadding + mLongLineLength + mTextSize / 2;
			canvas.rotate(-i * 30, px, py);// 以字为中心旋转画布
			canvas.drawText(String.valueOf(i), x, y, mNumberPaint);
			// 把画布掰回来，不影响下次画后续的数字
			canvas.rotate(i * 30, px, py);// 以字为中心旋转画布
		}
	}

	private void drawTimeLine(Canvas canvas) {
		for (int i = 0; i < 60; i++) {
			int startX = getWidth() / 2;
			int startY = mPadding;
			int stopX = startX;
			int stopY = 0;
			if (i % 5 == 0) {
				// 画长刻度
				stopY = startY + mLongLineLength;
			} else {
				stopY = startY + mShortLineLength;
			}

			canvas.drawLine(startX, startY, stopX, stopY, mPaint);
			canvas.rotate(6, mCx, mCy);
		}
	}
	
	public void startTick() {
		//获取时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		//8:55:30
		int seconds = calendar.get(Calendar.SECOND);
		int minutes = calendar.get(Calendar.MINUTE);
		int hours = calendar.get(Calendar.HOUR);
		
		//将时间转换指针对应的角度
		mSecondDegree = seconds * 6;
		mMinuteDegree = minutes * 6;
		mHourDegree = hours * 30;
		invalidate();
	}
	
	/**
	 * TextClock
	 * 当控件添加到window的调用
	 * 
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		postDelayed(mTicker, 1000);
	}
	
	private Runnable mTicker = new Runnable() {
		
		@Override
		public void run() {
			Log.d(TAG, "tick");
			startTick();
			postDelayed(mTicker, 1000);//再次延时1000执行tick
		}
	};
	
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		//停止更新
		removeCallbacks(mTicker);
	};
}
