package com.letrinhas03.BaseDados;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.letrinhas03.R;
import com.letrinhas03.ClassesObjs.CorrecaoTesteLeitura;

public class Send_ResultTest extends Activity {
    // url DO servidor, preenchido em baixo
    private static String url_Server = "";
    // Progress Dialog
    public ProgressDialog pDialog;
    public EditText inputestudanteId;
    public EditText inputObservacoes;
    public EditText inputWpm;
    public EditText inputTestId;
    public EditText inputCompletionDate;
    JSONParser jsonParser = new JSONParser();
    EditText inputVoiceBase64;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_result_test);

        // Obtendo Detalhes dos Testes do intent
        Intent i = getIntent();

        // Obtendo CAMPO IP e PORTA enviados para esta Janela
        String ip = i.getStringExtra("IP");
        String porta = i.getStringExtra("PORTA");
        url_Server = "http://" + ip + ":" + porta + "/postTestResults";

        // Preenchimento das variaveis do a informacao da janela
        inputTestId = (EditText) findViewById(R.id.txtBoxId);
        inputestudanteId = (EditText) findViewById(R.id.txtBoxEstudanteId);
        inputCompletionDate = (EditText) findViewById(R.id.txtBoxDate);
        inputObservacoes = (EditText) findViewById(R.id.txtBoxObservacoes);
        inputWpm = (EditText) findViewById(R.id.txtBoxWpm);

        // criar o botao
        Button btnSend = (Button) findViewById(R.id.btnSendResult);

        // evento do botao
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new SendResult().execute();
            }
        });
    }

    public void exec()
    {
        int idEstudante = Integer.parseInt(inputestudanteId.getText().toString());
        String observacoes = inputObservacoes.getText().toString();
        float wpm = Float.parseFloat(inputWpm.getText().toString());
        int testId = Integer.parseInt(inputTestId.getText().toString());
        String date = inputCompletionDate.getText().toString();

        LetrinhasDB db = new LetrinhasDB(this);
       byte[]  img = db.getProfessorById(3).getFoto();

        CorrecaoTesteLeitura corrTestLeit = new CorrecaoTesteLeitura(testId, idEstudante, date, observacoes, wpm, 10, 12, 12.0f, 12.0f, 12.0f, 12.0F, img);
        NetworkUtils.postResultados(url_Server, corrTestLeit);



    }
    /**
     * Background Async Task para enviar os resultadost
     */
    class SendResult extends AsyncTask<String, String, String> {

        /**
         * Antes de come�ar o processo exibe uma Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Send_ResultTest.this);
            pDialog.setMessage("Enviando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Envia os Resultados
         */
        protected String doInBackground(String... args) {

            exec();



            return null;
        }

        /**
         * Depois de concluida a background task fecha a Progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
