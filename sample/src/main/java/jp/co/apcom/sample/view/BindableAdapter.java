package jp.co.apcom.sample.view;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import jp.co.apcom.sample.view.datasource.DataSource;

public abstract class BindableAdapter<T> extends RecyclerView.Adapter<BindableViewHolder> {
	private final DataSource<T> dataSource;

	public BindableAdapter(@NonNull DataSource<T> dataSource) {
		this.dataSource = dataSource;
		dataSource.adapter = this;
	}

	public DataSource<T> getDataSource() {
		return dataSource;
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		dataSource.onAttachedToRecyclerView(recyclerView);
	}

	@Override
	public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
		dataSource.onDetachedFromRecyclerView(recyclerView);
	}

	@NonNull
	@Override
	public BindableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new BindableViewHolder<>(onCreateDataBinding(parent, viewType));
	}

	@Override
	public int getItemCount() {
		return dataSource.getCount();
	}

	@NonNull
	protected abstract ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType);
}
