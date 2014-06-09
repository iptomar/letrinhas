package com.letrinhas05.BaseDados;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import android.view.View;
import com.letrinhas05.ClassesObjs.*;
import com.letrinhas05.PaginaInicial;
import com.letrinhas05.util.Utils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 03/05/2014. ESTA CLASSE VAI FAZER A SINCRON DAS TABELAS EM
 * BACKGROUND
 */
public class SincAllBd extends AsyncTask<String, String, String> {

    public Context context;
    public String msg = "";
    private PaginaInicial mActivity;
    public int tipo, perct;

    public SincAllBd(Context context, PaginaInicial mActivity, int tipo) {
        this.context = context;
        this.mActivity = mActivity;
        mActivity.progBar.setProgress(5);
        this.tipo = tipo;
    }

    @Override
    protected String doInBackground(String[] strings) {

        if (tipo == 0) {
            mActivity.progBar.setMax(100);
            lerSynProfessores(strings[0]);
            mActivity.progBar.setProgress(10);

            lerSynEscolas(strings[0]);
            mActivity.progBar.setProgress(15);

            lerSynEstudante(strings[0]);
            mActivity.progBar.setProgress(20);

            lerSynTurmas(strings[0]);
            mActivity.progBar.setProgress(25);

            lerSynTurmasProfessor(strings[0]);
            mActivity.progBar.setProgress(30);

            lerSynTestes(strings[0]);
            mActivity.progBar.setProgress(50);

            lerSynTestesLista(strings[0]);
            mActivity.progBar.setProgress(60);

            lerSynTestesPoema(strings[0]);
            mActivity.progBar.setProgress(70);

            lerSynTestesMultimedia(strings[0]);
            mActivity.progBar.setProgress(80);

            lerSynCorrecaoLeitura(strings[0]);
            mActivity.progBar.setProgress(90);

            lerSynCorrecaoMultimedia(strings[0]);
            mActivity.progBar.setProgress(100);

        }
        else if (tipo == 1) {
            mActivity.txtViewMSG.setText("A Enviar ....");
            LetrinhasDB db = new LetrinhasDB(context);
            List<CorrecaoTesteLeitura> listCrtl =  db.getAllCorrecaoTesteLeitura();
            List<CorrecaoTesteMultimedia> listCrtM =  db.getAllCorrecaoTesteMultime();
            int prog = 1;
            int totalCampos = listCrtl.size() + listCrtM.size();
            mActivity.progBar.setMax(totalCampos);
            mActivity.progBar.setProgress(prog);
            if (listCrtl.size() != 0)
            for (CorrecaoTesteLeitura cn : listCrtl) {

                Log.e("CENAAAA" ,"Testid:"+ cn.getTestId()+"");
                Log.e("CENAAAA" ,"estudante:"+  cn.getIdEstudante()+"");
                Log.e("CENAAAA" ,"expressividad:"+  cn.getExpressividade()+"");
                Log.e("CENAAAA" ,"readingspeed:"+  cn.getVelocidade()+"");
                Log.e("CENAAAA" ,"*************************************");


                NetworkUtils.postResultados(strings[0], cn);
                mActivity.progBar.setProgress(prog);
                prog++;
            }
            if (listCrtM.size() != 0)
            for (CorrecaoTesteMultimedia tstM : listCrtM) {
                Log.e("CENAAAA" ,"Testid:"+ tstM.getTestId()+"");
                Log.e("CENAAAA" ,"estudante:"+  tstM.getIdEstudante()+"");
                Log.e("CENAAAA" ,"readingspeed:"+  tstM.getDataExecucao()+"");
                Log.e("CENAAAA" ,"*************************************");
                NetworkUtils.postResultados(strings[0], tstM);

                mActivity.progBar.setProgress(prog);
                prog++;
            }
        }
        else if (tipo == 2) {

            mActivity.progBar.setProgress(10);
            lerSynCorrecaoLeitura(strings[0]);
            mActivity.progBar.setProgress(50);
            lerSynCorrecaoMultimedia(strings[0]);
            mActivity.progBar.setProgress(100);

        }


        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        if (tipo == 0){
        if (msg.equals("")) {
            mActivity.txtViewMSG.setText("Sincronizacao Realizada Com Sucesso!!");
            mActivity.bentrar.setEnabled(true);
        } else {
            mActivity.txtViewMSG.setText("ERRO a Sinc: " + msg);
            mActivity.bentrar.setEnabled(false);
        }
        }
        else
        {
            mActivity.progBar.setVisibility(View.INVISIBLE);
            mActivity.txtViewMSG.setText("Enviado Correcoes para o Servidor!");
            mActivity.bentrar.setEnabled(true);
        }

    }

