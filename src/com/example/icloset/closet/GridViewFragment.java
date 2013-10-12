package com.example.icloset.closet;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.icloset.R;
import com.example.icloset.database.ItemDAO;
import com.example.icloset.model.Item;
import com.example.utilities.BasicUtilities;
import com.example.utilities.PhotoUtilities;

public class GridViewFragment extends Fragment {

	public static String TAG = GridViewFragment.class.getSimpleName();
	// The type

	public static String TYPE = GridViewFragment.class.getSimpleName()
			+ " type ";
	public static final int TYPE_SHIRT = 0;
	public static final int TYPE_PANTS = 1;
	public static final int TYPE_DRESS = 2;
	public static final int TYPE_SHOES = 3;
	public static final int TYPE_BAGS = 4;
	public static final int TYPE_ACCESSORIES = 5;
	public int type = TYPE_SHIRT;

	public ImageAdapter adapter;
	public GridView gridview;
	ItemFetcher asycTask;
	ArrayList<Item> items;

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			this.type = arguments.getInt(TYPE);
		}
		this.context = getActivity();
		updateView();

	}

	public void updateView() {
		items = new ArrayList<Item>();
		asycTask = new ItemFetcher();
		asycTask.execute();
	}

	public class ItemFetcher extends AsyncTask<Void, Void, List<Item>> {
		ItemDAO itemDAO;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected List<Item> doInBackground(Void... params) {
			itemDAO = new ItemDAO(getActivity());
			itemDAO.open();
			// TODO To change the this to retrieve only the specific type of
			// item that needs to be displayed.
			// default value for the category
			String category = ClosetFragment.TAB_SHIRT;
			switch (type) {
			case TYPE_SHIRT:
				category = ClosetFragment.TAB_SHIRT;
				break;
			case TYPE_ACCESSORIES:
				category = ClosetFragment.TAB_ACCESORIES;
				break;
			case TYPE_BAGS:
				category = ClosetFragment.TAB_BAG;
				break;
			case TYPE_DRESS:
				category = ClosetFragment.TAB_DRESS;
				break;
			case TYPE_PANTS:
				category = ClosetFragment.TAB_PANTS;
				break;
			case TYPE_SHOES:
				category = ClosetFragment.TAB_SHOES;
				break;

			default:
				break;
			}

			items = (ArrayList<Item>) itemDAO.getItemInCategory(category);
			itemDAO.close();

			return items;
		}

		@Override
		protected void onPostExecute(List<Item> result) {
			super.onPostExecute(result);
			// TODO remove the loading indicator and show the grid view
			items = (ArrayList<Item>) result;
			adapter.notifyDataSetChanged();

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_grid_view, null);
		gridview = (GridView) view.findViewById(R.id.gridview);

		adapter = new ImageAdapter(getActivity());
		gridview.setAdapter(adapter);
		gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		gridview.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// Do nothing

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				mode.setTitle("Select Items");
				mode.setSubtitle("1 item selected");
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.context_menu, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

				switch (item.getItemId()) {
				case R.id.delete:
					// Delete the item
					deleteItems();
					mode.finish(); // Action picked, so close the CAB
					// TODO call the async task to delete the item and to
					// refresh the view

					return true;
				default:
					return false;
				}
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				// TODO Auto-generated method stub
				int selectCount = gridview.getCheckedItemCount();
				switch (selectCount) {
				case 1:
					mode.setSubtitle("1 item selected");
					break;
				default:
					mode.setSubtitle("" + selectCount + " items selected");
					break;
				}

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment
						.getInstance(items.get(position));
				editItemDialogFragment.show(getFragmentManager(), "dialog");
			}
		});

		return view;
	}

	protected void deleteItems() {
		final long[] checkedItems = gridview.getCheckedItemIds();

		AsyncTask<Void, Void, Void> deleteTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage("Deleting " + checkedItems.length + " items ");
				dialog.show();
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				ItemDAO itemDAO = new ItemDAO(getActivity());
				itemDAO.open();
				itemDAO.delete(checkedItems);
				itemDAO.close();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				dialog.dismiss();
				ClosetFragment.gridViewFragment.updateView();
			}

		};

		deleteTask.execute();

	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;

		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		public int getCount() {
			// return mThumbIds.length;
			return items.size();
		}

		public Object getItem(int position) {
			return items.get(position);
		}

		public long getItemId(int position) {
			return items.get(position).id;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize
				// some
				// attributes
				imageView = new ImageView(mContext);
				imageView.setFocusable(false);
				imageView.setClickable(false);

				int size = BasicUtilities
						.convertDPIntoPixel(100, getActivity());
				imageView
						.setLayoutParams(new GridView.LayoutParams(size, size));
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setPadding(10, 10, 10, 10);
				imageView.setBackgroundColor(getResources().getColor(
						R.color.black));
			} else {
				imageView = (ImageView) convertView;
			}

			String path = items.get(position).path;
			PhotoUtilities.setPic(imageView, path, 100, 100);
			// BaseActivity baseActivity = (BaseActivity) getActivity();
			// BitmapManager bm = baseActivity.bm;
			// bm.loadImage("file:" + path, imageView);
			return imageView;
		}
	}

}
