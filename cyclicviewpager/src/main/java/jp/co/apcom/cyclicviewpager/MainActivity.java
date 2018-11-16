package jp.co.apcom.cyclicviewpager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Objects;

import dagger.android.support.DaggerAppCompatActivity;
import jp.co.apcom.cyclicviewpager.databinding.ActivityMainBinding;
import jp.co.apcom.cyclicviewpager.viewmodel.MainActivityViewModel;
import jp.co.apcom.cyclicviewpager.viewmodel.SavableInstanceStateViewModelFactory;

public class MainActivity extends DaggerAppCompatActivity {
	private static final String TAG_HOME = "home";
	private static final String TAG_SEARCH = "search";
	private static final SparseArray<String> MAP = new SparseArray<>();
	static {
		MAP.put(R.id.home, TAG_HOME);
		MAP.put(R.id.search, TAG_SEARCH);
	}

	private ActivityMainBinding binding;
	private MainActivityViewModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.setLifecycleOwner(this);
		setSupportActionBar(binding.toolBar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolBar, 0, 0);
		binding.drawer.addDrawerListener(toggle);
		toggle.syncState();

		binding.navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				binding.drawer.closeDrawer(Gravity.START, true);
				return false;
			}
		});
		binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				model.tab.setValue(item.getItemId());
				return true;
			}
		});

		model = ViewModelProviders.of(this, new SavableInstanceStateViewModelFactory(this, savedInstanceState)).get(MainActivityViewModel.class);
		model.tab.observe(this, new Observer<Integer>() {
			@Override
			public void onChanged(@Nullable Integer tab) {
				show(MAP.get(Objects.requireNonNull(tab)));
			}
		});

		if(savedInstanceState != null) return;

		model.tab.setValue(R.id.home);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//model.writeTo(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void show(String tag) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		List<Fragment> fragments = fm.getFragments();
		Fragment fragment = tag == null ? null : fm.findFragmentByTag(tag);
		if(tag != null && fragment == null) {
			switch(tag) {
			case TAG_HOME:
				fragment = new HomeFragment();
				break;
			case TAG_SEARCH:
				fragment = new SearchFragment();
				break;
			default:
				throw new RuntimeException();
			}
			ft.add(R.id.container, fragment, tag);
		}
		for(Fragment f : fragments) {
			if(f == fragment) {
				ft.show(f);
			} else {
				ft.hide(f);
			}
		}
		ft.commit();

		if(TAG_HOME.equals(tag)) {
			binding.fab.show();
		} else {
			binding.fab.hide();
		}
	}
}
