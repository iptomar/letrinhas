package com.letrinhas05;

import java.io.File;

import android.app.ActionBar;
import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.Professor;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.ClassesObjs.TesteLeitura;
import com.letrinhas05.util.Autenticacao;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Classe de apoio à activity de execução de um teste de texto de poema
 * 
 * @author Thiago
 * 
 */
public class Teste_Poema extends Activity {

	// flags para verificar os diversos estados do teste
	boolean gravado, recording, playing;
	TesteLeitura teste;
	// objetos
	Button record, demo, play, voltar, avancar;// , cancelar;
	private MediaRecorder gravador;
	private MediaPlayer reprodutor = new MediaPlayer();
	private String endereco, audio, fileName;
	Context context;
	long timeStamp;
	int tipo, idTesteAtual, source = 0;
	String[] Nomes;
	int[] iDs, testesID;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teste_poema);

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
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        /////////////////////////////////////


		context = this;
		// new line faz a rotacao do ecran 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// / esconder o title************************************************+
		final View contentView = findViewById(R.id.testPoema);

		// buscar os parametros
		Bundle b = getIntent().getExtras();
		inicia(b);

		// atribuir os botões
		record = (Button) findViewById(R.id.txtRecord);
		demo = (Button) findViewById(R.id.txtDemo);
		play = (Button) findViewById(R.id.txtPlay);
		play.setVisibility(View.INVISIBLE);
		voltar = (Button) findViewById(R.id.txtVoltar);
		// cancelar = (Button) findViewById(R.id.txtCancel);
		// cancelar.setVisibility(View.INVISIBLE);
		((Button) findViewById(R.id.txtCancel)).setVisibility(View.INVISIBLE);
		avancar = (Button) findViewById(R.id.txtAvaliar);

		escutaBotoes();
	}

	/**
	 * metodo para iniciar os componetes, que dependem do conteudo passado por
	 * parametros (extras)
	 * 
	 * @param b
	 *            Bundle, contem informação da activity anterior
	 * 
	 * @author Thiago
	 */
	public void inicia(Bundle b) {
		// Compor novamente e lista de testes
		testesID = b.getIntArray("ListaID");
		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		Nomes = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");

		/** Consultar a BD para preencher o conte�do.... */
		LetrinhasDB bd = new LetrinhasDB(this);
		teste = bd.getTesteLeituraById(testesID[0]);

//		this.setTitle(teste.getTitulo());
        ((TextView) findViewById(R.id.txtTituloTextLei2)).setText(teste.getTitulo());

		((TextView) findViewById(R.id.txtTexto)).setText(teste
				.getConteudoTexto());

		idTesteAtual = testesID[0];
		endereco = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/School-Data/CorrectionReadTest/" + idTesteAtual + "/"
				+ iDs[3] + "/";

		// fileName = getCurrentTimeStamp() + ".3gpp";
		audio = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/School-Data/ReadingTests/" + teste.getProfessorAudioUrl();

		// descontar este teste da lista.
		int[] aux = new int[testesID.length - 1];
		for (int i = 1; i < testesID.length; i++) {
			aux[i - 1] = testesID[i];
		}
		testesID = aux;

	}
	/**
	 * Preparar os parametros do gravador
	 * 
	 * @author Dário
	 */
	public void setUp() {

		timeStamp = System.currentTimeMillis() / 1000;
		fileName = timeStamp + ".3gpp";

		gravador = new MediaRecorder();
		gravador.setAudioSource(MediaRecorder.AudioSource.MIC);
		gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		gravador.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

		// construir as pastas caso necess�rio
		File file = new File(endereco + fileName);
		if (file.getParent() != null && !file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		gravador.setOutputFile(endereco + fileName);

	}

	/**
	 * Garantir que o existe controlo mesmo quando se clica em Back na tecla de sistema
	 * @author Thiago
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		voltar.performClick();
	}

	/**
	 * método que fica à espera de um resultado da ativity de autenticação
	 * @author Thiago
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (data.getExtras().getBoolean("Resultado")) {
			stopPlayRec();
			elimina();
			if (source == 1) {
				finaliza();
			} else {
				finish();
			}
		}
	}

	/** Método para defenir o Touchlistener de cada botão
	 * @author Thiago
	 */
	private void escutaBotoes() {
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startGrava();
			}

		});

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

		/*
		 * cancelar.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View view) { android.app.AlertDialog
		 * alerta; // Cria o gerador do AlertDialog AlertDialog.Builder builder
		 * = new AlertDialog.Builder(context); // define o titulo
		 * builder.setTitle("Letrinhas"); // define a mensagem
		 * builder.setMessage
		 * ("Tens a certeza que queres abandonar este teste?"); // define os
		 * botoes builder.setNegativeButton("Nao", null);
		 * 
		 * builder.setPositiveButton("Sim", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * LetrinhasDB bd = new LetrinhasDB( getApplicationContext()); Professor
		 * prf = bd.getProfessorById(iDs[1]);
		 * 
		 * String PIN = prf.getPassword(); Bundle wrap = new Bundle();
		 * wrap.putString("PIN", PIN);
		 * 
		 * // iniciar a pagina (Autentica��o) Intent at = new
		 * Intent(getApplicationContext(), Autenticacao.class);
		 * at.putExtras(wrap); startActivityForResult(at, 1); source = 1; } });
		 * // cria o AlertDialog alerta = builder.create(); // Mostra
		 * alerta.show();
		 * 
		 * } });
		 */

		avancar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				android.app.AlertDialog alerta;
				// Cria o gerador do AlertDialog
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				// define o titulo
				builder.setTitle("Letrinhas");
				// define a mensagem
				builder.setMessage("Confirmas a submissao deste teste?");
				// define os botoes
				builder.setNegativeButton("Nao", null);

				builder.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								stopPlayRec();
								submit();
							}
						});
				// cria o AlertDialog
				alerta = builder.create();
				// Mostra
				alerta.show();
			}

		});

		voltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				android.app.AlertDialog alerta;
				// Cria o gerador do AlertDialog
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				// define o titulo
				builder.setTitle("Letrinhas");
				// define a mensagem
				builder.setMessage("Tens a certeza que queres voltar para a listagem dos testes\n"
						+ "E abandonar este?");
				// define os botoes
				builder.setNegativeButton("Não", null);

				builder.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LetrinhasDB bd = new LetrinhasDB(
										getApplicationContext());
								Professor prf = bd.getProfessorById(iDs[1]);

								String PIN = prf.getPassword();
								Bundle wrap = new Bundle();
								wrap.putString("PIN", PIN);

								// iniciar a pagina (Autentica��o)
								Intent at = new Intent(getApplicationContext(),
										Autenticacao.class);
								at.putExtras(wrap);
								startActivityForResult(at, 1);
								source = 0;
							}

						});
				// cria o AlertDialog
				alerta = builder.create();
				// Mostra
				alerta.show();

			}
		});
	}

	/**
	 * forçar a paragem da gravação / reprodução do audio!
	 * @author Thiago
	 */
	private void stopPlayRec() {
		if (recording) {
			gravador.stop();
			gravador.release();
		}
		if (playing) {
			reprodutor.stop();
			reprodutor.release();
		}
	}

	/** temos de manter o onDestroy, devido a existir a possibilidade de fazer
	 *  finhish() através da barra de sistema!
	 *  @author Thiago
	 */
	@Override
	protected void onDestroy() {
		if (recording) {
			gravador.stop();
			gravador.release();
		}
		if (playing) {
			reprodutor.stop();
			reprodutor.release();
		}
		super.onDestroy();
	}

	/**
	 * Eliminar o audio gravado
	 * @author Thiago
	 */
	public void elimina() {
		File file = new File(endereco + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	int minuto, segundo;

	/**
	 * Serve para começar ou parar o recording do audio
	 * 
	 * @author Dário Jorge, adaptado por Thiago
	 */
	@SuppressLint("HandlerLeak")
	private void startGrava() {
		if (!gravado) {
			if (!recording) {
				ImageView img = new ImageView(this);
				img.setImageResource(R.drawable.stop);
				record.setCompoundDrawablesWithIntrinsicBounds(null,
						img.getDrawable(), null, null);
				record.setText("Parar");
				play.setVisibility(View.INVISIBLE);
				demo.setVisibility(View.INVISIBLE);
				// cancelar.setVisibility(View.INVISIBLE);
				voltar.setVisibility(View.INVISIBLE);
				avancar.setVisibility(View.INVISIBLE);
				recording = true;

				try {
					setUp();

					gravador.prepare();
					gravador.start();
					Toast.makeText(getApplicationContext(), "A gravar.",
							Toast.LENGTH_SHORT).show();

					// pequena thread, para contar o tempo
					new Thread(new Runnable() {
						public void run() {
							minuto = 0;
							segundo = 0;
							while (recording) {

								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								segundo++;
								if (segundo == 60) {
									minuto++;
									segundo = 0;
								}
							}
						}
					}).start();

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Erro na gravação.\n" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					record.performClick();
					gravado = false;
				}

			} else {
				ImageView img = new ImageView(this);
				img.setImageResource(R.drawable.record);
				record.setCompoundDrawablesWithIntrinsicBounds(null,
						img.getDrawable(), null, null);
				record.setText("Repetir");
				play.setVisibility(View.VISIBLE);
				demo.setVisibility(View.VISIBLE);
				// cancelar.setVisibility(View.VISIBLE);
				voltar.setVisibility(View.VISIBLE);
				avancar.setVisibility(View.VISIBLE);
				recording = false;
				try {
					gravador.stop();
					gravador.release();
					Toast.makeText(getApplicationContext(),
							"Gravação efetuada com sucesso!",
							Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(),
							"Tempo de leitura: " + minuto + ":" + segundo,
							Toast.LENGTH_LONG).show();

					gravado = true;
					record.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Erro na gravação.\n" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					gravado = false;
					record.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private final int PARADO = 2;
	private Handler play_handler;

	/**
	 * serve para a aplicacao reproduzir ou parar o audio do aluno
	 * 
	 * @author Dario Jorge, adaptado por Thiago
	 */
	@SuppressLint("HandlerLeak")
	private void startPlay() {
		if (!playing) {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.pause);
			play.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			play.setText("Parar");
			// record.setVisibility(View.INVISIBLE);
			demo.setVisibility(View.INVISIBLE);
			playing = true;
			try {
				reprodutor = new MediaPlayer();
				reprodutor.setDataSource(endereco + fileName);
				reprodutor.prepare();
				reprodutor.start();
				Toast.makeText(getApplicationContext(), "A reproduzir.",
						Toast.LENGTH_SHORT).show();

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

							// record.setVisibility(View.VISIBLE);
							demo.setVisibility(View.VISIBLE);
							playing = false;
							try {
								reprodutor.stop();
								reprodutor.release();
								Toast.makeText(getApplicationContext(),
										"Fim da reprodução.",
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
						"Erro na reprodução.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();

				img.setImageResource(R.drawable.play);
				play.setCompoundDrawablesWithIntrinsicBounds(null,
						img.getDrawable(), null, null);
				play.setText("Ouvir");
				record.setVisibility(View.VISIBLE);
				demo.setVisibility(View.VISIBLE);
				playing = false;
			}

		} else {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.play);
			play.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			play.setText("Ouvir");
			// record.setVisibility(View.VISIBLE);
			demo.setVisibility(View.VISIBLE);
			playing = false;
			try {
				reprodutor.stop();
				reprodutor.release();
				Toast.makeText(getApplicationContext(),
						"Reprodução interrompida.", Toast.LENGTH_SHORT).show();
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro a finalizar a reprodução.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * serve para a aplicacao reproduzir ou parar o audio do professor
	 * 
	 * @author Dario Jorge, adaptado por Thiago
	 */
	@SuppressLint("HandlerLeak")
	private void startDemo() {
		if (!playing) {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.play_on);
			demo.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			demo.setText("Parar");
			record.setVisibility(View.INVISIBLE);
			play.setVisibility(View.INVISIBLE);
			playing = true;
			try {
				reprodutor = new MediaPlayer();
				reprodutor.setDataSource(audio);
				reprodutor.prepare();
				reprodutor.start();
				Toast.makeText(getApplicationContext(),
						"A reproduzir demonstração.", Toast.LENGTH_SHORT)
						.show();

				final ImageView img2 = new ImageView(this);
				img2.setImageResource(R.drawable.palyoff);
				// espetar aqui uma thread, para caso isto pare
				// handler para controlara a GUI do androi e a thread seguinte
				play_handler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case PARADO:
							demo.setCompoundDrawablesWithIntrinsicBounds(null,
									img2.getDrawable(), null, null);
							if (gravado)
								play.setVisibility(View.VISIBLE);
							else
								record.setVisibility(View.VISIBLE);
							demo.setText("Demo");
							playing = false;
							try {
								reprodutor.stop();
								reprodutor.release();
								Toast.makeText(getApplicationContext(),
										"Fim da reprodução da demo.",
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
						"Erro na reprodução da demo.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();

				img.setImageResource(R.drawable.palyoff);
				demo.setCompoundDrawablesWithIntrinsicBounds(null,
						img.getDrawable(), null, null);
				if (gravado)
					play.setVisibility(View.VISIBLE);
				else
					record.setVisibility(View.VISIBLE);
				demo.setText("Demo");
				playing = false;
			}

		} else {
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.palyoff);
			demo.setCompoundDrawablesWithIntrinsicBounds(null,
					img.getDrawable(), null, null);
			if (gravado)
				play.setVisibility(View.VISIBLE);
			else
				record.setVisibility(View.VISIBLE);
			demo.setText("Demo");
			playing = false;
			try {
				reprodutor.stop();
				reprodutor.release();
				Toast.makeText(getApplicationContext(),
						"Reprodução da demo interrompida.", Toast.LENGTH_SHORT)
						.show();
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro na reprodução da demo.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * Prepara a finalizacao da activity, descobrindo qual o proximo teste a
	 * realizar Este metodo devera ser usado em todas as paginas de teste.
	 * @author Thiago
	 */
	private void finaliza() {
		if (testesID.length != 0) {
			// enviar o parametro de modo
			Bundle wrap = new Bundle();

			wrap.putIntArray("ListaID", testesID);// id's dos testes
			wrap.putStringArray("Nomes", Nomes);
			wrap.putIntArray("IDs", iDs);

			// identifico o tipo do proximo teste
			LetrinhasDB bd = new LetrinhasDB(this);
			Teste tst = bd.getTesteById(testesID[0]);
			tipo = tst.getTipo();

			switch (tipo) {
			case 0:
				// lançar a nova activity do tipo texto,
				Intent it = new Intent(getApplicationContext(),
						Teste_Texto.class);
				it.putExtras(wrap);

				startActivity(it);

				break;
			case 1:// lançar a nova activity do tipo imagem
				Intent ip = new Intent(getApplicationContext(),
						TesteMultimediaW.class);
				ip.putExtras(wrap);

				startActivity(ip);

				break;
			case 2: // lançar a nova activity do tipo Palavras
				Intent ipm = new Intent(getApplicationContext(),
						Teste_Palavras_Aluno.class);
				ipm.putExtras(wrap);

				startActivity(ipm);

				break;
			case 3: // lançar a nova activity do tipo poema
				Intent ipp = new Intent(getApplicationContext(),
						Teste_Poema.class);
				ipp.putExtras(wrap);

				startActivity(ipp);
				break;
			default:
				Toast.makeText(getApplicationContext(), " - Tipo não defenido",
						Toast.LENGTH_SHORT).show();
				// retirar o teste errado e continuar

				// descontar este teste da lista.
				int[] aux = new int[testesID.length - 1];
				for (int i = 1; i < testesID.length; i++) {
					aux[i - 1] = testesID[i];
				}
				testesID = aux;
				finaliza();
				break;
			}

		}
		//
		finish();
	}

	/**
	 * este metodo serve para enviar todos os dados para a tabela respectiva e
	 * preparar os dados para a activity seguinte
	 * 
	 * @author Thiago
	 */
	@SuppressLint("ShowToast")
	public void submit() {
		CorrecaoTesteLeitura ctl = new CorrecaoTesteLeitura();
		File file = new File(endereco + fileName);
		if (!file.exists()) {

			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas");
			// define a mensagem
			builder.setMessage("Para Avancar e avaliar, necessitas de fazer uma gravação da tua leitura!");
			// define um bot�o como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();

		} else {
			try {
				String aux = idTesteAtual + "" + iDs[3] + "" + timeStamp + "";
				ctl.setIdCorrrecao(Long.parseLong(aux));
				String[] yo = endereco.split("School-Data");
				ctl.setAudiourl("/School-Data" + yo[1] + fileName);

				ctl.setDataExecucao(timeStamp);
				ctl.setTipo(3);// pois estou num teste texto
				ctl.setEstado(0);
				ctl.setTestId(idTesteAtual);
				ctl.setIdEstudante(iDs[3]);
				LetrinhasDB bd = new LetrinhasDB(this);
				bd.addNewItemCorrecaoTesteLeitura(ctl);
			} catch (Exception ex) {
				Toast.makeText(this, "Falha no INSERT da BD!",
						Toast.LENGTH_SHORT);
			}
			finaliza();

			Bundle wrap = new Bundle();
			wrap.putInt("IDTeste", idTesteAtual);// id do teste atual
			wrap.putInt("IDAluno", iDs[3]); // id do aluno
			wrap.putInt("TipoTeste", 3); // id do aluno
			// listar submissoes anteriores do mesmo teste
			Intent it = new Intent(getApplicationContext(),
					ResumoSubmissoes.class);
			it.putExtras(wrap);
			startActivity(it);

		}
	}

}
