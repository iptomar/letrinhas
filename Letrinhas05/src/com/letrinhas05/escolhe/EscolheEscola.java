package com.letrinhas05.escolhe;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.letrinhas05.PaginaInicial;
import com.letrinhas05.R;
import com.letrinhas05.Teste_Texto_Aluno;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Escola;
import com.letrinhas05.util.Custom;
import com.letrinhas05.util.SystemUiHider;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class EscolheEscola extends Activity {

	Button volt, exect;
	public int nEscolas, numero = 0;
	List<Escola> escolas;
	LetrinhasDB db;
	int[] id;
	String[] morada;
	String[] img;
	String[] nome;
	byte[] logotipo;
	ListView list;
	Integer[] image;

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
		setContentView(R.layout.activity_escolhe_escola);

		// Não necessitamos de tantos dados para esta Pagina *******

		// Cria o objecto da base de dados
		// db = new LetrinhasDB(this);
		// escolas = db.getAllSchools();
		// morada = new String[escolas.size()];
		// nome = new String[escolas.size()];

		// img = new String[escolas.size()];
		// id = new int[escolas.size()];
		// nEscolas = escolas.size();
		/*
		 * for (Escola cn : escolas) { String storage =
		 * cn.getMorada()+","+cn.getIdEscola
		 * ()+","+cn.getNome()+","+cn.getLogotipoNome();
		 * Log.d("letrinhas-Escola", storage.toString()); morada[numero] =
		 * cn.getMorada(); Log.d("letrinhas-Tipo",String.valueOf(morada[0]));
		 * nome[numero] = cn.getNome(); Log.d("letrinhas-Titulo",
		 * nome[0].toString()); id[numero] = cn.getIdEscola();
		 * Log.d("letrinhas-ID", String.valueOf(id[0])); img[numero] =
		 * cn.getLogotipoNome(); Log.d("letrinhas-IMG", img[0]); setUp(nome,
		 * img, id); numero++; }
		 */

		// new line faz a rotação do ecrãn em 180 graus
				int currentOrientation = getResources().getConfiguration().orientation;
				if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				} else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}
		
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

		volt = (Button) findViewById(R.id.btnVoltar);
		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da ativity
				Intent it = new Intent(getApplicationContext(),
						PaginaInicial.class);
				startActivity(it);
				finish();
			}
		});
		makeTabela();
	}

	/**
	 * Novo método para criar o painel dinâmico para os botões de selecção da
	 * escola
	 * 
	 * @author Thiago
	 */
	@SuppressLint("NewApi")
	private void makeTabela() {
		// Cria o objecto da base de dados
		db = new LetrinhasDB(this);
		escolas = db.getAllSchools();
		nome = new String[escolas.size()];
		id = new int[escolas.size()];
		img = new String[escolas.size()];

		for (int i = 0; i < escolas.size(); i++) {
			nome[i] = escolas.get(i).getNome();
			id[i] = escolas.get(i).getIdEscola();
			img[i] = escolas.get(i).getLogotipoNome();
		}

		// img = new String[escolas.size()];
		// morada = new String[escolas.size()];
		// nEscolas = escolas.size();

		for (Escola cn : escolas) {
			String storage = cn.getMorada() + "," + cn.getIdEscola() + ","
					+ cn.getNome() + "," + cn.getLogotipoNome();
			Log.d("letrinhas-Escola", storage.toString());
			// morada[numero] = cn.getMorada();
			// Log.d("letrinhas-Tipo", String.valueOf(morada[0]));
			// nome[numero] = cn.getNome();
			// Log.d("letrinhas-Titulo", nome[0].toString());
			// id[numero] = cn.getIdEscola();
			// Log.d("letrinhas-ID", String.valueOf(id[0]));
			// img[numero] = cn.getLogotipoNome();
			// Log.d("letrinhas-IMG", img[0]);
			// setUp(nome, img, id);
			// numero++;
		}

		/**
		 * Scroll view com uma table de 4 colunas(max)
		 */
		// numero de elementos =15 Buscar esta informação à BD.
		// int nelement=15;//escolas.size(); //*********************************
		// nome das escolas
		// final String[] s = new String[nelement];//escolas.size()];

		// tabela a editar
		TableLayout tabela = (TableLayout) findViewById(R.id.tblEscolhe);
		// linha da tabela a editar
		TableRow linha = (TableRow) findViewById(R.id.linha01);
		// 1º botão
		Button bt = (Button) findViewById(R.id.Button04);
		bt.setText("teste escolas");

		// Contador de controlo
		int cont = 0;
		for (int i = 0; i < escolas.size() / 4; i++) {
			// nova linha da tabela
			TableRow linha1 = new TableRow(getBaseContext());
			// Copiar os parametros da 1ª linha
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < 4; j++) {

				// **********************************
				// Nome da escola,

				final String school = nome[cont];
				final int idEs = id[cont];
				// ***********************************

				// novo botão
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do botão original
				bt1.setLayoutParams(bt.getLayoutParams());

				// se a escola já tiver logotipo, vou busca-lo
				if (img[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Schools/"+ img[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					//ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 240, 240, false));
					//enviar para o botão
					bt1.setCompoundDrawablesWithIntrinsicBounds(null, imageView.getDrawable(),
							null, null);
				} else {
					// senão copia a imagem do botão original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}

				// addicionar o nome
				bt1.setText(nome[cont]);
				// Defenir o que faz o botão ao clicar, neste caso muda o texto
				// do cabeçalho
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", school);
						wrap.putInt("Escola_ID", idEs);
						Intent it = new Intent(getApplicationContext(),
								EscolheProfessor.class);
						it.putExtras(wrap);

						startActivity(it);
						finish();
					}
				});
				// inserir o botão na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1);
		}

		// resto
		if (nome.length % 4 != 0) {
			TableRow linha1 = new TableRow(getBaseContext());
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < nome.length % 4; j++) {

				final String school = nome[cont];
				final int idEs = id[cont];

				Button bt1 = new Button(bt.getContext());
				bt1.setLayoutParams(bt.getLayoutParams());

				// se a escola já tiver logotipo, vou busca-lo
				if (img[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Schools/"+ img[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					//ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 240, 240, false));
					//enviar para o botão
					bt1.setCompoundDrawablesWithIntrinsicBounds(null, imageView.getDrawable(),
							null, null);

				} else {
					// senão copia a imagem do botão original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}
				
				bt1.setText(nome[cont]);
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", school);
						wrap.putInt("Escola_ID", idEs);

						Intent it = new Intent(getApplicationContext(),
								EscolheProfessor.class);
						it.putExtras(wrap);

						startActivity(it);
						finish();

					}
				});
				linha1.addView(bt1);
				cont++;

			}
			// inserir a linha criada com o resto dos botões
			tabela.addView(linha1);
		}

		// por fim escondo a 1ª linha
		tabela.removeView(linha);
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

}
