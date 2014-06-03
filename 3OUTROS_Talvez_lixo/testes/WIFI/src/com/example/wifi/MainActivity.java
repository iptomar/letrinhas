package com.example.wifi;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

   WifiManager mainWifiObj;
   WifiScanReceiver wifiReciever;
   ListView list;
   String wifis[];
   
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);    
      list = (ListView)findViewById(R.id.listView1);
      mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
      //Automatic Connection to wifi 
      if (mainWifiObj.isWifiEnabled()){
          //wifi is enabled
    	  Toast.makeText(getApplicationContext(),"Enabled", Toast.LENGTH_LONG).show();
          }else{
        	  Toast.makeText(getApplicationContext(),"Disable", Toast.LENGTH_SHORT).show();
        	  Toast.makeText(getApplicationContext(),"Activating", Toast.LENGTH_SHORT).show();
        	  mainWifiObj.setWifiEnabled(true);
        	  Toast.makeText(getApplicationContext(),"Enabled", Toast.LENGTH_LONG).show();
          }
      wifiReciever = new WifiScanReceiver();
      mainWifiObj.startScan();
      list.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			lists(list.getItemAtPosition(arg2).toString());
		}
	});
   }
   
   public void lists(String ssid){
	   Bundle wrap = new Bundle();
	   wrap.putString("key", ssid);
	   Toast.makeText(getApplicationContext(),ssid, Toast.LENGTH_LONG).show();
	   Intent intent = new Intent(this,SSID.class);
	   intent.putExtras(wrap);
	   startActivity(intent);
	}
   
	/**
	 * 
	 */
   protected void onPause() {
      unregisterReceiver(wifiReciever);
      super.onPause();
   }
   
	 /**
	 * 
	 */
   protected void onResume() {
      registerReceiver(wifiReciever, new IntentFilter(
      WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
      super.onResume();
   }

   class WifiScanReceiver extends BroadcastReceiver {
      @SuppressLint("UseValueOf")
      /**
       * 
       */
      public void onReceive(Context c, Intent intent) {
         List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
         wifis = new String[wifiScanList.size()];
         for(int i = 0; i < wifiScanList.size(); i++){
            wifis[i] = ((wifiScanList.get(i)).toString());
         }
         list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
         android.R.layout.simple_list_item_1,wifis));
      }
   }
}