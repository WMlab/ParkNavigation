package com.wmlab.navigation;

import android.R.integer;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorData implements SensorEventListener{
	
	private Context context;
	//传感器
	private SensorManager manager;
	private Sensor accelerometerSensor;
	private Sensor magneticSensor;
	//方向
	private float orientation;
	private float currentOrientation = 0f;
	private float oldOrientation = 1000f;//随便设一个初值
	private static final short NoTurn = 0;
	private static final short TurnLeft = 1;
	private static final short TurnRight = 2;
	
//	private enum turn {NoTurn, TurnLeft, TurnRight;}
	private float[] OrientationValues = new float[3]; 
    private float[] Ro = new float[9];
    
    public float[] Accel = new float[3]; 
	public float[] Magnific = new float[3]; 
	
	public SensorData(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public void initSensors() {
		//获取传感器
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		accelerometerSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		//监听传感器
		manager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
		manager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public short turnLeftOrRightOrNot() {
		short turn = 0;
		if(oldOrientation == 1000)
		{
			oldOrientation = currentOrientation;
		}
		else
		{
			if ((currentOrientation>(oldOrientation-180)&&currentOrientation<=(oldOrientation-60))
				||(currentOrientation>(oldOrientation+180)&&currentOrientation<=(oldOrientation+300))) 
			{
				Log.i("turn", "old:"+oldOrientation+"---"+"new:"+currentOrientation);
				oldOrientation = currentOrientation;
				turn = TurnRight;
			}
			else if ((currentOrientation>(oldOrientation-300)&&currentOrientation<=(oldOrientation-180))
				||(currentOrientation>(oldOrientation+60)&&currentOrientation<=(oldOrientation+180)))
			{
				Log.i("turn", "old:"+oldOrientation+"---"+"new:"+currentOrientation);
				oldOrientation = currentOrientation;
				turn = TurnLeft;
			}
			else {
				turn = NoTurn;
			}
		}
		return turn;
	}
	public void registerSensorListener() {
		manager.registerListener(this, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
		manager.registerListener(this, magneticSensor,SensorManager.SENSOR_DELAY_GAME);
	}
	public void unRegisterSensorListener() {
		manager.unregisterListener(this, accelerometerSensor);
		manager.unregisterListener(this, magneticSensor);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized(this){
			switch (event.sensor.getType()) {
			case Sensor.TYPE_MAGNETIC_FIELD:
				Magnific = event.values.clone();
				break;
			case Sensor.TYPE_ACCELEROMETER:
				Accel = event.values.clone();
				break;
			default:
				break;
			}
		}
		SensorManager.getRotationMatrix(Ro, null, Accel, Magnific);
		SensorManager.getOrientation(Ro, OrientationValues);
		orientation = (float) Math.toDegrees(OrientationValues[0]);
		currentOrientation = 0 - orientation;
//		Log.i("orientation", orientation+"");
	}
}
