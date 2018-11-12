package jp.co.apcom.retrofit2;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface ApiInterface {
	@GET("/api/test")
	@Headers({
			"Content-Type: application/json"
	})
	Single<Data> test();
}
