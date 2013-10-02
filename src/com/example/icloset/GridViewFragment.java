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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			this.type = arguments.getInt(TYPE);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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

			// TODO to move this from here to the constructor of the adapter
			switch (type) {
			case TYPE_SHIRT:
				imageView.setImageResource(R.drawable.test_image_shirt);
				break;
			case TYPE_PANTS:
				imageView.setImageResource(R.drawable.test_image_pants);
				break;
			case TYPE_DRESS:
				imageView.setImageResource(R.drawable.test_image_dress);
				break;
			case TYPE_SHOES:
				imageView.setImageResource(R.drawable.test_image_shoes);
				break;
			case TYPE_BAGS:
				imageView.setImageResource(R.drawable.test_image_bags);
				break;
			case TYPE_ACCESSORIES:
				imageView.setImageResource(R.drawable.test_image_accessories);
				break;

			default:
				break;
			}

			return imageView;
		}
	}
}
