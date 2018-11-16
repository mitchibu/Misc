package jp.co.apcom.cyclicviewpager.view;

import android.view.View;

public interface OnItemClickListener<T> {
	void onItemClick(View view, int position, T data);
}
