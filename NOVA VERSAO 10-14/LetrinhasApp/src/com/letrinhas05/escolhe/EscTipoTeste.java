package com.letrinhas05.escolhe;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Professor;
import com.letrinhas05.PaginaInicial;
import com.letrinhas05.R;
import com.letrinhas05.util.Autenticacao;
import com.letrinhas05.util.SystemUiHider;

/**
 * Created by Alex on 17/05/2014.
 */
public class EscTipoTeste extends Activity {

	protected Button btnVoltar, btnTestePalavras, btnTesteTexto, btnTesteMulti, btnMenu;
	protected String strings[];
	protected int[] iDs;
	protected String disciplina;
    Context context;
	protected int idDisciplina;


	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolhe_tipo_teste);

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

        context = this;
		// /////////////////7Retirar os Extras DA JANELA ANTERIOR
		// ///////////////////
		Bundle b = getIntent().getExtras();
		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		strings = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");
		disciplina = b.getString("Disciplina");
		idDisciplina = b.getInt("idDisciplina");

		// /////////////////////////////////////ACEDER A OBJECTOS VISUAIS DA
		// JANELA//////////////
		btnVoltar = (Button) findViewById(R.id.escTipoTVoltar);

        btnMenu = (Button) findViewById(R.id.btnMenuEscTipoTest);

		btnTestePalavras = (Button) findViewById(R.id.btnTestePalav);
		btnTesteTexto = (Button) findViewById(R.id.btnLeituraTest);
		btnTesteMulti = (Button) findViewById(R.id.btnTestMulti);
		((TextView) findViewById(R.id.escTipoTEscola)).setText(strings[0]);
		((TextView) findViewById(R.id.escTipoTProfname)).setText(strings[1]);
		((TextView) findViewById(R.id.txtTipoTDisciplina))
				.setText("Escolha o tipo de Teste de " + disciplina + " :");
		((TextView) findViewById(R.id.escTipoTTurma)).setText(strings[3]);
		((TextView) findViewById(R.id.escTipoTAluni)).setText(strings[4]);
		final View contentView = findViewById(R.id.escTipoTeste);
		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

		int largura = getResources().getDimensionPixelSize(R.dimen.dim100);
		// se PROFESSOR tem uma foto, usa-se
		if (strings[2] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.escTipoTImgProf));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/School-Data/Professors/"
					+ strings[2];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}

		// se ALUNO tem uma foto, usa-se
		if (strings[5] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.escTipoTImgAluno));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Students/" + strings[5];
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
			escutaBotoes();
	}

	/**
	 * Procedimento para veirficar os botoes
	 * 
	 * @author Alex
	 */
	private void escutaBotoes() {
		// ////////////////botao boltar /////////////
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
        // ////////////////botao Menu /////////////
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Navegar para:");
                builder.setItems(new CharSequence[] { "Janela Inicial",
                                "Escolher Aluno","Escolher Professor", "Cancelar" },
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // The 'which' argument contains the index
                                // position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        navegarPaginaINI();
                                        break;
                                    case 1:
                                        navegarEscAluno();
                                        break;
                                    case 2:
                                        navegarEscProfessor();
                                        break;
                                    case 3:
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });



		// ////////////////botao testePalavras /////////////
		btnTestePalavras.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// //////////////ENVIAR DADOS PARA A JANELA SEGUINTE
				// ////////////////
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", idDisciplina);
				wrap.putString("Disciplina", disciplina);
				wrap.putInt("TipoTesteid", 2);
				wrap.putString("TipoTeste", "Leitura de Palavras");
				Intent inten = new Intent(EscTipoTeste.this, EscolheTeste.class);
				inten.putExtras(wrap);
				startActivity(inten);
			}
		});
		// ////////////////botao testeTexto/////////////
		btnTesteTexto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// //////////////ENVIAR DADOS PARA A JANELA SEGUINTE
				// ////////////////
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", idDisciplina);
				wrap.putString("Disciplina", disciplina);
				wrap.putInt("TipoTesteid", 0);
				wrap.putString("TipoTeste", "Leitura de Textos");
				Intent inten = new Intent(EscTipoTeste.this, EscolheTeste.class);
				inten.putExtras(wrap);
				startActivity(inten);
			}
		});

		// ////////////////botao testeMultimedia /////////////
		btnTesteMulti.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// //////////////ENVIAR DADOS PARA A JANELA SEGUINTE
				// ////////////////
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", idDisciplina);
				wrap.putString("Disciplina", disciplina);
				wrap.putInt("TipoTesteid", 1);
				wrap.putString("TipoTeste", "Interpretacao atraves de Imagens");
				Intent inten = new Intent(EscTipoTeste.this, EscolheTeste.class);
				inten.putExtras(wrap);
				startActivity(inten);
			}
		});

	}

    public void navegarPaginaINI() {
        Intent intent = new Intent(getApplicationContext(), PaginaInicial.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void navegarEscAluno() {

        LetrinhasDB bd = new LetrinhasDB(
                getApplicationContext());
        Professor prf = bd.getProfessorById(iDs[1]);

        String PIN = prf.getPassword();
        Bundle wrap = new Bundle();
        wrap.putString("PIN", PIN);

        // iniciar a pagina (Autentica��o)
        Intent at = new Intent(getApplicationContext(),
                Autenticacao.class);
        at.putExtras(wrap);
        startActivityForResult(at, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getExtras().getBoolean("Resultado")) {
            Bundle wrap = new Bundle();
        wrap.putString("Escola", strings[0]);
        wrap.putInt("Escola_ID", iDs[0]);
        wrap.putString("Professor", strings[1]);
        wrap.putInt("Professor_ID", iDs[1]);
        wrap.putString("foto_Professor", strings[2]);
        wrap.putString("Turma", strings[3]);
        wrap.putInt("turma_ID", iDs[2]);
        Intent intent = new Intent(getApplicationContext(), EscolheAluno.class);
        intent.putExtras(wrap);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        }
    }

    public void navegarEscProfessor() {
        Bundle wrap = new Bundle();
        wrap.putString("Escola", strings[0]);
        wrap.putInt("Escola_ID", iDs[0]);
        Intent intent = new Intent(getApplicationContext(), EscolheProfessor.class);
        intent.putExtras(wrap);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
