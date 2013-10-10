package com.example.icloset;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.icloset.addItem.AddItemEnterDetails;
import com.example.utilities.BasicUtilities;
import com.example.utilities.PhotoUtilities;

/**
 * @author Ben
 * 
 *         This is the base activity that contains the action bar item This
 *         class only deals with the action bar
 */

public class BaseActivity extends FragmentActivity {

	// IclostApplication app;
	// BitmapManager bm;

	public static String TAG = BaseActivity.class.getSimpleName();

	int menuItemLastClicked = 0;
	private String mCurrentPhotoPath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		// app = (IclostApplication) getApplication();
		// app.currentActivity = this;
		// bm = new BitmapManager();
		// bm.onCreate(this, R.drawable.icon_shirt);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// bm.onDestroy();
		// clearReferences();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// bm.onPause();
		// clearReferences();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// bm.onResume();
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
				dispatchTakePictureIntent(0);
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

		File f = null;
		try {
			f = PhotoUtilities.setUpPhotoFile(this);
			// TODO to do the stuff in background.
			mCurrentPhotoPath = f.getAbsolutePath();
			Log.e(TAG, " THe current phtoto path before dispatch : "
					+ mCurrentPhotoPath);
			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		} catch (IOException e) {
			Log.v(TAG, "Catch exception: " + " Dispatch Take picture Intent\n"
					+ e.getMessage());
			f = null;
			mCurrentPhotoPath = null;
		}
		startActivityForResult(takePictureIntent, actionCode);
	}

	/**
	 * This method is called when the intent is complete
	 */

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			if (mCurrentPhotoPath != null) {
				Intent addItemIntent = new Intent(this,
						AddItemEnterDetails.class);
				addItemIntent.putExtra("path", mCurrentPhotoPath);
				startActivity(addItemIntent);
				this.finish();
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

	/**
	 * Coded to handle the orientation changes, this is important
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("path", mCurrentPhotoPath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mCurrentPhotoPath = savedInstanceState.getString("path");

	}
	//
	// private void clearReferences() {
	// Activity currActivity = app.currentActivity;
	// if (currActivity != null && currActivity.equals(this))
	// app.currentActivity = null;
	// }
}
