package com.bookncart.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.baseobjects.AddressObject;
import com.bookncart.app.baseobjects.AddressObject.SingleAddressObject;

public class AddressListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	AddressObject mData;

	public AddressListAdapter(Context context, AddressObject mData) {
		super();
		this.context = context;
		this.mData = mData;
	}

	@Override
	public int getItemCount() {
		return mData.getAddresses().size();
	}

	@Override
	public void onBindViewHolder(ViewHolder CommholderC, int pos) {
		AddressListHolder holder = (AddressListHolder) CommholderC;
		SingleAddressObject obj = mData.getAddresses().get(pos);
		holder.name.setText(obj.getName());
		holder.mobile.setText(obj.getMobile_number());
		holder.address1.setText(obj.getAddress_line_1() + ", "
				+ obj.getAddress_line_2());
		holder.address2.setText(obj.getCity() + ", " + obj.getState() + " - "
				+ obj.getPincode());

		if (pos == mData.getAddresses().size() - 1) {
			holder.divider.setVisibility(View.GONE);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.select_address_list_item_layout, parent, false);
		AddressListHolder holder = new AddressListHolder(v);
		return holder;
	}

	class AddressListHolder extends RecyclerView.ViewHolder {

		TextView name, address1, address2, mobile;
		View divider;

		public AddressListHolder(View v) {
			super(v);
			name = (TextView) v.findViewById(R.id.name);
			address1 = (TextView) v.findViewById(R.id.address1);
			address2 = (TextView) v.findViewById(R.id.address2);
			mobile = (TextView) v.findViewById(R.id.mobile);
			divider = (View) v.findViewById(R.id.dividerview);
		}
	}
}
