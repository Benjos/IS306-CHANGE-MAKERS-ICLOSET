package com.example.icloset.event;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.icloset.BaseActivity;
import com.example.icloset.database.EventDAO;
import com.example.icloset.model.Event;

public class EditEventDialogFragment extends DialogFragment {

	Event event;
	public final String TAG = EditEventDialogFragment.class.getSimpleName();
	BaseActivity baseActivity;
	private static EditEventDialogFragment editEventDialogFragment;

	public static EditEventDialogFragment getInstance(Event event) {
		editEventDialogFragment = new EditEventDialogFragment();
		editEventDialogFragment.event = event;

		return editEventDialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		baseActivity = (BaseActivity) getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(event.name);

		builder.setNegativeButton("Edit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// call the intent to edit the event
						Intent intent = new Intent(getActivity(),
								EditEventActivity.class);
						intent.putExtra("event", event);
						getActivity().startActivity(intent);
					}
				});
		builder.setNeutralButton("Delete",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Delete the item.
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("Are you sure you want to delete the event ?");
						builder.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {
										// Do nothing it will just close the
										// dialog

										editEventDialogFragment.show(
												baseActivity
														.getSupportFragmentManager(),
												"dialog");

									}

								});
						builder.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										EventDeleter eventDeleter = new EventDeleter();
										eventDeleter.execute(baseActivity);

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

	public class EventDeleter extends AsyncTask<Context, Void, Boolean> {
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
			// Delete the event and the link to the items

			EventDAO eventDAO = new EventDAO(baseActivity);
			eventDAO.open();
			eventDAO.delete(event);
			eventDAO.close();

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				// This will tell the EventFragment to update its view
				EventFragment.currentFragment.updateView();
			}
		}
	}

}
