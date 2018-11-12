package jp.co.apcom.sampleandroidx.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
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
