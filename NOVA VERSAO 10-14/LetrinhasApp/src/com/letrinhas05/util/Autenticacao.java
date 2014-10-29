package com.letrinhas05.util;

import android.app.ActionBar;
import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Classe de apoio à activity de autenticação PIN request.
 * 
 * @author Thiago
 *
 */
public class Autenticacao extends Activity {
	String userName, passWord="", pin="";
	// EditText password;
	ImageView img[];

	Button btn[], cancel, login;
	LetrinhasDB db;
	int id, controlo = 0;

	/**
	 * Atcivity para bloquear o acesso do aluno �s escolhas de modo de execu��o
	 * dos testes e escolha dos intervenientes
	 * 
	 * @author Thiago
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autenticacao);



        //////Ocultar barra de cima/////
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Hide the status bar.
        int uiOptionss = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptionss);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.



		Bundle b = getIntent().getExtras();
		passWord = b.getString("PIN");

		img = new ImageView[4];
		img[0] = (ImageView) findViewById(R.id.im1);
		img[1] = (ImageView) findViewById(R.id.im2);
		img[2] = (ImageView) findViewById(R.id.im3);
		img[3] = (ImageView) findViewById(R.id.im4);

		img[0].setVisibility(View.INVISIBLE);
		img[1].setVisibility(View.INVISIBLE);
		img[2].setVisibility(View.INVISIBLE);
		img[3].setVisibility(View.INVISIBLE);

		btn = new Button[10];
		btn[0] = (Button) findViewById(R.id.bt0);
		btn[1] = (Button) findViewById(R.id.bt1);
		btn[2] = (Button) findViewById(R.id.bt2);
		btn[3] = (Button) findViewById(R.id.bt3);
		btn[4] = (Button) findViewById(R.id.bt4);
		btn[5] = (Button) findViewById(R.id.bt5);
		btn[6] = (Button) findViewById(R.id.bt6);
		btn[7] = (Button) findViewById(R.id.bt7);
		btn[8] = (Button) findViewById(R.id.bt8);
		btn[9] = (Button) findViewById(R.id.bt9);

		login = (Button) findViewById(R.id.btnOk);
		login.setEnabled(false);
		cancel = (Button) findViewById(R.id.btnAutCancel);

		// por defeito envio um false, para o caso de cancelar
		Intent data = new Intent();
		data.putExtra("Resultado", false);
		setResult(2, data);

		escutaBotoes();

	}

	/**
	 * Garantir que vai assumir o cancelar
	 * 
	 * @author Thiago
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		cancel.performClick();
	}

	private void escutaBotoes() {
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				finish();
			}
		});

		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// /////////////////////////////////////////////////////////
				// se o pin estiver correto
				// /////////// Neste teste o pin = 3756 ////////////////////
				String passWdDef = "" + 3756;

				////////////////////////////////////////////////////////////
				if (passWord.equals(pin) || passWdDef.equals(pin)) {
					Intent data = new Intent();
					data.putExtra("Resultado", true);
					setResult(2, data);

				}
				finish();
			}
		});

		btn[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(0);
			}
		});
		btn[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(1);
			}
		});
		btn[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(2);
			}
		});
		btn[3].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(3);
			}
		});
		btn[4].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(4);
			}
		});
		btn[5].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(5);
			}
		});
		btn[6].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(6);
			}
		});
		btn[7].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(7);
			}
		});
		btn[8].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(8);
			}
		});
		btn[9].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ins(9);
			}
		});

	}

	private void ins(int input) {
		// se o botão ainda nao esta disponivel, inserir numeros
		if (!login.isEnabled()) {
			pin += "" + input;
			img[controlo].setVisibility(View.VISIBLE);
			controlo++;
			// a melhorar isto
			if (controlo == 4) {
				//desbloqueia o avancar
				login.setEnabled(true);
				//bloqueia o botoes num�ricos
				for (int i = 0; i < btn.length; i++) {
					btn[i].setEnabled(false);
				}
			}
		}
	}

}