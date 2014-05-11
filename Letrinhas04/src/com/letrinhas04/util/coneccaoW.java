package com.letrinhas04.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

/**
 * Classe para garantir que existe conecção à rede, seja por WI-FI
 * (preferencialmente) 3G, 4G, GSM
 * 
 * 
 * @author Dário
 * 
 */
public class coneccaoW extends Thread {
	WifiManager mainWifiObj;
	Activity act;
	protected static final int desligado = 0, ligado=1;
	Handler handler;
	public coneccaoW(Activity act, Handler hand) {
		this.act = act;
		this.handler=hand;
	}

	@Override
	public void run() {
		mainWifiObj = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
		// Automatic Connection to wifi
		if (!mainWifiObj.isWifiEnabled()) {// se estiver desligado, vai ligar-se
			Message msg = new Message();
			msg.what = desligado;
			handler.sendMessage(msg);
			mainWifiObj.setWifiEnabled(true);
			msg.what= ligado;
			handler.sendMessage(msg);			
		}
		

	}
}
