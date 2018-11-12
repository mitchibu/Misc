package jp.co.apcom.retrofit2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
	private final CompositeDisposable disposables = new CompositeDisposable();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		Client client = new Client(this);
		disposables.add(MyApplication.client().test()
				.retryWhen(Client.retryDialog(this))
				.subscribe(new Consumer<Data>() {
					@Override
					public void accept(Data data) throws Exception {
						android.util.Log.v("test", data.toString());

					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
					}
				})
		);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		disposables.dispose();
	}
}
