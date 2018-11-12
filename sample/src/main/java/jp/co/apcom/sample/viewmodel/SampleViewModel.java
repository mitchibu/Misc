package jp.co.apcom.sample.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import jp.co.apcom.sample.room.EntityA;

public class SampleViewModel extends ViewModel {
	public final MutableLiveData<List<EntityA>> entries = new MutableLiveData<>();
}
