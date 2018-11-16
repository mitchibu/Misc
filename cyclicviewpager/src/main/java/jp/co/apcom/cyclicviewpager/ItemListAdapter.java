package jp.co.apcom.cyclicviewpager;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import jp.co.apcom.cyclicviewpager.databinding.ItemSampleBinding;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.view.BindableAdapter;
import jp.co.apcom.cyclicviewpager.view.BindableViewHolder;
import jp.co.apcom.cyclicviewpager.view.OnItemClickListener;

public class ItemListAdapter extends BindableAdapter<EntityA> {
	private OnItemClickListener<EntityA> onItemClickListener = null;

	public ItemListAdapter() {
		super(new DiffUtil.ItemCallback<EntityA>() {
			@Override
			public boolean areItemsTheSame(@NonNull EntityA s, @NonNull EntityA t1) {
				return s.id == t1.id;
			}

			@Override
			public boolean areContentsTheSame(@NonNull EntityA s, @NonNull EntityA t1) {
				return s.equals(t1);
			}
		});
	}

	public ItemListAdapter setOnItemClickListener(OnItemClickListener<EntityA> listener) {
		onItemClickListener = listener;
		return this;
	}

	@Override
	public ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType) {
		return ItemSampleBinding.inflate((LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), parent, false);
	}

	@Override
	public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
		((ItemSampleBinding)holder.getBinding()).setItem(getItem(position));
		((ItemSampleBinding)holder.getBinding()).setPosition(position);
		((ItemSampleBinding)holder.getBinding()).setOnItemClickListrener(onItemClickListener);
	}
}
