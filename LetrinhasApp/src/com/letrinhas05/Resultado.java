package com.letrinhas05;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.TesteLeitura;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Classe para apresentar o descritivo do resultado de uma correção
 * 
 * @author Thiago
 * 
 */
public class Resultado extends Activity {
	TextView cab, result;
	LetrinhasDB db = new LetrinhasDB(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultado);

		// buscar os parametros
		Bundle b = getIntent().getExtras();

		// Correcao
		CorrecaoTesteLeitura crt = db.getCorrecaoTesteLeirutaById(b
				.getLong("ID"));

		// Teste para buscar titulo
		TesteLeitura teste = db.getTesteLeituraById(crt.getTestId());
		String titulo = teste.getTitulo() + " - ";
		titulo += "" + getDate(crt.getDataExecucao());

		cab = ((TextView) findViewById(R.id.resCabecalho));
		cab.setText(titulo);

		// agora que temos a correc��o vamos reconstruir o relatorio
		String resultado = "==========Avalia��o============\n"
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
		
		result = ((TextView) findViewById(R.id.tvResultado));
		result.setText(resultado);
		

		((ImageButton) findViewById(R.id.resAvancar))
				.setOnClickListener(new View.OnClickListener() {
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
	 * @author Alexandre
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

}
