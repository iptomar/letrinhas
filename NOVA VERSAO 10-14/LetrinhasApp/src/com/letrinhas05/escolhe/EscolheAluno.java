package com.letrinhas05.escolhe;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Estudante;
import com.letrinhas05.util.SystemUiHider;

/**
 * Classe de apoio para a escolha do aluno
 * 
 * @author Thiago
 *
 */
public class EscolheAluno extends Activity {

    protected Button btnVoltar;
    protected  int  idEscola, idProfessor, idTurma;
    protected  String nomeEscola, nomeProfessor, fotoNomeProf, nomeTurma;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_escolhe_aluno);

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

		//////////// Retirar os Extras da janela anterior ////////////////////////
		Bundle b = getIntent().getExtras();
		// escola
		idEscola = b.getInt("Escola_ID");
        nomeEscola = b.getString("Escola");
		// professor
		idProfessor = b.getInt("Professor_ID");
        nomeProfessor = b.getString("Professor");
        fotoNomeProf = b.getString("foto_Professor");
        //Turma
        idTurma = b.getInt("turma_ID");
        nomeTurma = b.getString("Turma");

//////////////////////////////Aceder a objectos Visuais da janela ///////////////////////
        btnVoltar = (Button) findViewById(R.id.escAlbtnVoltar);
        ((TextView) findViewById(R.id.escAlEscola)).setText(nomeEscola);
		((TextView) findViewById(R.id.tvAlProf)).setText(nomeProfessor);
        ImageView imageView = ((ImageView) findViewById(R.id.imgProfEscolhAluno));
        ((TextView) findViewById(R.id.escAlTurma)).setText(nomeTurma);
        final View contentView = findViewById(R.id.escAluno);
   ////////////////////////////////////////////////////////////////////////////////////////////v
        int largura = getResources().getDimensionPixelSize(R.dimen.dim100);
        if (fotoNomeProf != null) {
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Professors/" + fotoNomeProf;
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
					largura, largura, false));
		}
		// new line faz a rotacao do ecran em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
           ////////////BOTAO VOLTAR
        btnVoltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da aplica��o
				finish();
			}
		});

		makeTabela();
	}

	/**
	 * Novo método para criar o painel dinâmico para os botões de seleção da
	 * turma
	 * 
	 * @author Thiago
	 */
	@SuppressLint("NewApi")
	private void makeTabela() {

		// Cria o objecto da base de dados
		LetrinhasDB db = new LetrinhasDB(this);
		// ************* Todos os alunos desta turma
		List<Estudante> alunos = db.getAllStudentsByTurmaId(idTurma);
		// *******************************************************************************
		int nAlunos = alunos.size();
		int[] idAluno = new int[nAlunos];
		String nomeAluno[] = new String[nAlunos];
		String fotoAluno[] = new String[nAlunos];
		
		// preenche os arrays so com a informacao necessaria
		for (int i = 0; i < nAlunos; i++) {
			idAluno[i] = alunos.get(i).getIdEstudante();
			nomeAluno[i] = alunos.get(i).getNome();
			fotoAluno[i]= alunos.get(i).getNomefoto();
		}

		/**
		 * Scroll view com uma tabela de 4 colunas(max)
		 */
		// tabela a editar
		TableLayout tabela = (TableLayout) findViewById(R.id.tblEscolheAl);
		// linha da tabela a editar
		TableRow linha = (TableRow) findViewById(R.id.escAllinha01);
		// 1� bot�o
		Button bt = (Button) findViewById(R.id.AlBtOriginal);
		bt.setText("teste alunos");
		// Contador de controlo
		int cont = 0;
		int largura = getResources().getDimensionPixelSize(R.dimen.dim210);
		int comprimento = getResources().getDimensionPixelSize(R.dimen.dim200);
		// criar o n� de linhas a dividir por 4 colunas
		for (int i = 0; i < nAlunos / 4; i++) {
			// nova linha da tabela
			TableRow linha1 = new TableRow(getBaseContext());
			// Copiar os parametros da 1� linha
			linha1.setLayoutParams(linha.getLayoutParams());
			// criar os 4 bot�es da linha
			for (int j = 0; j < 4; j++) {
				// **********************************
				// Nome, id e foto do aluno

				final String alumni = nomeAluno[cont];
				final int idAL = idAluno[cont];
				final String alunFot = fotoAluno[cont];
				// ***********************************

				// novo bot�o
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do bot�o original
				bt1.setLayoutParams(bt.getLayoutParams());
				
				// se o aluno tiver foto, vou busca-la
				if (fotoAluno[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Students/" + fotoAluno[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					// ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
							largura, comprimento, false));
					// enviar para o bot�o
					bt1.setCompoundDrawablesWithIntrinsicBounds(null,
							imageView.getDrawable(), null, null);
				} else {
					// sen�o copia a imagem do bot�o original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}

				// addicionar o nome
				bt1.setText(nomeAluno[cont]);
				// Defenir o que faz o bot�o ao clicar
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
                     ////////////////CAMPOS PARA A PROXIMA JANELA///////////////////
						Bundle wrap = new Bundle();
						wrap.putString("Escola", nomeEscola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", nomeProfessor);
						wrap.putInt("Professor_ID", idProfessor);
						wrap.putString("Foto_professor", fotoNomeProf);
						wrap.putString("Turma",nomeTurma);
						wrap.putInt("Turma_ID", idTurma);
						wrap.putString("Aluno",alumni);
						wrap.putInt("Aluno_ID", idAL);
						wrap.putString("Foto_Aluno", alunFot);
						Intent it = new Intent(getApplicationContext(),EscModo.class);
						it.putExtras(wrap);
						startActivity(it);
					}
				});
				// inserir o bot�o na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1);
		}
		// resto
		if (nAlunos % 4 != 0) {
			TableRow linha1 = new TableRow(getBaseContext());
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < nAlunos % 4; j++) {
				// **********************************
				// Nome, id e foto do aluno
				final String alumni = nomeAluno[cont];
				final int idAL = idAluno[cont];
				final String alunFot = fotoAluno[cont];
				// ***********************************
				// novo bot�o
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do bot�o original
				bt1.setLayoutParams(bt.getLayoutParams());

				// se o aluno tiver foto, vou busca-la
				if (fotoAluno[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Students/" + fotoAluno[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					// ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
							largura, comprimento, false));
					// enviar para o bot�o
					bt1.setCompoundDrawablesWithIntrinsicBounds(null,
							imageView.getDrawable(), null, null);
				} else {
					// sen�o copia a imagem do bot�o original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}

				// addicionar o nome
				bt1.setText(nomeAluno[cont]);
				// Defenir o que faz o bot�o ao clicar
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
                        ////////////////CAMPOS PARA A PROXIMA JANELA///////////////////
						Bundle wrap = new Bundle();
						wrap.putString("Escola", nomeEscola);
						wrap.putInt("Escola_ID", idEscola);
						wrap.putString("Professor", nomeProfessor);
						wrap.putInt("Professor_ID", idProfessor);
						wrap.putString("fotoProfs", fotoNomeProf);
						wrap.putString("Turma",nomeTurma);
						wrap.putInt("Turma_ID", idTurma);
						wrap.putString("Aluno",alumni);
						wrap.putInt("Aluno_ID", idAL);
						wrap.putString("Foto_Aluno", alunFot);
						Intent it = new Intent(getApplicationContext(),EscModo.class);
						it.putExtras(wrap);
						startActivity(it);
					}
				});
				// inserir o bot�o na linha
				linha1.addView(bt1);
				// incrementar o contador de controlo
				cont++;
			}
			// inserir a linha criada
			tabela.addView(linha1);
		}
		// por fim escondo a 1� linha
		tabela.removeView(linha);
	}

}
