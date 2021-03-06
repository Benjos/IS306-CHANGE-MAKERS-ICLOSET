package com.example.icloset.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.icloset.model.Event;
import com.example.icloset.model.Item;

public class EventDAO {

	public static ArrayList<Event> createDummyEvents() {
		ArrayList<Event> events = new ArrayList<Event>();

		for (int i = 1; i <= 10; i++) {
			Event event = new Event();
			event.id = i;
			event.startTimeDate = " Start time: " + i;
			event.endTimeDate = " End Time: " + i;
			event.name = "Event " + i;

			events.add(event);
		}
		return events;
	}

	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	public final static String TAG = EventDAO.class.getSimpleName();

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
	 * 
	 * @return the event that was just created
	 */
	public Event create(String eventName, String desrciption,
			String startDataTime, String endDateTime) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.EVENT_END_DATE_TIME, endDateTime);
		values.put(DBHelper.EVENT_START_DATE_TIME, startDataTime);
		values.put(DBHelper.EVENT_NAME, eventName);
		values.put(DBHelper.EVENT_DESCRIPTION, desrciption);

		long insertId = database.insert(DBHelper.EVENT_TABLE, null, values);
		Cursor cursor = database.query(DBHelper.EVENT_TABLE, null,
				DBHelper.EVENT_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Event event = convert(cursor);
		cursor.close();
		return event;
	}

	public Event update(Event event) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.EVENT_END_DATE_TIME, event.endTimeDate);
		values.put(DBHelper.EVENT_START_DATE_TIME, event.startTimeDate);
		values.put(DBHelper.EVENT_NAME, event.name);
		values.put(DBHelper.EVENT_DESCRIPTION, event.description);
		int noOfRowsAffected = database.update(DBHelper.EVENT_TABLE, values,
				DBHelper.EVENT_ID + " = " + event.id, null);

		Log.e(TAG, "No of events affected on update  is  : " + noOfRowsAffected);
		return event;
	}

	public void delete(Event event) {
		long id = event.id;
		database.delete(DBHelper.EVENT_TABLE, DBHelper.EVENT_ID + " = " + id,
				null);
		// should also delete from the Event_item table
		database.delete(DBHelper.EVENT_ITEM_TABLE, DBHelper.EVENT_ID + " = "
				+ id, null);

	}

	public List<Event> getAll() {
		List<Event> events = new ArrayList<Event>();
		Cursor cursor = database.query(DBHelper.EVENT_TABLE, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = convert(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return events;
	}

	private Event convert(Cursor cursor) {
		Event event = new Event();
		event.id = cursor.getLong(0);
		event.name = cursor.getString(1);
		event.description = cursor.getString(2);
		event.startTimeDate = cursor.getString(3);
		event.endTimeDate = cursor.getString(4);
		return event;

	}

	public void addItemsToEvent(Event event, ArrayList<Item> items) {

		ContentValues values = new ContentValues();
		// For each of the items add a row in the event_item table to keep track
		// of the items that belong to the event
		for (Item item : items) {
			values.put(DBHelper.EVENT_ID, event.id);
			values.put(DBHelper.ITEM_ID, item.id);
			database.insert(DBHelper.EVENT_ITEM_TABLE, null, values);
		}

	}

	/**
	 * @param event
	 * @return get the id's of the items that belong to the event
	 */
	public ArrayList<Long> getEventItems(Event event) {
		ArrayList<Long> items = new ArrayList<Long>();
		Cursor cursor = database.query(DBHelper.EVENT_ITEM_TABLE, null,
				DBHelper.EVENT_ID + " = " + event.id, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// Get the item id
			items.add(cursor.getLong(1));
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return items;
	}

	public void deleteExistingItemsFromEvent(Event event) {

		database.delete(DBHelper.EVENT_ITEM_TABLE, DBHelper.EVENT_ID + " = "
				+ event.id, null);

	}

}