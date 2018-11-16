package jp.co.apcom.cyclicviewpager;

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

import dagger.android.support.DaggerFragment;
import jp.co.apcom.cyclicviewpager.databinding.FragmentHomeBinding;
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
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		model.page.setValue(binding.pager.getCurrentItem());
		model.writeTo(outState);
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
				f = page == 0 ? new RecyclerViewFragment() : new TestFragment();
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
