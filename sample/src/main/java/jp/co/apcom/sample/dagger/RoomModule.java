package jp.co.apcom.sample.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.apcom.sample.room.MyDatabase;

@Module
public class RoomModule {
	@Provides
	@Singleton
	public MyDatabase provideMyDatabase(Application app) {
		return Room.inMemoryDatabaseBuilder(app, MyDatabase.class)
				//.allowMainThreadQueries()
				.build();
	}
}
