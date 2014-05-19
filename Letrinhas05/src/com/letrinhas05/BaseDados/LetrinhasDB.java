/////////////////////////////////////////////////////////////////////////
///// ESTA CLASS É A CLASS QUE GERE A BASE DE  DADOS, CRIA A BASE DE////
/////DADOS E CONTEM METODOS PARA GERIR AS VARIAS TABELAS           ////
//////////////////////////////////////////////////////////////////////


package com.letrinhas05.BaseDados;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.letrinhas05.ClassesObjs.*;
import com.letrinhas05.util.Utils;

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
    private static final String TABELA_TURMAS = "tblTurmas";
    private static final String TABELA_SISTEMA = "tblSistema";
    private static final String TABELA_TESTE = "tblTeste";
    private static final String TABELA_TESTELEITURA = "tblTesteLeitura";
    private static final String TABELA_TESTEMULTIMEDIA = "tblTesteMultimedia";
    private static final String TABELA_TURMAPROFESSOR = "tblTurmaProfessor";
    private static final String TABELA_CORRECAOTESTE = "tblCorrecaoTeste";
    private static final String TABELA_CORRECAOTESTELEITURA = "tblCorrecaoTesteLeitura";
    private static final String TABELA_CORRECAOMULTIMEDIA = "tblCorrecaoTesteMultimedia";

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

    // Nomes dos campos da tabela Turmas
    private static final String TUR_ID = "id";
    private static final String TUR_IDESCOLA = "idEscola";
    private static final String TUR_ANO = "ano";
    private static final String TUR_NOME = "nome";
    private static final String TUR_ANOLETIVO = "anoLetivo";

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

    // Nomes dos campos da tabela Turmas Prof
    private static final String TURPROF_IDTURMA = "idTeste";
    private static final String TURPROF_IDPROFESSOR = "idProfessor";

        // Nomes dos campos da tabela Correcao Teste
    private static final String CORRT_ID = "idCorrecao";
    private static final String CORRT_IDTESTE = "idTeste";
    private static final String CORRT_IDALUNO = "idAluno";
    private static final String CORRT_DATAEXEC = "dataExecucao";
    private static final String CORRT_TIPO = "tipo";
    private static final String CORRT_ESTADO = "estado";


    // Nomes dos campos da tabela CorrecaoTesteleitura
    private static final String CORRTLEIT_IDCORRECAO= "idCorrecao";
    private static final String CORRTLEIT_AUDIOURL = "audioURL";
    private static final String CORRTLEIT_OBSERVACOES = "obervacoes";
    private static final String CORRTLEIT_NUMPALAVRASPORMIN = "numPalavrasPorMin";
    private static final String CORRTLEIT_NUMPALAVRASCORRET = "numPalavrasCorret";
    private static final String CORRTLEIT_NUMPALAVRASINCORRE = "numPalavrasIncorre";
    private static final String CORRTLEIT_PRECISAO = "precisao";
    private static final String CORRTLEIT_VELOCIDADE = "velocidade";
    private static final String CORRTLEIT_EXPRESSIVIDADE = "expressividade";
    private static final String CORRTLEIT_RITMO = "ritmo";
    private static final String CORRTLEIT_DETALHES = "detalhes";

    // Nomes dos campos da tabela CorrecaoTesteMultimedia
    private static final String CORRTMULTIMEDIA_ID = "idCorrecao";
    private static final String CORRTMULTIMEDIA_OPCAOESCOL = "opcaoEscolhida";
    private static final String CORRTMULTIMEDIA_CERTA = "certa";

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

        ////////Construir a Tabela Turmas //////////////////
        createTableString = "CREATE TABLE " + TABELA_TURMAS + "("
                + TUR_ID + " INTEGER PRIMARY KEY,"
                + TUR_IDESCOLA + " INTEGER,"
                + TUR_ANO + " INTEGER,"
                + TUR_NOME + " TEXT,"
                + TUR_ANOLETIVO + " TEXT" + ")";
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


        //Construir a Tabela TesteLeitura //////////////////
        createTableString = "CREATE TABLE " + TABELA_TURMAPROFESSOR + "("
                + TURPROF_IDTURMA + " INTEGER NOT NULL,"
                + TURPROF_IDPROFESSOR + " INTEGER NOT NULL," +
                "PRIMARY KEY ("+ TURPROF_IDTURMA +", "+ TURPROF_IDPROFESSOR +"))";
        db.execSQL(createTableString);


        //Construir a Tabela TesteMultimedia //////////////////
        createTableString = "CREATE TABLE " + TABELA_TESTEMULTIMEDIA + "("
                + TESTM_ID + " INTEGER PRIMARY KEY ,"
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

        //Construir a Tabela CorrecaoTeste //////////////////
        createTableString = "CREATE TABLE " + TABELA_CORRECAOTESTE + "("
                + CORRT_ID + " LONG PRIMARY KEY,"
                + CORRT_IDTESTE + " INT,"
                + CORRT_IDALUNO + " INT,"
                + CORRT_DATAEXEC + " LONG,"
                + CORRT_TIPO + " INT,"
                + CORRT_ESTADO + " INT)";
        db.execSQL(createTableString);

        //Construir a Tabela CorrecaoTeste //////////////////
        createTableString = "CREATE TABLE " + TABELA_CORRECAOTESTELEITURA + "("
                + CORRTLEIT_IDCORRECAO + " LONG PRIMARY KEY,"
                + CORRTLEIT_AUDIOURL + " TEXT,"
                + CORRTLEIT_OBSERVACOES + " TEXT,"
                + CORRTLEIT_NUMPALAVRASPORMIN + " REAL,"
                + CORRTLEIT_NUMPALAVRASCORRET + " INT,"
                + CORRTLEIT_NUMPALAVRASINCORRE + " INT,"
                + CORRTLEIT_PRECISAO + " REAL,"
                + CORRTLEIT_VELOCIDADE + " REAL,"
                + CORRTLEIT_EXPRESSIVIDADE + " REAL,"
                + CORRTLEIT_RITMO + " REAL,"
                + CORRTLEIT_DETALHES + " TEXT)";
        db.execSQL(createTableString);

        //Construir a Tabela CorrecaoTesteMultimedia //////////////////
        createTableString = "CREATE TABLE " + TABELA_CORRECAOMULTIMEDIA + "("
                + CORRTMULTIMEDIA_ID + " LONG PRIMARY KEY,"
                + CORRTMULTIMEDIA_OPCAOESCOL + " INT,"
                + CORRTMULTIMEDIA_CERTA + " INT)";
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
     * Adiciona um novo registo na tabela Turmas
     * @param turma Recebe um objecto do tipo Turma onde vai inserir
     *             os dados na base de dados na tabela Turmas
     */
    public void addNewItemTurmas(Turma turma) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TUR_ID, turma.getId());   // Inserir na tabela campo Id
        values.put(TUR_IDESCOLA, turma.getIdEscola());   // Inserir na tabela campo Id Escola
        values.put(TUR_ANO, turma.getAnoEscolar());         // Inserir na tabela ano escolar
        values.put(TUR_NOME, turma.getNome());  // Inserir na tabela campo nome
        values.put(TUR_ANOLETIVO, turma.getAnoLetivo());     // Inserir na tabela ano letivo
        // Inserir LINHAS
        db.insert(TABELA_TURMAS, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }


    /**
     * Adiciona um novo registo na tabela TurmasProfessor
     * @param turmaProfessor Recebe um objecto do tipo Turma onde vai inserir
     *             os dados na base de dados na tabela Turmas
     */
    public void addNewItemTurmasProfessor(TurmaProfessor turmaProfessor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TURPROF_IDTURMA, turmaProfessor.getIdTurma());   // Inserir na tabela campo IdTurma
        values.put(TURPROF_IDPROFESSOR, turmaProfessor.getIdProfessor());   // Inserir na tabela campo IdProfessor
        // Inserir LINHAS
        db.insert(TABELA_TURMAPROFESSOR, null, values);
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




    /**
     * Adiciona um novo registo na tabela CorrecaoTesteLeitura
     * @param correcaoTesteLeitura Recebe um objecto CorrecaoTesteLeitura onde vai inserir
     *             os dados na base de dados na tabela CorrecaoTesteLeitura
     */
    public void addNewItemCorrecaoTesteLeitura (CorrecaoTesteLeitura correcaoTesteLeitura) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCorrecaoTeste = new ContentValues();
        valuesCorrecaoTeste.put(CORRT_ID, correcaoTesteLeitura.getIdCorrrecao());           // Inserir na tabela campo Id
        valuesCorrecaoTeste.put(CORRT_IDTESTE, correcaoTesteLeitura.getTestId());        // Inserir na tabela campo teste id
        valuesCorrecaoTeste.put(CORRT_IDALUNO, correcaoTesteLeitura.getIdEstudante());   // Inserir na tabela campo Id estudante
        valuesCorrecaoTeste.put(CORRT_DATAEXEC, correcaoTesteLeitura.getDataExecucao());              // Inserir na tabela data execucao
        valuesCorrecaoTeste.put(CORRT_TIPO, correcaoTesteLeitura.getTipo());
        valuesCorrecaoTeste.put(CORRT_ESTADO, correcaoTesteLeitura.getEstado());                 // Inserir na tabela estado
               db.insert(TABELA_CORRECAOTESTE, null, valuesCorrecaoTeste);


        //////////////////////////////////////////////////////
        ContentValues valuesCorrecaoTestesLeitura = new ContentValues();
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_IDCORRECAO, correcaoTesteLeitura.getIdCorrrecao());                           // Inserir na tabela o campo ID
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_AUDIOURL, correcaoTesteLeitura.getAudiourl());                          // Inserir na tabela o campo audio URL
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_OBSERVACOES, correcaoTesteLeitura.getObservacoes());                     // Inserir na tabela o campo OBSERVACOES
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_NUMPALAVRASPORMIN, correcaoTesteLeitura.getNumPalavrasMin());             // Inserir na tabela palavras por minuto
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_NUMPALAVRASCORRET, correcaoTesteLeitura.getNumPalavCorretas());            // Inserir na tabela o palavras correcas
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_NUMPALAVRASINCORRE, correcaoTesteLeitura.getNumPalavIncorretas());          // Inserir na tabela o campo palavras incorrectas
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_PRECISAO, correcaoTesteLeitura.getPrecisao());                             // Inserir na tabela o campo precisao
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_VELOCIDADE, correcaoTesteLeitura.getVelocidade());                      // Inserir na tabela o campo velocidade
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_EXPRESSIVIDADE, correcaoTesteLeitura.getExpressividade());           // Inserir na tabela o campo expressividade
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_RITMO, correcaoTesteLeitura.getRitmo());                           // Inserir na tabela o campo ritmo
        valuesCorrecaoTestesLeitura.put(CORRTLEIT_DETALHES, correcaoTesteLeitura.getDetalhes());                   // Inserir na tabela o campo detalhes
        // Inserir LINHAS:
        db.insert(TABELA_CORRECAOTESTELEITURA, null, valuesCorrecaoTestesLeitura);
        //  db.close(); // Fechar a conecao a Base de dados
    }


    /**
     * Adiciona um novo registo na tabela CorrecaoTesteMultimedia
     * @param correcaoTesteMultimedia Recebe um objecto CorrecaoTesteMultimedia onde vai inserir
     *             os dados na base de dados na tabela CorrecaoTesteMultimedia
     */
    public void addNewItemCorrecaoTesteMultimedia (CorrecaoTesteMultimedia correcaoTesteMultimedia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesCorrecaoTeste = new ContentValues();
        valuesCorrecaoTeste.put(CORRT_ID, correcaoTesteMultimedia.getIdCorrrecao());           // Inserir na tabela campo Id
        valuesCorrecaoTeste.put(CORRT_IDTESTE, correcaoTesteMultimedia.getTestId());        // Inserir na tabela campo teste id
        valuesCorrecaoTeste.put(CORRT_IDALUNO, correcaoTesteMultimedia.getIdEstudante());   // Inserir na tabela campo Id estudante
        valuesCorrecaoTeste.put(CORRT_DATAEXEC, correcaoTesteMultimedia.getDataExecucao());              // Inserir na tabela data execucao
        valuesCorrecaoTeste.put(CORRT_TIPO, correcaoTesteMultimedia.getTipo());
        valuesCorrecaoTeste.put(CORRT_ESTADO, correcaoTesteMultimedia.getEstado());                 // Inserir na tabela estado
        db.insert(TABELA_CORRECAOTESTE, null, valuesCorrecaoTeste);
        //////////////////////////////////////////////////////
        ContentValues valuesCorrecaoTestesMultimedia = new ContentValues();
        valuesCorrecaoTestesMultimedia.put(CORRTMULTIMEDIA_ID, correcaoTesteMultimedia.getIdCorrrecao());                     // Inserir na tabela o campo ID
        valuesCorrecaoTestesMultimedia.put(CORRTMULTIMEDIA_OPCAOESCOL, correcaoTesteMultimedia.getOpcaoEscolhida());     // Inserir na tabela o campo OPCAOEscolhida
        valuesCorrecaoTestesMultimedia.put(CORRTMULTIMEDIA_CERTA, correcaoTesteMultimedia.getCerta());                  // Inserir na tabela o campo certa?
        // Inserir LINHAS:
        db.insert(TABELA_CORRECAOMULTIMEDIA, null, valuesCorrecaoTestesMultimedia);
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
                 cursor.getString(7), cursor.getInt(8));
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
                cursor.getString(3),
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

    /**
     * Buscar Um Campo Teste pelo o id
     * @id recebe o id
     * Retorna um objecto que contem Teste preenchido
     */
    public Teste getTesteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_TESTE,
                new String[]{TEST_ID, TEST_AREAID, TEST_PROFESSORID,
                        TEST_TITULO, TEST_TEXTO,TEST_DATAINSERCAO, TEST_GRAU, TEST_TIPO },
                TEST_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        ////// Se existir dados comeca a preencher o Objecto Estudante
        if (cursor != null)
            cursor.moveToFirst();
        Teste test = new Teste();
        test.setIdTeste(  cursor.getInt(0));
        test.setAreaId(  cursor.getInt(1));
        test.setProfessorId(  cursor.getInt(2));
        test.setTitulo(  cursor.getString(3));
        test.setTexto(  cursor.getString(4));
        test.setDataInsercaoTeste(  cursor.getLong(5));
        test.setGrauEscolar(  cursor.getInt(6));
        test.setTipos(  cursor.getInt(7));
        // return o Item ja carregado com os dados
        db.close();
        return test;
    }

    /**
     * Buscar Um Campo turma pelo o id
     * @id recebe o id
     * Retorna um objecto que contem turma preenchido
     */
    public Turma getTurmaByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_TURMAS,
                new String[]{TUR_ID, TUR_IDESCOLA, TUR_ANO,
                        TUR_NOME, TUR_ANOLETIVO},
                TUR_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        ////// Se existir dados comeca a preencher o Objecto Estudante
        if (cursor != null)
            cursor.moveToFirst();
        Turma turma = new Turma();
        turma.setId(  cursor.getInt(0));
        turma.setIdEscola(  cursor.getInt(1));
        turma.setAnoEscolar(  cursor.getInt(2));
        turma.setNome(  cursor.getString(3));
        turma.setAnoLetivo(  cursor.getString(4));
        // return o Item ja carregado com os dados
        db.close();
        return turma;
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
     * Buscar todos os campos da Tabela Escola
     * Retorna uma lista com varios objectos do tipo "Escola"
     */
    public List<Turma> getAllTurmas() {
        List<Turma> listTurmas = new ArrayList<Turma>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TURMAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Turma turma = new Turma();
                turma.setId(cursor.getInt(0));
                turma.setIdEscola(cursor.getInt(1));
                turma.setAnoEscolar(cursor.getInt(2));
                turma.setNome(cursor.getString(3));
                turma.setAnoLetivo(cursor.getString(4));
                // Adicionar os os items da base de dados a lista
                listTurmas.add(turma);
            } while (cursor.moveToNext());
        }
        // return a lista com todos os items da base de dados
        db.close();
        return listTurmas;
    }

    /**
     * Buscar todos os campos da Tabela TurmasProfessores
     * Retorna uma lista com varios objectos do tipo "TurmasProfessores"
     */
    public List<TurmaProfessor> getAllTurmasProfessores() {
        List<TurmaProfessor> listTurmasProf = new ArrayList<TurmaProfessor>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TURMAPROFESSOR;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                TurmaProfessor turma = new TurmaProfessor();
                turma.setIdTurma(cursor.getInt(0));
                turma.setIdProfessor(cursor.getInt(1));
                // Adicionar os os items da base de dados a lista
                listTurmasProf.add(turma);
            } while (cursor.moveToNext());
        }
        // return a lista com todos os items da base de dados
        db.close();
        return listTurmasProf;
    }


    /**
     * Buscar todos os campos da Tabela Turmas referentes ao ID Professor
     * @param Prof Recebe um Id do Professor
     * Retorna uma lista com varios objectos do tipo "Turmas"
     */
    public List<Turma> getAllTurmasByProfid(int Prof) {
        List<Turma> listTurmas = new ArrayList<Turma>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT "+ TUR_ID +", "+ TUR_IDESCOLA +",  "+ TUR_ANO +", "+ TUR_NOME +", "+ TUR_ANOLETIVO +" "+
        "FROM " + TABELA_TURMAS + " , " + TABELA_TURMAPROFESSOR +
                " WHERE " + TUR_ID + " = "+ TURPROF_IDTURMA +" AND "+ TURPROF_IDPROFESSOR +" = "+ Prof;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atraves de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Turma turma = new Turma();
                turma.setId(cursor.getInt(0));
                turma.setIdEscola(cursor.getInt(1));
                turma.setAnoEscolar(cursor.getInt(2));
                turma.setNome(cursor.getString(3));
                turma.setAnoLetivo(cursor.getString(4));
                // Adicionar os os items da base de dados a lista
                listTurmas.add(turma);
            } while (cursor.moveToNext());
        }
        // return a lista com todos os items da base de dados
        db.close();
        return listTurmas;
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
     * Buscar todos os campos da Tabela Estudante pelo Id DE TURMA
     * @param idTurma Recebe um Id da turma
     * Retorna uma lista com varios objectos do tipo "estudante"
     */
    public List<Estudante> getAllStudentsByTurmaId(int idTurma) {
        List<Estudante> listEstudantes = new ArrayList<Estudante>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT * FROM " + TABELA_ESTUDANTE +" WHERE "+EST_IDTURMA+" = " + idTurma;
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
     * Buscar todos os campos da Tabela CorrecaoTest
     * Retorna uma lista com varios objectos do tipo "CorrecaoTest"
     */
    public List<CorrecaoTeste> getAllCorrecaoTeste() {
        List<CorrecaoTeste> listcorrecaoTestes = new ArrayList<CorrecaoTeste>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_CORRECAOTESTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atravEs de todas as linhas e adicionando Alista
        if (cursor.moveToFirst()) {
            do {
                CorrecaoTeste corrteste = new CorrecaoTeste();
                corrteste.setIdCorrrecao(cursor.getLong(0));
                corrteste.setTestId(cursor.getInt(1));
                corrteste.setIdEstudante(cursor.getInt(2));
                corrteste.setDataExecucao(cursor.getLong(3));
                corrteste.setTipo(cursor.getInt(4));
                corrteste.setEstado(cursor.getInt(5));
                // Adicionar os os items da base de dados a lista
                listcorrecaoTestes.add(corrteste);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listcorrecaoTestes;
    }

    /**
     *  /**
     * Buscar todos os campos da Tabela CorrecaoTest pelo ID DO PROFESSOR
     * Retorna uma lista com varios objectos do tipo "CorrecaoTest"
     * @param idProf id Professor
     * @return Conjunto de testesCorrecao
     */
    public List<CorrecaoTeste> getAllCorrecaoTesteByProfID(int idProf) {
        List<CorrecaoTeste> listcorrecaoTestes = new ArrayList<CorrecaoTeste>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  "+
        TABELA_CORRECAOTESTE +"."+ CORRT_ID +", "+
        TABELA_CORRECAOTESTE +"."+ CORRT_IDTESTE +", "+
        TABELA_CORRECAOTESTE +"."+ CORRT_IDALUNO +", "+
        TABELA_CORRECAOTESTE +"."+ CORRT_DATAEXEC +", "+
        TABELA_CORRECAOTESTE +"."+ CORRT_TIPO +", "+
        TABELA_CORRECAOTESTE +"."+ CORRT_ESTADO +
                " FROM " + TABELA_CORRECAOTESTE +", "+
                TABELA_ESTUDANTE + ", "+ TABELA_TURMAPROFESSOR + " WHERE "+ TABELA_CORRECAOTESTE +"."+
                CORRT_IDALUNO + " = " +TABELA_ESTUDANTE+ "." + EST_ID + " AND "+ TABELA_ESTUDANTE +"."+
                EST_IDTURMA + " = "+ TABELA_TURMAPROFESSOR +"."+TURPROF_IDTURMA + " AND "+ TABELA_TURMAPROFESSOR + "."+
                TURPROF_IDPROFESSOR + " = " +idProf;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atravEs de todas as linhas e adicionando Alista
        if (cursor.moveToFirst()) {
            do {

                CorrecaoTeste corrteste = new CorrecaoTeste();
                corrteste.setIdCorrrecao(cursor.getLong(0));
                corrteste.setTestId(cursor.getInt(1));
                corrteste.setIdEstudante(cursor.getInt(2));
                corrteste.setDataExecucao(cursor.getLong(3));
                corrteste.setTipo(cursor.getInt(4));
                corrteste.setEstado(cursor.getInt(5));
                // Adicionar os os items da base de dados a lista
                listcorrecaoTestes.add(corrteste);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listcorrecaoTestes;
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
     * Buscar todos os campos da Tabela Testes pelo AreaId e pelo Tipo
     * @areaId id da Area ou seja Disciplina
     * @tipo Tipo
     * Retorna uma lista com varios objectos do tipo "Testes"
     */
    public List<Teste> getAllTesteByAreaIdAndType(int areaId, int tipo) {
        List<Teste> listTeste = new ArrayList<Teste>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TESTE +" WHERE "+TEST_AREAID +" = " + areaId + " AND "+ TEST_TIPO +" = "+ tipo;
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
     * Buscar todos os campos da Tabela Testes pelo AreaId e pelo dois tipos
     * @areaId id da Area ou seja Disciplina
     * @tipo1 Tipo1
     * @tipo2 Tipo1
     * Retorna uma lista com varios objectos do tipo "Testes"
     */
    public List<Teste> getAllTesteByAreaIdAndTwoTypes(int areaId, int tipo1, int tipo2) {
        List<Teste> listTeste = new ArrayList<Teste>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_TESTE +" WHERE "+TEST_AREAID +" = " + areaId + " AND ("+ TEST_TIPO +" = "+ tipo1 + " OR "+ TEST_TIPO +" = "+ tipo2 +")";
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
     * Apaga todos os dados da tabela turmas
     */
    public void deleteAllItemsTurmas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_TURMAS + " WHERE 1");
        db.close();
    }


    /**
     * Apaga todos os dados da tabela turmas
     */
    public void deleteAllItemsTurmasProfessor() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_TURMAPROFESSOR + " WHERE 1");
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

    /**
     * Apaga todos os dados da tabela testeslEITURA
     */
    public void deleteAllItemsCorrecaoTeste() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_CORRECAOTESTE + " WHERE 1");
        db.close();
        //     Utils.deleteAllFileFolder("MultimediaTest");
    }



///////////////////Codigo antigo mais tarde deve dar jeito///////////
    /**
     * Obtendo Contagem Items na Base de  dados
     * Retorna um inteiro com o total de resgisto da Base de dados
     */
	public int getEscolasCount() {
		String countQuery = "SELECT  * FROM " + TABELA_ESCOLAS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
        int total = cursor.getCount();
		cursor.close();
		// return Total de registos da Base de Dados
		return total;
	}

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