    /**
     * Vai por HTTP buscar toda a informacao sobre os Professor e no final chama
     * o metodo para guardar na base dados
     */
    protected void lerSynProfessores(String URlString) {

        String url = URlString + "Api/Professors/All";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {

            JSONArray profs = JSONParser.getJSONArray(url, params);
            Professor[] arrProf = new Professor[profs.length()];

            // For (loop)looping
            for (int i = 0; i < profs.length(); i++) {
                JSONObject c = profs.getJSONObject(i);
                // Armazenar cada item json nas vari�veis
                arrProf[i] = new Professor(c.getInt("id"),
                        c.getInt("schoolId"), c.getString("name"),
                        c.getString("username"), c.getString("password"),
                        c.getString("emailAddress"),
                        c.getString("telephoneNumber"),
                        NetworkUtils.getFile(URlString
                                + c.getString("photoUrl")),
                        c.getInt("isActive")
                );
            }
            guardarProfBD(arrProf);
        } catch (Exception e) {
            Log.d("ERRO", "ERRO DE SINC PROFS TALVEZ SERVIDOR EM BAIXO" + e.getMessage());
            msg = " Professores ||";
        }
    }

    /**
     * Vai por HTTP buscar toda a informacao sobre os escolas e no final chama o
     * metodo para guardar na base de dados
     */
    protected void lerSynEscolas(String URlString) {
        String url = URlString + "Api/Schools/All";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {

            JSONArray escola = JSONParser.getJSONArray(url, params);
            Escola[] arrEscolas = new Escola[escola.length()];

            // For (loop)looping
            for (int i = 0; i < escola.length(); i++) {
                JSONObject c = escola.getJSONObject(i);
                // Armazenar cada item json nas variaveis
                arrEscolas[i] = new Escola(c.getInt("id"),
                        c.getString("schoolName"),
                        NetworkUtils.getFile(URlString
                                + c.getString("schoolLogoUrl")),
                        c.getString("schoolAddress")
                );
            }
            guardarEscolaBD(arrEscolas);
        } catch (Exception e) {
            Log.d("ERRO", "ERRO DE SINC ESCOLAS TALVEZ SERVIDOR EM BAIXO");
            msg += " Escolas ||";
        }
    }

    /**
     * Vai por HTTP buscar toda a informacao sobre os estudantes e no final
     * chama o metodo para guardar na base de dados
     */
    protected void lerSynEstudante(String URlString) {
        String url = URlString + "Api/Students/All";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONArray alunos = JSONParser.getJSONArray(url, params);

        try {

            Estudante[] arrEstudantes = new Estudante[alunos.length()];

            // For (loop)looping
            for (int i = 0; i < alunos.length(); i++) {
                JSONObject c = alunos.getJSONObject(i);
                // Armazenar cada item json nas vari�veis
                arrEstudantes[i] = new Estudante(c.getInt("id"),
                        c.getInt("classId"), c.getString("name"),
                        NetworkUtils.getFile(URlString
                                + c.getString("photoUrl")),
                        c.getInt("isActive")
                );
            }
            guardarEstudantesBD(arrEstudantes);
        } catch (Exception e) {
            Log.d("ERRO", "ERRO DE SINC ESTUDANTES TALVEZ SERVIDOR EM BAIXO");
            msg += " Estudantes ||";
        }

    }

    /**
     * Vai por HTTP buscar toda a informacao sobre os estudantes e no final
     * chama o metodo para guardar na base de dados
     */
    protected void lerSynTurmas(String URlString) {
        String url = URlString + "Api/Classes/All";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONArray turmas = JSONParser.getJSONArray(url, params);
        try {
            Turma[] arrTurmas = new Turma[turmas.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < turmas.length(); i++) {
                JSONObject c = turmas.getJSONObject(i);
                // Armazenar cada item json nas variaveis
                arrTurmas[i] = new Turma(c.getInt("id"), c.getInt("schoolId"),
                        c.getInt("classLevel"), c.getString("className"),
                        c.getString("classYear"));
            }
            guardarTurmasBD(arrTurmas);
        } catch (Exception e) {
            Log.d("ERRO", "ERRO DE SINC TURMAS TALVEZ SERVIDOR EM BAIXO");
            msg += " Turmas ||";
        }
    }


    /**
     * Vai por HTTP buscar toda a informacao sobre os estudantes e no final
     * chama o metodo para guardar na base de dados
     */
    protected void lerSynTurmasProfessor(String URlString) {
        String url = URlString + "Api/Classes/Professors";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONArray turmaProf = JSONParser.getJSONArray(url, params);
        try {
            TurmaProfessor[] arrTurmaProfs = new TurmaProfessor[turmaProf.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < turmaProf.length(); i++) {
                TurmaProfessor turmaProfess = new TurmaProfessor();
                JSONObject c = turmaProf.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                turmaProfess.setIdTurma(c.getInt("classId"));
                turmaProfess.setIdProfessor(c.getInt("professorId"));
                arrTurmaProfs[i] = turmaProfess;
            }
            guardarTurmasProfessoresBD(arrTurmaProfs);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC TESTES TurmasProf TALVEZ SERVIDOR EM BAIXO");
            msg += " TurmasProf ||";
        }

    }


