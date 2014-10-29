package com.letrinhas05.escolhe;
import android.app.ActionBar;
import com.letrinhas05.R;
import com.letrinhas05.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Classe de apoio à Pagina de escola do modo (menu)
 * 
 * @author Thiago
 */
public class EscModo extends Activity {
    protected Button btnModoAluno, btnLResultados, btnModoProf, btnVoltar;
    protected int idEscola, idProfessor, idTurma, idAluno, iDs[];
    protected String nomeEscola, nomeProfessor, nomeTurma, nomeAluno, Nomes[];
    protected String fotoNomeProf, fotoAluno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.esc_modo);

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


		///////////////////// Retirar os Extras da janela Anterior /////////////////
		Bundle b = getIntent().getExtras();
		// escola
		idEscola = b.getInt("Escola_ID");
        nomeEscola = b.getString("Escola");
		// professor
		idProfessor = b.getInt("Professor_ID");
        nomeProfessor = b.getString("Professor");
        fotoNomeProf = b.getString("fotoProfs");
        //Turma
        nomeTurma = b.getString("Turma");
        idTurma = b.getInt("Turma_ID");
        //Aluno
        nomeAluno = b.getString("Aluno");
        idAluno = b.getInt("Aluno_ID");
        fotoAluno = b.getString("Foto_Aluno");
        ////////////////Aceder a objectos visuais da janela///////////////////
        btnModoAluno = (Button) findViewById(R.id.btModoAluno);
        btnModoProf = (Button) findViewById(R.id.btModoProf);
        btnLResultados = (Button) findViewById(R.id.btVerResult);
        btnVoltar = (Button) findViewById(R.id.escMbtnVoltar);
        ((TextView) findViewById(R.id.escMEscola)).setText(nomeEscola);
		((TextView) findViewById(R.id.tvMProf)).setText(nomeProfessor);
        ((TextView) findViewById(R.id.tvMTurma)).setText(nomeTurma);
        ((TextView) findViewById(R.id.tvMAluno)).setText(nomeAluno);
        final View contentView = findViewById(R.id.escModo);
/////////////////////////////////////////////////////////////////////////////////

		int largura = getResources().getDimensionPixelSize(R.dimen.dim100);
		// se professor tem uma foto, usa-se
		if (fotoNomeProf != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.imgProfEscmODO));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Professors/" + fotoNomeProf;
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}
		// se o aluno tem uma foto, usa-se
		if (fotoAluno != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.ivMAluno));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Students/" + fotoAluno;
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}

		// new line faz a rotação do ecrãn em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

        // juntar tudo num array, para simplificar o código
        // String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
        Nomes = new String[6];
        Nomes[0] = nomeEscola;
        Nomes[1] = nomeProfessor;
        Nomes[2] = fotoNomeProf;
        Nomes[3] = nomeTurma;
        Nomes[4] = nomeAluno;
        Nomes[5] = fotoAluno;

        // int's - idEscola, idProfessor, idTurma, idAluno
        iDs = new int[4];
        iDs[0] = idEscola;
        iDs[1] = idProfessor;
        iDs[2] = idTurma;
        iDs[3] = idAluno;

		escutaBotoes();
	}

	/**
	 * Método para defenir o Touchlistener de cada botão
	 * 
	 * @author Thiago
	 */
	private void escutaBotoes() {
        btnModoAluno.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				modAluno();
			}
		});
        btnModoProf.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				modProf();
			}
		});
        btnVoltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
        btnLResultados.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				modResult();
			}
		});
	}

	/** 
	 * selecionar o modo aluno - executar os testes
	 * @author Thiago 
	 */
	public void modAluno() {
		btnModoAluno.setTextColor(Color.GREEN);
		btnModoProf.setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
		btnLResultados.setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));		
	////////////////enviar os parametros necesserios para a proxima janela ////////////
		Bundle wrap = new Bundle();
		wrap.putStringArray("Nomes", Nomes);
		wrap.putIntArray("IDs", iDs);
		// iniciar a pagina 2 (escolher Dsiciplina)
		Intent it = new Intent(EscModo.this, EscolheDisciplina.class);
		it.putExtras(wrap);
		startActivity(it);
	}

	/**
	 * Selecionar o modo professor corrigir - testes
	 * @author Thiago
	 */
	public void modProf() {
		btnModoProf.setTextColor(Color.GREEN);
		btnModoAluno.setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
		btnLResultados.setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
		
////////////////enviar os parametros necesserios para a proxima janela ////////////
		Bundle wrap = new Bundle();
		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		wrap.putStringArray("Nomes", Nomes);
		// int's - idEscola, idProfessor, idTurma, idAluno
		wrap.putIntArray("IDs", iDs);
		// iniciar a pagina (escolher testes a corrigir)
		Intent it = new Intent(EscModo.this, ListarSubmissoes.class);
		it.putExtras(wrap);
		startActivity(it);
	}

	/**
	 * Selecionar o modo de listar os resultados das correções
	 * @author Thiago
	 */
	public void modResult() {
		btnLResultados.setTextColor(Color.GREEN);
		btnModoAluno.setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
		btnModoProf.setTextColor(Color.rgb(0x5d,
				0xdf, 0xff));
////////////////enviar os parametros necesserios para a proxima janela ////////////
		Bundle wrap = new Bundle();
		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		wrap.putStringArray("Nomes", Nomes);
		// int's - idEscola, idProfessor, idTurma, idAluno
		wrap.putIntArray("IDs", iDs);
		// iniciar a pagina (escolher testes a corrigir)
		Intent it = new Intent(EscModo.this, ListaResultados.class);
		it.putExtras(wrap);
		startActivity(it);
	}
}
