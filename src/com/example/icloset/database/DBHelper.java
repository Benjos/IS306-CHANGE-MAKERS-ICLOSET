package com.example.icloset.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Ben The database helper that helps to create all the table and helps
 *         with versioning
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String CATEGORIES_TABLE = "categories";
	public static final String CATEGORY_ID = "category_id";
	public static final String CATEGORY_NAME = "category_name";
	public static final String ITEM_TABLE = "item";
	public static final String ITEM_ID = "item_id";
	public static final String ITEM_PATH = "item_path";
	public static final String ITEM_DESCRIPTION = "item_description";
	// Database creation sql statement
	private static final String SQL_CREATE_CATEGORIES = "create table "
			+ CATEGORIES_TABLE + "(" + CATEGORY_ID
			+ " integer primary key autoincrement, " + CATEGORY_NAME
			+ " text not null);";

	private static final String SQL_CREATE_ITEM = " create table " + ITEM_TABLE
			+ "(" + ITEM_ID + " integer primary key autoincrement, "
			+ ITEM_PATH + " text not null," + ITEM_DESCRIPTION + " text "
			+ CATEGORY_ID + " integer, " + " FOREIGN KEY(" + CATEGORY_ID
			+ ") REFERENCES " + CATEGORIES_TABLE + "(" + CATEGORY_ID + ") "
			+ ");";

	public DBHelper(Context context) {
		super(context, DataBaseConstants.DB_NAME, null,
				DataBaseConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create all the tables related to the item (item table and the item)
		database.execSQL(SQL_CREATE_CATEGORIES);
		database.execSQL(SQL_CREATE_ITEM);
		// populate the values of the table with these
		ContentValues v1 = new ContentValues();
		v1.put(DBHelper.CATEGORY_NAME, "shirt");
		database.insert(DBHelper.CATEGORIES_TABLE, null, v1);

		ContentValues v2 = new ContentValues();
		v2.put(DBHelper.CATEGORY_NAME, "pants");
		database.insert(DBHelper.CATEGORIES_TABLE, null, v2);

		ContentValues v3 = new ContentValues();
		v3.put(DBHelper.CATEGORY_NAME, "dress");
		database.insert(DBHelper.CATEGORIES_TABLE, null, v3);

		ContentValues v4 = new ContentValues();
		v4.put(DBHelper.CATEGORY_NAME, "shoes");
		database.insert(DBHelper.CATEGORIES_TABLE, null, v4);

		ContentValues v5 = new ContentValues();
		v5.put(DBHelper.CATEGORY_NAME, "bags");
		database.insert(DBHelper.CATEGORIES_TABLE, null, v5);

		ContentValues v6 = new ContentValues();
		v6.put(DBHelper.CATEGORY_NAME, "accessories");
		database.insert(DBHelper.CATEGORIES_TABLE, null, v6);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
		onCreate(db);
	}

}