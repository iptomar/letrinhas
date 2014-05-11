package com.letrinhas04;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.letrinhas03.R;
import com.letrinhas04.ClassesObjs.Escola;
import com.letrinhas04.util.SystemUiHider;

public class EscolheAno extends Activity {

	ImageButton volt, exect;
	public int nEscolas;
	//boolean modo;
	List<Escola> escolas;
	//Teste[] lista;
	//LetrinhasDB db;
	Escola escola;

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
		setContentView(R.layout.escolhe_escola);
		
		
		
		//Cria o objecto da base de dados
		//db = new LetrinhasDB(this);
		
		

		// recebe o parametro de modo
		//Bundle b = getIntent().getExtras();
		//modo = b.getBoolean("Modo");

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escEscola);

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
		 * Criação de um painel dinâmico para os botões de selecção das escolas
		 * existentes.
		 * 
		 * 
		 */

		// Em comentario codigo preparado para ler da BD
//		escolas = db.getAllSchools();
//		nEscolas = escolas.size();
		nEscolas = 3;
		//escolas = new Teste[nEscolas];
		
		
		for (int i = 0; i < nEscolas; i++) {
			//int tip = i; // tipo texto
			
			//escolas[i] = new Teste(i, 0, "Atirei o Pau ao gato.");
			escola.setIdEscola(i);
			escola.setNome("Escola " + i + 1);
			escolas.add(escola);
		}

		// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

		// Painel dinâmico ****************************************************
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescescola);
		// Botão original que existe por defenição
		Button bt1 = (Button) findViewById(R.id.Button_escl);

		// Cria o nº de botões referentes ao nº de escolas presentes na lista
		if (0 < nEscolas) {
			int i = 0;
			// Atribir a primeira escola ao primeiro botão
			// ********************************+
			// Nome da Escola
			bt1.setText(escolas.get(i).getNome());
			i++;
			// Resto das escolas
			while (i <= nEscolas) {
				// um novo botão
				Button bt = new Button(getBaseContext());
				// copiar os parametros de layout do 1º botão
				bt.setLayoutParams(bt1.getLayoutParams());
				bt.setTextSize(bt1.getTextSize());
				// Nome da Escola
				bt.setText(escolas.get(i).getNome());
				// inserir no scroll view
				ll.addView(bt);
				i++;
			}
		} else {
			// esconder os botões
			bt1.setVisibility(View.INVISIBLE);
			exect.setVisibility(View.INVISIBLE);

			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas 03");
			// define a mensagem
			builder.setMessage("Não foram encontradas escolas no sistema");
			// define um botão como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();
			
		}

		volt = (ImageButton) findViewById(R.id.escTVoltar_escl);
		exect = (ImageButton) findViewById(R.id.ibComecar_escl);

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
				//executarTestes();
				Toast.makeText(getApplicationContext(), " - Tipo não defenido",
				Toast.LENGTH_SHORT).show();
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
//	public void executarTestes() {
//		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
//		// contar o nº de elementos (testes)
//		int nElements = ll.getChildCount();
//
//		int j = 0;
//		// contar quantos e quais foram selecionados
//		for (int i = 0; i < nElements; i++) {
//			// verificar se o teste está ativo
//			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
//				j++;
//			}
//		}
//		Toast.makeText(getApplicationContext(), j + " Testes seleccionados",
//				Toast.LENGTH_SHORT).show();
//
//		// Copiar os testes seleccionados para uma lista auxiliar
//		lista = new Teste[j];
//		j = 0;
//		for (int i = 0; i < nElements; i++) {
//			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
//				lista[j] = teste[i];
//				j++;
//			}
//		}
//
//		iniciar(j);
//	}

//	public void iniciar(int j) {
//		// iniciar os testes....
//		// Se existir items seleccionados arranca com os testes,
//		if (0 < j) {
//			// Decompor o array de teste, para poder enviar por parametros
//			int[] lstID = new int[lista.length];
//			int[] lstTipo = new int[lista.length];
//			String[] lstTitulo = new String[lista.length];
//			for (int i = 0; i < lista.length; i++) {
//				lstID[i] = lista[i].getID();
//				lstTipo[i] = lista[i].getTipo();
//				lstTitulo[i] = lista[i].getTitulo();
//			}
//
//			// enviar o parametro de modo
//			Bundle wrap = new Bundle();
//			wrap.putBoolean("Modo", modo);
//
//			// teste, a depender das informações da BD
//			// ****************************************************************************
//			wrap.putString("Aluno", "EI3-Tiago Fernandes");
//			wrap.putString("Professor", "ESTT- Pedro Dias");
//
//			// resto dos parametros
//			wrap.putIntArray("ListaID", lstID);
//			wrap.putIntArray("ListaTipo", lstTipo);
//			wrap.putStringArray("ListaTitulo", lstTitulo);
//
//			switch (lista[0].getTipo()) {
//			case 0: // lançar a nova activity do tipo texto,
//
//				Intent it = new Intent(getApplicationContext(),
//						Teste_Texto.class);
//				it.putExtras(wrap);
//
//				startActivity(it);
//
//				break;
//			case 1:// lançar a nova activity do tipo Palavras, e o seu conteúdo
//					//
//				Intent ip = new Intent(getApplicationContext(),
//						Teste_Palavras.class);
//				ip.putExtras(wrap);
//
//				startActivity(ip);
//
//				break;
//			case 2: // lançar a nova activity do tipo Poema, e o seu conteúdo
//				//
//				Intent ipm = new Intent(getApplicationContext(),
//						Teste_Poema.class);
//				ipm.putExtras(wrap);
//
//				startActivity(ipm);
//
//				break;
//			case 3: // lançar a nova activity do tipo imagem, e o seu conteúdo
//				//
//				// Intent it = new Intent(getApplicationContext(),
//				// Teste_Texto.class);
//				// it.putExtras(wrap);
//
//				// startActivity(it);
//				break;
//			default:
//				Toast.makeText(getApplicationContext(), " - Tipo não defenido",
//						Toast.LENGTH_SHORT).show();
//				// retirar o teste errado e continuar
//
//				int k = 0;
//				Teste aux[] = new Teste[lista.length - 1];
//				for (int i = 1; i < lista.length; i++) {
//					aux[k] = lista[i];
//					k++;
//				}
//				lista = aux;
//				iniciar(lista.length);
//				break;
//			}
//
//		} else {// senão avisa que não existe nada seleccionado
//			android.app.AlertDialog alerta;
//			// Cria o gerador do AlertDialog
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			// define o titulo
//			builder.setTitle("Letrinhas 03");
//			// define a mensagem
//			builder.setMessage("Não existem testes seleccionados!");
//			// define um botão como positivo
//			builder.setPositiveButton("OK", null);
//			// cria o AlertDialog
//			alerta = builder.create();
//			// Mostra
//			alerta.show();
//		}
//
//	}


}
