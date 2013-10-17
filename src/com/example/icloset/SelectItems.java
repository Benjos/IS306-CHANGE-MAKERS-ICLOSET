package com.example.icloset;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.icloset.database.EventDAO;
import com.example.icloset.database.ItemDAO;
import com.example.icloset.model.Event;
import com.example.icloset.model.Item;
import com.example.utilities.PhotoUtilities;

public class SelectItems extends BaseActivity {

	ViewHolder holder;
	ItemAdapter adapter;
	Event event;
	ArrayList<Item> items;
	ListView listview;
	ItemFetcher itemFetcher;
	String mode;

	public final static String MODE = "mode";
	public final static int CREATE = 0;
	public final static int UPDATE = 1;

	public int currentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_items);
		Intent intent = getIntent();
		event = (Event) intent.getSerializableExtra("event");
		currentMode = intent.getIntExtra(MODE, CREATE);

		items = new ArrayList<Item>();
		// TODO get all the items
		adapter = new ItemAdapter(this, R.layout.list_element_choose_items,
				items);
		listview = (ListView) findViewById(R.id.list_view);
		listview.setAdapter(adapter);
		// Get all the items to be displayed
		itemFetcher = new ItemFetcher();
		itemFetcher.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select_items, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.getItem(0);
		if (currentMode == UPDATE) {
			menuItem.setTitle("Update");
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getItemId() == R.id.create) {
			EventItemInserter eventItemInserter = new EventItemInserter();
			eventItemInserter.execute();

		}

		return true;
	}

	class ItemAdapter extends ArrayAdapter<Item> {

		LayoutInflater inflater;
		int textViewResourceId;

		public ItemAdapter(Context context, int textViewResourceId,
				ArrayList<Item> items) {
			super(context, textViewResourceId, items);
			inflater = getLayoutInflater();
			this.textViewResourceId = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(textViewResourceId, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.select_items_image);

				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkbox);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox checkBox = (CheckBox) v;
					Item item = (Item) checkBox.getTag();
					// Check the item if the item is checked
					item.isChecked = checkBox.isChecked();
				}
			});

			Item item = items.get(position);
			if (item.isChecked) {
				holder.checkBox.setChecked(true);
			} else {
				holder.checkBox.setChecked(false);
			}
			holder.checkBox.setTag(item);
			PhotoUtilities.setPic(holder.imageView, item.path, 150, 150);

			return convertView;
		}
	}

	public class ViewHolder {
		ImageView imageView;
		CheckBox checkBox;
	}

	/**
	 * @author Ben
	 * 
	 *         The async method that fetches the items from the memory
	 * 
	 */
	public class ItemFetcher extends AsyncTask<Void, Void, List<Item>> {
		ItemDAO itemDAO;
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new ProgressDialog(SelectItems.this);
			progressDialog.setTitle("Fetching the items.. Just a moment");
			progressDialog.show();

		}

		@Override
		protected List<Item> doInBackground(Void... params) {
			itemDAO = new ItemDAO(SelectItems.this);
			itemDAO.open();
			// get all the items
			items = (ArrayList<Item>) itemDAO.getAll();
			itemDAO.close();
			if (currentMode == UPDATE) {
				// check the existing items
				for (Item item : items) {
					for (Item alreadySelectedItem : event.items) {
						if (item.id == alreadySelectedItem.id) {
							item.isChecked = true;
						}
					}
				}

			}

			return items;
		}

		@Override
		protected void onPostExecute(List<Item> result) {
			super.onPostExecute(result);
			items = (ArrayList<Item>) result;
			adapter.clear();
			adapter.addAll(items);
			adapter.notifyDataSetChanged();
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public class EventItemInserter extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SelectItems.this);
			progressDialog
					.setTitle("Adding the items to the event just a moment... ");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			ArrayList<Item> itemsToAdd = new ArrayList<Item>();
			// TODO add the items to the database
			for (Item item : items) {
				if (item.isChecked) {
					itemsToAdd.add(item);
				}
			}
			EventDAO eventDAO = new EventDAO(SelectItems.this);
			eventDAO.open();
			// Just delete all the existing items and add the new ones
			eventDAO.deleteExistingItemsFromEvent(event);
			eventDAO.addItemsToEvent(event, itemsToAdd);
			eventDAO.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			Intent intent = new Intent(SelectItems.this, LauncherActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		}
	}

}
