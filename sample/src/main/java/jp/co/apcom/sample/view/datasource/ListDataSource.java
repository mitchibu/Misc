package jp.co.apcom.sample.view.datasource;

import java.util.List;

public class ListDataSource<T> extends DataSource<T> {
	private List<T> entries = null;
	private OnEmptyListener onEmptyListener = null;

	public void update(List<T> entries) {
		this.entries = entries;
		adapter.notifyDataSetChanged();
		onEmptyListener.onEmpty(entries.isEmpty());
	}

	public void setOnEmptyListener(OnEmptyListener listener) {
		onEmptyListener = listener;
	}

	@Override
	public int getCount() {
		return entries == null ? 0 : entries.size();
	}

	@Override
	public T getAt(int position) {
		return entries.get(position);
	}

	public interface OnEmptyListener {
		void onEmpty(boolean isEmpty);
	}
}
