package sg.changeMakers.iCloset.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icloset.R;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

public class ClosetFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_closet, null);
		
		StickyGridHeadersGridView l = (StickyGridHeadersGridView) view.findViewById(R.id.gridview);
		//StickyGridHeadersBaseAdapter mAdapter = new WeekCalendarAdapter(getActivity(), weekCalendar, lista);
      //  l.setAdapter(mAdapter);
		return view;
	}

}
