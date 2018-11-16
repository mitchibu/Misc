package jp.co.apcom.cyclicviewpager;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import jp.co.apcom.cyclicviewpager.retrofit.ApiInterface;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;
import jp.co.apcom.cyclicviewpager.view.OnItemClickListener;
import jp.co.apcom.cyclicviewpager.viewmodel.SavableInstanceStateViewModelFactory;
import jp.co.apcom.cyclicviewpager.viewmodel.RecyclerViewFragmentViewModel;

public class RecyclerViewFragment extends DaggerFragment {
	@Inject
	MyDatabase db;
	@Inject
	ApiInterface api;

	private final ItemListAdapter adapter = new ItemListAdapter();
	private final CompositeDisposable disposables = new CompositeDisposable();

	private RecyclerViewFragmentViewModel model;
	private FragmentRecyclerViewBinding binding;
	private boolean isLoaded = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = ViewModelProviders.of(this, new SavableInstanceStateViewModelFactory(this, savedInstanceState)).get(RecyclerViewFragmentViewModel.class);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_view, container, false);
		binding.setLifecycleOwner(this);
		binding.list.setAdapter(adapter.setOnItemClickListener(new OnItemClickListener<EntityA>() {
			@Override
			public void onItemClick(View view, int position, EntityA data) {
				showDetail(data, view.findViewById(R.id.image));
			}
		}));
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
		disposables.add(model.get(db.entityADao())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<PagedList<EntityA>>() {
					@Override
					public void accept(PagedList<EntityA> entityAS) throws Exception {
						binding.setIsEmpty(entityAS.isEmpty());
						adapter.submitList(entityAS);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
					}
				}));
	}

	@Override
	public void onStop() {
		super.onStop();
		disposables.dispose();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!isLoaded()) loadData();
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		model.writeTo(outState);
	}

	private boolean isLoaded() {
		return isLoaded;
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
								if(!entityAS.isEmpty()) {
									db.entityADao().insert(entityAS.toArray(new EntityA[0]));
								}
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

	private void showDetail(EntityA data, View share) {
		Intent intent = new Intent(getContext(), DetailActivity.class);
		intent.putExtra(DetailActivity.EXTRA_ITEM, data);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), share, "share");
		ActivityCompat.startActivity(Objects.requireNonNull(getContext()), intent, options.toBundle());
	}
}
