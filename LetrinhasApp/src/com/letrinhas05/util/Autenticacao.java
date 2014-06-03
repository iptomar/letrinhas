package com.letrinhas05.util;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Professor;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Autenticacao extends Activity {
	String userName, passWord="", pin="";
	// EditText password;
	ImageView img[];

	Button btn[], cancel, login;
	LetrinhasDB db;
	int id, controlo = 0;
	boolean blok = true;

	/**
	 * Atcivity para bloquear o acesso do aluno às escolhas de modo de execução
	 * dos testes e escolha dos intervenientes
	 * 
	 * @author Thiago
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autenticacao);

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

		/*
		 * UI elements gets bind in form of Java Objects password =
		 * (EditText)findViewById(R.id.password); login =
		 * (Button)findViewById(R.id.login); // now we have got the handle over
		 * the UI widgets // setting listener on Login Button // i.e. OnClick
		 * Event db = new LetrinhasDB(this); Bundle b = getIntent().getExtras();
		 * id = b.getInt("idProf"); //passWord = b.getString("pass"); passWord =
		 * db.getProfessorById(id).getPassword(); Log.d("authentication",
		 * "pass->"+passWord); login.setOnClickListener(loginListener);
		 */

	}

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

				passWord = "" + 3756;

				////////////////////////////////////////////////////////////
				if (passWord.equals(pin)) {
					Intent data = new Intent();
					data.putExtra("Resultado", true);
					setResult(2, data);

					blok = false;
				}
				finish();
			}
		});

		btn[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(0);
			}
		});
		btn[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(1);
			}
		});
		btn[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(2);
			}
		});
		btn[3].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(3);
			}
		});
		btn[4].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(4);
			}
		});
		btn[5].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(5);
			}
		});
		btn[6].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(6);
			}
		});
		btn[7].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(7);
			}
		});
		btn[8].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				ins(8);
			}
		});
		btn[9].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
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
				login.setEnabled(true);
			}
		}
	}

	/*
	 * private OnClickListener loginListener = new OnClickListener() { public
	 * void onClick(View v) { //vai buscar os dados que o utilizador introduzio
	 * if(password.getText().toString().equals(passWord)){ //responde aos inputs
	 * do user Toast.makeText(getApplicationContext(), "Login Successfully",
	 * Toast.LENGTH_LONG).show(); //Intent it= new Intent(Autenticacao.this,
	 * EscModo.class); // startActivity(it); }else
	 * Toast.makeText(getApplicationContext(), "Wrong Pin",
	 * Toast.LENGTH_LONG).show(); } };
	 */
}