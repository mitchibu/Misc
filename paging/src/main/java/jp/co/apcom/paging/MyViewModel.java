package jp.co.apcom.paging;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends AndroidViewModel {
	public MutableLiveData<Boolean> hasLoading = new MutableLiveData<>();
	private EntityA_DAO dao;
	public final LiveData<PagedList<EntityA>> modelList;
//	public final Flowable<PagedList<EntityA>> modelList;

	public MyViewModel(Application app) {
		super(app);
		hasLoading.setValue(false);
		this.dao = ((MyApplication)app).db.entityADao();
		//mConcertDao = concertDao;
		PositionalDataSource d;
		DataSource.Factory<Integer, EntityA> factory = new DataSource.Factory<Integer, EntityA>() {
			@Override
			public DataSource<Integer, EntityA> create() {
				return new PageKeyedDataSource<Integer, EntityA>() {
					@Override
					public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, EntityA> callback) {
						android.util.Log.v("test", "loadInitial");
						test(1, params.requestedLoadSize, new MyCallback() {
							@Override
							public void on(List<EntityA> list, Integer next) {
//								hasLoading.postValue(true);
								EntityA a = new EntityA();
								a.id = 0;
								a.name = "loading";
								//list.add(a);
								callback.onResult(list, null, next);
							}
						});
					}

					@Override
					public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, EntityA> callback) {
						android.util.Log.v("test", "loadBefore");
					}

					@Override
					public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, EntityA> callback) {
						android.util.Log.v("test", "loadAfter");
						PagedList<EntityA> list = modelList.getValue();
						test(params.key, params.requestedLoadSize, new MyCallback() {
							@Override
							public void on(List<EntityA> list, Integer next) {
								if(next > 100) next = null;
//								hasLoading.postValue(next != null);
								EntityA a = new EntityA();
								a.id = 0;
								a.name = "loading";
								//list.add(a);
								callback.onResult(list, next);
							}
						});
					}

					void test(final Integer key, final Integer requestedLoadSize, @NonNull final MyCallback cb) {
						new Thread() {
							public void run() {
								try {
									Thread.sleep(1000);
									test1(key, requestedLoadSize, cb);
								} catch(InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
					void test1(Integer key, Integer requestedLoadSize, @NonNull MyCallback cb) {
						android.util.Log.v("test", String.format("test: %d %d", key, requestedLoadSize));
						List<EntityA> list = new ArrayList<>();
						for(int i = key; i < key + requestedLoadSize; ++ i) {
							EntityA e = new EntityA();
							e.id = i;
							e.name = "sample: " + i;
							list.add(e);
						}
						cb.on(list, key + requestedLoadSize);
					}
				};
			}
		};
		DataSource.Factory<Integer, EntityA> factory2 = new DataSource.Factory<Integer, EntityA>() {
			@Override
			public DataSource<Integer, EntityA> create() {
				return new PositionalDataSource<EntityA>() {
					@Override
					public void loadInitial(@NonNull LoadInitialParams params, @NonNull final LoadInitialCallback<EntityA> callback) {
						android.util.Log.v("test", "loadInitial");
						test(params.requestedStartPosition, params.requestedLoadSize, new MyCallback() {
							@Override
							public void on(List<EntityA> list, Integer next) {
//								hasLoading.postValue(true);
								EntityA a = new EntityA();
								a.id = 0;
								a.name = "loading";
								//list.add(a);
								callback.onResult(list, 0, 100);
							}
						});
					}

					@Override
					public void loadRange(@NonNull LoadRangeParams params, @NonNull final LoadRangeCallback<EntityA> callback) {
						android.util.Log.v("test", "loadRange");
						test(params.startPosition, params.loadSize, new MyCallback() {
							@Override
							public void on(List<EntityA> list, Integer next) {
								if(next > 100) next = null;
//								hasLoading.postValue(next != null);
								EntityA a = new EntityA();
								a.id = 0;
								a.name = "loading";
								//list.add(a);
								callback.onResult(list);
							}
						});
					}

					void test(final Integer key, final Integer requestedLoadSize, @NonNull final MyCallback cb) {
						new Thread() {
							public void run() {
								try {
									Thread.sleep(1000);
									test1(key, requestedLoadSize, cb);
								} catch(InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
					void test1(Integer key, Integer requestedLoadSize, @NonNull MyCallback cb) {
						android.util.Log.v("test", String.format("test: %d %d", key, requestedLoadSize));
						List<EntityA> list = new ArrayList<>();
						for(int i = key; i < key + requestedLoadSize; ++ i) {
							EntityA e = new EntityA();
							e.id = i;
							e.name = "sample: " + i;
							list.add(e);
						}
						cb.on(list, key + requestedLoadSize);
					}
				};
			}
		};
//		RxPagedListBuilder d;
//		modelList = new RxPagedListBuilder<>(
//				dao.get3(), /* page size */ 20).buildFlowable(BackpressureStrategy.LATEST);
//		modelList = new LivePagedListBuilder<>(
//				factory2, /* page size */ 20).build();
		PagedList.Config.Builder builder = new PagedList.Config.Builder();
		builder.setPageSize(20);
		builder.setPrefetchDistance(10);
		builder.setInitialLoadSizeHint(30);
		builder.setEnablePlaceholders(false);
		modelList = new LivePagedListBuilder<>(
				factory, /* page size */ builder.build()).setBoundaryCallback(new PagedList.BoundaryCallback<EntityA>() {
			@Override
			public void onZeroItemsLoaded() {
				android.util.Log.v("test", String.format("onZeroItemsLoaded"));
			}

			@Override
			public void onItemAtFrontLoaded(@NonNull EntityA itemAtFront) {
				// 最初の読み込み
				android.util.Log.v("test", String.format("onItemAtFrontLoaded"));
			}

			@Override
			public void onItemAtEndLoaded(@NonNull EntityA itemAtEnd) {
				// 全データ読み込み完了
				android.util.Log.v("test", String.format("onItemAtEndLoaded"));
			}
		}).build();
	}

	interface MyCallback {
		void on(List<EntityA> list, Integer next);
	}
}
