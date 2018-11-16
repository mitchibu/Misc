package jp.co.apcom.cyclicviewpager.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public abstract class CyclicPagerAdapter extends FragmentPagerAdapter {
	public CyclicPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public abstract int getPageCount();

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		return super.instantiateItem(container, getPositionToPage(position));
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		super.destroyItem(container, getPositionToPage(position), object);
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public int getCount() {
		return getPageCount() * 10;
	}

	protected int getPositionToPage(int position) {
		return position % getPageCount();
	}
}
