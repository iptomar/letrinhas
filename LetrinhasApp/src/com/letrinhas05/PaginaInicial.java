package com.letrinhas05;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.BaseDados.SincAllBd;
import com.letrinhas05.escolhe.EscolheEscola;
import com.letrinhas05.util.SystemUiHider;

/**
 * Pagina Inicial
 * 
 * com um exemplo de uma actividade full-screen, que mostra e esconde o User
 * Interface de sistema (i.e. status bar and navigation/system bar) com a
 * interacao do utilizador
 * 
 * @see SystemUiHider
 * 
 * @author Thiago
 */
public class PaginaInicial extends Activity {
    //Variaveis
	public Button bentrar; // botoes para aceder ao menu
    public ImageButton btnSair, btnSettings;// botao para sair da app
    public ProgressBar progBar;
    public TextView txtViewMSG;
    public Context context;
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

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pagina_inicial);

        //Declaracao de objectos da janela
        progBar = (ProgressBar) findViewById(R.id.progressBarLoad);
        progBar.setVisibility(View.INVISIBLE);
        txtViewMSG = (TextView) findViewById(R.id.txtMsg);
        txtViewMSG.setVisibility(View.INVISIBLE);
        findViewById(R.id.bEntrar1).setOnTouchListener(mDelayHideTouchListener);
        bentrar = (Button) findViewById(R.id.bEntrar1);
        btnSair = (ImageButton) findViewById(R.id.iBSair);
        btnSettings = (ImageButton) findViewById(R.id.btnSettingsMenuP);
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        context = this;
		        // new line faz a rotacao do ecren em 180 graus
				int currentOrientation = getResources().getConfiguration().orientation;
				if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				} else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
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


		// verifica se existe alguma coisa na BD local, vou � primeira tabela
		// necessaria
		LetrinhasDB db = new LetrinhasDB(this);
		int totalEscolas= db.getEscolasCount();

		// Se n�o existir, tentamos aqui ligar ao servidor
		if (totalEscolas == 0) {
			try {
                // bloqueiar o bot�o Entrar
				Toast.makeText(this,"Sem informacao na Base de dados local!\n"
								+ "A descarregar BASE DADOS do servidor!\n",
						Toast.LENGTH_LONG).show();
                sincBd();
				// /////////////////////////////////////////////////////////////////////////////////////////
			} catch (Exception e2) {
                txtViewMSG.setText("ERROO....");
				Toast.makeText(this, "Nao foi possivel aceder ao servidor!\n"
						+ "Debug: " + e2, Toast.LENGTH_LONG).show();
			}
		} else {
			// desbloqueia botao de entrar
            progBar.setVisibility(View.INVISIBLE);
            txtViewMSG.setVisibility(View.INVISIBLE);
			bentrar.setEnabled(true);
		}
		escutaBotoes();
	}



	private void escutaBotoes() {
		bentrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// *****************************************************************************
				// iniciar a pagina 2 (escolher escola ou ent�o passar logo para
				// a pagina escolher turma(brevemente))
				Intent it = new Intent(PaginaInicial.this, EscolheEscola.class);// Autenticacao.class);
				startActivity(it);
				finish();
			}
		});

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Configuracoes");
                builder.setItems(new CharSequence[]
                                {"Sinc Manual", "Enviar Correcoes Corrigidas", "Cancelar"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        sincBd();
                                        break;
                                    case 1:
                                        EnviarCorrecoes();
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });
        btnSair.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// sair da aplica��o
				java.lang.System.exit(RESULT_OK);
			}
		});
	}

    /**
     * sinc fAZ A SINCRONIZACAO
     */
    private void sincBd() {
        bentrar.setEnabled(false);
        txtViewMSG.setText("A carregar....");
        progBar.setVisibility(View.VISIBLE);
        txtViewMSG.setVisibility(View.VISIBLE);
        ////////////CHAMA EM BAKCGORUND A SINCRO DE TABELAS E INSERE NA BASE DE DADOS /////////////
        String ip = "code.dei.estt.ipt.pt"; // //TROCAR ISTO POR
        // VARIAVEIS
        // COM OS ENDERECOS IP QUE NAO SEI ONDE TEM/////////
        String porta = "80";
        // Forma o endere�o http
        String URlString = "http://" + ip + ":" + porta + "/";
        String[] myTaskParams = { URlString, URlString, URlString };
        new SincAllBd(this, this, 0).execute(myTaskParams);
    }

        public void EnviarCorrecoes(){
            bentrar.setEnabled(false);
            txtViewMSG.setText("A carregar....");
            progBar.setVisibility(View.VISIBLE);
            txtViewMSG.setVisibility(View.VISIBLE);
            String URlString = "http://code.dei.estt.ipt.pt:80/Api/Tests/Submit";
            String[] myTaskParams = { URlString, URlString, URlString };
            new SincAllBd(this, this, 1).execute(myTaskParams);
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
