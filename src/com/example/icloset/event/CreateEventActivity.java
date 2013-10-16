package com.example.icloset.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.icloset.BaseActivity;
import com.example.icloset.R;
import com.example.icloset.SelectItems;
import com.example.icloset.database.EventDAO;
import com.example.icloset.model.Event;

@SuppressLint("SimpleDateFormat")
public class CreateEventActivity extends BaseActivity {
	final static String DATE_PICKER_START = "DATE_PICKER_START";
	final static String DATE_PICKER_END = "DATE_PICKER_END";
	final static String TIME_PICKER_START = "TIME_PICKER_START";
	final static String TIME_PICKER_END = "TIME_PICKER_END";

	public TextView tvEventName;
	public TextView tvDescription;
	public Button buStartDate;
	public Button buStartTime;
	public Button buEndDate;
	public Button buEndTime;

	Calendar startDateTime;
	Calendar endDateTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		tvEventName = (TextView) findViewById(R.id.create_event_activity_event_name);
		tvDescription = (TextView) findViewById(R.id.create_event_activity_description);
		buStartTime = (Button) findViewById(R.id.create_event_activity_start_time);
		buEndTime = (Button) findViewById(R.id.create_event_activity_end_time);
		buStartDate = (Button) findViewById(R.id.create_event_activity_start_date);
		buEndDate = (Button) findViewById(R.id.create_event_activity_end_date);

		buStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(TIME_PICKER_START);
			}
		});
		buEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(TIME_PICKER_END);
			}
		});

		buStartDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(DATE_PICKER_START);

			}
		});
		buEndDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(DATE_PICKER_END);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == R.id.next) {
			EventCreator eventCreator = new EventCreator();
			eventCreator.execute();

		}
		return true;
	}

	// The time picker fragment
	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		String tag = "";
		CreateEventActivity createEventActivity;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			createEventActivity = (CreateEventActivity) getActivity();
			tag = getTag();
			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			if (tag.equals(TIME_PICKER_START)) {
				createEventActivity.buStartTime.setText("" + hourOfDay + ":"
						+ minute);

				if (createEventActivity.startDateTime == null) {
					createEventActivity.startDateTime = Calendar.getInstance();
				}
				createEventActivity.startDateTime.set(Calendar.HOUR_OF_DAY,
						hourOfDay);
				createEventActivity.startDateTime.set(Calendar.MINUTE, minute);

			} else if (tag.equals(TIME_PICKER_END)) {
				createEventActivity.buEndTime.setText("" + hourOfDay + ":"
						+ minute);
				if (createEventActivity.endDateTime == null) {
					createEventActivity.endDateTime = Calendar.getInstance();
				}
				createEventActivity.endDateTime.set(Calendar.HOUR_OF_DAY,
						hourOfDay);

				createEventActivity.endDateTime.set(Calendar.MINUTE, minute);

			}

		}
	}

	/**
	 * @param tag
	 *            Show the dialog fragment based on the tag, this is to identify
	 *            which view should be updated after the time is selected
	 */
	public void showTimePickerDialog(String tag) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), tag);
	}

	public void showDatePickerDialog(String tag) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), tag);
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		String tag = "";
		CreateEventActivity createEventActivity;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			createEventActivity = (CreateEventActivity) getActivity();
			tag = getTag();
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			if (tag.equals(DATE_PICKER_START)) {
				if (createEventActivity.startDateTime == null) {
					createEventActivity.startDateTime = Calendar.getInstance();
				}
				createEventActivity.startDateTime.set(year, month, day);
				createEventActivity.buStartDate.setText(day + " - " + month
						+ " - " + year);
			} else if (tag.equals(DATE_PICKER_END)) {

				if (createEventActivity.endDateTime == null) {
					createEventActivity.endDateTime = Calendar.getInstance();
				}
				createEventActivity.endDateTime.set(year, month, day);
				createEventActivity.buEndDate.setText(day + " - " + month
						+ " - " + year);
			}

		}
	}

	class EventCreator extends AsyncTask<Void, Void, Void> {

		EventDAO eventDAO;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");

		String eventName;
		String description;
		String startDateTime;
		String endDateTime;
		ProgressDialog progressDialog;
		Event createdEvent;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			eventName = tvEventName.getText().toString();
			description = tvDescription.getText().toString();

			
			startDateTime = dateFormat
					.format(CreateEventActivity.this.startDateTime.getTime());
			endDateTime = dateFormat
					.format(CreateEventActivity.this.endDateTime.getTime());

			progressDialog = new ProgressDialog(CreateEventActivity.this);
			progressDialog.setTitle("Creating event.. Just a moment");
			progressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			eventDAO = new EventDAO(CreateEventActivity.this);
			eventDAO.open();
			createdEvent = eventDAO.create(eventName, description,
					startDateTime, endDateTime);
			eventDAO.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();

			}
			if (createdEvent == null) {
				Toast.makeText(CreateEventActivity.this, "Event not created",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(CreateEventActivity.this,
						SelectItems.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("event", createdEvent);
				startActivity(intent);
			}

		}

	}
}
