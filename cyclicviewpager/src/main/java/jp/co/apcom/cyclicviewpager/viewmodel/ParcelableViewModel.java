package jp.co.apcom.cyclicviewpager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class ParcelableViewModel extends AndroidViewModel {
	public ParcelableViewModel(@NonNull Application application) {
		super(application);
	}

	public abstract void writeTo(@NonNull Bundle bundle);
	public abstract void readFrom(@NonNull Bundle bundle);
}
