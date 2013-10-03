package com.example.icloset.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoriesHelper extends SQLiteOpenHelper {
	public static final String CATEGORIES = "categories";
	public static final String CATEGORY_ID = "id";
	public static final String CATEGORY_NAME = "category";
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + CATEGORIES
			+ "(" + CATEGORY_ID + " integer primary key autoincrement, "
			+ CATEGORY_NAME + " text not null);";

	public CategoriesHelper(Context context) {
		super(context, DataBaseConstants.DB_NAME, null,
				DataBaseConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create all the tables related to the item (item table and the item)
		database.execSQL(DATABASE_CREATE);

		// populate the values of the table with these
		ContentValues v1 = new ContentValues();
		v1.put(CategoriesHelper.CATEGORY_NAME, "shirt");
		database.insert(CategoriesHelper.CATEGORIES, null, v1);

		ContentValues v2 = new ContentValues();
		v2.put(CategoriesHelper.CATEGORY_NAME, "pants");
		database.insert(CategoriesHelper.CATEGORIES, null, v2);

		ContentValues v3 = new ContentValues();
		v3.put(CategoriesHelper.CATEGORY_NAME, "dress");
		database.insert(CategoriesHelper.CATEGORIES, null, v3);

		ContentValues v4 = new ContentValues();
		v4.put(CategoriesHelper.CATEGORY_NAME, "shoes");
		database.insert(CategoriesHelper.CATEGORIES, null, v4);

		ContentValues v5 = new ContentValues();
		v5.put(CategoriesHelper.CATEGORY_NAME, "bags");
		database.insert(CategoriesHelper.CATEGORIES, null, v5);

		ContentValues v6 = new ContentValues();
		v6.put(CategoriesHelper.CATEGORY_NAME, "accessories");
		database.insert(CategoriesHelper.CATEGORIES, null, v6);

		// TODO add all the types to the table
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CategoriesHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES);
		onCreate(db);
	}

}