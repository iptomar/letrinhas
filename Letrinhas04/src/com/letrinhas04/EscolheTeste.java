package com.letrinhas04;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.letrinhas03.R;
import com.letrinhas04.BaseDados.LetrinhasDB;
import com.letrinhas04.ClassesObjs.Teste;
import com.letrinhas04.util.SystemUiHider;

public class EscolheTeste extends Activity {
	ImageButton volt, exect;
	public int nTestes,numero=0;
	boolean modo;
	String teste;
	String[] lista;
	String[] array;
	int[] tipo,id;
	String[] titulo;
	String[] texto;
	LetrinhasDB ldb;

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
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

		// recebe o parametro de modo
		Bundle b = getIntent().getExtras();
		modo = b.getBoolean("Modo");

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escTeste);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		/************************************************************************
		 * Criação de um painel dinâmico para os botões de selecção dos testes
		 * existentes.
		 * 
		 * É necessário de saber primeiro onde estão os testes e quantos são!
		 * (Comunicar com a BD)
		 * 
		 * aceder à BD local, contar o nº de testes, ex: nTestes
		 * ="Conta(blá.blá.blá)" ; teste= new Teste[nTestes];
		 * 
		 * e os seus títulos guardar essa informação num array para se aceder na
		 * construção do scroll view. ex: for(int i=0;i<teste.length i++){ int
		 * tipo= "tipo(blá.blá.blá)"; String tit = "titulo(blábláblá)"; String
		 * ender = "endereço(blábláblá)"; teste[i]=new Teste(tip,tit,ender); }
		 * 
		 */

		// teste:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		ldb = new LetrinhasDB(this);
		List<Teste> dados = ldb.getAllTeste();
		array = new String[dados.size()];
		tipo = new int[dados.size()];
		titulo = new String[dados.size()];
		texto = new String[dados.size()];
		id = new int[dados.size()];
		for (Teste cn : dados) {
           // String storage = cn.getIdTeste()+","+cn.getTitulo().toString()+","+cn.getTexto().toString()+","+cn.getTipo()+","+cn.getDataInsercaoTeste()+","+cn.getGrauEscolar();
			String storage = cn.getIdTeste()+","+cn.getTitulo().toString()+","+cn.getTexto().toString()+","+cn.getTipo();
            Log.d("letrinhas-Store", storage.toString());
            array[numero] = storage.toString();
            Log.d("letrinhas-Array", array[0].toString());
            tipo[numero] = cn.getTipo();
            Log.d("letrinhas-Tipo",String.valueOf(tipo[0]));
            titulo[numero] = cn.getTitulo();
            Log.d("letrinhas-Titulo", titulo[0].toString());
            texto[numero] = cn.getTexto();
            Log.d("letrinhas-Texto", texto[0].toString());
            id[numero] = cn.getIdTeste();
            Log.d("letrinhas-ID", String.valueOf(id[0]));
            numero++;
        }
		// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

		// Painel dinâmico ****************************************************
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// Botão original que existe por defenição
		ToggleButton tg1 = (ToggleButton) findViewById(R.id.ToggleButton1);
		// Se existirem testes no repositório correspondentes, cria o nº de
		// botões referentes ao nº de testes existentes
		if (0 < numero) {
			int i = 0;
			teste = titulo[i].toString();
			Log.d("Texto-Apenas", teste+" int:"+numero);
			// Atribuo o primeiro título ao primeiro botão
			// ********************************+
			// texto por defeito
			tg1.setText(teste);
			// texto se não seleccionado = "titulo do teste sem numeração"
			tg1.setTextOff(teste);
			// texto se seleccionado = "titulo do teste com numeração"
			tg1.setTextOn((i + 1) + " - " + teste);
			i++;

			// Resto do títulos
			while (i < numero) {
				// um novo botão
				ToggleButton tg = new ToggleButton(getBaseContext());
				// copiar os parametros de layout do 1º botão
				tg.setLayoutParams(tg1.getLayoutParams());
				tg.setTextSize(tg1.getTextSize());
				teste = titulo[i].toString();
				Log.d("Texto-Apenas-part2", teste+" int:"+i);
				// texto por defeito
				tg.setText(teste);
				// texto se não seleccionado = "titulo do teste sem numeração"
				tg.setTextOff(teste);
				// texto se seleccionado = "titulo do teste com numeração"
				tg.setTextOn((i + 1) + " - " + teste);
				// inserir no scroll view
				ll.addView(tg);
				i++;
			}
		} else {
			// esconder os botões
			tg1.setVisibility(View.INVISIBLE);
			exect.setVisibility(View.INVISIBLE);

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
			
		}

		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);

		escutaBotoes();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(1000);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
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

	/**
	 * Procedimento para executar os testes selecionados, um de cada vez
	 * 
	 * @author Thiago
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

		// Copiar os testes seleccionados para uma lista auxiliar
		lista = new String[j];
		j = 0;
		for (int i = 0; i < nElements; i++) {
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				lista[j] = array[i];
				j++;
			}
		}

		iniciar(j);
	}

	public void iniciar(int j) {
		// iniciar os testes....
		// Se existir items seleccionados arranca com os testes,
		if (0 < j) {
			// Decompor o array de teste, para poder enviar por parametros
			int[] lstID = new int[lista.length];
			int[] lstTipo = new int[lista.length];
			String[] lstTitulo = new String[lista.length];
			String[] lstTexto = new String[lista.length];
			for (int i = 0; i < lista.length; i++) {
				lstID[i] = id[i];
				lstTipo[i] = tipo[i];
				lstTitulo[i] = titulo[i];
				lstTexto[i] = texto[i];
			}

			// enviar o parametro de modo
			Bundle wrap = new Bundle();
			wrap.putBoolean("Modo", modo);

			// teste, a depender das informações da BD
			// ****************************************************************************
			wrap.putString("Aluno", "EI3-Tiago Fernandes");
			wrap.putString("Professor", "ESTT- Pedro Dias");

			// resto dos parametros
			wrap.putIntArray("ListaID", lstID);
			wrap.putIntArray("ListaTipo", lstTipo);
			wrap.putStringArray("ListaTitulo", lstTitulo);
			wrap.putStringArray("ListaTexto", lstTexto);
			wrap.putStringArray("Storage", array);

			switch (tipo[0]) {
			case 0: // lançar a nova activity do tipo texto,

				Intent it = new Intent(getApplicationContext(),
						Teste_Texto.class);
				it.putExtras(wrap);

				startActivity(it);

				break;
			case 1:// lançar a nova activity do tipo Palavras, e o seu conteúdo
					//
				Intent ip = new Intent(getApplicationContext(),
						Teste_Palavras.class);
				ip.putExtras(wrap);

				startActivity(ip);

				break;
			case 2: // lançar a nova activity do tipo Poema, e o seu conteúdo
				//
				Intent ipm = new Intent(getApplicationContext(),
						Teste_Poema.class);
				ipm.putExtras(wrap);

				startActivity(ipm);

				break;
			case 3: // lançar a nova activity do tipo imagem, e o seu conteúdo
				//
				// Intent it = new Intent(getApplicationContext(),
				// Teste_Texto.class);
				// it.putExtras(wrap);

				// startActivity(it);
				break;
			default:
				Toast.makeText(getApplicationContext(), " - Tipo não defenido",
						Toast.LENGTH_SHORT).show();
				// retirar o teste errado e continuar

				/*int k = 0;
				Teste aux[] = new Teste[lista.length - 1];
				for (int i = 1; i < lista.length; i++) {
					aux[k] = lista[i];
					k++;
				}
				lista = aux;
				iniciar(lista.length);*/
				break;
			}

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
