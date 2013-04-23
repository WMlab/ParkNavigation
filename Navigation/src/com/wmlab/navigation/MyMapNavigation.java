package com.wmlab.navigation;

import android.R.color;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MyMapNavigation extends View implements Runnable{
	private Paint paint=null;
	int currentPoint,destinationPoint;
	float pointX[] = {0,15};
	float pointY[] = {0,15};
	public MyMapNavigation(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		new Thread(this).start();
	}
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		Bitmap parkingMap = null;
		parkingMap = ((BitmapDrawable)getResources().getDrawable(R.drawable.parking_map)).getBitmap();
		Matrix bkgMatrix = new Matrix();
		DisplayMetrics  dm = getResources().getDisplayMetrics(); // 获取手机屏幕的大小
		float displayWidth = dm.widthPixels;
		float displayHeight = dm.heightPixels;
		bkgMatrix.setScale(displayWidth/parkingMap.getWidth(), displayWidth/parkingMap.getHeight());
		canvas.drawBitmap(parkingMap, bkgMatrix, null);
		
		//画线
		paint.setStrokeWidth(5);//笔宽5像素
		paint.setColor(Color.BLUE);//设置为蓝笔
		paint.setAntiAlias(true);//锯齿不显示
		canvas.drawLine(displayWidth*(105f/744f), displayWidth, displayWidth*(105f/744f), displayWidth/30f, paint);
		
		Bitmap myLoc = null;
		myLoc =((BitmapDrawable)getResources().getDrawable(R.drawable.arrow)).getBitmap();
		Matrix myLocMatrix = new Matrix();
		myLocMatrix.setScale(displayWidth/(myLoc.getWidth()*15f), displayWidth/(myLoc.getHeight()*15f));
		myLocMatrix.postTranslate(displayWidth*(105f/744f)-displayWidth/30f,displayWidth-displayWidth/30f);
		canvas.drawBitmap(myLoc, myLocMatrix, null);
		
		Bitmap myDes = null;
		myDes =((BitmapDrawable)getResources().getDrawable(R.drawable.destination)).getBitmap();
		Matrix myDesMatrix = new Matrix();
		myDesMatrix.setScale(displayWidth/(myDes.getWidth()*15f), displayWidth/(myDes.getHeight()*15f));
		myDesMatrix.postTranslate(displayWidth*(105f/744f)-displayWidth/30f,0);
		canvas.drawBitmap(myDes, myDesMatrix, null);
		
		
		
		
		//以下为坐标换算
		//float locX = (x/40f)*displayWidth*(526f/744f)+displayWidth*(105f/744f)-displayWidth/30f;
		//float locY = displayWidth-(y/44f)*displayWidth-displayWidth/30f;
		
		//myMatrix.postTranslate(locX,locY);
		//canvas.drawBitmap(myLoc, myMatrix, null);
	
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!Thread.currentThread().isInterrupted())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO: handle exception
				Thread.currentThread().interrupt();
			}
			postInvalidate();//刷新界面
		}
	}

}

