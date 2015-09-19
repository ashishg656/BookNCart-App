package com.bookncart.app.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.activities.WishlistAndCartActivity;
import com.bookncart.app.application.ZApplication;
import com.bookncart.app.baseobjects.AddToCartObject;
import com.bookncart.app.baseobjects.CartObject;
import com.bookncart.app.baseobjects.CartObject.CartObjectSingleBook;
import com.bookncart.app.extras.AppConstants;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class CartRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants,
		ZRequestTags, UploadManagerCallback {

	CartObject mData;
	Context context;
	int imageHeightGrid;
	MyClickListener myClickListener;
	ProgressDialog progressDialog;

	int editItemQuantity;
	int editItemPosition;
	CartlistItemHolder editItemHolder;

	int removeItemPosition;
	CartObjectSingleBook removeItemCartObject;
	CartlistItemHolder removeItemHolder;

	public CartRecyclerAdapter(CartObject mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
		myClickListener = new MyClickListener();
	}

	@Override
	public int getItemCount() {
		return mData.getBooks().size() == 0 ? 0 : mData.getBooks().size() + 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return BNC_CART_LIST_TYPE_HEADER;
		else if (position == mData.getBooks().size() + 1)
			return BNC_CART_LIST_TYPE_FOOTER;
		else
			return BNC_CART_LIST_TYPE_CART_ITEM;
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {
		pos = commonHolder.getAdapterPosition();
		if (getItemViewType(pos) == BNC_CART_LIST_TYPE_FOOTER) {
			CartFooterViewHolder holder = (CartFooterViewHolder) commonHolder;
			holder.addMoreFromWishlist.setOnClickListener(myClickListener);
			holder.totalQuantity.setText(mData.getTotal_quantity() + "");
			holder.cartTotal.setText("₹ " + mData.getCart_total());
			holder.deliveryCharge.setText("₹ " + mData.getDelivery_charge());
			holder.totalAmount.setText("₹ " + mData.getTotal_amount());
		} else if (getItemViewType(pos) == BNC_CART_LIST_TYPE_CART_ITEM) {
			CartlistItemHolder holder = (CartlistItemHolder) commonHolder;
			CartObjectSingleBook obj = mData.getBooks().get(pos - 1);
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

			holder.editCartItem.setTag(holder);
			holder.editCartItem.setOnClickListener(myClickListener);

			holder.removeCartItem.setTag(holder);
			holder.removeCartItem.setOnClickListener(myClickListener);

			holder.mainContentHolder.setTag(mData.getBooks().get(pos - 1)
					.getId());
			holder.mainContentHolder.setOnClickListener(myClickListener);
		} else if (getItemViewType(pos) == BNC_CART_LIST_TYPE_HEADER) {
			CartHeaderViewHolder holder = (CartHeaderViewHolder) commonHolder;
			holder.itemsCount.setText("ITEMS (" + mData.getTotal_quantity()
					+ ")");
			holder.itemTotal.setText("₹ " + mData.getTotal_amount());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == BNC_CART_LIST_TYPE_CART_ITEM) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_item_layout, parent, false);
			CartlistItemHolder holder = new CartlistItemHolder(view);
			return holder;
		} else if (viewType == BNC_CART_LIST_TYPE_FOOTER) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_footer_item, parent, false);
			CartFooterViewHolder holder = new CartFooterViewHolder(view);
			return holder;
		} else {
			View view = LayoutInflater.from(context).inflate(
					R.layout.cart_list_header_item, parent, false);
			CartHeaderViewHolder holder = new CartHeaderViewHolder(view);
			return holder;
		}
	}

	class CartlistItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName, authorName, bookPrice, quantity, condition, total;
		FrameLayout mainContentHolder;
		FrameLayout removeCartItem, editCartItem;

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
			removeCartItem = (FrameLayout) v.findViewById(R.id.removecart);
			editCartItem = (FrameLayout) v.findViewById(R.id.editcart);
			mainContentHolder = (FrameLayout) v
					.findViewById(R.id.maincontentcart);
		}
	}

	class CartHeaderViewHolder extends RecyclerView.ViewHolder {

		TextView itemsCount;
		TextView itemTotal;

		public CartHeaderViewHolder(View v) {
			super(v);
			itemsCount = (TextView) v.findViewById(R.id.itemscart);
			itemTotal = (TextView) v.findViewById(R.id.carttotalhead);
		}
	}

	class CartFooterViewHolder extends RecyclerView.ViewHolder {

		LinearLayout addMoreFromWishlist;
		TextView totalQuantity, cartTotal, deliveryCharge, totalAmount;

		public CartFooterViewHolder(View v) {
			super(v);
			addMoreFromWishlist = (LinearLayout) v
					.findViewById(R.id.addmorefromwishlist);
			totalQuantity = (TextView) v.findViewById(R.id.totalquantity);
			cartTotal = (TextView) v.findViewById(R.id.carttotal);
			deliveryCharge = (TextView) v.findViewById(R.id.deliverycarge);
			totalAmount = (TextView) v.findViewById(R.id.totalamount);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.addmorefromwishlist:
				((WishlistAndCartActivity) context).viewPager.setCurrentItem(1,
						true);
				break;
			case R.id.editcart:
				editItemHolder = (CartlistItemHolder) v.getTag();
				editItemPosition = editItemHolder.getAdapterPosition();

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Edit Quantity");
				String[] items = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
						"10" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						editItemQuantity = which + 1;
						editCartItemCountRequest((editItemQuantity), mData
								.getBooks().get(editItemPosition - 1).getId());
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
				break;
			case R.id.removecart:
				removeItemHolder = (CartlistItemHolder) v.getTag();
				removeItemPosition = removeItemHolder.getAdapterPosition();
				removeItemCartObject = mData.getBooks().get(removeItemPosition - 1);

				sendRemoveCartItemRequest();
				break;
			case R.id.maincontentcart:
				int bookid = (int) v.getTag();
				Intent intent = new Intent(context, BookDetailActivity.class);
				intent.putExtra("bookid", bookid);
				context.startActivity(intent);
				break;

			default:
				break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	void editCartItemCountRequest(int quantity, int bookid) {
		String url = ZApplication.getInstance().getBaseUrl() + "add_to_cart/";
		List<NameValuePair> nameValuePair = new ArrayList<>();
		nameValuePair.add(new BasicNameValuePair("quantity", Integer
				.toString(quantity)));
		nameValuePair.add(new BasicNameValuePair("book_id", Integer
				.toString(bookid)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_EDIT_CART_ITEM_QUANTITY_TAG,
				BNC_EDIT_CART_ITEM_QUANTITY_TAG,
				BNC_EDIT_CART_ITEM_QUANTITY_TAG, null, nameValuePair, null);
	}

	public void sendRemoveCartItemRequest() {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "remove_from_cart/";
		List<NameValuePair> nameValuePair = new ArrayList<>();
		nameValuePair.add(new BasicNameValuePair("book_id", Integer
				.toString(removeItemCartObject.getId())));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_CART_REMOVE_FROM_CART, BNC_CART_REMOVE_FROM_CART,
				BNC_CART_REMOVE_FROM_CART, null, nameValuePair, null);
	}

	protected void sendUndoDeleteRequestToServer() {
		String url = ZApplication.getInstance().getBaseUrl() + "add_to_cart/";
		List<NameValuePair> nameValuePair = new ArrayList<>();
		nameValuePair.add(new BasicNameValuePair("quantity", Integer
				.toString(removeItemCartObject.getQuantity())));
		nameValuePair.add(new BasicNameValuePair("book_id", Integer
				.toString(removeItemCartObject.getId())));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_CART_READD_CART_ITEM, BNC_CART_READD_CART_ITEM,
				BNC_CART_READD_CART_ITEM, null, nameValuePair, null);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_EDIT_CART_ITEM_QUANTITY_TAG) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (status) {
				AddToCartObject obj = (AddToCartObject) data;
				mData.setCart_total(obj.getCart_total());
				mData.setDelivery_charge(obj.getDelivery_charge());
				mData.setTotal_amount(obj.getTotal_amount());
				mData.setTotal_quantity(obj.getTotal_quantity());
				mData.getBooks().get(editItemPosition - 1)
						.setQuantity(editItemQuantity);
				ZPreferences.setCartCount(context, obj.getCart_count());
				notifyDataSetChanged();
				Snackbar.make(
						((WishlistAndCartActivity) context).coordinatorLayout,
						"Edited item quantity", Snackbar.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						context,
						"Unable to process request.Check internet and try again",
						Toast.LENGTH_SHORT).show();
			}
		} else if (requestType == BNC_CART_REMOVE_FROM_CART) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (status) {
				AddToCartObject obj = (AddToCartObject) data;
				mData.setCart_total(obj.getCart_total());
				mData.setDelivery_charge(obj.getDelivery_charge());
				mData.setTotal_amount(obj.getTotal_amount());
				mData.setTotal_quantity(obj.getTotal_quantity());
				mData.getBooks().remove(removeItemPosition - 1);
				ZPreferences.setCartCount(context, obj.getCart_count());
				notifyDataSetChanged();
				Snackbar.make(
						((WishlistAndCartActivity) context).coordinatorLayout,
						"Removed from cart", Snackbar.LENGTH_SHORT)
						.setAction("UNDO", new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								sendUndoDeleteRequestToServer();
							}
						})
						.setActionTextColor(
								context.getResources().getColor(
										R.color.bnc_yellow_color)).show();
			} else {
				Toast.makeText(
						context,
						"Unable to process request.Check internet and try again",
						Toast.LENGTH_SHORT).show();
			}
		} else if (requestType == BNC_CART_READD_CART_ITEM) {
			if (progressDialog != null)
				progressDialog.dismiss();
			if (status) {
				AddToCartObject obj = (AddToCartObject) data;
				mData.setCart_total(obj.getCart_total());
				mData.setDelivery_charge(obj.getDelivery_charge());
				mData.setTotal_amount(obj.getTotal_amount());
				mData.setTotal_quantity(obj.getTotal_quantity());
				mData.getBooks().add(removeItemPosition - 1,
						removeItemCartObject);
				ZPreferences.setCartCount(context, obj.getCart_count());
				notifyDataSetChanged();
				Snackbar.make(
						((WishlistAndCartActivity) context).coordinatorLayout,
						"Added to cart", Snackbar.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context,
						"Unable to add to cart.Check internet and try again",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_EDIT_CART_ITEM_QUANTITY_TAG) {
			progressDialog = ProgressDialog.show(context, null,
					"Editing item quantity", true, false);
		} else if (requestType == BNC_CART_REMOVE_FROM_CART) {
			progressDialog = ProgressDialog.show(context, null,
					"Removing from cart", true, false);
		} else if (requestType == BNC_CART_READD_CART_ITEM) {
			progressDialog = ProgressDialog.show(context, null,
					"Adding item to cart", true, false);
		}
	}
}