package jp.co.apcom.sample.view.datasource;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class DataSource<T> {
	public RecyclerView.Adapter adapter;

	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
	}

	public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
	}

	public void release() {
	}

	public abstract int getCount();
	public abstract T getAt(int position);
}
