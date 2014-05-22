package com.letrinhas05.escolhe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.letrinhas05.R;
import com.letrinhas05.util.SystemUiHider;

/**
 * Created by Alex on 17/05/2014.
 */
public class EscTipoTeste extends Activity {

    Button voltar, testePalavras, testeTexto, testeMulti;
    String strings[];
    int[] iDs;
    String disciplina;
    int idDisciplina;
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
        setContentView(R.layout.escolhe_tipo_teste);

        // Retirar os Extras
        Bundle b = getIntent().getExtras();

        // String's - Escola, Professor, fotoProf, Turma, Aluno, fotoAluno
        strings = b.getStringArray("Nomes");
        // int's - idEscola, idProfessor, idTurma, idAluno
        iDs = b.getIntArray("IDs");
        disciplina = b.getString("Disciplina");
        idDisciplina = b.getInt("idDisciplina");

        // preencher informa��o na activity
        ((TextView) findViewById(R.id.escTipoTEscola)).setText(strings[0]);
        ((TextView) findViewById(R.id.escTipoTProfname)).setText(strings[1]);
        ((TextView) findViewById(R.id.txtTipoTDisciplina)).setText("Escolha o tipo de Teste de "+disciplina + " :");

        // se professor tem uma foto, usa-se
        if (strings[2] != null) {
            ImageView imageView = ((ImageView) findViewById(R.id.escTipoTImgProf));
            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/School-Data/Professors/"
                    + strings[2];
            Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100,
                    100, false));
        }
        ((TextView) findViewById(R.id.escTipoTTurma)).setText(strings[3]);
        ((TextView) findViewById(R.id.escTipoTAluni)).setText(strings[4]);
        // se professor tem uma foto, usa-se
        if (strings[5] != null) {
            ImageView imageView = ((ImageView) findViewById(R.id.escTipoTImgAluno));
            String imageInSD = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/School-Data/Students/" + strings[5];
            Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100,
                    100, false));
        }

        // esconder o title************************************************+
        final View contentView = findViewById(R.id.escTipoTeste);

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

        voltar = (Button) findViewById(R.id.escTipoTVoltar);
        testePalavras = (Button) findViewById(R.id.btnTestePalav);
        testeTexto = (Button) findViewById(R.id.btnLeituraTest);
        testeMulti = (Button) findViewById(R.id.btnTestMulti);
        escutaBotoes();
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

    /**
     * Procedimento para veirficar os botoes
     *
     * @author Alex
     */
    private void escutaBotoes() {

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// sair da activity
                finish();
            }
        });

        testePalavras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// sair da activity
//                enviar os parametros necess�rios
                Bundle wrap = new Bundle();
                wrap.putStringArray("Nomes", strings);
                wrap.putIntArray("IDs", iDs);
                wrap.putInt("idDisciplina", idDisciplina);
                wrap.putString("Disciplina", disciplina);
                wrap.putInt("TipoTesteid", 2);
                wrap.putString("TipoTeste", "Leitura de Palavras");

                // iniciar a pagina 2 (escolher testes a executar)
                Intent ipt = new Intent(EscTipoTeste.this, EscolheTeste.class);
                ipt.putExtras(wrap);
                startActivity(ipt);
        //   finish();

                ////////////AQUI VOU PARA JANELA LEITURA DE PLAVRAS////////////////////
            }
        });

        testeTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// sair da activity
               // enviar os parametros necess�rios
                Bundle wrap = new Bundle();
                wrap.putStringArray("Nomes", strings);
                wrap.putIntArray("IDs", iDs);
                wrap.putInt("idDisciplina", idDisciplina);
                wrap.putString("Disciplina", disciplina);
                wrap.putInt("TipoTesteid", 0);
                wrap.putString("TipoTeste", "Leitura de Textos");

                // iniciar a pagina 2 (escolher testes a executar)
                Intent ipt = new Intent(EscTipoTeste.this, EscolheTeste.class);
                ipt.putExtras(wrap);
                startActivity(ipt);
            }
        });

        testeMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// sair da activity
                // enviar os parametros necesserios
                Bundle wrap = new Bundle();
                wrap.putStringArray("Nomes", strings);
                wrap.putIntArray("IDs", iDs);
                wrap.putInt("idDisciplina", idDisciplina);
                wrap.putString("Disciplina", disciplina);
                wrap.putInt("TipoTesteid", 1);
                wrap.putString("TipoTeste", "Interpretação atraves de Imagens");

                // iniciar a pagina 2 (escolher testes a executar)
                Intent ipt = new Intent(EscTipoTeste.this, EscolheTeste.class);
                ipt.putExtras(wrap);
                startActivity(ipt);
            }
        });

    }


}
