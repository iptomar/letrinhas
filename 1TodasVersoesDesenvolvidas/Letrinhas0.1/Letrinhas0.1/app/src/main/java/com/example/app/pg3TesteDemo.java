package com.example.app;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.inputmethodservice.InputMethodService;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Classe que representa o layout e uma demonstração de um teste
 *
 * @author Thiago & Dário
 */
public class pg3TesteDemo extends ActionBarActivity {
    TextView msg,texto;
    ImageButton teste, ouvir, fimteste, voltar;
    ImageView sinal;

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teste_demo);

        //iniciar o ficheiro que guarda a gravação


        //inicializar a tela
        msg = (TextView) findViewById(R.id.tvMsg);
        texto = (TextView) findViewById(R.id.tvText);
        teste = (ImageButton) findViewById(R.id.start);
        ouvir = (ImageButton) findViewById(R.id.play);
        fimteste = (ImageButton) findViewById(R.id.stop);
        voltar = (ImageButton) findViewById(R.id.imVoltar);
        sinal = (ImageView) findViewById(R.id.ivRecording);

        msg.setText("Pronto a iniciar a leitura.");
        texto.setVisibility(View.INVISIBLE);
        texto.setText("Abelhinha, abelhinha\n" +
                "Toma lá a tua mosquinha\n" +
                "Zurra, zurra, pica na burra\n" +
                "Come, come, se tens fome");
        sinal.setVisibility(View.INVISIBLE);
        fimteste.setVisibility(View.INVISIBLE);
        ouvir.setVisibility(View.INVISIBLE);

        escutaBotoes();
    }

    private void escutaBotoes(){
        teste.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        teste.setVisibility(View.INVISIBLE);
                        fimteste.setVisibility(View.VISIBLE);
                        sinal.setVisibility(View.VISIBLE);
                        texto.setVisibility(View.VISIBLE);
                        voltar.setVisibility(View.INVISIBLE);
                        msg.setText("A gravar...");
                        start();
                    }
                }
        );
        fimteste.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fimteste.setVisibility(View.INVISIBLE);
                        sinal.setVisibility(View.INVISIBLE);
                        texto.setVisibility(View.INVISIBLE);
                        voltar.setVisibility(View.VISIBLE);
                        ouvir.setVisibility(View.VISIBLE);
                        stop();
                        msg.setText("Teste foi guardado.");
                    }
                }
        );
        ouvir.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sinal.setVisibility(View.VISIBLE);
                        texto.setVisibility(View.VISIBLE);
                        voltar.setVisibility(View.INVISIBLE);
                        ouvir.setVisibility(View.INVISIBLE);
                        msg.setText("A reproduzir...");
                        play();
                    }
                }
        );
        voltar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it= new Intent(pg3TesteDemo.this,pg2Menu.class);
                        startActivity(it);
                        finish();
                    }
                }
        );

    }

    private void start(){
        try {
            outputFile = Environment.getExternalStorageDirectory().
                    getAbsolutePath() + "/DemmoTeste.3gpp";
            myRecorder = new MediaRecorder();
            
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
            myRecorder.setOutputFile(outputFile);
            myRecorder.prepare();
            myRecorder.start();
            Toast.makeText(getApplicationContext(), "Iniciada a gravação", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro na gravação "+ e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }
    private void stop(){
        try{
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;
            Toast.makeText(getApplicationContext(), "Gravação com sucesso", Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
        }
    }
    private void play(){
         try{
             myPlayer = new MediaPlayer();
             myPlayer.setDataSource(outputFile);
             myPlayer.prepare();
             myPlayer.start();
            Toast.makeText(getApplicationContext(), "A reproduzir", Toast.LENGTH_LONG).show();

             while(myPlayer.isPlaying()){}

             myPlayer.release();
             myPlayer = null;

             sinal.setVisibility(View.INVISIBLE);
             texto.setVisibility(View.INVISIBLE);
             voltar.setVisibility(View.VISIBLE);
             ouvir.setVisibility(View.VISIBLE);
             teste.setVisibility(View.VISIBLE);
             msg.setText("Novamente pronto a iniciar a leitura.");
        }catch(Exception ex){
             Toast.makeText(getApplicationContext(),"Erro na reprodução", Toast.LENGTH_LONG).show();
         }}


}
