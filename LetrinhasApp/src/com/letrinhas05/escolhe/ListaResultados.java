package com.letrinhas05.escolhe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.letrinhas05.Correcao_Poema;
import com.letrinhas05.Correcao_Texto;
import com.letrinhas05.R;
import com.letrinhas05.Resultado;
import com.letrinhas05.Teste_Palavras_Prof;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTeste;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.CorrecaoTesteMultimedia;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.util.Avaliacao;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;

/**
 * Activity para listar os resultados 
 * 
 * @author Thiago
 * 
 */
public class ListaResultados extends Activity {
	Button volt;

	int iDs[];
	String Nomes[];

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
		setContentView(R.layout.lista_resultados);

		// new line faz a rotacao do ecran em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.lResult);

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

		// Retirar os Extras
		Bundle b = getIntent().getExtras();
		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		Nomes = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");

		((TextView) findViewById(R.id.lREscola)).setText(Nomes[0]);
		((TextView) findViewById(R.id.lRtvProf)).setText(Nomes[1]);

		// se professor tem uma foto, usa-se
		if (Nomes[2] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.lRivProfessor));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Professors/" + Nomes[2];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100,
					100, false));
		}

		volt = (Button) findViewById(R.id.lRbtnVoltar);
		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		makeLista();

	}

	@SuppressLint("NewApi")
	private void makeLista() {

		LetrinhasDB bd = new LetrinhasDB(this);
		// vou buscar todas as submissoes de teste nao corrigidas existentes..
		// ... dos alunos, das turmas, do professor selecionado.
		List<CorrecaoTeste> ct = bd.getAllCorrecaoTesteByProfID(iDs[1]);

		// verifico se estas submissoes estao corrigidas
		int cont = 0;
		for (int i = 0; i < ct.size(); i++) {
			// se nao esta corrigido, conta-o
			if (ct.get(i).getEstado() == 1) {
				cont++;
			}
		}
		// objetos do XML
		LinearLayout ll = (LinearLayout) findViewById(R.id.llListRes);
		Button btOriginal = (Button) findViewById(R.id.btnLRCorrecao_Original);
		// remove o botao original do layerlayout
		ll.removeView(btOriginal);
		// se existirem submissoes a corrigir
		if (cont != 0) {
			// crio um array de correcoes auxiliar
			CorrecaoTeste ctAux[] = new CorrecaoTeste[cont];
			cont = 0;
			for (int i = 0; i < ct.size(); i++) {
				// se nao esta corrigido, acrescenta-o
				if (ct.get(i).getEstado() == 1) {
					ctAux[cont] = ct.get(i);
					cont++;
				}
			}
			// *Destaque do aluno selecionado
			CorrecaoTeste ctAux2[] = new CorrecaoTeste[cont];
			cont = 0;
			for (int i = 0; i < ctAux.length; i++) {

				if (ctAux[i].getIdEstudante() == iDs[3]) {
					ctAux2[cont] = ctAux[i];
					cont++;
				}
			}
			for (int i = 0; i < ctAux.length; i++) {

				if (ctAux[i].getIdEstudante() != iDs[3]) {
					ctAux2[cont] = ctAux[i];
					cont++;
				}
			}
			ctAux = ctAux2;

			// Agora vou construir os botoes com a informacao necessaria:
			for (int i = 0; i < ctAux.length; i++) {

				// criar o botao
				Button btIn = new Button(this);
				// copiar os parametros de layout
				btIn.setLayoutParams(btOriginal.getLayoutParams());
				// nome do aluno do teste
				Estudante aluno = bd
						.getEstudanteById(ctAux[i].getIdEstudante());
				String title = aluno.getNome() + " - ";
				// titulo do teste
				Teste tst = bd.getTesteById(ctAux[i].getTestId());
				title += tst.getTitulo() + " - ";

				title += "" + getDate(ctAux[i].getDataExecucao());

				// colocar toda a string no botao
				btIn.setText(title);

				// buscar a imagem do tipo
				ImageView imgTip = new ImageView(this);
				switch (ctAux[i].getTipo()) {
				case 0:
					imgTip.setImageResource(R.drawable.textos);// texto
					break;
				case 1:
					imgTip.setImageResource(R.drawable.imags);// multimedia
					break;
				case 2:
					imgTip.setImageResource(R.drawable.palavras);// palavras
					break;
				case 3:
					imgTip.setImageResource(R.drawable.textos);// poema
					break;
				}
				// colocar a foto do aluno
				if (aluno.getNomefoto() != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Students/" + aluno.getNomefoto();
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imgAl = new ImageView(this);
					// ajustar o tamanho da imagem
					imgAl.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120,
							120, false));

					// enviar para o botï¿½o
					btIn.setCompoundDrawablesWithIntrinsicBounds(
							imgAl.getDrawable(), null, imgTip.getDrawable(),
							null);
				} else {
					// senao copia a imagem do botao original
					btIn.setCompoundDrawables(
							btOriginal.getCompoundDrawablesRelative()[0], null,
							imgTip.getDrawable(), null);
				}

				CorrecaoTesteLeitura ctl = bd
						.getCorrecaoTesteLeirutaById(ctAux[i].getIdCorrrecao());

				final String resultado, titulo=title;

				resultado = "==========Avaliação============\n"
						+ ctl.getObservacoes() + "\n"
						+ "Palavras lidas por minuto (plm): "
						+ ctl.getNumPalavrasMin() + "\n"
						+ "Palavras corretamente lidas (pcl): "
						+ ctl.getNumPalavCorretas() + "\n"
						+ "Precisão de Leitura (PL): " + ctl.getPrecisao()
						+ "\n" + "Velocidade de leitura (VL): "
						+ ctl.getVelocidade() + "\n" + "Expressividade: "
						+ ctl.getExpressividade() + "\n" + "Ritmo: "
						+ ctl.getRitmo() + "\n\n"
						+ "===============================\n" + "Detalhes\n"
						+ "===============================\n"
						+ ctl.getDetalhes();

				if (ctAux[i].getTipo()!= 1) {
					// Defenir o que faz o botao ao clicar
					btIn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Entrar na activity

							// teste do resultado!
							Bundle wrap = new Bundle();
							wrap.putString("teste", titulo);// titulo do teste + data
							wrap.putString("Avaliac", resultado); // descritivo
																	// do
																	// resultado
							// listar submissoes anteriores do mesmo teste
							Intent it = new Intent(getApplicationContext(),
									Resultado.class);
							it.putExtras(wrap);
							startActivity(it);
						}
					});
				} else {
					CorrecaoTesteMultimedia ctm = bd
							.getCorrecaoTesteMultimediaById(ctAux[i]
									.getIdCorrrecao());
					ctm.getCerta();
					ctm.getOpcaoEscolhida();

					if (ctm.getCerta() == ctm.getOpcaoEscolhida()) {
						btIn.setText(btIn.getText() + " - Acertou");
					} else {
						btIn.setText(btIn.getText() + " - Errou");
					}

				}

				ll.addView(btIn);
			}

			// Senao lanca um alerta... de sem submissoes de momento
		} else {

			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas");
			// define a mensagem
			builder.setMessage("Nao foram encontradas correcoes no repositorio");
			// define um botï¿½o como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();

		}

	}

	/**
	 * Funcao importante que transforma um TimeStamp em uma data com hora
	 * 
	 * @param timeStamp
	 *            timestamp a converter
	 * @return retorna uma string
	 */
	@SuppressLint("SimpleDateFormat")
	private String getDate(long timeStamp) {
		try {
			long timeStampCorrigido = timeStamp * 1000;
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date netDate = (new Date(timeStampCorrigido));
			return sdf.format(netDate);
		} catch (Exception ex) {
			return "0";
		}
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
