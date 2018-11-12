package jp.co.apcom.databindingrecyclerview;

import android.view.View;

public interface OnItemClickListener<T> {
	void onItemClick(View view, int position, T data);
}