    /**
     * Vai por HTTP buscar toda a informacao sobre os TestesLeitura e no final
     * chama o metodo para guardar na base de dados
     */

    protected void lerSynTestes(String URlString) {
        String url = URlString + "Api/Tests/All?type=0"; // // type 0 -> leitura | 1 ->
        // multimédia | 2 -> lista
        // | 3 -> poema
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONArray tests = JSONParser.getJSONArray(url, params);
        try {
            TesteLeitura[] arrTestesLeitura = new TesteLeitura[tests.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < tests.length(); i++) {
                TesteLeitura testeleitura = new TesteLeitura();
                JSONObject c = tests.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                // ///////////////
                testeleitura.setIdTeste(c.getInt("id"));
                testeleitura.setAreaId(c.getInt("areaId"));
                testeleitura.setProfessorId(c.getInt("professorId"));
                testeleitura.setTitulo(c.getString("title"));
                testeleitura.setTexto(c.getString("mainText"));
                testeleitura.setDataInsercaoTeste(c.getLong("creationDate"));
                testeleitura.setGrauEscolar(c.getInt("grade"));
                testeleitura.setTipos(0);
                // //////////////////
                testeleitura.setConteudoTexto(c.getString("textContent"));
                testeleitura.setProfessorAudioUrl("SOM" + c.getInt("id")
                        + ".mp3");
                Utils.saveFileSD(
                        "ReadingTests",
                        "SOM" + c.getInt("id") + ".mp3",
                        NetworkUtils.getFile(URlString
                                + c.getString("professorAudioUrl"))
                );
                arrTestesLeitura[i] = testeleitura;
            }
            guardarTestesLeituraBD(arrTestesLeitura);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC TESTES LEITURA TALVEZ SERVIDOR EM BAIXO");
            msg += " TestesLeitura ||";
        }
    }


    protected void lerSynTestesLista(String URlString) {
        String url = URlString + "Api/Tests/All?type=2"; // // type 0 -> leitura | 1 ->
        // multimédia | 2 -> lista
        // | 3 -> poema
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONArray tests = JSONParser.getJSONArray(url, params);
        try {
            TesteLeitura[] arrTestesLeitura = new TesteLeitura[tests.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < tests.length(); i++) {
                TesteLeitura testeleitura = new TesteLeitura();
                JSONObject c = tests.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                // ///////////////
                testeleitura.setIdTeste(c.getInt("id"));
                testeleitura.setAreaId(c.getInt("areaId"));
                testeleitura.setProfessorId(c.getInt("professorId"));
                testeleitura.setTitulo(c.getString("title"));
                testeleitura.setTexto(c.getString("mainText"));
                testeleitura.setDataInsercaoTeste(c.getLong("creationDate"));
                testeleitura.setGrauEscolar(c.getInt("grade"));
                testeleitura.setTipos(2);
                // //////////////////
                testeleitura.setConteudoTexto(c.getString("textContent"));
                testeleitura.setProfessorAudioUrl("SOM" + c.getInt("id")
                        + ".mp3");
                Utils.saveFileSD(
                        "ReadingTests",
                        "SOM" + c.getInt("id") + ".mp3",
                        NetworkUtils.getFile(URlString
                                + c.getString("professorAudioUrl"))
                );
                arrTestesLeitura[i] = testeleitura;
            }
            guardarTestesLeituraListaBD(arrTestesLeitura);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC TESTES LEITURATesteLista TALVEZ SERVIDOR EM BAIXO");
            msg += " TestesLista ||";
        }
    }


    protected void lerSynTestesPoema(String URlString) {
        String url = URlString + "Api/Tests/All?type=3"; // // type 0 -> leitura | 1 ->
        // multimédia | 2 -> lista
        // | 3 -> poema
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONArray tests = JSONParser.getJSONArray(url, params);
        try {
            TesteLeitura[] arrTestesLeitura = new TesteLeitura[tests.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < tests.length(); i++) {
                TesteLeitura testeleitura = new TesteLeitura();
                JSONObject c = tests.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                // ///////////////
                testeleitura.setIdTeste(c.getInt("id"));
                testeleitura.setAreaId(c.getInt("areaId"));
                testeleitura.setProfessorId(c.getInt("professorId"));
                testeleitura.setTitulo(c.getString("title"));
                testeleitura.setTexto(c.getString("mainText"));
                testeleitura.setDataInsercaoTeste(c.getLong("creationDate"));
                testeleitura.setGrauEscolar(c.getInt("grade"));
                testeleitura.setTipos(3);
                // //////////////////
                testeleitura.setConteudoTexto(c.getString("textContent"));
                testeleitura.setProfessorAudioUrl("SOM" + c.getInt("id")
                        + ".mp3");
                Utils.saveFileSD(
                        "ReadingTests",
                        "SOM" + c.getInt("id") + ".mp3",
                        NetworkUtils.getFile(URlString
                                + c.getString("professorAudioUrl"))
                );
                arrTestesLeitura[i] = testeleitura;
            }
            guardarTestesPoemaBD(arrTestesLeitura);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC TESTES POEMA TALVEZ SERVIDOR EM BAIXO");
            msg += " TestesPoema ||";
        }
    }


