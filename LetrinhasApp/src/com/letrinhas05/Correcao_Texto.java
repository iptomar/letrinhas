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
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Correcao_Texto extends Activity {

	boolean playing;
	LetrinhasDB bd = new LetrinhasDB(this);
	CorrecaoTesteLeitura crt;
	int iDs[], minuto, segundo;
	String Nomes[], demoUrl, audioUrl;

	// objetos
	Button play, pDemo, voltar, cancelar, avancar;
	ImageButton p1, p2, v1, v2, f1, f2, s1, s2, r1, r2;
	TextView pnt, vcl, frg, slb, rpt, pErr, texto;
	Chronometer chrono;
	ProgressBar pbDuracao;

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

		// new line faz a rota��o do ecr�n 180 graus
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

		// correcao para buscar o id do teste, titulo e o endere�o do audio do
		// aluno
		crt = bd.getCorrecaoTesteLeirutaById(idCorrecao);

		// Teste para buscar o texto, titulo e o endere�o da demonstra��o
		TesteLeitura teste = bd.getTesteLeituraById(crt.getTestId());

		String s = teste.getTitulo() + " - ";
		// timeStamp ***** Nao sei bem se esta funciona
		// ****************************+
		s += ""
				+ DateUtils.formatSameDayTime(crt.getDataExecucao(),
						System.currentTimeMillis(), 3, 1);// 3=short; 1=long
		// ********************************************************************

		// tiulo do teste
		((TextView) findViewById(R.id.textCabecalho)).setText(s);
		// coteudo do teste
		texto = ((TextView) findViewById(R.id.txtTexto));
		texto.setText(teste.getConteudoTexto());

		// endereco da demonstracao
		demoUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/School-Data/ReadingTests/" + teste.getProfessorAudioUrl();
		// endereco da gravacao do aluno
		audioUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ crt.getAudiourl();

		// progressbar de posicao temporal do audio
		pbDuracao = (ProgressBar) findViewById(R.id.pbText);
		try {
			reprodutor.setDataSource(audioUrl);
			reprodutor.prepare();
			pbDuracao.setMax(reprodutor.getDuration());
			segundo = ((reprodutor.getDuration() / 1000) % 60);
			minuto = ((reprodutor.getDuration() / 1000) / 60);
		} catch (Exception ex) {
		}
		chrono = (Chronometer) findViewById(R.id.cromTxt);
		chrono.setText(n2d(minuto) + ":" + n2d(segundo));

		// Estudate, para ir buscar o seu nome
		Estudante aluno = bd.getEstudanteById(crt.getIdEstudante());

		((TextView) findViewById(R.id.textRodape)).setText(aluno.getNome());

		pDemo = (Button) findViewById(R.id.txtDemo);
		play = (Button) findViewById(R.id.txtPlay);
		voltar = (Button) findViewById(R.id.txtVoltar);
		cancelar = (Button) findViewById(R.id.txtCancel);
		avancar = (Button) findViewById(R.id.txtAvaliar);

		setCorreccao();
		escutaBotoes();
	}

	// m�todo para acrescentar um 0 nas casas das dezenas,
	// caso o n�mer seja inferior a 10
	private String n2d(int n) {
		String num;
		if (n / 10 == 0) {
			num = "0" + n;
		} else {
			num = "" + n;
		}
		return num;
	}

	@Override
	protected void onDestroy() {
		try {
			reprodutor.stop();
			reprodutor.release();
		} catch (Exception ex) {
		}
		super.onDestroy();
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
		pDemo.setOnClickListener(new View.OnClickListener() {
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
				cancelAvaliacao();

			}

		});

		avancar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startAvalia();
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

	private final int PARADO = 2, ANDANDO = 1;
	private int tSegundo= segundo, tMinuto=minuto;
	private Handler play_handler, play_handler2;

	/**
	 * M�todo para reproduzir a demosntracao do professor
	 */
	@SuppressLint("HandlerLeak")
	private void startDemo() {
		if (!playing) {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.play_on);
			pDemo.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			pDemo.setText("Parar");
			play.setVisibility(View.INVISIBLE);
			playing = true;
			try {
				reprodutor = new MediaPlayer();
				reprodutor.setDataSource(demoUrl);
				reprodutor.prepare();
				reprodutor.start();

				final ImageView img2 = new ImageView(this);
				img2.setImageResource(R.drawable.palyoff);
				// espetar aqui uma thread, para caso isto pare
				// handler para controlara a GUI do androi e a thread seguinte
				play_handler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case PARADO:
							pDemo.setCompoundDrawablesWithIntrinsicBounds(null,
									img2.getDrawable(), null, null);
							play.setVisibility(View.VISIBLE);
							pDemo.setText("Demo");
							playing = false;
							try {
								reprodutor.stop();
								reprodutor.release();
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
						"Erro na reprodu��o da demo.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();

				img.setImageResource(R.drawable.palyoff);
				pDemo.setCompoundDrawablesWithIntrinsicBounds(null,
						img.getDrawable(), null, null);
				play.setVisibility(View.VISIBLE);
				pDemo.setText("Demo");
				playing = false;
			}

		} else {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.palyoff);
			pDemo.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			play.setVisibility(View.VISIBLE);
			pDemo.setText("Demo");
			playing = false;
			try {
				reprodutor.stop();
				reprodutor.release();

			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro na reprodu��o da demo.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * serve para a aplicacao reproduzir ou parar o som
	 * 
	 * @author Dario Jorge
	 */
	@SuppressLint("HandlerLeak")
	private void startPlay() {
		if (!playing) {
			resetTimer();
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.pause);
			play.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			play.setText("Parar");
			pDemo.setVisibility(View.INVISIBLE);
			playing = true;
			try {
				reprodutor = new MediaPlayer();
				reprodutor.setDataSource(audioUrl);
				reprodutor.prepare();
				reprodutor.start();
				final ImageView img2 = new ImageView(this);
				img2.setImageResource(R.drawable.play);
				// espetar aqui uma thread, para caso isto pare
				// handler para controlara a GUI do android e a thread seguinte
				play_handler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case PARADO:
							play.setCompoundDrawablesWithIntrinsicBounds(null,
									img2.getDrawable(), null, null);
							play.setText("Ouvir");
							pDemo.setVisibility(View.VISIBLE);

							playing = false;
							try {
								reprodutor.stop();
								reprodutor.release();
							} catch (Exception ex) {
							}

							resetTimer();
							break;
						default:
							break;
						}
					}
				};

				new Thread(new Runnable() {
					public void run() {
						Message msg = new Message();
						while (reprodutor.isPlaying())
							;
						msg.what = PARADO;
						play_handler.sendMessage(msg);
					}
				}).start();

				play_handler2 = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case ANDANDO:
							
							pbDuracao.setProgress(reprodutor
									.getCurrentPosition());
							chrono.setText(n2d(tMinuto) + ":" + n2d(tSegundo));
							break;
						default:
							break;
						}
					}
				};

				new Thread(new Runnable() {
					public void run() {
						Message msg = new Message();
						while (playing) {
							msg = new Message();
							msg.what = ANDANDO;
							play_handler2.sendMessage(msg);

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							tSegundo--;
							if (tSegundo == (-1)) {
								tMinuto--;
								tSegundo = 59;
							}
						}

					}
				}).start();

			} catch (Exception ex) {
				img.setImageResource(R.drawable.play);
				play.setCompoundDrawablesWithIntrinsicBounds(null,
						img.getDrawable(), null, null);
				play.setText("Ouvir");
				pDemo.setVisibility(View.VISIBLE);

				resetTimer();
				playing = false;
			}

		} else {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.play);
			play.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			play.setText("Ouvir");
			pDemo.setVisibility(View.VISIBLE);
			playing = false;
			try {
				reprodutor.stop();
				reprodutor.release();
			} catch (Exception ex) {
			}
			resetTimer();

		}

	}

	protected void resetTimer() {
		try {
			reprodutor.setDataSource(audioUrl);
			reprodutor.prepare();
			pbDuracao.setMax(reprodutor.getDuration());
			pbDuracao.setProgress(0);
			
		} catch (Exception ex) {
		}
		tSegundo = segundo;
		tMinuto = minuto;
		chrono.setText(n2d(tMinuto) + ":" + n2d(tSegundo));
	}

	
	  private void startAvalia() { 
		  // inicia a avaliaCAo 
		  avaliador.calcula(minuto, segundo);
		  long time = System.currentTimeMillis() / 1000;
		  bd.updateCorrecaoTesteLeitura(crt.getIdCorrrecao(), time, avaliador.obs,
				  avaliador.PLM(minuto, segundo), avaliador.palavrasCertas(),
				  avaliador.getPlvErradas(), avaliador.PL(), avaliador.VL(minuto, segundo),
				  avaliador.Expressividade(),avaliador.Ritmo()
				  , avaliador.detalhes);
		  finish();
		 }
	
	private void cancelAvaliacao() {
		android.app.AlertDialog alerta;
		// Cria o gerador do AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle("Letrinhas");
		// define a mensagem
		builder.setMessage("Tem a certeza que quer eliminar esta submissao?");

		// define os botoes
		builder.setNegativeButton("N�o", null);

		builder.setPositiveButton("Sim",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						elimina();						
					}
				});
		// cria o AlertDialog
		alerta = builder.create();
		// Mostra
		alerta.show();
	}
	
	public void elimina() {
		File file = new File(audioUrl);
		if (file.exists()) {
			bd.deleteCorrecaoLeituraByid(crt.getIdCorrrecao());
			file.delete();
		}
		finish();
	}

	/**
	 * Procedimento para ativar a seleccao das palavras erradas no texto e o
	 * painel de controlo de erros.
	 */
	private void setCorreccao() {
		// Painel de controlo:
		ImageButton p1, p2, v1, v2, f1, f2, s1, s2, r1, r2;

		p1 = (ImageButton) findViewById(R.id.txtPontErrMn);
		p2 = (ImageButton) findViewById(R.id.txtPontErrMS);
		v1 = (ImageButton) findViewById(R.id.txtVacilMen);
		v2 = (ImageButton) findViewById(R.id.txtVacilMais);
		f1 = (ImageButton) findViewById(R.id.txtFragMen);
		f2 = (ImageButton) findViewById(R.id.txtFragMais);
		s1 = (ImageButton) findViewById(R.id.txtSilbMen);
		s2 = (ImageButton) findViewById(R.id.txtSilbMais);
		r1 = (ImageButton) findViewById(R.id.txtRepMen);
		r2 = (ImageButton) findViewById(R.id.txtRepMais);

		pnt = (TextView) findViewById(R.id.txtPontuacao);
		vcl = (TextView) findViewById(R.id.txtVacilacao);
		frg = (TextView) findViewById(R.id.txtFagmentacao);
		slb = (TextView) findViewById(R.id.txtSilabacao);
		rpt = (TextView) findViewById(R.id.txtRepeticao);
		pErr = (TextView) findViewById(R.id.txtPErrada);

		// objeto para avaliacao

		// necessitamos de contar no texto, o n. de palavras
		// de sinais de pontua��o
		avaliador = new Avaliacao(contaPalavras(), contaSinais());
		pnt.setText("" + avaliador.getPontua());
		vcl.setText("" + avaliador.getVacil());
		frg.setText("" + avaliador.getFragment());
		slb.setText("" + avaliador.getSilabs());
		rpt.setText("" + avaliador.getRepeti());
		pErr.setText("" + avaliador.getPlvErradas());

		// ativar os controlos
		// violacao da pontuacao
		p1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.decPontua();
				pnt.setText("" + avaliador.getPontua());
			}
		});
		p2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.incPontua();
				pnt.setText("" + avaliador.getPontua());
			}
		});
		// ocorrencia de vacilacoes
		v1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.decVacil();
				vcl.setText("" + avaliador.getVacil());
			}
		});
		v2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.incVacil();
				vcl.setText("" + avaliador.getVacil());
			}
		});
		// ocorrencia de fragmentacoes
		f1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.decFragment();
				frg.setText("" + avaliador.getFragment());
			}
		});
		f2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.incFragment();
				frg.setText("" + avaliador.getFragment());
			}
		});
		// ocorrencia de silabacoes
		s1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.decSilabs();
				slb.setText("" + avaliador.getSilabs());
			}
		});
		s2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.incSilabs();
				slb.setText("" + avaliador.getSilabs());
			}
		});
		// ocorrencia de repeticoes
		r1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.decRepeti();
				rpt.setText("" + avaliador.getRepeti());
			}
		});
		r2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				avaliador.incRepeti();
				rpt.setText("" + avaliador.getRepeti());
			}
		});

		// tela do texto
		((TextView) findViewById(R.id.txtTexto))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						marcaPalavra();
					}
				});
	}

	private int contaSinais() {
		boolean flag = false;
		int sinal = 0;
		// percorre todo o texto e conta os sinais validos
		for (int i = 0; i < texto.getText().length(); i++) {
			switch (texto.getText().charAt(i)) {// procuro os sinais possiveis
			// se encontrar um, ativo uma flag
			case '!':
				flag = true;
				break;
			case '?':
				flag = true;
				break;
			case '.':
				flag = true;
				break;
			case ',':
				flag = true;
				break;
			case ';':
				flag = true;
				break;
			case ':':
				flag = true;
				break;
			default:
				if (flag) {// se nao for um sinal e a flag estiver ativa,
							// entao passei por uma
					// seccao de pontuacao e incremento o num de sinais e
					// desativo a flag
					sinal++;
					flag = false;
				}
			}
		}
		// podendo ser o ultimo caracter do texto um sinal (o que e o mais
		// provavel), verifico novamente
		// a flag e valido a pontuacao, caso esta seja verdade
		if (flag) {
			sinal++;
		}
		return sinal; // devolvo o num de pontuacoes existentes no texto
	}

	private int contaPalavras() {
		boolean flag = false;
		int palavras = 0;
		// percorre todo o texto e conta as palavras e outros caracteres
		// agregados as palavras
		for (int i = 0; i < texto.getText().length(); i++) {
			// procura um caracter que corresponda a uma letra
			if (('A' <= texto.getText().charAt(i) && texto.getText().charAt(i) <= 'Z')
					|| ('a' <= texto.getText().charAt(i) && texto.getText()
							.charAt(i) <= 'z')
					|| (128 <= texto.getText().charAt(i) && texto.getText()
							.charAt(i) <= 237))
				flag = true;
			else {
				if (flag) {
					if (texto.getText().charAt(i) != '-') {
						// aqui elimina-se a hipotese de h�fen (-)
						palavras++;
						flag = false;
					}
				}
			}
		}
		if (flag)
			palavras++;
		return palavras;
	}

	/******************************************************
	 * ***************** Marcar a palvra errada no texto *** A melhorar,
	 * dever� contabilizar correctamente a palavra, e desmarcar se repetir a
	 * selec��o da palavra.
	 * 
	 * @author Jorge
	 */
	public void marcaPalavra() {

		// Mostrar Popup se caregou no ecra
		final TextView textozico = texto;
		textozico.performLongClick();
		final int startSelection = textozico.getSelectionStart();
		final int endSelection = textozico.getSelectionEnd();

		PopupMenu menu = new PopupMenu(getApplicationContext(), textozico);
		menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
		menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Mostrar palavra seleccionada na textbox
				switch (item.getItemId()) {
				case R.id.PalavraErrada:
					avaliador.incPalErrada();
					pErr.setText("" + avaliador.getPlvErradas());
					Spannable WordtoSpan = (Spannable) textozico.getText();
					ForegroundColorSpan cor = new ForegroundColorSpan(Color.RED);
					WordtoSpan.setSpan(cor, startSelection, endSelection,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textozico.setText(WordtoSpan);
					break;
				case R.id.CancelarSeleccao:
					if (avaliador.getPlvErradas() > 0) {
						Spannable WordtoCancelSpan = (Spannable) textozico
								.getText();
						ForegroundColorSpan corCancelar = new ForegroundColorSpan(
								Color.BLACK);
						WordtoCancelSpan.setSpan(corCancelar, startSelection,
								endSelection,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						textozico.setText(WordtoCancelSpan);
						avaliador.decPalErrada();
						pErr.setText("" + avaliador.getPlvErradas());
					} else {
						Toast toast = Toast.makeText(getApplicationContext(),
								"No existem palavras erradas",
								Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				return true;
			}
		});
		menu.show();

	}

}
