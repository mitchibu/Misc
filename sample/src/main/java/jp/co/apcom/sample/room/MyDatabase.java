package jp.co.apcom.sample.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {EntityA.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
	public abstract EntityA_DAO entityADao();
}
