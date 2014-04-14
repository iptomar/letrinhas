package com.letrinhas02;

import com.letrinhas02.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class EscModo extends Activity {
	ImageButton aluno, prof, volt;

	/*********************************************************************
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.esc_modo);

		// esconder o title
		final View contentView = findViewById(R.id.escModo);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();

		// inicializar os botões
		aluno = (ImageButton) findViewById(R.id.ecmAluno);
		prof = (ImageButton) findViewById(R.id.esmProf);
		volt = (ImageButton) findViewById(R.id.escmVoltar);

		escutaBotoes();
	}

	@Override
	public boolean hasWindowFocus() {
		// esconder o title
		final View contentView = findViewById(R.id.escModo);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();
		return true;
	}

	private void escutaBotoes() {
		aluno.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {modAluno();	}});

		prof.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {modProf();}});

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// voltar para pag inicial
				finish();}});
	}

	public void modAluno() {
		((TextView) findViewById(R.id.tvmoAluno)).setTextColor(Color.GREEN);
		((TextView) findViewById(R.id.tvmoProf)).setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
		
		// iniciar a pagina 2 (escolher teste)
		Intent it = new Intent(EscModo.this, EscolheTeste.class);
		startActivity(it);
		
	}

	public void modProf() {
		// declarar de como o teste será apresentado!

		((TextView) findViewById(R.id.tvmoProf)).setTextColor(Color.GREEN);
		((TextView) findViewById(R.id.tvmoAluno)).setTextColor(Color.rgb(0x5d,0xdf, 0xff));
		
		// iniciar a pagina 2 (escolher teste)
		Intent it = new Intent(EscModo.this, EscolheTeste.class);
		startActivity(it);
		
	}


}
