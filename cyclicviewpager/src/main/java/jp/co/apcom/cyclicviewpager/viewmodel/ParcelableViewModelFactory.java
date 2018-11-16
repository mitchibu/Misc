package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ParcelableViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
	private final Bundle bundle;

	public ParcelableViewModelFactory(Application app, @Nullable Bundle bundle) {
		super(app);
		this.bundle = bundle;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		T model = super.create(modelClass);
		if(bundle != null && model instanceof ParcelableViewModel) ((ParcelableViewModel)model).readFrom(bundle);
		return model;
	}
}