    /**
     * Vai por HTTP buscar toda a informacao sobre os TestesMultimedia e no
     * final chama o metodo para guardar na base de dados
     */
    @SuppressWarnings("static-access")
    protected void lerSynTestesMultimedia(String URlString) {
        String url = URlString + "Api/Tests/All?type=1"; // // type 0 -> leitura | 1 ->
        // multimédia | 2 -> lista
        // | 3 -> poema
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONParser jParser = new JSONParser();
        JSONArray tests = jParser.getJSONArray(url, params);
        try {
            TesteMultimedia[] arrTestesMultimedia = new TesteMultimedia[tests
                    .length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < tests.length(); i++) {
                TesteMultimedia testeMultimedia = new TesteMultimedia();
                JSONObject c = tests.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                // ///////////////
                testeMultimedia.setIdTeste(c.getInt("id"));
                testeMultimedia.setAreaId(c.getInt("areaId"));
                testeMultimedia.setProfessorId(c.getInt("professorId"));
                testeMultimedia.setTitulo(c.getString("title"));
                testeMultimedia.setTexto(c.getString("mainText"));
                testeMultimedia.setDataInsercaoTeste(c.getLong("creationDate"));
                testeMultimedia.setGrauEscolar(c.getInt("grade"));
                testeMultimedia.setTipos(1);
                // /////////CONJUNTO DE IFS QUE VAI ANALISAR SE VAI GUARDAR
                // FICHEIROS NA SDCARD/////////
                testeMultimedia.setContentIsUrl(c.getInt("contentIsUrl"));

                if (c.getInt("contentIsUrl") == 1) {
                    testeMultimedia.setConteudoQuestao("IMG-CONTENT" + c.getInt("id") + ".jpg");
                    Utils.saveFileSD(
                            "MultimediaTest",
                            "IMG-CONTENT" + c.getInt("id") + ".jpg",
                            NetworkUtils.getFile(URlString
                                    + c.getString("questionContent"))
                    );
                } else
                    testeMultimedia.setConteudoQuestao(c
                            .getString("questionContent"));


                if (c.getInt("option1IsUrl") == 1) {
                    testeMultimedia.setOpcao1("IMG-OPTION1-"
                            + c.getInt("id") + ".jpg");
                    Utils.saveFileSD(
                            "MultimediaTest",
                            "IMG-OPTION1-" + c.getInt("id") + ".jpg",
                            NetworkUtils.getFile(URlString
                                    + c.getString("option1"))
                    );
                } else
                    testeMultimedia.setOpcao1(c.getString("option1"));

                if (c.getInt("option2IsUrl") == 1) {
                    testeMultimedia.setOpcao2("IMG-OPTION2-"
                            + c.getInt("id") + ".jpg");
                    Utils.saveFileSD(
                            "MultimediaTest",
                            "IMG-OPTION2-" + c.getInt("id") + ".jpg",
                            NetworkUtils.getFile(URlString
                                    + c.getString("option2"))
                    );
                } else
                    testeMultimedia.setOpcao2(c.getString("option2"));


                if (c.getInt("option3IsUrl") == 1) {
                    testeMultimedia.setOpcao3("IMG-OPTION3-"
                            + c.getInt("id") + ".jpg");
                    Utils.saveFileSD(
                            "MultimediaTest",
                            "IMG-OPTION3-" + c.getInt("id") + ".jpg",
                            NetworkUtils.getFile(URlString
                                    + c.getString("option3"))
                    );
                } else
                    testeMultimedia.setOpcao3(c.getString("option3"));


                testeMultimedia.setOpcao1IsUrl(c.getInt("option1IsUrl"));
                testeMultimedia.setOpcao2IsUrl(c.getInt("option2IsUrl"));
                testeMultimedia.setOpcao3IsUrl(c.getInt("option3IsUrl"));
                testeMultimedia.setCorrectOption(c.getInt("correctOption"));
                arrTestesMultimedia[i] = testeMultimedia;
            }
            guardarTestesMultimediaBD(arrTestesMultimedia);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC TESTESMULTIMEDIA TALVEZ SERVIDOR EM BAIXO");
            msg += " TestesMultimedia ||";
        }
    }



