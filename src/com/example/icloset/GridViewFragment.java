package com.example.icloset;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			this.type = arguments.getInt(TYPE);
		}
		items = new ArrayList<Item>();
		asycTask = new ItemFetcher();
		asycTask.execute();

	}

	public class ItemFetcher extends AsyncTask<Void, Void, List<Item>> {
		ItemDAO itemDAO;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// TODO show a loading indicator

		}

		@Override
		protected List<Item> doInBackground(Void... params) {
			itemDAO = new ItemDAO(getActivity());
			itemDAO.open();
			// TODO To change the this to retrieve only the specific type of
			// item that needs to be displayed.
			items = (ArrayList<Item>) itemDAO.getAll();
			itemDAO.close();

			return items;
		}

		@Override
		protected void onPostExecute(List<Item> result) {
			super.onPostExecute(result);
			// TODO remove the loading indicator and show the grid view
			items = (ArrayList<Item>) result;
			adapter.notifyDataSetChanged();
			Toast.makeText(getActivity(), "Loading has completed",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_grid_view, null);
		gridview = (GridView) view.findViewById(R.id.gridview);
		adapter = new ImageAdapter(getActivity());
		gridview.setAdapter(adapter);

		// String[] values = { "a", "b", "c" };
		//
		// gridview.setAdapter(new ArrayAdapter(getActivity(),
		// android.R.layout.simple_list_item_1, values));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT)
						.show();
			}
		});

		return view;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;

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
				int size = BasicUtilities.convertDpToPx(getActivity(), 100);
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
