package jp.co.apcom.sampleandroidx.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntityA {
	@PrimaryKey(autoGenerate = true)
	public long id;

	public String name;
}
