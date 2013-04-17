package com.wmlab.navigation;

import java.util.List;

import android.R.integer;
import android.R.string;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class WifiSignal {
	private Context context;
	private WifiManager wifiManager;
	private WifiReceiver wifiReceiver;
	private List<ScanResult> scanWifiList;
	
	private static final short NumberOfAPs = 8;
	int[][] wifiRssi = new int[2][NumberOfAPs];

	final Handler scanHandler = new Handler();
	//constructor, initialize context
	public WifiSignal(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public void initWifiManager() {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiReceiver = new WifiReceiver();
		context.registerReceiver(this.wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		scanHandler.post(scanRunnable);
	}
	/*
	 * return rssi values used for positioning
	 */
	public int[][] getWifiRssiArrays(){
		return wifiRssi;
	}
	
	public int[] getLatestWifiRssi() {
		return wifiRssi[0];
	}
	
	Runnable scanRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			wifiManager.startScan();
		}
	};
	
	class WifiReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Once receive scan result, start a new scan
			scanHandler.post(scanRunnable);
			
			scanWifiList = wifiManager.getScanResults();
			for (int i = 0; i < NumberOfAPs; i++) {
				wifiRssi[1][i] = wifiRssi[0][i];
				//rssi initialize with value -90
				wifiRssi[0][i] = -90;
			}
			for (int i = 0; i < scanWifiList.size(); i++) {
				String wifiName = scanWifiList.get(i).SSID;
				int wifiLevel = scanWifiList.get(i).level;
				
				if (wifiName.substring(0, 2).equals("AP")) {
					String apNumString = wifiName.substring(2,wifiName.length());
					if (isNum(apNumString)) {
						int apNum = Integer.parseInt(apNumString);
						wifiRssi[0][apNum - 1] = wifiLevel;
					}
				}
			}
		}
	}
	/*
	 * 判读一个字符串是否是数字
	 */
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
}
