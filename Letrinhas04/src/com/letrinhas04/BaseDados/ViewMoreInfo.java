package com.letrinhas04.BaseDados;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.letrinhas03.R;

public class ViewMoreInfo extends Activity {

    // JSON Nome dos campos
    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT = "textContent";
    private static final String TAG_MAX_TRIES = "maxTries";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_info);

        // Obtendo Detalhes dos Testes do intent
        Intent i = getIntent();

        // Obtendo Campos enviados para esta Janela
        String title = i.getStringExtra(TAG_TITLE);
        String text = i.getStringExtra(TAG_TEXT);
        String tries = i.getStringExtra(TAG_MAX_TRIES);

        //Declara��o dos TextViews Da janela
        TextView txtTtilo = (TextView) findViewById(R.id.txtTitulo);
        txtTtilo.setText(title);

        TextView txtText = (TextView) findViewById(R.id.txtTextHist);
        txtText.setText(text);

        TextView txtTries = (TextView) findViewById(R.id.txtTries);
        txtTries.setText("N� Tentativas:  " + tries);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Voltar para a Janela Anterior
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_more_info, menu);
        return true;
    }

}
