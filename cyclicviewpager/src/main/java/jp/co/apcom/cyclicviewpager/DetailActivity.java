package jp.co.apcom.cyclicviewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import jp.co.apcom.cyclicviewpager.databinding.ActivityDetailBinding;
import jp.co.apcom.cyclicviewpager.room.EntityA;

public class DetailActivity extends DaggerAppCompatActivity {
	public static final String EXTRA_ITEM = DetailActivity.class.getName() + ".extra.ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
		binding.setLifecycleOwner(this);
		binding.setItem((EntityA)getIntent().getParcelableExtra(EXTRA_ITEM));
	}
}
