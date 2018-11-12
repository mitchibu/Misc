package jp.co.apcom.sample;

import android.databinding.DataBindingUtil;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import jp.co.apcom.sample.retrofit.ApiInterface;
import jp.co.apcom.sample.room.EntityA;
import jp.co.apcom.sample.room.MyDatabase;
import jp.co.apcom.sample.databinding.ActivitySampleBinding;
import jp.co.apcom.sample.databinding.ListItemBinding;
import jp.co.apcom.sample.rx.RxLifecycle;
import jp.co.apcom.sample.rx.RxLocation;
import jp.co.apcom.sample.view.BindableAdapter;
import jp.co.apcom.sample.view.BindableViewHolder;
import jp.co.apcom.sample.view.OnItemClickListener;
import jp.co.apcom.sample.view.datasource.DaoDataSource;
import jp.co.apcom.sample.view.datasource.DataSource;

public class SampleActivity extends AppCompatActivity implements Injectable {
	@Inject
	MyDatabase db;
	@Inject
	ApiInterface api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
		final ActivitySampleBinding binding =  DataBindingUtil.setContentView(this, R.layout.activity_sample);

//  	ListDataSource<EntityA> dataSource = new ListDataSource<>();
		DaoDataSource<EntityA> dataSource = new DaoDataSource<>(db.entityADao().get2().toObservable());
		dataSource.setOnEmptyListener(binding::setIsEmpty);
		MyAdapter adapter = new MyAdapter(dataSource);
		adapter.setOnItemClickListener((view, position, data) -> android.util.Log.v("test", "click: " + position));
		binding.recycler.setLayoutManager(new LinearLayoutManager(this));
		binding.recycler.setAdapter(adapter);

//		final SampleViewModel model = ViewModelProviders.of(this).get(SampleViewModel.class);
//		model.entries.observe(this, dataSource::update);

		if(savedInstanceState != null) return;

		//noinspection unused
		Disposable d = api.test()
				.delay(1, TimeUnit.SECONDS)
				.flatMap(c -> insert(c.toArray(new EntityA[0])).toSingleDefault(c))
				.takeUntil(RxLifecycle.OnDestroyAsObservable(getLifecycle()).firstOrError())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(disposable -> binding.setIsLoading(true))
				.doFinally(() -> binding.setIsLoading(false))
				.subscribe(v -> {}, Throwable::printStackTrace);

		final RxLocation location = new RxLocation(this);
		//UnusedAssignment
		d = location.getCheckLocationSettings()
				.flatMapObservable(locationSettingsResponse -> location.requestLocation())
				.takeUntil(RxLifecycle.OnDestroyAsObservable(getLifecycle()))
				.subscribe(l -> android.util.Log.v("test", "location: " + l), throwable -> RxLocation.error(throwable, SampleActivity.this));
	}

	private Completable insert(final EntityA... data) {
		return Completable.create(emitter -> {
			db.entityADao().insert(data);
			emitter.onComplete();
		});
	}

	public class MyAdapter extends BindableAdapter<EntityA> {
		OnItemClickListener<EntityA> onItemClickListener = null;

		MyAdapter(DataSource<EntityA> dataSource) {
			super(dataSource);
		}

		void setOnItemClickListener(OnItemClickListener<EntityA> listener) {
			onItemClickListener = listener;
		}

		@NonNull
		@Override
		protected ViewDataBinding onCreateDataBinding(@NonNull ViewGroup parent, int viewType) {
			return ListItemBinding.inflate(getLayoutInflater(), parent, false);
		}

		@Override
		public void onBindViewHolder(@NonNull BindableViewHolder holder, int position) {
			ViewDataBinding binding = holder.getBinding();
			if(binding instanceof ListItemBinding) {
				((ListItemBinding)binding).setPosition(position);
				((ListItemBinding)binding).setItem(getDataSource().getAt(position));
				((ListItemBinding)binding).setListener(onItemClickListener);
			}
			binding.executePendingBindings();
		}
	}
}
