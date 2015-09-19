package com.bookncart.app.adapters;

import java.util.List;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.baseobjects.BookObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchListAdapter extends BaseAdapter {

	List<BookObject> mData;
	Context context;
	MyClickListener clickListener;

	public SearchListAdapter(List<BookObject> mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
		clickListener = new MyClickListener();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_list_item_layout, parent, false);
			holder = new SearchViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (SearchViewHolder) convertView.getTag();

		holder.bookName.setText(mData.get(position).getName());

		holder.container.setTag(mData.get(position).getId());
		holder.container.setOnClickListener(clickListener);

		return convertView;
	}

	class SearchViewHolder {

		TextView bookName;
		LinearLayout container;

		public SearchViewHolder(View v) {
			bookName = (TextView) v.findViewById(R.id.searchbookname);
			container = (LinearLayout) v.findViewById(R.id.searchitemcontainer);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = (int) v.getTag();
			Intent intent = new Intent(context, BookDetailActivity.class);
			intent.putExtra("bookid", id);
			context.startActivity(intent);
		}
	}
}
