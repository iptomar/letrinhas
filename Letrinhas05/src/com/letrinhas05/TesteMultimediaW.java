package com.letrinhas05;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.TesteLeitura;
import com.letrinhas05.ClassesObjs.TesteMultimedia;
import com.letrinhas05.escolhe.EscolheEscola;
import com.letrinhas05.util.SystemUiHider;
import com.letrinhas05.util.Teste;

/**
 * Created by Alex on 23/05/2014.
 */
public class TesteMultimediaW  extends Activity  {

    TesteMultimedia teste;
    TextView txtTitulo, txtCabeTituloMul;
    ImageView imgTitulo;
    Button bntOpcao1, bntOpcao2 ,bntOpcao3;
    int tipo, idTesteAtual;
    String[] Nomes;
    int[] iDs, testesID;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    /*********************************************************************
     * The flags to pass to {@link com.letrinhas05.util.SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teste_multimedia_w);

        ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltarTestMult);

        LinearLayout line = (LinearLayout) findViewById(R.id.linearTestMultw);

        txtCabeTituloMul = (TextView) findViewById(R.id.txtCabeTituloTextMult);
         imgTitulo = (ImageView) findViewById(R.id.imgTituloTextMult);
          txtTitulo = (TextView) findViewById(R.id.txtMultiTextoTitul);

         bntOpcao1 = (Button) findViewById(R.id.btnOpcao1Mult);
        bntOpcao2 = (Button) findViewById(R.id.btnOpcao2Mult);
        bntOpcao3 = (Button) findViewById(R.id.btnOpcao3Mult);

        Bundle b = getIntent().getExtras();
        inicia(b);

        // Botao de voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void inicia(Bundle b) {
        // Compor novamente e lista de testes
        testesID = b.getIntArray("ListaID");
        // String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
        Nomes = b.getStringArray("Nomes");
        // int's - idEscola, idProfessor, idTurma, idAluno
        iDs = b.getIntArray("IDs");

        /** Consultar a BD para preencher o conte�do.... */
        LetrinhasDB bd = new LetrinhasDB(this);
        teste = bd.getTesteMultimediaById(testesID[0]);

        ((TextView) findViewById(R.id.textCabecalhoTestMultimedia))
                .setText(teste.getTitulo());

        txtCabeTituloMul.setText(teste.getTexto());

       if (teste.getContentIsUrl() == 1){
           String imageInSD = Environment.getExternalStorageDirectory()
                   .getAbsolutePath() + "/School-Data/MultimediaTest/" + teste.getConteudoQuestao();
           Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
           imgTitulo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100,
                   100, false));
           imgTitulo.setVisibility(View.VISIBLE);
       }
        else
       {
           txtTitulo.setText(teste.getConteudoQuestao());
           txtTitulo.setVisibility(View.VISIBLE);

       }


        if (teste.getOpcao1IsUrl() == 1)
        {

            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/MultimediaTest/" + teste.getOpcao1();
            Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
            ImageView imageView = new ImageView(this);
            // ajustar o tamanho da imagem
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
                    200, 200, false));
            // enviar para o bot�o
            bntOpcao1.setCompoundDrawablesWithIntrinsicBounds(null,
                    imageView.getDrawable(), null, null);
          }
        else
        {
            bntOpcao1.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            bntOpcao1.setText(teste.getOpcao1());
        }

        if (teste.getOpcao2IsUrl() == 1)
        {

            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/MultimediaTest/" + teste.getOpcao2();
            Bitmap bitmap2 = BitmapFactory.decodeFile(imageInSD);
            ImageView imageView2 = new ImageView(this);
            // ajustar o tamanho da imagem
            imageView2.setImageBitmap(Bitmap.createScaledBitmap(bitmap2,
                    200, 200, false));
            // enviar para o bot�o
            bntOpcao2.setCompoundDrawablesWithIntrinsicBounds(null,
                    imageView2.getDrawable(), null, null);
        }
        else
        {
            bntOpcao2.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            bntOpcao2.setText(teste.getOpcao2());
        }


        if (teste.getOpcao3IsUrl() == 1)
        {

            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/MultimediaTest/" + teste.getOpcao3();
            Bitmap bitmap3 = BitmapFactory.decodeFile(imageInSD);
            ImageView imageView3 = new ImageView(this);
            // ajustar o tamanho da imagem
            imageView3.setImageBitmap(Bitmap.createScaledBitmap(bitmap3,
                    200, 200, false));
            // enviar para o bot�o
            bntOpcao3.setCompoundDrawablesWithIntrinsicBounds(null,
                    imageView3.getDrawable(), null, null);
        }
        else
        {
            bntOpcao3.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            bntOpcao3.setText(teste.getOpcao3());
        }

//
//        ((TextView) findViewById(R.id.txtTexto)).setText(teste
//                .getConteudoTexto());
//
//        // **********************************************************************************************
//
//        idTesteAtual = testesID[0];
//        endereco = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/School-Data/submits/" + iDs[0] + "/" + iDs[1] + "/"
//                + iDs[2] + "/" + iDs[3] + "/" + "/" + testesID[0] + "/";
//
//        fileName = getCurrentTimeStamp() + ".3gpp";
//
//        audio = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/School-Data/ReadingTests/" + teste.getProfessorAudioUrl();
//
//        // descontar este teste da lista.
//        int[] aux = new int[testesID.length - 1];
//        for (int i = 1; i < testesID.length; i++) {
//            aux[i - 1] = testesID[i];
//        }
//        testesID = aux;

    }

}
