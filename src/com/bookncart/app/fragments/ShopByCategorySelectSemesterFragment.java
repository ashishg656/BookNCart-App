package com.bookncart.app.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookncart.app.R;
import com.bookncart.app.activities.ShopByCategoriesActivity;
import com.bookncart.app.adapters.ShopByCategoriesFragmentRecyclerViewAdapter;
import com.bookncart.app.baseobjects.ShopByCategoriesObject;
import com.bookncart.app.baseobjects.ShopByCategoriesSingleCategoryObject;

public class ShopByCategorySelectSemesterFragment extends Fragment {

	public RecyclerView recyclerView;
	public GridLayoutManager layoutManager;
	ShopByCategoriesFragmentRecyclerViewAdapter adapter;
	int position;
	List<ShopByCategoriesSingleCategoryObject> mData;
	String categoryName;

	public static ShopByCategorySelectSemesterFragment newInstance(Bundle b) {
		ShopByCategorySelectSemesterFragment frg = new ShopByCategorySelectSemesterFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.shop_by_categories_select_semester_fragment_layout,
				container, false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.category_recycler_view);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new GridLayoutManager(getActivity(), 2);
		layoutManager.setSpanSizeLookup(new SpanSizeLookup() {

			@Override
			public int getSpanSize(int pos) {
				if (pos == 0)
					return 2;
				else
					return 1;
			}
		});
		recyclerView.setLayoutManager(layoutManager);

		position = getArguments().getInt("position");
		mData = ((ShopByCategoriesActivity) getActivity()).mData
				.getMain_categories().get(position).getSub_categories();

		categoryName = getArguments().getString("categoryname", "BookNCart");

		adapter = new ShopByCategoriesFragmentRecyclerViewAdapter(mData,
				getActivity(), categoryName);
		recyclerView.setAdapter(adapter);

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				((ShopByCategoriesActivity) getActivity()).translateUp(dy);
				super.onScrolled(recyclerView, dx, dy);
			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}
		});
	}
}
