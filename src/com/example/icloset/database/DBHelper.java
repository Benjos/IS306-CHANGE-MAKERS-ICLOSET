package com.example.icloset.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Ben The database helper that helps to create all the table and helps
 *         with versioning
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String CATEGORY = "category";
	public static final String ITEM_TABLE = "item";
	public static final String ITEM_ID = "item_id";
	public static final String ITEM_PATH = "item_path";
	public static final String ITEM_DESCRIPTION = "item_description";
	// Database creation sql statement

	private static final String SQL_CREATE_ITEM = " create table " + ITEM_TABLE
			+ "(" + ITEM_ID + " integer primary key autoincrement, "
			+ ITEM_PATH + " text not null," + ITEM_DESCRIPTION + " text, "
			+ CATEGORY + " text not null " + ");";

	public DBHelper(Context context) {
		super(context, DataBaseConstants.DB_NAME, null,
				DataBaseConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create all the tables related to the item (item table and the item)
		database.execSQL(SQL_CREATE_ITEM);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		;
		db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
		onCreate(db);
	}

}