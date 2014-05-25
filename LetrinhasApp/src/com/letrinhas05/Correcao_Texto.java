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
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Correcao_Texto extends Activity {

			boolean  playing;
			LetrinhasDB bd = new LetrinhasDB(this);
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
				
				//new line faz a rotaï¿½ï¿½o do ecrï¿½n 180 graus
				int currentOrientation = getResources().getConfiguration().orientation;
				if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				}else {
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
				
				//correcao para buscar o id do teste, titulo e o endereço do audio do aluno
				CorrecaoTesteLeitura crt = bd.getCorrecaoTesteLeirutaById(idCorrecao);
				
				//Teste para buscar o texto, titulo e o endereço da demonstração
				TesteLeitura teste = bd.getTesteLeituraById(crt.getTestId());
				
				String s= teste.getTitulo() + " - ";
				// timeStamp ***** Nao sei bem se esta funciona ****************************+
				s += ""	+ DateUtils.formatSameDayTime(
								crt.getDataExecucao(),
								System.currentTimeMillis(), 3, 1);// 3=short; 1=long
				//********************************************************************
				
				//tiulo do teste
				((TextView) findViewById(R.id.textCabecalho)).setText(s);
				//coteudo do teste
				texto =((TextView) findViewById(R.id.txtTexto));
				texto.setText(teste.getConteudoTexto());
				
				//endereco da demonstracao
				demoUrl=  Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/School-Data/ReadingTests/" + teste.getProfessorAudioUrl();
				//endereco da gravacao do aluno
				audioUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ crt.getAudiourl();
				
				//progressbar de posicao temporal do audio
				pbDuracao = (ProgressBar)findViewById(R.id.pbText);
				try{
					reprodutor.setDataSource(audioUrl);
					pbDuracao.setMax(reprodutor.getDuration());	
					segundo = (reprodutor.getDuration()/1000)%60;
					minuto =  (reprodutor.getDuration()/1000)/60;
				}catch(Exception ex){}
				
				
				//Estudate, para ir buscar o seu nome
				Estudante aluno = bd.getEstudanteById(crt.getIdEstudante()); 
				
				((TextView) findViewById(R.id.textRodape)).setText(aluno.getNome());
				
				
				
				chrono = (Chronometer) findViewById(R.id.cromTxt);
				chrono.setText(n2d(minuto)+":"+n2d(segundo));
				
				
				
				pDemo = (Button) findViewById(R.id.txtDemo);
				play = (Button) findViewById(R.id.txtPlay);
				voltar = (Button) findViewById(R.id.txtVoltar);
				cancelar = (Button) findViewById(R.id.txtCancel);
				avancar = (Button) findViewById(R.id.txtAvaliar);

				setCorreccao();
				escutaBotoes();
			}

			//método para acrescentar um 0 nas casas das dezenas, 
			//caso o númer seja inferior a 10
			private String n2d(int n) {
				String num;
				if(n/10==0){
					num="0"+n;					
				}
				else{
					num=""+n;
				}
				return num;
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
						//startGrava();
					}

				});

				play.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//startPlay();
					}

				});

				cancelar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
					}
				});

				avancar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//startAvalia();
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


