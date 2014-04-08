package com.letrinhas02.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

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
	Activity act;
	public coneccaoW(Activity act) {
		this.act = act;
	}
	
	public void run(){
	mainWifiObj = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
    //Automatic Connection to wifi 
    if (mainWifiObj.isWifiEnabled()){
        //wifi is enabled
  	  Toast.makeText(act.getApplicationContext(),"Enabled", Toast.LENGTH_LONG).show();
        }else{
      	  Toast.makeText(act.getApplicationContext(),"Disable", Toast.LENGTH_SHORT).show();
      	  Toast.makeText(act.getApplicationContext(),"Activating", Toast.LENGTH_SHORT).show();
      	  mainWifiObj.setWifiEnabled(true);
      	  Toast.makeText(act.getApplicationContext(),"Enabled", Toast.LENGTH_LONG).show();
        }
	}
}
