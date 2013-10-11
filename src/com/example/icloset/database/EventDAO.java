package com.example.icloset.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.icloset.model.Event;

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
	public Event create(String eventName, String startDataTime,
			String endDateTime) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.EVENT_END_DATE_TIME, endDateTime);
		values.put(DBHelper.EVENT_START_DATE_TIME, startDataTime);
		values.put(DBHelper.EVENT_NAME, eventName);
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
		database.update(DBHelper.ITEM_TABLE, values, DBHelper.EVENT_ID + " = "
				+ event.id, null);
		return event;
	}

	public void delete(Event event) {
		long id = event.id;
		database.delete(DBHelper.EVENT_TABLE, DBHelper.EVENT_ID + " = " + id,
				null);
	}

	public List<Event> getAll() {
		List<Event> events = new ArrayList<Event>();
		Cursor cursor = database.query(DBHelper.ITEM_TABLE, null, null, null,
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
		event.startTimeDate = cursor.getString(1);
		event.endTimeDate = cursor.getString(2);
		return event;

	}

}