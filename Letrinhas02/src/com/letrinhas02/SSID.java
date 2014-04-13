package com.letrinhas02;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SSID extends Activity {
	TextView tvSsid,tvPass;
	Button button;
	String value;
	WifiManager mainWifiObj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ssid);
		tvSsid = (TextView)findViewById(R.id.SSID);
		tvPass = (TextView)findViewById(R.id.PASSW);
		button = (Button)findViewById(R.id.OK);
		Bundle b = getIntent().getExtras();
		value = b.getString("key");
		String[] separated = value.split(" ");
		value = separated[1];
		String[] separated1 = value.split(",");
		value = separated1[0];
		//Toast.makeText(getApplicationContext(),value, Toast.LENGTH_LONG).show();
		tvSsid.setText(value);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void OK(View view){
		Toast.makeText(getApplicationContext(),"herehere", Toast.LENGTH_LONG).show();
		connectToSSID("aluno17104@ipt.pt", ".PuqnCa");
		Toast.makeText(getApplicationContext(),"here", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, ConnectionList.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connection_list, menu);
		return true;
	}

	/**
	 * 
	 * @param ssid
	 * @param key
	 */
	public void connectToSSID(final String ssid, final String key) {
		WifiConfiguration wc=new WifiConfiguration();
		wc.SSID="\"" + ssid + "\"";
		wc.preSharedKey = "\"" + key + "\"";
		wc.status = WifiConfiguration.Status.ENABLED;
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

		int netId=mainWifiObj.addNetwork(wc);
		mainWifiObj.enableNetwork(netId, true);
}
	}
