package com.example.icloset.event;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.example.icloset.R;
import com.example.icloset.database.EventDAO;
import com.example.icloset.model.Event;
import com.example.utilities.BasicUtilities;

public class EventFragment extends Fragment {

	ArrayList<Event> events;
	ListView listView;
	EventAdapter adapter;
	LayoutInflater inflater;
	ViewHolder holder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.fragment_event_list_view, null);
		listView = (ListView) view.findViewById(R.id.fragment_event_list_view);
		events = EventDAO.createDummyEvents();
		adapter = new EventAdapter(getActivity(),
				R.layout.fragment_event_list_element, events);
		listView.setAdapter(adapter);
		return view;
	}

	/**
	 * @author Ben The adapter for the list view
	 * 
	 */
	public class EventAdapter extends ArrayAdapter<Event> {

		int textViewResourceId;
		ArrayList<Event> events;

		public EventAdapter(Context context, int textViewResourceId,
				ArrayList<Event> events) {
			super(context, textViewResourceId, events);
			this.textViewResourceId = textViewResourceId;
			this.events = events;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(textViewResourceId, null);
				// LinearLayout dummyLayout = (LinearLayout) inflater.inflate(
				// R.layout.dummy_horizontal_linear_layout, null);

				holder = new ViewHolder();
				// assign all the view here
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.list_element_event_date);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.list_element_event_time);
				holder.tvEventName = (TextView) convertView
						.findViewById(R.id.list_element_event_name);

				holder.llImageContainer = (LinearLayout) convertView
						.findViewById(R.id.list_element_image_containter);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// Assign the value here
			Event event = events.get(position);
			holder.tvDate.setText(event.startTimeDate);
			holder.tvTime.setText(event.startTimeDate);
			holder.tvEventName.setText(event.name);
			holder.llImageContainer.removeAllViews();

			int noOfImages = 5;
			for (int i = 0; i < noOfImages; i++) {
				LinearLayout dummyLinearLayout = new LinearLayout(getActivity());
				LinearLayout.LayoutParams params = new LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				dummyLinearLayout.setLayoutParams(params);
				dummyLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
				dummyLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

				for (int j = 0; j < 5; j++) {

					ImageView iv = new ImageView(getActivity());
					iv.setLayoutParams(new ViewGroup.LayoutParams(
							BasicUtilities
									.convertDPIntoPixel(60, getActivity()),
							BasicUtilities
									.convertDPIntoPixel(60, getActivity())));
					iv.setImageResource(R.drawable.test_image_bags);
					dummyLinearLayout.addView(iv);

				}
				holder.llImageContainer.addView(dummyLinearLayout);

			}

			return convertView;
		}
	}

	/**
	 * ImageView iv = new CircleImageView(getActivity()); iv.setLayoutParams(new
	 * ViewGroup.LayoutParams(size, size)); String imageUrl =
	 * i.getPicture_URL(); bm.loadImage(imageUrl, iv);
	 * inviteeImageContainer.addView(iv);
	 * 
	 */
	/**
	 * @author Ben
	 * 
	 *         The view holder for the list view
	 * 
	 */
	public class ViewHolder {
		TextView tvDate;
		TextView tvTime;
		TextView tvEventName;
		LinearLayout llImageContainer;

	}

}
