package com.letrinhas05;

import com.letrinhas05.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Resultado extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultado);

		// buscar os parametros
		Bundle b = getIntent().getExtras();
		((TextView)findViewById(R.id.resCabecalho)).setText(b.getString("teste"));
		((TextView)findViewById(R.id.tvResultado)).setText(b.getString("Avaliac"));
		((ImageButton)findViewById(R.id.resAvancar)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

	}


}