//////////////////////////////////ainda nao feito nao testado
//		    public void sendToServer()
//		    {
//		     String url_Server = "http://code.dei.estt.ipt.pt/:80/postTestResults";
//		    byte[] som =  Utils.getFileSDTest(pastas, fileName);
//		    CorrecaoTesteLeitura corrTestLeit = new CorrecaoTesteLeitura(1, 1, "21-12-2014", "MUNDO", 2, 10, 12, 12.0f, 12.0f, 12.0f, 12.0F, som);
//		    NetworkUtils.postResultados(url_Server, corrTestLeit);
//		    }




			

			/**
			 * Serve para comeï¿½ar ou parar o recording do audio
			 * 
			 * @author Dï¿½rio Jorge
			 */
	/*		@SuppressLint("HandlerLeak")
			private void startGrava() {
				if (!recording) {
					record.setImageResource(R.drawable.stop);
					play.setVisibility(View.INVISIBLE);
					cancelar.setVisibility(View.INVISIBLE);
					voltar.setVisibility(View.INVISIBLE);
					avancar.setVisibility(View.INVISIBLE);
					recording = true;

					try {
						setUp();
						gravador.prepare();
						gravador.start();
						Toast.makeText(getApplicationContext(), "A gravar.",
								Toast.LENGTH_SHORT).show();
						// O cronometro nï¿½o funciona assim tï¿½o bem no seu modo
						// original...
						// handler para controlar a GUI do android e a thread seguinte
						play_handler = new Handler() {
							public void handleMessage(Message msg) {
								switch (msg.what) {
								case PARADO:
									String m,
									s;
									if (minuto < 10) {
										m = "0" + minuto + ":";
									} else {
										m = minuto + ":";
									}
									if (segundo < 10) {
										s = "0" + segundo;
									} else {
										s = "" + segundo;
									}
									chrono.setText(m + s);
									break;
								default:
									break;
								}
							}
						};

						// pequena thread, para interagir com o cronometro
						new Thread(new Runnable() {
							public void run() {
								minuto = 0;
								segundo = 0;
								while (recording) {
									Message msg = new Message();
									msg.what = PARADO;
									play_handler.sendMessage(msg);

									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
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
								"Erro na gravaï¿½ï¿½o.\n" + e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

				} else {
					record.setImageResource(R.drawable.record);
					play.setVisibility(View.VISIBLE);
					cancelar.setVisibility(View.VISIBLE);
					voltar.setVisibility(View.VISIBLE);
					avancar.setVisibility(View.VISIBLE);
					recording = false;
					try {
						gravador.stop();
						gravador.release();
						Toast.makeText(getApplicationContext(),
								"Gravaï¿½ï¿½o efetuada com sucesso!", Toast.LENGTH_SHORT)
								.show();
						Toast.makeText(getApplicationContext(),
								"Tempo de leitura: " + minuto + ":" + segundo,
								Toast.LENGTH_LONG).show();

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Erro na gravaï¿½ï¿½o.\n" + e.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}

			}

			private final int PARADO = 2;
			private Handler play_handler;

			/**
			 * serve para a aplicaï¿½ï¿½o reproduzir ou parar o som
			 * 
			 * @author Dï¿½rio Jorge
			 */
		/*	@SuppressLint("HandlerLeak")
			private void startPlay() {
				if (!playing) {
					play.setImageResource(R.drawable.play_on);
					record.setVisibility(View.INVISIBLE);
					playing = true;
					try {
						reprodutor = new MediaPlayer();
						reprodutor.setDataSource(endereco);
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
									play.setImageResource(R.drawable.palyoff);
									record.setVisibility(View.VISIBLE);
									playing = false;
									try {
										reprodutor.stop();
										reprodutor.release();
										Toast.makeText(getApplicationContext(),
												"Fim da reproduï¿½ï¿½o.",
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
					play.setImageResource(R.drawable.palyoff);
					record.setVisibility(View.VISIBLE);
					playing = false;
					try {
						reprodutor.stop();
						reprodutor.release();

						Toast.makeText(getApplicationContext(),
								"Reproduï¿½ï¿½o interrompida.", Toast.LENGTH_SHORT).show();
					} catch (Exception ex) {
						Toast.makeText(getApplicationContext(),
								"Erro na reproduï¿½ï¿½o.\n" + ex.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				}

			}

			private void startAvalia() {
				if (modo) { // se estï¿½ em modo de professor
							// inicia a avaliaï¿½ï¿½o
					File file = new File(endereco);
					if (file.exists()) { // Verifica se jï¿½ fez uma gravaï¿½ï¿½o
						//prepara um ficheiro para guardar o relatï¿½rio da avaliaï¿½ï¿½o
						file= new File(endereco.substring(0, endereco.length()-5)+"_aval.txt");
						if(file.exists())file.delete();
						// usar a classe Avaliaï¿½ï¿½o para calcular os resultados.
						// avanï¿½ar para o prï¿½ximo teste caso este exista.;
						avaliacao = avaliador.calcula(minuto, segundo);
						try {
							FileOutputStream out = new FileOutputStream(file);
							out.write(avaliacao.getBytes());
							out.close();
						} catch (FileNotFoundException e) {
						} catch (IOException e) {
						}
						finaliza();
					} else {
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
				}
	
			}*/

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
				// de sinais de pontuaï¿½ï¿½o
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
							|| ('a' <= texto.getText().charAt(i) && texto.getText().charAt(i) <= 'z')
							|| (128 <= texto.getText().charAt(i) && texto.getText().charAt(i) <= 237))
						flag = true;
					else {
						if (flag) {
							if (texto.getText().charAt(i) != '-') {
								//aqui elimina-se a hipotese de hï¿½fen (-)
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
			 * ***************** Marcar a palvra errada no texto *** A melhorar, deverï¿½
			 * contabilizar correctamente a palavra, e desmarcar se repetir a selecï¿½ï¿½o
			 * da palavra.
			 * 
			 * @author Jorge
			 */
			public void marcaPalavra() {
	
				/*
				 * final TextView textozico = (TextView) findViewById(R.id.txtTexto);
				 * textozico.performLongClick(); final int startSelection =
				 * textozico.getSelectionStart(); final int endSelection =
				 * textozico.getSelectionEnd(); plvErradas++; Spannable WordtoSpan =
				 * (Spannable) textozico.getText(); ForegroundColorSpan cor = new
				 * ForegroundColorSpan(Color.RED); WordtoSpan.setSpan(cor,
				 * startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				 * textozico.setText(WordtoSpan); pErr.setText("" + plvErradas);
				 */
	
				// Mostrar Popup se caregou no ecra
				final TextView textozico = texto;
				textozico.performLongClick();
				final int startSelection = textozico.getSelectionStart();
				final int endSelection = textozico.getSelectionEnd();
				//final String selectedText = textozico.getText().toString().substring(startSelection, endSelection);
	
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

			/**
			 * Prepara a finalizaï¿½ï¿½o da activity, descobrindo qual o prï¿½ximo teste a
			 * realizar Este mï¿½todo deverï¿½ ser usado em todas as paginas de teste.
			 */
		/*	private void finaliza() {
				if (array.length != 0) {
					// Decompor o array de teste, para poder enviar por parametros
					int[] lstID = new int[array.length];
					int[] lstTipo = new int[array.length];
					String[] lstTitulo = new String[array.length];
					String[] lstTexto = new String[array.length];
					for (int i = 0; i < array.length-1; i++) {
						lstID[i] = id[i];
						lstTipo[i] = tipo[i];
						lstTitulo[i] = titulo[i];
						lstTexto[i] = texto0[i];
					}
					// enviar o parametro de modo
					Bundle wrap = new Bundle();
					wrap.putBoolean("Modo", modo);

					// teste, a depender das informaï¿½ï¿½es da BD
					// ***********************************************************+
					wrap.putString("Aluno", "EI3C-Tiago Fernandes");
					wrap.putString("Professor", "ESTT-Antonio Manso");
					wrap.putIntArray("ListaID", lstID);
					wrap.putIntArray("ListaTipo", lstTipo);
					wrap.putStringArray("ListaTitulo", lstTitulo);
					wrap.putStringArray("ListaTexto", lstTexto);

					// identifico o tipo de teste
					switch (lstTipo[0]) {
					case 0:
						// lanï¿½ar a nova activity do tipo texto,

						// iniciar a pagina 2 (escolher teste)
						Intent it = new Intent(getApplicationContext(),
								Teste_Texto_Aluno.class);
						it.putExtras(wrap);

						startActivity(it);

						break;
					case 1:// lanï¿½ar a nova activity do tipo Palavras, e o seu conteï¿½do
							//
						Intent ip = new Intent(getApplicationContext(),
								Teste_Palavras_Aluno.class);
						ip.putExtras(wrap);

						startActivity(ip);

						break;
					case 2: // lanï¿½ar a nova activity do tipo Poema, e o seu conteï¿½do
						//
						Intent ipm = new Intent(getApplicationContext(),
								Teste_Poema_Prof.class);
						ipm.putExtras(wrap);

						startActivity(ipm);

						break;
					case 3: // lanï¿½ar a nova activity do tipo imagem, e o seu conteï¿½do
						//
						// Intent it = new Intent(getApplicationContext(),
						// Teste_Texto.class);
						// it.putExtras(wrap);

						// startActivity(it);
						break;
					default:
						Toast.makeText(getApplicationContext(), " - Tipo nï¿½o defenido",
								Toast.LENGTH_SHORT).show();
						// retirar o teste errado e continuar

						int k = 0;
						String aux[] = new String[array.length - 1];
						for (int i = 1; i < array.length; i++) {
							aux[k] = array[i];
							k++;
						}
						array = aux;
						finaliza();
						break;
					}

				}
				//se existir resultados de uma avaliaï¿½ï¿½o, apresenta o resultado.
				if(avaliacao!=null){
					//resultado 
					Bundle wrap = new Bundle();
					wrap.putString("Avaliac",avaliacao);
					//aluno e titulo do teste
					wrap.putString("teste",((TextView)findViewById(R.id.textRodape)).getText()
							+"\n"
							+((TextView)findViewById(R.id.textCabecalho)).getText());
					
					// iniciar a pagina de resultado
					Intent av = new Intent(getApplicationContext(),
							Resultado.class);
					av.putExtras(wrap);

					startActivity(av);			
				}
				finish();
			}*/


}
