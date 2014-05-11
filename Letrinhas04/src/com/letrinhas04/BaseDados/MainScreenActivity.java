package com.letrinhas04.BaseDados;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.letrinhas03.R;


public class MainScreenActivity extends Activity {

    Button btnViewProducts;
    Button btnNewProduct;
    Button btnReceberImagem;
    Button btnJanelaTocar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Buttons do menu
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnSendResult);
        btnReceberImagem = (Button) findViewById(R.id.btnVerImagem);
        btnJanelaTocar = (Button) findViewById(R.id.btnJanelaTocar);


        // view products click event
        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // Executar janela de listar Testes
                Intent i = new Intent(getApplicationContext(), MainBD.class);
                //Enviar IP e Porta para outra janela
                i.putExtra("IP", ((TextView) findViewById(R.id.txtBoxIpServer)).getText().toString());
                i.putExtra("PORTA", ((TextView) findViewById(R.id.txtBoxPorta)).getText().toString());
                startActivity(i);

            }
        });


        btnReceberImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Executar janela de listar Testes
                Intent i = new Intent(getApplicationContext(), ReceberImagem.class);
                //Enviar IP e Porta para outra janela
                i.putExtra("IP", ((TextView) findViewById(R.id.txtBoxIpServer)).getText().toString());
                i.putExtra("PORTA", ((TextView) findViewById(R.id.txtBoxPorta)).getText().toString());
                startActivity(i);
            }
        });


        // view products click event
        btnNewProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Executar janela de enviar resultados
                Intent i = new Intent(getApplicationContext(), Send_ResultTest.class);
                //Enviar IP e Porta para outra janela
                i.putExtra("IP", ((TextView) findViewById(R.id.txtBoxIpServer)).getText().toString());
                i.putExtra("PORTA", ((TextView) findViewById(R.id.txtBoxPorta)).getText().toString());
                startActivity(i);

            }
        });

        btnJanelaTocar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Executar janela de enviar resultados
                Intent i = new Intent(getApplicationContext(), ReprodAudio.class);
                //Enviar IP e Porta para outra janela
                i.putExtra("IP", ((TextView) findViewById(R.id.txtBoxIpServer)).getText().toString());
                i.putExtra("PORTA", ((TextView) findViewById(R.id.txtBoxPorta)).getText().toString());
                startActivity(i);

            }
        });

    }


}
