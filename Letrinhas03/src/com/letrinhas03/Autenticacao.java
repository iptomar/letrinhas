package com.letrinhas03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Autenticacao extends Activity {
	String userName, passWord;
    EditText username, password;
    Button login;    
  /**
   * Chamado apenas uma vez quando é criado
   * @author Dario
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.autenticacao);
      // UI elements gets bind in form of Java Objects
      username = (EditText)findViewById(R.id.username);
      password = (EditText)findViewById(R.id.password);
      login = (Button)findViewById(R.id.login);
      // now we have got the handle over the UI widgets
      // setting listener on Login Button
      // i.e. OnClick Event
      login.setOnClickListener(loginListener);  
  }
  
  private OnClickListener loginListener = new OnClickListener() {
    public void onClick(View v) {
    	  //vai buscar os dados que o utilizador introduzio    
          if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
        	  //responde aos inputs do user
              Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();  
              Intent it= new Intent(Autenticacao.this, EscModo.class);
              startActivity(it);
          }else
              Toast.makeText(getApplicationContext(), "Login Not Successful", Toast.LENGTH_LONG).show();                           
    }
  };
}