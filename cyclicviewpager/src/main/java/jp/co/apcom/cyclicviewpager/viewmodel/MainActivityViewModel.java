package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;

import jp.co.apcom.cyclicviewpager.R;

public class MainActivityViewModel extends ParcelableViewModel {
	public final MutableLiveData<Integer> tab = new MutableLiveData<>();

	public MainActivityViewModel(@NonNull Application application) {
		super(application);
	}

	@Override
	public void writeTo(@NonNull Bundle bundle) {
		Integer p = tab.getValue();
		if(p != null) bundle.putInt("tab", p);
	}

	@Override
	public void readFrom(@NonNull Bundle bundle) {
		tab.setValue(bundle.getInt("tab", R.id.home));
	}
}
