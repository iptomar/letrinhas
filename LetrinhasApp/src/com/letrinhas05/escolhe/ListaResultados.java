package com.letrinhas05.escolhe;

import com.letrinhas05.R;
import com.letrinhas05.R.layout;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


/**Activity para listar os resultados do aluno selecionado
 * 
 * @author Thiago
 *
 */
public class ListaResultados extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_resultados);
	}


}
