package com.letrinhas02.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Classe para garantir que existe conecção à rede, seja por WI-FI
 * (preferencialmente) 3G, 4G, GSM
 * 
 * 
 * @author Dário
 * 
 */
public class coneccaoW extends Thread{
	WifiManager mainWifiObj;
	Activity act;

	public coneccaoW(Activity act) {
		this.act = act;
	}

	@Override
	public void run() {
		mainWifiObj = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
		// Automatic Connection to wifi
		if (mainWifiObj.isWifiEnabled()) {
			// wifi is enabled
			Toast.makeText(act.getApplicationContext(), "WI-FI Ligado",
					Toast.LENGTH_LONG).show();
		} else {
			try {
				Toast.makeText(act.getApplicationContext(), "WI-FI Desligado",
						Toast.LENGTH_SHORT).show();
				Thread.sleep(500);
				Toast.makeText(act.getApplicationContext(), "A Ligar",
						Toast.LENGTH_SHORT).show();
				Thread.sleep(500);
				mainWifiObj.setWifiEnabled(true);
				Toast.makeText(act.getApplicationContext(), "WI-FI Ligado",
						Toast.LENGTH_LONG).show();
			} catch (InterruptedException e) {
				Toast.makeText(act.getApplicationContext(),
						"Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		
		
	}
}
