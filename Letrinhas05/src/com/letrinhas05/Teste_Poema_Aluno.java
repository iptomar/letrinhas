package com.letrinhas05;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.ClassesObjs.Teste;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Teste_Poema_Aluno extends Activity {

	// flags para verificar os diversos estados do teste
	 			boolean modo, gravado, recording, playing;
	 			// objetos
	 			ImageButton record, playAluno, voltar, playProf, avancar;
	 			int nTestes;
	 			// variaveis contadoras para a avaliação
	 			private MediaRecorder gravador;
	 			private MediaPlayer reprodutorAluno = new MediaPlayer();
	 			private MediaPlayer reprodutorProf = new MediaPlayer();
	 			private String fileName;
	 			private String fileVozProf;
	 			private Handler play_handler = new Handler();
	 			Chronometer chrono;
	 			LetrinhasDB db;
	 			List<Teste> testePoema;
	 			String[] texto;
	 			String[] titulo;
	 			int[] tipo;
	 			

	 			
	 			
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
	 			 	setContentView(R.layout.teste_poema_aluno);
	 			 	//new line faz a rotação do ecrãn 180 graus
	 			 	int currentOrientation = getResources().getConfiguration().orientation;
	 			 	if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
	 			 		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
	 			 		}
	 			 	else {
	 			 		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
	 			 		}
	 			 		
	 			 			
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
	 			 			//  parametros
	 			 				Bundle b = getIntent().getExtras();
	 			 					 				 				
	 			 					db = new LetrinhasDB(this);
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
	 			 				 	}
	 			 				 	
	 			 				 	// nome do ficheiro  de gravação
	 			 				 	fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getString("Professor") + "/" + b.getString("Aluno") + "/" + b.getString("titulo") + ".3gpp";
	 			 				 	//nome do ficheiro da gravação do professor
	 			 				 	fileVozProf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + b.getString("Professor") +  "/" + b.getString("titulo") + ".3gpp";
	 			 				 	//grava leitura
	 			 				 	record = (ImageButton) findViewById(R.id.txtRecord);
	 			 					//ouve professor
	 			 				 	playProf = (ImageButton) findViewById(R.id.txtDemo);
	 			 				 	//volta a traz
	 			 					voltar = (ImageButton) findViewById(R.id.txtVoltar);
	 			 					//termina o teste
	 			 					avancar = (ImageButton) findViewById(R.id.txtAvaliar);
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

				public void setUp() {
					gravador = new MediaRecorder();
					gravador.setAudioSource(MediaRecorder.AudioSource.MIC);
					gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					gravador.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
					// construir as pastas caso necessário
					File file = new File(fileName);
					if (file.getParent() != null && !file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					gravador.setOutputFile(fileName);
				}

				private void escutaBotoes() {
					record.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startGrava();
							chrono.start();
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
							Intent it = new Intent(Teste_Poema_Aluno.this,PaginaInicial.class);
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
							Intent it = new Intent(Teste_Poema_Aluno.this,PaginaInicial.class);
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
						
					}
							
				}
							
							
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
										}
									catch (Exception ex) {
										ex.printStackTrace();
										}	
								}
								else{
									playProf.setImageResource(R.drawable.palyoff);
									record.setVisibility(View.VISIBLE);
									playing = false;
									try {
										reprodutorProf.stop();
										reprodutorProf.release();
										Toast.makeText(getApplicationContext(),"Fim da reprodução.",Toast.LENGTH_SHORT).show();
									} 
									catch (Exception ex) {
										ex.printStackTrace();
										}				
									}
								}				
								}