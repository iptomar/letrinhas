package com.letrinhas05;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.TesteLeitura;

public class RelatasCorrection extends Activity {

	LetrinhasDB db = new LetrinhasDB(this);
	TextView cab1, cab2, result1, result2;
	Button next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_relatas_correction);

		long id1, id2;
		Bundle b = getIntent().getExtras();
		id1 = b.getLong("ID1");
		id2 = b.getLong("ID2");

		cab1 = (TextView) findViewById(R.id.resCabecalho1);
		cab2 = (TextView) findViewById(R.id.resCabecalho2);
		result1 = (TextView) findViewById(R.id.tvResultado1);
		result2 = (TextView) findViewById(R.id.tvResultado2);


		//1a Correcao
		CorrecaoTesteLeitura crt = db.getCorrecaoTesteLeirutaById(id1);

		// Teste para buscar titulo
		TesteLeitura teste = db.getTesteLeituraById(crt.getTestId());
		String titulo = teste.getTitulo() + " - ";
		titulo += "" + getDate(crt.getDataExecucao());
		cab1.setText(titulo);

		// agora que temos a correcção vamos reconstruir o 1o relatorio
		String resultado = "==========Avaliação============\n"
				+ crt.getObservacoes() + "\n";
		resultado += "Palavras lidas por minuto (plm): "
				+ crt.getNumPalavrasMin() + "\n";
		resultado += "Palavras corretamente lidas (pcl): "
				+ crt.getNumPalavCorretas() + "\n";
		resultado += "Precisão de Leitura (PL): " + crt.getPrecisao() + "\n";
		resultado += "Velocidade de leitura (VL): " + crt.getVelocidade()
				+ "\n";
		resultado += "Expressividade: " + crt.getExpressividade() + "\n";
		resultado += "Ritmo: " + crt.getRitmo() + "\n\n";
		resultado += "===============================\n" + "Detalhes\n"
				+ "===============================\n" + crt.getDetalhes();
		result1.setText(resultado);

		
		//2a Correcao
		crt = db.getCorrecaoTesteLeirutaById(id2);

		// Teste para buscar titulo
		teste = db.getTesteLeituraById(crt.getTestId());
		titulo = teste.getTitulo() + " - ";
		titulo += "" + getDate(crt.getDataExecucao());
		cab2.setText(titulo);

		// agora que temos a correcção vamos reconstruir o 2o relatorio
		resultado = "==========Avaliação============\n"
				+ crt.getObservacoes() + "\n";
		resultado += "Palavras lidas por minuto (plm): "
				+ crt.getNumPalavrasMin() + "\n";
		resultado += "Palavras corretamente lidas (pcl): "
				+ crt.getNumPalavCorretas() + "\n";
		resultado += "Precisão de Leitura (PL): " + crt.getPrecisao() + "\n";
		resultado += "Velocidade de leitura (VL): " + crt.getVelocidade()
				+ "\n";
		resultado += "Expressividade: " + crt.getExpressividade() + "\n";
		resultado += "Ritmo: " + crt.getRitmo() + "\n\n";
		resultado += "===============================\n" + "Detalhes\n"
				+ "===============================\n" + crt.getDetalhes();
		
		result2.setText(resultado);

		

		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

	}

	/**
	 * Funcao importante que transforma um TimeStamp em uma data com hora
	 * 
	 * @param timeStamp
	 *            timestamp a converter
	 * @return retorna uma string
	 */
	@SuppressLint("SimpleDateFormat")
	private String getDate(long timeStamp) {
		try {
			long timeStampCorrigido = timeStamp * 1000;
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date netDate = (new Date(timeStampCorrigido));
			return sdf.format(netDate);
		} catch (Exception ex) {
			return "0";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.relatas_correction, menu);
		return true;
	}

}
