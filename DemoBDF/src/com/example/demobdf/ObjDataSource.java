package com.example.demobdf;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class ObjDataSource {
	private SQLiteDatabase database;
	private DemoSQLiteHelper dbHelper;
	private String[] allColumns = { DemoSQLiteHelper.COLUMN_ID, DemoSQLiteHelper.COLUMN_TYPE, DemoSQLiteHelper.COLUMN_FILE};
	
	public ObjDataSource(Context context){
		dbHelper = new DemoSQLiteHelper(context);
	}
	
	public void open(){
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Obj insertObj(String type, Object obj) throws IOException{
		ContentValues values = new ContentValues();
		values.put(DemoSQLiteHelper.COLUMN_TYPE, type); 
        
		values.put(DemoSQLiteHelper.COLUMN_FILE, Serializer.serialize(obj));
		
		long insertId = database.insert(DemoSQLiteHelper.TABLE_TESTS, null, values);
		Cursor cursor = database.query(DemoSQLiteHelper.TABLE_TESTS, allColumns, DemoSQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Obj newObj = cursorToObj(cursor);
		cursor.close();
		return newObj;
	}
	
	public Obj getObj(long id){
		Cursor cursor = database.query(DemoSQLiteHelper.TABLE_TESTS, allColumns, DemoSQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		Obj obj = cursorToObj(cursor);
		cursor.close();
		try {
			obj.setObj(Serializer.deserialize(obj.getData()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public void deleteObj(Obj obj){
		long id = obj.getId();
		
		database.delete(DemoSQLiteHelper.TABLE_TESTS, DemoSQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	private Obj cursorToObj(Cursor cursor){
		Obj obj = new Obj();
		obj.setId(cursor.getLong(0));
		obj.setType(cursor.getString(1));
		obj.setData(cursor.getBlob(2));
		return obj;
	}
}
