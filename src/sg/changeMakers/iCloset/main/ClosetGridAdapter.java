package sg.changeMakers.iCloset.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.icloset.R;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

public class ClosetGridAdapter extends BaseAdapter implements
		StickyGridHeadersBaseAdapter {

	private LayoutInflater inflater;

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_element_closet, parent, false);
		}

		// EDIT YOUR GRID ITEM HERE
		return convertView;
	}

	@Override
	public int getCountForHeader(int i) {
		return 1;
	}

	@Override
	public int getNumHeaders() {
		return 1;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(0, parent, false);
		}
		// EDIT YOUR VIEW HEADER HERE

		return convertView;
	}
}