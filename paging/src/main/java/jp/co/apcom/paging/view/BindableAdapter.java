package jp.co.apcom.paging.view;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.ViewGroup;

public abstract class BindableAdapter<T> extends ListAdapter<T, BindableViewHolder> {
	public BindableAdapter(DiffUtil.ItemCallback<T> callback) {
		super(callback);
	}

	@NonNull
	@Override
	public BindableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new BindableViewHolder<>(onCreateDataBinding(parent, viewType));
	}

	public abstract ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType);
}
