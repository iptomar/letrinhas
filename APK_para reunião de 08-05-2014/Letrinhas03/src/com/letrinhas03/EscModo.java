package com.letrinhas03;

import com.letrinhas03.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EscModo extends Activity {
	ImageButton aluno, prof, volt;

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
		setContentView(R.layout.esc_modo);

		// / esconder o title************************************************+
		final View contentView = findViewById(R.id.escModo);

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

		// inicializar os botões
		aluno = (ImageButton) findViewById(R.id.ecmAluno);
		prof = (ImageButton) findViewById(R.id.esmProf);
		volt = (ImageButton) findViewById(R.id.escmVoltar);

		escutaBotoes();
		Toast.makeText(this, "BD sincronizada.", 
				Toast.LENGTH_SHORT).show();
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
		aluno.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				modAluno();
			}
		});

		prof.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				modProf();
			}
		});

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// voltar para pag inicial
				finish();
			}
		});
	}

	public void modAluno() {
		((TextView) findViewById(R.id.tvmoAluno)).setTextColor(Color.GREEN);
		((TextView) findViewById(R.id.tvmoProf)).setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
		// enviar o parametro de modo
		Bundle wrap = new Bundle();
		wrap.putBoolean("Modo", false);

		// iniciar a pagina 2 (escolher teste)
		Intent it = new Intent(EscModo.this, EscolheTeste.class);
		it.putExtras(wrap);
		startActivity(it);

	}

	public void modProf() {
		// declarar de como o teste será apresentado!
		((TextView) findViewById(R.id.tvmoProf)).setTextColor(Color.GREEN);
		((TextView) findViewById(R.id.tvmoAluno)).setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));

		// enviar o parametro de modo
		Bundle wrap = new Bundle();
		wrap.putBoolean("Modo", true);
		// iniciar a pagina 2 (escolher teste)
		Intent it = new Intent(EscModo.this, EscolheTeste.class);
		it.putExtras(wrap);
		startActivity(it);

	}

}
