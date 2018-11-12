package jp.co.apcom.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class MainFragment extends DaggerFragment {
	@Inject
	FragmentManager fm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		android.util.Log.v("test", "inject fm: " + fm);
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

}
