package jp.co.apcom.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {EntityA.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
	private static volatile MyDatabase db;

	public static synchronized MyDatabase getInstance(Context context) {
		if(db == null) {
			db = Room.inMemoryDatabaseBuilder(context, MyDatabase.class)
					.allowMainThreadQueries()
					.build();
		}
		return db;
	}

	public abstract EntityA_DAO entityADao();
}
