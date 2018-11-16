package jp.co.apcom.cyclicviewpager.dagger;

import android.app.Application;

import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.apcom.cyclicviewpager.retrofit.ApiInterface;
import jp.co.apcom.cyclicviewpager.retrofit.MockInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class RetrofitModule {
	@Provides
	@Singleton
	public OkHttpClient provideOkHttpClient(Application app) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS)
				.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
		builder.addInterceptor(new MockInterceptor(app));
		return builder.build();
	}

	@Provides
	@Singleton
	public ApiInterface provideApiInterface(OkHttpClient client) {
		return new Retrofit.Builder()
				.baseUrl("http://localhost")
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder().build()))
				.client(client)
				.build()
				.create(ApiInterface.class);
	}
}