    /**
     * Vai por HTTP buscar toda a informacao sobre os CorrecaoTestesLeitura e no final
     * chama o metodo para guardar na base de dados
     */
    protected void lerSynCorrecaoLeitura(String URlString) {
        String url = URlString + "Api/Tests/Submissions/Read";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONArray tests = JSONParser.getJSONArray(url, params);
        try {
            CorrecaoTesteLeitura[] arrCorrTestesLeitura = new CorrecaoTesteLeitura[tests.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < tests.length(); i++) {
                CorrecaoTesteLeitura corretesteleitura = new CorrecaoTesteLeitura();
                JSONObject c = tests.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                String aux = c.getInt("testId") +""+ c.getInt("studentId") +""+ c.getLong("executionDate") + "";

                corretesteleitura.setIdCorrrecao(Long.parseLong(aux));
                corretesteleitura.setTestId(c.getInt("testId"));
                corretesteleitura.setIdEstudante(c.getInt("studentId"));
                corretesteleitura.setDataExecucao(c.getLong("executionDate"));
                corretesteleitura.setTipo(c.getInt("type"));

                corretesteleitura.setObservacoes(c.getString("professorObservations"));
                corretesteleitura.setNumPalavrasMin(Float.parseFloat(c.getString("wordsPerMinute")));
                corretesteleitura.setNumPalavCorretas(c.getInt("correctWordCount"));
                corretesteleitura.setNumPalavIncorretas(c.getInt("testId"));

                corretesteleitura.setPrecisao(Float.parseFloat(c.getString("readingPrecision")));
                corretesteleitura.setExpressividade(c.getInt("expressiveness"));
                corretesteleitura.setRitmo(c.getInt("rhythm"));
                corretesteleitura.setDetalhes(c.getString("details"));
                corretesteleitura.setEstado(c.getInt("wasCorrected"));


                //Guardar ficheiro
                corretesteleitura.setAudiourl("/School-Data/CorrectionReadTest/"+c.getInt("testId")+"/"+c.getInt("studentId")+"/"+c.getLong("executionDate")+".3gpp");
                Utils.saveFileSDCorre(
                        "/School-Data/CorrectionReadTest/"+c.getInt("testId")+"/"+c.getInt("studentId")+"/",
                        c.getLong("executionDate")+".3gpp",
                        NetworkUtils.getFile(URlString+ c.getString("soundFileUrl")));


                arrCorrTestesLeitura[i] = corretesteleitura;
            }
           guardarCorrecaoTesteLeituraBD(arrCorrTestesLeitura);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC Correcao TESTES LEITURA TALVEZ SERVIDOR EM BAIXO");
            msg += " CorrecaoTestesLeitura ||";
        }
    }




    /**
     * Vai por HTTP buscar toda a informacao sobre os CorrecaoTestesmultimedia e no final
     * chama o metodo para guardar na base de dados
     */
    protected void lerSynCorrecaoMultimedia(String URlString) {
        String url = URlString + "Api/Tests/Submissions/Multimedia";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        LetrinhasDB db = new LetrinhasDB(context);
        JSONArray tests = JSONParser.getJSONArray(url, params);
        try {
            CorrecaoTesteMultimedia[] arrCorrTestesMulti = new CorrecaoTesteMultimedia[tests.length()];
            // For (loop)looping atraves de todos os Testes
            for (int i = 0; i < tests.length(); i++) {
                CorrecaoTesteMultimedia corretesteMult = new CorrecaoTesteMultimedia();
                JSONObject c = tests.getJSONObject(i);
                // /////// Preencher um objecto do tipo teste com a informaçao
                String aux = c.getInt("testId") +""+ c.getInt("studentId") +""+ c.getLong("executionDate") + "";

                corretesteMult.setIdCorrrecao(Long.parseLong(aux));
                corretesteMult.setTestId(c.getInt("testId"));
                corretesteMult.setIdEstudante(c.getInt("studentId"));
                corretesteMult.setDataExecucao(c.getLong("executionDate"));
                corretesteMult.setEstado(1);
                corretesteMult.setTipo(1);

                corretesteMult.setOpcaoEscolhida(c.getInt("optionChosen"));
                corretesteMult.setOpcaoEscolhida(db.getTesteMultimediaById(c.getInt("testId")).getCorrectOption());
                arrCorrTestesMulti[i] = corretesteMult;
            }
            guardarCorrecaoTesteMultimediaBD(arrCorrTestesMulti);
        } catch (Exception e) {
            Log.d("ERRO",
                    "ERRO DE SINC Correcao TESTES Multimedia TALVEZ SERVIDOR EM BAIXO");
            msg += " CorrecaoTestesMultimedia ||";
        }
    }




















    /**
     * Guarda um array de ObJECTOS professores na Base de dados
     *
     * @param profs Array com Professores para se guardar
     */
    public void guardarProfBD(Professor[] profs) {
        LetrinhasDB db = new LetrinhasDB(context);
        db.deleteAllItemsProf();
        Log.d("DB", "Inserir Dados na base de dados dos Professores ..");
        for (int i = 0; i < profs.length; i++) {
            db.addNewItemProf(profs[i]);
        }
        Log.d("DB", "Tudo inserido nos Professores");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Professor> dadosImg = db.getAllProfesors();
        Log.d("BDDADOS: ", "***********PROFESSORES******************");
        for (Professor cn : dadosImg) {
            String logs = "Id: " + cn.getId() + ", idEscola: "
                    + cn.getIdEscola() + "  , nome: " + cn.getNome()
                    + "  , username: " + cn.getUsername() + "  , password: "
                    + cn.getPassword() + "  , telefone: " + cn.getTelefone()
                    + "  , email: " + cn.getEmail() + "  , foto: "
                    + cn.getFotoNome();
            // Writing Contacts to log
            Log.d("BDDADOS: ", logs);
        }
    }

