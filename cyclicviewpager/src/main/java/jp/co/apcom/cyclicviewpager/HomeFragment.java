package jp.co.apcom.cyclicviewpager;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jp.co.apcom.cyclicviewpager.databinding.FragmentHomeBinding;
import jp.co.apcom.cyclicviewpager.fragment.PageInHomeFragment;
import jp.co.apcom.cyclicviewpager.retrofit.ApiInterface;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;
import jp.co.apcom.cyclicviewpager.rx.RxLifecycle;
import jp.co.apcom.cyclicviewpager.viewmodel.HomeFragmentViewModel;
import jp.co.apcom.cyclicviewpager.viewmodel.SavableInstanceStateViewModelFactory;
import jp.co.apcom.cyclicviewpager.viewpager.CyclicPagerAdapter;
import jp.co.apcom.cyclicviewpager.viewpager.PageTransformer;

public class HomeFragment extends DaggerFragment {
	private static final String[] TITLE = {
			"All",
			"Men's",
			"Lady's",
			"Kid's"
	};

	@Inject
	MyDatabase db;
	@Inject
	ApiInterface api;

	private HomeFragmentViewModel model;
	private FragmentHomeBinding binding;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = ViewModelProviders.of(this, new SavableInstanceStateViewModelFactory(this, savedInstanceState)).get(HomeFragmentViewModel.class);
		model.page.observe(this, new Observer<Integer>() {
			@Override
			public void onChanged(@Nullable Integer page) {
				binding.pager.setCurrentItem(page == null ? 0 : page, false);
			}
		});
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
		binding.setLifecycleOwner(this);
		binding.pager.setPageTransformer(false, new PageTransformer());
		MyPagerAdapter adapter = new MyPagerAdapter();
		binding.pager.setAdapter(adapter);
		binding.tab.setupWithViewPager(binding.pager, false);
		return binding.getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null) return;
		loadData(null, null);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		model.page.setValue(binding.pager.getCurrentItem());
		model.writeTo(outState);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@SuppressLint("CheckResult")
	public void loadData(Runnable doStart, final Runnable doFinally) {
		if(doStart != null) doStart.run();
		api.test()
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
				.takeUntil(RxLifecycle.OnStopAsObservable(getLifecycle()).ignoreElements())
				.doFinally(new Action() {
					@Override
					public void run() throws Exception {
						if(doFinally != null) doFinally.run();
					}
				})
				.subscribe(new Action() {
					@Override
					public void run() throws Exception {
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
					}
				});
	}

	class MyPagerAdapter extends CyclicPagerAdapter {
		SparseArray<Fragment> m = new SparseArray<>();

		MyPagerAdapter() {
			super(getChildFragmentManager());
		}

		@Override
		public Fragment getItem(int position) {
			int page = getPositionToPage(position);
			Bundle args = new Bundle();
			args.putString("test", "" + page);
			Fragment f = m.get(page);
			if(f == null) {
//				f = page == 0 ? new PageInHomeFragment() : new TestFragment();
				f = new PageInHomeFragment();
				m.put(page, f);
				f.setArguments(args);
			}
			return f;
		}

		@Override
		public int getPageCount() {
			return TITLE.length;
		}

		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[getPositionToPage(position)];
		}
	}
}
