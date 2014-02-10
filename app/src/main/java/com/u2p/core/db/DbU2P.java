package com.u2p.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbU2P extends SQLiteOpenHelper{
	public static final String TABLE_USERS="u2p_users";
	public static final String COLUM_ID="_id";
	public static final String COLUM_USER="user";
	public static final String COLUM_GROUP="usergroup";
	public static final String COLUM_HASH="hash";
		
	public static final String GROUP_COLUM_ID="_id";
	public static final String GROUP_COLUM_NAME="filename";
	public static final String GROUP_COLUM_URI="uri";
	public static final String GROUP_COLUM_POSITIVE="positive";
	public static final String GROUP_COLUM_NEGATIVE="negative";
	
	private static final String DATABASE_NAME="u2p.db";
	private static final int DATABASE_VERSION=1;
	
	private static final String CREATE_TABLE_USERS=String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY," +
			" %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",TABLE_USERS,COLUM_ID,COLUM_USER,COLUM_GROUP,COLUM_HASH);
	
	public DbU2P(Context context){
		super( context,DATABASE_NAME, null,DATABASE_VERSION); 
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(DbU2P.class.getName(),"DATABASE CREATED");
		Log.d(DbU2P.class.getName(),"SQL command: "+CREATE_TABLE_USERS);
		db.execSQL(CREATE_TABLE_USERS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DbU2P.class.getName(),
				"Upgrading database from version "+oldVersion+" to "+newVersion
				+", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS+";");
		onCreate(db);
	}

}
