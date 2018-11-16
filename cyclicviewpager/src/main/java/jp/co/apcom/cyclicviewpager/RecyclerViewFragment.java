package jp.co.apcom.cyclicviewpager;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.co.apcom.cyclicviewpager.databinding.FragmentRecyclerViewBinding;
import jp.co.apcom.cyclicviewpager.databinding.ItemSampleBinding;
import jp.co.apcom.cyclicviewpager.retrofit.ApiInterface;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;
import jp.co.apcom.cyclicviewpager.view.BindableAdapter;
import jp.co.apcom.cyclicviewpager.view.BindableViewHolder;
import jp.co.apcom.cyclicviewpager.viewmodel.ParcelableViewModelFactory;
import jp.co.apcom.cyclicviewpager.viewmodel.RecyclerViewFragmentViewModel;

public class RecyclerViewFragment extends DaggerFragment {
	@Inject
	MyDatabase db;
	@Inject
	ApiInterface api;

	private final Adapter adapter = new Adapter();
	private final CompositeDisposable disposables = new CompositeDisposable();

	private RecyclerViewFragmentViewModel model;
	private FragmentRecyclerViewBinding binding;
	private boolean isLoaded = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = ViewModelProviders.of(this, new ParcelableViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(), savedInstanceState)).get(RecyclerViewFragmentViewModel.class);
		model.init(db);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_view, container, false);
		binding.setLifecycleOwner(this);
		binding.list.setAdapter(adapter);
		binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
			}
		});
		return binding.getRoot();
	}

	@Override
	public void onStart() {
		super.onStart();
		disposables.add(model.modelList
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<PagedList<EntityA>>() {
					@Override
					public void accept(PagedList<EntityA> entityAS) throws Exception {
						adapter.submitList(entityAS);
					}
				}));
	}

	@Override
	public void onStop() {
		super.onStop();
		disposables.dispose();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null) return;
		if(!isLoaded) loadData();
	}

	private void loadData() {
		binding.swipe.setRefreshing(true);
		disposables.add(api.test()
				.flatMapCompletable(new Function<List<EntityA>, CompletableSource>() {
					@Override
					public CompletableSource apply(final List<EntityA> entityAS) throws Exception {
						return Completable.create(new CompletableOnSubscribe() {
							@Override
							public void subscribe(CompletableEmitter emitter) throws Exception {
								db.entityADao().deleteAll();
								db.entityADao().insert(entityAS.toArray(new EntityA[0]));
								emitter.onComplete();
							}
						});
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doFinally(new Action() {
					@Override
					public void run() throws Exception {
						binding.swipe.setRefreshing(false);
					}
				})
				.subscribe(new Action() {
					@Override
					public void run() throws Exception {
						isLoaded = true;
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
					}
				}));
	}

	class Adapter extends BindableAdapter<EntityA> {
		Adapter() {
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

		@Override
		public ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType) {
			return ItemSampleBinding.inflate((LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), parent, false);
		}

		@Override
		public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
			((ItemSampleBinding)holder.getBinding()).setItem(getItem(position));
		}
	}
}
