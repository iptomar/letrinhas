package com.letrinhas05.util;

import com.letrinhas05.R;
import com.letrinhas05.BaseDados.LetrinhasDB;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Autenticacao extends Activity {
	String userName, passWord;
    EditText password;
    Button login;
    LetrinhasDB db;
    int id;
  /**
   * Chamado apenas uma vez quando é criado
   * @author Dario
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.autenticacao);
      // UI elements gets bind in form of Java Objects
      password = (EditText)findViewById(R.id.password);
      login = (Button)findViewById(R.id.login);
      // now we have got the handle over the UI widgets
      // setting listener on Login Button
      // i.e. OnClick Event
      db = new LetrinhasDB(this);
      Bundle b = getIntent().getExtras();
      id = b.getInt("idProf");
		//passWord = b.getString("pass");
      passWord = db.getProfessorById(id).getPassword();
      Log.d("authentication", "pass->"+passWord);
      login.setOnClickListener(loginListener);  
  }
  
  private OnClickListener loginListener = new OnClickListener() {
    public void onClick(View v) {
    	  //vai buscar os dados que o utilizador introduzio    
          if(password.getText().toString().equals(passWord)){
        	  //responde aos inputs do user
              Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();  
              //Intent it= new Intent(Autenticacao.this, EscModo.class);
             // startActivity(it);
          }else
              Toast.makeText(getApplicationContext(), "Wrong Pin", Toast.LENGTH_LONG).show();                           
    }
  };
}