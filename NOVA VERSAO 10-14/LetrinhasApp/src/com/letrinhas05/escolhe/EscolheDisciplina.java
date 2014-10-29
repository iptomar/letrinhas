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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.letrinhas05.PaginaInicial;
import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Professor;
import com.letrinhas05.util.Autenticacao;
import com.letrinhas05.util.SystemUiHider;

/**
 * Classe de apoio à Pagina de escolher disciplina
 *  
 * @author Thiago
 */
public class EscolheDisciplina extends Activity {

	Button volt, pt, mat, estMeio, ingl, btnMenu;
	String strings[];
    boolean menu = false;
	int[] iDs;
    Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.escolhe_disciplina);

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
		// Retirar os Extras
		Bundle b = getIntent().getExtras();

		// String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
		strings = b.getStringArray("Nomes");
		// int's - idEscola, idProfessor, idTurma, idAluno
		iDs = b.getIntArray("IDs");

		// preencher informa��o na activity
		((TextView) findViewById(R.id.escDEscola)).setText(strings[0]);
		((TextView) findViewById(R.id.tvDProf)).setText(strings[1]);
		int largura = getResources().getDimensionPixelSize(R.dimen.dim100);
		// se professor tem uma foto, usa-se
		if (strings[2] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.ivDProfessor));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/School-Data/Professors/"
					+ strings[2];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}
		((TextView) findViewById(R.id.tvDTurma)).setText(strings[3]);
		((TextView) findViewById(R.id.tvDAluno)).setText(strings[4]);
		// se professor tem uma foto, usa-se
		if (strings[5] != null) {
			ImageView imageView = ((ImageView) findViewById(R.id.ivDAluno));
			String imageInSD = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/School-Data/Students/" + strings[5];
			Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura,
					largura, false));
		}

		// new line faz a rotacao do ecran em 180 graus
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		// esconder o title************************************************+
		final View contentView = findViewById(R.id.escDisciplina);


		volt = (Button) findViewById(R.id.btnDVoltar);
		pt = (Button) findViewById(R.id.pt);
		mat = (Button) findViewById(R.id.mat);
		estMeio = (Button) findViewById(R.id.estMeio);
		ingl = (Button) findViewById(R.id.english);
        btnMenu= (Button) findViewById(R.id.btnMenuEscDisc);
		escutaBotoes();
	}


	/**
	 * Garantir que vai fazer um controlo de acesso quando tenta sair deste modo
	 * 
	 * @author Thiago
	 */
	@Override
	public void onBackPressed() {
		volt.performClick();
	}
	
	/**
	 * Fica à espera da resposta da outra activity
	 * 
	 * @author Thiago
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (menu == false) {
            if (data.getExtras().getBoolean("Resultado")) {
                finish();
            }
        }
        else
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
	
	
	/**
	 * Procedimento para veirficar os botoes
	 * 
	 * @author Thiago
	 */
	private void escutaBotoes() {

		volt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
                menu = false;
					LetrinhasDB bd = new LetrinhasDB(getApplicationContext());
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
		});


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

		pt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necess�rios
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 1);
				wrap.putString("Disciplina", "Portugues");

				// iniciar a pagina (escolher tipo teste)
				Intent ipt = new Intent(getApplicationContext(),
						EscTipoTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
			}
		});

		mat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necess�rios
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 2);
				wrap.putString("Disciplina", "Matematica");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(getApplicationContext(),
						EscTipoTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
			}
		});

		estMeio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necess�rios
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 3);
				wrap.putString("Disciplina", "Estudo do Meio");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(getApplicationContext(),
						EscTipoTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
			}
		});

		ingl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da activity
				// enviar os parametros necess�rios
				Bundle wrap = new Bundle();
				wrap.putStringArray("Nomes", strings);
				wrap.putIntArray("IDs", iDs);
				wrap.putInt("idDisciplina", 4);
				wrap.putString("Disciplina", "English");

				// iniciar a pagina 2 (escolher testes a executar)
				Intent ipt = new Intent(getApplicationContext(),
						EscTipoTeste.class);
				ipt.putExtras(wrap);
				startActivity(ipt);
			}
		});
	}

    public void navegarPaginaINI() {
        Intent intent = new Intent(getApplicationContext(), PaginaInicial.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void navegarEscAluno() {

        menu = true;
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
