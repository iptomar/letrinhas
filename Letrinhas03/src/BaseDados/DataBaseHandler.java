package BaseDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	// Versão da base de dados
	private static final int DATABASE_VERSION = 3;

	// Nome da Base de dados
	private static final String DATABASE_NAME = "letrinhasDb03";
	// Nome das tabelas da Base de dados
	private static final String TABLE_texto = "tblTesteTexto";
	private static final String TABLE_prof = "tblProfessor";
	private static final String TABLE_aluno = "tblAluno";
	private static final String TABLE_result = "tblResultado";

	// Nomes dos campos da tabela texto
	private static final String txt_ID = "id";
	private static final String txt_NAME = "titulo";
	private static final String txt_content = "conteudo";
	private static final String txt_type = "tipo";

	// Nomes dos campos da tabela professor
	private static final String prof_ID = "id";
	private static final String prof_NAME = "Nome";
	private static final String prof_escola = "Escola";
	private static final String prof_secret = "passWD";

	// Nomes dos campos da tabela aluno
	private static final String aluno_ID = "id";
	private static final String aluno_NAME = "nome";
	private static final String aluno_escola = "escola";
	private static final String aluno_turma = "turma";

	// Nomes dos campos da tabela resultado
	private static final String res_ID = "id";
	private static final String res_aluno = "aluno";
	private static final String res_teste = "teste";
	private static final String res_nota = "nota";
	private static final String res_gravacao = "gravacao";

	
	
	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Criar as Tabelas
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
