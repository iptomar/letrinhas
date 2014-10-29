package com.letrinhas05.escolhe;

import java.util.List;

import android.app.ActionBar;
import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Turma;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

/**
 * Classe de apoio à Pagina de escolha da turma
 * 
 * @author Thiago
 */
public class EscolheTurma extends Activity {
	protected Button btnVoltar;
	protected String nomeEscola, nomeProfessor, fotoProfNome;
	protected int idEscola, idProfessor, nTurmas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolhe_turma);

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

		// //////////// Retirar os Extras da janela
		// anterior/////////////////////
		Bundle b = getIntent().getExtras();
		// escola/////////
		idEscola = b.getInt("Escola_ID");
		nomeEscola = b.getString("Escola");
		// professor/////
		idProfessor = b.getInt("Professor_ID");
		nomeProfessor = b.getString("Professor");
		fotoProfNome = b.getString("foto_Professor");

		// //////////////////Aceder a objectos visuais////////////////////
		btnVoltar = (Button) findViewById(R.id.btnVoltarTurm);
		((TextView) findViewById(R.id.tvTProf)).setText(nomeProfessor);
		((TextView) findViewById(R.id.escTEscola)).setText(nomeEscola);
		ImageView imageView = ((ImageView) findViewById(R.id.ivTProfessor));
		final View contentView = findViewById(R.id.escTurma);
		// ///////////////////////////////////////////////////////

		int largura = getResources().getDimensionPixelSize(R.dimen.dim100);
		if (fotoProfNome != null) {
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/School-Data/Professors/"
					+ fotoProfNome;
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}
		// new line faz a rota��o do ecr�n em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// Botao de voltar
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle wrap = new Bundle();
				wrap.putString("Escola", nomeEscola);
				wrap.putInt("Escola_ID", idEscola);
				Intent it = new Intent(getApplicationContext(),
						EscolheProfessor.class);
				it.putExtras(wrap);
				startActivity(it);
				finish();
			}
		});
		makeTabela();
	}

	/**
	 * Novo m�todo para criar o painel dinâmico para os botões de
	 * seleção da turma
	 * 
	 * @author Thiago
	 */
	@SuppressLint("NewApi")
	private void makeTabela() {

		// Cria o objecto da base de dados
		LetrinhasDB db = new LetrinhasDB(this);
		// ************* Mudar este select de all para por ID de professor
		List<Turma> turmas = db.getAllTurmasByProfid(idProfessor);
		// *******************************************************************************
		nTurmas = turmas.size();
		int[] idTurmas = new int[turmas.size()];
		String nomeTurma[] = new String[turmas.size()];
		int anoEscolarTurmas[] = new int[turmas.size()];
		// preenche os arrays com a informação necessária
		for (int i = 0; i < nTurmas; i++) {
			idTurmas[i] = turmas.get(i).getId();
			nomeTurma[i] = turmas.get(i).getNome();
			anoEscolarTurmas[i] = turmas.get(i).getAnoEscolar();
		}
		/**
		 * Scroll view com uma tabela de 4 colunas(max)
		 */
		// tabela a editar
		TableLayout tabela = (TableLayout) findViewById(R.id.tblEscolheTurm);
		// linha da tabela a editar
		TableRow linha = (TableRow) findViewById(R.id.Turmlinha01);
		// 1º botão
		Button bt = (Button) findViewById(R.id.TurmBtOriginal);
		bt.setText("teste turmas");

		// Contador de controlo
		int cont = 0;
		// criar o nº de linhas a dividir por 4 colunas
		for (int i = 0; i < nTurmas / 4; i++) {
			// nova linha da tabela
			TableRow linha1 = new TableRow(getBaseContext());
			// Copiar os parametros da 1ª linha
			linha1.setLayoutParams(linha.getLayoutParams());
			// criar os 4 bot�es da linha
			for (int j = 0; j < 4; j++) {
				// **********************************
				// Nome da turma

				final int idturm = idTurmas[cont];
				// ***********************************
				// novo bot�o
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do botão original
				bt1.setLayoutParams(bt.getLayoutParams());
				// copia a imagem do botão original
				bt1.setCompoundDrawables(null,
						bt.getCompoundDrawablesRelative()[1], null, null);

				final String aux = anoEscolarTurmas[cont] + "º - "
						+ nomeTurma[cont];
				// addicionar o nome
				bt1.setText(aux);
				// Defenir o que faz o botão ao clicar
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						// //////////////CAMPOS PARA A PROXIMA
						// JANELA///////////////////
						Bundle wrap = new Bundle();
						wrap.putString("Escola", nomeEscola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", nomeProfessor);
						wrap.putInt("Professor_ID", idProfessor);
						wrap.putString("foto_Professor", fotoProfNome);
						wrap.putString("Turma", aux);
						wrap.putInt("turma_ID", idturm);
						Intent it = new Intent(getApplicationContext(),
								EscolheAluno.class);
						it.putExtras(wrap);
						startActivity(it);
					}
				});
				// inserir o botão na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1);
		}

		// resto
		if (nTurmas % 4 != 0) {
			TableRow linha1 = new TableRow(getBaseContext());
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < nTurmas % 4; j++) {
				final int idturm = idTurmas[cont];
				// ***********************************
				// novo botao
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do botão original
				bt1.setLayoutParams(bt.getLayoutParams());

				// copia a imagem do bot�o original
				bt1.setCompoundDrawables(null,
						bt.getCompoundDrawablesRelative()[1], null, null);

				// addicionar o nome
				final String aux = anoEscolarTurmas[cont] + "º - "
						+ nomeTurma[cont];
				bt1.setText(aux);
				// Defenir o que faz o botão ao clicar
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						// //////////////CAMPOS PARA A PROXIMA
						// JANELA///////////////////
						Bundle wrap = new Bundle();
						wrap.putString("Escola", nomeEscola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", nomeProfessor);
						wrap.putInt("Professor_ID", idProfessor);
						wrap.putString("foto_Professor", fotoProfNome);
						wrap.putString("Turma", aux);
						wrap.putInt("turma_ID", idturm);
						Intent it = new Intent(getApplicationContext(),
								EscolheAluno.class);
						it.putExtras(wrap);
						startActivity(it);
					}
				});
				// inserir o botão na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1);
		}
		// por fim elimino a 1ª linha
		tabela.removeView(linha);
	}

}
