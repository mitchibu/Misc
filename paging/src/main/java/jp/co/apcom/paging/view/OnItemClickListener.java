package jp.co.apcom.paging.view;

import android.view.View;

public interface OnItemClickListener<T> {
	void onItemClick(View view, int position, T data);
}
