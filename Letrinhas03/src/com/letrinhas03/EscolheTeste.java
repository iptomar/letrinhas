package com.letrinhas03;

import com.letrinhas03.util.ExecutaTestes;
import com.letrinhas03.util.SystemUiHider;
import com.letrinhas03.util.Teste;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EscolheTeste extends Activity {
	ImageButton volt, exect;
	public int nTestes;
	boolean modo;
	Teste[] teste;

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
		setContentView(R.layout.escolhe_teste);
		//recebe o parametro de modo
		Bundle b = getIntent().getExtras();
		modo = b.getBoolean("Modo");

		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);
		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escTeste);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();

		/************************************************************************
		 * Criação de um painel dinâmico para os botões de selecção dos testes
		 * existentes.
		 * 
		 * É necessário de saber primeiro onde estão os testes e quantos são!
		 * (Comunicar com a BD)
		 * 
		 * aceder à BD local, contar o nº de testes, ex: 
		 * 		nTestes ="Conta(blá.blá.blá)" ; 
		 * 		teste= new Teste[nTestes];
		 * 
		 * e os seus títulos guardar essa informação num array para se aceder na
		 * construção do scroll view. ex:
		 * 			 for(int i=0;i<teste.length i++){ 
		 * 				int tipo= "tipo(blá.blá.blá)"; 
		 * 				String tit = "titulo(blábláblá)"; 
		 * 				String ender = "endereço(blábláblá)"; 
		 * 				teste[i]=new Teste(tip,tit,ender); 
		 * 			}
		 * 
		 */

		
		//teste:::::::::::
		nTestes = 30;
		teste = new Teste[nTestes];
		for (int i = 0; i < teste.length; i++) {
			int tip = i%3; //tipo texto
			String tit = "O título do teste";
			String ender =  "teste/myteste.txt";
			teste[i] = new Teste(tip, tit, ender);
		}//:::::::::::::::::::::::::::::::::::::::::::

		// Painel dinâmico ****************************************************
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// Botão original que existe por defenição
		ToggleButton tg1 = (ToggleButton) findViewById(R.id.ToggleButton1);

		// Se existirem testes no repositório correspondentes, cria o nº de
		// botões referentes ao nº de testes existentes
		if (0 < nTestes) {
			int i = 0;
			// Atribuo o primeiro título ao primeiro botão
			// ********************************+
			// texto por defeito
			tg1.setText(teste[i].getTitulo());
			// texto se não seleccionado = "titulo do teste sem numeração"
			tg1.setTextOff(teste[i].getTitulo());
			// texto se seleccionado = "titulo do teste com numeração"
			tg1.setTextOn((i + 1) + " - " + teste[i].getTitulo());
			i++;

			// Resto do títulos
			while (i < nTestes) {
				// um novo botão
				ToggleButton tg = new ToggleButton(getBaseContext());
				// copiar os parametros de layout do 1º botão
				tg.setLayoutParams(tg1.getLayoutParams());
				tg.setTextSize(tg1.getTextSize());
				// texto por defeito
				tg.setText(teste[i].getTitulo());
				// texto se não seleccionado = "titulo do teste sem numeração"
				tg.setTextOff(teste[i].getTitulo());
				// texto se seleccionado = "titulo do teste com numeração"
				tg.setTextOn((i + 1) + " - " + teste[i].getTitulo());
				// inserir no scroll view
				ll.addView(tg);
				i++;
			}
		} else {
			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas 03");
			// define a mensagem
			builder.setMessage("Não foram encontrados testes no repositório");
			// define um botão como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();
			
			//esconder os botões
			tg1.setVisibility(View.INVISIBLE);
			exect.setVisibility(View.INVISIBLE);
			
		}
		
		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);

		escutaBotoes();
	}

	/**
	 * Procedimento para voltar a esconder o titulo caso este esteja activo
	 * 
	 * @author Thiago
	 */
	@Override
	public boolean hasWindowFocus() {
		// esconder o title
		final View contentView = findViewById(R.id.escTeste);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();
		return true;
	}

	/**
	 * Procedimento para veirficar os botões
	 * 
	 * @author Thiago
	 */
	private void escutaBotoes() {
		exect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				executarTestes();
			}
		});

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da aplicação
				finish();
			}
		});
	}
	

	/**************************************************************************
	 * Por Fazer ******************************** executar os testes
	 * selecionados, um de cada vez
	 */
	public void executarTestes() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// contar o nº de elementos (testes)
		int nElements = ll.getChildCount();

		int j = 0;
		// contar quantos e quais foram selecionados
		for (int i = 0; i < nElements; i++) {
			// verificar se o teste está ativo
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				j++;
			}
		}
		Toast.makeText(getApplicationContext(), j + " Testes seleccionados",
				Toast.LENGTH_SHORT).show();
		
		//Copiar os testes seleccionados para uma lista auxiliar
		Teste []lista= new Teste[j];
		j = 0;
		for (int i = 0; i < nElements; i++) {
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				lista[j]=teste[i];
				j++;
			}
		}
		
		
		// iniciar os testes.... 
		// Se existe items seleccionados arranca com os testes,
		if (0 < j) {
			ExecutaTestes exect = new ExecutaTestes(this, modo, lista);
			exect.run(); // Método run, pois a DVM é burra!!! e não funciona
							// muito bem com as threads

		} else {// senão avisa que não existe nada seleccionado
			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas 03");
			// define a mensagem
			builder.setMessage("Não existem testes seleccionados!");
			// define um botão como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();
		}

	}

}
