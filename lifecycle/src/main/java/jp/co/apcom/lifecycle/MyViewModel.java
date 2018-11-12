package jp.co.apcom.lifecycle;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
	public final MutableLiveData<Integer> test1 = new MutableLiveData<>();
	public final MutableLiveData<Data> test2 = new MutableLiveData<>();
}
