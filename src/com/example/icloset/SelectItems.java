package com.example.icloset;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.icloset.model.Item;

public class SelectItems extends BaseActivity {

	ViewHolder holder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_items);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	class ItemAdapter extends ArrayAdapter<Item> {

		LayoutInflater inflater;
		int textViewResourceId;

		public ItemAdapter(Context context, int textViewResourceId,
				ArrayList<Item> items) {
			super(context, textViewResourceId, items);
			inflater = getLayoutInflater();
			this.textViewResourceId = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(textViewResourceId, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(textViewResourceId);

			}

			return super.getView(position, convertView, parent);
		}
	}

	public class ViewHolder {
		ImageView imageView;

	}

}
