package com.letrinhas05;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTeste;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.ClassesObjs.TesteLeitura;
import com.letrinhas05.escolhe.EscolheEscola;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Teste_Poema extends Activity {

				TesteLeitura teste;
				// flags para verificar os diversos estados do teste
	 			boolean modo, gravado, recording, playing;
	 			// objetos
	 			ImageButton record, playAluno, voltar, playProf, avancar;
	 			int nTestes;
	 			// variaveis contadoras para a avaliação
	 			private MediaRecorder gravador;
	 			private MediaPlayer reprodutorAluno = new MediaPlayer();
	 			private MediaPlayer reprodutorProf = new MediaPlayer();
	 			private String fileName,filePath, audio, tempo;
	 			private String fileVozProf;
	 			
	 			Chronometer chrono;
	 			LetrinhasDB db;
	 			List<Teste> testePoema;
	 			String[] texto;
	 			String[] titulo;
	 	
	 			
	 			int tipo, idTesteAtual;
	 			String[] Nomes;
	 			int[] iDs, testesID;
	 			
	 			
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
	 			 	setContentView(R.layout.teste_poema);
	 			 	//ecrã em landscape para poema
	 			 	int currentOrientation = getResources().getConfiguration().ORIENTATION_PORTRAIT;
	 			 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
	 			 	/* 	if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
	 			 		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
	 			 		}
	 			 	else {
	 			 		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
	 			 		}*/
	 			 	
	 			 			
	 			 	final View contentView = findViewById(R.id.testPoema);
	 			 				
	 			 	// Set up an instance of SystemUiHider to control the system UI for
	 			 	// this activity.
	 			 	mSystemUiHider = SystemUiHider.getInstance(this, contentView,HIDER_FLAGS);
					mSystemUiHider.setup();
					mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
						
					// Cached values.
	 			 	int mShortAnimTime;
	 			 	
	 			 	@Override
	 			 	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	 			 	public void onVisibilityChange(boolean visible) {
	 			 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	 			 			if (mShortAnimTime == 0) {
	 			 				mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
	 			 				}
	 			 			}
	 			 		if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
	 			 				}
	 			 			});
	 			 				//parametros
	 			 				Bundle b = getIntent().getExtras();
	 			 				inicia(b); 				 				
	 			 					/*db = new LetrinhasDB(this);
	 			 					testePoema = db.getAllTeste();
	 			 				 	nTestes = testePoema.size();
	 			 				 	texto = new String[nTestes];
	 			 				 	titulo = new String[nTestes];

	 			 				 	for(int i=0; i<nTestes;i++){
	 			 				 		for(Teste t: testePoema){
	 			 				 		texto[i] = t.getTexto();
	 				 	 				tipo[i] = t.getTipo();
	 				 	 				titulo[i] = t.getTitulo();
	 			 				 		}
	 			 				 	}*/
	 			 				 	
	 			 				 	// nome do ficheiro  de gravação
	 			 				 	fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getString("Professor") + "/" + b.getString("Aluno") + "/" + b.getString("titulo") + ".3gpp";
	 			 				 	//nome do ficheiro da gravação do professor
	 			 				 	fileVozProf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getString("Professor") +  "/" + b.getString("titulo") + ".3gpp";
	 			 				 	//grava leitura
	 			 				 	record = (ImageButton) findViewById(R.id.txtRecord);
	 			 				 	//ouve a própria gravação
	 			 				 	playAluno =(ImageButton) findViewById(R.id.play);
	 			 				 	//butão invisivel até ter gravação para mostrar
	 			 				 	playAluno.setVisibility(View.INVISIBLE);
	 			 					//ouve professor
	 			 				 	playProf = (ImageButton) findViewById(R.id.txtDemo);
	 			 				 	//volta a traz
	 			 					voltar = (ImageButton) findViewById(R.id.txtVoltar);
	 			 					//termina o teste
	 			 					avancar = (ImageButton) findViewById(R.id.txtAvaliar);
	 			 					escutaBotoes();
	 			 				
	 			 }	
	 			 
	 			public void inicia(Bundle b){
	 				// Compor novamente e lista de testes
	 				testesID = b.getIntArray("ListaID");
	 				// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
	 				Nomes = b.getStringArray("Nomes");
	 				// int's - idEscola, idProfessor, idTurma, idAluno
	 				iDs = b.getIntArray("IDs");

	 				/** Consultar a BD para preencher o conteï¿½do.... */
	 				LetrinhasDB bd = new LetrinhasDB(this);
	 				teste =  bd.getTesteLeituraById(testesID[0]);

	 				((TextView) findViewById(R.id.textCabecalho))
	 						.setText(teste.getTitulo());
	 				((TextView) findViewById(R.id.txtTexto)).setText(teste.getConteudoTexto());

	 				// **********************************************************************************************

	 				idTesteAtual = testesID[0];
	 				filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
	 						+ "/School-Data/submits/" + iDs[0] + "/" + iDs[1] + "/"
	 						+ iDs[2] + "/" + iDs[3] + "/" + "/" + testesID[0] + "/";

	 				fileName = getCurrentTimeStamp() + ".3gpp";

	 				audio = Environment.getExternalStorageDirectory().getAbsolutePath()
	 						+ "/School-Data/ReadingTests/"+teste.getProfessorAudioUrl();

	 				// descontar este teste da lista.
	 				int[] aux = new int[testesID.length - 1];
	 				for (int i = 1; i < testesID.length; i++) {
	 					aux[i - 1] = testesID[i];
	 				}
	 				testesID = aux;
	 				
	 			}
	 			 
	 			 
	 			/**
	 			 * 
	 			 * @return yyyy-MM-dd HH:mm:ss formate date as string
	 			 */
	 			@SuppressLint("SimpleDateFormat")
	 			public static String getCurrentTimeStamp() {
	 				String aux = "";
	 				try {
	 					SimpleDateFormat dateFormat = new SimpleDateFormat(
	 							"yyyy-MM-dd HH:mm:ss");
	 					String currentTimeStamp = dateFormat.format(new Date()); // Find
	 																				// todays
	 																				// date
	 					for (int i = 0; i < currentTimeStamp.length(); i++) {
	 						// descarto tudo o que não é um numero
	 						if (currentTimeStamp.charAt(i) >= '0'
	 								&& currentTimeStamp.charAt(i) < '9') {
	 							aux += currentTimeStamp.charAt(i);
	 						}
	 					}

	 				} catch (Exception e) {
	 					aux = "today";
	 				}
	 				return aux;
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

				public void setUp() {
					gravador = new MediaRecorder();
					gravador.setAudioSource(MediaRecorder.AudioSource.MIC);
					gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					gravador.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
					// construir as pastas caso necessário
					File file = new File(filePath+fileName);
					if (file.getParent() != null && !file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					gravador.setOutputFile(filePath+fileName);
				}

				private void escutaBotoes() {
					record.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startGrava();
							chrono.start();
						}

					});
					
					//para ouvir a própia leitura 
					playAluno.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startPlay();
						}
					});
					
					//para ouvir a leitura do professor
					playProf.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startPlay();
						}
					});
					
					avancar.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							chrono.stop();
							//isto depois tem de ir para outra página
							Intent it = new Intent(Teste_Poema.this,PaginaInicial.class);
							startActivity(it);
						}
					});
					voltar.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if(playing){
								reprodutorProf.stop();
							}
							if(recording){
								gravador.stop();
							}
							Intent it = new Intent(Teste_Poema.this,PaginaInicial.class);
							startActivity(it);
						}
					});
					}

		
				@SuppressLint("HandlerLeak")
				private void startGrava() {
					if (!recording) {
						
						record.setImageResource(R.drawable.stop);
						playProf.setVisibility(View.INVISIBLE);
						voltar.setVisibility(View.INVISIBLE);
						avancar.setVisibility(View.INVISIBLE);
						recording = true;
						
						chrono = (Chronometer) findViewById(R.id.cromTxt);
						
							setUp();
							try {
								gravador.prepare();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							gravador.start();
							Toast.makeText(getApplicationContext(), "A gravar.",Toast.LENGTH_SHORT).show();
							chrono.start();
								
								}
					else{
						record.setImageResource(R.drawable.record);
						playProf.setVisibility(View.VISIBLE);
						voltar.setVisibility(View.VISIBLE);
						avancar.setVisibility(View.VISIBLE);
						recording = false;
						chrono.stop();
						tempo=chrono.getText().toString();
						chrono.refreshDrawableState();
					}
							
				}
							
				private final int PARADO = 2;
				private Handler play_handler;

							@SuppressLint("HandlerLeak")
							private void startPlay() {
								if (!playing) {
									playProf.setImageResource(R.drawable.play_on);
									record.setVisibility(View.INVISIBLE);
									playing = true;
									try {
										reprodutorProf = new MediaPlayer();
										reprodutorProf.setDataSource("fileVozProfessor");
										reprodutorProf.prepare();
										reprodutorProf.start();
										Toast.makeText(getApplicationContext(), "A reproduzir.",Toast.LENGTH_SHORT).show();
										//a tratar
										final ImageView img2 = new ImageView(this);
										img2.setImageResource(R.drawable.palyoff);
										// espetar aqui uma thread, para caso isto pare
										// handler para controlara a GUI do androi e a thread seguinte
										play_handler = new Handler() {
											public void handleMessage(Message msg) {
												switch (msg.what) {
												case PARADO:
													//demo.setCompoundDrawablesWithIntrinsicBounds(null,
													//		img2.getDrawable(), null, null);
													record.setVisibility(View.VISIBLE);
													playProf.setVisibility(View.VISIBLE);
													//demo.setText("Demo");
													playing = false;
													try {
														reprodutorProf.stop();
														reprodutorProf.release();
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
										while (reprodutorProf.isPlaying())
											;
										Message msg = new Message();
										msg.what = PARADO;
										play_handler.sendMessage(msg);
									}
								}).start();
										
								}
									catch(Exception e){
										
									}
								}
							}
							private void finaliza() {
								
								if (testesID.length != 0) {
									// enviar o parametro de modo
									Bundle wrap = new Bundle();

									wrap.putIntArray("ListaID", testesID);// id's dos testes
									wrap.putStringArray("Nomes", Nomes);
									wrap.putIntArray("IDs", iDs);

									// identifico o tipo do próximo teste
									LetrinhasDB bd = new LetrinhasDB(this);
									Teste tst = bd.getTesteById(testesID[0]);
									tipo = tst.getTipo();

									switch (tipo) {
									case 0:
										// lanï¿½ar a nova activity do tipo texto,
										Intent it = new Intent(getApplicationContext(),
												Teste_Texto.class);
										it.putExtras(wrap);

										startActivity(it);

										break;
									case 1:// lanï¿½ar a nova activity do tipo imagem
										Intent ip = new Intent(getApplicationContext(),
												Teste_Imagem.class);
										ip.putExtras(wrap);

										startActivity(ip);

										break;
									case 2: // lanï¿½ar a nova activity do tipo Palavras
										Intent ipm = new Intent(getApplicationContext(),
												Teste_Palavras_Aluno.class);
										ipm.putExtras(wrap);

										startActivity(ipm);

										break;
									case 3: // lanï¿½ar a nova activity do tipo poema
										Intent ipp = new Intent(getApplicationContext(),
												Teste_Palavras_Aluno.class);
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

							
							public void submit() {
								CorrecaoTesteLeitura ctl = new CorrecaoTesteLeitura();
								File file = new File(filePath + fileName);
								if (!file.exists()) {
									Toast.makeText(getApplicationContext(), "Não gravou nada",
											Toast.LENGTH_SHORT).show();
								} else {
									
									long time = System.currentTimeMillis() / 1000;
									
									String aux = idTesteAtual + iDs[3] + time + "";
									ctl.setIdCorrrecao(Long.parseLong(aux));
									ctl.setAudiourl(filePath + fileName);
									ctl.setDataExecucao(time);
									ctl.setTipo(0);// pois estou num teste texto
									ctl.setEstado(0);
									ctl.setTestId(idTesteAtual);
									ctl.setIdEstudante(iDs[3]);
									Toast.makeText(getApplicationContext(),
											"audio: " + ctl.getAudiourl(), Toast.LENGTH_SHORT).show();
									Toast.makeText(getApplicationContext(),
											"data: " + getCurrentTimeStamp(), Toast.LENGTH_SHORT)
											.show();
									Toast.makeText(getApplicationContext(),
											"ID Aluno: " + String.valueOf(ctl.getIdEstudante()),
											Toast.LENGTH_SHORT).show();
									Toast.makeText(getApplicationContext(),
											"Id Teste: "+String.valueOf(ctl.getTestId()), Toast.LENGTH_SHORT).show();

									LetrinhasDB bd = new LetrinhasDB(this);
									bd.addNewItemCorrecaoTesteLeitura(ctl);

									List<CorrecaoTeste> data1 = bd.getAllCorrecaoTeste();
									Log.d("CheckInserts: ", "***********Testes******************");
									for (CorrecaoTeste cn : data1) {
										String logs = "Id: " + cn.getIdCorrrecao() + ", idEstudante: "
												+ cn.getIdEstudante() + "  , estado: " + cn.getEstado()
												+ "  , testeId: " + cn.getTestId() + "  , tipo: "
												+ cn.getTipo() + "  , data: " + cn.getDataExecucao();
										// Writing Contacts to log
										Log.d("CheckInserts: ", logs);
									}
									 finaliza();
									 
									 Bundle wrap = new Bundle();
										wrap.putInt("IDTeste", idTesteAtual);// id do teste atual
										wrap.putInt("IDAluno", iDs[3]); //id do aluno
										// listar submissões anteriores do mesmo teste
										 Intent it = new Intent(getApplicationContext(),
										 ResumoSubmissoes.class);
										 it.putExtras(wrap);
										 startActivity(it);
										 
								}
							}

							}
