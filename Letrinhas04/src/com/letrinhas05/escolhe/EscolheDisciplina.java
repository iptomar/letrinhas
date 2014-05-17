package com.letrinhas05.escolhe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.letrinhas05.R;
import com.letrinhas05.ClassesObjs.Escola;
import com.letrinhas05.util.Custom;
import com.letrinhas05.util.SystemUiHider;

public class EscolheDisciplina extends Activity {

	Button volt, pt, mat, estMeio, ingl;
	String strings[];
	int[] iDs;

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
		setContentView(R.layout.escolhe_disciplina);

		// Retirar os Extras
		Bundle b = getIntent().getExtras();

		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		strings = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");

		// preencher informação na activity
		((TextView) findViewById(R.id.escDEscola)).setText(strings[0]);
		((TextView) findViewById(R.id.tvDProf)).setText(strings[1]);
		// se professor tem uma foto, usa-se
		if (strings[2] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.ivDProfessor));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/School-Data/Professors/"
					+ strings[2];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100,
					100, false));
		}
		((TextView) findViewById(R.id.tvDTurma)).setText(strings[3]);
		((TextView) findViewById(R.id.tvDAluno)).setText(strings[4]);
		// se professor tem uma foto, usa-se
		if (strings[5] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.ivDAluno));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Students/" + strings[5];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100,
					100, false));
		}

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escDisciplina);

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

		volt = (Button) findViewById(R.id.btnDVoltar);
		pt = (Button) findViewById(R.id.pt);
		mat = (Button) findViewById(R.id.mat);
		estMeio = (Button) findViewById(R.id.estMeio);
		ingl = (Button) findViewById(R.id.english);

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

	/**
	 * Procedimento para veirficar os botões
	 * 
	 * @author Thiago
	 */
	private void escutaBotoes() {

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				finish();
			}
		});

		pt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necessários
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 0);
				wrap.putString("Disciplina", "Português");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(EscolheDisciplina.this, EscolheTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
				finish();
			}
		});
		
		mat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necessários
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 1);
				wrap.putString("Disciplina", "Matemática");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(EscolheDisciplina.this, EscolheTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
				finish();
			}
		});

		estMeio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necessários
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 2);
				wrap.putString("Disciplina", "Estudo do Meio");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(EscolheDisciplina.this, EscolheTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
				finish();
			}
		});

		ingl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necessários
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 3);
				wrap.putString("Disciplina", "English");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(EscolheDisciplina.this, EscolheTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
				finish();
			}
		});


	}

}
