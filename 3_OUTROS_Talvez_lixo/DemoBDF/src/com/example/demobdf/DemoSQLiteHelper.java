package com.example.demobdf;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DemoSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_TESTS = "tests";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_FILE = "file";
	private static final String DATABASE_NAME = "demo.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "+TABLE_TESTS+" (" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_TYPE+" text not null, "+COLUMN_FILE+" blob not null);";
	
	public DemoSQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
