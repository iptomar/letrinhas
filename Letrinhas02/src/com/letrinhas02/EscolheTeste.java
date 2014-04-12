package com.letrinhas02;

import com.letrinhas02.util.ExecutaTestes;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EscolheTeste extends Activity {
	//modo prof, tvmoAluno = #5ddfff
	ImageButton volt, exect;
	int nTestes = 15;
	String titulos[];
	boolean modo;
	//Enderço/Query dos testes [];

	/****************************
	 * Por Fazer ******************************** executar os testes
	 * selecionados, um de cada vez
	 */
	public void executarTestes() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		int nElements = ll.getChildCount();
		
		int j = 0;
		//descobrir quantos e quais foram selecionados
		for (int i = 0; i < nElements; i++) {
			// verificar se o teste está ativo
			if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
				// inserir numa lista a criar o teste a realizar.
				// por fazer ********************************************+
				j++;
			}
		}

		//mostra quantos foram pressionados
		Toast.makeText(getApplicationContext(),
				"" + j + " Testes seleccionados", Toast.LENGTH_SHORT).show();
		// iniciar os testes.... por fazer
		
		if(
		ExecutaTestes exect = new ExecutaTestes(this, modo);
		exect.start();

	}

	/**
	 * Finalizar a ativity e voltar para a pagina anterior
	 */
	public void voltar() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.escolhe_teste);

		/************************************************************************
		 * Criação de um painel dinâmico para os botões de selecção dos testes
		 * existentes.
		 * 
		 * É necessário de saber primeiro onde estão os testes e quantos são!
		 * (Comunicar com a BD)
		 */
		// aceder à BD local, contar o nº de testes e os seus títulos
		// guardar essa informação num array para se aceder na construção do
		// scroll view

		//
		// ScrollView sv = (ScrollView)findViewById(R.id.svEscolher);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
		// Botão original que existe por defenição
		ToggleButton tg1 = (ToggleButton) findViewById(R.id.ToggleButton1);

		// Se o nº de testes for superior a 0, cria o nº de botões referentes aos testes
		if (0<nTestes) {
			int i = 0;
			// Atribuo o primeiro título ao primeiro botão
			// texto por defeito
			tg1.setText("O título do teste");
			// texto se não seleccionado = "titulo do teste sem numeração"
			tg1.setTextOff("O título do teste");
			// texto se seleccionado = "titulo do teste com numeração"
			tg1.setTextOn((i+1) + " - " + "O título do teste");
			i++;

			//Resto do títulos
			while(i<nTestes){
				// um novo botão
				ToggleButton tg = new ToggleButton(getBaseContext());
				// copiar os parametros de layout do 1º botão
				tg.setLayoutParams(tg1.getLayoutParams());
				tg.setTextSize(tg1.getTextSize());				
				// texto por defeito
				tg.setText("O título do teste");
				// texto se não seleccionado = "titulo do teste sem numeração"
				tg.setTextOff("O título do teste");
				// texto se seleccionado = "titulo do teste com numeração"
				tg.setTextOn((i+1) + " - " + "O título do teste");				
				// inserir no scroll view
				ll.addView(tg);
				i++;
			}
		}
		else{
			
		}

		volt = (ImageButton) findViewById(R.id.escTVoltar);
		exect = (ImageButton) findViewById(R.id.ibComecar);
		
		escutaBotoes();
	}
	
	private void escutaBotoes(){
        exect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	executarTestes();
                    }
                }

        );

        volt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //sair da aplicação
                        finish();
                    }
                }
        );
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.escolhe_teste, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.escolhe_teste, container,
					false);
			return rootView;
		}
	}

}
