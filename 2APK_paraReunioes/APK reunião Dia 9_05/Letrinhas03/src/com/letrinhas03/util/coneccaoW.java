package com.letrinhas03.util;

import com.letrinhas03.SincAllBd;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Classe para garantir que existe conec��o � rede, seja por WI-FI
 * (preferencialmente) 3G, 4G, GSM
 * 
 * 
 * @author D�rio
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
			Toast.makeText(act.getApplicationContext(), "WI-FI est� desligado.", 
					Toast.LENGTH_SHORT).show();
			Toast.makeText(act.getApplicationContext(), "Estou a Ligar WI-FI.",
					Toast.LENGTH_SHORT).show();
			mainWifiObj.setWifiEnabled(true);
			Toast.makeText(act.getApplicationContext(), "O WI-FI j� est� ligado.", 
					Toast.LENGTH_SHORT).show();
			 /////////////////////////////////////////////////////////////////////////////////////////////////////
	        ///////////CHAMA EM BAKCGORUND A SINCRO DE TABELAS E INSERE NA BASE DE DADOS///////////////////
	        String ip = "code.dei.estt.ipt.pt";  ////TROCAR ISTO POR VARIAVEIS COM OS ENDEREÇOS IP QUE NAO SEI ONDE TEM/////////
	        String porta = "80";
	        //Forma o endereço http
	        String   URlString = "http://" + ip + ":" + porta + "/";


	        String[] myTaskParams = { URlString, URlString, URlString };
	        new SincAllBd(act.getApplicationContext()).execute(myTaskParams);
		}
		
		Toast.makeText(act.getApplicationContext(), "A Sincronizar a BD...", 
				Toast.LENGTH_SHORT).show();

	}
}
