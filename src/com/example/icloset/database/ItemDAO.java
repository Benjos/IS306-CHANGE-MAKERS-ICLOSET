package com.example.icloset.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.icloset.model.Item;

public class ItemDAO {

	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public ItemDAO(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * @param path
	 * @param description
	 *            can be null
	 * @param categoryId
	 *            must be something in the category table
	 * @return
	 */
	public Item create(String path, String description, long categoryId) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.ITEM_ID, categoryId);
		if (description != null) {
			values.put(DBHelper.ITEM_DESCRIPTION, description);
		}
		values.put(DBHelper.ITEM_PATH, path);
		long insertId = database.insert(DBHelper.ITEM_TABLE, null, values);
		Cursor cursor = database.query(DBHelper.ITEM_TABLE, null,
				DBHelper.ITEM_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Item item = convert(cursor);
		cursor.close();
		return item;
	}

	public void delete(Item item) {
		long id = item.id;
		System.out.println("Category deleted with id: " + id);
		database.delete(DBHelper.ITEM_TABLE, DBHelper.CATEGORY_ID + " = "
				+ id, null);
	}

	public List<Item> getAll() {
		List<Item> items = new ArrayList<Item>();

		Cursor cursor = database.query(DBHelper.ITEM_TABLE, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item item = convert(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return items;
	}

	private Item convert(Cursor cursor) {

		Item item = new Item();
		item.id = cursor.getLong(0);
		item.path = cursor.getString(1);
//		item.description = cursor.getString(2);
//		item.categoryId = cursor.getLong(3);
		return item;
	}
}