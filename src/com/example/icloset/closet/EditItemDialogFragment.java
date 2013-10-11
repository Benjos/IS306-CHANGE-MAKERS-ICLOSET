package com.example.icloset.closet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icloset.BaseActivity;
import com.example.icloset.R;
import com.example.icloset.R.id;
import com.example.icloset.R.layout;
import com.example.icloset.database.ItemDAO;
import com.example.icloset.model.Item;
import com.example.utilities.PhotoUtilities;

// add these codes to the place you want the dialog framgment to appear

/**
 * @author Ben Always use the getInstanceMethod
 */
public class EditItemDialogFragment extends DialogFragment {

	Item item;
	public final String TAG = EditItemDialogFragment.class.getSimpleName();
	BaseActivity baseActivity;
	private static EditItemDialogFragment editItemDialogFragment;

	public static EditItemDialogFragment getInstance(Item item) {
		editItemDialogFragment = new EditItemDialogFragment();
		editItemDialogFragment.item = item;

		return editItemDialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		baseActivity = (BaseActivity) getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Closet Item");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_fragment_edit_item, null);

		TextView itemDescription = (TextView) view
				.findViewById(R.id.dialog_fragment_edit_item_description);

		TextView itemCategory = (TextView) view
				.findViewById(R.id.dialog_fragment_edit_item_category);

		ImageView imageView = (ImageView) view
				.findViewById(R.id.dialog_fragment_edit_item_image);

		itemCategory.setText(item.category);
		itemDescription.setText(item.description);
		PhotoUtilities.setPic(imageView, item.path, 200, 200);

		builder.setView(view);

		builder.setNegativeButton("Edit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(baseActivity,
								EditItemEnterDetails.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("item", editItemDialogFragment.item);
						baseActivity.startActivity(intent);
					}
				});
		builder.setNeutralButton("Delete",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Delete the item.
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("Are you sure you want to delete the item ?");
						builder.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {
										// Do nothing it will just close the
										// dialog

										editItemDialogFragment.show(
												baseActivity
														.getSupportFragmentManager(),
												"dialog");

									}

								});
						builder.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										ItemDeleter itemDeleter = new ItemDeleter();
										itemDeleter.execute(baseActivity);

									}
								});

						builder.show();

					}
				});

		builder.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// TODO Edit the item.
					}
				});

		return builder.create();

	}

	public class ItemDeleter extends AsyncTask<Context, Void, Boolean> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(baseActivity);
			dialog.setMessage("Deleting please wait ...");
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(Context... params) {

			ItemDAO itemDao = new ItemDAO(params[0]);
			itemDao.open();
			itemDao.delete(editItemDialogFragment.item);
			itemDao.close();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				// This will tell the closet fragment to refresh the current
				// gridView fragment.
				ClosetFragment.gridViewFragment.updateView();

			}
		}
	}

}
