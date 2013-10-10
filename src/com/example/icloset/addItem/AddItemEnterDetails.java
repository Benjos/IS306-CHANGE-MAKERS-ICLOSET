package com.example.icloset.addItem;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.icloset.BaseActivity;
import com.example.icloset.LauncherActivity;
import com.example.icloset.R;
import com.example.icloset.database.ItemDAO;
import com.example.utilities.BasicUtilities;
import com.example.utilities.PhotoUtilities;

public class AddItemEnterDetails extends BaseActivity {

	ImageView iv;
	EditText et;
	String currentCategory;
	String path = null;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	ArrayList<String> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item_enter_details);
		iv = (ImageView) findViewById(R.id.add_item_enter_details_image);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		path = bundle.getString("path");
		PhotoUtilities.setPic(iv, path, 200, 200);
		// PhotoUtilities.galleryAddPic(this, path);
		et = (EditText) findViewById(R.id.add_item_enter_details_description);

		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		spinner = (Spinner) findViewById(R.id.add_item_enter_details_spinner);
		if (categories == null) {
			categories = new ArrayList<String>();
			categories.add("Shirt");
			categories.add("Pants");

		}
		currentCategory = categories.get(1);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, categories);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currentCategory = categories.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean valueToReturn = false;
		// valueToReturn = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.add_item_enter_details_menu, menu);
		return valueToReturn;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.add) {
			save();
		}
		return super.onOptionsItemSelected(item);
	}

	// TODO add the item to the database and store the image url and the options
	private void save() {

		AddItemTask task = new AddItemTask();
		task.execute();

	}

	public class AddItemTask extends AsyncTask<Void, Void, Boolean> {

		String description;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Get the description
			description = et.getText().toString();
			// TODO Show the loading indicator
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			ItemDAO itemDAO = new ItemDAO(AddItemEnterDetails.this);
			itemDAO.open();
			itemDAO.create(path, description, currentCategory);
			itemDAO.close();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			BasicUtilities.redirectWithClearTop(AddItemEnterDetails.this,
					LauncherActivity.class);

		}

	}
}
