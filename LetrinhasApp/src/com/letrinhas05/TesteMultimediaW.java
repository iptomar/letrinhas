package com.letrinhas05;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.CorrecaoTesteMultimedia;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.ClassesObjs.TesteMultimedia;
import com.letrinhas05.util.SystemUiHider;

import java.io.File;

/**
 * Created by Alex on 23/05/2014.
 */
public class TesteMultimediaW  extends Activity  {

    protected TesteMultimedia testeM;
    protected TextView  txtCabeTituloMul;
    protected Button bntOpcao1, bntOpcao2 ,bntOpcao3;
    protected int tipo, idTesteAtual, opcaoCerta;
    protected String[] Nomes;
    protected int[] iDs, testesID;
    protected LinearLayout line;
    protected Context context;


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
     * The flags to pass to {@link SystemUiHider#getInstance}.
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

        ////////////////////////Aceder a objectos visuais da Janela//////////////////////////////////////////7
        ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltarTestMult);
        line = (LinearLayout) findViewById(R.id.linearTestMultw);
        txtCabeTituloMul = (TextView) findViewById(R.id.txtCabeTituloTextMult);
        bntOpcao1 = (Button) findViewById(R.id.btnOpcao1Mult);
        bntOpcao2 = (Button) findViewById(R.id.btnOpcao2Mult);
        bntOpcao3 = (Button) findViewById(R.id.btnOpcao3Mult);
/////////////////////////////////////////////////////////////////////////////////////////////
        // new line faz a rotacao do ecran 180 graus
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        // / esconder o title************************************************+
        final View contentView = findViewById(R.id.testMulayoutrincipal);
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView,
                HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mShortAnimTime;
                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                        }
                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

                context = this;
        //////////////////////////////////////////////////////////////////////
        inicia(getIntent().getExtras());

        // Botao de voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog alerta;
                // Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // define o titulo
                builder.setTitle("Letrinhas - AVISO");
                // define a mensagem
                builder.setMessage("Tem a certeza que deseja sair deste teste?");
                // define os botoes
                builder.setNegativeButton("Nao",null);
                builder.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                // cria o AlertDialog
                alerta = builder.create();
                // Mostra
                alerta.show();
            }
        });

        // Botoes de Opcao
        bntOpcao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(1);
            }
        }); // Botao de Opcao 1
        bntOpcao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(2);
            }
        }); // Botao de Opcao 2
        bntOpcao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(3);
            }
        }); // Botao de Opcao 3

    }

    /**
     * Inicia toda a atividade preenche variaveis e carrega a janela de acordo com o tipo de campos
     * @param b Bundle da janela anterior
     */
    public void inicia(Bundle b) {
        testesID = b.getIntArray("ListaID");
        // String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
        Nomes = b.getStringArray("Nomes");
        // int's - idEscola, idProfessor, idTurma, idAluno
        iDs = b.getIntArray("IDs");


        /** Consultar a BD para preencher o conte�do.... */
        LetrinhasDB bd = new LetrinhasDB(this);
        testeM = bd.getTesteMultimediaById(testesID[0]);

        ////////////Preencher DADOS///////////
        ((TextView) findViewById(R.id.textCabecalhoTestMultimedia)).setText(testeM.getTitulo());
        txtCabeTituloMul.setText(testeM.getTexto());
        idTesteAtual = testeM.getIdTeste();
        opcaoCerta = testeM.getCorrectOption();
        ////////////lIMPAR A LinearLayout/////////////////////
        line.removeAllViews();
        ImageView img1Vtitulo= new ImageView(this);
        TextView txtVTitulo = new TextView(this);
        txtVTitulo.setTextColor(Color.rgb(0, 0, 0));
        /////////////////////////////////////////////////////////////////////////////
        //////////verifica qual o tipo de de Opcao2/////////////////////////////////
        /////////////////////////////////////////////////////////////////////////
       if (testeM.getContentIsUrl() == 1){
           String imageInSD = Environment.getExternalStorageDirectory()
                   .getAbsolutePath() + "/School-Data/MultimediaTest/" + testeM.getConteudoQuestao();
           Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
           img1Vtitulo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 445,
                   445, false));
           LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(445, 445);
           img1Vtitulo.setLayoutParams(layoutParams);
           line.addView(img1Vtitulo);
       }
        else
       {
           txtVTitulo.setText(testeM.getConteudoQuestao());
           txtVTitulo.setTextSize(40);
           line.addView(txtVTitulo);
       }

        /////////////////////////////////////////////////////////////////////////////
        //////////verifica qual o tipo de de Opcao1/////////////////////////////////
        if (testeM.getOpcao1IsUrl() == 1)
        {

            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/MultimediaTest/" + testeM.getOpcao1();
            Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
            ImageView imageView = new ImageView(this);
            // ajustar o tamanho da imagem
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
                    210, 210, false));
            // enviar para o bot�o
            bntOpcao1.setCompoundDrawablesWithIntrinsicBounds(null,
                    imageView.getDrawable(), null, null);
          }
        else
        {
            bntOpcao1.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            bntOpcao1.setText(testeM.getOpcao1());
        }

        /////////////////////////////////////////////////////////////////////////////
        //////////verifica qual o tipo de de Opcao2/////////////////////////////////
        if (testeM.getOpcao2IsUrl() == 1)
        {

            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/MultimediaTest/" + testeM.getOpcao2();
            Bitmap bitmap2 = BitmapFactory.decodeFile(imageInSD);
            ImageView imageView2 = new ImageView(this);
            // ajustar o tamanho da imagem
            imageView2.setImageBitmap(Bitmap.createScaledBitmap(bitmap2,
                    210, 210, false));
            // enviar para o bot�o
            bntOpcao2.setCompoundDrawablesWithIntrinsicBounds(null,
                    imageView2.getDrawable(), null, null);
        }
        else
        {
            bntOpcao2.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            bntOpcao2.setText(testeM.getOpcao2());
        }

        /////////////////////////////////////////////////////////////////////////////
        //////////verifica qual o tipo de de Opcao3/////////////////////////////////
        if (testeM.getOpcao3IsUrl() == 1)
        {
            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/MultimediaTest/" + testeM.getOpcao3();
            Bitmap bitmap3 = BitmapFactory.decodeFile(imageInSD);
            ImageView imageView3 = new ImageView(this);
            // ajustar o tamanho da imagem
            imageView3.setImageBitmap(Bitmap.createScaledBitmap(bitmap3,
                    210, 210, false));
            // enviar para o bot�o
            bntOpcao3.setCompoundDrawablesWithIntrinsicBounds(null,
                    imageView3.getDrawable(), null, null);
        }
        else
        {
            bntOpcao3.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            bntOpcao3.setText(testeM.getOpcao3());
        }
        // descontar este teste da lista.
        int[] aux = new int[testesID.length - 1];
        for (int i = 1; i < testesID.length; i++) {
            aux[i - 1] = testesID[i];
        }
        testesID = aux;

    }

    /**
     * Submete o teste na bd e  termina a actividade
     * @param opcaoEscolhida opcao escolhida pelo aluno
     */
    public void submit(int opcaoEscolhida) {
       final CorrecaoTesteMultimedia ctM = new CorrecaoTesteMultimedia();
       final LetrinhasDB bd = new LetrinhasDB(this);
       final int opcaoEscolh = opcaoEscolhida;

        android.app.AlertDialog alerta;
        // Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // define o titulo
        builder.setTitle("CONFIRMAR");
        // define a mensagem
        builder.setMessage("Deseja escolher essa Opcao?");
        // define os botoes
        builder.setNegativeButton("Nao",null);
        builder.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

 ////////////////////////////////////////////////////////////////////////////////
                long time = System.currentTimeMillis() / 1000;
                String aux = idTesteAtual + iDs[3] + time + "";
                ctM.setIdCorrrecao(Long.parseLong(aux));
                ctM.setIdEstudante(iDs[3]);
                ctM.setTestId(idTesteAtual);
                ctM.setTipo(1);// pois estou num teste Multimedia
                ctM.setEstado(0);
                ctM.setDataExecucao(time);
                ctM.setOpcaoEscolhida(opcaoEscolh);
                ctM.setCerta(opcaoCerta);
                bd.addNewItemCorrecaoTesteMultimedia(ctM);
                finaliza();
                Bundle wrap = new Bundle();
                wrap.putInt("IDTeste", idTesteAtual);// id do teste atual
                wrap.putInt("IDAluno", iDs[3]); // id do aluno
                wrap.putInt("TipoTeste", 1); // id do aluno
                // listar submiss�es anteriores do mesmo teste
                Intent it = new Intent(getApplicationContext(),
                        ResumoSubmissoes.class);
                it.putExtras(wrap);
                startActivity(it);
    /////////////////////////////////////////////////////////////////
            }
        });
        // cria o AlertDialog
        alerta = builder.create();
        // Mostra
        alerta.show();
    }

    /**
     * Prepara a finalizacao da activity, descobrindo qual o proximo teste a
     * realizar Este metodo devera ser usado em todas as paginas de teste.
     */
    private void finaliza() {

        if (testesID.length != 0) {
            // enviar o parametro de modo
            Bundle wrap = new Bundle();
            wrap.putIntArray("ListaID", testesID);// id's dos testes
            wrap.putStringArray("Nomes", Nomes);
            wrap.putIntArray("IDs", iDs);
            // identifico o tipo do pr�ximo teste
            LetrinhasDB bd = new LetrinhasDB(this);
            Teste tst = bd.getTesteById(testesID[0]);
            tipo = tst.getTipo();

            switch (tipo) {
                case 0:
                    // lan�ar a nova activity do tipo texto,
                    Intent it = new Intent(getApplicationContext(),
                            Teste_Texto.class);
                    it.putExtras(wrap);

                    startActivity(it);

                    break;
                case 1:// lan�ar a nova activity do tipo imagem
                    Intent ip = new Intent(getApplicationContext(),
                            TesteMultimediaW.class);
                    ip.putExtras(wrap);

                    startActivity(ip);

                    break;
                case 2: // lan�ar a nova activity do tipo Palavras
                    Intent ipm = new Intent(getApplicationContext(),
                            Teste_Palavras_Aluno.class);
                    ipm.putExtras(wrap);

                    startActivity(ipm);

                    break;
                case 3: // lan�ar a nova activity do tipo poema
                    Intent ipp = new Intent(getApplicationContext(),
                            Teste_Palavras_Aluno.class);
                    ipp.putExtras(wrap);

                    startActivity(ipp);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), " - Tipo n�o defenido",
                            Toast.LENGTH_SHORT).show();
                    // retirar o teste errado e continuar

                    // descontar este teste da lista.
                    int[] aux = new int[testesID.length - 1];
                    for (int i = 1; i < testesID.length; i++) {
                        aux[i - 1] = testesID[i];
                    }
                    testesID = aux;
                    finaliza();
                    break;
            }

        }
        //
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(1000);
    }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


}
