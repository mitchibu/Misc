package jp.co.apcom.paging;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.DataSource;
import android.arch.paging.ItemKeyedDataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.ColorSpace;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.co.apcom.paging.databinding.ActivityMainBinding;
import jp.co.apcom.paging.databinding.ListItemBinding;
import jp.co.apcom.paging.view.BindableViewHolder;
import jp.co.apcom.paging.view.PagedBindableAdapter;

public class MainActivity extends AppCompatActivity {
	MyViewModel model;
	MyAdapter adapter;
	Disposable disposable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.setLifecycleOwner(this);

		EntityA a = new EntityA();
		a.id = 0;
		a.name = "loading";
		binding.indicator.setItem(a);

		MyDatabase db = ((MyApplication)getApplication()).db;
		List<EntityA> list = new ArrayList<>();
		for(int i = 0; i < 100; ++ i) {
			EntityA e = new EntityA();
			//e.id = i + 1;
			e.name = "test:" + (i + 1);
			list.add(e);
		}
		db.entityADao().insert(list.toArray(new EntityA[0]));

		model = ViewModelProviders.of(this).get(MyViewModel.class);
		binding.setModel(model);

		adapter = new MyAdapter();
		model.modelList.observe(this, new Observer<PagedList<EntityA>>() {
			@Override
			public void onChanged(@Nullable PagedList<EntityA> entityAS) {
				entityAS.addWeakCallback(new ArrayList<EntityA>(), new PagedList.Callback() {
					@Override
					public void onChanged(int position, int count) {
						android.util.Log.v("test", String.format("onChanged"));
					}

					@Override
					public void onInserted(int position, int count) {
						android.util.Log.v("test", String.format("onInserted"));
					}

					@Override
					public void onRemoved(int position, int count) {
						android.util.Log.v("test", String.format("onRemoved"));
					}
				});
				adapter.submitList(entityAS);
			}
		});
		binding.list.setAdapter(adapter);
	}

//	@Override
//	protected void onStart() {
//		super.onStart();
//		disposable = model.modelList.subscribe(new Consumer<PagedList<EntityA>>() {
//			@Override
//			public void accept(PagedList<EntityA> entityAS) throws Exception {
//				android.util.Log.v("test", "size: " + entityAS.size());
//				adapter.submitList(entityAS);
//			}
//		});
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		disposable.dispose();
//	}

	class MyAdapter extends PagedBindableAdapter<EntityA> {
		MyAdapter() {
			super(new DiffUtil.ItemCallback<EntityA>() {
				@Override
				public boolean areItemsTheSame(@NonNull EntityA oldModel, @NonNull EntityA newModel) {
					return oldModel.id == newModel.id;
				}

				@Override
				public boolean areContentsTheSame(@NonNull EntityA oldModel, @NonNull EntityA newModel) {
					return oldModel.equals(newModel);
				}
			});
		}

		@NonNull
		@Override
		public ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType) {
			return ListItemBinding.inflate(getLayoutInflater(), parent, false);
		}

		@Override
		public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
			PagedList<EntityA> list = getCurrentList();
			if(list != null && list.size() <= position) {
				EntityA a = new EntityA();
				a.id = 0;
				a.name = "loading";
				ListItemBinding binding = (ListItemBinding)holder.getBinding();
				binding.setItem(a);
				binding.executePendingBindings();
				return;
			}
			EntityA item = getItem(position);
			if(item == null) {
				//holder.clear();
			} else {
				//holder.bindTo(item);
				ListItemBinding binding = (ListItemBinding)holder.getBinding();
				binding.setItem(item);
				binding.executePendingBindings();
			}
		}

		@Override
		public int getItemCount() {
			int count = super.getItemCount();
			return count == 0 ? 0 : count + 1;
		}
	}
}
