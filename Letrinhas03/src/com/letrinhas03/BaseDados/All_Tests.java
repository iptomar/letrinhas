package com.letrinhas03.BaseDados;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.letrinhas03.R;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class All_Tests extends ListActivity {

    ////////////////////////////////////////////////////////////////////////////////
    // JSON Nome dos campos
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TESTS = "tests";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT = "textContent";
    private static final String TAG_PROF = "professorName";
    private static final String TAG_MAX_TRIES = "maxTries";
    // URL do Servidor  Nota: Recebido em baixo//////////////////////////////
    private static String url_all_tests = ""; ////
    /////Variaveis que vai guardar o ip e porta recebido da janela anterior/////
    public String ip;
    public String porta;
    // Criar o objecto JSON Parser
    JSONParser jParser = new JSONParser();
    //Array list de testes
    ArrayList<HashMap<String, String>> testsList;
    //Ficheiro JSONArray dos testes
    JSONArray tests = null;
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__tests);
        // Obtendo Detalhes dos Testes do intent
        Intent i = getIntent();
        // Obtendo CAMPO IP e PORTA enviados para esta Janela
        ip = i.getStringExtra("IP");
        porta = i.getStringExtra("PORTA");
        url_all_tests = "http://" + ip + ":" + porta + "/testSummary";
        // Hashmap para ListView
        testsList = new ArrayList<HashMap<String, String>>();

        // Executar  a thread em background para carregar uma lista de Testes
        new LoadAllTests().execute();
        // Get listview
        ListView lv = getListView();

        // Ao Clickar num Item da lista
        // Abre a janela de MoreInfo
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String idSelect = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();
                GetItemSelectRequest(idSelect);
            }
        });
    }


    /**
     * Faz o Request ao servidor Para obter os detalhes do teste selecionado
     */
    public void GetItemSelectRequest(String idSelect) {
        // Construi os Parametros
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // getting JSON string from URL
        String urlGetItem = "http://" + ip + ":" + porta + "/getTest?id=" + idSelect;

        JSONObject json = jParser.Get(urlGetItem, params);
        try {
            /// L� o campo sucesso para verificar se a mensagem chegou toda
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                // Obtendo um Array de os detalhes do Teste
                tests = json.getJSONArray(TAG_TESTS);

                JSONObject c = tests.getJSONObject(0);
                // Armazenar cada item json nas vari�veis
                String idItem = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String text = c.getString(TAG_TEXT);
                String profName = c.getString(TAG_PROF);
                String maxTries = c.getString(TAG_MAX_TRIES);

                Intent i = new Intent(getApplicationContext(), ViewMoreInfo.class);
                i.putExtra(TAG_TITLE, title);
                i.putExtra(TAG_TEXT, text);
                i.putExtra(TAG_PROF, profName);
                i.putExtra(TAG_MAX_TRIES, maxTries);
                startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            pDialog = new ProgressDialog(All_Tests.this);
            pDialog.setMessage("A Carregar Testes. Por Favor aguarde...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Faz em Background
         */
        protected String doInBackground(String... args) {
            // Construi os Parametros
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.Get(url_all_tests, params);
            try {
                // verifica a Tag SUCCESS, usada para verificar de a mensagem chegou a n�s como deve ser
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Testes encontrados
                    // Obtendo um Array de todos os Tests
                    tests = json.getJSONArray(TAG_TESTS);

                    // For (loop)looping atraves de todos os Testes
                    for (int i = 0; i < tests.length(); i++) {
                        JSONObject c = tests.getJSONObject(i);
                        // Armazenar cada item json nas vari�veis
                        String id = c.getString(TAG_ID);
                        String title = c.getString(TAG_TITLE);
                        // Criar um novo HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
                        // Adicionar cada NODE filho ao HashMap chave => valor
                        map.put(TAG_ID, id);
                        map.put(TAG_TITLE, title);
                        // Acrescentando HashList para ArrayList
                        testsList.add(map);
                    }
                } else {
                    // Sem testes Encontrados
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(All_Tests.this);
                    alertDialog.setMessage("Sem Registos");
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    /**
                     * Inserir parsed JSON para dentro da ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            All_Tests.this, testsList,
                            R.layout.list_item, new String[]{TAG_ID, TAG_TITLE},
                            new int[]{R.id.id, R.id.title});
                    // actualizar listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}
