package jp.co.apcom.room;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class
MainActivity extends AppCompatActivity {
	CompositeDisposable disposables = new CompositeDisposable();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MyDatabase db = MyDatabase.getInstance(this);

		EntityA entity = new EntityA();
		entity.name = "sima";
		db.entityADao().insert(entity);
//		List<EntityA> entityAList = db.entityADao().get();
//		for(EntityA e : entityAList) {
//			android.util.Log.v("test", "name: " + e.name);
//		}
		disposables.add(db.entityADao().get2()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<List<EntityA>>() {
					@Override public void accept(List<EntityA> entityAS) throws Exception {
						for(EntityA e : entityAS) {
							android.util.Log.v("test", "name: " + e.name);
						}
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
