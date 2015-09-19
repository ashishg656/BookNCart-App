package com.bookncart.app.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.bookncart.app.baseobjects.AddToFavouritesObject;
import com.bookncart.app.baseobjects.BookObject;
import com.bookncart.app.extras.ZRequestTags;
import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.serverApi.ImageRequestManager;
import com.bookncart.app.serverApi.UploadManager;
import com.bookncart.app.serverApi.UploadManagerCallback;

public class WishlistRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements ZRequestTags,
		UploadManagerCallback {

	MyOnClickListener onClickListener;
	public List<BookObject> mData;
	Context context;

	int positionToRemove = -1;
	WishlistItemHolder holderToRemove;
	BookObject bookObjectToRemove;

	boolean isFavouriteRequestRunning = false;

	public WishlistRecyclerAdapter(List<BookObject> obj, Context context) {
		super();
		this.mData = obj;
		this.context = context;
		onClickListener = new MyOnClickListener();
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder commonHolder, int pos) {
		pos = commonHolder.getAdapterPosition();
		WishlistItemHolder holder = (WishlistItemHolder) commonHolder;

		holder.bookName.setText(mData.get(pos).getName());
		holder.bookPrice.setText("₹ " + mData.get(pos).getPrice());
		holder.bookAuthor.setText(mData.get(pos).getAuthor());

		holder.containerLayout.setTag(R.integer.bnc_shop_tag_bookname, mData
				.get(pos).getName());
		holder.containerLayout.setTag(R.integer.bnc_shop_tag_bookid,
				mData.get(pos).getId());
		holder.containerLayout.setTag(R.integer.bnc_shop_tag_book_isfavourite,
				mData.get(pos).isIs_favourite());

		holder.removeButton.setTag(R.integer.bnc_wishlist_holder, holder);
		holder.removeButton.setOnClickListener(onClickListener);
		holder.removeButton.setEnabled(true);

		holder.containerLayout.setAlpha(1f);

		holder.containerLayout.setOnClickListener(onClickListener);

		ImageRequestManager.get(context).requestImage(
				context,
				holder.bookImage,
				ZApplication.getInstance().getImageUrl(
						mData.get(pos).getImage_url()), -1);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.wishlist_recycler_view_list_item_layout_listlike,
				parent, false);
		WishlistItemHolder holder = new WishlistItemHolder(view);
		return holder;
	}

	class WishlistItemHolder extends RecyclerView.ViewHolder {

		ImageView bookImage;
		TextView bookName;
		TextView bookPrice, bookAuthor;
		FrameLayout containerLayout;
		LinearLayout removeButton;

		public WishlistItemHolder(View v) {
			super(v);
			bookImage = (ImageView) v.findViewById(R.id.shop_layout_image_grid);
			bookName = (TextView) v.findViewById(R.id.book_name_shop_grid);
			bookPrice = (TextView) v.findViewById(R.id.book_price_shop_grid);
			bookAuthor = (TextView) v.findViewById(R.id.wishlistbookauthor);
			containerLayout = (FrameLayout) v
					.findViewById(R.id.shopactivityitemviewholdercontainer);
			removeButton = (LinearLayout) v
					.findViewById(R.id.ic_remove_from_wishlist_wishlist);
		}
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shopactivityitemviewholdercontainer:
				int bookid = (int) v.getTag(R.integer.bnc_shop_tag_bookid);
				Intent intent = new Intent(context, BookDetailActivity.class);
				intent.putExtra("bookid", bookid);
				context.startActivity(intent);
				break;
			case R.id.ic_remove_from_wishlist_wishlist:
				if (!isFavouriteRequestRunning) {
					WishlistItemHolder holder = (WishlistItemHolder) v
							.getTag(R.integer.bnc_wishlist_holder);
					positionToRemove = holder.getAdapterPosition();
					holderToRemove = holder;
					bookObjectToRemove = mData.get(positionToRemove);
					removeWishlistItem(mData.get(positionToRemove).getId());
				}
				break;
			}
		}
	}

	private void removeWishlistItem(int bookid) {
		String url = ZApplication.getInstance().getBaseUrl()
				+ "add_to_favourite/";
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("book_id", Integer
				.toString(bookid)));
		UploadManager.getInstance().makeAyncRequest(url,
				BNC_WISHLIST_REMOVE_FROM_WISHLIST,
				BNC_WISHLIST_REMOVE_FROM_WISHLIST,
				BNC_WISHLIST_REMOVE_FROM_WISHLIST, null, nameValuePairs, null);
	}

	@Override
	public void uploadFinished(int requestType, int objectId, Object data,
			int uploadId, boolean status, int parserId) {
		if (requestType == BNC_WISHLIST_REMOVE_FROM_WISHLIST) {
			isFavouriteRequestRunning = false;
			if (status) {
				AddToFavouritesObject obj = (AddToFavouritesObject) data;
				if (obj.isError())
					makeToast("Server error");
				else {
					if (obj.isRemovedFromFavourites()) {
						Snackbar.make(
								((WishlistAndCartActivity) context).coordinatorLayout,
								"Removed book from wishlist",
								Snackbar.LENGTH_SHORT)
								.setAction("UNDO", new OnClickListener() {

									@Override
									public void onClick(View v) {
										removeWishlistItem(bookObjectToRemove
												.getId());
									}
								})
								.setActionTextColor(
										context.getResources().getColor(
												R.color.bnc_yellow_color))
								.show();
						mData.remove(positionToRemove);
						notifyItemRemoved(positionToRemove);
					} else {
						makeToast("Successfully added book to wishlist.");
						mData.add(positionToRemove, bookObjectToRemove);
						notifyItemInserted(positionToRemove);
					}
					ZPreferences.setWishlistCount(context,
							obj.getWishlist_count());
				}
			} else {
				makeToast("Unable to remove from wishlist. Please check your network connection.");
				holderToRemove.containerLayout.setAlpha(1f);
			}
		}
	}

	@Override
	public void uploadStarted(int requestType, int objectId, int parserId,
			Object object) {
		if (requestType == BNC_WISHLIST_REMOVE_FROM_WISHLIST) {
			isFavouriteRequestRunning = true;
			holderToRemove.removeButton.setEnabled(false);
			holderToRemove.containerLayout.setAlpha(0.5f);
		}
	}

	void makeToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}