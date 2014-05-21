package com.letrinhas05;

import java.util.List;

import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTeste;
import com.letrinhas05.ClassesObjs.CorrecaoTesteLeitura;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class RelatasCorrection extends Activity {

	LetrinhasDB db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatas_correction);
		db = new  LetrinhasDB(this);
		//Bundle b = new Bundle();
		
		List<CorrecaoTeste> data1 = db.getAllCorrecaoTeste();
		Log.d("CheckInserts: ", "***********Testes******************");
		for (CorrecaoTeste cn : data1) {
			String logs = "Id: " + cn.getIdCorrrecao() + ", idEstudante: "
					+ cn.getIdEstudante() + "  , estado: " + cn.getEstado()
					+ "  , testeId: " + cn.getTestId() + "  , tipo: "
					+ cn.getTipo() + "  , data: " + cn.getDataExecucao();
			// Writing Contacts to log
			Log.d("CheckInserts: ", logs);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.relatas_correction, menu);
		return true;
	}

}
