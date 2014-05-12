package com.letrinhas04.escolhe;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.letrinhas04.R;
import com.letrinhas04.BaseDados.LetrinhasDB;
import com.letrinhas04.ClassesObjs.Escola;
import com.letrinhas04.ClassesObjs.Professor;
import com.letrinhas04.util.SystemUiHider;

public class EscolheProfessor extends Activity {

	Button volt;
	public int nProfs, numero = 0, idEscola;
	String Escola;
	List<Professor> profs;
	LetrinhasDB db;
	ListView list;
	Integer[] image;
	int[] idProf;
	String[] username;
	String[] password;
	String[] telefone;
	String[] email;
	String[] fotoNome;
	int[] estado;

	// Escola escola;
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
		setContentView(R.layout.activity_escolhe_professor);

		// sacar os extras
		Bundle b = getIntent().getExtras();
		idEscola = b.getInt("Escola_ID");
		Escola = b.getString("Escola");
		((TextView) findViewById(R.id.escPEscola)).setText(Escola);

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escProf);

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

		// Botão de voltar
		volt = (Button) findViewById(R.id.btnVoltarProf);
		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// sair da activity
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
		profs = db.getAllProfesorsBySchool(idEscola);
		nProfs = profs.size();
		idProf = new int[profs.size()];
		username = new String[profs.size()];
		fotoNome = new String[profs.size()];
		;

		// password = new String[profs.size()];
		// telefone = new String[profs.size()];
		// email = new String[profs.size()];
		// estado = new int[profs.size()];
		for (int i = 0; i < nProfs; i++) {
			username[i] = profs.get(i).getNome();
			idProf[i] = profs.get(i).getId();
			fotoNome[i] = profs.get(i).getFotoNome();
		}

		for (Professor cn : profs) {
			String storage = cn.getEmail() + "," + cn.getFotoNome() + ","
					+ cn.getId() + "," + cn.getNome() + "," + cn.getPassword()
					+ "," + cn.getTelefone() + "," + cn.getUsername();
			Log.d("letrinhas-Escola", storage.toString());
			// password[numero] = cn.getPassword();
			// Log.d("letrinhas-Tipo", String.valueOf(password[0]));
			// username[numero] = cn.getNome();
			// Log.d("letrinhas-Titulo", username[0].toString());
			// idProf[numero] = cn.getId();
			// Log.d("letrinhas-ID", String.valueOf(idProf[0]));
			// fotoNome[numero] = cn.getFotoNome();
			// Log.d("letrinhas-IMG", fotoNome[0]);
			// setUp(username, fotoNome, idProf, username, password);
			// numero++;
		}

		/**
		 * Scroll view com uma table de 4 colunas(max)
		 */
		// tabela a editar
		TableLayout tabela = (TableLayout) findViewById(R.id.tblEscolheProf);
		// linha da tabela a editar
		TableRow linha = (TableRow) findViewById(R.id.Proflinha01);
		// 1º botão
		Button bt = (Button) findViewById(R.id.PrfBtOriginal);
		bt.setText("teste professores");

		// Contador de controlo
		int cont = 0;
		for (int i = 0; i < nProfs / 4; i++) {
			// nova linha da tabela
			TableRow linha1 = new TableRow(getBaseContext());
			// Copiar os parametros da 1ª linha
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < 4; j++) {

				// **********************************
				// Nome do professor

				final String proff = username[cont];
				final int idPrf = idProf[cont];
				// ***********************************

				// novo botão
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do botão original
				bt1.setLayoutParams(bt.getLayoutParams());

				// se a escola já tiver logotipo, vou busca-lo
				if (fotoNome[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Professors/" + fotoNome[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					// ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
							250, 250, false));
					// enviar para o botão
					bt1.setCompoundDrawablesWithIntrinsicBounds(null,
							imageView.getDrawable(), null, null);
				} else {
					// senão copia a imagem do botão original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}

				// addicionar o nome
				bt1.setText(username[cont]);
				// Defenir o que faz o botão ao clicar, neste caso muda o texto
				// do cabeçalho
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", Escola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", proff);
						wrap.putInt("Professor_ID", idPrf);

						Intent it = new Intent(getApplicationContext(),
								EscolheTurma.class);
						it.putExtras(wrap);

						startActivity(it);
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
		if (nProfs % 4 != 0) {
			TableRow linha1 = new TableRow(getBaseContext());
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < nProfs % 4; j++) {
				// **********************************
				// Nome do professor

				final String proff = username[cont];
				final int idPrf = idProf[cont];
				// ***********************************

				// novo botão
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do botão original
				bt1.setLayoutParams(bt.getLayoutParams());

				// se a escola já tiver logotipo, vou busca-lo
				if (fotoNome[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Professors/" + fotoNome[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					// ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
							250, 250, false));
					// enviar para o botão
					bt1.setCompoundDrawablesWithIntrinsicBounds(null,
							imageView.getDrawable(), null, null);
				} else {
					// senão copia a imagem do botão original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}

				// addicionar o nome
				bt1.setText(username[cont]);
				// Defenir o que faz o botão ao clicar, neste caso muda o texto
				// do cabeçalho
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", Escola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", proff);
						wrap.putInt("Professor_ID", idPrf);

						Intent it = new Intent(getApplicationContext(),
								EscolheTurma.class);
						it.putExtras(wrap);

						startActivity(it);
					}
				});
				// inserir o botão na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada com o resto dos botões
			tabela.addView(linha1);
		}

		// por fim escondo a 1ª linha
		tabela.removeView(linha);
	}

	/*
	 * public void setUp(final String[] nome, String[] imgNome, final int[] id,
	 * final String[] userName, final String[] pass) { Custom adapter = new
	 * Custom(EscolheProfessor.this, nome, imgNome, "professores"); list =
	 * (ListView) findViewById(R.id.lista); list.setAdapter(adapter);
	 * list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long idd) { Toast.makeText(EscolheProfessor.this,
	 * "You Clicked at " + nome[+position], Toast.LENGTH_SHORT) .show(); /*
	 * Bundle wrap = new Bundle(); wrap.putInt("IdProf", id[position]);
	 * wrap.putString("pass", pass[position]); wrap.putString("user",
	 * userName[position]); Intent itp = new Intent(getApplicationContext(),
	 * Autenticacao.class); itp.putExtras(wrap); startActivity(itp);
	 * 
	 * } }); }
	 */

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
	/*
	 * private void escutaBotoes() { exect.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { //executarTestes();
	 * Toast.makeText(getApplicationContext(), " - Tipo não defenido",
	 * Toast.LENGTH_SHORT).show(); } });
	 * 
	 * volt.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) {// sair da aplicação finish();
	 * } }); }
	 */

}
