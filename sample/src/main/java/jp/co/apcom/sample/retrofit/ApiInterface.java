package jp.co.apcom.sample.retrofit;

import java.util.List;

import io.reactivex.Single;
import jp.co.apcom.sample.room.EntityA;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiInterface {
	@GET("/api/test")
	@Headers({
			"Content-Type: application/json"
	})
	Single<List<EntityA>> test();
}
