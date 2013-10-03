package com.example.icloset;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.icloset.addItem.AddItemEnterDetails;
import com.example.utilities.BasicUtilities;

/**
 * @author Ben
 * 
 *         This is the base activity that contains the action bar item This
 *         class only deals with the action bar
 */

public class BaseActivity extends FragmentActivity {

	int menuItemLastClicked = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Call the camera intent
		int itemId = item.getItemId();
		menuItemLastClicked = itemId;
		switch (itemId) {
		case R.id.action_add_item:
			if (BasicUtilities.isIntentAvailable(this,
					MediaStore.ACTION_IMAGE_CAPTURE)) {
				dispatchTakePictureIntent(1);
			} else {
				// TODO Need to have my own camera view
			}
			break;

		case R.id.action_add_event:
			Toast.makeText(this, " Add event is called", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.action_settings:
			Toast.makeText(this, " Settings is called", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Take Picture action item code.
	 * 
	 */

	private void dispatchTakePictureIntent(int actionCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(takePictureIntent, actionCode);
	}

	/**
	 * This method is called when the intent is complete
	 */

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {

			// Get the image and store in the memory.

//			File storageDir = new File(
//					Environment
//							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//					getAlbumName());

			// Get the bitmap image to be passed to the Add item page
			Bundle extras = intent.getExtras();
			Bitmap mImageBitmap = (Bitmap) extras.get("data");
			Intent addItemIntent = new Intent(this, AddItemEnterDetails.class);
			addItemIntent.putExtra("bitmap", mImageBitmap);
			startActivity(addItemIntent);
			this.overridePendingTransition(0, 0);
			this.finish();

		} else {
			Toast.makeText(this, "Image was not taken", Toast.LENGTH_LONG)
					.show();
		}
		// TODO check if the result code is OK and add the bitmap to the

	}
}
