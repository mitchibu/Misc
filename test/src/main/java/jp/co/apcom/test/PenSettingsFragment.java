package jp.co.apcom.test;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import jp.co.apcom.test.databinding.FragmentPenSettingsBinding;

public class PenSettingsFragment extends BottomSheetDialogFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Model model = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(Model.class);
		FragmentPenSettingsBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_pen_settings, container, false);
		binding.setLifecycleOwner(this);
		binding.setModel(model);
		return binding.getRoot();
	}
}
