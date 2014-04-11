package com.jorge.seleccionartexto;

import android.R.color;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.provider.UserDictionary.Words;


public class MainActivity extends Activity {

	int contadorPalavrasErradas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}		
	}	

/*	public void alertButton(){
	//	new AlertDialog.Builder(this).setTitle("Classificacao").setMessage("Cenaças")
	//	.setNeutralButton("OK", null).show();
	final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
	final RatingBar rating = new RatingBar(this);
	rating.setMax(5);
	
	popDialog.setIcon(android.R.drawable.btn_star_big_on);
	popDialog.setTitle("Classificacao");
	popDialog.setView(rating);
	
	// Button OK
	popDialog.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
						classificacaozica = rating.getProgress();
						dialog.dismiss();
				}
				
			})
	// Button Cancel
	.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
	popDialog.create();
	popDialog.show();
	TextView ClassificarPaaa =(TextView)findViewById(R.id.zicoico);
	ClassificarPaaa.setText(ClassificarPaaa.getText().toString() + "Tá bem");
	}*/
	
	public void onClick(View view) {
		
		// Mostrar Popup se caregou no ecra
		final TextView textozico = (TextView)view;
        textozico.performLongClick();
        final int startSelection=textozico.getSelectionStart();
        final int endSelection=textozico.getSelectionEnd();
        final String selectedText = textozico.getText().toString().substring(startSelection, endSelection);
        
        PopupMenu menu = new PopupMenu(this, textozico);
		menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
		menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            	// Mostrar palavra seleccionada na textbox
            	switch (item.getItemId()) {
                    case R.id.PalavraErrada : 
                    	contadorPalavrasErradas +=1;
                    	TextView PErrada =(TextView)findViewById(R.id.TextoErradas); 
                    	PErrada.setText("Palavras Erradas: " + contadorPalavrasErradas);
                    	Spannable WordtoSpan =  (Spannable)textozico.getText();
                        ForegroundColorSpan cor = new ForegroundColorSpan(Color.RED);
                        WordtoSpan.setSpan(cor, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);        
                        textozico.setText(WordtoSpan);	
                        
                    	break;    
                    case R.id.CancelarSeleccao :
                    	if(contadorPalavrasErradas>0){
                    	Spannable WordtoCancelSpan =  (Spannable)textozico.getText();
                        ForegroundColorSpan corCancelar = new ForegroundColorSpan(Color.BLACK);
                        WordtoCancelSpan.setSpan(corCancelar, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);        
                        textozico.setText(WordtoCancelSpan);
                        contadorPalavrasErradas -=1;
                        TextView PErradaa =(TextView)findViewById(R.id.TextoErradas); 
                    	PErradaa.setText("Palavras Erradas: " + contadorPalavrasErradas); 
                    	} else {
                    	    Context context = getApplicationContext();
                 		   CharSequence text = "Não existem palavras erradas";
                 		   int duration = Toast.LENGTH_SHORT;
                 		   Toast toast = Toast.makeText(context, text, duration);
                 		   toast.show();
                    	}
                }
                return true;
            }
        });
		menu.show();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
	
