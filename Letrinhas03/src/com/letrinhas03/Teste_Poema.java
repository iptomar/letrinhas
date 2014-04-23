package com.letrinhas03;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Teste_Poema extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teste__poema);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teste__poema, menu);
		return true;
	}

}
