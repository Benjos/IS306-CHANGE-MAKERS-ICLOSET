package com.example.icloset.event;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.example.icloset.R;
import com.example.icloset.database.EventDAO;
import com.example.icloset.database.ItemDAO;
import com.example.icloset.model.Event;
import com.example.icloset.model.Item;
import com.example.utilities.BasicUtilities;
import com.example.utilities.PhotoUtilities;

public class EventFragment extends Fragment {

	public ArrayList<Event> events;
	public ListView listView;
	public EventAdapter adapter;
	LayoutInflater inflater;
	ViewHolder holder;
	public static EventFragment eventFragment;
	final static int IMAGE_DIMENSION = 120;
	public static EventFragment currentFragment;

	public void updateView() {
		events = new ArrayList<Event>();
		EventFetcher eventFetcher = new EventFetcher();
		eventFetcher.execute();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		eventFragment = this;
		View view = inflater.inflate(R.layout.fragment_event_list_view, null);
		listView = (ListView) view.findViewById(R.id.fragment_event_list_view);
		events = new ArrayList<Event>();
		adapter = new EventAdapter(getActivity(),
				R.layout.fragment_event_list_element, events);
		listView.setAdapter(adapter);
		updateView();
		currentFragment = this;

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
				holder.ivEditButton = (ImageView) convertView
						.findViewById(R.id.edit);

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

			holder.ivEditButton.setTag(event);
			holder.ivEditButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Popup to ask if you need to delete or edit the event
					Event event = (Event) v.getTag();
					EditEventDialogFragment editEventDialogFragment = EditEventDialogFragment
							.getInstance(event);

					editEventDialogFragment
							.show(getFragmentManager(), "dialog");

				}
			});

			int noOfImages = event.items.size();

			int count = 0;

			outer: for (int i = 0; i < noOfImages; i++) {

				LinearLayout dummyLinearLayout = new LinearLayout(getActivity());
				LinearLayout.LayoutParams params = new LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				dummyLinearLayout.setLayoutParams(params);
				dummyLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
				dummyLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

				for (int j = 0; j < 2; j++) {

					ImageView iv = new ImageView(getActivity());
					iv.setLayoutParams(new ViewGroup.LayoutParams(
							BasicUtilities.convertDPIntoPixel(IMAGE_DIMENSION,
									getActivity()), BasicUtilities
									.convertDPIntoPixel(IMAGE_DIMENSION,
											getActivity())));

					// iv.setImageResource(R.drawable.test_image_bags);
					PhotoUtilities.setPic(iv, event.items.get(count).path,
							IMAGE_DIMENSION, IMAGE_DIMENSION);
					dummyLinearLayout.addView(iv);
					++count;
					if (count >= noOfImages) {
						holder.llImageContainer.addView(dummyLinearLayout);
						break outer;
					}

				}
				holder.llImageContainer.addView(dummyLinearLayout);

			}

			return convertView;
		}
	}

	public class ViewHolder {
		TextView tvDate;
		TextView tvTime;
		TextView tvEventName;
		ImageView ivEditButton;
		LinearLayout llImageContainer;

	}

	class EventFetcher extends AsyncTask<Void, Void, ArrayList<Event>> {
		ProgressDialog progressDialog;
		Context context;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("Fetching events ..  just a moment... ");
			progressDialog.show();
			this.context = getActivity();
		}

		@Override
		protected ArrayList<Event> doInBackground(Void... params) {
			// get the events
			EventDAO eventDAO = new EventDAO(context);
			eventDAO.open();
			events = (ArrayList<Event>) eventDAO.getAll();

			ItemDAO itemDAO = new ItemDAO(context);
			itemDAO.open();

			for (Event event : events) {
				event.items = new ArrayList<Item>();
				ArrayList<Long> itemIds = eventDAO.getEventItems(event);
				for (Long itemId : itemIds) {
					Item item = itemDAO.getItem(itemId);
					// add the item to the items of the event
					event.items.add(item);
				}
			}
			itemDAO.close();
			eventDAO.close();

			return events;
		}

		@Override
		protected void onPostExecute(ArrayList<Event> result) {
			super.onPostExecute(result);
			events = result;
			adapter.clear();
			adapter.addAll(events);
			adapter.notifyDataSetChanged();
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

}
