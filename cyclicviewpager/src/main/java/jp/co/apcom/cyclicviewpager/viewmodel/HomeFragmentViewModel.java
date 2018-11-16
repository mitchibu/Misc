package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class HomeFragmentViewModel extends ParcelableViewModel {
	public final MutableLiveData<Integer> page = new MutableLiveData<>();

	public HomeFragmentViewModel(@NonNull Application application) {
		super(application);
	}

	@Override
	public void writeTo(@NonNull Bundle bundle) {
		Integer p = page.getValue();
		if(p != null) bundle.putInt("page", p);
	}

	@Override
	public void readFrom(@NonNull Bundle bundle) {
		page.setValue(bundle.getInt("page", 0));
	}
}
