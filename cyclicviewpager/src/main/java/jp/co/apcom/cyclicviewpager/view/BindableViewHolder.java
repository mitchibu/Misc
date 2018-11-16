package jp.co.apcom.cyclicviewpager.view;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindableViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
	private final T binding;

	public BindableViewHolder(T binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	public T getBinding() {
		return binding;
	}
}
