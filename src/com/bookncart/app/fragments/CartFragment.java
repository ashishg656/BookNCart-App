package com.bookncart.app.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookncart.app.R;
import com.bookncart.app.adapters.CartRecyclerAdapter;
import com.bookncart.app.objects.CartObject;

public class CartFragment extends Fragment {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	CartRecyclerAdapter adapter;
	private ArrayList<CartObject> mData;

	public static CartFragment newInstance(Bundle b) {
		CartFragment frg = new CartFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.cart_fragment_layout, container,
				false);
		recyclerView = (RecyclerView) v.findViewById(R.id.cart_recycler_view);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new LinearLayoutManager(getActivity(),
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);

		addData();

		adapter = new CartRecyclerAdapter(mData, getActivity());
		recyclerView.setAdapter(adapter);
	}

	private void addData() {
		mData = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			mData.add(new CartObject(0,
					"hjchjfch fb jjk bfjbjk  fbvkjfv   jkvnfv vk ", 50, false,
					80, "", 5));
		}
	}
}