    /**
     * Guarda um array de ObJECTOS escola na Base de dados
     *
     * @param escolas Array com escolas para se guardar
     */
    public void guardarEscolaBD(Escola... escolas) {
        LetrinhasDB db = new LetrinhasDB(context);
        db.deleteAllItemsEscola();
        Log.d("DB", "Inserir Dados na base de dados das Escolas ..");
        for (int i = 0; i < escolas.length; i++) {
            db.addNewItemEscolas(escolas[i]);
        }
        Log.d("DB", "Tudo inserido nas Escolas");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Escola> dadosImg = db.getAllSchools();
        Log.d("BDDADOS: ", "********ESCOLAS********************");
        for (Escola cn : dadosImg) {
            String logs = "Id: " + cn.getIdEscola() + ", nome: " + cn.getNome()
                    + ", logotipo: " + cn.getLogotipoNome() + ", morada: "
                    + cn.getMorada();
            // Writing Contacts to log
            Log.d("BDDADOS: ", logs);
        }
    }

    /**
     * Guarda um array de ObJECTOS escola na Base de dados
     *
     * @param turmas Array com escolas para se guardar
     */
    public void guardarTurmasBD(Turma... turmas) {
        LetrinhasDB db = new LetrinhasDB(context);
        db.deleteAllItemsTurmas();
        Log.d("DB", "Inserir Dados na base de dados das Turmas ..");
        for (int i = 0; i < turmas.length; i++) {
            db.addNewItemTurmas(turmas[i]);
        }
        Log.d("DB", "Tudo inserido nas Turmas");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Turma> dadosImg = db.getAllTurmas();
        Log.d("BDDADOS: ", "********TURMAS********************");
        for (Turma cn : dadosImg) {
            String logs = "Id: " + cn.getId() + ", getIdEscola: "
                    + cn.getIdEscola() + ", getAnoEscolar: "
                    + cn.getAnoEscolar() + ", getNome: " + cn.getNome()
                    + ", getNome: " + cn.getAnoLetivo();
            // Writing Contacts to log
            Log.d("BDDADOS: ", logs);
        }
    }

    /**
     * Guarda um array de ObJECTOS estudantes na Base de dados
     *
     * @param estudantes Array com estudantres para se guardar
     */
    public void guardarEstudantesBD(Estudante... estudantes) {
        LetrinhasDB db = new LetrinhasDB(context);
        db.deleteAllItemsEstudante();
        Log.d("DB", "Inserir Dados na base de dados dos Estudantes ..");
        for (int i = 0; i < estudantes.length; i++) {
            db.addNewItemEstudante(estudantes[i]);
        }
        Log.d("DB", "Tudo inserido nas Estudantes");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Estudante> dados = db.getAllStudents();
        Log.d("BDDADOS: ", "*********Estudantes********************");
        for (Estudante cn : dados) {
            String logs = "IdEstudante:" + cn.getIdEstudante() + ", IdTurma: "
                    + cn.getIdTurma() + ", nome: " + cn.getNome()
                    + "  , foto: " + cn.getNomefoto() + "  , estado: "
                    + cn.getEstado();
            Log.d("BDDADOS: ", logs);
        }
    }

