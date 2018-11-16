package jp.co.apcom.cyclicviewpager.view;

import android.arch.paging.PagedListAdapter;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

public abstract class PagedBindableAdapter<T> extends PagedListAdapter<T, BindableViewHolder> {
	public PagedBindableAdapter(DiffUtil.ItemCallback<T> callback) {
		super(callback);
	}

	@NonNull
	@Override
	public BindableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new BindableViewHolder<>(onCreateDataBinding(parent, viewType));
	}

	public abstract ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType);
}
