package com.letrinhas02.util;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Classe para garantir que existe conecção à rede, seja por WI-FI (preferencialmente)
 * 3G, 4G, GSM
 * 
 * 
 * @author ... 
 *
 */
public class coneccaoW extends Thread{
	WifiManager mainWifiObj;
	WifiScanReceiver wifiReciever;
	ListView list;
	String wifis[];
	//activity
	Activity atc;
	

	   
	   
	   
	private class WifiScanReceiver extends BroadcastReceiver {
	      @SuppressLint("UseValueOf")
	      public void onReceive(Context c, Intent intent) {
	         List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
	         wifis = new String[wifiScanList.size()];
	         for(int i = 0; i < wifiScanList.size(); i++){
	            wifis[i] = ((wifiScanList.get(i)).toString());
	         }

	         list.setAdapter(new ArrayAdapter<String>(atc.getApplicationContext(),
	         android.R.layout.simple_list_item_1,wifis));
	      }
	   }
	   
}
