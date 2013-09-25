package com.example.icloset;

import android.content.Context;
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

public class GridViewFragment extends Fragment {

	// The type

	public static String TYPE = GridViewFragment.class.getSimpleName()
			+ " type ";
	public static String TYPE_SHIRT = GridViewFragment.class.getSimpleName()
			+ " shirt ";

	public static String TYPE_PANTS = GridViewFragment.class.getSimpleName()
			+ " pants ";
	public String type = TYPE_SHIRT;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (!(arguments == null || arguments.isEmpty())) {
			this.type = arguments.getString(TYPE);
		}

		GridView gridview = (GridView) inflater.inflate(
				R.layout.fragment_grid_view, null);

		gridview.setAdapter(new ImageAdapter(getActivity()));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT)
						.show();
			}
		});

		return gridview;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		// references to our images
		// private Integer[] mThumbIds;

		public ImageAdapter(Context c) {
			mContext = c;

		}

		public int getCount() {
			// return mThumbIds.length;
			return 5;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			if (TYPE.equals(TYPE_PANTS)) {
				imageView.setImageResource(R.drawable.pants);
			} else {
				imageView.setImageResource(R.drawable.shirt);
			}

			return imageView;
		}

	}
}
