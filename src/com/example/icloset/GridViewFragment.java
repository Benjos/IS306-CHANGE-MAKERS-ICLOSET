package com.example.icloset;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icloset.database.CategoriesDAO;
import com.example.icloset.database.ItemDAO;
import com.example.icloset.model.Item;
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
	ItemDAO itemDAO;
	List<Item> items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			this.type = arguments.getInt(TYPE);
		}
		itemDAO = new ItemDAO(getActivity());
		itemDAO.open();
		items = itemDAO.getAll();
		Log.e(TAG, "No of items  existing " + items.size());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		gridview = (GridView) inflater.inflate(R.layout.fragment_grid_view,
				null);
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

		return gridview;
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
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			// switch (type) {
			// case TYPE_SHIRT:
			//
			// imageView.setImageResource(R.drawable.test_image_shirt);
			// break;
			// case TYPE_PANTS:
			// imageView.setImageResource(R.drawable.test_image_pants);
			// break;
			// case TYPE_DRESS:
			// imageView.setImageResource(R.drawable.test_image_dress);
			// break;
			// case TYPE_SHOES:
			// imageView.setImageResource(R.drawable.test_image_shoes);
			// break;
			// case TYPE_BAGS:
			// imageView.setImageResource(R.drawable.test_image_bags);
			// break;
			// case TYPE_ACCESSORIES:
			// imageView.setImageResource(R.drawable.test_image_accessories);
			// break;
			//
			// default:
			// break;
			// }
			String path = items.get(position).path;
			PhotoUtilities.setPic(imageView, path);
			return imageView;
		}
	}
}
