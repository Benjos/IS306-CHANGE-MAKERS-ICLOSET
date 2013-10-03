package com.example.icloset.addItem;

import java.util.ArrayList;

import android.content.Intent;
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
import android.widget.Toast;

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
		PhotoUtilities.setPic(iv, path, 200,200);
		// PhotoUtilities.galleryAddPic(this, path);
		et = (EditText) findViewById(R.id.add_item_enter_details_description);

		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
				BasicUtilities.redirect(AddItemEnterDetails.this,
						LauncherActivity.class);
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
			BasicUtilities.redirect(this, LauncherActivity.class);
		}
		return super.onOptionsItemSelected(item);
	}

	// TODO add the item to the database and store the image url and the options
	private boolean save() {
		String description = et.getText().toString();
		Toast.makeText(this, "Description : " + description, Toast.LENGTH_LONG)
				.show();
		// add the item to the database
		ItemDAO itemDAO = new ItemDAO(this);
		itemDAO.open();
		itemDAO.create(path, description, currentCategory);
		itemDAO.close();

		return true;
	}

}
