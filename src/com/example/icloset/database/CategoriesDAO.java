package com.example.icloset.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.icloset.model.Category;

public class CategoriesDAO {

	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public CategoriesDAO(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Category createCategory(String category) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.CATEGORY_NAME, category);
		long insertId = database
				.insert(DBHelper.CATEGORIES_TABLE, null, values);
		Cursor cursor = database
				.query(DBHelper.CATEGORIES_TABLE, null, DBHelper.CATEGORY_ID
						+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Category cat = cursorToCategory(cursor);
		cursor.close();
		return cat;
	}

	public void deleteCategory(Category cat) {
		long id = cat.id;
		System.out.println("Category deleted with id: " + id);
		database.delete(DBHelper.CATEGORIES_TABLE, DBHelper.CATEGORY_ID + " = "
				+ id, null);
	}

	public List<Category> getAllCategorys() {
		List<Category> categories = new ArrayList<Category>();

		Cursor cursor = database.query(DBHelper.CATEGORIES_TABLE, null, null,
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category Category = cursorToCategory(cursor);
			categories.add(Category);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return categories;
	}

	private Category cursorToCategory(Cursor cursor) {
		Category cat = new Category();
		cat.id = cursor.getLong(0);
		cat.name = cursor.getString(1);
		return cat;
	}
}