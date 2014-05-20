package com.letrinhas05;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RelatasCorrection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatas_correction);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.relatas_correction, menu);
		return true;
	}

}
