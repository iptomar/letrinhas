package com.letrinhas04.BaseDados;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.letrinhas03.R;

import java.io.*;

public class ReprodAudio extends Activity {

    // URL do Servidor  Nota: Recebido em baixo////////
    private static String url_all_tests = "";
    public String ip;
    public String porta;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reprod_audio);
        Button btnTocarSom = (Button) findViewById(R.id.btnTocarSom);
         ///Recebe Ip da janela Anterior
        Intent i = getIntent();
        ip = i.getStringExtra("IP");
        porta = i.getStringExtra("PORTA");
        url_all_tests = "http://" + ip + ":" + porta + "/testAudio.mp3";


        btnTocarSom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BuscarEreproduzir().execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reprod_audio, menu);
        return true;
    }

    /**
     * Background Async Task para enviar os resultadost
     */
    class BuscarEreproduzir extends AsyncTask<String, String, String> {

        /**
         * Antes de comecar o processo exibe uma Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReprodAudio.this);
            pDialog.setMessage("Recebendo Audio....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         *Vai fazer um HTTP Request onde recebe um Audio e o reproduz
         */
        protected String doInBackground(String... args) {
            byte[]  audio = NetworkUtils.getFile(url_all_tests);
            File tempMp3 = null;
            try {
                ///Escreve a musica no ficheiro na pasta da aplicacao para depois o reproduzir no player
                tempMp3 = File.createTempFile("test", ".mp3", getCacheDir());
                FileOutputStream fos = new FileOutputStream(tempMp3);
                fos.write(audio);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ///Reproduz o audio num player
            MediaPlayer       mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(tempMp3));
            mediaPlayer.start();
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
