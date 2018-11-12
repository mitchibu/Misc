package jp.co.apcom.sample.room;

import android.arch.persistence.room.Dao;
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

	@Insert
	void insert(EntityA... entities);
}
