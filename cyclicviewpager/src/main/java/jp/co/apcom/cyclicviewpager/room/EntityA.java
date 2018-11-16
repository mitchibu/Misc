package jp.co.apcom.cyclicviewpager.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.co.apcom.cyclicviewpager.R;

@Entity
public class EntityA implements Parcelable {
	public static final Creator<EntityA> CREATOR = new Creator<EntityA>() {
		@Override
		public EntityA createFromParcel(Parcel in) {
			return new EntityA(in);
		}

		@Override
		public EntityA[] newArray(int size) {
			return new EntityA[size];
		}
	};

	@BindingAdapter("android:src")
	public static void loadImage(ImageView view, EntityA item) {
		Picasso.get().load(item.imageUrl).placeholder(R.drawable.indicator_circle).error(R.mipmap.ic_launcher).fit().centerCrop().into(view);
	}

	@PrimaryKey(autoGenerate = true)
	public long id;

	public String name;
	public String imageUrl;

	public EntityA() {
	}

	protected EntityA(Parcel in) {
		id = in.readLong();
		name = in.readString();
		imageUrl = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(imageUrl);
	}
}
