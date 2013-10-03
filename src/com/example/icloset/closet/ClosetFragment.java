package com.example.icloset.closet;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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
	GridViewFragment gridViewFragment;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_closet, null);
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

	/**
	 * @param tag
	 * @param labelId
	 * @param tabContentId
	 * @return The place where you change the icons of the tab
	 */
	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		Log.e(TAG, "buildTab(): tag=" + tag);
		Drawable drawable;
		if (tag.equals(TAB_SHIRT)) {
			drawable = getResources().getDrawable(R.drawable.icon_shirt);
		} else if (tag.equals(TAB_PANTS)) {
			drawable = getResources().getDrawable(R.drawable.icon_pants);
		} else if (tag.equals(TAB_DRESS)) {
			drawable = getResources().getDrawable(R.drawable.icon_dress);
		} else if (tag.equals(TAB_SHOES)) {
			drawable = getResources().getDrawable(R.drawable.icon_shoes);
		} else if (tag.equals(TAB_BAG)) {
			drawable = getResources().getDrawable(R.drawable.icon_bag);
		} else {
			drawable = getResources().getDrawable(R.drawable.icon_accessories);
		}
		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(null, drawable);
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

	/**
	 * @param tabId
	 * @param placeholder
	 * 
	 *            The place where you change the contents of the tab
	 */
	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		if (gridViewFragment != null) {
			// remove the previous existing fragment from the memory
			fm.popBackStack();
		}
		gridViewFragment = new GridViewFragment();
		Bundle args = new Bundle();
		if (tabId.equals(TAB_PANTS)) {
			args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_PANTS);
		} else if (tabId.equals(TAB_SHOES)) {
			args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_SHOES);
		} else if (tabId.equals(TAB_ACCESORIES)) {
			args.putInt(GridViewFragment.TYPE,
					GridViewFragment.TYPE_ACCESSORIES);
		} else if (tabId.equals(TAB_DRESS)) {
			args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_DRESS);
		} else if (tabId.equals(TAB_BAG)) {
			args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_BAGS);
		} else {
			args.putInt(GridViewFragment.TYPE, GridViewFragment.TYPE_SHIRT);
		}
		gridViewFragment.setArguments(args);

		// } else {
		// // if the fragment already exists then just change the contents
		// if (tabId.equals(TAB_PANTS)) {
		// gridViewFragment.type = GridViewFragment.TYPE_PANTS;
		// gridViewFragment.adapter.notifyDataSetChanged();
		//
		// } else if (tabId.equals(TAB_SHOES)) {
		// gridViewFragment.type = GridViewFragment.TYPE_SHOES;
		// gridViewFragment.adapter.notifyDataSetChanged();
		// } else if (tabId.equals(TAB_ACCESORIES)) {
		// gridViewFragment.type = GridViewFragment.TYPE_ACCESSORIES;
		// gridViewFragment.adapter.notifyDataSetChanged();
		// } else if (tabId.equals(TAB_DRESS)) {
		// gridViewFragment.type = GridViewFragment.TYPE_DRESS;
		// gridViewFragment.adapter.notifyDataSetChanged();
		// } else if (tabId.equals(TAB_BAG)) {
		// gridViewFragment.type = GridViewFragment.TYPE_BAGS;
		// gridViewFragment.adapter.notifyDataSetChanged();
		// } else {
		// gridViewFragment.type = GridViewFragment.TYPE_SHIRT;
		// gridViewFragment.adapter.notifyDataSetChanged();
		// }
		//
		// }
		fm.beginTransaction().replace(placeholder, gridViewFragment, tabId)
				.commit();
	}
}