package jp.co.apcom.cyclicviewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ViewPager pager = findViewById(R.id.pager);
		MyPagerAdapter2 adapter = new MyPagerAdapter2(getSupportFragmentManager());
		pager.setAdapter(adapter);
//		pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			@Override
//			public void onPageScrolled(int i, float v, int i1) {
//			}
//
//			@Override
//			public void onPageSelected(int i) {
//				if(i == 0) pager.setCurrentItem(3, false);
//				else if(i == 4) pager.setCurrentItem(1, false);
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int i) {
//			}
//		});
		pager.setOffscreenPageLimit(1);
		pager.setCurrentItem(50, false);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {
		SparseArray<Fragment> m = new SparseArray<>();
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

//		@Override
//		public long getItemId(int position) {
//			return super.getItemId(position % getRealCount());
//		}


//		@NonNull
//		@Override
//		public Object instantiateItem(@NonNull ViewGroup container, int position) {
//			Fragment f = (Fragment)super.instantiateItem(container, position % getRealCount());
//			return f;
//		}
//
		@Override
		public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
			super.destroyItem(container, position % getRealCount(), object);
			getSupportFragmentManager().beginTransaction().remove((Fragment)object).commit();
		}


		@Override
		public int getItemPosition(@NonNull Object object) {
			return FragmentStatePagerAdapter.POSITION_NONE;
		}

		@Override
		public Fragment getItem(int i) {
			i = i % getRealCount();
			Bundle args = new Bundle();
//			if(i == 0 || i == 4) {
//				args.putString("test", "term: " + i);
//			} else {
//				args.putString("test", "" + i);
//			}
			args.putString("test", "" + i);
//			Fragment f = new TestFragment();
			Fragment f = m.get(i);
			if(f == null) {
				f = new TestFragment();
				m.put(i, f);
				f.setArguments(args);
			}
			return f;
		}

		@Override
		public int getCount() {
			return 100;
		}

		public int getRealCount() {
			return 4;
		}
	}

	class MyPagerAdapter2 extends PagerAdapter {
		SparseArray<Fragment> m = new SparseArray<>();
		FragmentManager fm;
		FragmentTransaction ft = null;
		MyPagerAdapter2(FragmentManager fm) {
			this.fm = fm;
		}
		public Fragment getItem(int i) {
			i = i % getRealCount();
			Bundle args = new Bundle();
//			if(i == 0 || i == 4) {
//				args.putString("test", "term: " + i);
//			} else {
//				args.putString("test", "" + i);
//			}
			args.putString("test", "" + i);
//			Fragment f = new TestFragment();
			Fragment f = m.get(i);
			if(f == null) {
				f = new TestFragment();
				m.put(i, f);
				f.setArguments(args);
			}
			return f;
		}
		public int getRealCount() {
			return 3;
		}
		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
			return ((Fragment)o).getView() == view;
		}

		@Override
		public void startUpdate(@NonNull ViewGroup container) {
			super.startUpdate(container);
		}

		@NonNull
		@Override
		public Object instantiateItem(@NonNull ViewGroup container, int position) {
			android.util.Log.v("test", "instantiateItem: " + (position % getRealCount()));
			position = position % getRealCount();
			if(ft == null) ft = fm.beginTransaction();
			String tag = getTag(container, position);
			Fragment f = fm.findFragmentByTag(tag);
			if(f == null) {
				f = getItem(position);
				ft.add(container.getId(), f, tag);
			} else {
//				ft.detach(f);
//				ft.commitNow();
//				ft = fm.beginTransaction();
				ft.attach(f);
			}
//			ft.add(container.getId(), f, tag);
			return f;
		}

		@Override
		public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
			android.util.Log.v("test", "destroyItem: " + (position % getRealCount()));
			if(ft == null) ft = fm.beginTransaction();
			ft.detach((Fragment)object);
		}

		@Override
		public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public void finishUpdate(@NonNull ViewGroup container) {
			if(ft != null) {
				ft.commit();
				ft = null;
			}
		}

		String getTag(ViewGroup container, int position) {
			return String.format("test%d_%d", container.getId(), position);
		}
	}
}
