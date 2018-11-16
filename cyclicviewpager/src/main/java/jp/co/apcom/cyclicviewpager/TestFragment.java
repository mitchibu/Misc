package jp.co.apcom.cyclicviewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import dagger.android.support.DaggerFragment;
import jp.co.apcom.cyclicviewpager.databinding.FragmentTestBinding;

public class TestFragment extends DaggerFragment {
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentTestBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
		binding.setLifecycleOwner(this);
		binding.test.setText(Objects.requireNonNull(getArguments()).getString("test"));
		return binding.getRoot();
	}
}
