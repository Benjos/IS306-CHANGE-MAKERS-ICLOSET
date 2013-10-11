package com.example.icloset.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.icloset.model.Item;

public class EventDAO {

	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public EventDAO(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * @param eventName
	 * @param startDataTime
	 *            can be null
	 * @param categoryId
	 *            must be something in the category table
	 * @return
	 */
	public Item create(String eventName, String startDataTime,
			String endDateTime) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.EVENT_END_DATE_TIME, endDateTime);
		values.put(DBHelper.EVENT_START_DATE_TIME, startDataTime);
		values.put(DBHelper.EVENT_NAME, eventName);
		long insertId = database.insert(DBHelper.EVENT_TABLE, null, values);
		Cursor cursor = database.query(DBHelper.ITEM_TABLE, null,
				DBHelper.ITEM_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Item item = convert(cursor);
		cursor.close();
		return item;
	}

	public Item updateItem(Item item) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.CATEGORY, item.category);
		values.put(DBHelper.ITEM_DESCRIPTION, item.description);
		values.put(DBHelper.ITEM_PATH, item.path);
		database.update(DBHelper.ITEM_TABLE, values, DBHelper.ITEM_ID + " = "
				+ item.id, null);
		return item;
	}

	public void delete(Item item) {
		long id = item.id;
		database.delete(DBHelper.ITEM_TABLE, DBHelper.ITEM_ID + " = " + id,
				null);
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

	public List<Item> getItemInCategory(String category) {

		List<Item> items = new ArrayList<Item>();

		Cursor cursor = database.query(DBHelper.ITEM_TABLE, null,
				DBHelper.CATEGORY + "=\"" + category + "\"", null, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item item = convert(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		Log.e("Item Dao",
				"No of items for the " + category + " is  " + items.size());
		return items;
	}

	private Item convert(Cursor cursor) {

		Item item = new Item();
		item.id = cursor.getLong(0);
		item.path = cursor.getString(1);
		item.description = cursor.getString(2);
		item.category = cursor.getString(3);
		return item;
	}

}