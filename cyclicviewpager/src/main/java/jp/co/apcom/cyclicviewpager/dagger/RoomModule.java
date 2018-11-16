package jp.co.apcom.cyclicviewpager.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.apcom.cyclicviewpager.room.MyDatabase;

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
