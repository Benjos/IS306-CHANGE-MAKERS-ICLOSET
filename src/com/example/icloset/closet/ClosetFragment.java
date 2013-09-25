package com.example.icloset.closet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.icloset.GridViewFragment;
import com.example.icloset.R;

public class ClosetFragment extends Fragment implements OnTabChangeListener {

	private static final String TAG = "FragmentTabs";
	public static final String TAB_SHIRT = "shirt";
	public static final String TAB_PANTS = "pants";
	public static final String TAB_DRESS = "dress";
	public static final String TAB_SHOES = "shoes";
	public static final String TAB_BAG = "bag";
	public static final String TAB_ACCESORIES = "accesories";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.tabs_fragment, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrentTab);
		// manually start loading stuff in the first tab
		updateTab(TAB_SHIRT, R.id.tab_1);
	}

	private void setupTabs() {
		mTabHost.setup(); // you must call this before adding your tabs!
		mTabHost.addTab(newTab(TAB_SHIRT, R.string.add, R.id.tab_1));
		mTabHost.addTab(newTab(TAB_PANTS, R.string.contentDescription,
				R.id.tab_2));
		mTabHost.addTab(newTab(TAB_DRESS, R.string.contentDescription,
				R.id.tab_3));
		mTabHost.addTab(newTab(TAB_SHOES, R.string.contentDescription,
				R.id.tab_4));
		mTabHost.addTab(newTab(TAB_BAG, R.string.contentDescription, R.id.tab_5));
		mTabHost.addTab(newTab(TAB_ACCESORIES, R.string.contentDescription,
				R.id.tab_6));

	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		Log.e(TAG, "buildTab(): tag=" + tag);

		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab,
				(ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
		//((TextView) indicator.findViewById(R.id.text)).setText(labelId);
		

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		if (TAB_SHIRT.equals(tabId)) {
			updateTab(tabId, R.id.tab_1);
			mCurrentTab = 0;
			return;
		}
		if (TAB_PANTS.equals(tabId)) {
			updateTab(tabId, R.id.tab_2);
			mCurrentTab = 1;
			return;
		}

		if (TAB_DRESS.equals(tabId)) {
			updateTab(tabId, R.id.tab_3);
			mCurrentTab = 2;
			return;
		}
		if (TAB_SHOES.equals(tabId)) {
			updateTab(tabId, R.id.tab_4);
			mCurrentTab = 3;
			return;
		}
		if (TAB_BAG.equals(tabId)) {
			updateTab(tabId, R.id.tab_5);
			mCurrentTab = 4;
			return;
		}
		if (TAB_ACCESORIES.equals(tabId)) {
			updateTab(tabId, R.id.tab_6);
			mCurrentTab = 5;
			return;
		}

	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(tabId) == null) {
			GridViewFragment fragment = new GridViewFragment();
			Bundle args = new Bundle();
			if (tabId.equals(TAB_PANTS)) {
				args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_PANTS);
			} else {
				args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_SHIRT);
			}

			fragment.setArguments(args);

			fm.beginTransaction().replace(placeholder, fragment, tabId)
					.commit();
		}
	}

}