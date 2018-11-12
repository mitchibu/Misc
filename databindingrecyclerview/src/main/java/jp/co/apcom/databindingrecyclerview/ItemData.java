package jp.co.apcom.databindingrecyclerview;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ItemData extends Item {
	public ItemData(String name) {
		super(1, name);
	}

	@BindingAdapter("android:src")
	public static void loadImage(ImageView view, Item item) {
		Picasso.with(view.getContext()).load(item.name).placeholder(R.drawable.indicator_circle).into(view);
	}
}
