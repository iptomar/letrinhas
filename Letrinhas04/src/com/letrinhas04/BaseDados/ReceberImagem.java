package com.letrinhas04.BaseDados;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.letrinhas03.R;
import com.letrinhas04.ClassesObjs.Professor;

import org.json.JSONArray;

public class ReceberImagem extends Activity {
    static String url_all_tests = "";
    byte[] arrayBytesImage;
    String title;
    String textTxtBox;
    // Criar o objecto JSON Parser
    JSONParser jParser = new JSONParser();
    JSONArray tests = null;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receber_imagem);
        // Obtendo Detalhes dos Testes do intent
        Intent i = getIntent();
        // Obtendo CAMPO IP e PORTA enviados para esta Janela
        String ip = i.getStringExtra("IP");
        String porta = i.getStringExtra("PORTA");
        url_all_tests = "http://" + ip + ":" + porta + "/image";

        Button btnLerDaBD = (Button) findViewById(R.id.btnGuardarEmBD);
        // Botao receber imagem do servidor
        btnLerDaBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtBoxIdImage = (TextView) findViewById(R.id.txtBoxIDcampoImg);
                textTxtBox = txtBoxIdImage.getText().toString();
                new LoadAllTests().execute();
            }
        });

    }

    /**
     * LerDaBd - Vai ler uma linha na base de dados enviada por parametro
     * Recebe como parametro o id do campo que se pretende ler
     * *
     */
    public void LerDaBd(int idCampo) {
        LetrinhasDB db = new LetrinhasDB(this);
        Professor data = db.getProfessorById(idCampo);
        arrayBytesImage = data.getFoto();
        title = data.getNome();
    }

    /**
     * Background Async Task para carregar todos os Testes por HTTP Request
     */
    class LoadAllTests extends AsyncTask<String, String, String> {

        /**
         * Antes de iniciar a Thread Bacground aparece a progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReceberImagem.this);
            pDialog.setMessage("LER IMAGEM DA BD , Por Favor aguarde...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Faz em Background
         */
        protected String doInBackground(String... args) {
            //Vai ler da BD a linha com o ID  recebido da TxtBox
            LerDaBd(Integer.parseInt(textTxtBox));
            return null;
        }

        /**
         * Depois de completar tarefa de background Fechar a Progress Dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            //fechar a janela de Progress Dialog depois de receber todos os Tests
            pDialog.dismiss();
            // Actualizar a UI a partir da Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    TextView txtViewImage = (TextView) findViewById(R.id.txtTituloImageView);
                    txtViewImage.setText("Nome Prof: " + title);
                    ImageView imgViews = (ImageView) findViewById(R.id.imgVerImg);
                    Bitmap bmp = BitmapFactory.decodeByteArray(arrayBytesImage, 0, arrayBytesImage.length);
                    imgViews.setImageBitmap(bmp);
                }
            });

        }

    }

}
