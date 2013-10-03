package com.example.icloset.addItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.icloset.BaseActivity;
import com.example.icloset.LauncherActivity;
import com.example.icloset.R;
import com.example.utilities.BasicUtilities;
import com.example.utilities.PhotoUtilities;

public class AddItemEnterDetails extends BaseActivity {

	ImageView iv;
	String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item_enter_details);
		iv = (ImageView) findViewById(R.id.add_item_enter_details_image);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		path = bundle.getString("path");
		PhotoUtilities.setPic(iv, path);
		PhotoUtilities.galleryAddPic(this, path);
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
		return true;
	}

}
