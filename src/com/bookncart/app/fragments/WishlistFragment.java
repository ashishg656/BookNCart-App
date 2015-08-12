package com.bookncart.app.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookncart.app.R;
import com.bookncart.app.adapters.WishlistRecyclerAdapter;
import com.bookncart.app.objects.WishlistObject;

public class WishlistFragment extends Fragment {

	RecyclerView recyclerView;
	GridLayoutManager layoutManager;
	WishlistRecyclerAdapter adapter;
	ArrayList<WishlistObject> mData;

	public static WishlistFragment newInstance(Bundle b) {
		WishlistFragment frg = new WishlistFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wishlist_fragment_layout, container,
				false);
		recyclerView = (RecyclerView) v
				.findViewById(R.id.wishlist_recycler_view);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new GridLayoutManager(getActivity(), 2);
		recyclerView.setLayoutManager(layoutManager);

		addData();

		adapter = new WishlistRecyclerAdapter(mData, getActivity());
		recyclerView.setAdapter(adapter);
	}

	private void addData() {
		mData = new ArrayList<>();
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
		mData.add(new WishlistObject(0,
				"cbjhbckjbk hbkjcvkklnkl b jnkdvnk bjbj ", 50, false, 50, ""));
	}
}
