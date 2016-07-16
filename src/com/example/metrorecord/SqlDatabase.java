package com.example.metrorecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDatabase {

	public static final String KEY_ROWID = "id";
	private static final String DATABASE_NAME = "MetroData";
    private static final String TABLE_5 = "Line5info";
	private static final String TABLE_1 = "Line1info";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper mHelper;
	
	private final Context mContext;
	
	private SQLiteDatabase mDatabase;

	static int[] station_info = new int[17];
	private static class DbHelper extends SQLiteOpenHelper {
	
	    public DbHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	
	    @Override
	    //Set up database here
	    public void onCreate(SQLiteDatabase db) {
	    	String sql_exec = ("CREATE TABLE " + TABLE_5 + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
	    	int i=0;
	    	for (i=1;i<=10;i++){
	    		sql_exec+="station"+i+" TEXT NOT NULL, ";
	    	}
	    	sql_exec +="station"+i+" TEXT NOT NULL);";
	    	db.execSQL(sql_exec);

	    	sql_exec = ("CREATE TABLE " + TABLE_1 + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
	    	for (i=1;i<=27;i++){
	    		sql_exec+="station"+i+" TEXT NOT NULL, ";
	    	}
	    	sql_exec +="station"+i+" TEXT NOT NULL);";
	    	db.execSQL(sql_exec);
	    }
	
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_5);
	        db.execSQL("DROP IF TABLE EXISTS " + TABLE_1);
	        onCreate(db);
	    }
	
	}

	public SqlDatabase (Context c) {
	    mContext = c;
	}
	
	public SqlDatabase open() throws SQLException {
	    //Set up the helper with the context
	    mHelper = new DbHelper (mContext);
	    //Open the database with our helper
	    mDatabase = mHelper.getWritableDatabase();
	    return this;
	}

	public void close() throws SQLException{
		mHelper.close();
	}


	public long createEntry(String[] result, boolean[] halt,int which_line) {
	    ContentValues cv = new ContentValues();
	    int i;
	    for (i=0;i<result.length/2;i++){
	    	int j = i*2;
	    	cv.put("station"+(i+1) ,result[j]+";"+result[j+1]+";"+halt[i]);
	    }
	    return mDatabase.insert("Line"+which_line+"info", null, cv); 
	}
}
