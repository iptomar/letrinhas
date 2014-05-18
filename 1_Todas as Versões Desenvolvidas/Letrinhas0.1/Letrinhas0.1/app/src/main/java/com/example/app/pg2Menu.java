package com.example.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Página menu para que o docente faça a sua opção
 *
 * @author Thiago
 */
public class pg2Menu extends Activity {
    ImageButton ibTeste, ibEscola, ibProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pg2_menu);

        ibTeste = (ImageButton) findViewById(R.id.ibTeste);
        ibEscola = (ImageButton) findViewById(R.id.ibEscola);
        ibProf = (ImageButton) findViewById(R.id.ibProfessor);
        escutaBotoes();
    }

    private void escutaBotoes(){
        /**
         * para aceder à demonstração de teste
         */
        ibTeste.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //iniciar a pagina 2 (menu)
                        Intent it= new Intent(pg2Menu.this,pg3TesteDemo.class);
                        startActivity(it);
                        finish();
                    }
                }

        );

        ibEscola.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //opção não disponivel
                        opNaoDisponive();
                    }
                }
        );

        ibProf.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //opção não disponivel
                        opNaoDisponive();
                    }
                }
        );



    }

    /**
     * Método para apresentar uma menagem ao utilizador de que
     * a opção escolhida não está disponivel
     */
    public void opNaoDisponive(){
        android.app.AlertDialog alerta;

        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Letrinhas 0.1");
        //define a mensagem
        builder.setMessage("A opção escolhida ainda não está disponivel!");
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(pg2Menu.this, "Lamentamos", Toast.LENGTH_SHORT).show();
            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Mostra
        alerta.show();

    }


}
