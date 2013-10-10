package com.example.icloset;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

// add these codes to the place you want the dialog framgment to appear

/**
 * demoteDialogFragment = DemoteDialogFragment.getInstance(event);
 demoteDialogFragment.show(getFragmentManager(), "dialog");
 */

import com.example.icloset.model.Item;

/**
 * @author Ben Always use the getInstanceMethod
 */
public class EditItemDialogFragment extends DialogFragment {

	Item item;
	BaseActivity baseActivity;

	public static EditItemDialogFragment getInstance(Item item) {
		EditItemDialogFragment dialogFragment = new EditItemDialogFragment();
		dialogFragment.item = item;
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		baseActivity = (BaseActivity) getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(item.description);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = null;
		builder.setView(view);
		builder.setPositiveButton("Edit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Edit the item.
					}
				});
		builder.setNeutralButton("Delete",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Delete the item.

					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Do nothing

					}
				});

		return builder.create();

	}
}
