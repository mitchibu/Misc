package jp.co.apcom.sample.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class EntityA {
	@PrimaryKey(autoGenerate = true)
	public long id;

	public String name;
}
