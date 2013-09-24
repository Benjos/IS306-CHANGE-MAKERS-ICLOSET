package com.example.icloset.addItem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.icloset.BaseActivity;
import com.example.icloset.LauncherActivity;
import com.example.icloset.R;
import com.example.utilities.BasicUtilities;

public class AddItemEnterDetails extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item_enter_details);
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
		boolean valueToReturn = super.onCreateOptionsMenu(menu);
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

	// TODO add the item to the database
	private boolean save() {
		return true;
	}

}
