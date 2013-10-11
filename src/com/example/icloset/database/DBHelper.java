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
	public static final String EVENT_TABLE = "event";
	public static final String EVENT_ITEM_TABLE = "event_item";

	public static final String ITEM_ID = "item_id";
	public static final String ITEM_PATH = "item_path";
	public static final String ITEM_DESCRIPTION = "item_description";
	// Database creation sql statement

	public static final String EVENT_ID = "event_id";
	public static final String EVENT_NAME = "event_name";
	public static final String EVENT_START_DATE_TIME = "event_start_date_time";
	public static final String EVENT_END_DATE_TIME = "event_end_data_time";

	private static final String SQL_CREATE_ITEM = " create table " + ITEM_TABLE
			+ "(" + ITEM_ID + " integer primary key autoincrement, "
			+ ITEM_PATH + " text not null," + ITEM_DESCRIPTION + " text, "
			+ CATEGORY + " text not null " + ");";

	private static final String SQL_CREATE_EVENT = " create table "
			+ EVENT_TABLE + "(" + EVENT_ID
			+ " integer primary key autoincrement, " + EVENT_NAME
			+ " text not null," + EVENT_START_DATE_TIME + " DATETIME, "
			+ EVENT_END_DATE_TIME + " DATETIME not null " + ");";

	// The database is not properly impplemented , but is ok
	private static final String SQL_EVENT_ITEM = " create table "
			+ EVENT_ITEM_TABLE + "(" + EVENT_ID + " integer not null , "
			+ ITEM_ID + " integer not null " + ");";

	public DBHelper(Context context) {
		super(context, DataBaseConstants.DB_NAME, null,
				DataBaseConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create all the tables related to the item (item table and the item)
		database.execSQL(SQL_CREATE_ITEM);
		database.execSQL(SQL_CREATE_EVENT);
		database.execSQL(SQL_EVENT_ITEM);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		;
		db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
		onCreate(db);
	}

}