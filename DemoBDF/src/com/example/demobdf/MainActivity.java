package com.example.demobdf;

import java.io.IOException;

import com.example.demobdf.R;
import com.example.demobdf.Obj;
import com.example.demobdf.ObjDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ObjDataSource datasource;
	private Obj obj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		datasource = new ObjDataSource(this);
		datasource.open();
		
		//obj = datasource.insertObj("texto", );
		//obj = datasource.getObj(2);
		//TextView txtV = (TextView) findViewById(R.id.txtReadText);
		//String result = obj.getId() +" - " + obj.getType() +" - "+ obj.getObj();
		//txtV.setText(result);
		
		final Button buttonSet = (Button) findViewById(R.id.btSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	EditText editText = (EditText) findViewById(R.id.edtWriteText);
            	String message = editText.getText().toString();
            	try {
					obj = datasource.insertObj("texto", message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        final Button buttonGet = (Button) findViewById(R.id.btGet);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	obj = datasource.getObj(9);
            	TextView txtV = (TextView) findViewById(R.id.txtReadText);
            	String result = obj.getId() +" - " + obj.getType() +" - "+ (String)obj.getObj();
        		txtV.setText(result);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
