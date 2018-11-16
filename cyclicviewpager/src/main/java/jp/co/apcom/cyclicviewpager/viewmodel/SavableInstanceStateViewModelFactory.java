package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Objects;

public class SavableInstanceStateViewModelFactory implements ViewModelProvider.Factory {
	private final Bundle bundle;
	private final ViewModelProvider.Factory factory;

	public SavableInstanceStateViewModelFactory(@Nullable Bundle bundle) {
		this(bundle, new ViewModelProvider.NewInstanceFactory());
	}

	public SavableInstanceStateViewModelFactory(@NonNull Activity activity, @Nullable Bundle bundle) {
		this(bundle, new ViewModelProvider.AndroidViewModelFactory(activity.getApplication()));
	}

	public SavableInstanceStateViewModelFactory(@NonNull Fragment fragment, @Nullable Bundle bundle) {
		this(Objects.requireNonNull(fragment.getActivity()), bundle);
	}

	public SavableInstanceStateViewModelFactory(@NonNull Application app, @Nullable Bundle bundle) {
		this(bundle, new ViewModelProvider.AndroidViewModelFactory(app));
	}

	public SavableInstanceStateViewModelFactory(@Nullable Bundle bundle, @NonNull ViewModelProvider.Factory factory) {
		this.bundle = bundle;
		this.factory = factory;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		T model = factory.create(modelClass);
		if(bundle != null && model instanceof SavableInstanceState) ((SavableInstanceState)model).readFrom(bundle);
		return model;
	}
}
