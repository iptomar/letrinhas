package com.letrinhas05.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Classe para garantir que existe conecção à rede, por WI-FI
 * 
 * @author Dário
 * 
 */
public class coneccaoW extends Thread {
	WifiManager mainWifiObj;
	Activity act;

	public coneccaoW(Activity act) {
		this.act = act;
	}

	@Override
	public void run() {
		mainWifiObj = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
		// Automatic Connection to wifi
		if (!mainWifiObj.isWifiEnabled()) {// se estiver desligado, vai ligar-se
			Toast.makeText(act.getApplicationContext(), "WI-FI está desligado.", 
					Toast.LENGTH_SHORT).show();
			Toast.makeText(act.getApplicationContext(), "Estou a Ligar WI-FI.",
					Toast.LENGTH_SHORT).show();
			mainWifiObj.setWifiEnabled(true);
			Toast.makeText(act.getApplicationContext(), "O WI-FI já está ligado.", 
					Toast.LENGTH_SHORT).show();
		}

	}
}
