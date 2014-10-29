package com.letrinhas05.escolhe;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.letrinhas05.*;
import com.letrinhas05.BaseDados.LetrinhasDB;
import com.letrinhas05.ClassesObjs.Teste;
import com.letrinhas05.util.SystemUiHider;

/**
 * Classe de apoio à Pagina de escolher teste
 *  
 * @author Thiago
 */
public class EscolheTeste extends Activity {
    ////Variaveis
    protected Button volt, exect;
    protected int numero = 0, alunoId,tipo;
    protected String teste;
    protected String[] Nomes, array, titulo;
    protected int[]  idTestes, ids;
    protected LetrinhasDB ldb;

    protected int idArea, idTipo; // ////IDaREA IDTIPO DE TESTE
    protected String nomeDsiciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escolhe_teste);

        //////Ocultar barra de cima/////
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Hide the status bar.
        int uiOptionss = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptionss);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        /////////////////////////////////////

        volt = (Button) findViewById(R.id.escTVoltar);
        exect = (Button) findViewById(R.id.ibComecar);
        // recebe o parametro de modo
        Bundle b = getIntent().getExtras();
        ids = b.getIntArray("IDs");
        Nomes = b.getStringArray("Nomes");
        alunoId = ids[3];
        idArea = b.getInt("idDisciplina");
        idTipo = b.getInt("TipoTesteid");
        nomeDsiciplina = b.getString("Disiciplina");

        //disciplina
        TextView tvd= ((TextView)findViewById(R.id.escTDisciplina));
        tvd.setText(b.getString("Disciplina"));
        ImageView imgD = new ImageView(this);
        switch(idArea){
            case 1:
                imgD.setImageResource(R.drawable.pt);// Portugu�s
                break;
            case 2:
                imgD.setImageResource(R.drawable.mat);// Matem�tica
                break;
            case 3:
                imgD.setImageResource(R.drawable.estmeio);// Estudo do meio
                break;
            case 4:
                imgD.setImageResource(R.drawable.en);// English
                break;
        }
        tvd.setCompoundDrawablesWithIntrinsicBounds(null, null, imgD.getDrawable(), null);

        //tipo
        imgD = (ImageView)findViewById(R.id.escTivTipo);
        switch (idTipo){
            case 0:
                imgD.setImageResource(R.drawable.textos);
                break;
            case 1:
                imgD.setImageResource(R.drawable.imags);
                break;
            case 2:
                imgD.setImageResource(R.drawable.palavras);
                break;
        }

        // new line faz a rota��o do ecr�n em 180 graus
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        // esconder o title************************************************+
        final View contentView = findViewById(R.id.escTeste);

        /************************************************************************
         * Criação de um painel dinâmico para os botões de selecão dos testes
         * existentes.
         */

        ldb = new LetrinhasDB(this);
        // if getAllTesteByAreaIdAndTwoTypes
        List<Teste> dados;
        if (idTipo == 0)
            //texto e poemas
            dados = ldb.getAllTesteByAreaIdAndTwoTypes(idArea, idTipo, 3);
        else
            //imagens ou lista
            dados = ldb.getAllTesteByAreaIdAndType(idArea, idTipo);


        array = new String[dados.size()];
        titulo = new String[dados.size()];
        idTestes = new int[dados.size()];
        for (Teste cn : dados) {
            // String storage =
            // cn.getIdTeste()+","+cn.getTitulo().toString()+","+cn.getTexto().toString()+","+cn.getTipo()+","+cn.getDataInsercaoTeste()+","+cn.getGrauEscolar();
            String storage = cn.getIdTeste() + "," + cn.getTitulo().toString()
                    + "," + cn.getTexto().toString() + "," + cn.getTipo();
            
            array[numero] = storage.toString();
          
            titulo[numero] = cn.getTitulo();
            
            idTestes[numero] = cn.getIdTeste();
           
            numero++;
        }
        
        
        // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

        // Painel dinâmico
        // ****************************************************
        LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
        // Botão original que existe por defenição
        ToggleButton tg1 = (ToggleButton) findViewById(R.id.escTtbtn);
        // Se existirem testes no repositório correspondentes, cria o nº de
        // botões referentes ao nº de testes existentes
        if (0 < numero) {
            int i = 0;
            teste = titulo[i].toString();
            
            // Atribuo o primeiro t�tulo ao primeiro bot�o
            // ********************************+
            // texto por defeito
            tg1.setText(teste);
            // texto se n�o seleccionado = "titulo do teste sem numeração"
            tg1.setTextOff(teste);
            // texto se seleccionado = "titulo do teste com numeração"
            tg1.setTextOn((i + 1) + " - " + teste);
            i++;

            // Resto do t�tulos
            while (i < numero) {
                // um novo bot�o
                ToggleButton tg = new ToggleButton(getBaseContext());
                // copiar os parametros de layout do 1º botão
                tg.setLayoutParams(tg1.getLayoutParams());
                tg.setTextSize(tg1.getTextSize());
                teste = titulo[i].toString();
                // texto por defeito
                tg.setText(teste);
                // texto se n�o seleccionado =
                // "titulo do teste sem numeração"
                tg.setTextOff(teste);
                // texto se seleccionado = "titulo do teste com numeração"
                tg.setTextOn((i + 1) + " - " + teste);
                // inserir no scroll view
                ll.addView(tg);
                i++;
            }
        } else {
            // esconder os bot�es
            tg1.setVisibility(View.INVISIBLE);
            exect.setVisibility(View.INVISIBLE);

            android.app.AlertDialog alerta;
            // Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // define o titulo
            builder.setTitle("Letrinhas");
            // define a mensagem
            builder.setMessage("Nao foram encontrados testes no repositorio");
            // define um bot�o como positivo
            builder.setPositiveButton("OK", null);
            // cria o AlertDialog
            alerta = builder.create();
            // Mostra
            alerta.show();

        }


        
        escutaBotoes();
    }
    /**
     * Procedimento para veirficar os botões
     *
     * @author Thiago
     */
    private void escutaBotoes() {
        exect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executarTestes();
            }
        });

        volt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// sair da aplica��o
                finish();
            }
        });
    }

    /**
     * Procedimento 1/2 para executar os testes selecionados, um de cada vez
     * sequêncialmente, independentemente do seu tipo.
     *
     * @author Thiago
     */
    public void executarTestes() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.llescteste);
        // contar o numero de elementos (testes)
        int nElements = ll.getChildCount();

        int j = 0;
        // contar quantos e quais foram selecionados
        for (int i = 0; i < nElements; i++) {
            // verificar se o teste est� ativo
            if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
                j++;
            }
        }
        // Copiar os id's dos testes seleccionados para uma lista auxiliar
        int[] lstID = new int[j];
        j = 0;
        for (int i = 0; i <idTestes.length; i++) {
            if (((ToggleButton) ll.getChildAt(i)).isChecked()) {
                lstID[j] = idTestes[i];
                j++;
            }
        }

        iniciar(lstID);
    }


    /**
     * Procedimento 2/2 para executar os testes selecionados, um de cada vez
     * sequêncialmente, independentemente do seu tipo.
     *
     * @author Thiago
     */
    public void iniciar(int[] lstID) {
        // iniciar os testes....
        // Se existir items seleccionados arranca com os testes,
        if (0 < lstID.length) {

            // enviar os parametros
            Bundle wrap = new Bundle();

            wrap.putIntArray("ListaID", lstID);//id's dos testes selecionados
            wrap.putStringArray("Nomes", Nomes);
            wrap.putIntArray("IDs", ids);

            //consuta � base de dados o tipo do primeiro teste a executar
            tipo = ldb.getTesteById(lstID[0]).getTipo();

            switch (tipo) {
                case 0: // lançar a nova activity do tipo texto leitura,

                    Intent it = new Intent(getApplicationContext(),
                            Teste_Texto.class);
                    it.putExtras(wrap);

                    startActivity(it);

                    break;
                case 1:// lancar a nova activity do tipo multimedia, e o seu
                    // conteudo
                    Intent ip = new Intent(getApplicationContext(),
                            TesteMultimediaW.class);
                    ip.putExtras(wrap);

                    startActivity(ip);

                    break;
                case 2: // lancar a nova activity do tipo LIsta, e o seu
                    // conteudo
                    Intent ipm = new Intent(getApplicationContext(),
                            Teste_Palavras_Aluno.class);
                    ipm.putExtras(wrap);

                    startActivity(ipm);

                    break;
                case 3: // lancar a nova activity do tipo poema, e o seu
                    // conteudo
                    Intent ipp = new Intent(getApplicationContext(),
                            Teste_Poema.class);
                    ipp.putExtras(wrap);

                    startActivity(ipp);
                    break;
                default:
                    Toast.makeText(getApplicationContext(),
                            " - Tipo nao defenido", Toast.LENGTH_SHORT).show();
                    // retirar o teste errado e continuar

				/*
				 * int k = 0; Teste aux[] = new Teste[lista.length - 1]; for
				 * (int i = 1; i < lista.length; i++) { aux[k] = lista[i]; k++;
				 * } lista = aux; iniciar(lista.length);
				 */
                    break;
            }

        } else {// senao avisa que nao existe nada seleccionado
            android.app.AlertDialog alerta;
            // Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // define o titulo
            builder.setTitle("Letrinhas");
            // define a mensagem
            builder.setMessage("Nao existem testes seleccionados!");
            // define um botao como positivo
            builder.setPositiveButton("OK", null);
            // cria o AlertDialog
            alerta = builder.create();
            // Mostra
            alerta.show();
        }

    }

}
