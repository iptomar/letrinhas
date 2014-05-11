package com.letrinhas04;


import com.letrinhas04.PagInicial;
import com.letrinhas04.R;
import com.letrinhas04.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Página Inicial
 *
 * Uma actividade em full-screen, que mostra e esconde o User Interface de sistema
 * (i.e. status bar and navigation/system bar) com a interação do utilizador
 *
 * @see SystemUiHider
 *
 * @author Thiago
 */
public class PagInicial extends Activity {
	 Button bentrar, exper;	//botão para aceder ao menu
	 ImageButton ibotao;//botão para sair da app
	 ProgressBar link;	//barra de progresso para simbolizar a sincronização caso exista ligação ao servidor.
	
	
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

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	protected static final int desligado = 0, ligado=1, sincroniza=2, sucesso=3, fail=4;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pag_inicial);
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.bEntrar1).setOnTouchListener(mDelayHideTouchListener);
		
		exper = (Button) findViewById(R.id.experiment);
		
		bentrar = (Button) findViewById(R.id.bEntrar1);
        ibotao = (ImageButton) findViewById(R.id.iBSair);
        link= (ProgressBar) findViewById(R.id.pBarLink);
        //bentrar.setEnabled(false);
        
      
		
        
        Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case desligado:
					Toast.makeText(getApplicationContext(), "WI-FI está desligado.", 
							Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(), "Estou a Ligar o WI-FI.",
							Toast.LENGTH_SHORT).show();
					break;
				case ligado:
					Toast.makeText(getApplicationContext(), "O WI-FI já está ligado.", 
							Toast.LENGTH_SHORT).show();
					break;
				case sincroniza:
					Toast.makeText(getApplicationContext(), "A sincronizar a Base de Dados", 
							Toast.LENGTH_SHORT).show();
					break;
				case sucesso:
					Toast.makeText(getApplicationContext(), "Base de Dados sincronizada com sucesso!!",
							Toast.LENGTH_SHORT).show();
					bentrar.setEnabled(true);
					break;
				case fail:
					Toast.makeText(getApplicationContext(), "Perdeu-se a ligação ao servidor, tente mais tarde.",
							Toast.LENGTH_SHORT).show();
					bentrar.setEnabled(true);
					break;
				default:
					break;
				}
			}
		};
		
               
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////CHAMA EM BAKCGORUND A SINCRO DE TABELAS E INSERE NA BASE DE DADOS///////////////////
        String ip = "code.dei.estt.ipt.pt";  ////TROCAR ISTO POR VARIAVEIS COM OS ENDEREÃ‡OS IP QUE NAO SEI ONDE TEM/////////
        String porta = "80";
        //Forma o endereÃ§o http
        String   URlString = "http://" + ip + ":" + porta + "/";
        //SincAllBd sin = new SincAllBd(this,handler, URlString);
        //sin.start();
        ///////////////PODEM VER EM LOGCAT A INSERIR TODOS OS DADOS NA TABELA /////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        escutaBotoes();
	}

	private void escutaBotoes(){
		
		
        bentrar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	 /************** Por fazer, ##############################################################
                         * Verificar se existe algum professor "logado", 
                         * se sim, avança para a escolha do modo de execução (Prof. ou aluno)
                         * se não, executa a escolha da escola, do professor,
                         * que posteriormente carrega  turmas / alunos ao seu encargo.
                         * 
                         *  o utilizador (professor) escolhe o aluno que vai executar o teste e o seu modo (Aluno = treino) 
                         *  ou (Professor=avaliação).
                         * 
                         */
                    	
                    	
                    //    Intent it= new Intent(PaginaInicial.this, EscModo.class);//Autenticacao.class);
                        
                        
                    //    startActivity(it);
                        //finish();
                    }
                }

        );

        ibotao.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //sair da aplicaï¿½ï¿½o
                        java.lang.System.exit(RESULT_OK);
                    }
                }
        );
        
        exper.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //iniciar a pagina 
                        //Intent it= new Intent(PaginaInicial.this,MainScreenActivity.class);//Autenticacao.class);
                        //startActivity(it);
                        //finish();
                    	Toast.makeText(getApplicationContext(), "Experiências (testes), nenhuma Pagina associada!",
        						Toast.LENGTH_LONG).show();
                    }
                }
        );
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
}