package jp.co.apcom.cyclicviewpager.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.security.InvalidParameterException;

public class CyclicViewPager extends ViewPager {
	private Bitmap bm = null;

	public CyclicViewPager(@NonNull Context context) {
		this(context, null);
	}

	public CyclicViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		super.setOffscreenPageLimit(1);
		addOnPageChangeListener(new OnSimplePageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
				if(state == ViewPager.SCROLL_STATE_IDLE) {
					adjustPage();
				} else {
					setEnableShadow(null);
				}
			}
		});
	}

	private void setEnableShadow(Fragment fragment) {
		if(fragment == null) {
			if(bm == null) return;
			setBackground(null);
			bm.recycle();
			bm = null;
		} else {
			View v = fragment.getView();
			if(v == null) return;
			if(bm != null) bm.recycle();
			bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bm);
			v.draw(c);
			setBackground(new BitmapDrawable(getResources(), bm));
		}
	}

	private void adjustPage() {
		CyclicPagerAdapter adapter = (CyclicPagerAdapter)getAdapter();
		if(adapter == null) return;
		int position = getCurrentItem();
		int newPosition = getCenterPosition() + adapter.getPositionToPage(position);
		if(newPosition == position) return;

		setEnableShadow(adapter.getItem(adapter.getPositionToPage(position)));
		setCurrentItem(position, false);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean b = super.onInterceptTouchEvent(ev);
		if(!b) {
			setEnableShadow(null);
		}
		return b;
	}

	@Override
	protected void onDetachedFromWindow() {
		setEnableShadow(null);
		super.onDetachedFromWindow();
	}

	@Override
	public void setAdapter(@Nullable PagerAdapter adapter) {
		if(!(adapter instanceof CyclicPagerAdapter) || adapter.getCount() < 4) throw new InvalidParameterException();
		super.setAdapter(adapter);
		setCurrentItem(0, false);
	}

	@Override
	public void setCurrentItem(int position, boolean smooth) {
		CyclicPagerAdapter adapter = (CyclicPagerAdapter)getAdapter();
		if(adapter == null) return;
		super.setCurrentItem(getCenterPosition() + adapter.getPositionToPage(position), smooth);
	}

	@Override
	public void setOffscreenPageLimit(int limit) {
		throw new UnsupportedOperationException();
	}

	private int getCenterPosition() {
		CyclicPagerAdapter adapter = (CyclicPagerAdapter)getAdapter();
		if(adapter == null) return 0;
		return (adapter.getCount() / adapter.getPageCount() / 2) * adapter.getPageCount();
	}
}
