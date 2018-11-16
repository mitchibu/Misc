package jp.co.apcom.cyclicviewpager.fragment;

import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;

import dagger.android.support.DaggerFragment;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("unused")
public abstract class PagedListFragment<T> extends DaggerFragment {
	private final CompositeDisposable disposables = new CompositeDisposable();

	public Flowable<PagedList<T>> pagedList;
	private ListAdapter<T, ? extends RecyclerView.ViewHolder> adapter;

	@NonNull
	protected abstract DataSource.Factory<Integer, T> getDataSourceFactory();

	@NonNull
	protected ListAdapter<T, ? extends RecyclerView.ViewHolder> getListAdapter() {
		return adapter;
	}

	protected void setListAdapter(@NonNull ListAdapter<T, ? extends RecyclerView.ViewHolder> adapter) {
		this.adapter = adapter;
	}

	protected int getPageSize() {
		return 20;
	}

	@SuppressWarnings("RedundantThrows")
	protected void onQuery(PagedList<T> list) throws Exception {
	}

	@SuppressWarnings("RedundantThrows")
	protected void onQueryError(Throwable e) throws Exception {
	}

	@Override
	public void onStart() {
		super.onStart();
		pagedList = new RxPagedListBuilder<>(getDataSourceFactory(), getPageSize()).buildFlowable(BackpressureStrategy.LATEST);
		disposables.add(pagedList
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<PagedList<T>>() {
					@Override
					public void accept(PagedList<T> list) throws Exception {
						onQuery(list);
						adapter.submitList(list);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						onQueryError(throwable);
					}
				}));
	}

	@Override
	public void onStop() {
		super.onStop();
		disposables.dispose();
	}
}
