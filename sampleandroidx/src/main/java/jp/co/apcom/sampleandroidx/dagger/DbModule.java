package jp.co.apcom.sampleandroidx.dagger;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

import jp.co.apcom.sampleandroidx.MyApplication;
import jp.co.apcom.sampleandroidx.db.MyDatabase;

@Module
public class DbModule {
	@Provides
	@Singleton
	public MyDatabase provideMyDatabase(MyApplication app) {
		return Room.inMemoryDatabaseBuilder(app, MyDatabase.class)
				.allowMainThreadQueries()
				.build();
	}
}
