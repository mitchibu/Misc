package jp.co.apcom.cyclicviewpager.fragment;

import android.arch.paging.DataSource;
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

import java.util.Objects;

import javax.inject.Inject;

import jp.co.apcom.cyclicviewpager.DetailActivity;
import jp.co.apcom.cyclicviewpager.HomeFragment;
import jp.co.apcom.cyclicviewpager.ItemListAdapter;
import jp.co.apcom.cyclicviewpager.R;
import jp.co.apcom.cyclicviewpager.databinding.FragmentRecyclerViewBinding;
import jp.co.apcom.cyclicviewpager.room.EntityA;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;
import jp.co.apcom.cyclicviewpager.view.OnItemClickListener;

public class PageInHomeFragment extends PagedListFragment<EntityA> {
	@Inject
	MyDatabase db;

	private FragmentRecyclerViewBinding binding;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ItemListAdapter());
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_view, container, false);
		binding.setLifecycleOwner(this);
		binding.list.setAdapter(((ItemListAdapter)getListAdapter()).setOnItemClickListener(new OnItemClickListener<EntityA>() {
			@Override
			public void onItemClick(View view, int position, EntityA data) {
				showDetail(data, view.findViewById(R.id.image));
			}
		}));
		binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				((HomeFragment)Objects.requireNonNull(getParentFragment())).loadData(new Runnable() {
					@Override
					public void run() {
						binding.swipe.setRefreshing(true);
					}
				}, new Runnable() {
					@Override
					public void run() {
						binding.swipe.setRefreshing(false);
					}
				});
			}
		});
		return binding.getRoot();
	}

	@NonNull
	@Override
	protected DataSource.Factory<Integer, EntityA> getDataSourceFactory() {
		return db.entityADao().get3();
	}

	@Override
	protected void onQuery(PagedList<EntityA> list) throws Exception {
		binding.setIsEmpty(list.isEmpty());
	}

	@Override
	protected void onQueryError(Throwable e) throws Exception {
		e.printStackTrace();
	}

	private void showDetail(EntityA data, View share) {
		Intent intent = new Intent(getContext(), DetailActivity.class);
		intent.putExtra(DetailActivity.EXTRA_ITEM, data);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), share, "share");
		ActivityCompat.startActivity(Objects.requireNonNull(getContext()), intent, options.toBundle());
	}
}
