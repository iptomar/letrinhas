package com.letrinhas04.escolhe;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.letrinhas04.R;
import com.letrinhas04.BaseDados.LetrinhasDB;
import com.letrinhas04.ClassesObjs.Escola;
import com.letrinhas04.util.Custom;
import com.letrinhas04.util.SystemUiHider;

public class EscolheEscola extends Activity {

	ImageButton volt, exect;
	public int nEscolas,numero=0; 
	List<Escola> escolas;
	LetrinhasDB db;
	int[] id;
	String[] morada;
	String[] img;
	String[] nome;
	byte[] logotipo;
	ListView list;
	Integer[] image;

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
		setContentView(R.layout.activity_escolhe_escola);
		
		
		
		//Cria o objecto da base de dados
		db = new LetrinhasDB(this);
		escolas = db.getAllSchools();
		morada = new String[escolas.size()];
		nome = new String[escolas.size()];
		img = new String[escolas.size()];
		id = new int[escolas.size()];
		nEscolas = escolas.size();
		for (Escola cn : escolas) {
				String storage = cn.getMorada()+","+cn.getIdEscola()+","+cn.getNome()+","+cn.getLogotipoNome();
	            Log.d("letrinhas-Escola", storage.toString());
	            morada[numero] = cn.getMorada();
	            Log.d("letrinhas-Tipo",String.valueOf(morada[0]));
	            nome[numero] = cn.getNome();
	            Log.d("letrinhas-Titulo", nome[0].toString());
	            id[numero] = cn.getIdEscola();
	            Log.d("letrinhas-ID", String.valueOf(id[0]));
	            img[numero] = cn.getLogotipoNome();
	            Log.d("letrinhas-IMG", img[0]);
	            setUp(nome, img, id);
	            numero++;
	     }

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escEscola);

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

	

		volt = (ImageButton) findViewById(R.id.escTVoltar_escl);
		exect = (ImageButton) findViewById(R.id.ibComecar_escl);

		escutaBotoes();
	}
	
	public void setUp(final String[] nome, String[] imgNome, final int[] id){
		Custom adapter = new Custom(EscolheEscola.this, nome, imgNome,"escola");
		list=(ListView)findViewById(R.id.list);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,int position, long idd) {
		                Bundle wrap = new Bundle();
		    			wrap.putInt("IdEscola", id[position]);
		    			Intent it = new Intent(getApplicationContext(),EscolheProfessor.class);
						it.putExtras(wrap);
						finish();
						startActivity(it);
		          }
		 });
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
		exect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//executarTestes();
				Toast.makeText(getApplicationContext(), " - Tipo não defenido",
				Toast.LENGTH_SHORT).show();
			}
		});

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da aplicação
				finish();
			}
		});
	}

}
