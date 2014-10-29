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
import com.letrinhas05.PaginaInicial;
import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Escola;
import com.letrinhas05.util.SystemUiHider;

/**
 * Classe de apoio à Pagina de Escolher escola
 * 
 * @author Thiago
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class EscolheEscola extends Activity {

    protected Button btnVoltar;
    protected List<Escola> listaEscolas;
    protected LetrinhasDB db;
    protected int[] id;
    protected String[] img;
    protected String[] arrNomeEscolas;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_escolhe_escola);

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

        ///Declaracao de coisas da janela /////////////////////////////////
        btnVoltar = (Button) findViewById(R.id.btnVoltarEscolherEscola);
        final View contentView = findViewById(R.id.escEscola);
        ///////////////////////////////////////////////////////////////////

		// new line faz a rotacao do ecr�n em 180 graus
				int currentOrientation = getResources().getConfiguration().orientation;
				if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				} else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}

        ////////////ClickLister De botoes//////////////////////
        btnVoltar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {// sair da ativity
				Intent it = new Intent(getApplicationContext(),
						PaginaInicial.class);
				startActivity(it);
				finish();
			}
		});
		makeTabela();
	}

	/**
	 * Novo método para criar o painel dinâmico para os botões de seleção da
	 * escola
	 * 
	 * @author Thiago
	 */
	@SuppressLint("NewApi")
	private void makeTabela() {
		// Cria o objecto da base de dados
		db = new LetrinhasDB(this);
        listaEscolas = db.getAllSchools();
        arrNomeEscolas = new String[listaEscolas.size()];
		id = new int[listaEscolas.size()];
		img = new String[listaEscolas.size()];

		for (int i = 0; i < listaEscolas.size(); i++) {
            arrNomeEscolas[i] = listaEscolas.get(i).getNome();
			id[i] = listaEscolas.get(i).getIdEscola();
			img[i] = listaEscolas.get(i).getLogotipoNome();
		}


		/**
		 * Scroll view com uma table de 4 colunas(max)
		 */
		// tabela a editar
		TableLayout tabela = (TableLayout) findViewById(R.id.tblEscolhe);
		// linha da tabela a editar
		TableRow linha = (TableRow) findViewById(R.id.linha01);
		// 1� bot�o
		Button bt = (Button) findViewById(R.id.bt4);
		bt.setText("teste escolas");

		// Contador de controlo
		int cont = 0;
		for (int i = 0; i < listaEscolas.size() / 4; i++) {
			// nova linha da tabela
			TableRow linha1 = new TableRow(getBaseContext());
			// Copiar os parametros da 1� linha
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < 4; j++) {

				// **********************************
				// Nome da escola,

				final String school = arrNomeEscolas[cont];
				final int idEs = id[cont];
				// ***********************************

				// novo bot�o
				Button bt1 = new Button(bt.getContext());
				// copiar os parametros do bot�o original
				bt1.setLayoutParams(bt.getLayoutParams());

				// se a escola ja tiver logotipo, vou busca-lo
				if (img[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Schools/"+ img[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					///////////////////TESTE MULTIRESOLUCAO //////////
					int largura = getResources().getDimensionPixelSize(R.dimen.dim240);
					
					///////////////////////////////////////////////////
									
					//ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura, largura, false));
					//enviar para o botao
					bt1.setCompoundDrawablesWithIntrinsicBounds(null, imageView.getDrawable(),
							null, null);
				} else {
					// senão copia a imagem do botão original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}

				// addicionar o nome
				bt1.setText(arrNomeEscolas[cont]);
				// Defenir o que faz o botao ao clicar, neste caso muda o texto
				// do cabeçalho
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", school);
						wrap.putInt("Escola_ID", idEs);
						Intent it = new Intent(getApplicationContext(),
								EscolheProfessor.class);
						it.putExtras(wrap);

						startActivity(it);
						finish();
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
		if (arrNomeEscolas.length % 4 != 0) {
			TableRow linha1 = new TableRow(getBaseContext());
			linha1.setLayoutParams(linha.getLayoutParams());
			for (int j = 0; j < arrNomeEscolas.length % 4; j++) {

				final String school = arrNomeEscolas[cont];
				final int idEs = id[cont];

				Button bt1 = new Button(bt.getContext());
				bt1.setLayoutParams(bt.getLayoutParams());

				// se a escola já tiver logotipo, vou busca-lo
				if (img[cont] != null) {
					String imageInSD = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/School-Data/Schools/"+ img[cont];
					Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
					ImageView imageView = new ImageView(this);

					int largura = getResources().getDimensionPixelSize(R.dimen.dim240);
					//ajustar o tamanho da imagem
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, largura, largura, false));
					//enviar para o botao
					bt1.setCompoundDrawablesWithIntrinsicBounds(null, imageView.getDrawable(),
							null, null);

				} else {
					// sen�o copia a imagem do botao original
					bt1.setCompoundDrawables(null,
							bt.getCompoundDrawablesRelative()[1], null, null);
				}
				
				bt1.setText(arrNomeEscolas[cont]);
				bt1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Entrar na activity
						Bundle wrap = new Bundle();
						wrap.putString("Escola", school);
						wrap.putInt("Escola_ID", idEs);
						Intent it = new Intent(getApplicationContext(),
								EscolheProfessor.class);
						it.putExtras(wrap);
						startActivity(it);
						finish();

					}
				});
				linha1.addView(bt1);
				cont++;

			}
			// inserir a linha criada com o resto dos botoes
			tabela.addView(linha1);
		}
		// por fim elimino a 1ª linha
		tabela.removeView(linha);
	}

}
