package com.example.icloset.closet;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.example.icloset.model.Item;
import com.example.utilities.BasicUtilities;
import com.example.utilities.PhotoUtilities;

public class EditItemEnterDetails extends BaseActivity {

	HashMap<String, Integer> categoriesMap;
	ImageView iv;
	EditText et;
	String currentCategory;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	ArrayList<String> categories;
	Item item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item_enter_details);
		iv = (ImageView) findViewById(R.id.edit_item_enter_details_image);
		Intent intent = getIntent();
		item = (Item) intent.getSerializableExtra("item");
		PhotoUtilities.setPic(iv, item.path, 200, 200);
		et = (EditText) findViewById(R.id.edit_item_enter_details_description);
		et.setText(item.description);
		Button edit = (Button) findViewById(R.id.edit);
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
		ImageView takePicture = (ImageView) findViewById(R.id.edit_item_enter_details_take_picture);
		takePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent(0);
			}
		});

		categoriesMap = new HashMap<String, Integer>();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			if (mCurrentPhotoPath != null) {
				item.path = mCurrentPhotoPath;
				PhotoUtilities.setPic(iv, mCurrentPhotoPath, 200, 200);
				mCurrentPhotoPath = null;
			} else {
				Toast.makeText(this, " Image was not taken", Toast.LENGTH_LONG)
						.show();
				Log.e(TAG, "mCurrentPhoto is null");
			}
		} else {
			Toast.makeText(this, "Image was not taken", Toast.LENGTH_LONG)
					.show();
			Log.e(TAG, "result code is not ok");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		spinner = (Spinner) findViewById(R.id.edit_item_enter_details_spinner);
		if (categories == null) {
			categories = new ArrayList<String>();
			categories.add(ClosetFragment.TAB_SHIRT);
			categories.add(ClosetFragment.TAB_PANTS);
			categories.add(ClosetFragment.TAB_DRESS);
			categories.add(ClosetFragment.TAB_BAG);
			categories.add(ClosetFragment.TAB_SHOES);
			categories.add(ClosetFragment.TAB_ACCESORIES);

			categoriesMap.put(ClosetFragment.TAB_SHIRT, 0);
			categoriesMap.put(ClosetFragment.TAB_PANTS, 1);
			categoriesMap.put(ClosetFragment.TAB_DRESS, 2);
			categoriesMap.put(ClosetFragment.TAB_BAG, 3);
			categoriesMap.put(ClosetFragment.TAB_SHOES, 4);
			categoriesMap.put(ClosetFragment.TAB_ACCESORIES, 5);

		}
		currentCategory = categories.get(categoriesMap.get(item.category));

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, categories);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(categoriesMap.get(item.category));
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
		getMenuInflater().inflate(R.menu.edit_item_enter_details_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.edit) {
			save();
		}
		return super.onOptionsItemSelected(item);
	}

	private void save() {

		EditItemTask task = new EditItemTask();
		task.execute();

	}

	public class EditItemTask extends AsyncTask<Void, Void, Boolean> {

		String description;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			description = et.getText().toString();
			item.description = description;
			item.category = currentCategory;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ItemDAO itemDAO = new ItemDAO(EditItemEnterDetails.this);
			itemDAO.open();
			itemDAO.updateItem(item);
			itemDAO.close();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			BasicUtilities.redirectWithClearTop(EditItemEnterDetails.this,
					LauncherActivity.class);
		}

	}
}
