package com.letrinhas05.escolhe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import com.letrinhas05.R;
import com.letrinhas05.Resultado;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTeste;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;
import com.letrinhas05.ClassesObjs.CorrecaoTesteMultimedia;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

/**
 * Activity para listar os resultados
 * 
 * @author Thiago
 * 
 */
public class ListaResultados extends Activity {
	Button volt;

	int iDs[];
	String Nomes[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_resultados);

        //////Ocultar barra de cima/////
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Hide the status bar.
        int uiOptionss = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptionss);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        /////////////////////////////////////


		// new line faz a rotacao do ecran em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.lResult);

		// Retirar os Extras
		Bundle b = getIntent().getExtras();
		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		Nomes = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");

		((TextView) findViewById(R.id.lREscola)).setText(Nomes[0]);
		((TextView) findViewById(R.id.lRtvProf)).setText(Nomes[1]);
		((TextView) findViewById(R.id.tvRTurma)).setText(Nomes[3]);
		((TextView) findViewById(R.id.tvRAluno)).setText(Nomes[4]);

		int largura = getResources().getDimensionPixelSize(R.dimen.dim100);
		// se professor tem uma foto, usa-se
		if (Nomes[2] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.lRivProfessor));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Professors/" + Nomes[2];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}

		// se aluno tem uma foto, usa-se
		if (Nomes[5] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.ivRAluno));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Students/" + Nomes[5];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}
		volt = (Button) findViewById(R.id.lRbtnVoltar);
		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		makeLista();

	}

	/**
	 * Procedimento de preenchimeto do painel
	 * 
	 * @author Thiago
	 */
	@SuppressLint("NewApi")
	private void makeLista() {

		LetrinhasDB bd = new LetrinhasDB(this);
		// vou buscar todas as submissoes de teste corrigidas existentes..
		// ... dos alunos selecionado.
		List<CorrecaoTeste> ct = bd.getAllCorrecaoTesteByProfID(iDs[1]);

		// verifico se estas submissoes estao corrigidas e s�o do aluno
		// selecionado
		int cont = 0;
		for (int i = 0; i < ct.size(); i++) {
			// se esta corrigido e pertence ao aluno selecionado, conta-o
			if (ct.get(i).getEstado() == 1
					&& ct.get(i).getIdEstudante() == iDs[3]) {
				cont++;
			}
		}
		// objetos do XML
		LinearLayout ll = (LinearLayout) findViewById(R.id.llListRes);
		Button btOriginal = (Button) findViewById(R.id.btnLRCorrecao_Original);
		// remove o botao original do layerlayout
		ll.removeView(btOriginal);
		// se existirem submissoes
		if (cont != 0) {
			// crio um array de correcoes auxiliar
			CorrecaoTeste ctAux[] = new CorrecaoTeste[cont];
			cont = 0;
			for (int i = 0; i < ct.size(); i++) {
				// se esta corrigido, acrescenta-o
				if (ct.get(i).getEstado() == 1
						&& ct.get(i).getIdEstudante() == iDs[3]) {
					ctAux[cont] = ct.get(i);
					cont++;
				}
			}

			// Agora vou construir os botoes com a informacao necessaria:
			for (int i = 0; i < ctAux.length; i++) {

				// criar o botao
				Button btIn = new Button(this);
				// copiar os parametros de layout
				btIn.setLayoutParams(btOriginal.getLayoutParams());
				// nome do aluno do teste
				Estudante aluno = bd
						.getEstudanteById(ctAux[i].getIdEstudante());
				String title = aluno.getNome() + " - ";
				// titulo do teste
				Teste tst = bd.getTesteById(ctAux[i].getTestId());
				title += tst.getTitulo() + " - ";

				title += "" + getDate(ctAux[i].getDataExecucao());

				// colocar toda a string no botao
				btIn.setText(title);

				// buscar a imagem do tipo
				ImageView imgTip = new ImageView(this);
				switch (ctAux[i].getTipo()) {
				case 0:
					imgTip.setImageResource(R.drawable.textos);// texto
					break;
				case 1:
					imgTip.setImageResource(R.drawable.imags);// multimedia
					break;
				case 2:
					imgTip.setImageResource(R.drawable.palavras);// palavras
					break;
				case 3:
					imgTip.setImageResource(R.drawable.textos);// poema
					break;
				}
				int largura = getResources().getDimensionPixelSize(R.dimen.dim120);
				// colocar a foto do aluno
				if (aluno.getNomefoto() != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Students/" + aluno.getNomefoto();
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imgAl = new ImageView(this);
					// ajustar o tamanho da imagem
					imgAl.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
							largura, false));

					// enviar para o bot�o
					btIn.setCompoundDrawablesWithIntrinsicBounds(
							imgAl.getDrawable(), null, imgTip.getDrawable(),
							null);
				} else {
					// senao copia a imagem do botao original
					btIn.setCompoundDrawables(
							btOriginal.getCompoundDrawablesRelative()[0], null,
							imgTip.getDrawable(), null);
				}

				if (ctAux[i].getTipo() != 1) { // tipo texto / Lista /poema
					final CorrecaoTesteLeitura ctl = bd
							.getCorrecaoTesteLeirutaById(ctAux[i]
									.getIdCorrrecao());

					// Defenir o que faz o botao ao clicar
					btIn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Entrar na activity

							// teste do resultado!
							Bundle wrap = new Bundle();
							wrap.putLong("ID", ctl.getIdCorrrecao());
							// listar submissoes anteriores do mesmo teste
							Intent it = new Intent(getApplicationContext(),
									Resultado.class);
							it.putExtras(wrap);
							startActivity(it);
						}
					});
				} else {// tipo multim�dia
					CorrecaoTesteMultimedia ctm = bd
							.getCorrecaoTesteMultimediaById(ctAux[i]
									.getIdCorrrecao());
					ctm.getCerta();
					ctm.getOpcaoEscolhida();

					if (ctm.getCerta() == ctm.getOpcaoEscolhida()) {
						btIn.setText(btIn.getText() + " - Acertou");
					} else {
						btIn.setText(btIn.getText() + " - Errou");
					}

				}

				ll.addView(btIn);
			}

			// Senao lanca um alerta... de sem submissoes de momento
		} else {

			android.app.AlertDialog alerta;
			// Cria o gerador do AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// define o titulo
			builder.setTitle("Letrinhas");
			// define a mensagem
			builder.setMessage("Nao foram encontradas correcoes no repositorio");
			// define um bot�o como positivo
			builder.setPositiveButton("OK", null);
			// cria o AlertDialog
			alerta = builder.create();
			// Mostra
			alerta.show();

		}

	}

	/**
	 * Funcao importante que transforma um TimeStamp em uma data com hora
	 * 
	 * @param timeStamp
	 *            timestamp a converter
	 * @return retorna uma string
	 * 
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
