package com.letrinhas03.util;

import com.letrinhas03.SincAllBd;

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
			 /////////////////////////////////////////////////////////////////////////////////////////////////////
	        ///////////CHAMA EM BAKCGORUND A SINCRO DE TABELAS E INSERE NA BASE DE DADOS///////////////////
	        String ip = "code.dei.estt.ipt.pt";  ////TROCAR ISTO POR VARIAVEIS COM OS ENDEREÃ‡OS IP QUE NAO SEI ONDE TEM/////////
	        String porta = "80";
	        //Forma o endereÃ§o http
	        String   URlString = "http://" + ip + ":" + porta + "/";


	        String[] myTaskParams = { URlString, URlString, URlString };
	        new SincAllBd(act.getApplicationContext()).execute(myTaskParams);
		}
		
		Toast.makeText(act.getApplicationContext(), "A Sincronizar a BD...", 
				Toast.LENGTH_SHORT).show();

	}
}
