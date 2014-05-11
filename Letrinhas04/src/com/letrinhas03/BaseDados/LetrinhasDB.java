/////////////////////////////////////////////////////////////////////////
///// ESTA CLASS É A CLASS QUE GERE A BASE DE  DADOS, CRIA A BASE DE////
/////DADOS E CONTEM METODOS PARA GERIR AS VARIAS TABELAS           ////
//////////////////////////////////////////////////////////////////////


package com.letrinhas03.BaseDados;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.letrinhas03.ClassesObjs.*;
import com.letrinhas03.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class LetrinhasDB extends SQLiteOpenHelper {

    // Versao da base de dados
    private static final int VERSAO_BASEDADOS = 1;

    // Nome da Base  de dados
    private static final String NOME_BASEDADOS = "letrinhasDb";
    // Nome da tabela da Base de dados
    private static final String TABELA_PROFESSORES = "tblProfessores";
    private static final String TABELA_ESCOLAS = "tblEscolas";
    private static final String TABELA_ESTUDANTE = "tblEstudantes";
    private static final String TABELA_SISTEMA = "tblSistema";
    private static final String TABELA_TESTE = "tblTeste";
    private static final String TABELA_TESTELEITURA = "tblTesteLeitura";
    private static final String TABELA_TESTEMULTIMEDIA = "tblTesteMultimedia";

    // Nomes dos campos da tabela Professores
    private static final String PROF_IDPROFS = "idProfessor";
    private static final String PROF_IDESCOLA = "iDescola";
    private static final String PROF_NOME = "nome";
    private static final String PROF_USERNAME = "username";
    private static final String PROF_PASSWORD = "password";
    private static final String PROF_TELEFONE = "telefone";
    private static final String PROF_EMAIL = "email";
    private static final String PROF_FOTO = "foto";
    private static final String PROF_ESTADO = "estadoAtividade";

    // Nomes dos campos da tabela Escolas
    private static final String ESC_IDESCOLA = "idEscola";
    private static final String ESC_NOME = "nome";
    private static final String ESC_LOGOTIPO = "logotipo";
    private static final String ESC_MORADA = "morada";

    // Nomes dos campos da tabela Estudantes
    private static final String EST_ID = "id";
    private static final String EST_IDTURMA = "idTurma";
    private static final String EST_NOME = "nome";
    private static final String EST_FOTO = "foto";
    private static final String EST_ESTADO = "estado";

    // Nomes dos campos da tabela Sistema - esta tabela serve para guardar configuraçoes do sistema
    private static final String SIS_ID = "id";
    private static final String SIS_NOME = "nome";
    private static final String SIS_VALOR = "valor";

    // Nomes dos campos da tabela Testes
    private static final String TEST_ID = "idTeste";
    private static final String TEST_AREAID = "areaId";
    private static final String TEST_PROFESSORID = "professorId";
    private static final String TEST_TITULO = "titulo";
    private static final String TEST_TEXTO = "texto";
    private static final String TEST_DATAINSERCAO = "dataInsercaoTeste";
    private static final String TEST_GRAU = "grauEscolar";
    private static final String TEST_TIPO= "tipo";

    // Nomes dos campos da tabela TestesLeitura
    private static final String TESTL_ID = "idTeste";
    private static final String TESTL_TEXTO = "texto";
    private static final String TESTL_SOMPROFESSOR = "somProfessor";

    // Nomes dos campos da tabela TestesMultimedia
    private static final String TESTM_ID = "idTeste";
    private static final String TESTM_CONTEUDOQUESTAO = "conteudoQuestao";
    private static final String TESTM_CONTEUDOISURL= "conteudoIsUrl";
    private static final String TESTM_OPCAO1= "opcao1";
    private static final String TESTM_OPCAO1ISURL= "opcao1IsUrl";
    private static final String TESTM_OPCAO2= "opcao2";
    private static final String TESTM_OPCAO2ISURL= "opcao2IsUrl";
    private static final String TESTM_OPCAO3= "opcao3";
    private static final String TESTM_OPCAO3ISURL= "opcao3IsUrl";
    private static final String TESTM_OPCAOCORRETA= "opcaoCorreta";

    public LetrinhasDB(Context context) {
        super(context, NOME_BASEDADOS, null, VERSAO_BASEDADOS);
    }

    /**
     * Criar Tabela Professores
     * @db recebe a base de dados onde inserir a tabela
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("db", "A criar tabela " + TABELA_PROFESSORES);
        /// Construir a Tabela Professores
        String createTableString = "CREATE TABLE " + TABELA_PROFESSORES + "("
                + PROF_IDPROFS + " INTEGER PRIMARY KEY,"
                + PROF_IDESCOLA + " INTEGER,"
                + PROF_NOME + " TEXT, "
                + PROF_USERNAME + " TEXT, "
                + PROF_PASSWORD + " TEXT, "
                + PROF_TELEFONE + " TEXT, "
                + PROF_EMAIL + " TEXT, "
                + PROF_FOTO + " TEXT, "
                + PROF_ESTADO + " INTEGER )";
        db.execSQL(createTableString);
        Log.d("db", "A criar tabela " + TABELA_ESCOLAS);

        //////// Construir a Tabela Escolas //////////////////
        createTableString = "CREATE TABLE " + TABELA_ESCOLAS + "("
                + ESC_IDESCOLA + " INTEGER PRIMARY KEY," + ESC_NOME
                + " INTEGER," + ESC_MORADA + " TEXT, " + ESC_LOGOTIPO
                + " TEXT )";
        db.execSQL(createTableString);

////////Construir a Tabela Estudante //////////////////
        createTableString = "CREATE TABLE " + TABELA_ESTUDANTE + "("
                + EST_ID + " INTEGER PRIMARY KEY,"
                + EST_IDTURMA + " INTEGER,"
                + ESC_NOME + " TEXT,"
                + EST_FOTO + " TEXT,"
                + EST_ESTADO + " INTEGER" + ")";
        db.execSQL(createTableString);

        //Construir a Tabela Sistema //////////////////
        createTableString = "CREATE TABLE " + TABELA_SISTEMA + "("
                + SIS_ID + " INTEGER PRIMARY KEY,"
                + SIS_NOME + " TEXT,"
                + SIS_VALOR + " TEXT )";
        db.execSQL(createTableString);

        //Construir a Tabela Teste //////////////////
        createTableString = "CREATE TABLE " + TABELA_TESTE + "("
                + TEST_ID + " INTEGER PRIMARY KEY,"
                + TEST_AREAID + " INT,"
                + TEST_PROFESSORID + " INT,"
                + TEST_TITULO + " TEXT,"
                + TEST_TEXTO + " TEXT, "
                + TEST_DATAINSERCAO + " LONG, "
                + TEST_GRAU + " INT,"
                + TEST_TIPO + " INT)";
        db.execSQL(createTableString);

        //Construir a Tabela TesteLeitura //////////////////
        createTableString = "CREATE TABLE " + TABELA_TESTELEITURA + "("
                + TESTL_ID + " INTEGER PRIMARY KEY,"
                + TESTL_TEXTO + " TEXT,"
                + TESTL_SOMPROFESSOR + " TEXT)";
        db.execSQL(createTableString);

        //Construir a Tabela TesteMultimedia //////////////////
        createTableString = "CREATE TABLE " + TABELA_TESTEMULTIMEDIA + "("
                + TESTM_ID + " INTEGER PRIMARY KEY,"
                + TESTM_CONTEUDOQUESTAO + " TEXT,"
                + TESTM_CONTEUDOISURL + " INT,"
                + TESTM_OPCAO1 + " TEXT,"
                + TESTM_OPCAO1ISURL + " INT,"
                + TESTM_OPCAO2 + " TEXT,"
                + TESTM_OPCAO2ISURL + " INT,"
                + TESTM_OPCAO3 + " TEXT,"
                + TESTM_OPCAO3ISURL + " INT,"
                + TESTM_OPCAOCORRETA + " INT )";
        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("db", "No onUpgrade.");
        // Apagar tabelas antigas existentes
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PROFESSORES);
        // Create tables again
        this.onCreate(db);
    }
///////////////////////////////////////////////////////////////////////////////////////
/////////////////// Operacoes CRUD(Create, Read, Update, Delete) //////////////////////
///////////////////////////////////////////////////////////////////////////////////////

                               //*************************//
                               //**********INSERIR********//
                               //*************************//

    /**
     * Adiciona um novo registo na tabela Professores
     * @param prof Recebe um objecto do tipo professor onde vai inserir
     *             os dados na base de dados na tabela Professores
     */
    public  void addNewItemProf(Professor prof) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String UrlimgEscola = "IMG"+prof.getId()+".jpg";
        values.put(PROF_IDPROFS, prof.getId());   // Inserir na tabela campo Nome
        values.put(PROF_NOME, prof.getNome());   // Inserir na tabela campo Nome
        values.put(PROF_IDESCOLA, prof.getIdEscola());   // Inserir na tabela campo idEscola
        values.put(PROF_USERNAME, prof.getUsername());   // Inserir na tabela campo username
        values.put(PROF_PASSWORD, prof.getPassword());   // Inserir na tabela campo Password
        values.put(PROF_TELEFONE, prof.getTelefone());   // Inserir na tabela campo Telefone
        values.put(PROF_EMAIL, prof.getEmail());   // Inserir na tabela campo email
        values.put(PROF_FOTO, UrlimgEscola);   // Inserir na tabela campo fotoURL
        values.put(PROF_ESTADO, prof.isEstado());   // Inserir na tabela campo isEstado
        // Inserir LINHAS:
        Utils.saveFileSD("Professors", UrlimgEscola, prof.getFoto());
        db.insert(TABELA_PROFESSORES, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }

    /**
     * Adiciona um novo registo na tabela Escolas
     * @param escola Recebe um objecto do tipo Escolas onde vai inserir
     *             os dados na base de dados na tabela Escolas
     */
    public void addNewItemEscolas(Escola escola) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String UrlimgEscola = "IMG"+escola.getIdEscola()+".jpg";
        values.put(ESC_IDESCOLA, escola.getIdEscola());   // Inserir na tabela campo IDescola
        values.put(ESC_NOME, escola.getNome());         // Inserir na tabela campo nome
        values.put(ESC_LOGOTIPO, UrlimgEscola);  // Inserir na tabela campo logotipo
        values.put(ESC_MORADA, escola.getMorada());     // Inserir na tabela campo morada
        // Inserir LINHAS:

        Utils.saveFileSD("Schools", UrlimgEscola, escola.getLogotipo());
        db.insert(TABELA_ESCOLAS, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }

    /**
     * Adiciona um novo registo na tabela Estudante
     * @param estudante Recebe um objecto do tipo Estudante onde vai inserir
     *             os dados na base de dados na tabela Estudante
     */
    public  void addNewItemEstudante(Estudante estudante) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String UrlimgEstudante = "IMG" + estudante.getIdEstudante()+".jpg";
        values.put(EST_ID, estudante.getIdEstudante());   // Inserir na tabela campo Id
        values.put(EST_IDTURMA, estudante.getIdTurma());   // Inserir na tabela campo Id turma
        values.put(EST_NOME, estudante.getNome());         // Inserir na tabela nome
        values.put(EST_FOTO, UrlimgEstudante);  // Inserir na tabela campo foto
        values.put(EST_ESTADO, estudante.getEstado());     // Inserir na tabela estado
        // Inserir LINHAS:
        Utils.saveFileSD("Students", UrlimgEstudante, estudante.getFoto());
        db.insert(TABELA_ESTUDANTE, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }

    /**
     * Adiciona um novo registo na tabela Sistema
     * @param sistema Recebe um objecto do tipo Sistema onde vai inserir
     *             os dados na base de dados na tabela sistema
     */
    public void addNewItemSistema(Sistema sistema) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SIS_ID, sistema.getId());   // Inserir na tabela campo Id
        values.put(SIS_NOME, sistema.getNome());   // Inserir na tabela campo nome
        values.put(SIS_VALOR, sistema.getValor());         // Inserir na tabela o campo valor
        // Inserir LINHAS:
        db.insert(TABELA_SISTEMA, null, values);
      //  db.close(); // Fechar a conecao a Base de dados
    }


    /**
     * Adiciona um novo registo na tabela Testes
     * @param teste Recebe um objecto do tipo Testes onde vai inserir
     *             os dados na base de dados na tabela Testes
     */
    public void addNewItemTestes(Teste teste) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEST_ID, teste.getIdTeste());   // Inserir na tabela campo Id
        values.put(TEST_AREAID, teste.getAreaId());   // Inserir na tabela campo Id
        values.put(TEST_PROFESSORID, teste.getProfessorId());   // Inserir na tabela campo Id
        values.put(TEST_TITULO, teste.getTitulo());   // Inserir na tabela campo Titulo
        values.put(TEST_TEXTO, teste.getTexto());         // Inserir na tabela o campo Texto
        values.put(TEST_DATAINSERCAO, teste.getDataInsercaoTeste());         // Inserir na tabela o campo dataInsercao
        values.put(TEST_GRAU, teste.getGrauEscolar());         // Inserir na tabela o campo Grau
        values.put(TEST_TIPO, teste.getTipo());         // Inserir na tabela o campo Tipo
        // Inserir LINHAS:
        db.insert(TABELA_TESTE, null, values);
       // db.close(); // Fechar a conecao a Base de dados
    }


    /**
     * Adiciona um novo registo na tabela TestesLeitura
     * @param teste Recebe um objecto do tipo TestesLeitura onde vai inserir
     *             os dados na base de dados na tabela TestesLeitura
     */
    public void addNewItemTestesLeitura(TesteLeitura teste) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTest = new ContentValues();
        valuesTest.put(TESTL_ID, teste.getIdTeste());   // Inserir na tabela campo Id
        valuesTest.put(TEST_AREAID, teste.getAreaId());   // Inserir na tabela campo Id
        valuesTest.put(TEST_PROFESSORID, teste.getProfessorId());   // Inserir na tabela campo Id
        valuesTest.put(TEST_TITULO, teste.getTitulo());   // Inserir na tabela campo Titulo
        valuesTest.put(TEST_TEXTO, teste.getTexto());         // Inserir na tabela o campo Texto
        valuesTest.put(TEST_DATAINSERCAO, teste.getDataInsercaoTeste());         // Inserir na tabela o campo dataInsercao
        valuesTest.put(TEST_GRAU, teste.getGrauEscolar());         // Inserir na tabela o campo Grau
        valuesTest.put(TEST_TIPO, teste.getTipo());         // Inserir na tabela o campo tIPO
        db.insert(TABELA_TESTE, null, valuesTest);
        //////////////////////////////////////////////////////
        ContentValues valuesTestLeitura = new ContentValues();
        valuesTestLeitura.put(TESTL_ID, teste.getIdTeste());         // Inserir na tabela o campo ID
        valuesTestLeitura.put(TESTL_TEXTO, teste.getConteudoTexto());         // Inserir na tabela o campo TEXTO
        valuesTestLeitura.put(TESTL_SOMPROFESSOR, teste.getProfessorAudioUrl());         // Inserir na tabela o campo somProfessor
        // Inserir LINHAS:
        db.insert(TABELA_TESTELEITURA, null, valuesTestLeitura);
      //  db.close(); // Fechar a conecao a Base de dados
    }


    /**
     * Adiciona um novo registo na tabela TestesMultimedia
     * @param testeM Recebe um objecto do tipo TestesMultimedia onde vai inserir
     *             os dados na base de dados na tabela TestesMultimedia
     */
    public void addNewItemTestesMultimedia (TesteMultimedia testeM) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTest = new ContentValues();
        valuesTest.put(TESTL_ID, testeM.getIdTeste());           // Inserir na tabela campo Id
        valuesTest.put(TEST_AREAID, testeM.getAreaId());        // Inserir na tabela campo Id
        valuesTest.put(TEST_PROFESSORID, testeM.getProfessorId());   // Inserir na tabela campo Id
        valuesTest.put(TEST_TITULO, testeM.getTitulo());              // Inserir na tabela campo Titulo
        valuesTest.put(TEST_TEXTO, testeM.getTexto());                 // Inserir na tabela o campo Texto
        valuesTest.put(TEST_DATAINSERCAO, testeM.getDataInsercaoTeste());   // Inserir na tabela o campo dataInsercao
        valuesTest.put(TEST_GRAU, testeM.getGrauEscolar());            // Inserir na tabela o campo Grau
        valuesTest.put(TEST_TIPO, testeM.getTipo());                 // Inserir na tabela o campo tIPO
        db.insert(TABELA_TESTE, null, valuesTest);
        //////////////////////////////////////////////////////
        ContentValues valuesTestMultimedia = new ContentValues();
        valuesTestMultimedia.put(TESTM_ID, testeM.getIdTeste());                           // Inserir na tabela o campo ID
        valuesTestMultimedia.put(TESTM_CONTEUDOQUESTAO, testeM.getConteudoQuestao());     // Inserir na tabela o campo ConteudoQuestao
        valuesTestMultimedia.put(TESTM_CONTEUDOISURL, testeM.getContentIsUrl());         // Inserir na tabela o campo ContentIsUrl
        valuesTestMultimedia.put(TESTM_OPCAO1, testeM.getOpcao1());                      // Inserir na tabela o campo Opcao1
        valuesTestMultimedia.put(TESTM_OPCAO1ISURL, testeM.getOpcao1IsUrl());            // Inserir na tabela o campo Opcao1IsUrl
        valuesTestMultimedia.put(TESTM_OPCAO2, testeM.getOpcao2());                       // Inserir na tabela o campo Opcao2
        valuesTestMultimedia.put(TESTM_OPCAO2ISURL, testeM.getOpcao2IsUrl());            // Inserir na tabela o campo Opcao2IsUrl
        valuesTestMultimedia.put(TESTM_OPCAO3, testeM.getOpcao3());                      // Inserir na tabela o campo Opcao3
        valuesTestMultimedia.put(TESTM_OPCAO3ISURL, testeM.getOpcao3IsUrl());           // Inserir na tabela o campo Opcao3IsUrl
        valuesTestMultimedia.put(TESTM_OPCAOCORRETA, testeM.getCorrectOption());        // Inserir na tabela o campo CorrectOption
        // Inserir LINHAS:
        db.insert(TABELA_TESTEMULTIMEDIA, null, valuesTestMultimedia);
      //  db.close(); // Fechar a conecao a Base de dados
    }


                            //*************************//
                            //*********SELECT**********//
                            //*************************//

    /**
     * Buscar Um professor pelo o ID
     * @id recebe o Id
     * Retorna um objecto que contem Professor preenchido
     */
    public Professor getProfessorById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_PROFESSORES,
                new String[]{PROF_IDPROFS, PROF_IDESCOLA, PROF_NOME,
                        PROF_USERNAME, PROF_PASSWORD, PROF_TELEFONE, PROF_EMAIL,
                        PROF_FOTO, PROF_ESTADO},
                PROF_IDPROFS + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        ////// Se existir dados comeca a preencher o Objecto professor
        if (cursor != null)
            cursor.moveToFirst();
        Professor prof = new Professor(cursor.getInt(0), cursor.getInt(1),
                cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                Utils.getFileSD("Professors", cursor.getString(7)), cursor.getInt(8));
        // return o Item ja carregado com os dados
        db.close();
        return prof;
    }

    /**
     * Buscar Um estudante pelo o ID do ITEM
     * @id recebe o Id
     * Retorna um objecto que contem Estudante preenchido
     */
    public Estudante getEstudanteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_ESTUDANTE,
                new String[]{EST_ID, EST_IDTURMA, EST_NOME,
                        EST_FOTO, EST_ESTADO},
                EST_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        ////// Se existir dados comeca a preencher o Objecto Estudante
        if (cursor != null)
            cursor.moveToFirst();
        Estudante est = new Estudante(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                Utils.getFileSD("Students", cursor.getString(3)),
                cursor.getInt(4));
        // return o Item ja carregado com os dados
        db.close();
        return est;
    }

    /**
     * Buscar Um Campo desistema pelo o NOME
     * @id recebe o NOME
     * Retorna um objecto que contem Sistema preenchido
     */
    public  Sistema getSistemaByname(String name) {
        Sistema sist = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_SISTEMA,
                new String[]{SIS_ID, SIS_NOME, SIS_VALOR},
                SIS_NOME + "=?",
                new String[]{name}, null, null, null, null
        );
        ////// Se existir dados comeca a preencher o Objecto Sistema
        if (cursor != null) {
            cursor.moveToFirst();
             sist = new Sistema(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));
        }
        // return o Item ja carregado com os dados
        db.close();
        return sist;
    }


                 //*************************//
                 //********SELECT ALL*******//
                 //*************************//


    /**
     * Buscar todos os campos da Tabela Professores
     * Retorna uma lista com varios objectos do tipo "Professores"
     */
     public List<Professor> getAllProfesors() {
        List<Professor> listProfessores = new ArrayList<Professor>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_PROFESSORES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Professor prof = new Professor();
                prof.setId(cursor.getInt(0));
                prof.setIdEscola(cursor.getInt(1));
                prof.setNome(cursor.getString(2));
                prof.setUsername(cursor.getString(3));
                prof.setPassword(cursor.getString(4));
                prof.setTelefone(cursor.getString(5));
                prof.setEmail(cursor.getString(6));
                prof.setFotoNome( cursor.getString(7));
                prof.setEstado(cursor.getInt(8));
                // Adicionar os os items da base de dados a lista
                listProfessores.add(prof);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listProfessores;
    }


    /**
     * Buscar todos os Professores de uma determinada escola
     * Retorna uma lista com varios objectos do tipo "Professores"
     */
    public List<Professor> getAllProfesorsBySchool(int idescola) {
        List<Professor> listProfessores = new ArrayList<Professor>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_PROFESSORES + " WHERE "+PROF_IDESCOLA + " = "+idescola;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Professor prof = new Professor();
                prof.setId(cursor.getInt(0));
                prof.setIdEscola(cursor.getInt(1));
                prof.setNome(cursor.getString(2));
                prof.setUsername(cursor.getString(3));
                prof.setPassword(cursor.getString(4));
                prof.setTelefone(cursor.getString(5));
                prof.setEmail(cursor.getString(6));
                prof.setFotoNome( cursor.getString(7));
                prof.setEstado(cursor.getInt(8));
                // Adicionar os os items da base de dados a lista
                listProfessores.add(prof);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listProfessores;
    }







    /**
     * Buscar todos os campos da Tabela Escola
     * Retorna uma lista com varios objectos do tipo "Escola"
     */
    public List<Escola> getAllSchools() {
        List<Escola> listEscolas = new ArrayList<Escola>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_ESCOLAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Escola escola = new Escola();
                escola.setIdEscola(cursor.getInt(0));
                escola.setNome(cursor.getString(1));
                escola.setMorada(cursor.getString(2));
                escola.setLogotipoNome(cursor.getString(3));
                // Adicionar os os items da base de dados a lista
                listEscolas.add(escola);
            } while (cursor.moveToNext());
        }
        // return a lista com todos os items da base de dados
        db.close();
        return listEscolas;
    }


    /**
     * Buscar todos os campos da Tabela Estudante
     * Retorna uma lista com varios objectos do tipo "Estudante"
     */
    public List<Estudante> getAllStudents() {
        List<Estudante> listEstudantes = new ArrayList<Estudante>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_ESTUDANTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando  lista
        if (cursor.moveToFirst()) {
            do {
                Estudante estudante = new Estudante();
                estudante.setIdEstudante(cursor.getInt(0));
                estudante.setIdTurma(cursor.getInt(1));
                estudante.setNome(cursor.getString(2));
                estudante.setNomefoto(cursor.getString(3));
                estudante.setEstado(cursor.getInt(4));
                // Adicionar os os items da base de dados a lista
                listEstudantes.add(estudante);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listEstudantes;
    }


    /**
     * Buscar todos os campos da Tabela Sistema
     * Retorna uma lista com varios objectos do tipo "sistema"
     */
    public List<Sistema> getAllSistema() {
        List<Sistema> listSistema = new ArrayList<Sistema>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_SISTEMA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atravEs de todas as linhas e adicionando Alista
        if (cursor.moveToFirst()) {
            do {
                Sistema sistema = new Sistema();
                sistema.setId(cursor.getInt(0));
                sistema.setNome(cursor.getString(1));
                sistema.setValor(cursor.getString(2));
             // Adicionar os os items da base de dados a lista
                listSistema.add(sistema);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listSistema;
    }

    /**
     * Buscar todos os campos da Tabela Testes
     * Retorna uma lista com varios objectos do tipo "Testes"
     */
    public List<Teste> getAllTeste() {
        List<Teste> listTeste = new ArrayList<Teste>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TESTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atravEs de todas as linhas e adicionando Alista
        if (cursor.moveToFirst()) {
            do {
                Teste teste = new Teste();
                teste.setIdTeste(cursor.getInt(0));
                teste.setAreaId(cursor.getInt(1));
                teste.setProfessorId(cursor.getInt(2));
                teste.setTitulo(cursor.getString(3));
                teste.setTexto(cursor.getString(4));
                teste.setDataInsercaoTeste(cursor.getLong(5));
                teste.setGrauEscolar(cursor.getInt(6));
                teste.setTipos(cursor.getInt(7));
                // Adicionar os os items da base de dados a lista
                listTeste.add(teste);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listTeste;
    }

    /**
     * Buscar todos os campos da Tabela TestesLeitura
     * Retorna uma lista com varios objectos do tipo "TestesLeitura"
     */
    public List<TesteLeitura> getAllTesteLeitura() {
        List<TesteLeitura> listTeste = new ArrayList<TesteLeitura>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TESTELEITURA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atravEs de todas as linhas e adicionando Alista
        if (cursor.moveToFirst()) {
            do {
                TesteLeitura teste = new TesteLeitura();
                teste.setIdTeste(cursor.getInt(0));
                teste.setConteudoTexto(cursor.getString(1));
                teste.setProfessorAudioUrl(cursor.getString(2));
                // Adicionar os os items da base de dados a lista
                listTeste.add(teste);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listTeste;
    }


    /**
     * Buscar todos os campos da Tabela TestesMultimedia
     * Retorna uma lista com varios objectos do tipo "TestesMultimedia"
     */
    public List<TesteMultimedia> getAllTesteMultimedia() {
        List<TesteMultimedia> listTesteMultimedia = new ArrayList<TesteMultimedia>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TESTEMULTIMEDIA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando Alista
        if (cursor.moveToFirst()) {
            do {
                TesteMultimedia teste = new TesteMultimedia();
                teste.setIdTeste(cursor.getInt(0));
                teste.setConteudoQuestao(cursor.getString(1));
                teste.setContentIsUrl(cursor.getInt(2));
                teste.setOpcao1(cursor.getString(3));
                teste.setOpcao1IsUrl(cursor.getInt(4));
                teste.setOpcao2(cursor.getString(5));
                teste.setOpcao2IsUrl(cursor.getInt(6));
                teste.setOpcao3(cursor.getString(7));
                teste.setOpcao3IsUrl(cursor.getInt(8));
                teste.setCorrectOption(cursor.getInt(9));
                // Adicionar os os items da base de dados a lista
                listTesteMultimedia.add(teste);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listTesteMultimedia;
    }


                                //*************************//
                                //*********UPDATE**********//
                                //*************************//


    /**
     * Actualizar um registo unico da Tabela Sistema
     * @sistema Objecto com os dados a actualizar
     */
	public int updateSistemaItem(Sistema sistema) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SIS_NOME, sistema.getNome()); // Actualizar campo nome
		values.put(SIS_VALOR, sistema.getValor()); // Actualizar campo valor
		// Actualizar registos na Base de dados
		return db.update(TABELA_SISTEMA, values, SIS_NOME + " = ?",
				new String[] { String.valueOf(sistema.getNome()) });
	}

                                     //*************************//
                                     //*********DELETE**********//
                                     //*************************//
                                //DELETE DE TODOS OS DADOS DAS TABELAS//

    /**
     * Apaga todos os dados da tabela professores
     */
    public void deleteAllItemsProf() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_PROFESSORES + " WHERE 1");
        db.close();
        Utils.deleteAllFileFolder("Professors");
    }

    /**
     * Apaga todos os dados da tabela escolas
     */
    public void deleteAllItemsEscola() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_ESCOLAS + " WHERE 1");
        db.close();
        Utils.deleteAllFileFolder("Schools");
    }

    /**
     * Apaga todos os dados da tabela estudantes
     */
    public void deleteAllItemsEstudante() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_ESTUDANTE + " WHERE 1");
        db.close();
        Utils.deleteAllFileFolder("Students");
    }

    /**
     * Apaga todos os dados da tabela testes
     */
    public void deleteAllItemsTests() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_TESTE + " WHERE 1");
        db.close();
    }

    /**
     * Apaga todos os dados da tabela testeslEITURA
     */
    public void deleteAllItemsTestsLeitura() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_TESTELEITURA + " WHERE 1");
        db.close();
      //  Utils.deleteAllFileFolder("ReadingTests");
    }

    /**
     * Apaga todos os dados da tabela testeslEITURA
     */
    public void deleteAllItemsTestsMultimedia() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_TESTEMULTIMEDIA + " WHERE 1");
        db.close();
   //     Utils.deleteAllFileFolder("MultimediaTest");
    }



///////////////////Codigo antigo mais tarde deve dar jeito///////////
    /**
     * Obtendo Contagem Items na Base de  dados
     * Retorna um inteiro com o total de resgisto da Base de dados
     */
//	public int getContactsCount() {
//		String countQuery = "SELECT  * FROM " + TABLE_IMAGES;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();
//		// return Total de registos da Base de Dados
//		return cursor.getCount();
//	}

//////////////////////////////////////////APENAS PARA TESTES PARA MAIS TARDE

    // Apagar registo

    /**
     * Apagar registo na tabela
     *
     * @contact Objecto com os dados ao que se prentende apagar na bd
     * //
     */
//	public void deleteAllItemsProf(DadosImg contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABELA_PROFESSORES, PROF_ID + " = ?",
//				new String[] { String.valueOf(contact.getID()) });
//		db.close();
//	}
}
