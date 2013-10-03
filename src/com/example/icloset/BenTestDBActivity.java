package com.example.icloset;

import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.icloset.database.CategoriesDAO;
import com.example.icloset.model.Category;

public class BenTestDBActivity extends ListActivity {
	private CategoriesDAO dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ben_test_db);

		dao = new CategoriesDAO(this);
		dao.open();

		List<Category> values = dao.getAllCategorys();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Category> adapter = (ArrayAdapter<Category>) getListAdapter();
		Category cat = null;
		switch (view.getId()) {
		case R.id.add:
			String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
			int nextInt = new Random().nextInt(3);
			// Save the new comment to the database
			cat = dao.createCategory(comments[nextInt]);
			adapter.add(cat);
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				cat = (Category) getListAdapter().getItem(0);
				dao.deleteCategory(cat);
				adapter.remove(cat);
			}
			break;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		dao.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dao.close();
		super.onPause();
	}

}