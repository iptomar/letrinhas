package com.letrinhas05;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.ClassesObjs.TesteLeitura;
import com.letrinhas05.util.Avaliacao;

/**
 * Classe para apoiar a activity de corrigir um teste de texto de poema
 * 
 * @author Thiago
 * 
 */
public class Correcao_Poema extends Activity {

	boolean playing;
	LetrinhasDB bd = new LetrinhasDB(this);
	CorrecaoTesteLeitura crt;
	int iDs[], minuto, segundo;
	String Nomes[], demoUrl, audioUrl, titulo;
	int RetirarSeleccao = 0;
	boolean EscreverNaLista = true;
	ArrayList<Integer> ListaPalavrasErradas = new ArrayList<Integer>();

	// objetos
	Button play, pDemo, voltar, cancelar, avancar;
	ImageButton p1, p2, v1, v2, f1, f2, s1, s2, r1, r2;
	TextView pnt, vcl, frg, slb, rpt, pErr, texto;
	Chronometer chrono;
	ProgressBar pbDuracao;

	// Objeto controlador para a avaliação
	Avaliacao avaliador;
	String avaliacao;

	private MediaPlayer reprodutor = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.correcao_poema);
		ListaPalavrasErradas.add(-1);

		// new line faz a rotação do ecrãn 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

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

		titulo = teste.getTitulo() + " - ";

		titulo += "" + getDate(crt.getDataExecucao());

		this.setTitle(titulo);
		// tiulo do teste
		// ((TextView) findViewById(R.id.textCabecalho)).setText(s);
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
		// cancelar = (Button) findViewById(R.id.txtCancel);
		avancar = (Button) findViewById(R.id.txtAvaliar);

		setCorreccao();
		escutaBotoes();
	}

	/**
	 * Funcao importante que transforma um TimeStamp em uma data com hora
	 * 
	 * @param timeStamp
	 *            timestamp a converter
	 * @return retorna uma string
	 * @author Alexandre
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

	/**
	 * método para acrescentar um 0 nas casas das dezenas, caso o numero seja
	 * inferior a 10
	 * 
	 * @param n
	 *            Numero inteiro a ser verificado
	 * @return String adaptada.
	 * @author Thiago
	 */
	private String n2d(int n) {
		String num;
		if (n / 10 == 0) {
			num = "0" + n;
		} else {
			num = "" + n;
		}
		return num;
	}

	/**
	 * Método para defenir o Touchlistener de cada botão
	 * 
	 * @author Thiago
	 */
	private void escutaBotoes() {
		// Bloqueio do multi-touch na textView (só para Asus)
		texto.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {

				int dedos = e.getPointerCount();
				if (dedos > 1) {
					return true;
				} else {
					return false;
				}
			}
		});

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

		/*
		 * cancelar.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View view) { cancelAvaliacao();
		 * 
		 * }
		 * 
		 * });
		 */

		avancar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startAvalia();
			}

		});

		voltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// voltar para pagina anterior
				stopPlay();
				finish();
			}
		});
	}

	/**
	 * forçar a paragem da reprodução do audio!
	 * @author Thiago
	 */
	private void stopPlay() {
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
		if (playing) {
			reprodutor.stop();
			reprodutor.release();
		}
		super.onDestroy();
	}

	private final int PARADO = 2, ANDANDO = 1;
	private int tSegundo = segundo, tMinuto = minuto;
	private Handler play_handler, play_handler2;

	/**
	 * Método para reproduzir a demosntracao do professor
	 * @author Thiago & Dário
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
						"Erro na reprodução da demo.\n" + ex.getMessage(),
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
						"Erro na reprodução da demo.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * Método para reproduzir ou parar o som do aluno
	 * @author Thiago & Dário
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

	/** Método para reposicionar a progress bar no inicio e voltar 
	 * a colocar o timer no inicio.
	 * @author Thiago
	 */
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

	/** Método para iniciar o processo de avaliar a submissão
	 * @author Thiago
	 */
	@SuppressLint("ShowToast")
	private void startAvalia() {
		android.app.AlertDialog alerta;
		// Cria o gerador do AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle("Letrinhas");
		// define a mensagem
		builder.setMessage("Tem a certeza que quer submeter a avaliação?");

		// define os botoes
		builder.setNegativeButton("Não", null);

		builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				stopPlay();
				// inicia a avaliaCAo
				avaliador.calcula(minuto, segundo);
				try {
					bd.updateCorrecaoTesteLeitura(crt.getIdCorrrecao(),
							avaliador.obs, avaliador.PLM(minuto, segundo),
							avaliador.palavrasCertas(),
							avaliador.getPlvErradas(), avaliador.PL(),
							avaliador.VL(minuto, segundo),
							avaliador.Expressividade(), avaliador.Ritmo(),
							avaliador.detalhes);
				} catch (Exception ex) {
				}

				lancaResultados();

			}
		});
		// cria o AlertDialog
		alerta = builder.create();
		// Mostra
		alerta.show();
	}

	/**
	 * Método para verificar se existe mais do que um resultado deste aluno,
	 * sobre este teste e caso exista apresente o mais recente para comparar com
	 * este.
	 * 
	 * @param resultado
	 * @author Thiago
	 */
	private void lancaResultados() {

		List<CorrecaoTesteLeitura> crl = bd
				.getAllCorrecaoTesteLeitura_ByIDaluno_TestID(
						crt.getIdEstudante(), crt.getTestId());

		// Se o resultado for superior a 1 então vai procurar o mais recente
		// já corrigido
		if (1 < crl.size()) {
			// recebe o primeiro para comparar

			int contador = 0, ponteiro = 0;
			long data = crl.get(0).getDataExecucao();
			// corre o resto da lista e procura o mais recente, anterior a este
			for (int i = 0; i < crl.size(); i++) {
				if (crl.get(i).getEstado() == 1) {// estado
					if (data < crl.get(i).getDataExecucao()) {// Data mais
																// recente
						if (crt.getIdCorrrecao() != crl.get(i).getIdCorrrecao()) { // Corre��o_ID
							ponteiro = i;
							data = crl.get(i).getDataExecucao();
							contador++;
						}
					}
				}
			}

			if (contador > 0) {
				long id1, id2;
				id1 = crl.get(ponteiro).getIdCorrrecao();
				id2 = crt.getIdCorrrecao();
				Bundle wrap = new Bundle();
				// ID da correcao antiga
				wrap.putLong("ID1", id1);
				// ID desta correcao
				wrap.putLong("ID2", id2);
				// Mostrar os resultados das correções dos testes
				Intent it = new Intent(getApplicationContext(),
						RelatasCorrection.class);
				it.putExtras(wrap);
				startActivity(it);
				finish();
			} else {
				// teste do resultado!
				Bundle wrap = new Bundle();
				// ID desta correcao
				wrap.putLong("ID", crt.getIdCorrrecao());
				// Mostrar o resultado da correcao do teste
				Intent it = new Intent(getApplicationContext(), Resultado.class);
				it.putExtras(wrap);
				startActivity(it);
				finish();
			}

		}// Senão apresenta apenas este.
		else {
			// teste do resultado!
			Bundle wrap = new Bundle();
			// ID desta correcao
			wrap.putLong("ID", crt.getIdCorrrecao());
			// Mostrar o resultado da correcao do teste
			Intent it = new Intent(getApplicationContext(), Resultado.class);
			it.putExtras(wrap);
			startActivity(it);
			finish();
		}

	}

	/** método para calcelar a correção
	 * @author Thiago
	 */
	/*private void cancelAvaliacao() {
		android.app.AlertDialog alerta;
		// Cria o gerador do AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle("Letrinhas");
		// define a mensagem
		builder.setMessage("Tem a certeza que quer eliminar esta submissão?");

		// define os botoes
		builder.setNegativeButton("Não", null);

		builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				stopPlay();
				elimina();
			}
		});
		// cria o AlertDialog
		alerta = builder.create();
		// Mostra
		alerta.show();
	}*/

	/** método para eliminar a correção
	 * @author Thiago
	 */
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
	 * @author Thiago
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
		// de sinais de pontuação
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

	/** Método para contabilizar o numero de sinais de pontuação
	 * 
	 * @return numer de sinais
	 * @author Thiago
	 */
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

	/** Método para contar o numero de palavras do texto
	 * 
	 * @return nº de palavras
	 * @author Thiago
	 */
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

	/**
	 * Marcar a palvra errada no texto 
	 * contabiliza a palavra, e desmarcar se repetir a selecao da
	 * palavra.
	 * 
	 * @author Jorge
	 */
	public void marcaPalavra() { // Marcar Palavra Errada

		// Associar a variavel TextoLido a Textview que contém o texto
		final TextView TextoLido = texto;
		TextoLido.performLongClick();

		// Variáveis que contem o inicio e o fim da palavra que foi
		// selecionada
		final int startSelection = TextoLido.getSelectionStart();
		final int endSelection = TextoLido.getSelectionEnd();

		if (startSelection != endSelection) {

			// Definição do Span para pintar a palavra seleccionada
			Spannable WordtoSpan = (Spannable) TextoLido.getText();
			ForegroundColorSpan cor = new ForegroundColorSpan(Color.BLACK);

			// ciclo "for" que percorre a lista de palavras onde contem as
			// cordenadas da palavra que foi seleccionada
			// no inicio foi colocado o valor -1 para o array ter alguma coisa

			for (int i = 0; i < ListaPalavrasErradas.size(); i++) {
				// "if" que verifica se a cordenada inicial da palavra
				// seleccionada
				// está inserida no array, se sim guarda o valor da
				// posição
				if (ListaPalavrasErradas.get(i) == startSelection) {
					EscreverNaLista = false;
					RetirarSeleccao = i;
				}
			}
			// Se acima for falso, coloca o Span a preto e adiciona ao ArrayList
			if (EscreverNaLista == false) {
				cor = new ForegroundColorSpan(Color.BLACK);
				EscreverNaLista = true;
				ListaPalavrasErradas.remove(RetirarSeleccao);
				ListaPalavrasErradas.remove(RetirarSeleccao);
				avaliador.decPalErrada();
				pErr.setText("" + avaliador.getPlvErradas());
				// se for verdadeiro, coloca o span a vermelho e adiciona as
				// cordenadas ao array
			} else {
				cor = new ForegroundColorSpan(Color.RED);
				ListaPalavrasErradas.add(startSelection);
				ListaPalavrasErradas.add(endSelection);
				avaliador.incPalErrada();
				pErr.setText("" + avaliador.getPlvErradas());
			}
			// Pinta a palavra da respectiva cor
			WordtoSpan.setSpan(cor, startSelection, endSelection,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			TextoLido.setText(WordtoSpan);
		}
	}

}
