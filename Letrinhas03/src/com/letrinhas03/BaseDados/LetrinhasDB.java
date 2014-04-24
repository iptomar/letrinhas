/////////////////////////////////////////////////////////////////////////
///// ESTA CLASS É A CLASS QUE GERE A BASE DE  DADOS, CRIA A BASE DE////
/////DADOS E CONTEM METODOS PARA GERIR AS VARIAS TABELAS           ////
//////////////////////////////////////////////////////////////////////


package com.letrinhas03.BaseDados;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.letrinhas03.ClassesObjs.Escola;
import com.letrinhas03.ClassesObjs.Estudante;
import com.letrinhas03.ClassesObjs.Professor;
import com.letrinhas03.ClassesObjs.Sistema;

public class LetrinhasDB extends SQLiteOpenHelper {

    // Vers�o da base de dados
    private static final int VERSAO_BASEDADOS = 1;

    // Nome da Base  de dados
    private static final String NOME_BASEDADOS = "letrinhasDb";
    // Nome da tabela da Base de dados
    private static final String TABELA_PROFESSORES = "tblProfessores";
    private static final String TABELA_ESCOLAS = "tblEscolas";
    private static final String TABELA_ESTUDANTE = "tblEstudantes";
    private static final String TABELA_SISTEMA = "tblSistema";

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

    public LetrinhasDB(Context context) {
        super(context, NOME_BASEDADOS, null, VERSAO_BASEDADOS);
    }

    // Create Tabela

    /**
     * Criar Tabela Professores
     *
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
                + PROF_FOTO + " MEDIUMBLOB, "
                + PROF_ESTADO + " INTEGER )";
        db.execSQL(createTableString);
        Log.d("db", "A criar tabela " + TABELA_ESCOLAS);

        //////// Construir a Tabela Escolas //////////////////
        createTableString = "CREATE TABLE " + TABELA_ESCOLAS + "("
                + ESC_IDESCOLA + " INTEGER PRIMARY KEY," + ESC_NOME
                + " INTEGER," + ESC_MORADA + " TEXT, " + ESC_LOGOTIPO
                + " MEDIUMBLOB )";
        db.execSQL(createTableString);

////////Construir a Tabela Estudante //////////////////
        createTableString = "CREATE TABLE " + TABELA_ESTUDANTE + "("
                + EST_ID + " INTEGER PRIMARY KEY,"
                + EST_IDTURMA + " INTEGER,"
                + ESC_NOME + " TEXT,"
                + EST_FOTO + " MEDIUMBLOB,"
                + EST_ESTADO + " INTEGER" + ")";
        db.execSQL(createTableString);

        //Construir a Tabela Sistema //////////////////
        createTableString = "CREATE TABLE " + TABELA_SISTEMA + "("
                + SIS_ID + " INTEGER PRIMARY KEY,"
                + SIS_NOME + " TEXT,"
                + SIS_VALOR + " TEXT )";
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

    /**
     * Operacoes CRUD(Create, Read, Update, Delete)
     */