    /**
     * Guarda um array de ObJECTOS testesLeitura na Base de dados
     *
     * @param testeLeitura Array com testesLeitura para se guardar
     */
    public void guardarTestesLeituraBD(TesteLeitura... testeLeitura) {
        LetrinhasDB db = new LetrinhasDB(context);
        db.deleteAllItemsTests();
        db.deleteAllItemsTestsLeitura();
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < testeLeitura.length; i++) {
            db.addNewItemTestesLeitura(testeLeitura[i]);
        }
        Log.d("DB", "Tudo inserido nas Testes");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Teste> dados = db.getAllTeste();
        Log.d("BDDADOS: ",
                "*********Tabela Testes---- + testes Leitura********************");
        for (Teste cn : dados) {
            String logs = "getIdTeste:   " + cn.getIdTeste() + ",getTitulo:   "
                    + cn.getTitulo() + ",getTexto:    " + cn.getTexto()
                    + ", getDataInsercaoTeste:    " + cn.getDataInsercaoTeste()
                    + ", getGrauEscolar:    " + cn.getGrauEscolar()
                    + ", getTipo:    " + cn.getTipo();

            // Writing Contacts to log

            Log.d("BDDADOS: ", logs);
        }
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<TesteLeitura> dados2 = db.getAllTesteLeitura();
        Log.d("BDDADOS: ", "\n*********testesleitura********************");
        for (TesteLeitura cn : dados2) {
            String cenas = "getIdTeste:" + cn.getIdTeste()
                    + ", getConteudoTexto: " + cn.getConteudoTexto()
                    + ", getProfessorAudioUrl: " + cn.getProfessorAudioUrl();
            // Writing Contacts to log
            Log.d("BDDADOS: ", cenas);
        }
    }


    /**
     * Guarda um array de ObJECTOS testesLeitura na Base de dados
     *
     * @param testeLeitura Array com testesLeitura para se guardar
     */
    public void guardarTestesLeituraListaBD(TesteLeitura... testeLeitura) {
        LetrinhasDB db = new LetrinhasDB(context);
        //   db.deleteAllItemsTests();
        // db.deleteAllItemsTestsLeitura();
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < testeLeitura.length; i++) {
            db.addNewItemTestesLeitura(testeLeitura[i]);
        }
        Log.d("DB", "Tudo inserido nas Testes");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Teste> dados = db.getAllTeste();
        Log.d("BDDADOS: ",
                "*********Tabela Testes---- + testes Leitura LISTAS********************");
        for (Teste cn : dados) {
            String logs = "getIdTeste:   " + cn.getIdTeste() + ",getTitulo:   "
                    + cn.getTitulo() + ",getTexto:    " + cn.getTexto()
                    + ", getDataInsercaoTeste:    " + cn.getDataInsercaoTeste()
                    + ", getGrauEscolar:    " + cn.getGrauEscolar()
                    + ", getTipo:    " + cn.getTipo();

            // Writing Contacts to log

            Log.d("BDDADOS: ", logs);
        }
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<TesteLeitura> dados2 = db.getAllTesteLeitura();
        Log.d("BDDADOS: ", "\n*********testesleitura********************");
        for (TesteLeitura cn : dados2) {
            String cenas = "getIdTeste:" + cn.getIdTeste()
                    + ", getConteudoTexto: " + cn.getConteudoTexto()
                    + ", getProfessorAudioUrl: " + cn.getProfessorAudioUrl();
            // Writing Contacts to log
            Log.d("BDDADOS: ", cenas);
        }
    }

    /**
     * Guarda um array de ObJECTOS testesLeitura na Base de dados
     *
     * @param testeLeitura Array com testesLeitura para se guardar
     */
    public void guardarTestesPoemaBD(TesteLeitura... testeLeitura) {
        LetrinhasDB db = new LetrinhasDB(context);
        //   db.deleteAllItemsTests();
        // db.deleteAllItemsTestsLeitura();
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < testeLeitura.length; i++) {
            db.addNewItemTestesLeitura(testeLeitura[i]);
        }
        Log.d("DB", "Tudo inserido nas Testes");
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Teste> dados = db.getAllTeste();
        Log.d("BDDADOS: ",
                "*********Tabela Testes---- + testes Leitura POEMAS********************");
        for (Teste cn : dados) {
            String logs = "getIdTeste:   " + cn.getIdTeste() + ",getTitulo:   "
                    + cn.getTitulo() + ",getTexto:    " + cn.getTexto()
                    + ", getDataInsercaoTeste:    " + cn.getDataInsercaoTeste()
                    + ", getGrauEscolar:    " + cn.getGrauEscolar()
                    + ", getTipo:    " + cn.getTipo();

            // Writing Contacts to log

            Log.d("BDDADOS: ", logs);
        }
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<TesteLeitura> dados2 = db.getAllTesteLeitura();
        Log.d("BDDADOS: ", "\n*********testesPOEMA********************");
        for (TesteLeitura cn : dados2) {
            String cenas = "getIdTeste:" + cn.getIdTeste()
                    + ", getConteudoTexto: " + cn.getConteudoTexto()
                    + ", getProfessorAudioUrl: " + cn.getProfessorAudioUrl();
            // Writing Contacts to log
            Log.d("BDDADOS: ", cenas);
        }
    }



    /**
     * Guarda um array de ObJECTOS TurmasProfessores na Base de dados
     *
     * @param turmaProfessors Array com testesLeitura para se guardar
     */
    public void guardarTurmasProfessoresBD(TurmaProfessor... turmaProfessors) {
        LetrinhasDB db = new LetrinhasDB(context);
        db.deleteAllItemsTurmasProfessor();
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < turmaProfessors.length; i++) {
            db.addNewItemTurmasProfessor(turmaProfessors[i]);
        }
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<TurmaProfessor> dados = db.getAllTurmasProfessores();
        Log.d("BDDADOS: ",
                "*********TURMAS-PROFESSORES********************");
        for (TurmaProfessor cn : dados) {
            String logs = "getIdTurma:   " + cn.getIdTurma() + ",getIdProfessor:   "
                    + cn.getIdProfessor();
            // Writing Contacts to log
            Log.d("BDDADOS: ", logs);
        }
    }


    /**
     * Guarda um array de ObJECTOS testesMultimedia na Base de dados
     *
     * @param testeMultimedias Array com testesMultimedia para se guardar
     */
    public void guardarTestesMultimediaBD(TesteMultimedia... testeMultimedias) {
        LetrinhasDB db = new LetrinhasDB(context);
        // db.deleteAllItemsTests();
        db.deleteAllItemsTestsMultimedia();
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < testeMultimedias.length; i++) {
            db.addNewItemTestesMultimedia(testeMultimedias[i]);
        }
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<Teste> dados = db.getAllTeste();
        Log.d("BDDADOS: ",
                "********* Tabela Testes---- + testes multimedia********************");
        for (Teste cn : dados) {
            String logs = "getIdTeste:   " + cn.getIdTeste() + ",getTitulo:   "
                    + cn.getTitulo() + ",getTexto:    " + cn.getTexto()
                    + ", getDataInsercaoTeste:    " + cn.getDataInsercaoTeste()
                    + ", getGrauEscolar:    " + cn.getGrauEscolar()
                    + ", getTipo:    " + cn.getTipo();

            // Writing Contacts to log

            Log.d("BDDADOS: ", logs);
        }
        // ///PARA EFEITOS DE DEBUG E LOGO O CODIGO A FRENTE APENAS MOSTRA O
        // CONTEUDO DA TABELA//////////////
        List<TesteMultimedia> dados2 = db.getAllTesteMultimedia();
        Log.d("BDDADOS: ", "\n*********testesMultimeida********************");
        for (TesteMultimedia cn : dados2) {
            String cenas = "getIdTeste:" + cn.getIdTeste()
                    + ", getConteudoQuestao: " + cn.getConteudoQuestao()
                    + ", getContentIsUrl: " + cn.getContentIsUrl() + ", " + ", getOpcao1: "
                    + cn.getOpcao1() + ", getOpcao1IsUrl: "
                    + cn.getOpcao1IsUrl() + ", getOpcao2: " + cn.getOpcao2()
                    + ", getOpcao2IsUrl: " + cn.getOpcao2IsUrl()
                    + ", getOpcao3: " + cn.getOpcao3() + ", getOpcao3IsUrl: "
                    + cn.getOpcao3IsUrl() + ", getCorrectOption: "
                    + cn.getCorrectOption();
            // Writing Contacts to log
            Log.d("BDDADOS: ", cenas);
        }

}


    /**
     * Guarda um array de ObJECTOS CorrecaotestesLeitura na Base de dados
     *
     * @param correcaoTesteLeituras Array com testesLeitura para se guardar
     */
    public void guardarCorrecaoTesteLeituraBD(CorrecaoTesteLeitura... correcaoTesteLeituras) {
        LetrinhasDB db = new LetrinhasDB(context);
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < correcaoTesteLeituras.length; i++) {

            if (!db.isExistsCorrecaoTest(correcaoTesteLeituras[i].getTestId(), correcaoTesteLeituras[i].getIdEstudante(), correcaoTesteLeituras[i].getDataExecucao()))
            {
                Log.e("DB", "VAI FAZER INSERT CorrecaoTesteLeitura");
                db.addNewItemCorrecaoTesteLeitura(correcaoTesteLeituras[i]);
            }
            else
            {
                Log.e("DB", "VAI FAZER UPDATE CorrecaoTesteLeitura");
                int estadoExistente =  db.getCorrecaoTesteLeirutaById(correcaoTesteLeituras[i].getTestId()).getEstado();
                int estadoNovo = correcaoTesteLeituras[i].getEstado();
                if (estadoNovo == estadoExistente)
                db.updateCorrecaoTesteLeituraPorObjecto(correcaoTesteLeituras[i]);
            }
            }

        Log.d("DB", "Tudo inserido nas CorrecaoLeitura");
         }



    /**
     * Guarda um array de ObJECTOS CorrecaotestesLeitura na Base de dados
     *
     * @param correcaoTesteMultimedias Array com testesLeitura para se guardar
     */
    public void guardarCorrecaoTesteMultimediaBD(CorrecaoTesteMultimedia... correcaoTesteMultimedias) {
        LetrinhasDB db = new LetrinhasDB(context);
        Log.d("DB", "Inserir Dados na base de dados dos TESTES ..");
        for (int i = 0; i < correcaoTesteMultimedias.length; i++) {

            if (!db.isExistsCorrecaoTest(correcaoTesteMultimedias[i].getTestId(), correcaoTesteMultimedias[i].getIdEstudante(), correcaoTesteMultimedias[i].getDataExecucao()))
            {
                Log.e("DB", "VAI FAZER INSERT CorrecaoTesteMultimedia");
                db.addNewItemCorrecaoTesteMultimedia(correcaoTesteMultimedias[i]);
            }
            else
            {
                Log.e("DB", "VAI FAZER UPDATE CorrecaoTesteMultimedia");
                db.updateCorrecaoTesteMultimediaPorObjecto(correcaoTesteMultimedias[i]);
            }
        }

        Log.d("DB", "Tudo inserido nas CorrecaoMultimedia");
    }
}

