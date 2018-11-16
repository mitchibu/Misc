package jp.co.apcom.cyclicviewpager.room;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface EntityA_DAO {
	@Query("select * from entityA")
	List<EntityA> get();

	@Query("select * from entityA")
	Flowable<List<EntityA>> get2();

	@Query("select * from entityA")
	DataSource.Factory<Integer, EntityA> get3();

	@Insert
	void insert(EntityA... entities);

	@Query("delete from entityA")
	void deleteAll();
}
