package com.letrinhas04.BaseDados;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.letrinhas03.R;
import com.letrinhas04.ClassesObjs.Escola;
import com.letrinhas04.ClassesObjs.Estudante;
import com.letrinhas04.ClassesObjs.Professor;

public class MainBD extends Activity {

    /////Variaveis que vai guardar o ip e porta recebido da janela anterior/////
    public String ip;
    public String porta;
    public String URlString;
    public String log;
    // JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bd);
        // Obtendo Detalhes dos Testes do intent
        Intent i = getIntent();
        // Obtendo CAMPO IP e PORTA enviados para esta Janela
        ip = i.getStringExtra("IP");
        porta = i.getStringExtra("PORTA");
        URlString = "http://" + ip + ":" + porta + "/";

        Button btnBD = (Button) findViewById(R.id.btnBdReceber);
        Button btnSinc = (Button) findViewById(R.id.bntSinc);



        // view products click event
        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReceberDados().execute();
            }
        });

        btnSinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), All_Tests.class);
                //Enviar IP e Porta para outra janela
                i.putExtra("IP", ip);
                i.putExtra("PORTA", porta);
                startActivity(i);


            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_bd, menu);
        return true;
    }

    /**
     *  Guarda um array de  ObJECTOS professores na Base de dados
     * @param profs Array com  Professores para se guardar
     */
    public void guardarProfBD(Professor[] profs) {
        LetrinhasDB db = new LetrinhasDB(this);
        db.deleteAllItemsProf();
        Log.d("DB", "Inserir Dados na base de dados dos Professores ..");
        for (int i = 0; i < profs.length; i++) {
            db.addNewItemProf(profs[i]);
        }
        db.close();
        Log.d("DB", "Tudo inserido nos Professores");
        /////PARA EFEITOS DE DEBUG E LOGO  O CODIGO A FRENTE APENAS MOSTRA O CONTEUDO DA TABELA//////////////
        List<Professor> dadosImg = db.getAllProfesors();
        Log.d("BDDADOS: ", "***********PROFESSORES******************");
        for (Professor cn : dadosImg) {
            String logs = "Id: " + cn.getId() +
                    ", idEscola: " + cn.getIdEscola() +
                    "  , nome: " + cn.getNome() +
                    "  , username: " + cn.getUsername() +
                    "  , password: " + cn.getPassword() +
                    "  , telefone: " + cn.getTelefone() +
                    "  , email: " + cn.getEmail() +
                    "  , foto: " + cn.getFoto();
            log += "\n" + logs;
            // Writing Contacts to log
            Log.d("BDDADOS: ", logs);
        }

    }

    /**
     *  Guarda um array de  ObJECTOS escola na Base de dados
     * @param escolas Array com  escolas para se guardar
     */
    public void guardarEscolaBD(Escola... escolas) {
        LetrinhasDB db = new LetrinhasDB(this);
        db.deleteAllItemsEscola();
        Log.d("DB", "Inserir Dados na base de dados das Escolas ..");
        for (int i = 0; i < escolas.length; i++) {
            db.addNewItemEscolas(escolas[i]);
        }
        db.close();
        Log.d("DB", "Tudo inserido nas Escolas");
        /////PARA EFEITOS DE DEBUG E LOGO  O CODIGO A FRENTE APENAS MOSTRA O CONTEUDO DA TABELA//////////////
        List<Escola> dadosImg = db.getAllSchools();
        Log.d("BDDADOS: ", "********ESCOLAS********************");
        for (Escola cn : dadosImg) {
            String logs = "Id: " + cn.getIdEscola() +
                    ", nome: " + cn.getNome() +
                    ", logotipo: " + cn.getLogotipo() +
                    ", morada: " + cn.getMorada();
            log += "\n" + logs;
            // Writing Contacts to log
            Log.d("BDDADOS: ", logs);
        }
    }

    /**
     *  Guarda um array de  ObJECTOS estudantes na Base de dados
     * @param estudantes Array com  estudantres para se guardar
     */
    public void guardarEstudantesBD(Estudante... estudantes) {
        LetrinhasDB db = new LetrinhasDB(this);
        db.deleteAllItemsEstudante();
        Log.d("DB", "Inserir Dados na base de dados dos Estudantes ..");
        for (int i = 0; i < estudantes.length; i++) {
            db.addNewItemEstudante(estudantes[i]);
        }
        db.close();
        Log.d("DB", "Tudo inserido nas Estudantes");
        /////PARA EFEITOS DE DEBUG E LOGO  O CODIGO A FRENTE APENAS MOSTRA O CONTEUDO DA TABELA//////////////
        List<Estudante> dados = db.getAllStudents();
        Log.d("BDDADOS: ", "*********Estudantes********************");
        for (Estudante cn : dados) {
            String logs = "IdEstudante:" + cn.getIdEstudante() +
                    ", IdTurma: " + cn.getIdTurma() +
                    ", nome: " + cn.getNome() +
                    "  , foto: " + cn.getFoto() +
                    "  , estado: " + cn.getEstado();

            // Writing Contacts to log
            log += "\n" + logs;
            Log.d("BDDADOS: ", logs);

        }
    }

    class ReceberDados extends AsyncTask<String, String, String> {
        protected static final String LETRINHAS_APP_TAG = "letrinhas-app";

        /**
         * Antes de iniciar a Thread Background aparece a progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainBD.this);
            pDialog.setMessage("Por Favor aguarde...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            lerSynProfessores();
            lerSynEscolas();
            lerSynEstudante();
            return null;
        }

        /**
         *  Vai por HTTP buscar toda a informacao sobre os Professor e no final
         *  chama  o metodo para guardar na base dados
         *
         */
        protected void lerSynProfessores() {

            String url = URlString + "professors/";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // JSONObject json = jParser.getJSONObject(url, params);
            JSONObject json = JSONParser.getJSONObject(url, params);

            try {

                JSONArray profs = json.getJSONArray("professors");
                Professor[] arrProf = new Professor[profs.length()];

                // For (loop)looping
                for (int i = 0; i < profs.length(); i++) {
                    JSONObject c = profs.getJSONObject(i);
                    // Armazenar cada item json nas vari�veis
                    arrProf[i] = new Professor(
                            c.getInt("id"),
                            c.getInt("schoolId"), c.getString("name"),
                            c.getString("username"), c.getString("password"),
                            c.getString("emailAddress"), c.getString("telephoneNumber"),
                            NetworkUtils.getFile(URlString + c.getString("photoUrl")),
                            c.getInt("isActive")
                    );
                }
                guardarProfBD(arrProf);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         *  Vai por HTTP buscar toda a informacao sobre os escolas e no final
         *  chama  o metodo para guardar na base de dados
         *
         */
        protected void lerSynEscolas() {
            String url = URlString + "schools/";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = JSONParser.getJSONObject(url, params);

            try {

                JSONArray escola = json.getJSONArray("schools");
                Escola[] arrEscolas = new Escola[escola.length()];

                // For (loop)looping
                for (int i = 0; i < escola.length(); i++) {
                    JSONObject c = escola.getJSONObject(i);
                    // Armazenar cada item json nas variaveis
                    arrEscolas[i] = new Escola(
                            c.getInt("id"),
                            c.getString("schoolName"),
                            NetworkUtils.getFile(URlString + c.getString("schoolLogoUrl")),
                            c.getString("schoolAddress")
                    );
                }
                guardarEscolaBD(arrEscolas);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        /**
         *  Vai por HTTP buscar toda a informacao sobre os estudantes e no final
         *  chama  o metodo para guardar na base de dados
         *
         */
        protected void lerSynEstudante() {
            String url = URlString + "students/";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = JSONParser.getJSONObject(url, params);

            try {

                JSONArray estudante = json.getJSONArray("students");
                Estudante[] arrEstudantes = new Estudante[estudante.length()];

                // For (loop)looping
                for (int i = 0; i < estudante.length(); i++) {
                    JSONObject c = estudante.getJSONObject(i);
                    // Armazenar cada item json nas vari�veis
                    arrEstudantes[i] = new Estudante(
                            c.getInt("id"),
                            c.getInt("classId"),
                            c.getString("name"),
                            NetworkUtils.getFile(URlString + c.getString("photoUrl")),
                            c.getInt("isActive")
                    );
                }
                guardarEstudantesBD(arrEstudantes);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        /**
         * Depois de completar tarefa de background Fechar a Progress Dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // fechar a janela de Progress Dialog depois de receber todos os
            // Tests
            pDialog.dismiss();
            // Actualizar a UI a partir da Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                 /////////nao faz nada ////
                }
            });

        }
    }

    }
