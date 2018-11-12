package jp.co.apcom.dagger2;


import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import jp.co.apcom.dagger2.test.Pet;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
	@Inject
	Application app;
	@Inject
	LoginActivityUtil util;
	@Inject
	Pet pet;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
//		((MyApplication)getActivity().getApplication()).appComponent.inject(this);
		InjectionUtils.inject(this);
		super.onCreate(savedInstanceState);

		android.util.Log.v("test2", "app: " + app);
		android.util.Log.v("test2", "util: " + util);
		android.util.Log.v("test2", "util.app: " + util.app);
		android.util.Log.v("test2", "pet: " + pet);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

}
