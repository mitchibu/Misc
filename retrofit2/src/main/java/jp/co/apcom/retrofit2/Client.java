package jp.co.apcom.retrofit2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.squareup.moshi.Moshi;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Client {
	public static Function<Flowable<Throwable>, Publisher<?>> retryDialog(final Context context) {
		return new Function<Flowable<Throwable>, Publisher<?>>() {
			@Override
			public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {
				return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
					@Override
					public Publisher<?> apply(final Throwable throwable) throws Exception {
						return Flowable.create(new FlowableOnSubscribe<Object>() {
							@Override
							public void subscribe(final FlowableEmitter<Object> e) throws Exception {
								new AlertDialog.Builder(context)
										.setTitle("error")
										.setMessage("retry: " + throwable.getMessage())
										.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												e.onNext(0);
											}
										})
										.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												e.onError(throwable);
											}
										})
										.show();
							}
						}, BackpressureStrategy.LATEST);
					}
				});
			}
		};
	}

	private static <T> Single<T> api(Single<T> observable) {
		return observable.compose(new SingleTransformer<T, T>() {
			@Override
			public SingleSource<T> apply(Single<T> upstream) {
				return upstream
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread());
						//.retry(3)
//						.retry(new BiPredicate<Integer, Throwable>() {
//							@Override
//							public boolean test(Integer integer, Throwable throwable) throws Exception {
//								return integer.compareTo(3) <= 0 && !(throwable instanceof HttpException);
//							}
//						});
			}
		});
	}

	private final ApiInterface api;

	public Client(Context context) {
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS)
				.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
				.addInterceptor(new MockInterceptor(context))
				.build();
		api = new Retrofit.Builder()
				.baseUrl("https://www.yahoo.co.jp")
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder().build()))
				.client(client)
				.build()
				.create(ApiInterface.class);
	}

	public Single<Data> test() {
		return api(api.test());
	}

	protected void onShowRetryDialog(FlowableEmitter<Object> e, Throwable throwable) {
		e.onError(throwable);
	}
}
