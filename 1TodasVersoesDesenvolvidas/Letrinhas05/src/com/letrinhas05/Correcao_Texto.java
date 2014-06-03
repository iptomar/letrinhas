package com.letrinhas05;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.BaseDados.NetworkUtils;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.ClassesObjs.TesteLeitura;
import com.letrinhas05.util.Avaliacao;
import com.letrinhas05.util.SystemUiHider;
import com.letrinhas05.util.Utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.Spannable;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Correcao_Texto extends Activity {

	boolean playing;
	LetrinhasDB bd = new LetrinhasDB(this);
	CorrecaoTesteLeitura crt;
	int iDs[];
	String Nomes[], demoUrl, audioUrl;

	// objetos
	Button play, demo, voltar, cancelar, avancar;
	TextView pnt, vcl, frg, slb, rpt, pErr;
	Chronometer chrono;

	// Objeto controlador para a avaliacao
	Avaliacao avaliador;
	String avaliacao;

	private MediaPlayer reprodutor = new MediaPlayer();

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
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
		setContentView(R.layout.correcao_texto);

		// new line faz a rotaï¿½ï¿½o do ecrï¿½n 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// / esconder o title************************************************+
		final View contentView = findViewById(R.id.testTexto);

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

		// buscar os parametros
		Bundle b = getIntent().getExtras();

		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		Nomes = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");
		long idCorrecao = b.getLong("ID_Correcao");

		// correcao para buscar o id do teste, titulo e o endereço do audio do
		// aluno
		crt = bd.getCorrecaoTesteLeirutaById(idCorrecao);

		// Teste para buscar o texto, titulo e o endereço da demonstração
		TesteLeitura teste = bd.getTesteLeituraById(crt.getTestId());

		String s = teste.getTitulo() + " - ";
		// timeStamp ***** Nao sei bem se esta funciona
		// ****************************+
		s += ""
				+ DateUtils.formatSameDayTime(crt.getDataExecucao(),
						System.currentTimeMillis(), 1, 1);// 3=short; 1=long
		// ********************************************************************

		// tiulo do teste
		((TextView) findViewById(R.id.textCabecalho)).setText(s);
		((TextView) findViewById(R.id.txtTexto)).setText(teste
				.getConteudoTexto());

		// Estudate, para ier burcar o seu nome
		Estudante aluno = bd.getEstudanteById(crt.getIdEstudante());

		((TextView) findViewById(R.id.textRodape)).setText(aluno.getNome());

		demoUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/School-Data/ReadingTests/" + teste.getProfessorAudioUrl();

		audioUrl = crt.getAudiourl();

		chrono = (Chronometer) findViewById(R.id.cromTxt);

		play = (Button) findViewById(R.id.txtPlay);
		demo = (Button) findViewById(R.id.txtDemo);
		voltar = (Button) findViewById(R.id.txtVoltar);
		cancelar = (Button) findViewById(R.id.txtCancel);
		avancar = (Button) findViewById(R.id.txtAvaliar);

		escutaBotoes();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(2000);
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

	private void escutaBotoes() {
		demo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startDemo();
			}

		});

		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startPlay();
			}

		});

		cancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Apaga a correcão
				//
				//
				//

				finish();
			}
		});

		avancar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// avalia e atualiza a correção

				// //////////////// sendToServer();
				// startAvalia();
				// relatorio();
			}

		});

		voltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// voltar para pag inicial
				finish();
			}
		});
	}

	int minuto, segundo;

	private final int PARADO = 2;
	private Handler play_handler;

	/**
	 * serve para a aplicacao reproduzir ou parar o som
	 * 
	 * @author Dario Jorge
	 */
	@SuppressLint("HandlerLeak")
	private void startPlay() {
		if (!playing) {
			// play.setImageResource(R.drawable.play_on);
			demo.setVisibility(View.INVISIBLE);
			playing = true;
			try {
				reprodutor = new MediaPlayer();
				reprodutor.setDataSource(audioUrl);
				reprodutor.prepare();
				reprodutor.start();
				Toast.makeText(getApplicationContext(), "A reproduzir.",
						Toast.LENGTH_SHORT).show();
				// espetar aqui uma thread, para caso isto pare
				// handler para controlara a GUI do androi e a thread seguinte
				play_handler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case PARADO:
							// play.setImageResource(R.drawable.palyoff);
							demo.setVisibility(View.VISIBLE);
							playing = false;
							try {
								reprodutor.stop();
								reprodutor.release();
								Toast.makeText(getApplicationContext(),
										"Fim da reproducao.",
										Toast.LENGTH_SHORT).show();
							} catch (Exception ex) {
							}
							break;
						default:
							break;
						}
					}
				};

				new Thread(new Runnable() {
					public void run() {
						while (reprodutor.isPlaying())
							;
						Message msg = new Message();
						msg.what = PARADO;
						play_handler.sendMessage(msg);
					}
				}).start();

			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro na reproduï¿½ï¿½o.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			// play.setImageResource(R.drawable.palyoff);
			demo.setVisibility(View.VISIBLE);
			playing = false;
			try {
				reprodutor.stop();
				reprodutor.release();

				Toast.makeText(getApplicationContext(),
						"Reproducao interrompida.", Toast.LENGTH_SHORT).show();
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro na reproducao.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void startAvalia() {
		// usar a classe Avaliacao para calcular os resultados.
		// avancar para o proximo teste caso este exista.;
		avaliador = new Avaliacao(contaPalavras(), contaSinais());
		avaliador.calcula(minuto, segundo);
		crt.setEstado(1);
		crt.setExpressividade(avaliador.Expressividade());
		crt.setNumPalavCorretas(avaliador.palavrasCertas());
		crt.setNumPalavIncorretas(avaliador.getPlvErradas());
		crt.setPrecisao(avaliador.PL());
		crt.setRitmo(avaliador.Ritmo());
		crt.setVelocidade(avaliador.VL(minuto, segundo));
		
		bd.updateCorrecaoTesteLeitura(crt.getIdCorrrecao(),
					dataAlteracao,
					observacoes, 
					numPalavrasorMin,
					numPalavrasCorr, 
					numPalavrasInc, 
					precisao,
					velocidade, 
					expressividade, 
					ritmo, 
					detalhes);
		
		
		
		// finaliza();
		// } else {
		android.app.AlertDialog alerta;
		// Cria o gerador do AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle("Letrinhas 03");
		// define a mensagem
		builder.setMessage(" Ainda nï¿½o executou a gravaï¿½ï¿½o da leitura!\n"
				+ " Faï¿½a-o antes de avaliar.");
		// define um botï¿½o como positivo
		builder.setPositiveButton("OK", null);
		// cria o AlertDialog
		alerta = builder.create();
		// Mostra
		alerta.show();

	}

	/**
	 * Procedimento para ativar a selecï¿½ï¿½o das palavras erradas no texto e o
	 * painel de controlo de erros.
	 */
	/*
	 * private void setCorreccao() { // Painel de controlo: ImageButton p1, p2,
	 * v1, v2, f1, f2, s1, s2, r1, r2;
	 * 
	 * p1 = (ImageButton) findViewById(R.id.txtPontErrMn); p2 = (ImageButton)
	 * findViewById(R.id.txtPontErrMS); v1 = (ImageButton)
	 * findViewById(R.id.txtVacilMen); v2 = (ImageButton)
	 * findViewById(R.id.txtVacilMais); f1 = (ImageButton)
	 * findViewById(R.id.txtFragMen); f2 = (ImageButton)
	 * findViewById(R.id.txtFragMais); s1 = (ImageButton)
	 * findViewById(R.id.txtSilbMen); s2 = (ImageButton)
	 * findViewById(R.id.txtSilbMais); r1 = (ImageButton)
	 * findViewById(R.id.txtRepMen); r2 = (ImageButton)
	 * findViewById(R.id.txtRepMais);
	 * 
	 * pnt = (TextView) findViewById(R.id.textView9); vcl = (TextView)
	 * findViewById(R.id.escTDisciplina); frg = (TextView)
	 * findViewById(R.id.TextView02); slb = (TextView)
	 * findViewById(R.id.TextView03); rpt = (TextView)
	 * findViewById(R.id.TextView06); pErr = (TextView)
	 * findViewById(R.id.TextView07);
	 * 
	 * // texto TextView txt = ((TextView) findViewById(R.id.txtTexto));
	 * txt.setText(texto); txt.setTextColor(Color.rgb(30, 30, 30)); // objeto
	 * para avaliaï¿½ï¿½o
	 * 
	 * // necessitamos de contar no texto, o nï¿½ de palavras e o nï¿½ de
	 * pontuaï¿½ï¿½o avaliador = new Avaliacao(contaPalavras(), contaSinais());
	 * pnt.setText("" + avaliador.getPontua()); vcl.setText("" +
	 * avaliador.getVacil()); frg.setText("" + avaliador.getFragment());
	 * slb.setText("" + avaliador.getSilabs()); rpt.setText("" +
	 * avaliador.getRepeti()); pErr.setText("" + avaliador.getPlvErradas());
	 * 
	 * // ativar os controlos // violaï¿½ï¿½o da pontuaï¿½ï¿½o
	 * p1.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.decPontua();
	 * pnt.setText("" + avaliador.getPontua()); } }); p2.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.incPontua();
	 * pnt.setText("" + avaliador.getPontua()); } }); // ocorrï¿½ncia de
	 * vacilaï¿½ï¿½es v1.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.decVacil();
	 * vcl.setText("" + avaliador.getVacil()); } }); v2.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.incVacil();
	 * vcl.setText("" + avaliador.getVacil()); } }); // ocorrï¿½ncia de
	 * fragmentaï¿½ï¿½es f1.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.decFragment();
	 * frg.setText("" + avaliador.getFragment()); } });
	 * f2.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.incFragment();
	 * frg.setText("" + avaliador.getFragment()); } }); // ocorrï¿½ncia de
	 * silabaï¿½ï¿½es s1.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.decSilabs();
	 * slb.setText("" + avaliador.getSilabs()); } }); s2.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.incSilabs();
	 * slb.setText("" + avaliador.getSilabs()); } }); // ocorrï¿½ncia de
	 * repetiï¿½ï¿½es r1.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.decRepeti();
	 * rpt.setText("" + avaliador.getRepeti()); } }); r2.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View view) { avaliador.incRepeti();
	 * rpt.setText("" + avaliador.getRepeti()); } });
	 * 
	 * // tela do texto ((TextView) findViewById(R.id.txtTexto))
	 * .setOnClickListener(new View.OnClickListener() {
	 */
	  
	  private int contaSinais() { 
		  boolean flag = false; int sinal = 0; 
		  CharSequence texto = ((TextView)findViewById(R.id.txtTexto)).getText();
	  // percorre todo o texto e conta os sinais vï¿½lidos 
	  for (int i = 0; i < texto.length(); i++) { 
		  switch (texto.charAt(i)) {// procuro os sinais possiveis 
		  // se encontrar um, ativo uma flag 
		  case '!': flag = true;
		  	break; 
		  case '?': flag = true; 
		  	break; 
		 case '.': flag = true; 
		 break; 
		 case ',': flag = true; 
		 break; 
		 case ';': flag = true; 
		 break;
		 case ':': flag =
	  true; 
		 break;
		 default: if (flag) {// se nï¿½o for um sinal e a flag estiver ativa, ï¿½ 
		  // porque passei por uma 
		  // secï¿½ï¿½o de pontuaï¿½ï¿½o e incremento o nï¿½ de sinais e 
		  // desativo a flag 
			 sinal++;
	  flag = false; }
		  }}
	  //podendo ser o ultimo caracter do texto um sinal (o
	 // que ï¿½ o mais // provï¿½vel), verifico novamente // a flag e valido a
	 // pontuaï¿½ï¿½o, caso esta seja verdade 
		 if (flag) { sinal++; } 
		 return sinal;}  // devolvo o nï¿½ de pontuaï¿½ï¿½es existentes no texto }
	  
	  private int contaPalavras() { 
		  boolean flag = false; int palavras = 0; 
		  CharSequence texto = ((TextView)findViewById(R.id.txtTexto)).getText();
		  // percorre todo o texto e conta as palavras e outros caracteres 
		  //agregados ï¿½s palavras 
		  for (int i = 0; i < texto.length(); i++) { 
			  // procura um caracter que corresponda a uma letra 
			  if (('A' <= texto.charAt(i) && texto.charAt(i) <= 'Z') 
					  || ('a' <= texto.charAt(i) 
					  && texto.charAt(i) <= 'z')
					  || (128 <= texto.charAt(i) && texto.charAt(i)
					  <= 237)) 
				  flag = true; 
			  else { 
				  if (flag) { 
					  if (texto.charAt(i) != '-') {
						  //aqui elimina-se a hipotese de hifen (-) 
						  palavras++; flag = false; 
						  } 
					  } 
				  } 
			  } 
		  if (flag) palavras++; 
		  return palavras; 
		  }
	  
	  /****************************************************** *****************
	 * Marcar a palvra errada no texto *** A melhorar, deverï¿½ contabilizar
	 * correctamente a palavra, e desmarcar se repetir a selecï¿½ï¿½o da
	 * palavra.
	 * 
	 * @author Jorge
	 */
	/*
	 * public void marcaPalavra() {
	 * 
	 * /* final TextView textozico = (TextView) findViewById(R.id.txtTexto);
	 * textozico.performLongClick(); final int startSelection =
	 * textozico.getSelectionStart(); final int endSelection =
	 * textozico.getSelectionEnd(); plvErradas++; Spannable WordtoSpan =
	 * (Spannable) textozico.getText(); ForegroundColorSpan cor = new
	 * ForegroundColorSpan(Color.RED); WordtoSpan.setSpan(cor, startSelection,
	 * endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 * textozico.setText(WordtoSpan); pErr.setText("" + plvErradas);
	 */

	// Mostrar Popup se caregou no ecra
	/*
	 * final TextView textozico = (TextView) findViewById(R.id.txtTexto);
	 * textozico.performLongClick(); final int startSelection =
	 * textozico.getSelectionStart(); final int endSelection =
	 * textozico.getSelectionEnd(); //final String selectedText =
	 * textozico.getText().toString().substring(startSelection, endSelection);
	 * 
	 * PopupMenu menu = new PopupMenu(getApplicationContext(), textozico);
	 * menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
	 * menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	 * 
	 * @Override public boolean onMenuItemClick(MenuItem item) { // Mostrar
	 * palavra seleccionada na textbox switch (item.getItemId()) { case
	 * R.id.PalavraErrada: avaliador.incPalErrada(); pErr.setText("" +
	 * avaliador.getPlvErradas()); Spannable WordtoSpan = (Spannable)
	 * textozico.getText(); ForegroundColorSpan cor = new
	 * ForegroundColorSpan(Color.RED); WordtoSpan.setSpan(cor, startSelection,
	 * endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 * textozico.setText(WordtoSpan); break; case R.id.CancelarSeleccao: if
	 * (avaliador.getPlvErradas() > 0) { Spannable WordtoCancelSpan =
	 * (Spannable) textozico .getText(); ForegroundColorSpan corCancelar = new
	 * ForegroundColorSpan( Color.BLACK); WordtoCancelSpan.setSpan(corCancelar,
	 * startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 * textozico.setText(WordtoCancelSpan); avaliador.decPalErrada();
	 * pErr.setText("" + avaliador.getPlvErradas()); } else { Toast toast =
	 * Toast.makeText(getApplicationContext(), "Nï¿½o existem palavras erradas",
	 * Toast.LENGTH_SHORT); toast.show(); } } return true; } }); menu.show();
	 * 
	 * }
	 * 
	 * /** Prepara a finalizaï¿½ï¿½o da activity, descobrindo qual o prï¿½ximo
	 * teste a realizar Este mï¿½todo deverï¿½ ser usado em todas as paginas de
	 * teste.
	 */
	/*
	 * private void finaliza() { if (array.length != 0) { // Decompor o array de
	 * teste, para poder enviar por parametros int[] lstID = new
	 * int[array.length]; int[] lstTipo = new int[array.length]; String[]
	 * lstTitulo = new String[array.length]; String[] lstTexto = new
	 * String[array.length]; for (int i = 0; i < array.length-1; i++) { lstID[i]
	 * = id[i]; lstTipo[i] = tipo[i]; lstTitulo[i] = titulo[i]; lstTexto[i] =
	 * texto0[i]; } // enviar o parametro de modo Bundle wrap = new Bundle();
	 * wrap.putBoolean("Modo", modo);
	 * 
	 * // teste, a depender das informaï¿½ï¿½es da BD //
	 * ***********************************************************+
	 * wrap.putString("Aluno", "EI3C-Tiago Fernandes");
	 * wrap.putString("Professor", "ESTT-Antonio Manso");
	 * wrap.putIntArray("ListaID", lstID); wrap.putIntArray("ListaTipo",
	 * lstTipo); wrap.putStringArray("ListaTitulo", lstTitulo);
	 * wrap.putStringArray("ListaTexto", lstTexto);
	 * 
	 * // identifico o tipo de teste switch (lstTipo[0]) { case 0: // lanï¿½ar a
	 * nova activity do tipo texto,
	 * 
	 * // iniciar a pagina 2 (escolher teste) Intent it = new
	 * Intent(getApplicationContext(), Teste_Texto_Aluno.class);
	 * it.putExtras(wrap);
	 * 
	 * startActivity(it);
	 * 
	 * break; case 1:// lanï¿½ar a nova activity do tipo Palavras, e o seu
	 * conteï¿½do // Intent ip = new Intent(getApplicationContext(),
	 * Teste_Palavras_Aluno.class); ip.putExtras(wrap);
	 * 
	 * startActivity(ip);
	 * 
	 * break; case 2: // lanï¿½ar a nova activity do tipo Poema, e o seu
	 * conteï¿½do // Intent ipm = new Intent(getApplicationContext(),
	 * Teste_Poema_Prof.class); ipm.putExtras(wrap);
	 * 
	 * startActivity(ipm);
	 * 
	 * break; case 3: // lanï¿½ar a nova activity do tipo imagem, e o seu
	 * conteï¿½do // // Intent it = new Intent(getApplicationContext(), //
	 * Teste_Texto.class); // it.putExtras(wrap);
	 * 
	 * // startActivity(it); break; default:
	 * Toast.makeText(getApplicationContext(), " - Tipo nï¿½o defenido",
	 * Toast.LENGTH_SHORT).show(); // retirar o teste errado e continuar
	 * 
	 * int k = 0; String aux[] = new String[array.length - 1]; for (int i = 1; i
	 * < array.length; i++) { aux[k] = array[i]; k++; } array = aux; finaliza();
	 * break; }
	 * 
	 * } //se existir resultados de uma avaliaï¿½ï¿½o, apresenta o resultado.
	 * if(avaliacao!=null){ //resultado Bundle wrap = new Bundle();
	 * wrap.putString("Avaliac",avaliacao); //aluno e titulo do teste
	 * wrap.putString
	 * ("teste",((TextView)findViewById(R.id.textRodape)).getText() +"\n"
	 * +((TextView)findViewById(R.id.textCabecalho)).getText());
	 * 
	 * // iniciar a pagina de resultado Intent av = new
	 * Intent(getApplicationContext(), Resultado.class); av.putExtras(wrap);
	 * 
	 * startActivity(av); } finish(); }
	 */

}
