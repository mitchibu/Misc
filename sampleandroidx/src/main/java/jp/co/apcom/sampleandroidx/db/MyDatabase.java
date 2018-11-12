package jp.co.apcom.sampleandroidx.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
