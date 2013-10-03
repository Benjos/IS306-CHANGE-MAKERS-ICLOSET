package com.example.icloset.DAO;

import java.util.ArrayList;
import java.util.List;

import com.example.icloset.database.helpers.CategoriesHelper;
import com.example.icloset.model.Category;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoriesDAO {

	// Database fields
	private SQLiteDatabase database;
	private CategoriesHelper dbHelper;
	private String[] allColumns = { CategoriesHelper.CATEGORY_ID,
			CategoriesHelper.CATEGORY_NAME };

	public CategoriesDAO(Context context) {
		dbHelper = new CategoriesHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Category createCategory(String category) {
		ContentValues values = new ContentValues();
		values.put(CategoriesHelper.CATEGORY_NAME, category);
		long insertId = database.insert(CategoriesHelper.CATEGORIES, null,
				values);
		Cursor cursor = database.query(CategoriesHelper.CATEGORIES, allColumns,
				CategoriesHelper.CATEGORY_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Category cat = cursorToCategory(cursor);
		cursor.close();
		return cat;
	}

	public void deleteCategory(Category cat) {
		long id = cat.id;
		System.out.println("Category deleted with id: " + id);
		database.delete(CategoriesHelper.CATEGORIES,
				CategoriesHelper.CATEGORY_ID + " = " + id, null);
	}

	public List<Category> getAllCategorys() {
		List<Category> categories = new ArrayList<Category>();

		Cursor cursor = database.query(CategoriesHelper.CATEGORIES, allColumns,
				null, null, null, null, null);
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