package com.bookncart.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookncart.app.R;
import com.bookncart.app.activities.BookDetailActivity;
import com.bookncart.app.baseobjects.BookDetailObject;

public class BookDescriptionFragment extends BaseFragment {

	LinearLayout toolbarLayout;
	TextView authorName, publisherName, isbn, mrp, price, condition, binding,
			edition, language, numberofPages, bookName, publicationYear,
			description;
	BookDetailObject mData;

	public static BookDescriptionFragment newInstance(Bundle bundle) {
		BookDescriptionFragment frg = new BookDescriptionFragment();
		frg.setArguments(bundle);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.book_detail_description_fragment_layout, container,
				false);

		bookName = (TextView) v.findViewById(R.id.numberoferviews);
		toolbarLayout = (LinearLayout) v
				.findViewById(R.id.toolbarfakereviewfrag);
		authorName = (TextView) v.findViewById(R.id.author);
		publisherName = (TextView) v.findViewById(R.id.Publisher);
		isbn = (TextView) v.findViewById(R.id.isbn);
		mrp = (TextView) v.findViewById(R.id.mrp);
		price = (TextView) v.findViewById(R.id.price);
		condition = (TextView) v.findViewById(R.id.conditiom);
		binding = (TextView) v.findViewById(R.id.Binding);
		edition = (TextView) v.findViewById(R.id.Edition);
		language = (TextView) v.findViewById(R.id.Language);
		numberofPages = (TextView) v.findViewById(R.id.numberofpages);
		publicationYear = (TextView) v.findViewById(R.id.publicationyear);
		description = (TextView) v.findViewById(R.id.description_book_detail);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mData = ((BookDetailActivity) getActivity()).bookDetailObject;

		int color = ((BookDetailActivity) getActivity()).darkColor;
		toolbarLayout.setBackgroundColor(color);

		description.setText(mData.getDescription());
		bookName.setText(mData.getName());
		authorName.setText(mData.getAuthor());
		publisherName.setText(mData.getPublisher());
		isbn.setText(mData.getIsbn());
		mrp.setText("₹ " + mData.getMrp());
		price.setText("₹ " + mData.getPrice());
		condition.setText(mData.isCondition() ? "OLD" : "NEW");
		binding.setText(mData.getBinding());
		edition.setText(mData.getEdition());
		language.setText(mData.getLanguage());
		numberofPages.setText(mData.getNumber_of_pages() + "");
		publicationYear.setText(mData.getPublication_year());
	}
}