    /**
     * Adiciona um novo registo na tabela Professores
     *
     * @param prof Recebe um objecto do tipo professor onde vai inserir
     *             os dados na base de dados na tabela Professores
     */
    void addNewItemProf(Professor prof) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROF_IDPROFS, prof.getId());   // Inserir na tabela campo Nome
        values.put(PROF_NOME, prof.getNome());   // Inserir na tabela campo Nome
        values.put(PROF_IDESCOLA, prof.getIdEscola());   // Inserir na tabela campo idEscola
        values.put(PROF_USERNAME, prof.getUsername());   // Inserir na tabela campo username
        values.put(PROF_PASSWORD, prof.getPassword());   // Inserir na tabela campo Password
        values.put(PROF_TELEFONE, prof.getTelefone());   // Inserir na tabela campo Telefone
        values.put(PROF_EMAIL, prof.getEmail());   // Inserir na tabela campo email
        values.put(PROF_FOTO, prof.getFoto());   // Inserir na tabela campo foto
        values.put(PROF_ESTADO, prof.isEstado());   // Inserir na tabela campo isEstado
        // Inserir LINHAS:
        db.insert(TABELA_PROFESSORES, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }

    /**
     * Adiciona um novo registo na tabela Escolas
     *
     * @param escola Recebe um objecto do tipo Escolas onde vai inserir
     *             os dados na base de dados na tabela Escolas
     */
    void AddNewItemEscolas(Escola escola) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ESC_IDESCOLA, escola.getIdEscola());   // Inserir na tabela campo IDescola
        values.put(ESC_NOME, escola.getNome());         // Inserir na tabela campo nome
        values.put(ESC_LOGOTIPO, escola.getLogotipo());  // Inserir na tabela campo logotipo
        values.put(ESC_MORADA, escola.getMorada());     // Inserir na tabela campo morada
        // Inserir LINHAS:
        db.insert(TABELA_ESCOLAS, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }


    /**
     * Adiciona um novo registo na tabela Estudante
     *
     * @param estudante Recebe um objecto do tipo Estudante onde vai inserir
     *             os dados na base de dados na tabela Estudante
     */
    void AddNewItemEstudante(Estudante estudante) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EST_ID, estudante.getIdEstudante());   // Inserir na tabela campo Id
        values.put(EST_IDTURMA, estudante.getIdTurma());   // Inserir na tabela campo Id turma
        values.put(EST_NOME, estudante.getNome());         // Inserir na tabela nome
        values.put(EST_FOTO, estudante.getFoto());  // Inserir na tabela campo foto
        values.put(EST_ESTADO, estudante.getEstado());     // Inserir na tabela estado
        // Inserir LINHAS:
        db.insert(TABELA_ESTUDANTE, null, values);
        //	db.close(); // Fechar a conecao a Base de dados
    }

    /**
     * Adiciona um novo registo na tabela Sistema
     * @param sistema Recebe um objecto do tipo Sistema onde vai inserir
     *             os dados na base de dados na tabela sistema
     */
    void AddNewItemSistema(Sistema sistema) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SIS_ID, sistema.getId());   // Inserir na tabela campo Id
        values.put(SIS_NOME, sistema.getNome());   // Inserir na tabela campo nome
        values.put(SIS_VALOR, sistema.getValor());         // Inserir na tabela o campo valor
        // Inserir LINHAS:
        db.insert(TABELA_SISTEMA, null, values);
        	db.close(); // Fechar a conecao a Base de dados
    }



    //SELECT

    /**
     * Buscar Um professor pelo o ID
     * @id recebe o Id
     * Retorna um objecto que contem Professor preenchido
     */
    Professor getProfessorById(int id) {
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
                cursor.getBlob(7), cursor.getInt(8));
        // return o Item ja carregado com os dados
        db.close();
        return prof;
    }


    /**
     * Buscar Um professor pelo o ID do ITEM
     *
     * @id recebe o Id
     * Retorna um objecto que contem Professor preenchido
     */
    Estudante getEstudanteById(int id) {
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
                cursor.getBlob(3),
                cursor.getInt(4));
        // return o Item ja carregado com os dados
        db.close();
        return est;
    }

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
        // loop atrav�s de todas as linhas e adicionando � lista
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
                prof.setFoto(cursor.getBlob(7));
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
     * Buscar todos os campos da Tabela Professores
     * Retorna uma lista com varios objectos do tipo "Professores"
     */
    public List<Escola> getAllSchools() {
        List<Escola> listEscolas = new ArrayList<Escola>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_ESCOLAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atrav�s de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Escola escola = new Escola();
                escola.setIdEscola(cursor.getInt(0));
                escola.setNome(cursor.getString(1));
                escola.setMorada(cursor.getString(2));
                escola.setLogotipo(cursor.getBlob(3));
                // Adicionar os os items da base de dados a lista
                listEscolas.add(escola);
            } while (cursor.moveToNext());
        }
        // return a lista com todos os items da base de dados
        db.close();
        return listEscolas;
    }


    /**
     * Buscar todos os campos da Tabela Professores
     * Retorna uma lista com varios objectos do tipo "Professores"
     */
    public List<Estudante> getAllStudents() {
        List<Estudante> listEstudantes = new ArrayList<Estudante>();
        // Select TODOS OS DADOS
        String selectQuery = "SELECT  * FROM " + TABELA_ESTUDANTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop atrav�s de todas as linhas e adicionando � lista
        if (cursor.moveToFirst()) {
            do {
                Estudante estudante = new Estudante();
                estudante.setIdEstudante(cursor.getInt(0));
                estudante.setIdTurma(cursor.getInt(1));
                estudante.setNome(cursor.getString(2));
                estudante.setFoto(cursor.getBlob(3));
                estudante.setEstado(cursor.getInt(4));

                // Adicionar os os items da base de dados a lista
                listEstudantes.add(estudante);
            } while (cursor.moveToNext());
        }
        db.close();
        // return a lista com todos os items da base de dados
        return listEstudantes;
    }

//////////////////////////////////////////APENAS PARA TESTES PARA MAIS TARDE
    /**
     * Actualizar um registo unico
     * @DadosImg Objecto com os dados a actualizar
     */
//	public int updateContact(DadosImg contact) {
////		SQLiteDatabase db = this.getWritableDatabase();
////		ContentValues values = new ContentValues();
////		values.put(PROF_NAME, contact.getName()); // Actualizar campo nome
////		values.put(PROF_IMAGE, contact.getImage()); // Actualizar campo imagem
////		// Actualizar registos na Base de dados
////		return db.update(TABLE_IMAGES, values, PROF_ID + " = ?",
////				new String[] { String.valueOf(contact.getID()) });
//	}


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

    /////////////////////////////////////////////////
    ////DELETE DE TODOS OS DADOS DAS TABELAS

    /**
     * Apaga todos os dados da tabela professores
     */
    public void deleteAllItemsProf() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_PROFESSORES + " WHERE 1");
        db.close();
    }

    /**
     * Apaga todos os dados da tabela escolas
     */
    public void deleteAllItemsEscola() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_ESCOLAS + " WHERE 1");
        db.close();
    }

    /**
     * Apaga todos os dados da tabela estudantes
     */
    public void deleteAllItemsEstudante() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABELA_ESTUDANTE + " WHERE 1");
        db.close();
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

}
