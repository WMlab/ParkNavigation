package com.wmlab.navigation;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class Main extends Activity{
	
	private Timer sensorTimer = null;
	private TimerTask sensorTimerTask = null;
	
	private SensorData sensorData = new SensorData(this);
	private WifiSignal wifiSignal = new WifiSignal(this);
	
	private short turnToWhat = 0;
	public static final short TurnLeft = 1;
	public static final short TurnRight = 2;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//initialize sensors and wifi scan
		sensorData.initSensors();
		wifiSignal.initWifiManager();
		
		startSensorTimer();
	}
	public void startSensorTimer() {
		if (sensorTimer == null) {
			sensorTimerTask = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//判断是否转弯以及转向
					turnToWhat = sensorData.turnLeftOrRightOrNot();
					
					switch (turnToWhat) {
					case TurnLeft:
						int[] wifiRssi = wifiSignal.getLatestWifiRssi();
						//get the turing position's coords(cross)
						CoordsHolder coordsHolder = getPosition(wifiRssi);
						Log.i("turn", "左转:"+wifiRssi[5]);
						break;
					case TurnRight:
						Log.i("turn", "右转");
						break;
					default:
						break;
					}
				}
			};
			sensorTimer = new Timer();
			sensorTimer.schedule(sensorTimerTask, 100, 200);
		}
	}
	
	public void closeSensorTimer() {
		if (sensorTimer != null) {
			sensorTimer.cancel();
			sensorTimer = null;
		}
		if (sensorTimerTask != null) {
			sensorTimerTask = null;
		}
	}
	/*
	 * function to implement position algorithm, uncompleted!!!
	 */
	public CoordsHolder getPosition(int[] wifiRssi) {
		CoordsHolder positionCoords = new CoordsHolder();
		return positionCoords;
	}
	@Override
	protected void onResume()
	{
		//监听传感器
		super.onResume();
		sensorData.registerSensorListener();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		sensorData.unRegisterSensorListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public final class CoordsHolder {
		int pointID;
		float x;
		float y;
	}
}
