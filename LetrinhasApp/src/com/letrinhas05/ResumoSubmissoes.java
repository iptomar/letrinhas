package com.letrinhas05;

import java.util.List;

import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.Teste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Listar o hitorico de submissoes do teste executado do aluno atual
 * 
 * @author Thiago
 * 
 */
public class ResumoSubmissoes extends Activity {

	protected static final int PARADO = 0;
	int testeID, alunoID;
	Button continuar;
	boolean playing;
	MediaPlayer reprodutor;
	private Handler play_handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resumo_submissoes);

		// buscar os parametros */
		Bundle b = getIntent().getExtras();
		inicia(b);

		Button btn = (Button) findViewById(R.id.resBtnParar);
		btn.setVisibility(View.INVISIBLE);

	}

	/**
	 * metodo para iniciar os componetes, que dependem do conteudo passado por
	 * parametros (extras)
	 * 
	 * @param b
	 *            Bundle, contem informacao da activity anterior
	 */
	@SuppressLint("NewApi")
	public void inicia(Bundle b) {
		//
		testeID = b.getInt("IDTeste");
		alunoID = b.getInt("IDAluno");

		/** Consultar a BD para preencher o conteudo.... */
		LetrinhasDB bd = new LetrinhasDB(this);
		Teste teste = bd.getTesteById(testeID);

		List<CorrecaoTesteLeitura> crt = bd
				.getAllCorrecaoTesteLeitura_ByIDaluno_TestID(alunoID, testeID);

		// Painel Dinamico
		// objetos do XML
		LinearLayout ll = (LinearLayout) findViewById(R.id.llResumo);
		Button btOriginal = (Button) findViewById(R.id.rsBtnOriginal);
		// remove o botao original do layerlayout
		ll.removeView(btOriginal);

		// Contruir os botoes
		for (int i = 0; i < crt.size(); i++) {
			// criar o botao
			Button btIn = new Button(this);
			// copiar os parametros de layout
			btIn.setLayoutParams(btOriginal.getLayoutParams());
			// copiar a imagem do botao original
			btIn.setCompoundDrawablesWithIntrinsicBounds(null, null,
					btOriginal.getCompoundDrawablesRelative()[2], null);
			btIn.setText("" + crt.get(i).getDataExecucao());// crtAux[i].getDataExecucao());
			final String audioUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + crt.get(i).getAudiourl();

			// o que o botao vai fazer...
			btIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					play(audioUrl);
				}

			});

			ll.addView(btIn);
		}

		TextView txt = ((TextView) findViewById(R.id.rsTituloTeste));
		txt.setText(teste.getTitulo());

		Button stop = (Button)findViewById(R.id.resBtnParar);
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				play(null);
			}
		});
		
		
		continuar = (Button) findViewById(R.id.rsAvancar);
		continuar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private void play(String audioUrl) {
		if (!playing) {
			ScrollView sv = (ScrollView) findViewById(R.id.svResumo);
			sv.setVisibility(View.INVISIBLE);
			Button btn = (Button) findViewById(R.id.resBtnParar);
			btn.setVisibility(View.VISIBLE);
			continuar.setVisibility(View.INVISIBLE);
			
			final ScrollView llF = sv;
			;
			final Button btnF = btn;

			final Button btt = continuar;
			try {

				reprodutor = new MediaPlayer();
				reprodutor.setDataSource(audioUrl);
				reprodutor.prepare();
				reprodutor.start();
				Toast.makeText(getApplicationContext(), "A reproduzir.",
						Toast.LENGTH_SHORT).show();

				// espetar aqui uma thread, para quando isto pare
				// habilitar novamente a vista

				play_handler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case PARADO:
							llF.setVisibility(View.VISIBLE);
							btnF.setVisibility(View.INVISIBLE);
							btt.setVisibility(View.VISIBLE);
							try {
								reprodutor.stop();
								reprodutor.release();
								Toast.makeText(getApplicationContext(),
										"Fim da reproducao.",
										Toast.LENGTH_SHORT).show();
								playing=false;
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

				playing = true;
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro na reproducao.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			ScrollView sv = (ScrollView) findViewById(R.id.svResumo);
			sv.setVisibility(View.VISIBLE);
			Button btn = (Button) findViewById(R.id.resBtnParar);
			btn.setVisibility(View.INVISIBLE);
			continuar.setVisibility(View.VISIBLE);

			playing = false;
			try {
				reprodutor.stop();
				reprodutor.release();
				Toast.makeText(getApplicationContext(),
						"Reprodução interrompida.", Toast.LENGTH_SHORT).show();
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(),
						"Erro na reprodução.\n" + ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
