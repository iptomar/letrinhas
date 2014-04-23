package com.letrinhas03;

import com.letrinhas03.util.SystemUiHider;
import com.letrinhas03.util.Teste;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Teste_Texto extends Activity {
	// flags para verificar os diversos estados do teste
	boolean modo, gravado;
	// objetos
	ImageButton record, play, voltar, cancelar, avancar;
	TextView vcl, frg, slb, rpt;
	// variaveis contadoras para a avaliação
	int plvErradas, vacil, fragment, silabs, repeti;
	String endereco;
	Teste[] lista;

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
		setContentView(R.layout.teste_texto);

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.testTexto);

		// buscar os parametros
		Bundle b = getIntent().getExtras();
		
		//Compor novamnete e lista de testes
		int lstID[] = b.getIntArray("ListaID");
		int[] lstTipo = b.getIntArray("ListaTipo");
		String[] lstTitulo = b.getStringArray("ListaTitulo");
		
		//
		lista = new Teste[lstID.length];
		for (int i = 0; i < lstTitulo.length; i++) {
			lista[i] = new Teste(lstID[i], lstTipo[i], lstTitulo[i]);
		}

		modo = b.getBoolean("Modo");

		// Consultar a BD para preencher o conteúdo....
		((TextView) findViewById(R.id.textCabecalho)).setText(lista[0]
				.getTitulo());
		((TextView) findViewById(R.id.textRodape))
				.setText(b.getString("Aluno"));

		endereco = "/" + b.getString("Professor") + "/" + b.getString("Aluno")
				+ "/" + lista[0].getTitulo() + ".3gpp";

		// descontar este teste da lista.
		Teste[] aux = new Teste[lista.length - 1];
		for (int i = 1; i < lista.length; i++) {
			aux[i - 1] = lista[i];
		}
		lista = aux;

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();

		if (modo) {// está em modo professor
			setCorreccao();
		} else { // está em modo aluno
			((TableLayout) findViewById(R.id.txtControlo))
					.setVisibility(View.INVISIBLE);

		}

		record = (ImageButton) findViewById(R.id.txtRecord);
		play = (ImageButton) findViewById(R.id.txtPlay);
		play.setVisibility(View.INVISIBLE);
		voltar = (ImageButton) findViewById(R.id.txtVoltar);
		cancelar = (ImageButton) findViewById(R.id.txtCancel);
		avancar = (ImageButton) findViewById(R.id.txtAvaliar);

		escutaBotoes();
	}

	private void escutaBotoes() {
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startGrava();
			}

		});

		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startPlay();
			}

		});

		cancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// voltar para pag inicial
				finaliza();
			}
		});

		avancar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// voltar para pag inicial
				startAvalia();
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

	private void startGrava() {
		// TODO Auto-generated method stub

	}

	private void startPlay() {
		// TODO Auto-generated method stub

	}

	private void startAvalia() {
		if (modo) {//iniciar a avaliação

		}

		finaliza();

	}

	/**
	 * Procedimento para ativar a selecção das palavras erradas no texto
	 */
	private void setCorreccao() {
		// Painel de controlo:
		ImageButton v1, v2, f1, f2, s1, s2, r1, r2;

		v1 = (ImageButton) findViewById(R.id.txtVacilMen);
		v2 = (ImageButton) findViewById(R.id.txtVacilMais);
		f1 = (ImageButton) findViewById(R.id.txtFragMen);
		f2 = (ImageButton) findViewById(R.id.txtFragMais);
		s1 = (ImageButton) findViewById(R.id.txtSilbMen);
		s2 = (ImageButton) findViewById(R.id.txtSilbMais);
		r1 = (ImageButton) findViewById(R.id.txtRepMen);
		r2 = (ImageButton) findViewById(R.id.txtRepMais);

		vcl = (TextView) findViewById(R.id.textView1);
		frg = (TextView) findViewById(R.id.TextView02);
		slb = (TextView) findViewById(R.id.TextView03);
		rpt = (TextView) findViewById(R.id.TextView06);

		vcl.setText("" + vacil);
		frg.setText("" + fragment);
		slb.setText("" + silabs);
		rpt.setText("" + repeti);

		// ativar os controlos
		v1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (vacil != 0)
					vacil--;
				vcl.setText("" + vacil);
			}
		});
		v2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				vacil++;
				vcl.setText("" + vacil);
			}
		});
		f1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (fragment != 0)
					fragment--;
				frg.setText("" + fragment);
			}
		});
		f2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fragment++;
				frg.setText("" + fragment);
			}
		});
		s1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (silabs != 0)
					silabs--;
				slb.setText("" + silabs);
			}
		});
		s2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				silabs++;
				slb.setText("" + silabs);
			}
		});
		r1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (repeti != 0)
					repeti--;
				rpt.setText("" + repeti);
			}
		});
		r2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				repeti++;
				rpt.setText("" + repeti);
			}
		});

	}

	private void finaliza() {
		if (lista.length != 0) {
			//Decompor o array de teste, para poder enviar por parametros
			int[] lstID = new int[lista.length];
			int[] lstTipo = new int[lista.length];
			String[] lstTitulo = new String[lista.length];
			for (int i = 0; i < lista.length; i++) {
				lstID[i] = lista[i].getID();
				lstTipo[i] = lista[i].getTipo();
				lstTitulo[i] = lista[i].getTitulo();
			}

			switch (lista[0].getTipo()) {
			case 0:
				
				// lançar a nova activity do tipo texto,
				// enviar o parametro de modo
				Bundle wrap = new Bundle();
				wrap.putBoolean("Modo", modo);

				// teste, a depender das informações da BD
				// ****************************
				wrap.putString("Aluno", "EI3C-Tiago Fernandes");
				wrap.putString("Professor", "ESTT-Antonio Manso");

				wrap.putIntArray("ListaID", lstID);
				wrap.putIntArray("ListaTipo", lstTipo);
				wrap.putStringArray("ListaTitulo", lstTitulo);

				// iniciar a pagina 2 (escolher teste)
				Intent it = new Intent(getApplicationContext(),
						Teste_Texto.class);
				it.putExtras(wrap);

				startActivity(it);

				// while(isRunning);

				// Toast.makeText(null, "TEste acabou!!!",
				// Toast.LENGTH_LONG).show();

				break;
			case 1:

				Toast.makeText(getApplicationContext(), "" + 1 + " - Palavras",
						Toast.LENGTH_SHORT).show();

				// lançar a nova activity do tipo Palavras, e o seu conteúdo
				//
				// Intent it = new
				// Intent(act.getApplicationContext(),texto.class);
				// act.startActivity(it);

				// esperar que esta termine
				// while (!act.isDestroyed());

				break;
			case 2:
				Toast.makeText(getApplicationContext(), 2 + " - Poemas",
						Toast.LENGTH_SHORT).show();
				// lançar a nova activity do tipo Poema, e o seu conteúdo
				//
				//

				break;
			case 3:
				Toast.makeText(getApplicationContext(), 3 + " - Imagens",
						Toast.LENGTH_SHORT).show();
				// lançar a nova activity do tipo imagem, e o seu conteúdo
				//
				//

				break;
			default:
				Toast.makeText(getApplicationContext(), " - Tipo não defenido",
						Toast.LENGTH_SHORT).show();
				// não lançar nada e continuar

				break;
			}

		}
		finish();

	}

	/**
	 * Procedimento para voltar a esconder o titulo caso este esteja activo
	 * 
	 * @author Thiago
	 */
	@Override
	public boolean hasWindowFocus() {
		// esconder o title
		final View contentView = findViewById(R.id.testTexto);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.hide();
		return true;
	}

}
