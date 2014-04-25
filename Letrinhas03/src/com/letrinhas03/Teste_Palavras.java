package com.letrinhas03;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class Teste_Palavras extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teste_palavras);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teste__palavras, menu);
		return true;
	}
}
