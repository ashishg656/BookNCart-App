package com.bookncart.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.CartObject;
import com.bookncart.app.baseobjects.CartObject.CartObjectSingleBook;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.serverApi.ImageRequestManager;

public class ConfirmOrderRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants,
		ZRequestTags {

	CartObject mData;
	Context context;
	int imageHeightGrid;

	public ConfirmOrderRecyclerAdapter(CartObject mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return mData.getBooks().size() == 0 ? 0 : mData.getBooks().size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == mData.getBooks().size())
			return BNC_CART_LIST_TYPE_FOOTER;
		else
			return BNC_CART_LIST_TYPE_CART_ITEM;
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {
		pos = commonHolder.getAdapterPosition();
		if (getItemViewType(pos) == BNC_CART_LIST_TYPE_FOOTER) {
			CartFooterViewHolder holder = (CartFooterViewHolder) commonHolder;
			holder.totalQuantity.setText(mData.getTotal_quantity() + "");
			holder.cartTotal.setText("₹ " + mData.getCart_total());
			holder.deliveryCharge.setText("₹ " + mData.getDelivery_charge());
			holder.totalAmount.setText("₹ " + mData.getTotal_amount());
			holder.addMoreFromWishlist.setVisibility(View.GONE);
			holder.confirmOrderButton.setVisibility(View.GONE);
		} else if (getItemViewType(pos) == BNC_CART_LIST_TYPE_CART_ITEM) {
			CartlistItemHolder holder = (CartlistItemHolder) commonHolder;
			CartObjectSingleBook obj = mData.getBooks().get(pos);
			holder.authorName.setText(obj.getAuthor());
			holder.bookName.setText(obj.getName());
			holder.bookPrice.setText("₹ " + obj.getPrice());
			String condition = obj.isCondition() ? "OLD" : "NEW";
			holder.condition.setText(condition);
			holder.quantity.setText(obj.getQuantity() + "");
			holder.total.setText("₹ " + (obj.getQuantity() * obj.getPrice()));
			ImageRequestManager.get(context).requestImage(context,
					holder.bookImage,
					ZApplication.getInstance().getImageUrl(obj.getImage_url()),
					-1);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == BNC_CART_LIST_TYPE_CART_ITEM) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.confirm_order_list_item_layout, parent, false);
			CartlistItemHolder holder = new CartlistItemHolder(view);
			return holder;
		} else {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_footer_item, parent, false);
			CartFooterViewHolder holder = new CartFooterViewHolder(view);
			return holder;
		}
	}

	class CartlistItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName, authorName, bookPrice, quantity, condition, total;
		FrameLayout mainContentHolder;

		public CartlistItemHolder(View v) {
			super(v);
			bookImage = (ImageView) v
					.findViewById(R.id.shop_layout_image_grid_cart);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid_cart);
			authorName = (TextView) v.findViewById(R.id.authornamecart);
			bookPrice = (TextView) v.findViewById(R.id.pricecart);
			quantity = (TextView) v.findViewById(R.id.quantitycart);
			condition = (TextView) v.findViewById(R.id.conditioncart);
			total = (TextView) v.findViewById(R.id.cartitemtotlal);
			mainContentHolder = (FrameLayout) v
					.findViewById(R.id.maincontentcart);
		}
	}

	class CartFooterViewHolder extends RecyclerView.ViewHolder {

		LinearLayout addMoreFromWishlist;
		TextView confirmOrderButton;
		TextView totalQuantity, cartTotal, deliveryCharge, totalAmount;

		public CartFooterViewHolder(View v) {
			super(v);
			confirmOrderButton = (TextView) v
					.findViewById(R.id.placeorderbutton);
			addMoreFromWishlist = (LinearLayout) v
					.findViewById(R.id.addmorefromwishlist);
			totalQuantity = (TextView) v.findViewById(R.id.totalquantity);
			cartTotal = (TextView) v.findViewById(R.id.carttotal);
			deliveryCharge = (TextView) v.findViewById(R.id.deliverycarge);
			totalAmount = (TextView) v.findViewById(R.id.totalamount);
		}
	}
}