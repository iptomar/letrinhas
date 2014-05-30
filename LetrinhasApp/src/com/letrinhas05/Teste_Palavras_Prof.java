package com.letrinhas05;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTeste;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.util.Avaliacao;
import com.letrinhas05.util.SystemUiHider;
import com.letrinhas05.util.Teste;

public class Teste_Palavras_Prof extends Activity{
			boolean playing,flag;
			// objetos
			ImageButton play, voltar, cancelar, avancar;
			int plvErradas=0,id_teste, totalDePalavras=0;
			private MediaPlayer reprodutor = new MediaPlayer();
			private String endereco,uuidAudio;
			CorrecaoTesteLeitura ctl;
			Teste[] lista;
			LetrinhasDB db;
			String[] nomes;
			int[] ids;
			String text;
			TextView valueWord;
			int testId=0, idEstudante=0, tipo=0, estado=0, numPalavCorretas=0, numPalavIncorretas=0;
			long dataExecucao=0, idCorrrecao=0;
			float numPalavrasMin=0, precisao=0, velocidade=0, expressividade=0, ritmo=0;
			double milisegundos; 
			int segundos, minutos, horas,auxiliar;
			String observacoes="empty", detalhes="empty", stringAuxForType,dataDeExecucao;
			Avaliacao eval;
			Chronometer chrono;
			ProgressBar pbDuracao;
			
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
				setContentView(R.layout.teste_palavras_prof);
				//new line faz a rotaï¿½ï¿½o do ecrï¿½n 180 graus
				int currentOrientation = getResources().getConfiguration().orientation;
				if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				}else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}
				// / esconder o title************************************************+
				final View contentView = findViewById(R.id.corecaoPalavras);
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
				// buscar os parametros
				Bundle b = getIntent().getExtras();
				nomes = b.getStringArray("Nomes");
				ids = b.getIntArray("IDs");
				id_teste = b.getInt("ID_teste"); 
				dataDeExecucao = b.getString("dataDeExecucao");
				db = new LetrinhasDB(this);
				
				List<CorrecaoTesteLeitura> a = db.getAllCorrecaoTesteLeitura_ByIDaluno_TestID(ids[3], id_teste);
				String[] g = new String[a.size()];
				int x=0;
				for(CorrecaoTesteLeitura asdf: a){
					g[x] = asdf.getAudiourl().toString();
					auxiliar = asdf.getTipo();
					uuidAudio = g[x];
					Log.d("Debug-url", g[x]+" awehfe "+x);
					x++;
				}
				Log.d("Debug-id_teste", String.valueOf(id_teste));

				Log.d("Debug-uuidAudio", uuidAudio);
				
				text = db.getTesteLeituraById(id_teste).getConteudoTexto();
				Log.d("Debug-text",text);
				Log.d("Debug-nomes.lenght",String.valueOf(nomes.length));
				for(int i = 0;i<nomes.length;i++){
					// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
					Log.d("Debug-nomes",nomes[i]);
				}
				for(int i = 0;i<ids.length;i++){
					// int's - idEscola, idProfessor, idTurma, idAluno
					Log.d("Debug-ids",String.valueOf(ids[i]));
				}
				Log.d("Debug-id_teste",String.valueOf(id_teste));
				// Consultar a BD para preencher o conteï¿½do....
				//((TextView) findViewById(R.id.textCabecalho)).setText(lista[0].getTitulo());
				//((TextView) findViewById(R.id.textRodape)).setText(b.getString("Aluno"));
				endereco = Environment.getExternalStorageDirectory().getAbsolutePath() + uuidAudio;
				Log.d("Debug-pathAudio", endereco);
				valueWord = (TextView) findViewById(R.id.ValueWord);
				valueWord.setText("0");
				
				// ordenaï¿½ï¿½o do texto nas trï¿½s colunas de forma a preencher toda de seguida a 1ï¿½ e sï¿½ depois passa para as outras
				int lenght;
				String[] ar = text.split("[ ]");
				int[] restoVal = new int[2];
				lenght = ar.length/3;
				Log.d("Debug-length_totil", String.valueOf(ar.length));
				Log.d("Debug-length", String.valueOf(ar.length/3));
				Log.d("Debug-Resto", String.valueOf(ar.length%3));
				if(ar.length%3 == 2){
					restoVal[0] = 1;
					restoVal[1] = 1;
				}else if(ar.length%3 == 1){
					restoVal[0] = 1;
					restoVal[1] = 0;
				}else if(ar.length%3 == 0){
					restoVal[0] = 0;
					restoVal[1] = 0;
				}
				String[] texto, texto1, texto2;
				if(ar.length==1){
					texto = new String[lenght+restoVal[0]];
					texto1 = new String[0];
					texto2 = new String[0];
					for(int i=0;i<((ar.length)/3)+restoVal[0];i++){
						texto[i] = ar[i];
					}
				}else if(ar.length==2){
					texto = new String[lenght+restoVal[0]];
					texto1 = new String[lenght+restoVal[1]];
					texto2 = new String[0];
					int var=0,resto,support=0;
					for(int i=0;i<((ar.length)/3)+restoVal[0];i++){
						texto[i] = ar[i];
						var=i;
					}
					resto = ar.length - (ar.length)/3;
					for(int j = var+1;j<resto+restoVal[1];j++){
						texto1[support] = ar[j];
						support++;
					}
				}else{
					texto = new String[lenght+restoVal[0]];
					texto1 = new String[lenght+restoVal[1]];
					texto2 = new String[lenght];
					int var=0,var1=0,resto,support=0,support1=0;
					for(int i=0;i<(lenght)+restoVal[0];i++){
						texto[i] = ar[i];
						var=i;
					}
					resto = ar.length - lenght;
					Log.d("Debug-Resto_in", String.valueOf(resto));
					Log.d("Debug-Resto_var", String.valueOf(var));
					Log.d("Debug-value", String.valueOf(resto+restoVal[1]));
					for(int j = var+1;j<resto;j++){
						texto1[support] = ar[j];
						var1=j;
						support++;
					}
					for(int k = var1+1;k<ar.length;k++){
						texto2[support1] = ar[k];
						support1++;
					}
				}
				initSetup(getResources(), R.id.tlpScroll, R.id.tlpToggleButton, R.id.tlplllayer,texto);
				initSetup(getResources(), R.id.tlpScroll1, R.id.tlpToggleButton1, R.id.tlplllayer1,texto1);
				initSetup(getResources(), R.id.tlpScroll2, R.id.tlpToggleButton2, R.id.tlplllayer2,texto2);
				// **********************************************************************************************
				
				play = (ImageButton) findViewById(R.id.tlpVoicePlay);
				voltar = (ImageButton) findViewById(R.id.tlpVoltar);
				cancelar = (ImageButton) findViewById(R.id.tlpCancel);
				avancar = (ImageButton) findViewById(R.id.tlpAvaliar);
				chrono = (Chronometer) findViewById(R.id.tlpcrom);
				pbDuracao = (ProgressBar) findViewById(R.id.pbPalavreas);
				
				try {
					reprodutor.setDataSource(endereco);
					reprodutor.prepare();
					pbDuracao.setMax(reprodutor.getDuration());
					segundos = ((reprodutor.getDuration() / 1000) % 60);
					minutos = ((reprodutor.getDuration() / 1000) / 60);
				} catch (Exception ex) {
				}
				chrono.setText(n2d(minutos) + ":" + n2d(segundos));
				escutaBotoes();
			}

			// método para acrescentar um 0 nas casas das dezenas,
			// caso o númer seja inferior a 10
			private String n2d(int n) {
				String num;
				if (n / 10 == 0) {
					num = "0" + n;
				} else {
					num = "" + n;
				}
				return num;
			}
			
			// forçar a paragem da reprodução do audio!
			private void stopPlay() {
				if (playing) {
					reprodutor.stop();
					reprodutor.release();
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

			private void escutaBotoes() {
				play.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						startPlay();
					}
				});
				cancelar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						stopPlay();
						finish();
					}
				});
				avancar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// voltar para pag inicial
						startAvalia();
						finish();
					}
				});
				voltar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// voltar para pag inicial
						stopPlay();
						finish();
					}
				});
			}
			int minuto, segundo;
		
			/**
			 * znyt
			 * znrdy
			 * xdgh
			 * dnyx<
			 * dm
			 * @author Dï¿½rio
			 */
			public void submit(){
				ctl = new CorrecaoTesteLeitura();

				int[] valueInt1 = {testId, idEstudante, tipo, estado, numPalavCorretas, numPalavIncorretas};
				long[] valueLong1 = {dataExecucao, idCorrrecao};
				float[] valueFloat1 = {numPalavrasMin, precisao, velocidade, expressividade, ritmo};
				String[] valueString1 = {observacoes, detalhes};

				ctl.setTestId(valueInt1[0]);
				ctl.setIdEstudante(valueInt1[1]);
				ctl.setTipo(valueInt1[2]);
				ctl.setEstado(valueInt1[3]);
				ctl.setNumPalavCorretas(valueInt1[4]);
				ctl.setNumPalavIncorretas(valueInt1[5]);
				ctl.setDataExecucao(valueLong1[0]);
				ctl.setIdCorrrecao(valueLong1[1]);
				ctl.setNumPalavrasMin(valueFloat1[0]);
				ctl.setPrecisao(valueFloat1[1]);
				ctl.setVelocidade(valueFloat1[2]);
				ctl.setExpressividade(valueInt1[3]);
				ctl.setRitmo(valueInt1[4]);
				ctl.setObservacoes(valueString1[0]);
				ctl.setDetalhes(valueString1[1]);
				//db.addNewItemCorrecaoTesteLeitura(ctl);

				List<CorrecaoTeste> data1 = db.getAllCorrecaoTeste();
				Log.d("CheckInserts: ", "***********Testes******************");
				for (CorrecaoTeste cn : data1) {
					String logs = "Id: " + cn.getIdCorrrecao() + ", idEstudante: "
							+ cn.getIdEstudante() + "  , estado: " + cn.getEstado()
							+ "  , testeId: " + cn.getTestId() + "  , tipo: "
							+ cn.getTipo() + "  , data: " + cn.getDataExecucao();
					// Writing Contacts to log
					Log.d("CheckInserts: ", logs);
				}

				Bundle wrap = new Bundle();
				numPalavIncorretas = plvErradas;
				//int[] -> testId, idEstudante, tipo, estado,numPalavCorretas, numPalavIncorretas, totalDePalavras
				int[] valueInt = {testId, idEstudante, tipo, estado, numPalavCorretas, numPalavIncorretas, totalDePalavras};
				Log.d("Debug-valueInt[0]", String.valueOf(valueInt[0]));
				wrap.putIntArray("ints", valueInt);

				//float[] -> numPalavrasMin, precisao, velocidade, expressividade, ritmo
				float[] valueFloat = {numPalavrasMin, precisao, velocidade, expressividade, ritmo};
				wrap.putFloatArray("floats", valueFloat);

				//String[] -> observacoes, detalhes, stringAuxForType
				String[] valueString = {observacoes, detalhes,stringAuxForType,dataDeExecucao};
				wrap.putStringArray("strings", valueString);
				//wrap.putInt("IDTeste", idTesteAtual);// id do teste atual
				//wrap.putInt("IDAluno", iDs[3]); //id do aluno
				// listar submissï¿½es anteriores do mesmo teste
				 Intent it = new Intent(getApplicationContext(),
				 RelatasCorrection.class);
				 it.putExtras(wrap);
				 startActivity(it);
			}

			private final int PARADO = 2, ANDANDO = 1;
			private int tSegundo = segundos, tMinuto = minutos;
			private Handler play_handler, play_handler2;;
			
			/**
			 * serve para a aplicacao reproduzir ou parar o som
			 * 
			 * @author Dario Jorge
			 */
			@SuppressLint("HandlerLeak")
			private void startPlay() {
				if (!playing) {
					resetTimer();
					play.setImageResource(R.drawable.play_on);
					playing = true;
					try {
						reprodutor = new MediaPlayer();
						reprodutor.setDataSource(endereco);
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
									play.setImageResource(R.drawable.palyoff);
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
								while (reprodutor.isPlaying());
								msg.what = PARADO;
								play_handler.sendMessage(msg);
							}
						}).start();
						play_handler2 = new Handler() {
							public void handleMessage(Message msg) {
								switch (msg.what) {
								case ANDANDO:
									pbDuracao.setProgress(reprodutor.getCurrentPosition());
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
						play.setImageResource(R.drawable.palyoff);
						resetTimer();
						playing = false;
					}
				} else {
					play.setImageResource(R.drawable.palyoff);
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
					reprodutor.setDataSource(endereco);
					reprodutor.prepare();
					pbDuracao.setMax(reprodutor.getDuration());
					pbDuracao.setProgress(0);
				} catch (Exception ex) {
				}
				tSegundo = segundos;
				tMinuto = minutos;
				chrono.setText(n2d(tMinuto) + ":" + n2d(tSegundo));
			}

			/**
			 * Este metodo servirco para iniciar a avaliacao
			 */
			private void startAvalia() {
					File file = new File(endereco);
					if (file.exists()) { // se ja fez uma gravacao
						// uma pop-up ou activity para determinar o valor de
						// exprecividade da leitura
						// usar a classe Avaliacao para calcular os resultados.
						resetTimer();
						eval = new Avaliacao(totalDePalavras, 0, plvErradas);
						numPalavCorretas = eval.palavrasCertas();
						velocidade = eval.VL(tMinuto, tSegundo);
						precisao = eval.PL();
						switch(auxiliar){
							case 0:
								stringAuxForType = "Texto";
								break;
							case 1:
								stringAuxForType = "Multimedia";
								break;
							case 2:
								stringAuxForType = "Palavras";
								break;
							case 3:
								stringAuxForType = "Poema";
								break;
							default:
								stringAuxForType = "Indefenido";
								break;
						}
						numPalavrasMin = eval.PLM(tMinuto, tSegundo);
						submit();
					} else {
						android.app.AlertDialog alerta;
						// Cria o gerador do AlertDialog
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						// define o titulo
						builder.setTitle("Letrinhas");
						// define a mensagem
						builder.setMessage(" Nao existe o ficheiro audio!");
						// define um botï¿½o como positivo
						builder.setPositiveButton("OK", null);
						// cria o AlertDialog
						alerta = builder.create();
						// Mostra
						alerta.show();
					}
			}

			/**
			 * este metodo irao criar o primeiro butao, que irao servir de modelo para os restantes
			 */
			public void initSetup(Resources res,int list, int toggle, int layout, String[] ar){
				ToggleButton tg = (ToggleButton) findViewById(toggle);
				tg.setTextColor(Color.DKGRAY);
				tg.setTextSize(22);
				tg.setBackgroundColor(Color.DKGRAY);
				tg.setTextColor(Color.WHITE);
				totalDePalavras++;
				tg.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
				          if (((CompoundButton) v).isChecked()){
							v.setBackgroundColor(Color.RED);
							plvErradas++;
							Log.d("Debug-plvErradas",String.valueOf(plvErradas));
							valueWord.setText(String.valueOf(plvErradas));
				          }else{
							v.setBackgroundColor(Color.DKGRAY);
							plvErradas--;
							valueWord.setText(String.valueOf(plvErradas));
				          }
				   }
				});
		        tg.setText(ar[0]);
		        tg.setTextOn(ar[0]);
		        tg.setTextOff(ar[0]);
				LinearLayout ll = (LinearLayout) findViewById(layout);
		        // Resto do tï¿½tulos
				for(int i = 1; i<ar.length;i++){
					buttonSetUp(ar,i,ll,tg);
					totalDePalavras++;
				}	
				Log.d("Debug-totalDePalavras", String.valueOf(totalDePalavras));
			}

			/**
			 * Esta metodo serve para a criaï¿½ï¿½o de todos os outros butï¿½es
			 * @param teste
			 * @param i
			 * @param ll
			 * @param tg1
			 */
			public void buttonSetUp(String[] teste,int i,LinearLayout ll,ToggleButton tg1){
				ToggleButton tg = new ToggleButton(getBaseContext());
				tg.setLayoutParams(tg1.getLayoutParams());
				tg.setBackgroundColor(Color.DKGRAY);
				tg.setTextColor(Color.WHITE);
				tg.setTextSize(22);
				tg.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
				          if (((CompoundButton) v).isChecked()){
							v.setBackgroundColor(Color.RED);
							plvErradas++;
							valueWord.setText(String.valueOf(plvErradas));
				          }else{
							v.setBackgroundColor(Color.DKGRAY);
							plvErradas--;
							valueWord.setText(String.valueOf(plvErradas));
				          }
				   }
				});
		        tg.setText(teste[i]);
		        tg.setTextOn(teste[i]);
		        tg.setTextOff(teste[i]);
				ll.addView(tg);
			}
}